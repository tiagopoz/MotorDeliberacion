package ar.edu.uns.cs.ed.tdas.tdalista;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
@SuppressWarnings("rawtypes")
public class ListaDoblementeEnlazada<E> implements PositionList<E>     {
    private int cant;
    private DNodo<E> head;
    private DNodo<E> tail;
public ListaDoblementeEnlazada(){
    cant=0;
    head=new DNodo<>(null);

    tail=new DNodo<>(null,null,head);
    head.setSiguiente(tail);
    tail.setAnterior(head);
}
    @Override
    public int size() {
    return cant;
    }

    @Override
    public boolean isEmpty() {
        return cant==0;
    }

    @Override
    public Position<E> first() {
        if(isEmpty())throw new EmptyListException("lista vacia");
        return head.getSiguiente();
       }

    @Override
    public Position<E> last() {
        if(isEmpty()) throw new EmptyListException("lista vacia");
        return tail.getAnterior();
    }

    @Override
    public Position<E> next(Position<E> p) {
        DNodo<E> n= checkPosition(p);
        if(n.getSiguiente()==tail)throw new BoundaryViolationException("siguiente del ultimo");
        return n.getSiguiente(); 
    }
   

    @Override
    public Position<E> prev(Position<E> p) {
        DNodo<E> n= checkPosition(p);
        if(n.getAnterior()==head)throw new BoundaryViolationException("anterior del primero");
        return n.getAnterior();
    }

    @Override
    public void addFirst (E element) {
    DNodo<E> pviejo= head.getSiguiente();
    DNodo<E> nuevo= new DNodo<>(element,pviejo,head);
    head.setSiguiente(nuevo); 
    pviejo.setAnterior(nuevo); 
    cant++;
        }

    @Override
    public void addLast (E element) {
    DNodo<E> pviejo= tail.getAnterior();
    DNodo<E> nuevo= new DNodo<>(element,tail,pviejo);
        tail.setAnterior(nuevo);
        pviejo.setSiguiente(nuevo);
        cant++;
    }

    @Override
    public void addAfter(Position p, E element) {
    DNodo<E> ag= checkPosition(p);
    DNodo<E> nuevo= new DNodo(element,ag.getSiguiente(),ag);
        ag.getSiguiente().setAnterior(nuevo);
        ag.setSiguiente(nuevo);
        cant++;
    }

    @Override
    public void addBefore(Position p, E element) {
    DNodo<E> ag=checkPosition(p);
    DNodo<E> nuevo= new DNodo(element, ag, ag.getAnterior());
    ag.getAnterior().setSiguiente(nuevo);
    ag.setAnterior(nuevo);
    cant++;
}

    @Override
    public E remove(Position p) {
    DNodo<E> nodo= checkPosition(p);
    nodo.getAnterior().setSiguiente(nodo.getSiguiente());
    nodo.getSiguiente().setAnterior(nodo.getAnterior());
    E elemento = nodo.element();
    nodo.setElemento(null);
    cant--;
    return elemento;    
}

    @Override
    public E set(Position p, E element) {
    DNodo<E> nodo= checkPosition(p);
    E eviejo =nodo.element();
    nodo.setElemento(element);
    return eviejo;
    }

    @Override
    public Iterator<E> iterator() {
    return new ElementIterator(this);
    }
    @Override
    public Iterable<Position<E>> positions() {
        ListaDoblementeEnlazada<Position<E>> lista = new ListaDoblementeEnlazada<>();
        DNodo<E> actual = this.head.getSiguiente();
        while(actual!= tail){
            lista.addLast(actual);
            actual=actual.getSiguiente();
        }
        return lista;
    }
    public class ElementIterator implements Iterator<E>{
        private Position<E> cursor;
        private PositionList<E> lista;
        public ElementIterator(PositionList<E> l){
            this.lista = l;
            try{
                this.cursor= l.first();
            }
            catch(EmptyListException e){
                this.cursor=null;
            }
        }
        @Override
        public boolean hasNext() {
            return cursor!=null;
        }

