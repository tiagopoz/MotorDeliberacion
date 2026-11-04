package ar.edu.uns.cs.ed;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

/**
 * Tests de MotorDeliberacion .
 * En cada test se arma un debate de 16 nodos ("Dar asueto por el día del estudiante"):
 *
 * debate1 (usr1)
 * ├── posteo1  AFAVOR   usr2
 * │   ├── posteo3  ENCONTRA usr3
 * │   │   ├── posteo7  AFAVOR   usr4
 * │   │   └── posteo8  ENCONTRA usr2
 * │   └── posteo4  AFAVOR   usr5
 * │       └── posteo9  ENCONTRA usr1
 * ├── posteo2  ENCONTRA usr3
 * │   ├── posteo5  AFAVOR   usr4
 * │   │   ├── posteo10 ENCONTRA usr5
 * │   │   └── posteo11 AFAVOR   usr3
 * │   └── posteo6  ENCONTRA usr2
 * │       └── posteo12 AFAVOR   usr1
 * └── posteo13 AFAVOR   usr4
 *     ├── posteo14 ENCONTRA usr5
 *     └── posteo15 AFAVOR   usr2
 *
 * Argumentos por usuario: usr1=3, usr2=4, usr3=3, usr4=3, usr5=3.
 */
public class MotorDeliberacionTest {

    private MotorDeliberacion motor;

    private final Usuario usr1 = new Usuario("usr1", "usr1");
    private final Usuario usr2 = new Usuario("usr2", "usr2");
    private final Usuario usr3 = new Usuario("usr3", "usr3");
    private final Usuario usr4 = new Usuario("usr4", "usr4");
    private final Usuario usr5 = new Usuario("usr5", "usr5");

    @Before
    public void setUp() {
        motor = new MotorDeliveracion();
        construirArbolCompleto();
    }

    // Arma el árbol de 16 nodos usado como escenario base.
    private void construirArbolCompleto() {
        motor.crearDebate("debate1", "Dar asueto por el día del estudiante", "usr1", "usr1");

        motor.Postear(usr2, "posteo1", "debate1", "AFAVOR",
                "Los estudiantes merecen un día libre para descanso.");
        motor.Postear(usr3, "posteo2", "debate1", "ENCONTRA",
                "No sería posible porque no alcanzan los días para dar las clases e incluir todos los temas del cronograma.");

        motor.Postear(usr3, "posteo3", "posteo1", "ENCONTRA",
                "Un día libre no es necesario: ya existen fines de semana y feriados para descansar.");
        motor.Postear(usr5, "posteo4", "posteo1", "AFAVOR",
                "El día del estudiante es una fecha simbólica que merece ser reconocida.");

        motor.Postear(usr4, "posteo5", "posteo2", "AFAVOR",
                "El cronograma se puede reorganizar para recuperar la clase perdida.");
        motor.Postear(usr2, "posteo6", "posteo2", "ENCONTRA",
                "Perder un día de clase afecta sobre todo a las materias con poca carga horaria.");

        motor.Postear(usr4, "posteo7", "posteo3", "AFAVOR",
                "Descansar un día mejora la concentración y el rendimiento posterior.");
        motor.Postear(usr2, "posteo8", "posteo3", "ENCONTRA",
                "El descanso ya está contemplado en los recesos del calendario académico.");

        motor.Postear(usr1, "posteo9", "posteo4", "ENCONTRA",
                "Una fecha simbólica puede reconocerse con actividades sin suspender las clases.");

        motor.Postear(usr5, "posteo10", "posteo5", "ENCONTRA",
                "Reorganizar el cronograma sobrecarga a docentes y estudiantes.");
        motor.Postear(usr3, "posteo11", "posteo5", "AFAVOR",
                "Se pueden dar clases virtuales o material de apoyo para compensar.");

        motor.Postear(usr1, "posteo12", "posteo6", "AFAVOR",
                "Las materias afectadas pueden recuperar la clase en horarios extra.");

        motor.Postear(usr4, "posteo13", "debate1", "AFAVOR",
                "Otras instituciones ya dan asueto ese día y no tienen problemas con el calendario.");
        motor.Postear(usr5, "posteo14", "posteo13", "ENCONTRA",
                "Cada institución tiene un calendario distinto, no son comparables.");
        motor.Postear(usr2, "posteo15", "posteo13", "AFAVOR",
                "Puede darse asueto solo cuando la fecha cae en un día hábil sin evaluaciones.");
    }

