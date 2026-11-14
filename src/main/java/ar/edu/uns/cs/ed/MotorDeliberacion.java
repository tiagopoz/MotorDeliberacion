package ar.edu.uns.cs.ed;

import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;
import ar.edu.uns.cs.ed.tdas.tdaarbol.TDAArbol;
import ar.edu.uns.cs.ed.tdas.tdaarbol.Tree;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.DiccionarioHashAbierto;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.MapHashAbierto;
import ar.edu.uns.cs.ed.tdas.tdapila.Stack;
import ar.edu.uns.cs.ed.tdas.tdapila.TDAPila;

/**
 * Clase que representa el motor de deliveración.
 * 
 */
public class MotorDeliberacion {
// Estructuras principales de datos
    private Tree<Opinion> arbol;
    private Map<String, Position<Opinion>> mapaOpiniones;
    private Map<String, PositionList<Opinion>> mapaUsuarios;
    private Map<String, Usuario> usuarios;
    private Stack<Operaciones> pilaOperaciones;

    /**
     * Constructor de la clase MotorDeliveracion.
     * Inicializa el árbol de debate, y las estructuras correspondientes.  
     * 
     */
    public MotorDeliberacion() {
    //TODO: Inicializar el árbol de debate y las estructuras correspondientes. 
        arbol = new TDAArbol<Opinion>();
        mapaOpiniones = new MapHashAbierto<String, Position<Opinion>>();
        mapaUsuarios = new MapHashAbierto<String, PositionList<Opinion>>();
        usuarios = new MapHashAbierto<String, Usuario>();
        pilaOperaciones = new TDAPila<Operaciones>();
    }

