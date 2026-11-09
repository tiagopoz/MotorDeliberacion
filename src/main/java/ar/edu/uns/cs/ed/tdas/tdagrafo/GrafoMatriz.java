package ar.edu.uns.cs.ed.tdas.tdagrafo;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEdgeException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class GrafoMatriz<V,E> implements Graph<V,E> {
    protected PositionList<Vertex<V>> vertices;
    protected PositionList<Edge<E>> arcos;
    protected Edge<E> [][] matriz;
    protected int cantidadVertices;
    
    @SuppressWarnings("hiding")
    private class Vertice<V> implements Vertex<V>{
        private Position<Vertex<V>> posicionEnVertices;
        private V rotulo;
        private int indice;

        public Vertice(V rotulo, int indice){
            this.rotulo=rotulo;
            this.indice=indice;
            posicionEnVertices=null;
        }
        public void setPosEnVertices(Position<Vertex<V>> p){
            posicionEnVertices=p;
        }
        public void setRotulo(V r){
            rotulo=r;
        }
        public int getIndice(){
            return indice;
        }
        public Position<Vertex<V>> getPositionEnVertices(){
            return posicionEnVertices;
        }
        public V element(){
            return rotulo;
        }
    }
    @SuppressWarnings("hiding")
    private class Arco<V,E> implements Edge<E> {
        private Position<Edge<E>> posEnArcos;
        private Vertice<V> v1,v2;
        private E rotulo;

        public Arco(E r, Vertice<V> v1, Vertice<V> v2){
            rotulo=r;
            this.v1=v1;
            this.v2=v2;
            posEnArcos=null;
        }
        public void setPosEnArcos(Position<Edge<E>> p){
            posEnArcos=p;
        }
        public E element(){
            return rotulo;
        }
        public void setRotulo(E r){
            rotulo=r;
        }
        public Position<Edge<E>> getPosicionEnArcos(){
            return posEnArcos;
        }
        public Vertice<V> getV1(){
            return v1;
        }
        public Vertice<V> getV2(){
            return v2;
        }
    }

    public GrafoMatriz (int n){
        vertices= new ListaDoblementeEnlazada<Vertex<V>>();
        arcos= new ListaDoblementeEnlazada<Edge<E>>();
        matriz=(Edge<E>[][])new Arco[n][n];
        cantidadVertices=0;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                matriz[i][j]=null;
    }
    public Iterable<Vertex<V>> vertices() {
        PositionList<Vertex<V>> listaV= new ListaDoblementeEnlazada<Vertex<V>>();
        for(Vertex<V> v:vertices)
            listaV.addLast(v);
        return listaV;
    }
    public Iterable<Edge<E>> edges() {
    PositionList<Edge<E>> listaA= new ListaDoblementeEnlazada<Edge<E>>();
    for(Edge<E> a: arcos)
        listaA.addLast(a);
    return listaA;
    }
    @Override
    public Iterable<Edge<E>> incidentEdges(Vertex<V> v) {
        Vertice<V> vv=(Vertice<V>)v;
        int i = vv.getIndice();
        PositionList<Edge<E>> lista =new ListaDoblementeEnlazada<Edge<E>>();
        for(int j=0;j<cantidadVertices;j++){
            if (matriz[i][j]!= null)
                lista.addLast(matriz[i][j]);
        } 
        return lista;
    }
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) {
        Arco<V,E> ee= (Arco<V,E>)e;
        if(ee.getV1()==v)return ee.getV2();
        else if(ee.getV2()==v)return ee.getV1();
        else throw new InvalidEdgeException("arco y vertice no relacionados");
    }

    @Override
    public Vertex<V>[] endvertices(Edge<E> e) {
        Vertex<V>[] a= (Vertex<V>[]) new Vertice[2];
        Arco<V,E> ee=(Arco<V,E>)e;
        a[0]= ee.getV1();
        a[1]=ee.getV2();
        return a;
    }

    @Override
    public boolean areAdjacent(Vertex<V> v, Vertex<V> w) {
    Vertice<V> vv= (Vertice<V>) v;
    Vertice<V> vw= (Vertice<V>) w;
    int i=vv.getIndice();
    int j=vw.getIndice();
    return matriz[i][j] !=null;
    }

    @Override
    public V replace(Vertex<V> v, V x) {
    Vertice<V> vv=(Vertice<V>)v;
    V elem= vv.element();
    vv.setRotulo(x);
    return elem;
    }

    @Override
    public E replace(Edge<E> e, E x) {
    Arco<V,E> ee= (Arco<V,E>)e;
    E elem= ee.element();
    ee.setRotulo(x);
    return elem;
    }

    @Override
    public Vertex<V> insertVertex(V x) {
        Vertice<V> vv= new Vertice<V>(x, cantidadVertices++);
        vertices.addLast(vv);
        vv.setPosEnVertices(vertices.last());
        return vv;   
    }

    @Override
    public Edge<E> insertEdge(Vertex<V> v, Vertex<V> w, E e) {
        Vertice<V> vv=(Vertice<V>)v;
        Vertice<V> ww=(Vertice<V>)w;
        int i = vv.getIndice();
        int j = ww.getIndice();
        Arco<V,E> a= new Arco<V,E>(e, vv, ww);
        matriz[i][j]=matriz[j][i]=a;
        arcos.addLast(a);
        a.setPosEnArcos(arcos.last());
        return a;
    }

    @Override
    public V removeVertex(Vertex<V> v) {
        Vertice<V> vv=(Vertice<V>)v;
        int i=vv.getIndice();
        for(int j =0;j<cantidadVertices;j++){
            if(matriz[i][j]!=null){
                Arco<V,E> a=(Arco<V,E>)matriz[i][j];
                matriz[i][j]=matriz[j][i]=null;
                arcos.remove(a.getPosicionEnArcos());
            }
        }
        vertices.remove(vv.getPositionEnVertices());
        return v.element();
    }

    @Override
    public E removeEdge(Edge<E> e) {
        Arco<V,E> ee=(Arco<V,E>)e;
        int i = ee.getV1().getIndice();
        int j = ee.getV2().getIndice();
        matriz[i][j]= matriz[j][i]=null;
        arcos.remove(ee.getPosicionEnArcos());
        return e.element();
    }
    
}
