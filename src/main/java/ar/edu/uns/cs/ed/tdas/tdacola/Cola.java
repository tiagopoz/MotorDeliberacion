package ar.edu.uns.cs.ed.tdas.tdacola;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyQueueException;

/**
 * Implementación de la interfaz Queue utilizando un arreglo.
 * @author Cátedra de Estructuras de Datos, Departamento de Cs. e Ing. de la Computación, UNS.
 */
public class Cola<E> implements Queue<E> {
    private E [] datos;
    private int frente;
    private int fondo;
    private static final int CAPACIDAD_INICIAL=10;
    public Cola(){
        datos= (E[]) new Object[CAPACIDAD_INICIAL];
        this.frente=0;//frente es 0 porque el primer elemento de la cola se encuentra en la posición 0 del arreglo
        this.fondo=0;//fondo es 0 porque el primer elemento de la cola se encuentra en la posición 0 del arreglo
    }
    public void enqueue (E e){
        //SI ESTA LLENA REDIMENSIONO
        if (size()==datos.length-1){//
            redimensionar();
        }
        datos[fondo]=e;
        //movemos de forma circular el fondo de la cola
        fondo=(fondo+1)%datos.length;//esto se hace para que el fondo vuelva al inicio del arreglo cuando llegue al final, creando un efecto de cola circular.
        
    }
    public E dequeue(){
        if (isEmpty()) throw new EmptyQueueException ("la cola esta vacia");
        E elemento= datos[frente];
        datos[frente]=null;

        //movemos frente en forma circular
        frente=(frente+1)%datos.length;

        return elemento;
    }
    public E front(){
    if(isEmpty())throw new EmptyQueueException("la pila Esta Vacia");
    E elemento =datos[frente];
    return elemento;
    }

    public boolean isEmpty(){
        return frente==fondo;//si frente y fondo son iguales, entonces la cola esta vacia
    }
    
    public int size(){
        return (datos.length-frente+fondo)%datos.length;//esto se hace para calcular el tamaño de la cola teniendo en cuenta que el frente y el fondo pueden estar en cualquier posición del arreglo debido a la naturaleza circular de la cola.
    }

    private void redimensionar(){
        int nuevacapacidad=datos.length*2;
        int tamanioactual=size();
        E[] nuevosDatos = (E[]) new Object[nuevacapacidad];
        for (int i = 0; i < datos.length; i++) {
            nuevosDatos[i] = datos[
                (frente + i) % datos.length
            ];//esto se hace para copiar los elementos de la cola al nuevo arreglo en el orden correcto, teniendo en cuenta que el frente puede estar en cualquier posición del arreglo original debido a la naturaleza circular de la cola.
        }
        datos=nuevosDatos;
        fondo=tamanioactual;
        frente=0;

    }
}
