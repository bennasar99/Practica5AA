/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica5aa.control.Control;
import practica5aa.model.Model;
import practica5aa.vista.Vista;

/**
 *
 * @author Toni
 */
public class Practica5AA implements Notifica{

    private Vista vista;
    private Model model;
    private Control control;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new Practica5AA()).inici();
    }
    
    public void inici(){
        
       try {
           //Cream la carpeta "out", si no existeix
           Files.createDirectories(Paths.get(System.getProperty("user.dir")+"/out")); 
       } catch (IOException ex) {
           Logger.getLogger(Practica5AA.class.getName()).log(Level.SEVERE, null, ex);
       }
       
        // cream les estructures i passam un punter del programa principal (a vista)
        model = new Model();
        vista = new Vista("Corrector", this);
        
        vista.pinta();
        //control.start();
        
        //control.notificar(Missatge.ATURA);
        //control.notificar(Missatge.CONTINUA);
        
    }
    
    @Override
    public void notificar (Missatge m, long n){
        
        switch(m){
            
            case LLEGEIX: case CORREGEIX: case SEGUENT:
                if (control == null){
                    control = new Control(this);
                }
                if (!control.isAlive()){
                    control = new Control(this);
                }
                control.notificar(m, n);
                break;
            case DIBUIXA: case POPUP:
                vista.notificar(m, n);
                break;
        }
        
    }
    
    //Getters i Setters
    public Vista getVista(){
        return this.vista;
    }
    
    public Model getModel(){
        return this.model;
    }
    
    public Control getControl(){
        return this.control;
    }
}
