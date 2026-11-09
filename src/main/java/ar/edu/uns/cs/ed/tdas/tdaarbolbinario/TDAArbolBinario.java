package ar.edu.uns.cs.ed.tdas.tdaarbolbinario;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.Dictionary;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.TDADiccionario;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.MapHashAbierto;

public class TDAArbolBinario<E> implements BinaryTree<E> {
    protected int size;
    protected BTNodo<E> raiz;

    public TDAArbolBinario() {
        raiz = null;
        size = 0;
    }

   
    private BTNodo<E> checkPosition(Position<E> p) {
        try {
            if (p == null) throw new InvalidPositionException("Posición nula");
            if (p.element() == null) throw new InvalidPositionException("Posición eliminada previamente");
            return (BTNodo<E>) p;
        } catch (ClassCastException e) {
            throw new InvalidPositionException("p no es un nodo de árbol binario");
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

    private void preorden(BTNodo<E> r, PositionList<Position<E>> l) {
        l.addLast(r);
        if (r.getLeft() != null) preorden(r.getLeft(), l);
        if (r.getRight() != null) preorden(r.getRight(), l);
    }

    @Override
    public E replace(Position<E> v, E e) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
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
        if (v == null) throw new InvalidPositionException("Posición nula");
        if (this.isRoot(v)) throw new BoundaryViolationException("La posición es la raíz");
        BTNodo<E> n = checkPosition(v);
        return n.getPadre();
    }

    @Override
    public Iterable<Position<E>> children(Position<E> v) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        PositionList<Position<E>> l = new ListaDoblementeEnlazada<Position<E>>();
        if (n.getLeft() != null) l.addLast(n.getLeft());
        if (n.getRight() != null) l.addLast(n.getRight());
        return l;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        return n.getLeft() != null || n.getRight() != null;
    }

