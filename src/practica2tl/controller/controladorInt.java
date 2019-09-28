/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl.controller;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import practica2tl.listaG;


/**
 *
 * @author FABIAN
 */
public class controladorInt {
    
    private controlador cc = new controlador();
    private listaG lg;
    
    public void btnAceptar(JTextArea jt, JTextArea jt2){
        
        lg = cc.convertirALg(jt.getText());
        cc.mostrarMensaje("Gramatica procesada");
        jt2.setText("Anulables:\n"+lg.anulables().toString()+"\n"+"\n"
                +"Primeros:"+"\n"+cc.mostrarPrimeros()+"\n"+
                "Siguientes:"+"\n"+cc.mostrarSiguientes()+"\n"+
                "Seleccion por producción"+"\n"+cc.mostrarSeleccion());
        
        
    }
    
}