    /**
     * Registra una operación en la estructura que almacena las operaciones.
     * Las siguientes operaciones se pueden deshacer: "CREAR_DEBATE", "POSTEAR". El resto de las operaciones no se pueden deshacer.
     * 
     * La operación eliminar posteo no se puede deshacer, y si se intenta deshacer una operación cuando no hay operaciones registradas, se lanza una excepción.
     * 
     * 
     * @param operacion : La operación a registrar. 1: CREAR_DEBATE, 2: POSTEAR
     * @param detalle : Detalle de la operación, como el id del argumento o el id del usuario. El texto de la operación se almacena en mayúsculas para mantener consistencia. Por ejemplo: "CREAR_DEBATE:" + idRaiz.trim()
     * @param idArgumento : Identificador del argumento o de root en caso de ser el tema raíz.
     * @throws IllegalArgumentException si la operación es nula o vacía.
     */
    public void registrarOperacion(int operacion, String detalle, String idArgumento) {
        //TODO: Implementar el registro de la operación en la estructura correspondiente.
        if (detalle == null || detalle.trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle de la operación no puede ser nulo ni vacío.");
        }
        if (idArgumento == null || idArgumento.trim().isEmpty()) {
            throw new IllegalArgumentException("El idArgumento no puede ser nulo ni vacío.");
        }
        if (operacion != 1 && operacion != 2) {
            throw new IllegalArgumentException("Código de operación inválido. Solo se permite 1 (CREAR_DEBATE) o 2 (POSTEAR).");
        }

        Operaciones op = new Operaciones(operacion, detalle.trim(), idArgumento.trim());
        pilaOperaciones.push(op);
    }

/**
 * Deshace la última operación registrada en el motor de deliveración.
 * La operación eliminar posteo no se puede deshacer, y si se intenta deshacer una operación cuando no hay operaciones registradas, se lanza una excepción.
 * @return
 * @throws EmptyStackException
 */
    public String deshacerUltimaOperacion() throws EmptyStackException {
        //TODO: Implementar la funcionalidad de deshacer la última operación registrada.
        if (pilaOperaciones.isEmpty()) {
            throw new EmptyStackException("No hay operaciones registradas para deshacer.");
        }

        Operaciones op = pilaOperaciones.pop();
        int tipo = op.getOperacion();
        String idArg = op.getId();
        String detalle = op.getDetalle();

        if (tipo == 1) {
            clearDebate();
        } else if (tipo == 2) {
            Position<Opinion> posNodo = mapaOpiniones.get(idArg);
            if (posNodo == null) {
                throw new IllegalStateException("No se pudo deshacer la operación POSTEAR: el posteo ya fue eliminado.");
            }
            Opinion opNodo = posNodo.element();
            eliminarPosteo(opNodo.getUsuario().getId(), idArg);
        }

        return detalle;
    }
/**
 * Crea un nuevo debate con un argumento raíz.
 * @param idRaiz   : Identificador único del tema raíz.
 * @param texto:    Texto del tema raíz.
 * @param idUsuario: Identificador del usuario.
 * @param nombreUsuario: Nombre del usuario.
 * 
 * El método valida que los parámetros no sean nulos o vacíos, y que el identificador de la raíz sea numérico.
 * Si el árbol ya tiene un debate, lanza una excepción.
 * El método registra la operación en la pila de operaciones para permitir deshacerla posteriormente.
 * @throws IllegalArgumentException si algún parámetro es inválido.
 * @throws IllegalStateException si ya existe un debate en curso.
 * Crea un usuario y un tema raíz, y los agrega al árbol y a las estructuras correspondientes.
 */
    public void crearDebate(String idRaiz, String texto, String idUsuario, String nombreUsuario) {
    //TODO: Implementar la creación del debate con el argumento raíz y el usuario correspondiente.
    if (idRaiz == null || idRaiz.trim().isEmpty() ||
            texto == null || texto.trim().isEmpty() ||
            idUsuario == null || idUsuario.trim().isEmpty() ||
            nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Ninguno de los parámetros puede ser nulo o vacío.");
        }

        if (!arbol.isEmpty()) {
            throw new IllegalStateException("Ya existe un debate en curso.");
        }

        String idR = idRaiz.trim();
        String idU = idUsuario.trim();

        Usuario usr = new Usuario(idU, nombreUsuario.trim());
        Opinion opRaiz = new Opinion(idR, texto.trim(), "ROOT", usr);

        arbol.createRoot(opRaiz);
        Position<Opinion> posRaiz = arbol.root();

        mapaOpiniones.put(idR, posRaiz);
        usuarios.put(idU, usr);

        PositionList<Opinion> listaUser = new ListaDoblementeEnlazada<Opinion>();
        listaUser.addLast(opRaiz);
        mapaUsuarios.put(idU, listaUser);

        registrarOperacion(1, "CREAR_DEBATE:" + idR, idR);
    }

    public void Postear(Usuario usr, String idNuevo, String idPadre, String postura, String texto) {
        //TODO: Implementar la funcionalidad de postear un nuevo argumento en el debate.
        if (arbol.isEmpty()) {
            throw new IllegalStateException("No existe un debate activo para postear.");
        }

        if (usr == null || usr.getId() == null || usr.getId().trim().isEmpty() ||
            idNuevo == null || idNuevo.trim().isEmpty() ||
            idPadre == null || idPadre.trim().isEmpty() ||
            postura == null || postura.trim().isEmpty() ||
            texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Ninguno de los parámetros puede ser nulo o vacío.");
        }

        String idN = idNuevo.trim();
        String idP = idPadre.trim();
        String postNorm = postura.trim().toUpperCase();

        if (!postNorm.equals("AFAVOR") && !postNorm.equals("ENCONTRA")) {
            throw new IllegalArgumentException("La postura debe ser AFAVOR o ENCONTRA.");
        }

        if (mapaOpiniones.get(idN) != null) {
            throw new IllegalArgumentException("Ya existe una opinión con el identificador: " + idN);
        }

        Position<Opinion> posPadre = mapaOpiniones.get(idP);
        if (posPadre == null) {
            throw new IllegalArgumentException("No existe el argumento padre con id: " + idP);
        }

        Opinion nuevaOpinion = new Opinion(idN, texto.trim(), postNorm, usr);
        Position<Opinion> posNueva = arbol.addLastChild(posPadre, nuevaOpinion);

        mapaOpiniones.put(idN, posNueva);

        String idU = usr.getId().trim();
        if (usuarios.get(idU) == null) {
            usuarios.put(idU, usr);
        }

        PositionList<Opinion> listaUser = mapaUsuarios.get(idU);
        if (listaUser == null) {
            listaUser = new ListaDoblementeEnlazada<Opinion>();
            mapaUsuarios.put(idU, listaUser);
        }
        listaUser.addLast(nuevaOpinion);

        registrarOperacion(2, "POSTEAR:" + idN, idN);
    }


