/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Model {
    
    private ArrayList<Paraula> words = new ArrayList<>(); 
    private HashMap< Character, ArrayList<String> > dic = new HashMap<>();
    private String text = "";
    private String path;
    private final String dirPath = System.getProperty("user.dir") + "/dic/esutfnobom.dic";
    //private int nWord = 0;
    public final static String REDSTRING = "æ"; //Caràcter que ningú posarà (esperem)
    private String[] opcions; //Opcions per mostrar a la selecció de paraula correcte

    public Model(){
    }
    
    public void reset(){
        words.clear();
        opcions = null;
    }
    
    public void setOpcions(String[] opcions){
        this.opcions = opcions;
    }
    
    public String[] getOpcions(){
        return this.opcions;
    }
    
    public String getText(){
        return text.toString();
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public String getFitxer(){
        return this.path;
    }
    
    public void setFitxer(String path){
        this.path = path;
    }
    
    public void addParaulaDiccionari(String paraula){
        Character c = paraula.charAt(0);
        if (!dic.containsKey(c)){
            dic.put(c, new ArrayList<>());
        }
        dic.get(c).add(paraula);
    }
    
    public void addParaula(String paraula, int pos, int length){
        Paraula par = new Paraula(paraula, pos, length);
        if (words.contains(par)){
            return;
        }
        words.add(new Paraula(paraula, pos, length));
    }
    
    public int getNumParaules(){
        return words.size();
    }
    
    public Paraula getParaula(int index){
        return words.get(index);
    }
    
    public String getDirPath(){
        return this.dirPath;
    }
    
    public boolean isDicEmpty(){
        return (this.dic.isEmpty());
    }
    
    public void substitueix(String oldPar, String newPar){
        this.text = this.text.replaceFirst(REDSTRING+oldPar+REDSTRING, newPar);
    }
    
    public void marcaIncorrecta(String par){
        this.text = this.text.replaceFirst(par, REDSTRING+par+REDSTRING);
    }
    
    public String[] getDicStartingWith(char c){
        ArrayList<String> candidates = dic.get(c);
        if (candidates == null || candidates.isEmpty()){
            return null;
        }
        return Arrays.stream(candidates.toArray()).toArray(String[]::new);
    }
    
    public boolean isTextCorrecte(){
        return !text.contains(REDSTRING);
    }
    
    public int getNumIncorrectes(){
        return (int)text.chars().filter(ch -> ch == REDSTRING.charAt(0)).count()/2;
    }
    
    public String getPrimeraIncorrecta(){
        return this.text.split(REDSTRING)[1];
    }
}