        @Override
        public E next() {
            if(cursor==null) throw new NoSuchElementException("no hay mas elementos");
            E elemento = cursor.element();
            if(cursor!=last())
                cursor=lista.next(cursor);
            else
                cursor=null;
            return elemento;
        } 
    }
private DNodo<E> checkPosition(Position <E> p){
    try{
        if(p==null)throw new InvalidPositionException("posicion nula");
        if(p.element()==null) throw new InvalidPositionException("posicion eliminada previamente");
        return (DNodo<E>)p;// este casteo sirve para convertir la posición genérica a un nodo específico de la lista enlazada, 
            // lo que permite acceder a los atributos y métodos específicos de la clase DNodo, como el acceso a los nodos anterior y siguiente, 
            // así como al elemento almacenado en el nodo.        
        } catch (ClassCastException e){// vengo aca porque fallo el casting a NODO
            throw new InvalidPositionException("p no es un nodo de lista");
        }
    }
public void SegundoyAnteultimo(E e1, E e2){
    if(isEmpty()){
        this.addFirst(e2);
        this.addLast(e1);
    }
    if(size() == 1) throw new IllegalArgumentException("el size de la lista debe ser distinta de 1");
        Position<E> p1= first();
        Position<E> p2=last();
        this.addAfter(p1, e1);
        this.addBefore(p2, e2);
}
public boolean aparece (PositionList<E> l , E e1){
    if(l==null || e1 == null) return false;
    Iterator<E> it= l.iterator();
    while (it.hasNext()){
        E actual = it.next();
        if (e1.equals(actual)) return true;
    }
    return false;
    }
public int cantVeces(PositionList<E> l, E e1){
    int cant=0;
    if (l==null||e1==null) return 0;
    if (l.isEmpty()) throw new EmptyListException("lista vacia");
    Iterator<E> it= l.iterator();
    while (it.hasNext()){
        if(e1.equals(it.next()))
            cant++;
    }
    return cant;
    }
public boolean AlmenosNveces(PositionList<E> l, E x, int n){
    int cant=0;
    if(l==null || x==null) return false;
    if(l.isEmpty()) throw new EmptyListException("lista vacia, no va a estar contenido nunca");
    for(E elemento: l){
        if(elemento==x)cant++;
        if(cant>=n)return true;
    }
    return false;
}
public boolean AlmenosNveces2(PositionList<E> l, E x, int n){
    int cant=0;

    if(l==null || x==null) return false;
    if(l.isEmpty()) throw new EmptyListException("lista vacia, no va a estar contenido nunca");
    Iterator<E> it= l.iterator();
    while (it.hasNext()){
        if(x==it.next())cant++;
        if (cant>=n)return true;
    }
    return false;
}
public PositionList<E> doble(PositionList<E> l){
    if(l==null || l.isEmpty())return null;
    PositionList<E> ln= new ListaDoblementeEnlazada<E>();
    for (E elemento: l){
        ln.addLast(elemento);
        ln.addLast(elemento);
    }
    return ln;
}
public Iterable<Character> eliminar (PositionList<Character> l1,PositionList<Character> l2){
    PositionList<Character> ln= new ListaDoblementeEnlazada<>();
    if(l1==null || l2== null) return ln;
    if(l1.isEmpty()||l2.isEmpty()) return ln;
    Position<Character> posActual= l2.last();//obtenemos la pos de l2 arrancamos de atras 

    while(posActual!=null){
        Character c= posActual.element();
        if(esta(c,l1)){
            ln.addFirst(c);
            Position<Character> posAnt =l2.prev(posActual);
            l2.remove(posActual);
            posActual=posAnt;
        
        }
        else{
            try{
                posActual=l2.prev(posActual);
            }catch(BoundaryViolationException e){
                posActual=null;
            }
        }
    }
    return ln;
}
private boolean esta (Character elemento, PositionList<Character> l ){
    if(l==null)return false;
    if(l.isEmpty())return false;
    for(Character e:l){
        if(e.equals(elemento))return true;
    }
    return false;
}
public PositionList<E> intercalar(PositionList<E> l1, PositionList<E>l2){
    PositionList<E> ln=new ListaDoblementeEnlazada<>();
    Iterator<E> it1=l1.iterator();
    Iterator<E> it2=l2.iterator();

    while (it1.hasNext()&&it2.hasNext()){
        ln.addLast(it1.next());
        ln.addLast(it2.next());
    }
    while(it1.hasNext()){
        ln.addLast(it1.next());
    }
    while(it2.hasNext()){
        ln.addLast(it2.next());
    }
    return ln;
}
public PositionList<Integer> intercalarOrdenado (PositionList<Integer> l1, PositionList<Integer>l2){
    PositionList<Integer> ln=new ListaDoblementeEnlazada<>();
    Iterator<Integer> it1=l1.iterator();
    Iterator<Integer> it2=l2.iterator();
    Integer v1= it1.hasNext() ? it1.next():null;
    Integer v2= it2.hasNext() ? it2.next():null;

    while (v1!=null && v2!=null){
        if (v1<v2){
            ln.addLast(v1);
            v1=it1.hasNext()?it1.next():null;
        }
        else if(v1>v2){
            ln.addLast(v2);
            v2=it2.hasNext()?it2.next():null;
        }
        else{
            ln.addLast(v1);
            v1=it1.hasNext()?it1.next():null;
            v2=it2.hasNext()?it2.next():null;
        }
    }
    while(v1!=null){
        if(ln.isEmpty()||!ln.last().element().equals(v1)){
            ln.addLast(v1);
        v1=it1.hasNext()?it1.next():null;
        }
    }
    while(ln.isEmpty()||!ln.last().element().equals(v2)){
        if(it2.hasNext()){
            ln.addLast(v2);
        v2=it2.hasNext()?it2.next():null;
        }    
    }
    return ln;
}


}