    // Elimina un posteo y todo el subárbol.
    public void eliminarPosteo(String idUsuario, String idPosteo) {
    //TODO: Implementar la funcionalidad de eliminar un posteo y todo su subárbol en el debate.
    if (idUsuario == null || idUsuario.trim().isEmpty() ||
            idPosteo == null || idPosteo.trim().isEmpty()) {
            throw new IllegalArgumentException("El idUsuario y el idPosteo no pueden ser nulos ni vacíos.");
        }

        if (arbol.isEmpty()) {
            throw new IllegalStateException("No existe un debate activo.");
        }

        String idU = idUsuario.trim();
        String idP = idPosteo.trim();

        Position<Opinion> posPosteo = mapaOpiniones.get(idP);
        if (posPosteo == null) {
            throw new IllegalArgumentException("No existe el posteo con id: " + idP);
        }

        Opinion opPosteo = posPosteo.element();
        if (!opPosteo.getUsuario().getId().equals(idU)) {
            throw new IllegalStateException("El usuario " + idU + " no es el propietario del posteo " + idP + ".");
        }

        // Caso especial: si el posteo a eliminar es la raíz del debate, se limpia todo
        if (arbol.isRoot(posPosteo)) {
            clearDebate();
            return;
        }

        // Recolectamos en postorden todos los IDs y posiciones del subárbol a eliminar
        PositionList<String> idsSubarbol = new ListaDoblementeEnlazada<String>();
        PositionList<Position<Opinion>> posicionesSubarbol = new ListaDoblementeEnlazada<Position<Opinion>>();
        obtenerIdsYPosicionesSubarbol(posPosteo, idsSubarbol, posicionesSubarbol);

        // 1. Eliminamos cada nodo del mapa de opiniones y de la lista de su respectivo usuario
        for (Position<Opinion> pos : posicionesSubarbol) {
            Opinion op = pos.element();
            mapaOpiniones.remove(op.getIdArgumento());

            PositionList<Opinion> listaUser = mapaUsuarios.get(op.getUsuario().getId());
            if (listaUser != null) {
                for (Position<Opinion> posLista : listaUser.positions()) {
                    if (posLista.element().getIdArgumento().equals(op.getIdArgumento())) {
                        listaUser.remove(posLista);
                        break;
                    }
                }
            }
        }

        // 2. Como posicionesSubarbol está en postorden (hijos antes que padres),
        // todos los nodos se eliminan siendo hojas en TDAArbol
        for (Position<Opinion> pos : posicionesSubarbol) {
            arbol.removeNode(pos);
        }
    }

    //obtiene las posiciones en postorden.
    private void obtenerIdsYPosicionesSubarbol(Position<Opinion> nodo, PositionList<String> ids, PositionList<Position<Opinion>> posiciones) {
    //TODO: Implementar la funcionalidad de obtener los ids y posiciones de un subárbol en postorden.
    if (nodo == null) {
            return;
        }

        // 1. Primero recorremos recursivamente todos los hijos (postorden)
        for (Position<Opinion> hijo : arbol.children(nodo)) {
            obtenerIdsYPosicionesSubarbol(hijo, ids, posiciones);
        }

        // 2. Al final procesamos el nodo actual (agregando su ID y su Position)
        ids.addLast(nodo.element().getIdArgumento());
        posiciones.addLast(nodo);
    }

