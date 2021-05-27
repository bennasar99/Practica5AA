/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.model;

/**
 *
 * @author Toni
 */
public class Paraula {
    String par;
    int pos;
    int len;
    int dist = Integer.MAX_VALUE;

    public Paraula(String par, int pos, int len){
        this.par = par;
        this.pos = pos;
        this.len = len;
    }
    
    public Paraula(String par, int dist){
        this.par = par;
        this.dist = dist;
    }
    
    public int getLenght(){
        return this.len;
    }
    
    public void setLength(int length){
        this.len = length;
    }
    
    public int getPos(){
        return this.pos;
    }

    public void setPos(int pos){
        this.pos = pos;
    }
    
    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }
    
    public int getDist(){
        return this.dist;
    }
    
    public void setDist(int dist){
        this.dist = dist;
    }
    
    @Override
    public boolean equals(Object p2){
       if (!p2.getClass().equals(this.getClass())){
           return false;
       }
       Paraula other = (Paraula)p2;
       return other.par.equals(this.par);
    }
}
