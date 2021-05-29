/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica5aa.vista;
import practica5aa.Practica5AA;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import practica5aa.Notifica;
import practica5aa.model.Model;

/**
 *
 * @author Toni
 */
public class Vista extends JFrame implements ActionListener, ChangeListener, Notifica {
    
    private Practica5AA prog;
    
    private final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
    
    JTextPane textPane = new JTextPane();
    JButton botoSel;
    JButton botoCorr;
    JButton botoSeg;
    JComboBox boxCorr;
    JLabel parActual = new JLabel();
    
    public Vista (String titol, Practica5AA p){
        
        super(titol);
        this.setPreferredSize(new Dimension(800, 600));
        this.prog = p;
        this.getContentPane().setLayout(new BorderLayout());
        
        JPanel bots = new JPanel();

        botoSel = new JButton("Carregar fitxer");
        botoSel.addActionListener(this);
        bots.add(botoSel);
        
        botoCorr = new JButton("Marcar incorrectes");
        botoCorr.setEnabled(true);
        botoCorr.addActionListener(this);
        bots.add(botoCorr);
        
        bots.add(new JLabel("Correcció: "));
        Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
        parActual.setBorder(border);
        bots.add(parActual);
        bots.add(new JLabel("a"));

        String[] opcionsCorreccio = { "..." };

        //Create the combo box, select item at index 4.
        //Indices start at 0, so 4 specifies the pig.
        boxCorr = new JComboBox(opcionsCorreccio);
        boxCorr.addActionListener(this);
        botoCorr.setMultiClickThreshhold(1000);
        boxCorr.setToolTipText("Selecciona la paraula correcta:");
        
        bots.add(BorderLayout.NORTH, boxCorr);
        
        botoSeg = new JButton("Corregir següent");
        botoSeg.setEnabled(false);
        botoSeg.addActionListener(this);
        botoSeg.setMultiClickThreshhold(100);
        bots.add(botoSeg);
        
        this.add(BorderLayout.NORTH, bots);
        
        textPane.addComponentListener(new ComponentAdapter() { //Redimensionament de la finestra
            
            @Override
            public void componentResized(ComponentEvent e) {
                
                textPane.setSize(e.getComponent().getSize().width, e.getComponent().getSize().height);
                
            }
            
        });
        textPane.setVisible(true);
        textPane.setEditable(true);
        textPane.setEnabled(true);
        
        this.add(BorderLayout.CENTER, textPane);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void pinta(){
        
        this.pack();
        this.setVisible(true);
   
        textPane.setText("");
        
        String text = prog.getModel().getText(); 
        if (!text.isEmpty()){
            if (prog.getModel().getOpcions() != null){
                if (text.split(Model.REDSTRING).length > 1){
                    parActual.setText(text.split(Model.REDSTRING)[1]);
                }
                String[] opcions = new String[0];
                if (!parActual.getText().isEmpty()){
                    opcions = prog.getModel().getOpcions();
                }
                this.boxCorr.removeAllItems();
                for (int i = 0; i < opcions.length; i++){
                    this.boxCorr.addItem(opcions[i]);
                }
            }
        }
        
        String[] substr = text.split(Model.REDSTRING);
        Color col = Color.BLACK;
        if (text.startsWith(Model.REDSTRING)){
            col = Color.RED;
        }
        for (String part : substr){
            if (part.isEmpty()){
                continue;
            }
            //System.out.println("PART: "+part);
            appendToPane(textPane, part, col);
            if (col == Color.BLACK){
                col = Color.RED;
            }
            else{
                col = Color.BLACK;
            }
        }
        //appendToPane(textPane, str.toString(), Color.BLACK);
        
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        String comanda = e.toString();
        int a = comanda.indexOf(",cmd=") + 5;

        comanda = comanda.substring(a, comanda.indexOf(",", a)).toUpperCase();
        switch(comanda){
            case "CARREGAR FITXER":
                prog.getModel().reset();
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    String fname = fc.getSelectedFile().getPath();
                    System.out.println(fname);
                    prog.getModel().setFitxer(fname);
                    prog.notificar(Missatge.LLEGEIX, 0);
                    botoCorr.setEnabled(true);
                }
                break;
            case "MARCAR INCORRECTES":
                prog.getModel().reset();
                prog.getModel().setText(textPane.getText());
                prog.notificar(Missatge.CORREGEIX, 0);
                botoSeg.setEnabled(true);
                break;
            case "CORREGIR SEGÜENT":
                
                prog.getModel().substitueix(parActual.getText(), boxCorr.getSelectedItem().toString());
                System.out.println("Replace "+Model.REDSTRING+parActual.getText()+Model.REDSTRING+" with "+ boxCorr.getSelectedItem().toString());
                //botoSeg.setEnabled(true);
                prog.notificar(Missatge.SEGUENT, 0);
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
            JOptionPane.showMessageDialog(null, "No s'han trobat errades", "CORRECTE!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    
}
