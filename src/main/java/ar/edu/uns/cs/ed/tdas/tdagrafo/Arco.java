package ar.edu.uns.cs.ed.tdas.tdagrafo;
import ar.edu.uns.cs.ed.tdas.Position;
public class Arco<V,E> implements Edge<E> {
    private E rotulo;
    private Vertice<V,E> v1,v2;
    private Position<Arco<V,E>> posEnArcos;
    private Position<Arco<V,E>> posEnIv1, posEnIv2;
    public Arco(E r, Vertice<V,E> v1, Vertice<V,E> v2){
        rotulo=r;
        this.v1=v1;
        this.v2=v2;
        posEnArcos=null;
        posEnIv1=null;
        posEnIv2=null;
    }
    public Arco(E r, Vertice<V,E> v1, Vertice<V,E> v2, Position<Arco<V,E>> pea,Position<Arco<V,E>>p1,Position<Arco<V,E>>p2){
        rotulo=r;
        this.v1=v1;
        this.v2=v2;
        posEnArcos=pea;
        posEnIv1=p1;
        posEnIv2=p2;
    }
    public void setRotulo(E r){
        rotulo=r;
    }
    public void setPosEnArcos(Position<Arco<V,E>> p){
        posEnArcos=p;
    }
    public void setPosEnIv1(Position<Arco<V,E>> p1){
        posEnIv1=p1;
    }
    public void setPosEnIv2(Position<Arco<V,E>> p2){
        posEnIv2=p2;
    }
    public E element(){
        return rotulo;
    }
    public Vertice<V,E> getV1(){
        return v1;
    }
    public Vertice<V,E> getV2(){
        return v2;
    }
    public Position<Arco<V,E>> getPosEnArcos(){
        return posEnArcos;
    }
    public Position<Arco<V,E>> getPosInv1(){
        return posEnIv1;
    }
    public Position<Arco<V,E>> getPosInv2(){
        return posEnIv2;
    }
    
}
