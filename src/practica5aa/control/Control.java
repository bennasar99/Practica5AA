/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Math.ceil;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica5aa.Notifica;
import practica5aa.Practica5AA;
import practica5aa.model.Model;
/**
 *
 * @author becari2
 */
public class Control extends Thread implements Notifica {

    private Practica5AA prog;
    private boolean compress = true;
    private RandomAccessFile rafO;
    private RandomAccessFile rafD;

    public Control(Practica5AA p) {
        prog = p;
    }

    public void run() {
       
    }

    
    @Override
    public void notificar(Missatge m, long n) {
        switch (m) {
            case COMPRIMEIX:
                this.compress = true;
                this.start();
                break;
            case DESCOMPRIMEIX:
                this.compress = false;
                this.start();
                break;
        }
    }
    
}
