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
        CORREGIR
    }
    private Practica5AA prog;
    private Mode mode = Mode.LLEGIR;

    public Control(Practica5AA p) {
        prog = p;
    }

    public void run() {
       switch(this.mode){
           case LLEGIR:
               if (prog.getModel().isDicEmpty()){
                   System.out.println("LLEGINT DIC");
                   carregaDiccionari(prog.getModel().getDirPath());
               }
               llegirFitxer(prog.getModel().getFitxer());
               cercarParaules();
               prog.notificar(Missatge.DIBUIXA, 0);
               break;
           case CORREGIR:
               corregir();
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
            while (line != null){
                prog.getModel().addText(line+"\n");
                line = br.readLine();
            }
            br.close();
            fr.close();
            //byte[] arr = new byte[(int)raf.length()];
            //raf.read(arr);
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
            if (!Character.isWhitespace(c)){
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
        for (int j = 0; j < prog.getModel().getNumParaules(); j++){
            Paraula par = prog.getModel().getParaula(j);
            System.out.println(par.getPar()+", "+par.getLenght()+", "+par.getPos());
        }
    }
    
    public void corregir(){
        String corregit = prog.getModel().getText();
        for (int i = 0; i < prog.getModel().getNumParaules(); i++){
            String par = prog.getModel().getParaula(i).getPar();
            if (prog.getModel().esCorrecta(par)){
                System.out.print("CORRECTA: "+par);
            }
            else{
                String newPar = prog.getModel().getSimilar(par);
                System.out.println("INCORRECTA "+par+", CORRECCIÓ: "+newPar);
                corregit.replace(par, newPar);
            }
        }
        prog.getModel().clearText();
        prog.getModel().addText(corregit);
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
        }
    }
    
}
