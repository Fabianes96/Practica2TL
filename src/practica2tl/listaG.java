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

    public listaG() {
        nodoLg x = new nodoLg("*");
        posicion = raiz = primer = ultimo = x;
    }

    public boolean isEnd(nodoLg l) {
        return l == null;
    }

    public boolean esVacia(listaG lista) {
        nodoLg p = lista.getPrimer().getLigaD();
        return p == null;
    }

    public void conectarNodo(String dato, char tipo, boolean fin, int prod) {                          //Conecta un nuevo nodo hacia la derecha
        nodoLg dat = new nodoLg(dato);
        dat.setTipo(tipo);
        dat.setFinDeLinea(fin);
        posicion.setLigaD(dat);
        dat.setLigaI(posicion);
        posicion = dat;
        dat.setProduccion(prod);
    }

    public boolean encontrarDato(String d) {                                                 //Recorre la línea principal desde la raíz para buscar un NT
        posicion = raiz;
        while (posicion.getLigaD() != null) {
            posicion = posicion.getLigaD();
            if (posicion.getDato().equals(d)) {
                return true;
            }
        }
        return false;
    }

    public void agregarNodoCabeza() {                                                        //Conecta un nodo cabeza como hijo de un nodo cualquiera
        while (posicion.getLigaH() != null) {                                                    //Si existen más nodos cabeza hacia abajo
            posicion = posicion.getLigaH();
        }
        nodoLg nodocabeza = new nodoLg("*");
        posicion.setLigaH(nodocabeza);
        posicion = nodocabeza;

    }

    public void insertarNodo(String dato, char tipo, boolean finDeLinea, boolean padre, int prod) {    //Hace las validaciones para insertar T y NT a la lista
        if (padre == false) {                                                                   //Si no tiene padre entonces se encuentra en la parte izquierda de la producción
            if (encontrarDato(dato)) {                                                        //Si encuentra que el NT ya existe, entonces actualiza posicion para agregar ahí
                posicion = posicion.getLigaH();
            } else {                                                                           //Si no existe el NT, entonces lo agrega y le añade un nodo cabeza para empezar a agregar ahí
                conectarNodo(dato, tipo, finDeLinea, prod);
                agregarNodoCabeza();
            }
        } else {                                                                             //Si tiene padre es de la parte derecha de la producción
            if (posicion.getLigaD() == null) {                                                //Si no hay datos insertados a la derecha
                if (posicion.isFinDeLinea()) {                                                //Si la línea termina ahí, entonces añade nodo cabeza para seguir escribiendo
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea, prod);
                } else {                                                                     //Si aún no termina de escribir la línea entonces sigue añadiendo hacia la derecha
                    conectarNodo(dato, tipo, finDeLinea, prod);
                }
            } else {                                                                          //Si ya hay datos a la derecha
//                if(posicion.getLigaD().getDato().equals(dato)){                             //Compara si el dato a ingresar es igual al que ya está guardado y actualiza posición
//                    posicion=posicion.getLigaD();
//                    if(finDeLinea && posicion.getLigaD()!=null){                            //Si la producción que se está escribiendo es más corta que la existente
//                        reacomodarNodo();
//                    }
//                } else{                                                                     //Si es diferente, entonces añade una sub-línea
                agregarNodoCabeza();
                conectarNodo(dato, tipo, finDeLinea, prod);
                //}
            }
        }
    }

    public boolean esAnulable(nodoLg a) {
        if (a.getTipo() == 'T' && !"/".equals(a.getDato())) {
            return false;
        }
        if (a.getDato().equals("/")) {
            return true;
        }
        nodoLg b = a.getLigaH();
        if (b == null) {
            return false;
        }
        if ("/".equals(b.getLigaD().getDato())) {
            return true;
        }
        nodoLg c = b;
        b = b.getLigaD();
        boolean bool = true;
        int cont = 0;
        nodoLg r = a;
        while (c != null) {
            cont = 0;
            while (b != null) {
                if (b.getTipo() == 'N') {

                    while (r != raiz) {
                        if (r.getDato().equals(b.getDato())) {
                            break;
                        }
                        if (r.getLigaD() == null) {
                            while (r != raiz) {
                                if (r.getDato().equals(b.getDato())) {
                                    break;
                                }
                                r = r.getLigaI();
                            }
                            break;
                        }
                        r = r.getLigaD();
                    }
                    if (r != raiz.getLigaD()) {
                        bool = bool && esAnulable(r);
                    }

                } else {
                    bool = bool && esAnulable(b);
                }
                if (bool == false) {
                    cont++;
                }
                b = b.getLigaD();
            }
            if (bool == true) {
                return true;
            }
            c = c.getLigaH();

            bool = true;
            if (c != null) {
                b = c.getLigaD();
            }
        }
        return cont == 0;
    }

    public ArrayList anulables() {
        ArrayList<String> v = new ArrayList<>();
        nodoLg reco = primer;
        reco = reco.getLigaD();
        while (reco != null) {
            if (esAnulable(reco)) {
                v.add(reco.getDato());
            }
            reco = reco.getLigaD();
        }
        return v;
    }

    public ArrayList primeros(nodoLg lg) {
        ArrayList<String> prim = new ArrayList<>();
        nodoLg p = lg.getLigaH();
        nodoLg recoH = p;
        nodoLg aux = raiz.getLigaD();

        while (recoH != null) {
            p = p.getLigaD();
            if (p.getTipo() == 'T') {
                if (!p.getDato().equals("/")) {
                    prim.add(p.getDato());
                }
            } else {
                while (!aux.getDato().equals(p.getDato())) {
                    aux = aux.getLigaD();
                }
                if (esAnulable(aux)) {

                    if (!aux.getDato().equals(lg.getDato())) {
                        prim.addAll(primeros(aux));
                    }
                    if (p.getLigaD() != null) {
                        if (p.getLigaD().getTipo() == 'N') {
                            nodoLg a = raiz;
                            while (!a.getDato().equals(p.getLigaD().getDato())) {
                                a = a.getLigaD();
                            }
                            prim.addAll(primeros(a));
                        } else {
                            prim.add(p.getLigaD().getDato());
                        }
                    }
                } else {
                    if (!aux.getDato().equals(lg.getDato())) {
                        prim.addAll(primeros(aux));
                    }
                }
            }
            recoH = recoH.getLigaH();
            p = recoH;
        }
        return prim;
    }
     public boolean hayRamificaciones(nodoLg pos){
        do {            
            if(pos.getLigaH()!=null){
                return false;
            }
            if(pos.isFinDeLinea() && pos.getLigaD()!=null){
                return false;
            }
            pos = pos.getLigaD();
            
        } while (pos.getLigaD()!=null);
        return true;
    }

    public ArrayList siguientes(nodoLg lg) {
        
        ArrayList<String> sig = new ArrayList<>();
        nodoLg r = raiz.getLigaD();
        nodoLg recoP = r;
        if (r == lg) {
            sig.add("¬");
        }
        nodoLg reco = r.getLigaH();
        nodoLg aux = reco;
        nodoLg a;
        reco = reco.getLigaD();
        while (recoP != null) {
            while (reco != null) {
                if (reco.getDato().equals(lg.getDato())) {
                    a = reco.getLigaD();
                    if (a == null) {
                        if (!reco.getDato().equals(recoP.getDato())) {
                            sig.add("sig" + recoP.getDato());
                        }
                        break;
                    }
                    if (a.getTipo() == 'T') {
                        sig.add(a.getDato());
                        break;
                    } else {
                        while (!r.getDato().equals(a.getDato())) { //Ubicamos el no terminal en la lista generalizada
                            r = r.getLigaD();
                        }
                        ArrayList anul = anulables();
                        if (anul.contains(a.getDato())) {
                            if (a.isFinDeLinea()) {
                                sig.addAll(primeros(r));
                                if (!a.getDato().equals(lg.getDato())) {
                                    sig.add("sig" + recoP.getDato());
                                }
                            } else {
                                sig.addAll(primeros(r));
                                a = a.getLigaD();
                                while (a != null) {
                                    if (anul.contains(a.getDato())) {
                                        sig.addAll(primeros(r));
                                        if (a.getLigaD() == null && !a.getDato().equals(recoP.getDato())) {
                                            sig.add("sig" + recoP.getDato());
                                        }
                                        a = a.getLigaD();
                                    } else {
                                        if (a.getTipo() == 'T') {
                                            sig.add(a.getDato());
                                            break;
                                        } else {
                                            sig.addAll(primeros(r));
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            sig.addAll(primeros(r));
                            break;
                        }
                    }
                }
                r = raiz.getLigaD();
                reco = reco.getLigaD();
            }
            if (aux.getLigaH() != null) {
                reco = aux;
                aux = reco.getLigaH();
                reco = aux.getLigaD();
            } else {
                recoP = recoP.getLigaD();
                if (recoP != null) {
                    aux = recoP.getLigaH();
                    reco = aux.getLigaD();
                }
            }
        }
        
        Set<String> hashSet = new HashSet<>(sig);
        sig.clear();
        sig.addAll(hashSet);
        return sig;
    }
    
    public ArrayList validarSiguientes(nodoLg lg){
        ArrayList a = siguientes(lg);
        ArrayList otro = siguientes(lg);
        nodoLg pos = raiz.getLigaD();
        String sig ="sig";
        String analizados=lg.getDato();
        boolean flag = true;
        do {  
            
            for (Object e : a) {
                if(e.toString().contains(sig))
                {                    
                    int c = e.toString().indexOf('g')+1;
                    String aaa= e.toString().substring(c);
                    if(analizados.contains(aaa))
                    {
                        otro.remove(e);
                    }
                    else
                    {
                        while(!pos.getDato().equals(aaa)){
                            pos=pos.getLigaD();
                        }
                        otro.remove(e);
                        otro.addAll(siguientes(pos));
                        flag= false;
                        analizados=analizados + pos.getDato(); 
                        pos=raiz.getLigaD();
                                               
                    }
                }
            }            
            String c =otro.toString();            
            if(!c.contains("sig"))
            {
                flag=true;
            }
            else
            {                
                a.clear();
                a.addAll(otro);
            }
        } while (flag!=true);        
        
        Set<String> hashSet = new HashSet<>(otro);
        otro.clear();
        otro.addAll(hashSet);       
        
        return otro;
    }  

    public ArrayList getSeleccion() {
        nodoLg recoP = raiz;
        nodoLg r = recoP;
        recoP = recoP.getLigaD();
        nodoLg recoH = recoP.getLigaH();
        nodoLg aux = recoH;
        recoH = recoH.getLigaD();
        ArrayList anul = anulables();
        seleccion= new ArrayList<>(getTamano());
        String ss="";
        while (recoP != null) {

            if (recoH.getTipo() == 'T') {
                if (recoH.getDato().equals("/")) {
                    seleccion.addAll(recoH.getProduccion()-1, siguientes(recoP));
                } else {
                    seleccion.add(recoH.getProduccion()-1,recoH.getDato());
                }
            } else {
                while (!r.getDato().equals(recoH.getDato())) {
                    r = r.getLigaD();
                }
                if (anul.contains(recoH.getDato())) {
                    String s= primeros(r).toString();
                    s=s.replace(']', '-');                    
                    r = raiz.getLigaD();
                    if (recoH.getLigaD() != null) {
                        nodoLg a = recoH.getLigaD();
                        while (a != null) {
                            if(a.getTipo()=='T')
                            {
                                s = s+a.getDato();
                                s= s+"]";
                                seleccion.add(recoH.getProduccion()-1,s);
                                s="";
                                break;
                            }   
                            while (!r.getDato().equals(a.getDato())) {
                                r = r.getLigaD();
                            }
                            ss= primeros(r).toString().replace('[', ' ');
                            
                            s = s+ ss;
                            
                            if (anul.contains(a.getDato())) {
                                a = a.getLigaD();
                                if(a==null)
                                {
                                    s= s+ siguientes(recoP).toString();
                                }
                            } else {
                                break;
                            }
                        }
                        if(!s.equals("")){
                            s=s.replaceAll(" ", "");
                            seleccion.add(recoH.getProduccion()-1,s);
                        }
                    }
                } else {
                    seleccion.add(recoH.getProduccion()-1, primeros(r));
                }
            }
            if (aux.getLigaH() != null) {
                aux=aux.getLigaH();
                recoH=aux.getLigaD();
            }
            else{
                recoP=recoP.getLigaD();
                if(recoP!=null){
                    recoH = recoP.getLigaH();
                    aux = recoH;
                    recoH = recoH.getLigaD();
                }
            }
        }
        return seleccion;
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