    //limpia todo el debate, dejando la estructura vacía.   
    public void clearDebate() {
    //TODO: Implementar la funcionalidad de limpiar todo el debate, dejando la estructura vacía.
    arbol = new TDAArbol<Opinion>();
        mapaOpiniones = new MapHashAbierto<String, Position<Opinion>>();
        mapaUsuarios = new MapHashAbierto<String, PositionList<Opinion>>();
        usuarios = new MapHashAbierto<String, Usuario>();
        pilaOperaciones = new TDAPila<Operaciones>();
    }

    //consultar por usuario, devuelve una lista de argumentos que pertenecen a ese usuario.
    public PositionList<Opinion> consultarArgumentosPorUsuario(String idUsuario) {
    //TODO: Implementar la funcionalidad de consultar los argumentos de un usuario específico en el debate.
    if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El idUsuario no puede ser nulo ni vacío.");
        }

        PositionList<Opinion> lista = mapaUsuarios.get(idUsuario.trim());
        if (lista == null) {
            return new ListaDoblementeEnlazada<Opinion>();
        }

        return lista;
    }

    /**
     * Calcula la polarización de todos los nodos del debate.
     * La polarización se define como la cantidad de argumentos a favor y en contra que existen en el subárbol que tiene como raíz cada nodo.
     * @throws IllegalStateException si no hay un debate activo.
     * @return Un diccionario cuya clave es el ID del nodo y el valor es un diccionario con la polarización: 
     * "AFAVOR" y "ENCONTRA" y sus respectivos conteos.
     */
    public DiccionarioHashAbierto<String,DiccionarioHashAbierto<String,Integer>> calcularPolarizacionDebate(){
        //TODO
        if (arbol.isEmpty()) {
            throw new IllegalStateException("No hay un debate activo.");
        }

        DiccionarioHashAbierto<String, DiccionarioHashAbierto<String, Integer>> resultado = new DiccionarioHashAbierto<String, DiccionarioHashAbierto<String, Integer>>();
        calcularPolarizacionPostorden(arbol.root(), resultado);
        return resultado;
    }

    private DiccionarioHashAbierto<String, Integer> calcularPolarizacionPostorden(
            Position<Opinion> nodo,
            DiccionarioHashAbierto<String, DiccionarioHashAbierto<String, Integer>> resultado) {
        
        int aFavor = 0;
        int enContra = 0;

        // 1. Recorremos primero todos los hijos (Postorden) y acumulamos sus conteos
        for (Position<Opinion> hijo : arbol.children(nodo)) {
            DiccionarioHashAbierto<String, Integer> polHijo = calcularPolarizacionPostorden(hijo, resultado);
            aFavor += polHijo.find("AFAVOR").getValue();
            enContra += polHijo.find("ENCONTRA").getValue();
        }

        // 2. Sumamos el aporte del nodo actual (siempre que no sea "ROOT")
        Opinion op = nodo.element();
        String postura = op.getPostura();
        if ("AFAVOR".equals(postura)) {
            aFavor++;
        } else if ("ENCONTRA".equals(postura)) {
            enContra++;
        }

        // 3. Armamos el diccionario interno del nodo y lo insertamos en el diccionario global
        DiccionarioHashAbierto<String, Integer> polNodo = new DiccionarioHashAbierto<String, Integer>();
        polNodo.insert("AFAVOR", aFavor);
        polNodo.insert("ENCONTRA", enContra);

        resultado.insert(op.getIdArgumento(), polNodo);
        return polNodo;
    }

     /**
    * Calcula la polarización de un nodo específico en el árbol de debate.
    * La polarización se define como la cantidad de argumentos a favor y en contra que existen en el subárbol que tiene como raíz el nodo especificado por idNodo.
    * @throws IllegalStateException si no hay un debate activo. 
    * @param idNodo : Identificador del nodo para el cual se desea calcular la polarización.
    * @return Un DiccionarioHashAbierto<String,Integer> que contiene la polarización del nodo, con las claves "AFAVOR" y "ENCONTRA" y sus respectivos conteos.
    */
    public DiccionarioHashAbierto<String,Integer> calcularPolarizacionNodo(String idNodo){
        //TODO
        if (arbol.isEmpty()) {
            throw new IllegalStateException("No hay un debate activo.");
        }

        if (idNodo == null || idNodo.trim().isEmpty()) {
            throw new IllegalArgumentException("El idNodo no puede ser nulo ni vacío.");
        }

        Position<Opinion> posNodo = mapaOpiniones.get(idNodo.trim());
        if (posNodo == null) {
            throw new IllegalArgumentException("No existe un nodo con el identificador: " + idNodo);
        }

        int[] conteos = new int[2]; // conteos[0] = AFAVOR, conteos[1] = ENCONTRA
        contarPolarizacionSubarbol(posNodo, conteos);

        DiccionarioHashAbierto<String, Integer> resultado = new DiccionarioHashAbierto<String, Integer>();
        resultado.insert("AFAVOR", conteos[0]);
        resultado.insert("ENCONTRA", conteos[1]);
        return resultado;
    }

    private void contarPolarizacionSubarbol(Position<Opinion> nodo, int[] conteos) {
        String postura = nodo.element().getPostura();
        if ("AFAVOR".equals(postura)) {
            conteos[0]++;
        } else if ("ENCONTRA".equals(postura)) {
            conteos[1]++;
        }

        for (Position<Opinion> hijo : arbol.children(nodo)) {
            contarPolarizacionSubarbol(hijo, conteos);
        }

    }
 /*** Mostrar árbol por consola ***/

    /**
     * Imprime por consola, en modo texto, el contenido del árbol de debate,
     * con indentación jerárquica que muestra la estructura padre-hijo.
     * Cada nodo se muestra con su id, postura, usuario y texto del argumento.
     */
    public void imprimirDebate() {
        if (arbol == null || arbol.isEmpty()) {
            System.out.println("(El árbol de debate está vacío.)");
            return;
        }

        try {
            Position<Opinion> raiz = arbol.root();
            System.out.println(formatearNodo(raiz.element()));
            imprimirHijos(raiz, "");
        } catch (Exception e) {
            System.out.println("No se pudo imprimir el árbol: " + e.getMessage());
        }
    }

    /**
     * Imprime recursivamente los hijos de 'nodo', usando 'prefijo' para
     * mantener la indentación acumulada de los niveles superiores y
     * conectores tipo "├──" / "└──" para el nivel actual.
     */
    private void imprimirHijos(Position<Opinion> nodo, String prefijo) throws InvalidPositionException {
        PositionList<Position<Opinion>> hijos = new ListaDoblementeEnlazada<Position<Opinion>>();
        for (Position<Opinion> hijo : arbol.children(nodo)) {
            hijos.addLast(hijo);
        }


        for (Position<Opinion> hijo : arbol.children(nodo)) {
            boolean esUltimo = (hijo == hijos.last().element());

            String conector = esUltimo ? "└── " : "├── ";
            System.out.println(prefijo + conector + formatearNodo(hijo.element()));

            String prefijoHijo = prefijo + (esUltimo ? "    " : "│   ");
            imprimirHijos(hijo, prefijoHijo);

        }
    }

/** Arma la línea de texto que representa a un Argumento en la impresión. */
    private String formatearNodo(Opinion a) {
        if (a == null) {
            return "(argumento nulo)";
        }
        return "[" + a.getIdArgumento() + "] (" + a.getPostura() + ") "
                + "usuario=" + a.getUsuario().getId() + " -> " + a.getTexto();
    }
}