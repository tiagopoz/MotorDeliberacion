package ar.edu.uns.cs.ed.tdas;
public class TDAEntry<K,V> implements Entry<K,V> {
    protected K key;
    protected V value;

    public TDAEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
    public void setValue(V value) {
        this.value = value;
    }
    public  void setKey(K key) {
        this.key = key;
    }
}

