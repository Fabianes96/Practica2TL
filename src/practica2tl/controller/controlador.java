/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl.controller;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.util.ArrayList;
import javax.swing.JOptionPane;
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
    
    //Método que convierte el string ingresado a una lista generalizada
    public listaG convertirALg(String s){
        //Inicializamos la lista
        lg =new listaG();       
        //Instanciaciones
        String dato,nProd;
        char rec,token;        
        boolean ladoiz, finDeLinea;
        //Dividimos el string ingresado cada vez que haya un salto de linea
        String prod[]= s.split("\n");     
        //contador
        int cont = 0,c;
        //Ciclo for para cada elemento en el arreglo prod
        for (String prod1 : prod) {            
            //Instanciaciones
            dato = "";
            nProd="";
            finDeLinea = false;
            ladoiz= true;
            //Se eliminan los espacios que hayan
            prod1 =prod1.replace(" ", "");
            //Se elimina "\r" que aparece al cargar un archivo y se deja solo "\n"
            prod1 = prod1.replace("\r", "");
            cont++;
            c=0;
            //Convertimos el string que haya en prod1 a un arreglo de char
            //Este string prod1 corresponde a una producción
            char[] aux = prod1.toCharArray();
            //Con este for se analiza cada char
            for (int i = 0; i < aux.length; i++) {
                rec= aux[i];
                //Se analiza cada posible caso
                switch(rec){
                    //Simbolo para abrir un NT
                    case '<':                        
                        i++;
                        token=aux[i];
                        while(token!='>'){//Concatenamos el nombre del NT
                            dato=dato+Character.toString(token);
                            i++;
                            token=aux[i];
                        }
                        if(i+1 == aux.length){//Se lleva true a findelinea si es el fin de la producción
                            finDeLinea=true;
                        }
                        if(ladoiz){//Analiza si el NT corresponde al lado izquierdo de la produccion
                            nProd=dato;
                            lg.insertarNodo(dato, 'N', false, false,cont, nProd);                            
                        }else{
                            lg.insertarNodo(dato, 'N', finDeLinea, true,cont,nProd);                            
                        }                                                
                        //Actualiza dato
                        dato="";
                        break;
                    case '=':           
                        //Si el simbolo es = quiere decir que ya se analizó el lado izquierdo
                        
                        if(c!=0){
                            lg.insertarNodo(null,' ',false,false,0,null);
                        }
                        ladoiz=false;
                        c++;

                        
                        break;
                    case '/': //Simbolo definido para producciones nulas
                        finDeLinea=true;
                        lg.insertarNodo(Character.toString(rec),'T' , finDeLinea, true,cont,nProd);
                        i++;
                        break;                    
                    default:// caso general que puede ser cualquier terminal
                        if(ladoiz==true){
                            lg.insertarNodo(null,' ',false,false,0,null);
                        }
                        if(i+1 == aux.length){
                            finDeLinea=true;                            
                        }
                        lg.insertarNodo(Character.toString(rec), 'T', finDeLinea, true,cont,nProd);                         
                        break;
                }
            }            
        }
        lg.setTamano(cont);
        return lg;
    }
    
    //Método para definir si una gramática es de la forma especial
    public boolean esEspecial(listaG lg){
        // Nos ubicamos en el primer nodo
        reco = lg.getPrimer();
        if(reco == null || reco.getLigaD() == null){
            return false;
        }   
        int contT=0;
        int contN=0;
        reco = reco.getLigaD();
        //Ubicamos el hijo del nodo que correspondera a una producción
        nodoLg h = reco.getLigaH();
        nodoLg aux;                      
        //Avanzamos en la lista
        while(reco != null){   
            //Si el dato que tiene el hijo en su liga derecha es un NT entonces no es de la forma especial
            if(h.getLigaD() == null ||h.getLigaD().getTipo()!='T' )
            {
                return false;
            } 
            aux=h;
            h = h.getLigaD();
            //Recorremos todos los nodos liga derecha de los hijos (producciones)
            while(h!=null){               
                //Incrementamos los contadores en caso de que haya mas de un T o de un NT
                if(h.getTipo()=='T')
                {
                    contT++;
                }
                else
                {
                    contN++;
                }
                h = h.getLigaD();
                //Si tiene mas de un T o de un NT no es de la forma especial
                if(contT > 1 || contN > 1)
                {
                    return false;
                }
                //Avanzamos en los hijos
                if(h==null)
                {                    
                    if(aux.getLigaH()!=null)
                    {
                        //Actualizamos los contadores para analizar otra producción
                        contN=0;
                        contT=0;
                        h = aux.getLigaH();
                        aux = h;
                        h= h.getLigaD();
                    }
                }
            }
            //Si los contadores tienen mas de 1, no es de la forma especial
            if(contT > 1 || contN > 1)
            {
                return false;
            }
            //Actualizamos contadores y avanzamos en la lista generalizada con los NT
            contT =0;
            contN=0;
            reco=reco.getLigaD();
            if(reco != null)
            {
                h = reco.getLigaH();
            }            
        }
        //Si no retornó en ningún caso anterior false, entonces es de la forma especial
        return true;
    }
    //Método para analizar si el lineal por la derecha (LPD)
    public boolean esLinealPorDerecha(listaG lg){
        //Se ubica en el primer nodo
        reco = lg.getPrimer();
        if(reco == null || reco.getLigaD() == null){
            return false;
        }   
        int cont=0;      
        int contT=0;
        //Nos ubicamos en el hijo del primer NT
        reco = reco.getLigaD();
        nodoLg h = reco.getLigaH();
        nodoLg aux;                  
        String dato="";
        while(reco != null){//Recorre los nodos padres
             
            aux=h;
            h = h.getLigaD();
            while(h!=null){//Recorremos los hijos
                           
                if(h.getTipo()=='N')
                {
                    cont++;
                    //Si encontramos mas de un NT entonces no es LPD
                    if(cont>1)
                    {
                        return false;
                    }
                }
                else
                {
                    //El contador de no terminales debe ser 0 en este caso
                    if(cont==1)
                    {
                        return false;
                    }
                    contT++;
                    dato = h.getDato();
                }
                h = h.getLigaD();
                //Recorremos los nodos hijos
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
            {   //Con el contador en 0 se debe analizar si el contador de T es mayor o igual a 1
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
    //Método para definir si la gramática es S
    public boolean esS(listaG lg)
    {
        reco = lg.getPrimer();
        reco = reco.getLigaD();
        nodoLg h = reco.getLigaH();
        nodoLg aux = h;
        String dato= "";
        String todo="";
        //Recorremos la lista generalizada
        while(reco != null)
        {
            //Analizamos los nodos hijos que representan las producciones
            h = h.getLigaD();
            dato= dato + h.getDato();
            //Si el primer elemento es un NT no es S
            if(h.getLigaD()!=null && h.getLigaD().getTipo()=='T'){
                return false;
            }
            //recorrido de nodos hijos
            if(aux.getLigaH()!=null)
            {
                while(aux.getLigaH()!=null)
                {
                    h=aux.getLigaH();
                    //Si se repite el terminal de otra producción correspondiente al mismo nodo padre entonces no es S 
                    if(dato.contains(h.getLigaD().getDato()))
                    {
                        return false;
                    }                    
                    aux =h;
                    h=h.getLigaD();   
                    //Si el primer dato de la producción es un NT, no es S
                    if(h.getLigaD()!=null && h.getLigaD().getTipo()=='T')
                    {
                        return false;                        
                    }
                    dato=dato+h.getDato();
                }                
            }
            //Avanzamos en la gramática
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
            //Si la gramática tiene una producción anulable no es S
            //Llevamos true al booleano tieneNulo para utilizarlo con el método de gramaticas Q
            tieneNulo=true;
            return false;
        }
        return true;
    }
    //Retorna si la gramática es Q
    public boolean esQ(listaG lg)
    {
        if(esS(lg)){//Si la gramatica es S, tambien es Q
            return true;
        }
        else
        {
            //Si no es S, retornará el valor que tenga tieneNulo
            return tieneNulo;
        }
    }
    //Método para definir si la gramática es LL1
    public boolean esLL1(listaG lg)
    {
        //Flag de retorno
        boolean flag= true;
        //array con las selecciones de las producciones
        ArrayList sel = lg.getSeleccion();
        String comparacion="";        
        //Se realizan los mismos pasos de los anteriores metodos en cuanto a instanciación y recorrido

        reco = lg.getRaiz();
        reco = reco.getLigaD();
        nodoLg h = reco.getLigaH();        
        while(reco!=null){         
            //Nos intereza saber si el primer elemento de la producción en el lado derecho es igual al del lado izquierdo
            if(h.getLigaD().getDato().equals(reco.getDato())){
                //Es ese caso no es LL1
                return false;
            }
            h=h.getLigaH();
            if(h==null){
                
                reco=reco.getLigaD();
                if(reco!=null){                    
                    h=reco.getLigaH();
                }
            }
        }
        //Analizamos en este ciclo las producciones con igual selección
        for (int i = 1; i < sel.size()+1; i++) {
            for (int j = i+1; j < sel.size()+1; j++) {
                if(lg.seleccionPorProduccion(i).equals(lg.seleccionPorProduccion(j))){
                    comparacion =comparacion + Integer.toString(i)+Integer.toString(j);
                }
            }
        }
        //Si todas las selecciones son distintas, es LL1
        if(comparacion.equals("")){
            return true;
        }
        else{//Si no, entonces hay que mirar que los NT correspondientes a las producciones de seleccion igual, sean disintos
            reco= lg.getRaiz().getLigaD();
            h=reco.getLigaH();
            String s="";
            int i=0;
            while(reco!=null){//recorremos los NT
                if(i>s.length())
                {
                    break;
                }
                //Ubicamos el NT de una producción en particular
                if(h.getLigaD().getProduccion()== (int)comparacion.charAt(i)){
                    s = s +h.getLigaD().getNomProduccion();
                    i++;
                    reco=lg.getRaiz().getLigaD();
                    h= reco.getLigaH();
                }
                else{                    
                    //Avanzamos en la lista
                    h=h.getLigaH();
                    if(h==null){
                        reco= reco.getLigaD();
                        if(reco!=null){
                            h=reco.getLigaH();
                        }
                    }                    
                }
            }//Realizamos la comparación            
            for (int j = 0; j < s.length(); j++) {
                if(j+1 >s.length()){
                    break;
                }
                if(s.charAt(j)== s.charAt(j+1) && j+1%2!=0){
                    flag = false;
                }                
            }
            return flag;
        }        
    }
    //Muestra los primeros de todos los NT
    public String mostrarPrimeros(){
        String primeros="";
        nodoLg l = lg.getRaiz().getLigaD();
        while(l!=null)
        {
            primeros = primeros +"(<"+l.getDato()+">): " + lg.validarPrimeros(l).toString()+"\n";
            l=l.getLigaD();
        }
        return primeros; 
    }
    //Muestra los siguientes de todos los NT
    public String mostrarSiguientes(){
        String sgtes="";
        nodoLg l = lg.getRaiz().getLigaD();
        while(l!=null)
        {
            sgtes = sgtes +"(<"+l.getDato()+">): " + lg.validarSiguientes(l)+"\n";
            l=l.getLigaD();
        }
        return sgtes;
    }
    //Retorna la selección de todas las producciones
    public String mostrarSeleccion(){
        String sel="";
        try {//En caso de que haya algún error al obtener la selección, puede deberse a mal ingreso de la gramática
            ArrayList s = lg.getSeleccion();
            
        } catch (Exception e) {
            mostrarMensaje("Error al procesar la selección.\n"
                    + "Asegurese que no haya saltos de linea entre las producciones o que estas estén en desorden");
            return sel;
        }
        
        nodoLg l = lg.getRaiz().getLigaD();
        //Ubicamos la ultima producción
        while(true)
        {
            if(l.getLigaD()==null){
                break;
            }
            l=l.getLigaD();            
        }
        nodoLg h = l.getLigaH();
        while(true)
        {
            if(h.getLigaH()==null){
                break;
            }
            h=h.getLigaH();            
        }
        //Llevamos el número de la última producción al int p
        int p = h.getLigaD().getProduccion();
        for (int i = 1; i < p+1; i++) {//Llevamos al string sel todas selecciones
            sel = sel +"Prod (" +i+"): "+ lg.seleccionPorProduccion(i)+"\n";
        }
        return sel;        
    }
   //Despliega un mensaje a través de un JOptionPane
    public void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje);  
    }
}
