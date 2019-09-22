/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl;

import java.util.ArrayList;


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

    public listaG() {
        nodoLg x = new nodoLg("*");                                                        
        posicion=raiz=primer=ultimo=x; 
        
    }    
    
    public boolean isEnd(nodoLg l){
        return l==null;
    }
    
    public boolean esVacia(listaG lista){
        nodoLg p=lista.getPrimer().getLigaD();
        return p==null;
    }    
    public void conectarNodo(String dato, char tipo, boolean fin){                          //Conecta un nuevo nodo hacia la derecha
        nodoLg dat=new nodoLg(dato);        
        dat.setTipo(tipo);
        dat.setFinDeLinea(fin);
        posicion.setLigaD(dat);
        dat.setLigaI(posicion);
        posicion=dat;
    }
    public boolean encontrarDato(String d){                                                 //Recorre la línea principal desde la raíz para buscar un NT
        posicion=raiz;
        while(posicion.getLigaD() != null){
            posicion=posicion.getLigaD();
            if(posicion.getDato().equals(d)){
               return true;
            }
        }
        return false;
    }
    public void agregarNodoCabeza(){                                                        //Conecta un nodo cabeza como hijo de un nodo cualquiera
        while(posicion.getLigaH()!=null){                                                    //Si existen más nodos cabeza hacia abajo
            posicion = posicion.getLigaH();
        }
        nodoLg nodocabeza=new nodoLg("*");
        posicion.setLigaH(nodocabeza);
        posicion=nodocabeza;
        
    }
    
    
    public void insertarNodo(String dato, char tipo, boolean finDeLinea, boolean padre){    //Hace las validaciones para insertar T y NT a la lista
        if(padre==false){                                                                   //Si no tiene padre entonces se encuentra en la parte izquierda de la producción
            if(encontrarDato(dato)){                                                        //Si encuentra que el NT ya existe, entonces actualiza posicion para agregar ahí
                posicion=posicion.getLigaH();
            }
            else{                                                                           //Si no existe el NT, entonces lo agrega y le añade un nodo cabeza para empezar a agregar ahí
                conectarNodo(dato, tipo, finDeLinea);
                agregarNodoCabeza();
            }
        } else{                                                                             //Si tiene padre es de la parte derecha de la producción
            if(posicion.getLigaD() == null){                                                //Si no hay datos insertados a la derecha
                if(posicion.isFinDeLinea()){                                                //Si la línea termina ahí, entonces añade nodo cabeza para seguir escribiendo
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea);
                } else{                                                                     //Si aún no termina de escribir la línea entonces sigue añadiendo hacia la derecha
                    conectarNodo(dato, tipo, finDeLinea);
                }
            }else{                                                                          //Si ya hay datos a la derecha
//                if(posicion.getLigaD().getDato().equals(dato)){                             //Compara si el dato a ingresar es igual al que ya está guardado y actualiza posición
//                    posicion=posicion.getLigaD();
//                    if(finDeLinea && posicion.getLigaD()!=null){                            //Si la producción que se está escribiendo es más corta que la existente
//                        reacomodarNodo();
//                    }
//                } else{                                                                     //Si es diferente, entonces añade una sub-línea
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea);
                //}
            }
        }
    }
    
    public boolean esAnulable(nodoLg a)
    {
        if(a.getTipo()=='T' && !"/".equals(a.getDato())){
            return false;
        }
        if(a.getDato().equals("/"))
        {
            return true;
        }
        nodoLg b= a.getLigaH();
        if(b==null)
        {
            return false;
        }
        if("/".equals(b.getLigaD().getDato()))
        {
            return true;
        }
        nodoLg c= b;
        b=b.getLigaD();  
        boolean bool = true;
        int cont =0;
        nodoLg r = a;
        while(c!=null)
        {       
            cont =0;
            while(b!=null)
            {                
                if(b.getTipo()=='N'){

                    while(r!=raiz)
                    {
                        if(r.getDato().equals(b.getDato()))
                        {
                            break;
                        }                    
                        if(r.getLigaD()==null)
                        {
                            while(r!=raiz)
                            {
                                if(r.getDato().equals(b.getDato())){
                                    break;
                                }
                                r=r.getLigaI();
                            }break;
                        }
                        r=r.getLigaD();
                    }
                if(r!=raiz.getLigaD())
                {
                    bool = bool && esAnulable(r);
                }
                
                }
                else
                {
                    bool = bool && esAnulable(b);
                }                
                if(bool == false)
                {
                    cont++;
                }
                b=b.getLigaD();
            }
            if(bool==true)
            {
               return true;
            }
            c=c.getLigaH();
            
            bool = true;
            if(c!= null)
            {
                b= c.getLigaD();
            }
        }           
        return cont==0;
    }
    
    public ArrayList anulables()
    {
        ArrayList <String> v = new ArrayList<>();
        nodoLg reco = primer;
        reco = reco.getLigaD();
        while(reco!= null)
        {
            if(esAnulable(reco))
            {
                v.add(reco.getDato());
            }
            reco = reco.getLigaD();
        }
        return v;
    }
    public ArrayList primeros(nodoLg lg)
    {
        ArrayList<String> prim= new ArrayList<>();
        nodoLg p = lg.getLigaH();
        nodoLg recoH = p;
        nodoLg aux = raiz.getLigaD();
        
        while(recoH!=null)
        {
            p = p.getLigaD();
            if(p.getTipo()=='T')
            {
                if(p.getDato().equals("/")){
                    prim.add("");
                }
                else{
                    prim.add(p.getDato());
                }
            }
            else
            {
                while(!aux.getDato().equals(p.getDato()))
                {
                    aux=aux.getLigaD();
                }
                if(esAnulable(aux)){
                    
                    if(!aux.getDato().equals(lg.getDato()))
                    {
                        prim.addAll(primeros(aux));
                    }                    
                    if(p.getLigaD()!=null)
                    {
                        if(p.getLigaD().getTipo()=='N')
                        {
                            nodoLg a= raiz;
                            while(!a.getDato().equals(p.getLigaD().getDato()))
                            {
                                a=a.getLigaD();
                            }
                            prim.addAll(primeros(a));
                        }
                        else
                        {
                            prim.add(p.getLigaD().getDato());
                        }                        
                    }                    
                }
                else{
                    if(!aux.getDato().equals(lg.getDato()))
                    {
                        prim.addAll(primeros(aux));
                    }
                    else
                    {
                        prim.add("");
                    }
                }                
            }
            recoH=recoH.getLigaH();
            p=recoH;           
        }        
        return prim;
    }
    
    

    public nodoLg getRaiz() {
        return raiz;
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
