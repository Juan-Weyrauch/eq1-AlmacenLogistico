package ucu.edu.aed.structures.element;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ucu.edu.aed.tda.element.TDAElemento;

/* Todo lo que cubre esta clase:
[x] constructor simple
[x] constructor completo
[x] constructor null

[x] getDato / setDato
[x] hijos
[x] esHoja

[x] buscar raíz
[x] buscar izquierda
[x] buscar derecha
[x] buscar inexistente
[x] buscar null

[x] insertar menor
[x] insertar mayor
[x] inserciones recursivas
[x] duplicados
[x] insertar null

[x] eliminar hoja
[x] eliminar con un hijo
[x] eliminar con dos hijos
[x] eliminar inexistente
[x] eliminar null
[x] impedir eliminar nodo actual

[x] inOrder
[x] preOrder
[x] postOrder
[x] Consumer null

[x] cantidadNodos
[x] cantidadHojas
[x] cantidadNodosInternos

[x] altura hoja
[x] altura árbol
[x] altura degenerado

[x] nivel raíz
[x] nivel 1
[x] nivel 2
[x] nivel inexistente
[x] nivel null

pretty good huh

*/

class NodoTest {

    // =========================================================
    // CONSTRUCTORES Y DATOS
    // =========================================================

    @Test
    void constructorSimpleDebeInicializarDatoYSinHijos() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertEquals(10, nodo.getDato());
        assertNull(nodo.getHijoIzquierdo());
        assertNull(nodo.getHijoDerecho());
    }

    @Test
    void constructorCompletoDebeInicializarDatoEHijos() {
        Nodo<Integer> izquierdo = new Nodo<>(5);
        Nodo<Integer> derecho = new Nodo<>(15);

        Nodo<Integer> nodo =
                new Nodo<>(10, izquierdo, derecho);

        assertEquals(10, nodo.getDato());
        assertSame(izquierdo, nodo.getHijoIzquierdo());
        assertSame(derecho, nodo.getHijoDerecho());
    }

    @Test
    void constructorDebeRechazarDatoNulo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Nodo<Integer>(null));
    }

    @Test
    void setDatoDebeActualizarDato() {
        Nodo<Integer> nodo = new Nodo<>(10);

        nodo.setDato(20);

        assertEquals(20, nodo.getDato());
    }

    @Test
    void setDatoDebeRechazarDatoNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.setDato(null));
    }


    // =========================================================
    // HIJOS Y ESTADO DE HOJA
    // =========================================================

    @Test
    void nodoSinHijosDebeSerHoja() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertTrue(nodo.esHoja());
    }

    @Test
    void nodoConHijoIzquierdoNoDebeSerHoja() {
        Nodo<Integer> nodo = new Nodo<>(10);

        nodo.setHijoIzquierdo(new Nodo<>(5));

        assertFalse(nodo.esHoja());
    }

    @Test
    void nodoConHijoDerechoNoDebeSerHoja() {
        Nodo<Integer> nodo = new Nodo<>(10);

        nodo.setHijoDerecho(new Nodo<>(15));

        assertFalse(nodo.esHoja());
    }

    @Test
    void settersDeHijosDebenActualizarReferencias() {
        Nodo<Integer> nodo = new Nodo<>(10);
        Nodo<Integer> izquierdo = new Nodo<>(5);
        Nodo<Integer> derecho = new Nodo<>(15);

        nodo.setHijoIzquierdo(izquierdo);
        nodo.setHijoDerecho(derecho);

        assertSame(izquierdo, nodo.getHijoIzquierdo());
        assertSame(derecho, nodo.getHijoDerecho());
    }

    @Test
    void settersDeHijosDebenPermitirEliminarReferenciasConNull() {
        Nodo<Integer> nodo = new Nodo<>(10);

        nodo.setHijoIzquierdo(new Nodo<>(5));
        nodo.setHijoDerecho(new Nodo<>(15));

        nodo.setHijoIzquierdo(null);
        nodo.setHijoDerecho(null);

        assertNull(nodo.getHijoIzquierdo());
        assertNull(nodo.getHijoDerecho());
        assertTrue(nodo.esHoja());
    }


    // =========================================================
    // BUSCAR
    // =========================================================

    @Test
    void buscarDebeEncontrarNodoActual() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        TDAElemento<Integer> encontrado = nodo.buscar(10);

        assertNotNull(encontrado);
        assertEquals(10, encontrado.getDato());
    }

    @Test
    void buscarDebeEncontrarNodoEnSubarbolIzquierdo() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        TDAElemento<Integer> encontrado = nodo.buscar(2);

        assertNotNull(encontrado);
        assertEquals(2, encontrado.getDato());
    }

    @Test
    void buscarDebeEncontrarNodoEnSubarbolDerecho() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        TDAElemento<Integer> encontrado = nodo.buscar(15);

        assertNotNull(encontrado);
        assertEquals(15, encontrado.getDato());
    }

    @Test
    void buscarDebeRetornarNullCuandoDatoNoExiste() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertNull(nodo.buscar(100));
    }

    @Test
    void buscarDebeRechazarCriterioNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.buscar(null));
    }


    // =========================================================
    // INSERTAR
    // =========================================================

    @Test
    void insertarMenorDebeColocarloEnSubarbolIzquierdo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        boolean insertado = nodo.insertar(5);

        assertTrue(insertado);
        assertNotNull(nodo.getHijoIzquierdo());
        assertEquals(
                5,
                nodo.getHijoIzquierdo().getDato());
    }

    @Test
    void insertarMayorDebeColocarloEnSubarbolDerecho() {
        Nodo<Integer> nodo = new Nodo<>(10);

        boolean insertado = nodo.insertar(15);

        assertTrue(insertado);
        assertNotNull(nodo.getHijoDerecho());
        assertEquals(
                15,
                nodo.getHijoDerecho().getDato());
    }

    @Test
    void insertarDebeMantenerPropiedadDelABB() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertTrue(nodo.insertar(5));
        assertTrue(nodo.insertar(15));
        assertTrue(nodo.insertar(2));
        assertTrue(nodo.insertar(7));
        assertTrue(nodo.insertar(12));
        assertTrue(nodo.insertar(20));

        assertEquals(
                List.of(2, 5, 7, 10, 12, 15, 20),
                obtenerInOrder(nodo));
    }

    @Test
    void insertarDuplicadoDebeRetornarFalse() {
        Nodo<Integer> nodo = new Nodo<>(10);

        boolean insertado = nodo.insertar(10);

        assertFalse(insertado);
        assertEquals(1, nodo.cantidadNodos());
    }

    @Test
    void insertarDebeRechazarDatoNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.insertar(null));
    }


    // =========================================================
    // ELIMINAR
    // =========================================================

    @Test
    void eliminarHojaDebeEliminarNodoCorrectamente() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        TDAElemento<Integer> eliminado = nodo.eliminar(2);

        assertNotNull(eliminado);
        assertEquals(2, eliminado.getDato());

        assertNull(nodo.buscar(2));

        assertEquals(
                List.of(5, 7, 10, 12, 15, 20),
                obtenerInOrder(nodo));
    }

    @Test
    void eliminarNodoConUnHijoDebeReconectarSubarbol() {
        Nodo<Integer> nodo = new Nodo<>(10);

        nodo.insertar(5);
        nodo.insertar(2);

        TDAElemento<Integer> eliminado =
                nodo.eliminar(5);

        assertNotNull(eliminado);
        assertEquals(5, eliminado.getDato());

        assertNull(nodo.buscar(5));
        assertNotNull(nodo.buscar(2));

        assertEquals(
                List.of(2, 10),
                obtenerInOrder(nodo));
    }

    @Test
    void eliminarNodoConDosHijosDebeMantenerPropiedadDelABB() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        TDAElemento<Integer> eliminado =
                nodo.eliminar(5);

        assertNotNull(eliminado);
        assertEquals(5, eliminado.getDato());

        assertNull(nodo.buscar(5));

        assertEquals(
                List.of(2, 7, 10, 12, 15, 20),
                obtenerInOrder(nodo));
    }

    @Test
    void eliminarDatoInexistenteDebeRetornarNull() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        TDAElemento<Integer> eliminado =
                nodo.eliminar(100);

        assertNull(eliminado);

        assertEquals(
                List.of(2, 5, 7, 10, 12, 15, 20),
                obtenerInOrder(nodo));
    }

    @Test
    void eliminarDebeRechazarCriterioNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.eliminar(null));
    }

    @Test
    void eliminarNoDebePermitirEliminarElNodoActual() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalStateException.class,
                () -> nodo.eliminar(10));
    }


    // =========================================================
    // RECORRIDOS
    // =========================================================

    @Test
    void inOrderDebeRecorrerIzquierdaRaizDerecha() {
        Nodo<Integer> nodo = crearArbolEjemplo();
        List<Integer> recorrido = new ArrayList<>();

        nodo.inOrder(
                elemento ->
                        recorrido.add(elemento.getDato()));

        assertEquals(
                List.of(2, 5, 7, 10, 12, 15, 20),
                recorrido);
    }

    @Test
    void preOrderDebeRecorrerRaizIzquierdaDerecha() {
        Nodo<Integer> nodo = crearArbolEjemplo();
        List<Integer> recorrido = new ArrayList<>();

        nodo.preOrder(
                elemento ->
                        recorrido.add(elemento.getDato()));

        assertEquals(
                List.of(10, 5, 2, 7, 15, 12, 20),
                recorrido);
    }

    @Test
    void postOrderDebeRecorrerIzquierdaDerechaRaiz() {
        Nodo<Integer> nodo = crearArbolEjemplo();
        List<Integer> recorrido = new ArrayList<>();

        nodo.postOrder(
                elemento ->
                        recorrido.add(elemento.getDato()));

        assertEquals(
                List.of(2, 7, 5, 12, 20, 15, 10),
                recorrido);
    }

    @Test
    void inOrderDebeRechazarConsumidorNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.inOrder(null));
    }

    @Test
    void preOrderDebeRechazarConsumidorNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.preOrder(null));
    }

    @Test
    void postOrderDebeRechazarConsumidorNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.postOrder(null));
    }


    // =========================================================
    // CANTIDADES
    // =========================================================

    @Test
    void nodoHojaDebeTenerUnNodoUnaHojaYCeroInternos() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertEquals(1, nodo.cantidadNodos());
        assertEquals(1, nodo.cantidadHojas());
        assertEquals(0, nodo.cantidadNodosInternos());
    }

    @Test
    void cantidadesDebenSerCorrectasEnArbolCompleto() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertEquals(7, nodo.cantidadNodos());
        assertEquals(4, nodo.cantidadHojas());
        assertEquals(3, nodo.cantidadNodosInternos());
    }


    // =========================================================
    // ALTURA
    // =========================================================

    @Test
    void alturaDeNodoHojaDebeSerUno() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertEquals(1, nodo.altura());
    }

    @Test
    void alturaDebeCalcularCaminoMasLargo() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertEquals(3, nodo.altura());
    }

    @Test
    void alturaDebeFuncionarEnArbolDegenerado() {
        Nodo<Integer> nodo = new Nodo<>(10);

        nodo.insertar(20);
        nodo.insertar(30);
        nodo.insertar(40);

        assertEquals(4, nodo.altura());
    }


    // =========================================================
    // NIVEL
    // =========================================================

    @Test
    void nivelDelNodoActualDebeSerCero() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertEquals(0, nodo.obtenerNivel(10));
    }

    @Test
    void nivelDeHijoDebeSerUno() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertEquals(1, nodo.obtenerNivel(5));
        assertEquals(1, nodo.obtenerNivel(15));
    }

    @Test
    void nivelDeNietoDebeSerDos() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertEquals(2, nodo.obtenerNivel(2));
        assertEquals(2, nodo.obtenerNivel(7));
        assertEquals(2, nodo.obtenerNivel(12));
        assertEquals(2, nodo.obtenerNivel(20));
    }

    @Test
    void obtenerNivelDebeRetornarMenosUnoCuandoNoExiste() {
        Nodo<Integer> nodo = crearArbolEjemplo();

        assertEquals(-1, nodo.obtenerNivel(100));
    }

    @Test
    void obtenerNivelDebeRechazarCriterioNulo() {
        Nodo<Integer> nodo = new Nodo<>(10);

        assertThrows(
                IllegalArgumentException.class,
                () -> nodo.obtenerNivel(null));
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================

    /**
     * Crea el ABB utilizado por varios tests:
     *
     *              10
     *            /    \
     *           5      15
     *          / \    /  \
     *         2   7  12  20
     */
    private Nodo<Integer> crearArbolEjemplo() {
        Nodo<Integer> raiz = new Nodo<>(10);

        raiz.insertar(5);
        raiz.insertar(15);
        raiz.insertar(2);
        raiz.insertar(7);
        raiz.insertar(12);
        raiz.insertar(20);

        return raiz;
    }

    /**
     * Convierte el recorrido in-order en una lista para facilitar
     * la comprobación de que se mantiene la propiedad del ABB.
     */
    private List<Integer> obtenerInOrder(
            Nodo<Integer> nodo) {

        List<Integer> recorrido = new ArrayList<>();

        nodo.inOrder(
                elemento ->
                        recorrido.add(elemento.getDato()));

        return recorrido;
    }
}