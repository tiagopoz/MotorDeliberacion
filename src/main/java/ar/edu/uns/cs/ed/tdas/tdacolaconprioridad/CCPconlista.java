package ar.edu.uns.cs.ed.tdas.tdacolaconprioridad;

import java.util.Comparator;

import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyPriorityQueueException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class CCPconlista<K, V> implements PriorityQueue<K, V> {

    private PositionList<Entry<K, V>> lista;
    private Comparator<K> comp;

    private static class Entrada<K, V> implements Entry<K, V> {
        private K clave;
        private V valor;

        public Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }

        @Override
        public K getKey() {
            return clave;
        }

        @Override
        public V getValue() {
            return valor;
        }
    }

    public CCPconlista(Comparator<K> comp) {
        this.lista = new ListaDoblementeEnlazada<Entry<K, V>>();
        this.comp = comp;
    }

    @Override
    public int size() {
        return lista.size();
    }

    @Override
    public boolean isEmpty() {
        return lista.isEmpty();
    }

    @Override
    public Entry<K, V> min() {
        if (lista.isEmpty()) {
            throw new EmptyPriorityQueueException("La cola con prioridad está vacía.");
        }
        return lista.first().element();
    }

    @Override
    public Entry<K, V> insert(K key, V value) {
        if (key == null) {
            throw new InvalidKeyException("La clave no puede ser nula.");
        }

        Entry<K, V> nueva = new Entrada<K, V>(key, value);

        if (lista.isEmpty()) {
            lista.addFirst(nueva);
            return nueva;
        }

        // Insertamos manteniendo la lista ordenada de menor a mayor clave
        for (Position<Entry<K, V>> pos : lista.positions()) {
            if (comp.compare(key, pos.element().getKey()) < 0) {
                lista.addBefore(pos, nueva);
                return nueva;
            }
        }

        // Si es mayor o igual que todas las existentes, va al final
        lista.addLast(nueva);
        return nueva;
    }

    @Override
    public Entry<K, V> removeMin() {
        if (lista.isEmpty()) {
            throw new EmptyPriorityQueueException("La cola con prioridad está vacía.");
        }
        return lista.remove(lista.first());
    }
}