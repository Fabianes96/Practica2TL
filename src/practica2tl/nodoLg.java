/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica2tl;

/**
 *
 * @author FABIAN
 */
public class nodoLg {     
    /*
        Configuración del nodo con:
        1. Dato
        2. Tipo (T o NT)
        3. Tres ligas (izquierda, derecha, hijo)
        4. Fin de línea
        5. Número de la producción a la que pertenece
        6. Nombre del No terminal al que pertenece
    */
    
    private String dato;
    private char tipo;
    private nodoLg ligaD;
    private nodoLg ligaI;
    private nodoLg ligaH;
    private boolean finDeLinea;
    private int produccion;
    private String nomProduccion;
    public nodoLg(String dato) {
        this.dato = dato;
        ligaD=ligaI=ligaH=null;
        tipo=' ';
    }

    public String getDato() {
        return dato;
    }

    public int getProduccion() {
        return produccion;
    }

    public void setProduccion(int produccion) {
        this.produccion = produccion;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public nodoLg getLigaD() {
        return ligaD;
    }

    public void setLigaD(nodoLg ligaD) {
        this.ligaD = ligaD;
    }

    public nodoLg getLigaI() {
        return ligaI;
    }

    public void setLigaI(nodoLg ligaI) {
        this.ligaI = ligaI;
    }

    public nodoLg getLigaH() {
        return ligaH;
    }

    public String getNomProduccion() {
        return nomProduccion;
    }

    public void setNomProduccion(String nomProduccion) {
        this.nomProduccion = nomProduccion;
    }

    public void setLigaH(nodoLg ligaH) {
        this.ligaH = ligaH;
    }

    public boolean isFinDeLinea() {
        return finDeLinea;
    }

    public void setFinDeLinea(boolean finDeLinea) {
        this.finDeLinea = finDeLinea;
    }
    
}
