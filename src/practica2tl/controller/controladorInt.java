/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl.controller;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
    //

    JFileChooser fc = new JFileChooser();
    File archivo;
    FileInputStream is;
    FileOutputStream os;

    //Inicializa componentes en la vista
    public void iniciarComponentes(JButton btnValidar, JButton btnEditar, 
            JTextArea jt,JLabel esp,JLabel s, JLabel q, JLabel ll,JLabel lineal){
        //Botones desactivados
        btnValidar.setEnabled(false);
        btnEditar.setEnabled(false);
        //Labels ocultos
        esp.setVisible(false);
        s.setVisible(false);
        q.setVisible(false);
        ll.setVisible(false);
        jt.setEditable(false);     
        lineal.setVisible(false);
    }
    //Metodo para aceptar la gramática
    public void btnAceptar(JTextArea jt, JTextArea jt2, JButton Aceptar, JButton Editar, JButton Validar){
        //try para el manejo de errores
        try {
            //Convierte el texto ingresado en una gramatica
            lg = cc.convertirALg(jt.getText());
            cc.mostrarMensaje("Procesando gramatica");
            //Al segundo texttArea le lleva los anulables, primeros, siguientes y seleccion de la gramática
            jt2.setText("Anulables:\n"+lg.anulables().toString()+"\n"+"\n"
                    +"Primeros:"+"\n"+cc.mostrarPrimeros()+"\n"+
                    "Siguientes:"+"\n"+cc.mostrarSiguientes()+"\n"+
                    "Seleccion por producción"+"\n"+cc.mostrarSeleccion()); 
            //Inhabilita la edición del textArea que tiene la gramática
            jt.setEditable(false);
            //Desactiva botón aceptar
            Aceptar.setEnabled(false);
            //Activa botón de editar y validar
            Editar.setEnabled(true);
            Validar.setEnabled(true);
            
        } catch (Exception e) {
            cc.mostrarMensaje("Error al prosesar la gramatica. Asegurese que esté bien escrita");
        }
    }
    //Método para el botón editar
    public void btnEditar(JTextArea jt, JButton Aceptar, JButton Editar, JButton Validar,
            JLabel esp,JLabel lpd, JLabel s, JLabel q, JLabel ll){
        //Activa y desactiva botones 
        jt.setEditable(true);
        Aceptar.setEnabled(true);
        Editar.setEnabled(false);
        Validar.setEnabled(false);
        //Muestra los labels ocultos
        esp.setVisible(false);
        lpd.setVisible(false);
        s.setVisible(false);
        q.setVisible(false);
        ll.setVisible(false);
    
    }
    //Método para el botón validar
    public void btnValidar(JLabel esp, JLabel s, JLabel q, JLabel ll,JLabel lineal){
        //Escribe en el label si la gramatica es o no especial 
        if (cc.esEspecial(lg)){
            esp.setText("Es especial");
        }
        else{
            esp.setText("No es especial");
        }
        //Escribe en el label si la gramatica es o no S
        if (cc.esS(lg)){
            s.setText("Es S");
        }
        else{
            s.setText("No es S");
        }
        //Escribe en el label si la gramatica es o no Q
        if (cc.esQ(lg)){
            q.setText("Es Q");
        }
        else{
            q.setText("No es Q");
        }
        //Escribe en el label si la gramatica es o no LL1
        if (cc.esLL1(lg)){
            ll.setText("Es LL(1)");
        }
        else{
            ll.setText("No es LL(1)");
        }
        //Escribe en el label si la gramatica es o no lineal por la derecha
        if (cc.esLinealPorDerecha(lg)){
            lineal.setText("Es LPD");
        }
        else{
            lineal.setText("No es LPD");
        }
        //Muestra los labels ocultos
        esp.setVisible(true);
        s.setVisible(true);
        q.setVisible(true);
        ll.setVisible(true);        
        lineal.setVisible(true);
    }
    //Método para cargar un archivo de texto y llevarlo a un string 
    public String cargar(File archivo){
        String s = "";
        try {
            is= new FileInputStream(archivo);
            int i;
            while((i=is.read())!=-1){
                char c = (char)i;
                s+=c;
            }
        } catch (Exception e) {
        }
        return s;    
    }
    //Método para el botón de información
    public void btnInfo(){
        
        final String mensaje = ("TENGA EN CUENTA LAS SIGUIENTES INSTRUCCIONES PARA EL INGRESO DE LA GRAMATICA:\n"
            + "1.   Los No terminales se expresan entre <>. Por ejemplo <S>.\n"
            + "2.   Los terminales se toman como caracteres individuales.Por ejemplo ab son dos terminales distintos \n"
            + "3.   Se utiliza el simbolo '=' únicamente para asignarle el lado derecho de una producción a un No terminal .\n"
            + "4.   El simbolo para producción nula es /.\n"
            + "5.   Las producciones no deben tener espacios entre ellas \n"
            + "6.   Las producciones deben ir en orden, es decir si hay varias p/n de un mismo no terminal,\n"
            + "      estas deben ir una seguida de la otra\n"
            + "7.   Un ejemplo de una produccion valida es el siguiente: <S>=abc<B>.\n"                
            + "8.   Un ejemplo de una gramatica valida es el siguiente: \n "
                + "<S>=<S>a<A>\n"
                + " <S>=b\n"
                + " <A>=/\n"
                + " <A>=ab\n"
        );
        cc.mostrarMensaje(mensaje);
    }
    //Método para cargar el contenido del string que tiene el archivo de texto a un JTextArea  
    public void btnCargar(JTextArea jt){
        if(fc.showDialog(null, "Abrir")==JFileChooser.APPROVE_OPTION){
            archivo=fc.getSelectedFile();
            if(archivo.canRead()){
                if(archivo.getName().endsWith("txt")){
                    String s = cargar(archivo);
                    jt.setText(s);
                }else{
                    JOptionPane.showMessageDialog(null, "Archivo no compatible");
                }                
            }
        }
    }
    
}
