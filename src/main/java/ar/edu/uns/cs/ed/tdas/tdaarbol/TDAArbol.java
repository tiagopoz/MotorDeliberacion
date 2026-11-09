package ar.edu.uns.cs.ed.tdas.tdaarbol;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.MapHashAbierto;

public class TDAArbol<E> implements Tree<E> {
    protected TNodo<E> raiz;
    protected int size;

    public TDAArbol() {
        raiz = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private TNodo<E> checkPosition(Position<E> p) {
        try {
            if (p == null) throw new InvalidPositionException("Posición nula");
            if (p.element() == null) throw new InvalidPositionException("Posición eliminada previamente");
            return (TNodo<E>) p;
        } catch (ClassCastException e) {
            throw new InvalidPositionException("p no es un nodo de árbol válido");
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        PositionList<E> l = new ListaDoblementeEnlazada<E>();
        for (Position<E> pos : positions()) {
            l.addLast(pos.element());
        }
        return l.iterator();
    }

    @Override
    public Iterable<Position<E>> positions() {
        PositionList<Position<E>> l = new ListaDoblementeEnlazada<Position<E>>();
        if (!isEmpty()) {
            preorden(raiz, l);
        }
        return l;
    }

    private void preorden(TNodo<E> n, PositionList<Position<E>> l) {
        l.addLast(n);
        for (TNodo<E> t : n.getHijos()) {
            preorden(t, l);
        }
    }

    @Override
    public E replace(Position<E> v, E e) {
        TNodo<E> n = checkPosition(v);
        E viejo = n.element();
        n.setElemento(e);
        return viejo;
    }

    @Override
    public Position<E> root() {
        if (isEmpty()) throw new EmptyTreeException("Raíz vacía");
        return raiz;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        if (this.isRoot(v)) throw new BoundaryViolationException("La posición corresponde a la raíz");
        TNodo<E> n = checkPosition(v);
        return n.getPadre();
    }

    @Override
    public Iterable<Position<E>> children(Position<E> v) {
        TNodo<E> n = checkPosition(v);
        PositionList<Position<E>> l = new ListaDoblementeEnlazada<Position<E>>();
        for (Position<E> p : n.getHijos()) {
            l.addLast(p);
        }
        return l;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        TNodo<E> n = checkPosition(v);
        return !n.getHijos().isEmpty();
    }

    @Override
    public boolean isExternal(Position<E> v) {
        TNodo<E> n = checkPosition(v);
        return n.getHijos().isEmpty();
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TNodo<E> n = checkPosition(v);
        return n == raiz;
    }

    @Override
    public void createRoot(E e) {
        if (!isEmpty()) throw new InvalidOperationException("El árbol ya tiene raíz");
        raiz = new TNodo<E>(e);
        size++;
    }

    @Override
    public Position<E> addFirstChild(Position<E> p, E e) {
        if (isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> hijo = new TNodo<E>(e, padre);
        padre.getHijos().addFirst(hijo);
        size++;
        return hijo;
    }

    @Override
    public Position<E> addLastChild(Position<E> p, E e) {
        if (isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> hijo = new TNodo<E>(e, padre);
        padre.getHijos().addLast(hijo);
        size++;
        return hijo;
    }

    @Override
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e) {
        if (isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> hijoA = checkPosition(rb);
        if (hijoA.getPadre() != padre) throw new InvalidPositionException("rb no es hijo de p");
        
        TNodo<E> hijoN = new TNodo<E>(e, padre);
        for (Position<TNodo<E>> pos : padre.getHijos().positions()) {
            if (pos.element() == hijoA) {
                padre.getHijos().addBefore(pos, hijoN);
                size++;
                return hijoN;
            }
        }
        throw new InvalidPositionException("rb no fue encontrado entre los hijos de p");
    }

    @Override
    public Position<E> addAfter(Position<E> p, Position<E> lb, E e) {
        if (isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> hijoD = checkPosition(lb);
        if (hijoD.getPadre() != padre) throw new InvalidPositionException("lb no es hijo de p");
        
        TNodo<E> hijoN = new TNodo<E>(e, padre);
        for (Position<TNodo<E>> pos : padre.getHijos().positions()) {
            if (pos.element() == hijoD) {
                padre.getHijos().addAfter(pos, hijoN);
                size++;
                return hijoN;
            }
        }
        throw new InvalidPositionException("lb no fue encontrado entre los hijos de p");
    }

    @Override
    public void removeExternalNode(Position<E> p) {
        if (isEmpty()) throw new InvalidPositionException("Árbol vacío");
        if (!isExternal(p)) throw new InvalidPositionException("El nodo no es externo");
        
        if (isRoot(p)) {
            raiz = null;
            size--;
        } else {
            TNodo<E> n = checkPosition(p);
            TNodo<E> padre = n.getPadre();
            for (Position<TNodo<E>> pos : padre.getHijos().positions()) {
                if (pos.element() == n) {
                    padre.getHijos().remove(pos);
                    size--;
                    return;
                }
            }
        }
    }

    @Override
    public void removeInternalNode(Position<E> p) {
        if (isEmpty()) throw new InvalidPositionException("Árbol vacío");
        if (!isInternal(p)) throw new InvalidPositionException("El nodo no es interno");
        
        TNodo<E> n = checkPosition(p);
        if (n == raiz) {
            if (n.getHijos().size() == 1) {
                raiz = n.getHijos().first().element();
                raiz.setPadre(null);
                size--;
            } else {
                throw new InvalidPositionException("La raíz tiene más de un hijo");
            }
        } else {
            TNodo<E> padre = n.getPadre();
            Position<TNodo<E>> posN = null;
            for (Position<TNodo<E>> pos : padre.getHijos().positions()) {
                if (pos.element() == n) {
                    posN = pos;
                    break;
                }
            }
            while (!n.getHijos().isEmpty()) {
                TNodo<E> t = n.getHijos().first().element();
                n.getHijos().remove(n.getHijos().first());
                t.setPadre(padre);
                padre.getHijos().addBefore(posN, t);
            }
            padre.getHijos().remove(posN);
            size--;
        }
    }

    @Override
    public void removeNode(Position<E> p) {
        if (isExternal(p)) {
            removeExternalNode(p);
        } else {
            removeInternalNode(p);
        }
    }

    public Map<Character, Integer> cantidadRepeticiones(Tree<Character> t) {
        Map<Character, Integer> m = new MapHashAbierto<Character, Integer>();
        for (Position<Character> pos : t.positions()) {
            Character c = pos.element();
            Integer valor = m.get(c);
            if (valor == null) {
                m.put(c, 1);
            } else {
                m.put(c, valor + 1);
            }
        }
        return m;
    }
}