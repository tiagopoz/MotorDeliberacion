package ar.edu.uns.cs.ed.tdas.tdagrafo;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEdgeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidVertexException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class Grafo<V,E> implements Graph<V,E> {
    protected PositionList<Vertice<V,E>> nodos;
    protected PositionList<Arco<V,E>> arcos;

    public Grafo(){
        nodos= new ListaDoblementeEnlazada<Vertice<V,E>>();
        arcos= new ListaDoblementeEnlazada<Arco<V,E>>();
    }
    public Iterable<Vertex<V>> vertices() {
        PositionList<Vertex<V>> listaV= new ListaDoblementeEnlazada<Vertex<V>>();
        for(Vertex<V> v:nodos)
            listaV.addLast(v);
        return listaV;
    }
    public Iterable<Edge<E>> edges() {
    PositionList<Edge<E>> listaA= new ListaDoblementeEnlazada<Edge<E>>();
    for(Edge<E> a: arcos)
        listaA.addLast(a);
    return listaA;
    }
    public Iterable<Edge<E>> incidentEdges(Vertex<V> v) {
        if(v== null)throw new InvalidVertexException("vertice invalido");
        PositionList<Edge<E>> arcosI = new ListaDoblementeEnlazada<Edge<E>>();
        Vertice<V,E> vert= (Vertice<V,E>)v;
        for(Edge<E> a: vert.getAdyacentes())
            arcosI.addLast(a);
        return arcosI; 
    }
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) {
        if (v==null) throw new  InvalidVertexException("vertice invalido");
        if(e== null) throw new InvalidEdgeException("arco invalido");
        Arco<V,E> ee= (Arco<V,E>)e;
        if(ee.getV1()==v)return ee.getV2();
        else if(ee.getV2()==v)return ee.getV1();
        else throw new InvalidEdgeException("Vertice y arco no relacionados");
    }
    public Vertex<V>[] endvertices(Edge<E> e) {
        Vertex<V>[] a=(Vertex<V>[]) new Vertice[2];
        Arco<V,E> ee= (Arco<V,E>)e;
        a[0]=ee.getV1();
        a[1]=ee.getV2();
        return a;
    }
    public boolean areAdjacent(Vertex<V> v, Vertex<V> w) {
        Vertice<V,E> vv=(Vertice<V,E>)v;
        for(Arco<V,E> a:vv.getAdyacentes()){
            if(opposite(vv, a)==w)
                return true;
        }
        return false;
    }
    public boolean areAdjacent2(Vertex<V> v, Vertex<V> w) {
        Vertice<V,E> vv=(Vertice<V,E>)v;
        for(Arco<V,E> a:vv.getAdyacentes()){
            if(a.getV1()==vv || a.getV2()==vv){
                Vertice<V,E> otro = (a.getV1()==vv)? a.getV2():a.getV1();    
                if(otro==w)return true;
            }
        }
        return false;
    }
    public V replace(Vertex<V> v, V x) {
        Vertice<V,E> vv=(Vertice<V,E>)v;
        V elemviejo = vv.element();
        vv.setRotulo(x);
        return elemviejo; 
    }
    public E replace(Edge<E> e, E x) {
        Arco<V,E> ee= (Arco<V,E>)e;
        E elemViejo=ee.element();
        ee.setRotulo(x);
        return elemViejo;
    }
    public Vertex<V> insertVertex(V x) {
        Vertice<V,E> v= new Vertice<V,E> (x);
        nodos.addLast(v);
        v.setPosicionEnNodos(nodos.last());
        return v;
    }
    public Edge<E> insertEdge(Vertex<V> v, Vertex<V> w, E e) {
         Vertice<V,E> vv= (Vertice<V,E>)v;
        Vertice<V,E> vw= (Vertice<V,E>)w;
        Arco<V,E> a= new Arco<V,E>(e, vv, vw);

        vv.getAdyacentes().addLast(a);
        a.setPosEnIv1(vv.getAdyacentes().last());
        
        vw.getAdyacentes().addLast(a);
        a.setPosEnIv2(vw.getAdyacentes().last());
        
        arcos.addLast(a);
        a.setPosEnArcos(arcos.last());

        return a;
    }
    public V removeVertex(Vertex<V> v) {
        Vertice<V,E> vv= (Vertice<V,E>)v;
        while (!vv.getAdyacentes().isEmpty()){
            Arco<V,E> a= vv.getAdyacentes().first().element();
            removeEdge(a);
        }
        V elem= vv.element();
        Position<Vertice<V,E>> pos= vv.getPosicionEnNodos();
        nodos.remove(pos);
        return elem;
    }
    public E removeEdge(Edge<E> e) {
        Arco<V,E> ee= (Arco<V,E>)e;
        Vertice<V,E> v1=ee.getV1();
        Vertice<V,E> v2=ee.getV2();
        v1.getAdyacentes().remove(ee.getPosInv1());
        v2.getAdyacentes().remove(ee.getPosInv2());
        Position<Arco<V,E>> pos= ee.getPosEnArcos();
        E elem= ee.element();
        arcos.remove(pos);
        return elem;
    }
    
    
}
