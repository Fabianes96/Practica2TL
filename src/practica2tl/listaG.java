/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author FABIAN
 */
public class listaG {

    private final nodoLg raiz;
    private nodoLg posicion;
    private nodoLg posAux;
    private nodoLg primer;
    private nodoLg ultimo;
    private ArrayList seleccion;
    private int tamano;
    
    //Constructor de la lista que lleva un nodo con dato "*"
    public listaG() {
        nodoLg x = new nodoLg("*");
        posicion = raiz = primer = ultimo = x;
    }    
    //Conecta un nuevo nodo hacia la derecha
    public void conectarNodo(String dato, char tipo, boolean fin, int prod, String nProd) {
        nodoLg dat = new nodoLg(dato);
        dat.setTipo(tipo);
        dat.setFinDeLinea(fin);
        posicion.setLigaD(dat);
        dat.setLigaI(posicion);
        posicion = dat;
        dat.setProduccion(prod);
        dat.setNomProduccion(nProd);
    }
    //Recorre la línea principal desde la raíz para buscar un NT
    public boolean encontrarDato(String d) {                                                 
        posicion = raiz;
        while (posicion.getLigaD() != null) {
            posicion = posicion.getLigaD();
            if (posicion.getDato().equals(d)) {
                return true;
            }
        }
        return false;
    }
    //Conecta un nodo cabeza como hijo de un nodo cualquiera
    public void agregarNodoCabeza() {
    //Si existen más nodos cabeza hacia abajo
        while (posicion.getLigaH() != null) {
            posicion = posicion.getLigaH();
        }
        nodoLg nodocabeza = new nodoLg("*");
        posicion.setLigaH(nodocabeza);
        posicion = nodocabeza;

    }
    //Hace las validaciones para insertar T y NT a la lista
    public void insertarNodo(String dato, char tipo, boolean finDeLinea, boolean padre, int prod, String nProd) { 
        //Si no tiene padre entonces se encuentra en la parte izquierda de la producción
        if (padre == false) {                                                                  
        //Si encuentra que el NT ya existe, entonces actualiza posicion para agregar ahí
            if (encontrarDato(dato)) {                                                   
                posicion = posicion.getLigaH();
            } else {         
            //Si no existe el NT, entonces lo agrega y le añade un nodo cabeza para empezar a agregar ahí
                conectarNodo(dato, tipo, finDeLinea, prod, nProd);
                agregarNodoCabeza();
            }
        } else { //Si tiene padre es de la parte derecha de la producción            
             //Si no hay datos insertados a la derecha
            if (posicion.getLigaD() == null) {  
                //Si la línea termina ahí, entonces añade nodo cabeza para seguir escribiendo
                if (posicion.isFinDeLinea()) {                           
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea, prod, nProd);
                } else { //Si aún no termina de escribir la línea entonces sigue añadiendo hacia la derecha                                                                     
                    conectarNodo(dato, tipo, finDeLinea, prod,nProd);
                }
            } else {
                //Si es diferente, entonces añade una sub-línea
                agregarNodoCabeza();
                conectarNodo(dato, tipo, finDeLinea, prod, nProd);
                
            }
        }
    }
    //Verificar si un nodo es anulable
    public boolean esAnulable(nodoLg a) {
        // Si el nodo es un terminal retorna falso
        if (a.getTipo() == 'T' && !"/".equals(a.getDato())) {
            return false;
        }
        //Si es el simbolo de nulidad retorna verdadero
        if (a.getDato().equals("/")) {
            return true;
        }
        nodoLg b = a.getLigaH();
        //Si no tiene hijo
        if (b == null) {
            return false;
        }
        //Si el nodo hijo tiene "/" entonces es anulable
        if ("/".equals(b.getLigaD().getDato())) {
            return true;
        }
        //Si el lado derecho del nodo está compuesto por NT se debe verificar si son anulables
        //c se para en el primer hijo y los recorrerá. Cada hijo implica una producción distinta
        nodoLg c = b;
        //b recorre la produccion correspondiente 
        b = b.getLigaD();
        boolean bool = true;
        //contador que se usa como flag
        int cont = 0;
        //nodo r igual al que se analiza
        nodoLg r = a;
        while (c != null) {
            cont = 0;
            //recorremos las producciones
            while (b != null) {
                if (b.getTipo() == 'N') {
                    //r recorre la lista principal de NT
                    while (r != raiz) {
                        if (r.getDato().equals(b.getDato())) {
                            break;
                        }
                        //control para evitar nodo=null
                        if (r.getLigaD() == null) {
                            //Si no encontra a r en sus ligas derechas, entonces está en sus ligas izquierdas
                            while (r != raiz) {
                                if (r.getDato().equals(b.getDato())) {
                                    break;
                                }
                                r = r.getLigaI();
                            }
                            break;
                        }
                        //avanza con r
                        r = r.getLigaD();
                    }
                    if (r != raiz.getLigaD()) {
                        //Llamado recursivo para el NT que se analiza, si es anulable devuelve true y bool que contiene true se mantendrá en true
                        bool = bool && esAnulable(r);
                    }
                } else {
                    //Mismo llamado recursivo
                    bool = bool && esAnulable(b);
                }
                if (bool == false) {
                    //si el retorno del llamado recusivo es false, aumenta el contador
                    cont++;
                }
                //Avanza con b
                b = b.getLigaD();
            }
            if (bool == true) {
                //Si el bool se mantiene en true, el NT analizado es anulable
                return true;
            }
            //Avanza en los nodos hijos
            c = c.getLigaH();
            //bool nuevamente en true por si su valor habia cambiado
            bool = true;
            if (c != null) {
                //Avanza con b
                b = c.getLigaD();
            }
        }
        //Si el contador se mantuvo en 0 quiere decir que el NT es anulable
        return cont == 0;
    }
    //Devuelve un arraylist con las NT anulables
    public ArrayList anulables() {
        ArrayList<String> v = new ArrayList<>();
        //Nodo que se ubica sobre el primer nodo de la lista
        nodoLg reco = primer;
        reco = reco.getLigaD();
        //Recorremos los NT
        while (reco != null) {
            //Si es anulable lo agrega al arrayList
            if (esAnulable(reco)) {
                v.add(reco.getDato());
            }
            //avanza sobre los NT
            reco = reco.getLigaD();
        }
        return v;
    }
    //Retorna un ArrayList con los primeros de un NT
    public ArrayList primeros(nodoLg lg) {
        ArrayList<String> prim = new ArrayList<>();
        //Se ubica sobre el primer nodo y su hijo
        nodoLg p = lg.getLigaH();
        nodoLg recoH = p;
        nodoLg aux = raiz.getLigaD();
        //Hacemos recorrido sobre los hijos para ubicar los primeros
        while (recoH != null) {
            //avanza sobre la producción
            p = p.getLigaD();
            //Si el primer elemento es un terminal, entonces se agrega en el arraylist
            if (p.getTipo() == 'T') {
                //No agrega si el dato es el simbolo nulo
                if (!p.getDato().equals("/")) {
                    prim.add(p.getDato());
                }
            } else {//Si no es un T
                //El nodo auxiliar ubica el NT en la lista pricipal de los NT
                while (!aux.getDato().equals(p.getDato())) {
                    aux = aux.getLigaD();
                }
                //Analiza si es anulable
                if (esAnulable(aux)) {
                    //Analiza si el dato es el mismo que se está analizando 
                    if (!aux.getDato().equals(lg.getDato())) {
                        //Lleva la palabra "Prim" mas el dato que tiene el NT, simbolizando los primeros de ese NT
                        prim.add("Prim"+aux.getDato());
                        //Esto se hace para evitar un llamado recursivo que pueda terminar en un stack overflow
                    }
                    //Avanza en la producción
                    if (p.getLigaD() != null) {
                        //Analiza si el nodo es un NT, si es asi lo ubica en la lista principal de NT y lo agrega al arrayList
                        if (p.getLigaD().getTipo() == 'N') {
                            nodoLg a = raiz;
                            while (!a.getDato().equals(p.getLigaD().getDato())) {
                                a = a.getLigaD();
                            }
                            prim.add("Prim"+a.getDato());
                        } else {//Si no es NT simplemente agrega el T
                            prim.add(p.getLigaD().getDato());
                        }
                    }
                    
                } else {
                    //Si no es anulable agrega los primeros de ese NT
                    if (!aux.getDato().equals(lg.getDato())) {
                        prim.add("Prim"+aux.getDato());
                    }
                }
            }
            //Avanza en los hijos
            recoH = recoH.getLigaH();
            p = recoH;
        }
        return prim;
    }   
    //Devuelve los siguientes de un NT en un arraylist
    public ArrayList siguientes(nodoLg lg) {
        //Se declara el arraylist y se ubica un nodo en la liga derecha de la raiz
        ArrayList<String> sig = new ArrayList<>();
        nodoLg r = raiz.getLigaD();
        nodoLg recoP = r;
        //Para el nodo inicial, que representa el primer NT de la gramatica, se le lleva el simbolo "¬" de fin de secuencia
        if (r == lg) {
            sig.add("¬");
        }
        //Avanza en el hijo
        nodoLg reco = r.getLigaH();
        nodoLg aux = reco;
        nodoLg a;
        reco = reco.getLigaD();
        //Doble while para recorrer toda la lista generalizada
        while (recoP != null) {
            while (reco != null) {
                //Si encuentra el nodo que analizamos
                if (reco.getDato().equals(lg.getDato())) {
                    //Nodo auxiliar para avanzar sobre la producción
                    a = reco.getLigaD();
                    //Si a es nulo reco está en el final de la producción
                    if (a == null) {
                        //Si reco no es el NT que analizamos entonces llevamos la palabra "sig" mas el nombre del NT 
                        if (!reco.getDato().equals(recoP.getDato())) {                            
                            sig.add("sig" + recoP.getDato());
                            //sig + nombre del NT representa los siguientes de ese NT
                        }
                        break;
                    }
                    if (a.getTipo() == 'T') {
                        //Si es un T simplemente se agrega ese dato
                        sig.add(a.getDato());
                        break;
                    } else {//Si no es T se debe analizar el NT
                        while (!r.getDato().equals(a.getDato())) {
                            //Ubicamos el no terminal en la lista generalizada
                            r = r.getLigaD();
                        }
                        //Llamamos los anulables en un arraylist
                        ArrayList anul = anulables();
                        //Si ese NT es anulable
                        if (anul.contains(a.getDato())) {
                            // Si está en el final de la priducción
                            if (a.isFinDeLinea()) {
                                //Se agregan los siguientes del NT de la parte izquierda utilizando un método que entrega los primeros correctos 
                                sig.addAll(validarPrimeros(r));
                                if (!a.getDato().equals(lg.getDato())) {                                    
                                    sig.add("sig" + recoP.getDato());
                                }
                            } else {//Si no es fin de linea
                                //Llevamos los primeros de ese NT
                                sig.addAll(validarPrimeros(r));
                                a = a.getLigaD();
                                while (a != null) {//Se debe verificar si es anulable paa llevar los primeros del siguiente NT 
                                    if (anul.contains(a.getDato())) {
                                        sig.addAll(validarPrimeros(r));
                                        //Agrega los siguientes de los de NT que haya utilizando el while
                                        if (a.getLigaD() == null && !a.getDato().equals(recoP.getDato())) {
                                            sig.add("sig" + recoP.getDato());
                                        }
                                        a = a.getLigaD();
                                    } else {
                                        //Si es un terminal simplemente se agrega y rompe el whilw
                                        if (a.getTipo() == 'T') {
                                            sig.add(a.getDato());
                                            break;
                                        } else {
                                            //Si es NT se llevan sus primeros al arraylist
                                            sig.addAll(validarPrimeros(r));
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {//Si el NT no es anulable simplemente se llevan sus primeros al array
                            sig.addAll(validarPrimeros(r));
                            break;
                        }
                    }
                }
                //Ubica el NT que analizamos
                r = raiz.getLigaD();
                reco = reco.getLigaD();
            }
            //Avanza sobre los hijos
            if (aux.getLigaH() != null) {
                reco = aux;
                aux = reco.getLigaH();
                reco = aux.getLigaD();
            } else {//Si no quedan mos hijos, avanza sobre la lista
                recoP = recoP.getLigaD();
                if (recoP != null) {
                    aux = recoP.getLigaH();
                    reco = aux.getLigaD();
                }
            }
        }
        //Lineas de codigo para eliminar los repetidos que pueda haber en el array
        Set<String> hashSet = new HashSet<>(sig);
        sig.clear();
        sig.addAll(hashSet);
        return sig;
    }
    //Método para eliminar la palabra "Pri" que devuelve el método de los primeros y retorna los primeros de NT de manera correcta
    public ArrayList validarPrimeros(nodoLg lg){
        //Llevamos los primeros del nodo lg a un arraylist
        ArrayList a = primeros(lg);
        //Llamamos el metodo validar con el dato "Prim"
        ArrayList retorno = validar(a,"Prim",lg);
        return retorno;        
    }
    //Método para validar los primeros y siguientes de un nodo NT
    public ArrayList validar(ArrayList a, String sp,nodoLg lg)
    {       
        char c;
        ArrayList retorno;
        //Si la palabra clave ingresada corresponde a los siguientes
        if(sp.equals("sig")){
            //Se lleva al char c el dato g
            c='g';
            retorno = siguientes(lg);
        }
        else{//La palabra clave es prim correspondiente a los primeros
            c='m';
            retorno = primeros(lg);
        }
        boolean flag=true;
        
        nodoLg pos = raiz.getLigaD();
        //String para llevar los NT analizados
        String analizados=lg.getDato();
        do {
            //do while para recorrer el arraylist a ingresado como parametro
            for (Object e : a) {
                //Analiza cada dato en el array en busca de "sig" o de "Prim"
                if (e.toString().contains(sp)) {                    
                    int cont = e.toString().indexOf(c) + 1;
                    String aaa = e.toString().substring(cont);
                    //Si ya se analizó se retira del array a retornar
                    if (analizados.contains(aaa)) {
                        retorno.remove(e);
                    } else {
                        //Si no, entonces se busca el NT
                        while (!pos.getDato().equals(aaa)) {
                            pos = pos.getLigaD();
                        }
                        //Se remueve el objeto
                        retorno.remove(e);
                        if(sp.equals("sig")){
                            //Se añaden los siguientes si la palabra es sig
                            retorno.addAll(siguientes(pos));
                        }
                        else{
                            //se añaden los primeros si la palabra es Prim
                            retorno.addAll(primeros(pos));
                        }                        
                        //Flag cambia a false
                        flag = false;
                        //Se agrega el NT analizado a "analizados"
                        analizados = analizados + pos.getDato();
                        pos = raiz.getLigaD();
                    }
                }
            }
            String s = retorno.toString();
            //Si ya el array no tiene las palabras sig o Prim flag cambia a true
            if (!s.contains(sp)) {
                flag = true;
            } else {//Si no, entonces limpia el array y agrega los elementos del retorno para seguir procesando
                a.clear();
                a.addAll(retorno);
            }
        } while (flag != true);
        //Elimina los repetidos
        Set<String> hashSet = new HashSet<>(retorno);
        retorno.clear();
        retorno.addAll(hashSet);        
        return retorno;        
        
    }
    public ArrayList validarSiguientes(nodoLg lg) {
        ArrayList a = siguientes(lg);
        ArrayList sig = validar(a, "sig", lg);
        return sig;
    }
    //Retorna la seleccion en un arrayList. 
    //Cada posición en el arrayList corresponde a la selección de una producción en concreto
    public ArrayList getSeleccion() {
        //Asignaciones para rerridos
        nodoLg recoP = raiz;
        nodoLg r = recoP;
        recoP = recoP.getLigaD();
        nodoLg recoH = recoP.getLigaH();
        nodoLg aux = recoH;
        recoH = recoH.getLigaD();
        //Llevamos los anulables
        ArrayList anul = anulables();
        //Se le lleva un tamaño definido al array privado de selección
        seleccion = new ArrayList<>(getTamano());
        String ss = "";
        //Recorrido
        while (recoP != null) {
            //Si es un terminal se debe definir si es /
            if (recoH.getTipo() == 'T') {
                if (recoH.getDato().equals("/")) {
                    // Si es "/" se añaden los siguientes del NT de la parte izquierda
                    seleccion.add(recoH.getProduccion() - 1, validarSiguientes(recoP));
                } else {
                    //Si no, se añade el terminal
                    seleccion.add(recoH.getProduccion() - 1, recoH.getDato());
                }
            } else {
                //Si es NT se debe definir si es anulable
                while (!r.getDato().equals(recoH.getDato())) {
                    r = r.getLigaD();
                }
                if (anul.contains(recoH.getDato())) {
                    //Si es anulable se deben llevar los primeros de ese NT + los que sigan
                    //Se hacen validaciones netamente sintacticas
                    //Se añaden el string s que contendrá el dato de interés
                    String s = validarPrimeros(r).toString();
                    
                    s = s.replace(']', ',');
                    if(recoH.isFinDeLinea()){
                        s= s+ validarSiguientes(recoP).toString();                        
                    }
                    r = raiz.getLigaD();
                    if (recoH.getLigaD() != null) {
                        nodoLg a = recoH.getLigaD();
                        while (a != null) {
                            if (a.getTipo() == 'T') {
                                if(!s.contains(a.getDato())){
                                    s = s + a.getDato();
                                    s = s + "]";
                                    seleccion.add(recoH.getProduccion() - 1, s);
                                    s = "";                                    
                                }
                                else{
                                    s=s.substring(0, s.length()-1);
                                    s= s+"]";
                                    seleccion.add(recoH.getProduccion()-1,s);
                                    s="";
                                }
                                break;
                            }
                            while (!r.getDato().equals(a.getDato())) {
                                r = r.getLigaD();
                            }
                            ss = validarPrimeros(r).toString().replace('[', ' ');

                            s = s + ss;

                            if (anul.contains(a.getDato())) {
                                a = a.getLigaD();
                                if (a == null) {
                                    s = s + validarSiguientes(recoP).toString();
                                }
                            } else {
                                break;
                            }
                        }
                        if (!s.equals("")) {
                            //Elimina los espacios en caso de haberlos y añade el string s
                            s = s.replaceAll(" ", "");
                            seleccion.add(recoH.getProduccion() - 1, s);
                            s="";
                            
                        }
                    }                    
                        if (!s.equals("")) {
                            //Elimina los espacios en caso de haberlos y añade el string s
                            s = s.replaceAll(" ", "");
                            seleccion.add(recoH.getProduccion() - 1, s);
                        }
                } else { // si no es anulable simplemente se agregan los primeros
                    seleccion.add(recoH.getProduccion() - 1, validarPrimeros(r));
                    r=raiz.getLigaD();
                }
            }
            //Anvanza en los hijos
            if (aux.getLigaH() != null) {
                aux = aux.getLigaH();
                recoH = aux.getLigaD();
            } else {//Avanza sobre la lista de NT
                recoP = recoP.getLigaD();
                if (recoP != null) {
                    recoH = recoP.getLigaH();
                    aux = recoH;
                    recoH = recoH.getLigaD();
                }
            }
        }
        return seleccion;
    }
    //Método para obtener la selección de una producción en concreto
    public String seleccionPorProduccion(int prod) {
        String retorno;
        //Traemos el arraylist con todas las selecciones  
        seleccion = getSeleccion();
        ArrayList a = seleccion;
        //Ubicamos la producción que queremos
        retorno = a.get(prod - 1).toString();
        //Llevamos la selección de esa producción a un arreglo de char
        char[] c = retorno.toCharArray();
        //Lineas de código para sintaxis
        String s = "[";
        for (char d : c) {
            if (d == '[' || d == ',' || d == ']' || d == ' ') {
            } else {
                s = s + d + " ";
            }
        }
        //Reemplaza los espacios vacios por comas
        s = s.replaceAll(" ", ",");
        retorno = s.substring(0, s.length() - 1).concat("]");
        return retorno;
    }

    public nodoLg getRaiz() {
        return raiz;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public nodoLg getPosicion() {
        return posicion;
    }

    public void setPosicion(nodoLg posicion) {
        this.posicion = posicion;
    }

    public nodoLg getPosAux() {
        return posAux;
    }

    public void setPosAux(nodoLg posAux) {
        this.posAux = posAux;
    }

    public nodoLg getPrimer() {
        return primer;
    }

    public void setPrimer(nodoLg primer) {
        this.primer = primer;
    }

    public nodoLg getUltimo() {
        return ultimo;
    }

    public void setUltimo(nodoLg ultimo) {
        this.ultimo = ultimo;
    }

}
