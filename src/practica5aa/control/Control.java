/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.control;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Math.ceil;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica5aa.Notifica;
import practica5aa.Practica5AA;
import practica5aa.model.Model;
import practica5aa.model.Paraula;
/**
 *
 * @author becari2
 */
public class Control extends Thread implements Notifica {
    private enum Mode {
        LLEGIR,
        CORREGIR,
        SEGUENT
    }
    private Practica5AA prog;
    private Mode mode = Mode.LLEGIR;

    public Control(Practica5AA p) {
        prog = p;
    }

    public void run() {
       switch(this.mode){
           case LLEGIR:
               llegirFitxer(prog.getModel().getFitxer());
               prog.notificar(Missatge.DIBUIXA, 0);
               break;
           case CORREGIR:
               if (prog.getModel().isDicEmpty()){
                   System.out.println("LLEGINT DIC");
                   carregaDiccionari(prog.getModel().getDirPath());
               }
               cercarParaules();
               corregir();
               prog.notificar(Missatge.DIBUIXA, 0);
               break;
           case SEGUENT:
               seguent();
               prog.notificar(Missatge.DIBUIXA, 0);
               break;
       }
    }
    
    public void carregaDiccionari(String path){
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            
            String line = br.readLine();
            while (line != null){ //Convertir a for
                prog.getModel().addParaulaDiccionari(line);
                line = br.readLine();
                //System.out.println(line);
            }
            br.close();
            fr.close();
            System.out.println("FI CÀRREGA DIC");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void llegirFitxer(String path){
        try {
            //RandomAccessFile raf = new RandomAccessFile(path, "r");
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            StringBuilder str = new StringBuilder();
            while (line != null){
                str.append(line+"\n");
                line = br.readLine();
            }
            br.close();
            fr.close();
            //byte[] arr = new byte[(int)raf.length()];
            //raf.read(arr);
            prog.getModel().setText(str.toString());
            //prog.getModel().addText(new String(arr, StandardCharsets.UTF_8));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cercarParaules(){
        String text = prog.getModel().getText();
        int i = 0;
        int len = text.length();
        StringBuilder str = new StringBuilder();
        while (i < len){ //Convertir a for
            char c = text.charAt(i);
            if (Character.isAlphabetic(c)){
                str.append(c);
            }
            else{
                //System.out.println("i:" +i);
                if (str.length() > 0){
                    prog.getModel().addParaula(str.toString(), i, str.length());
                    str = new StringBuilder();   
                }
            }
            i++;
        }
        if (str.length() > 0){
            prog.getModel().addParaula(str.toString(), i, str.length());
        }
        for (int j = 0; j < prog.getModel().getNumParaules(); j++){
            Paraula par = prog.getModel().getParaula(j);
            //System.out.println(par.getPar()+", "+par.getLenght()+", "+par.getPos());
        }
    }
    
    public void corregir(){
        for (int i = 0; i < prog.getModel().getNumParaules(); i++){
            String par = prog.getModel().getParaula(i).getPar();
            if (esCorrecta(par)){
                System.out.print("CORRECTA: "+par);
            }
            else{
                prog.getModel().marcaIncorrecta(par);
                
            }
        }
        int incorrectes = prog.getModel().getNumIncorrectes();
        if (incorrectes > 0){
            prog.notificar(Missatge.POPUP, incorrectes);
        }
        seguent();
        prog.notificar(Missatge.DIBUIXA, 0);
    }
    
    public String[] getSimilar(String par, int maxDist){
        if (par.isEmpty()){
            return null;
        }
        List<Paraula> similars = new LinkedList<>();
        String[] candidates = prog.getModel().getDicStartingWith(par.charAt(0));
        if (candidates == null || candidates.length == 0){
            return null;
        }
        String newPar;
        for (int i = 0; i < candidates.length; i++){
            int dist = Levenshtein.calcularDistancia(par, candidates[i]);
            if (dist < maxDist){
                newPar = candidates[i];
                similars.add(new Paraula(newPar, dist));
            }
        }
        Collections.sort(similars, (Paraula left, Paraula right) -> left.getDist() - right.getDist());
        
        String[] ordenades = new String[similars.size()];
        for (int i = 0; i < similars.size(); i++) {
            Paraula paraula = similars.get(i);
            ordenades[i] = paraula.getPar();
        }
        return ordenades;
    }
    
        
    public boolean esCorrecta(String par){
        if (Character.isUpperCase(par.charAt(0))){ //La donam per bona, no corregim noms propis
            return true;
        }
        String[] candidates = prog.getModel().getDicStartingWith(par.charAt(0));
        if (candidates == null){
            return false;
        }
        for (int i = 0; i < candidates.length; i++){
            if (par.equals(candidates[i])){
                return true;
            }
        }
        return false;
    }
    
    public void seguent(){
        System.out.println("SEGÜENT");
        System.out.println("TEXT: "+prog.getModel().getText());
        if (!prog.getModel().isTextCorrecte()){
            String[] opcions = getSimilar(prog.getModel().getText().split(Model.REDSTRING)[1], 3);
            if (opcions != null && opcions.length > 0){
                prog.getModel().setOpcions(opcions);  
            }
            else{
                System.out.println("DFS");
                prog.getModel().setOpcions(new String[]{"BOTAR", "ELIMINAR"});
            }
        }
        else{
            prog.notificar(Missatge.DIBUIXA, 0);
            prog.notificar(Missatge.POPUP, 0);
        }
    }
    
    
    
    @Override
    public void notificar(Missatge m, long n) {
        switch (m) {
            case LLEGEIX:
                this.mode = Mode.LLEGIR;
                this.start();
                break;
            case CORREGEIX:
                this.mode = Mode.CORREGIR;
                this.start();
                break;
            case SEGUENT:
                this.mode = Mode.SEGUENT;
                this.start();
                break;
        }
    }
    
}

class Levenshtein {
    private static int min3(int a, int b, int c) {
         return Math.min(a, Math.min(b, c));
    }

    public static int calcularDistancia(String str1, String str2) {
        
        int [][]distance = new int[str1.length()+1][str2.length()+1];

        for(int i=0;i<=str1.length();i++){
                distance[i][0]=i;
        }
        for(int j=0;j<=str2.length();j++){
                distance[0][j]=j;
        }
        for(int i=1;i<=str1.length();i++){
            for(int j=1;j<=str2.length();j++){
                    int add = (str1.charAt(i-1)==str2.charAt(j-1))? 0:1;
                    distance[i][j]= min3(distance[i-1][j]+1, distance[i][j-1]+1,
                                    distance[i-1][j-1]+ add);
            }
        }
        return distance[str1.length()][str2.length()];
        
    }
}