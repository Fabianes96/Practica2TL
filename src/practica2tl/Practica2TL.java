/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl;

import practica2tl.view.view;
import practica2tl.controller.controlador;

/**
 *
 * 
 * @author FABIAN
 */
public class Practica2TL {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//         TODO code application logic here
//        view v = new view();
//        v.show();
        
        String s= "<B>=<T>\n<S>=fv\n<T>=/\n<S>=s";
       
        controlador c = new controlador();
        listaG g;
        g = c.convertirALg(s);
        String aux="";
//        nodoLg l = g.getRaiz();
//        
//        l=l.getLigaD();
//        nodoLg a=l.getLigaH();
//        aux = l.getDato();
//        l= a;
//        while(a!=null)
//        {
//            if(l==null)
//            {
//               a=a.getLigaH();
//               if(a!=null)l=a;
//            }            
//            if(l!=null){aux=aux+l.getDato();
//            l=l.getLigaD();}
//        }
//        
//        System.out.println(aux);
        nodoLg prueba = g.getPrimer().getLigaD();
        boolean loquesea = g.esAnulable(prueba);        
        System.out.println(loquesea);
        System.out.println(g.anulables());
      
    }
    
}
