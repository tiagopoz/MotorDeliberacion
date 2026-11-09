package ar.edu.uns.cs.ed.tdas.tdacola;

import ar.edu.uns.cs.ed.tdas.tdapila.TDAPila;
import ar.edu.uns.cs.ed.tdas.tdapila.Stack;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyQueueException;

public class ColaConPila<E> implements Queue<E>{
private Stack<E> pila1;
private Stack<E> pila2;
public ColaConPila(){
    pila1= new TDAPila<>();
    pila2=new TDAPila<>();
}
public void enqueue(E e){
    if (e==null)throw new IllegalArgumentException("no se permite null");
    pila1.push(e);
}
public E dequeue(){
    if (this.isEmpty()) throw new EmptyQueueException("la cola esta vacia");
    if(pila2.isEmpty()){
        while (!pila1.isEmpty()){//
            pila2.push(pila1.pop());//esto se hace para transferir los elementos de pila1 a pila2, lo que invierte el orden de los elementos y permite que el elemento que estaba al frente de la cola (el último elemento agregado a pila1) quede en la parte superior de pila2, listo para ser removido con el método pop().
        }
    }
    return pila2.pop();

}
public E front(){
    if (this.isEmpty()) throw new EmptyQueueException("la cola esta vacia");
    if(pila2.isEmpty()){
        while (!pila1.isEmpty()){
            pila2.push(pila1.pop());//esto se hace para transferir los elementos de pila1 a pila2, lo que invierte el orden de los elementos y permite que el elemento que estaba al frente de la cola (el último elemento agregado a pila1) quede en la parte superior de pila2, listo para ser inspeccionado con el método peek().
        }
    }
    return pila2.top();
}
public boolean isEmpty(){
    return pila1.isEmpty() && pila2.isEmpty();

}
public int size(){
    return pila1.size() + pila2.size();//esto se hace para calcular el tamaño total de la cola sumando el número de elementos en ambas pilas, ya que los elementos de la cola pueden estar distribuidos entre las dos pilas dependiendo de las operaciones realizadas (enqueue y dequeue).
}
}