/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Model {
    
    private ArrayList<Paraula> words = new ArrayList<>(); 
    private HashMap< Character, ArrayList<String> > dic = new HashMap<>();
    private StringBuilder text;
    private String path;
    private final String dirPath = System.getProperty("user.dir") + "/dic/esutfnobom.dic";
    private int nWord = 0;
    public final static String REDSTRING = "<b>";
    private String[] opcions;

    public Model(){
        text = new StringBuilder();
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
        this.text = new StringBuilder(text);
    }
    
    public String getFitxer(){
        return this.path;
    }
    
    public void setFitxer(String path){
        this.path = path;
    }
    
    public void addText(String text){
        this.text.append(text);
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
    
    public boolean esCorrecta(String par){
        ArrayList<String> candidates = dic.get(par.charAt(0));
        for (int i = 0; i < candidates.size(); i++){
            if (par.equals(candidates.get(i))){
                return true;
            }
        }
        return false;
    }
    
    public String[] getSimilar(String par, int maxDist){
        List<Paraula> similars = new LinkedList<>();
        ArrayList<String> candidates = dic.get(par.charAt(0));
        String newPar;
        for (int i = 0; i < candidates.size(); i++){
            int dist = LevenshteinDistance.computeLevenshteinDistance(par, candidates.get(i));
            if (dist < maxDist){
                newPar = candidates.get(i);
                similars.add(new Paraula(newPar, dist));
            }
        }
        Collections.sort(similars, (Paraula left, Paraula right) -> left.getDist() - right.getDist());
        
        String[] ordenades = new String[similars.size()];
        for (int i = 0; i < similars.size(); i++) {
            Paraula paraula = similars.get(i);
            ordenades[i] = paraula.par;
        }
        return ordenades;
    }
    
    public void clearText(){
        text = new StringBuilder();
    }
}

class LevenshteinDistance {
    private static int minimum(int a, int b, int c) {
         return Math.min(a, Math.min(b, c));
    }

    public static int computeLevenshteinDistance(String str1, String str2) {
        return computeLevenshteinDistance(str1.toCharArray(),
                                          str2.toCharArray());
    }
    private static int computeLevenshteinDistance(char [] str1, char [] str2) {
        int [][]distance = new int[str1.length+1][str2.length+1];

        for(int i=0;i<=str1.length;i++){
                distance[i][0]=i;
        }
        for(int j=0;j<=str2.length;j++){
                distance[0][j]=j;
        }
        for(int i=1;i<=str1.length;i++){
            for(int j=1;j<=str2.length;j++){ 
                  distance[i][j]= minimum(distance[i-1][j]+1,
                                        distance[i][j-1]+1,
                                        distance[i-1][j-1]+
                                        ((str1[i-1]==str2[j-1])?0:1));
            }
        }
        return distance[str1.length][str2.length];
        
    }
}