package ar.edu.uns.cs.ed.tdas.tdamapeo;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.TDAEntry;
public class TDAMapeo<K,V> implements Map<K,V> {
  protected PositionList<Entry<K, V>> M;  
  public TDAMapeo() {
    M = new ListaDoblementeEnlazada<Entry<K,V>>();
  }
public int size() {
    return M.size();
}
public boolean isEmpty() {
    return M.isEmpty();
}
public V get(K key){//
  if(key == null) throw new InvalidKeyException("Clave nula no permitida");
  for (Entry<K,V> e: M){
      if (e.getKey().equals(key)){ 
        return e.getValue();
      }    
    }
    return null;
}
public V put(K key, V value){
  if (key == null) throw new InvalidKeyException("Clave nula no permitida");
  for (Position<Entry<K,V>> p : M.positions()){
    if (p.element().getKey().equals(key)){
      V valorviejo = p.element().getValue();
      ((TDAEntry<K,V>) p.element()).setValue(value);
      return valorviejo;
    }
  }
  M.addLast(new TDAEntry<>(key, value));
  return null;
}
public V remove(K key){
  if (key == null) throw new InvalidKeyException("Clave nula no permitida");
  for (Position<Entry<K,V>>p: M.positions()){
    if (p.element().getKey().equals(key)){
      V valorviejo = p.element().getValue();
      M.remove(p);
      return valorviejo;
    }
  }
  return null;
}
public Iterable<K> keys(){
  PositionList<K> claves = new ListaDoblementeEnlazada<>();
  for (Entry<K,V> e: M){
    claves.addLast(e.getKey());
  }
  return claves;
}
public Iterable<V> values(){
  PositionList<V> valores = new ListaDoblementeEnlazada<>();
  for (Entry<K,V> e: M){
    valores.addLast(e.getValue());
  }
  return valores;
}
public Iterable<Entry<K,V>> entries(){
  return M;
}
public PositionList<Pair<Integer,Integer>> incisoA(Map<Integer,Integer> M1, Map<Integer,Integer> M2){
  PositionList<Pair<Integer,Integer>> l = new ListaDoblementeEnlazada<>();
  for (Integer k : M1.keys()){
    Integer v1= M1.get(k);
    Integer v2= M2.get(k);
    if (v2!=null){
      if (!v2.equals(v1)){
        Pair<Integer,Integer> p1= new Pair<Integer,Integer>(k,v1);
        Pair<Integer,Integer> p2= new Pair<Integer,Integer>(k,v2);
        l.addLast(p1);
        l.addLast(p2);
      }
    }
  }
return l;
}
public boolean incisoB( Map<K,V> M1, Map<K,V> M2){
  for (K k1: M1.keys()){
    if (M2.get(k1)==null)
      return false;
  }
  return true;
}
}
