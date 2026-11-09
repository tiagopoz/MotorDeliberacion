package ar.edu.uns.cs.ed.tdas.tdadiccionario;

import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.TDAEntry;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEntryException;

public class DiccionarioHashAbierto<K,V> implements Dictionary<K,V> {

    protected int n; // Tamaño del arreglo de cubetas
    protected int cant; // Cantidad de entradas en el diccionario
    protected PositionList<Entry<K,V>>[] tabla; // Arreglo de cubetas (listas)

    @SuppressWarnings("unchecked")
    public DiccionarioHashAbierto() {
        n = 13;
        tabla = (PositionList<Entry<K,V>>[]) new PositionList[n];
        for (int i = 0; i < n; i++) {
            tabla[i] = new ListaDoblementeEnlazada<>();
        }
        cant = 0;
    }

    @Override
    public int size() {
        return cant;
    }

    @Override
    public boolean isEmpty() {
        return cant == 0;
    }

    @Override
    public Entry<K, V> find(K key) {
        if (key == null) throw new InvalidKeyException("Clave nula no permitida");
        int i = h(key);
        for (Entry<K,V> e : tabla[i]) {
            if (e.getKey().equals(key)) { 
                return e;
            }    
        }
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> findAll(K key) {
        if (key == null) throw new InvalidKeyException("Clave nula no permitida");
        int i = h(key);
        PositionList<Entry<K,V>> lista = new ListaDoblementeEnlazada<>();
        for (Entry<K,V> e : tabla[i]) {
            if (e.getKey().equals(key)) {
                lista.addLast(e);
            }
        }
        return lista;
    }

    @Override
    public Entry<K, V> insert(K key, V value) {
        if (key == null) throw new InvalidKeyException("Clave nula no permitida");
        int i = h(key);
        Entry<K,V> e = new TDAEntry<K,V>(key, value);
        tabla[i].addLast(e);
        cant++;
        return e;
    }

    @Override
    public Entry<K, V> remove(Entry<K, V> e) {
        if (e == null) throw new InvalidEntryException("Entrada no valida");
        if (e.getKey() == null) throw new InvalidEntryException("Entrada con clave nula");
        
        int i = h(e.getKey());
        for (Position<Entry<K,V>> p : tabla[i].positions()) {
            if (p.element() == e) {
                Entry<K,V> r = p.element();
                tabla[i].remove(p);
                cant--;
                return r;   
            }
        }
        throw new InvalidEntryException("La entrada no pertenece al diccionario");
    }

    @Override
    public Iterable<Entry<K, V>> entries() {
        PositionList<Entry<K,V>> entradas = new ListaDoblementeEnlazada<>();
        for (int i = 0; i < n; i++) {
            for (Entry<K,V> e : tabla[i]) {
                entradas.addLast(e);
            }
        }
        return entradas;
    }

    private int h(K key) {
        if (key == null) throw new InvalidKeyException("Clave nula no permitida");
        return Math.abs(key.hashCode()) % n;
    }

    // Método auxiliar de parcial
    public Iterable<Entry<K,V>> eliminarTodas(K c, V v) {
        if (c == null) throw new InvalidKeyException("Clave nula no permitida");
        int i = h(c);
        PositionList<Entry<K,V>> res = new ListaDoblementeEnlazada<>();
        for (Position<Entry<K,V>> p : tabla[i].positions()) {
            if (p.element().getKey().equals(c) && p.element().getValue().equals(v)) {
                res.addLast(p.element());
                tabla[i].remove(p);
                cant--;
            }
        }
        return res;
    }

    // Método auxiliar de parcial corregido (usaba 'buckets' en vez de 'tabla')
    public boolean todas(K key, V value) {
        if (key == null) throw new InvalidKeyException("Clave no valida");
        int i = h(key);
        for (Entry<K,V> e : tabla[i]) {
            if (e.getKey().equals(key) && !e.getValue().equals(value))    
                return false;
        }
        return true;
    }
}