/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.vista;
import practica5aa.Practica5AA;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import practica5aa.Notifica;

/**
 *
 * @author Toni
 */
public class Vista extends JFrame implements ActionListener, ChangeListener, Notifica {
    
    private Practica5AA prog;
    
    private final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
    
    JTextArea fileInfo = new JTextArea("Fitxer: NULL");
    JButton botoSel;
    JButton botoCorr;
    
    public Vista (String titol, Practica5AA p){
        
        super(titol);
        this.setPreferredSize(new Dimension(400, 300));
        this.prog = p;
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel bots = new JPanel();

        botoSel = new JButton("Seleccionar fitxer");
        botoSel.addActionListener(this);
        bots.add(botoSel);
        
        botoCorr = new JButton("Marcar incorrectes");
        botoCorr.setEnabled(true);
        botoCorr.addActionListener(this);
        bots.add(botoCorr);

        this.add(BorderLayout.NORTH, bots);
        
        fileInfo.setEditable(false);        
        this.add(BorderLayout.CENTER, fileInfo);
        
        fileInfo.addComponentListener(new ComponentAdapter() { //Redimensionament de la finestra
            
            @Override
            public void componentResized(ComponentEvent e) {
                
                fileInfo.setSize(e.getComponent().getSize().width, e.getComponent().getSize().height);
                
            }
            
        });
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void pinta(){
        
        this.pack();
        this.setVisible(true);
        
        StringBuilder str = new StringBuilder();
        if (fc.getSelectedFile() == null){
            str.append("Selecciona un fitxer...");
        }
        else{
            str.append(prog.getModel().getText());
        }
        fileInfo.setText(str.toString());
        
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        String comanda = e.toString();
        int a = comanda.indexOf(",cmd=") + 5;

        comanda = comanda.substring(a, comanda.indexOf(",", a)).toUpperCase();
        switch(comanda){
            case "SELECCIONAR FITXER":
                //Create a file chooser
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    String fname = fc.getSelectedFile().getPath();
                    System.out.println(fname);
                    prog.getModel().setFitxer(fname);
                    prog.notificar(Missatge.LLEGEIX, 0);
                    botoCorr.setEnabled(true);
                    this.pinta();
                }
                break;
            case "MARCAR INCORRECTES":
                prog.notificar(Missatge.CORREGEIX, 0);
                break;
        }
        
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void notificar(Missatge m, long n) {
        if (m == Missatge.DIBUIXA){
            //Només redibuixam quan cal
            this.pinta();
        }
        else if (m == Missatge.POPUP){
            JOptionPane.showMessageDialog(null, "He tardat "+n+" nanosegons", "ACABAT!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
