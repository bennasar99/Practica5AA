/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Math.ceil;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.PriorityQueue;

/**
 *
 * @author becari2
 */
public class Model {

    private StringBuilder text;
    private int nWord = 0;

    public Model(){
        text = new StringBuilder();
    }
}