    @Override
    public boolean isExternal(Position<E> v) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        return n.getRight() == null && n.getLeft() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        return n == raiz;
    }

    @Override
    public void createRoot(E e) {
        if (!isEmpty()) throw new InvalidOperationException("El árbol ya tiene raíz");
        raiz = new BTNodo<E>(e);
        size++;
    }

    @Override
    public Position<E> addFirstChild(Position<E> p, E e) {
        return addLeft(p, e);
    }

    @Override
    public Position<E> addLastChild(Position<E> p, E e) {
        return addRight(p, e);
    }

    @Override
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e) {
        if (p == null || isEmpty()) throw new InvalidPositionException("Posición no válida");
        BTNodo<E> padre = checkPosition(p);
        if (padre.getLeft() != null && padre.getRight() != null) throw new InvalidPositionException("El padre ya tiene 2 hijos");
        BTNodo<E> hijoA = checkPosition(rb);
        if (hijoA.getPadre() != padre || hijoA == padre.getLeft()) throw new InvalidPositionException("Posición rb inválida");
        return addLeft(p, e);
    }

    @Override
    public Position<E> addAfter(Position<E> p, Position<E> lb, E e) {
        if (p == null || isEmpty()) throw new InvalidPositionException("Posición no válida");
        BTNodo<E> padre = checkPosition(p);
        if (padre.getLeft() != null && padre.getRight() != null) throw new InvalidPositionException("El padre ya tiene 2 hijos");
        BTNodo<E> hijoD = checkPosition(lb);
        if (hijoD.getPadre() != padre || hijoD == padre.getRight()) throw new InvalidPositionException("Posición lb inválida");
        return addRight(p, e);
    }

    @Override
    public void removeExternalNode(Position<E> p) {
        if (p == null || isEmpty() || !isExternal(p)) throw new InvalidPositionException("Posición inválida o no es externa");
        if (isRoot(p)) {
            raiz = null;
            size--;
        } else {
            BTNodo<E> n = checkPosition(p);
            if (n.getPadre().getLeft() == n) {
                n.getPadre().setLeft(null);
            } else {
                n.getPadre().setRight(null);
            }
            size--;
        }
    }

    @Override
    public void removeInternalNode(Position<E> p) {
        if (p == null || isEmpty() || !isInternal(p)) throw new InvalidPositionException("Posición inválida o no es interna");
        BTNodo<E> n = checkPosition(p);
        BTNodo<E> hijo;
        if (n.getLeft() != null && n.getRight() != null) {
            throw new InvalidPositionException("No se puede remover un nodo interno con 2 hijos");
        } else if (n.getLeft() != null) {
            hijo = n.getLeft();
        } else {
            hijo = n.getRight();
        }

        if (isRoot(n)) {
            raiz = hijo;
            size--;
            hijo.setPadre(null);
        } else {
            hijo.setPadre(n.getPadre());
            size--;
            if (n.getPadre().getLeft() == n) {
                n.getPadre().setLeft(hijo);
            } else {
                n.getPadre().setRight(hijo);
            }
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

    @Override
    public Position<E> left(Position<E> v) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        if (n.getLeft() == null) throw new BoundaryViolationException("No tiene hijo izquierdo");
        return n.getLeft();
    }

    @Override
    public Position<E> right(Position<E> v) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        if (n.getRight() == null) throw new BoundaryViolationException("No tiene hijo derecho");
        return n.getRight();
    }

    @Override
    public boolean hasLeft(Position<E> v) {
        BTNodo<E> n = checkPosition(v);
        return n.getLeft() != null;
    }

    @Override
    public boolean hasRight(Position<E> v) {
        BTNodo<E> n = checkPosition(v);
        return n.getRight() != null;
    }

    @Override
    public Position<E> addLeft(Position<E> v, E r) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        if (n.getLeft() != null) throw new InvalidOperationException("Ya tiene hijo izquierdo");
        BTNodo<E> pos = new BTNodo<E>(r, n);
        n.setLeft(pos);
        size++;
        return pos;
    }

    @Override
    public Position<E> addRight(Position<E> v, E r) {
        if (v == null) throw new InvalidPositionException("Posición nula");
        BTNodo<E> n = checkPosition(v);
        if (n.getRight() != null) throw new InvalidOperationException("Ya tiene hijo derecho");
        BTNodo<E> pos = new BTNodo<E>(r, n);
        n.setRight(pos);
        size++;
        return pos;
    }

    @Override
    public void attach(Position<E> v, BinaryTree<E> T1, BinaryTree<E> T2) {
        if (v == null || isEmpty() || !isExternal(v)) {
            throw new InvalidPositionException("Posición no válida para attach");
        }
        BTNodo<E> n = checkPosition(v);

        if (T1 != null && !T1.isEmpty()) {
            BTNodo<E> r1 = clonarSubarbol(T1, T1.root());
            n.setLeft(r1);
            r1.setPadre(n);
            size += T1.size();
        }

        if (T2 != null && !T2.isEmpty()) {
            BTNodo<E> r2 = clonarSubarbol(T2, T2.root());
            n.setRight(r2);
            r2.setPadre(n);
            size += T2.size();
        }
    }

    private BTNodo<E> clonarSubarbol(BinaryTree<E> t, Position<E> pos) {
        if (pos == null) return null;
        BTNodo<E> nuevo = new BTNodo<E>(pos.element());
        if (t.hasLeft(pos)) {
            try {
                BTNodo<E> izq = clonarSubarbol(t, t.left(pos));
                nuevo.setLeft(izq);
                izq.setPadre(nuevo);
            } catch (InvalidPositionException | BoundaryViolationException e) {
                // Manajeo de excepciones de navegación si aplican
            }
        }
        if (t.hasRight(pos)) {
            try {
                BTNodo<E> der = clonarSubarbol(t, t.right(pos));
                nuevo.setRight(der);
                der.setPadre(nuevo);
            } catch (InvalidPositionException | BoundaryViolationException e) {
                // Manejo de excepciones de navegación si aplican
            }
        }
        return nuevo;
    }

    // --- Métodos de ejercicios adicionales ---

    public Dictionary<E, E> dicDeBin() {
        Dictionary<E, E> d = new TDADiccionario<E, E>();
        for (Position<E> pos : positions()) {
            BTNodo<E> n = checkPosition(pos);
            if (n.getLeft() != null) d.insert(n.element(), n.getLeft().element());
            if (n.getRight() != null) d.insert(n.element(), n.getRight().element());
        }
        return d;
    }

    public Map<Character, Integer> eliminarHojas(BinaryTree<Character> a, Position<Character> p) {
        if (p == null) throw new InvalidPositionException("Posición nula");
        Map<Character, Integer> m = new MapHashAbierto<Character, Integer>();
        PositionList<Position<Character>> lh = new ListaDoblementeEnlazada<Position<Character>>();
        buscarHojas(lh, p, a);
        for (Position<Character> t : lh) {
            Integer cant = m.get(t.element());
            if (cant == null) {
                m.put(t.element(), 1);
            } else {
                m.put(t.element(), cant + 1);
            }
        }
        while (!lh.isEmpty()) {
            a.removeExternalNode(lh.first().element());
            lh.remove(lh.first());
        }
        return m;
    }

    private void buscarHojas(PositionList<Position<Character>> l, Position<Character> p, BinaryTree<Character> a) {
        if (a.isExternal(p)) {
            l.addLast(p);
        } else {
            if (a.hasLeft(p)) buscarHojas(l, a.left(p), a);
            if (a.hasRight(p)) buscarHojas(l, a.right(p), a);
        }
    }
}