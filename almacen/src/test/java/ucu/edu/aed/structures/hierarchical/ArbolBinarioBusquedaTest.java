package ucu.edu.aed.structures.hierarchical;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ucu.edu.aed.structures.element.Nodo;
import ucu.edu.aed.tda.element.TDAElemento;

class ArbolBinarioBusquedaTest {

    // =========================================================
    // CONSTRUCTORES Y ESTADO INICIAL
    // =========================================================

    @Test
    void constructorVacioDebeCrearArbolSinRaiz() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertTrue(arbol.esVacio());
        assertNull(arbol.obtenerRaiz());
    }

    @Test
    void constructorConRaizDebeInicializarArbol() {
        Nodo<Integer> raiz = new Nodo<>(10);

        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>(raiz);

        assertFalse(arbol.esVacio());
        assertSame(raiz, arbol.obtenerRaiz());
        assertEquals(10, arbol.obtenerRaiz().getDato());
    }


    // =========================================================
    // INSERTAR
    // =========================================================

    @Test
    void primeraInsercionDebeCrearRaiz() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        boolean insertado = arbol.insertar(10);

        assertTrue(insertado);
        assertFalse(arbol.esVacio());
        assertNotNull(arbol.obtenerRaiz());
        assertEquals(10, arbol.obtenerRaiz().getDato());
    }

    @Test
    void insertarDebeMantenerPropiedadDelABB() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        List<Integer> recorrido = new ArrayList<>();
        arbol.inOrder(recorrido::add);

        assertEquals(
                Arrays.asList(2, 5, 7, 10, 12, 15, 20),
                recorrido);
    }

    @Test
    void insertarDuplicadoDebeRetornarFalse() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertTrue(arbol.insertar(10));
        assertFalse(arbol.insertar(10));

        assertEquals(1, arbol.cantidadNodos());
    }

    @Test
    void insertarDebeRechazarDatoNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.insertar(null));
    }


    // =========================================================
    // BUSCAR
    // =========================================================

    @Test
    void buscarEnArbolVacioDebeRetornarNull() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertNull(arbol.buscar(10));
    }

    @Test
    void buscarDebeEncontrarRaiz() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        assertEquals(10, arbol.buscar(10));
    }

    @Test
    void buscarDebeEncontrarElementoEnSubarbolIzquierdo() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        assertEquals(2, arbol.buscar(2));
    }

    @Test
    void buscarDebeEncontrarElementoEnSubarbolDerecho() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        assertEquals(20, arbol.buscar(20));
    }

    @Test
    void buscarDatoInexistenteDebeRetornarNull() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        assertNull(arbol.buscar(100));
    }

    @Test
    void buscarDebeRechazarCriterioNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.buscar(null));
    }


    // =========================================================
    // ELIMINAR
    // =========================================================

    @Test
    void eliminarEnArbolVacioDebeRetornarFalse() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertFalse(arbol.eliminar(10));
    }

    @Test
    void eliminarDebeRechazarCriterioNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.eliminar(null));
    }

    @Test
    void eliminarDatoInexistenteDebeRetornarFalse() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        assertFalse(arbol.eliminar(100));

        assertEquals(7, arbol.cantidadNodos());
    }

    @Test
    void eliminarElementoQueNoEsRaizDebeDelegarCorrectamente() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        boolean eliminado = arbol.eliminar(5);

        assertTrue(eliminado);
        assertNull(arbol.buscar(5));

        assertEquals(
                Arrays.asList(2, 7, 10, 12, 15, 20),
                obtenerInOrder(arbol));
    }


    // =========================================================
    // ELIMINACIÓN DE LA RAÍZ
    // =========================================================

    @Test
    void eliminarRaizHojaDebeDejarArbolVacio() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);

        boolean eliminado = arbol.eliminar(10);

        assertTrue(eliminado);
        assertTrue(arbol.esVacio());
        assertNull(arbol.obtenerRaiz());
    }

    @Test
    void eliminarRaizConSoloHijoIzquierdoDebePromoverHijo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);
        arbol.insertar(5);

        boolean eliminado = arbol.eliminar(10);

        assertTrue(eliminado);

        assertNotNull(arbol.obtenerRaiz());
        assertEquals(5, arbol.obtenerRaiz().getDato());

        assertNull(arbol.buscar(10));
        assertEquals(1, arbol.cantidadNodos());
    }

    @Test
    void eliminarRaizConSoloHijoDerechoDebePromoverHijo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);
        arbol.insertar(15);

        boolean eliminado = arbol.eliminar(10);

        assertTrue(eliminado);

        assertNotNull(arbol.obtenerRaiz());
        assertEquals(15, arbol.obtenerRaiz().getDato());

        assertNull(arbol.buscar(10));
        assertEquals(1, arbol.cantidadNodos());
    }

    @Test
    void eliminarRaizConDosHijosDebeUsarSucesorInOrder() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);
        arbol.insertar(5);
        arbol.insertar(15);

        boolean eliminado = arbol.eliminar(10);

        assertTrue(eliminado);

        // El sucesor inmediato de 10 es 15.
        assertEquals(15, arbol.obtenerRaiz().getDato());

        assertNull(arbol.buscar(10));

        assertEquals(
                Arrays.asList(5, 15),
                obtenerInOrder(arbol));
    }

    @Test
    void eliminarRaizConSucesorProfundoDebeReconectarSubarboles() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        /*
         *              10
         *             /  \
         *            5    20
         *                /  \
         *               15   30
         *              /  \
         *             12   17
         *
         * El sucesor in-order de 10 es 12.
         */

        arbol.insertar(10);
        arbol.insertar(5);
        arbol.insertar(20);
        arbol.insertar(15);
        arbol.insertar(30);
        arbol.insertar(12);
        arbol.insertar(17);

        boolean eliminado = arbol.eliminar(10);

        assertTrue(eliminado);

        assertEquals(12, arbol.obtenerRaiz().getDato());
        assertNull(arbol.buscar(10));

        assertEquals(
                Arrays.asList(5, 12, 15, 17, 20, 30),
                obtenerInOrder(arbol));

        assertEquals(6, arbol.cantidadNodos());
    }


    // =========================================================
    // RECORRIDOS
    // =========================================================

    @Test
    void inOrderDebeRecorrerElementosOrdenados() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        List<Integer> recorrido = new ArrayList<>();

        arbol.inOrder(recorrido::add);

        assertEquals(
                Arrays.asList(2, 5, 7, 10, 12, 15, 20),
                recorrido);
    }

    @Test
    void preOrderDebeRecorrerRaizIzquierdaDerecha() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        List<Integer> recorrido = new ArrayList<>();

        arbol.preOrder(recorrido::add);

        assertEquals(
                Arrays.asList(10, 5, 2, 7, 15, 12, 20),
                recorrido);
    }

    @Test
    void postOrderDebeRecorrerIzquierdaDerechaRaiz() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        List<Integer> recorrido = new ArrayList<>();

        arbol.postOrder(recorrido::add);

        assertEquals(
                Arrays.asList(2, 7, 5, 12, 20, 15, 10),
                recorrido);
    }

    @Test
    void porNivelesDebeRealizarRecorridoBFS() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        List<Integer> recorrido = new ArrayList<>();

        arbol.porNiveles(recorrido::add);

        assertEquals(
                Arrays.asList(10, 5, 15, 2, 7, 12, 20),
                recorrido);
    }


    // =========================================================
    // RECORRIDOS SOBRE ÁRBOL VACÍO
    // =========================================================

    @Test
    void recorridosSobreArbolVacioNoDebenProcesarElementos() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        List<Integer> inOrder = new ArrayList<>();
        List<Integer> preOrder = new ArrayList<>();
        List<Integer> postOrder = new ArrayList<>();
        List<Integer> niveles = new ArrayList<>();

        arbol.inOrder(inOrder::add);
        arbol.preOrder(preOrder::add);
        arbol.postOrder(postOrder::add);
        arbol.porNiveles(niveles::add);

        assertTrue(inOrder.isEmpty());
        assertTrue(preOrder.isEmpty());
        assertTrue(postOrder.isEmpty());
        assertTrue(niveles.isEmpty());
    }


    // =========================================================
    // POLÍTICA NULL DE RECORRIDOS
    // =========================================================

    @Test
    void inOrderDebeRechazarConsumidorNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.inOrder(null));
    }

    @Test
    void preOrderDebeRechazarConsumidorNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.preOrder(null));
    }

    @Test
    void postOrderDebeRechazarConsumidorNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.postOrder(null));
    }

    @Test
    void porNivelesDebeRechazarConsumidorNulo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.porNiveles(null));
    }


    // =========================================================
    // INFORMACIÓN ESTRUCTURAL
    // =========================================================

    @Test
    void arbolVacioDebeTenerCantidadesYAlturaEnCero() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        assertEquals(0, arbol.cantidadNodos());
        assertEquals(0, arbol.cantidadHojas());
        assertEquals(0, arbol.cantidadNodosInternos());
        assertEquals(0, arbol.altura());
    }

    @Test
    void arbolConUnaRaizDebeTenerValoresCorrectos() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);

        assertEquals(1, arbol.cantidadNodos());
        assertEquals(1, arbol.cantidadHojas());
        assertEquals(0, arbol.cantidadNodosInternos());
        assertEquals(1, arbol.altura());
    }

    @Test
    void informacionEstructuralDebeSerCorrecta() {
        ArbolBinarioBusqueda<Integer> arbol =
                crearArbolEjemplo();

        assertEquals(7, arbol.cantidadNodos());
        assertEquals(4, arbol.cantidadHojas());
        assertEquals(3, arbol.cantidadNodosInternos());
        assertEquals(3, arbol.altura());
    }

    @Test
    void alturaDebeRepresentarCaminoMasLargo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);
        arbol.insertar(40);
        arbol.insertar(50);

        assertEquals(5, arbol.altura());
    }


    // =========================================================
    // ACCESO PROTEGIDO PARA SUBCLASES
    // =========================================================

    @Test
    void subclaseDebePoderEstablecerNuevaRaiz() {
        ArbolBinarioBusquedaExpuesto<Integer> arbol =
                new ArbolBinarioBusquedaExpuesto<>();

        Nodo<Integer> nuevaRaiz = new Nodo<>(20);

        arbol.cambiarRaiz(nuevaRaiz);

        assertSame(nuevaRaiz, arbol.obtenerRaiz());
        assertEquals(20, arbol.obtenerRaiz().getDato());
    }

    @Test
    void subclaseDebePoderConsultarSiRaizEsNula() {
        ArbolBinarioBusquedaExpuesto<Integer> arbol =
                new ArbolBinarioBusquedaExpuesto<>();

        assertTrue(arbol.consultarRaizEsNula());

        arbol.cambiarRaiz(new Nodo<>(10));

        assertFalse(arbol.consultarRaizEsNula());
    }

    @Test
    void subclaseDebePoderConvertirComparableADato() {
        ArbolBinarioBusquedaExpuesto<Integer> arbol =
                new ArbolBinarioBusquedaExpuesto<>();

        Integer dato = arbol.convertir(10);

        assertEquals(10, dato);
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================

    /**
     * Crea el ABB utilizado en varios tests:
     *
     *              10
     *            /    \
     *           5      15
     *          / \    /  \
     *         2   7  12  20
     */
    private ArbolBinarioBusqueda<Integer> crearArbolEjemplo() {
        ArbolBinarioBusqueda<Integer> arbol =
                new ArbolBinarioBusqueda<>();

        arbol.insertar(10);
        arbol.insertar(5);
        arbol.insertar(15);
        arbol.insertar(2);
        arbol.insertar(7);
        arbol.insertar(12);
        arbol.insertar(20);

        return arbol;
    }

    /**
     * Obtiene el recorrido in-order para comprobar fácilmente
     * que el ABB mantiene su propiedad de orden.
     */
    private List<Integer> obtenerInOrder(
            ArbolBinarioBusqueda<Integer> arbol) {

        List<Integer> recorrido = new ArrayList<>();

        arbol.inOrder(recorrido::add);

        return recorrido;
    }


    // =========================================================
    // SUBCLASE AUXILIAR PARA PROBAR ACCESO PROTECTED
    // =========================================================

    /**
     * Esta clase existe únicamente para comprobar que los métodos
     * protected de ArbolBinarioBusqueda están disponibles para una
     * futura subclase como ArbolAVL.
     */
    private static class ArbolBinarioBusquedaExpuesto<T>
            extends ArbolBinarioBusqueda<T> {

        void cambiarRaiz(TDAElemento<T> nuevaRaiz) {
            establecerRaiz(nuevaRaiz);
        }

        boolean consultarRaizEsNula() {
            return raizEsNula();
        }

        T convertir(Comparable<T> dato) {
            return convertirADato(dato);
        }
    }
}