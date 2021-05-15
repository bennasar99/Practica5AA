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

    public Paraula(String par, int pos, int len){
        this.par = par;
        this.pos = pos;
        this.len = len;
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
}
