package ar.edu.uns.cs.ed.tdas.tdaarbolbinario;

import ar.edu.uns.cs.ed.tdas.Position;

public class BTNodo<E> implements Position<E> {
    private E elemento;
    private BTNodo<E> padre;
    private BTNodo<E> hijoD;
    private BTNodo<E> hijoI;
    
    public BTNodo(E e, BTNodo<E> p){
        elemento=e;
        padre=p;
        hijoI=null;
        hijoD=null;
    }
    public BTNodo(E e){
        elemento=e;
        padre=null;
        hijoD=null;
        hijoI=null;
    }
    @Override
    public E element() {
    return elemento;
    }
    public BTNodo<E> getPadre(){
        return padre;
    }
    public BTNodo<E> getLeft(){
        return hijoI;
    }
    public BTNodo<E> getRight(){
        return hijoD;
    }
    public void setElemento(E e){
        elemento=e;
    }
    public void setPadre(BTNodo<E> p){
        padre=p;
    }
    public void setLeft(BTNodo<E> l){
        hijoI=l;
    }
    public void setRight(BTNodo<E> r){
        hijoD=r;
    }

}