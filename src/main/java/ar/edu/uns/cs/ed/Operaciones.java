package ar.edu.uns.cs.ed;

public class Operaciones {
    int operacion; // 1: CREAR_DEBATE, 2: POSTEAR
    String detalle; // Detalle de la operación, como el id del argumento o el id del usuario. El texto de la operación se almacena en mayúsculas para mantener consistencia. Por ejemplo: "CREAR_DEBATE:" + idRaiz.trim()
    String id; // Identificador del argumento o de root en caso de ser el tema raíz. 
     /**
     * @param operacion // 1: CREAR_DEBATE, 2: POSTEAR
     * @param detalle // Detalle de la operación
     * @param id // Identificador del argumento o de root en caso de ser el tema raíz.
     */
    Operaciones(int operacion, String detalle, String id) {
        this.operacion = operacion;
        this.detalle = detalle;
        this.id = id;
    }

    public int getOperacion() {
        return operacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getId() {
        return id;
    }

    
    
}
