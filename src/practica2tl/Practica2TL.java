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
        view v = new view();
        v.show();
        
        String s= "<A>=a<B><C>\n<A>=<D>b<A>\n<B>=/\n<B>=b<A><B>\n<C>=c<C>\n<C>=<D>d<B>\n<D>=/\n<D>=e<E>\n<E>=<B><D>\n<E>=f";
        String ss= "<A>=<B>c<D>a\n<A>=a<B>b\n<B>=b<A><C>\n<B>=/\n<B>=<C><D>a\n<C>=<D>a<B>\n<C>=/\n<D>=d\n<D>=<A><B>c";
        String sss= "<A>=<B>c<D>\n<A>=a<E>\n<B>=b<A>c\n<B>=/\n<D>=d<B><D>c\n<D>=/\n<E>=a\n<E>=<B><D>";
        controlador c = new controlador();
        listaG g;
        g = c.convertirALg(ss);
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
        nodoLg prueba = g.getPrimer().getLigaD().getLigaD();
        nodoLg prueba2 = g.getPrimer().getLigaD().getLigaD();
        System.out.println(prueba.getDato());
        
       //ArrayList ssada = g.validarSiguientes(prueba);
        //ArrayList sadsa= g.primeros(prueba);
      //String qwerty= ssada.toString();
       System.out.println(g.validarPrimeros(prueba));
//        System.out.println(g.siguientes(prueba));
//        System.out.println(prueba.getLigaD().getDato());
//        System.out.println(g.siguientes(prueba.getLigaD()));
//        System.out.println(prueba.getLigaD().getLigaD().getDato());
//        System.out.println(g.siguientes(prueba.getLigaD().getLigaD()));
//        System.out.println(prueba.getLigaD().getLigaD().getLigaD().getDato());
//        System.out.println(g.siguientes(prueba.getLigaD().getLigaD().getLigaD()));
//        System.out.println(prueba.getLigaD().getLigaD().getLigaD().getLigaD().getDato());
//        System.out.println(g.siguientes(prueba.getLigaD().getLigaD().getLigaD().getLigaD()));
//        System.out.println(g.getSeleccion());
//        System.out.println(g.seleccionPorProduccion(8));
//        System.out.println(qwerty);
        
        System.out.println(g.getSeleccion());
    }
    
}
