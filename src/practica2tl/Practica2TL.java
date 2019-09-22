/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl;

import java.util.ArrayList;
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
        
        String s= "<A>=a<B><C>\n<A>=<D>b<A>\n<B>=/\n<B>=b<A><B>\n<C>=c<C>\n<C>=<D>d<B>\n<D>=/\n<D>=e<E>\n<E>=<B><D>\n<E>=f";
       
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
        
        
        System.out.println(prueba.getDato());  
        System.out.println(g.primeros(prueba));
        
        
    }
    
}