    // Indica si la lista contiene una opinión con ese id.
    private boolean contiene(PositionList<Opinion> lista, String idArgumento) {
        for (Opinion o : lista) {
            if (o.getIdArgumento().equals(idArgumento)) {
                return true;
            }
        }
        return false;
    }

    // ---------------------- crearDebate ----------------------

    @Test
    public void crearDebate_creaLaRaizYPermitePostearSobreElla() {
        motor.Postear(usr4, "posteoExtra", "debate1", "AFAVOR", "texto");
    }

    @Test
    public void crearDebate_registraAlCreadorEnElIndicePorUsuario() {
        MotorDeliveracion m = new MotorDeliveracion();
        m.crearDebate("d1", "tema", "usr9", "usr9");
        PositionList<Opinion> res = m.consultarArgumentosPorUsuario("usr9");
        assertEquals(1, res.size());
        assertTrue(contiene(res, "d1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void crearDebate_idVacio_lanzaExcepcion() {
        new MotorDeliveracion().crearDebate(" ", "texto", "usr1", "usr1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void crearDebate_textoVacio_lanzaExcepcion() {
        new MotorDeliveracion().crearDebate("d1", "", "usr1", "usr1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void crearDebate_usuarioVacio_lanzaExcepcion() {
        new MotorDeliveracion().crearDebate("d1", "texto", " ", "usr1");
    }

    @Test(expected = IllegalStateException.class)
    public void crearDebate_dosVeces_lanzaExcepcion() {
        // motor ya tiene una raíz creada en setUp().
        motor.crearDebate("debate2", "otroTema", "usr1", "usr1");
    }

    // ---------------------- Postear ----------------------

    @Test
    public void postear_agregaHijoYLoRegistraEnElUsuario() {
        motor.Postear(usr3, "posteoNuevo", "posteo13", "ENCONTRA", "contraargumento");
        // Puede usarse como padre y figura en los argumentos de usr3.
        motor.Postear(usr1, "posteoNieto", "posteoNuevo", "AFAVOR", "texto");
        assertTrue(contiene(motor.consultarArgumentosPorUsuario("usr3"), "posteoNuevo"));
    }

    @Test
    public void postear_posturaEnMinusculas_seNormaliza() {
        motor.Postear(usr1, "posteoMin", "debate1", "afavor", "texto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void postear_padreInexistente_lanzaExcepcion() {
        motor.Postear(usr1, "posteoX", "noExiste", "AFAVOR", "texto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void postear_padreVacio_lanzaExcepcion() {
        motor.Postear(usr1, "posteoX", " ", "AFAVOR", "texto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void postear_idDuplicado_lanzaExcepcion() {
        motor.Postear(usr1, "posteo1", "debate1", "AFAVOR", "texto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void postear_posturaInvalida_lanzaExcepcion() {
        motor.Postear(usr1, "posteoX", "debate1", "NEUTRAL", "texto");
    }

    @Test(expected = IllegalArgumentException.class)
    public void postear_textoVacio_lanzaExcepcion() {
        motor.Postear(usr1, "posteoX", "debate1", "AFAVOR", " ");
    }

    @Test(expected = IllegalStateException.class)
    public void postear_sinDebateCreado_lanzaExcepcion() {
        new MotorDeliveracion().Postear(usr1, "posteoX", "debate1", "AFAVOR", "texto");
    }

    // ---------------------- eliminarPosteo ----------------------

    @Test
    public void eliminarPosteo_eliminaSubarbolCompleto() {
        // Subárbol de posteo1: posteo1, 3, 4, 7, 8, 9.
        motor.eliminarPosteo("usr2", "posteo1");

        try {
            motor.Postear(usr1, "posteoX", "posteo1", "AFAVOR", "texto");
            fail("posteo1 debería haber sido eliminado.");
        } catch (IllegalArgumentException e) {
            // esperado
        }
        try {
            motor.Postear(usr1, "posteoY", "posteo3", "AFAVOR", "texto");
            fail("posteo3 debería haber sido eliminado.");
        } catch (IllegalArgumentException e) {
            // esperado
        }

        // El resto del árbol sigue intacto.
        motor.Postear(usr1, "posteoZ", "debate1", "AFAVOR", "texto");
    }

    @Test
    public void eliminarPosteo_actualizaElIndicePorUsuario() {
        motor.eliminarPosteo("usr2", "posteo1");

        // usr2 pierde posteo1 y posteo8, conserva posteo6 y posteo15.
        PositionList<Opinion> u2 = motor.consultarArgumentosPorUsuario("usr2");
        assertEquals(2, u2.size());
        assertTrue(contiene(u2, "posteo6"));
        assertTrue(contiene(u2, "posteo15"));

        // usr1 pierde posteo9, conserva debate1 y posteo12.
        PositionList<Opinion> u1 = motor.consultarArgumentosPorUsuario("usr1");
        assertEquals(2, u1.size());
        assertFalse(contiene(u1, "posteo9"));

        // usr3, usr4 y usr5 pierden un argumento cada uno.
        assertEquals(2, motor.consultarArgumentosPorUsuario("usr3").size());
        assertEquals(2, motor.consultarArgumentosPorUsuario("usr4").size());
        assertEquals(2, motor.consultarArgumentosPorUsuario("usr5").size());
    }

    @Test
    public void eliminarPosteo_hoja_soloEliminaEseNodo() {
        motor.eliminarPosteo("usr5", "posteo14");
        assertEquals(2, motor.consultarArgumentosPorUsuario("usr5").size());
        assertEquals(4, motor.consultarArgumentosPorUsuario("usr2").size());
    }

    @Test(expected = IllegalStateException.class)
    public void eliminarPosteo_usuarioNoPropietario_lanzaExcepcion() {
        // posteo1 pertenece a usr2.
        motor.eliminarPosteo("usr1", "posteo1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void eliminarPosteo_idInexistente_lanzaExcepcion() {
        motor.eliminarPosteo("usr1", "noExiste");
    }

    @Test(expected = IllegalArgumentException.class)
    public void eliminarPosteo_usuarioVacio_lanzaExcepcion() {
        motor.eliminarPosteo(" ", "posteo1");
    }

    @Test(expected = IllegalStateException.class)
    public void eliminarPosteo_raiz_dejaElDebateVacio() {
        motor.eliminarPosteo("usr1", "debate1");
        // Sin raíz, no se puede postear.
        motor.Postear(usr2, "posteoX", "debate1", "AFAVOR", "texto");
    }

    @Test
    public void eliminarPosteo_raiz_limpiaElIndicePorUsuario() {
        // Ahora la raíz se elimina con clearDebate(), que reinicia todo.
        motor.eliminarPosteo("usr1", "debate1");
        assertTrue(motor.consultarArgumentosPorUsuario("usr1").isEmpty());
        assertTrue(motor.consultarArgumentosPorUsuario("usr2").isEmpty());
    }

    // ---------------------- clearDebate ----------------------

    @Test(expected = IllegalStateException.class)
    public void clearDebate_dejaElArbolVacio() {
        motor.clearDebate();
        motor.Postear(usr2, "posteoX", "debate1", "AFAVOR", "texto");
    }

    @Test
    public void clearDebate_limpiaElIndicePorUsuario() {
        motor.clearDebate();
        assertTrue(motor.consultarArgumentosPorUsuario("usr1").isEmpty());
        assertTrue(motor.consultarArgumentosPorUsuario("usr2").isEmpty());
    }

    @Test(expected = EmptyStackException.class)
    public void clearDebate_vaciaLaPilaDeOperaciones() throws Exception {
        motor.clearDebate();
        motor.deshacerUltimaOperacion();
    }

    @Test
    public void clearDebate_permiteCrearUnDebateNuevo() {
        motor.clearDebate();
        motor.crearDebate("debateNuevo", "OtroTema", "usr9", "usr9");
        assertEquals(1, motor.consultarArgumentosPorUsuario("usr9").size());
    }

    // ---------------------- consultarArgumentosPorUsuario ----------------------

    @Test
    public void consultarArgumentosPorUsuario_devuelveTodosLosDelUsuario() {
        PositionList<Opinion> u2 = motor.consultarArgumentosPorUsuario("usr2");
        assertEquals(4, u2.size());
        assertTrue(contiene(u2, "posteo1"));
        assertTrue(contiene(u2, "posteo6"));
        assertTrue(contiene(u2, "posteo8"));
        assertTrue(contiene(u2, "posteo15"));
    }

    @Test
    public void consultarArgumentosPorUsuario_incluyeLaRaizDelCreador() {
        PositionList<Opinion> u1 = motor.consultarArgumentosPorUsuario("usr1");
        assertEquals(3, u1.size());
        assertTrue(contiene(u1, "debate1"));
        assertTrue(contiene(u1, "posteo9"));
        assertTrue(contiene(u1, "posteo12"));
    }

    @Test
    public void consultarArgumentosPorUsuario_noMezclaUsuarios() {
        PositionList<Opinion> u4 = motor.consultarArgumentosPorUsuario("usr4");
        assertEquals(3, u4.size());
        for (Opinion o : u4) {
            assertEquals("usr4", o.getUsuario().getId());
        }
    }

    @Test
    public void consultarArgumentosPorUsuario_usuarioSinArgumentos_devuelveVacio() {
        assertTrue(motor.consultarArgumentosPorUsuario("usuarioQueNoExiste").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void consultarArgumentosPorUsuario_idVacio_lanzaExcepcion() {
        motor.consultarArgumentosPorUsuario(" ");
    }

    // ---------------------- registrarOperacion / deshacerUltimaOperacion ----------------------

    @Test(expected = IllegalArgumentException.class)
    public void registrarOperacion_codigoInvalido_lanzaExcepcion() {
        motor.registrarOperacion(9, "detalle", "id");
    }

    @Test(expected = EmptyStackException.class)
    public void deshacer_sinOperaciones_lanzaExcepcion() throws Exception {
        new MotorDeliveracion().deshacerUltimaOperacion();
    }

    @Test
    public void deshacer_ultimoPosteo_loElimina() throws Exception {
        // La última operación de setUp() es el posteo15 (usr2).
        String msg = motor.deshacerUltimaOperacion();
        assertTrue(msg.contains("posteo15"));
        assertEquals(3, motor.consultarArgumentosPorUsuario("usr2").size());
        try {
            motor.Postear(usr1, "posteoX", "posteo15", "AFAVOR", "texto");
            fail("posteo15 debería haber sido eliminado.");
        } catch (IllegalArgumentException e) {
            // esperado
        }
    }

    @Test(expected = IllegalStateException.class)
    public void deshacer_despuesDeEliminar_noSePuedeDeshacer() throws Exception {
        motor.eliminarPosteo("usr5", "posteo14");
        try{
            motor.deshacerUltimaOperacion();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("No se pudo deshacer la operación POSTEAR"));
            throw e;
        }
        motor.deshacerUltimaOperacion();
    }

    // Tests del comportamiento esperado; hoy fallan por bugs (ver motivo en @Ignore).

    //@Ignore("Bug: clearDebate() vacía la pila y luego top() lanza EmptyStackException")
    @Test
    public void deshacer_crearDebate_limpiaElDebate() throws Exception {
        MotorDeliveracion m = new MotorDeliveracion();
        m.crearDebate("d1", "tema", "usr1", "usr1");
        m.deshacerUltimaOperacion();
        try {
            m.Postear(usr1, "posteoX", "d1", "AFAVOR", "texto");
            fail("El debate debería estar vacío.");
        } catch (IllegalStateException e) {
            // esperado
        }
    }

    @Test
    public void deshacer_dosPosteosSeguidos_eliminaAmbos() throws Exception {
        motor.deshacerUltimaOperacion(); // posteo15 (usr2)
        motor.deshacerUltimaOperacion(); // posteo14 (usr5)
        
        assertEquals(3, motor.consultarArgumentosPorUsuario("usr2").size());
        assertEquals(2, motor.consultarArgumentosPorUsuario("usr5").size());
    }

    @Test public void mostrarDebate() {
        System.out.println("Árbol de debate:");
        motor.imprimirDebate();
    }
}