package ar.edu.uns.cs.ed.tdas.tdagrafo;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;


/**
 * Interface Vertex
 * @author Cátedra de Estructuras de Datos, Departamento de Cs. e Ing. de la Computación, UNS.
 */
public class Vertice<V,E> implements Vertex<V> {
    private V rotulo;
    private PositionList<Arco<V,E>> adyacentes;
    private Position<Vertice<V,E>> posicionEnNodos;
    
    public Vertice( V rotulo ) {
        this.rotulo = rotulo;
        adyacentes = new ListaDoblementeEnlazada<Arco<V,E>>();
    }
    public V element() { 
        return rotulo; 
    }
    // Setters y getters
    public void setRotulo(V nuevoRotulo) {
        rotulo=nuevoRotulo;
    }
    public PositionList<Arco<V,E>> getAdyacentes() { 
        return adyacentes;
    }
    public void setPosicionEnNodos(Position<Vertice<V,E>> p ) {
        posicionEnNodos=p;
    }
    public Position<Vertice<V,E>> getPosicionEnNodos() {
        return posicionEnNodos;
    }
}