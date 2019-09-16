/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl.controller;

import practica2tl.listaG;
import practica2tl.nodoLg;

/**
 *
 * @author FABIAN
 */
public class controlador {

    public controlador() {
    }
    
    listaG lg;
    nodoLg reco;
    boolean tieneNulo= false;
    
    public listaG convertirALg(String s){
        lg =new listaG();        
        String dato;
        char rec,token;        
        boolean ladoiz, finDeLinea;
        String prod[]= s.split("\n");     
        for (String prod1 : prod) {            
            dato = "";
            finDeLinea = false;
            ladoiz= true;
            prod1 =prod1.replace(" ", "");
            char[] aux = prod1.toCharArray();
            for (int i = 0; i < aux.length; i++) {
                rec= aux[i];
                switch(rec){
                    case '<':                        
                        i++;
                        token=aux[i];
                        while(token!='>'){                                  //Concatenar el nombre del N
                            dato=dato+Character.toString(token);
                            i++;
                            token=aux[i];
                        }
                        if(i+1 == aux.length){
                            finDeLinea=true;
                        }
                        if(ladoiz){
                            lg.insertarNodo(dato, 'N', false, false);                            
                        }else{
                            lg.insertarNodo(dato, 'N', finDeLinea, true);                            
                        }                        
                        
                        dato="";
                        break;
                    case '=':                        
                        ladoiz=false;
                        break;
                    case '/':
                        finDeLinea=true;
                        lg.insertarNodo(Character.toString(rec),'T' , finDeLinea, true);
                        i++;
                        break;                    
                    default:
                        if(i+1 == aux.length){
                            finDeLinea=true;                            
                        }
                        lg.insertarNodo(Character.toString(rec), 'T', finDeLinea, true);                         
                        break;
                }
            }            
        }  
        return lg;
    }
    
    
    public boolean esEspecial(listaG lg){
        
        reco = lg.getPrimer();
        if(reco == null || reco.getLigaD() == null){
            return false;
        }   
        int contT=0;
        int contN=0;
        reco = reco.getLigaD();
        nodoLg h = reco.getLigaH();
        nodoLg aux;                         
        while(reco != null){                                                    //Recorre los nodos padres
            if(h.getLigaD() == null ||h.getLigaD().getTipo()!='T' )
            {
                return false;
            } 
            aux=h;
            h = h.getLigaD();
            while(h!=null){               
                           
                if(h.getTipo()=='T')
                {
                    contT++;
                }
                else
                {
                    contN++;
                }
                h = h.getLigaD();
                
                if(contT > 1 || contN > 1)
                {
                    return false;
                }
                if(h==null)
                {                    
                    if(aux.getLigaH()!=null)
                    {
                        contN=0;
                        contT=0;
                        h = aux.getLigaH();
                        aux = h;
                        h= h.getLigaD();
                    }
                }
            }
            if(contT > 1 || contN > 1)
            {
                return false;
            }
            contT =0;
            contN=0;
            reco=reco.getLigaD();
            if(reco != null)
            {
                h = reco.getLigaH();
            }            
        }
        return true;
    }
    
    public boolean esLinealPorDerecha(listaG lg){
        
        reco = lg.getPrimer();
        if(reco == null || reco.getLigaD() == null){
            return false;
        }   
        int cont=0;      
        int contT=0;
        reco = reco.getLigaD();
        nodoLg h = reco.getLigaH();
        nodoLg aux;                  
        String dato="";
        while(reco != null){                                                    //Recorre los nodos padres
             
            aux=h;
            h = h.getLigaD();
            while(h!=null){               
                           
                if(h.getTipo()=='N')
                {
                    cont++;
                    if(cont>1)
                    {
                        return false;
                    }
                }
                else
                {
                    if(cont==1)
                    {
                        return false;
                    }
                    contT++;
                    dato = h.getDato();
                }
                h = h.getLigaD();
                
                if(h==null)
                {                    
                    if(aux.getLigaH()!=null)
                    {
                        cont=0;
                        contT=0;
                        h = aux.getLigaH();
                        aux = h;
                        h= h.getLigaD();
                    }
                }
            }       
            if(cont==0)
            {
                if(contT ==1 && !"/".equals(dato) )
                {
                    return false;
                }
                if(contT>1)
                {
                    return false;
                }
            }
            contT =0;
            cont=0;
            reco=reco.getLigaD();
            if(reco != null)
            {
                h = reco.getLigaH();
            }            
        }
        return true;          
    }
    
    public boolean esS(listaG lg)
    {
        reco = lg.getPrimer();
        reco = reco.getLigaD();
        nodoLg h = reco.getLigaH();
        nodoLg aux = h;
        String dato= "";
        String todo="";
        while(reco != null)
        {
            h = h.getLigaD();
            dato= dato + h.getDato();
            if(h.getLigaD()!=null && h.getLigaD().getTipo()=='T'){
                return false;
            }
            if(aux.getLigaH()!=null)
            {
                while(aux.getLigaH()!=null)
                {
                    h=aux.getLigaH();
                    if(dato.contains(h.getLigaD().getDato()))
                    {
                        return false;
                    }                    
                    aux =h;
                    h=h.getLigaD();                    
                    if(h.getLigaD()!=null && h.getLigaD().getTipo()=='T')
                    {
                        return false;                        
                    }
                    dato=dato+h.getDato();
                }                
            }
            reco = reco.getLigaD();
            todo=todo+dato;
            dato="";
            if(reco!=null)
            {
                h=reco.getLigaH();
                aux=h;                
            }
        }
        if(todo.contains("/"))
        {
            tieneNulo=true;
            return false;
        }
        return true;
    }
    public boolean esQ(listaG lg)
    {
        if(esS(lg)){
            return true;
        }
        else
        {
            return tieneNulo;
        }
    }
    
}
