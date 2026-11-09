package ar.edu.uns.cs.ed.tdas.tdalista;

import ar.edu.uns.cs.ed.tdas.Position;

public class DNodo <E> implements Position<E> {
    public DNodo<E> anterior;
    public DNodo<E> siguiente;
    public E elemento;

    public DNodo(E e, DNodo<E> sig, DNodo<E> ant){
        this.elemento=e;
        this.anterior=ant;
        this.siguiente=sig;
    }    
    public DNodo(E e){
        this.elemento=e;
        this.anterior=null;
        this.siguiente=null;
    }   
    @Override
    public E element() {
        return this.elemento;
    }
    public void setElemento(E e){
        this.elemento=e;
    }
    public DNodo<E> getAnterior() {
        return anterior;
    }
    public void setAnterior(DNodo<E> anterior) {
        this.anterior = anterior;
    }
    public DNodo<E> getSiguiente() {
        return siguiente;
    }
    public void setSiguiente(DNodo<E> siguiente) {
        this.siguiente = siguiente;
    }


}
