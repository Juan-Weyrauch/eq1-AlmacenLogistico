package ucu.edu.aed.structures.hierarchical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ArbolGeneralTest {

    @Test
    void arbolNuevoDebeSerVacio() {
        ArbolGeneral<Integer> arbol = new ArbolGeneral<>();

        assertTrue(arbol.esVacio());
        assertNull(arbol.obtenerRaiz());
        assertEquals(0, arbol.cantidadNodos());
        assertEquals(0, arbol.altura());
    }

    @Test
    void agregarRaizDebeEstablecerLaRaiz() {
        ArbolGeneral<Integer> arbol = new ArbolGeneral<>();

        arbol.agregarRaiz(1);

        assertFalse(arbol.esVacio());
        assertEquals(1, arbol.obtenerRaiz());
        assertEquals(1, arbol.cantidadNodos());
        assertEquals(1, arbol.altura());
    }

    @Test
    void agregarRaizDebeRechazarSegundaRaiz() {
        ArbolGeneral<Integer> arbol = new ArbolGeneral<>();
        arbol.agregarRaiz(1);

        assertThrows(
                IllegalStateException.class,
                () -> arbol.agregarRaiz(2));
    }

    @Test
    void agregarHijoDebeAgregarBajoElPadreIndicado() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();

        assertEquals(4, arbol.cantidadNodos());
        assertEquals(2, arbol.buscarHijoDirecto(1, 2));
        assertEquals(3, arbol.buscarHijoDirecto(1, 3));
        assertEquals(4, arbol.buscarHijoDirecto(1, 4));
    }

    @Test
    void agregarHijoDebeRetornarFalseCuandoPadreNoExiste() {
        ArbolGeneral<Integer> arbol = new ArbolGeneral<>();
        arbol.agregarRaiz(1);

        boolean agregado = arbol.agregarHijo(100, 2);

        assertFalse(agregado);
    }

    @Test
    void buscarDebeEncontrarNodoEnProfundidad() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        assertEquals(6, arbol.buscar(6));
    }

    @Test
    void buscarDebeRetornarNullCuandoNoExiste() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();

        assertNull(arbol.buscar(100));
    }

    @Test
    void buscarHijoDirectoDebeRetornarNullCuandoNoEsDirecto() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        assertNull(arbol.buscarHijoDirecto(1, 6));
    }

    @Test
    void cantidadNodosDebeContarTodoElArbol() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        assertEquals(6, arbol.cantidadNodos());
    }

    @Test
    void alturaDebeReflejarLaRamaMasProfunda() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        assertEquals(4, arbol.altura());
    }

    @Test
    void preOrdenDebeVisitarRaizAntesQueHijos() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();
        List<Integer> recorrido = new ArrayList<>();

        arbol.preOrden(recorrido::add);

        assertEquals(List.of(1, 2, 3, 4), recorrido);
    }

    @Test
    void postOrdenDebeVisitarHijosAntesQueRaiz() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();
        List<Integer> recorrido = new ArrayList<>();

        arbol.postOrden(recorrido::add);

        assertEquals(List.of(2, 3, 4, 1), recorrido);
    }

    @Test
    void porNivelesDebeVisitarNivelANivel() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();
        List<Integer> recorrido = new ArrayList<>();

        arbol.porNiveles(recorrido::add);

        assertEquals(List.of(1, 2, 3, 4, 5, 6), recorrido);
    }

    @Test
    void eliminarDebeRemoverSubarbolCompleto() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        boolean eliminado = arbol.eliminar(4);

        assertTrue(eliminado);
        assertNull(arbol.buscar(4));
        assertNull(arbol.buscar(5));
        assertNull(arbol.buscar(6));
        assertEquals(3, arbol.cantidadNodos());
    }

    @Test
    void eliminarRaizDebeVaciarElArbol() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();

        boolean eliminado = arbol.eliminar(1);

        assertTrue(eliminado);
        assertTrue(arbol.esVacio());
    }

    @Test
    void eliminarDebeRetornarFalseCuandoNoExiste() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();

        assertFalse(arbol.eliminar(100));
    }

    @Test
    void moverSubarbolDebeReubicarNodoYPreservarContenido() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        boolean movido = arbol.moverSubarbol(4, 2);

        assertTrue(movido);
        assertEquals(4, arbol.buscarHijoDirecto(2, 4));
        assertEquals(5, arbol.buscarHijoDirecto(4, 5));
        assertEquals(6, arbol.buscarHijoDirecto(5, 6));
        assertEquals(6, arbol.cantidadNodos());
    }

    @Test
    void moverSubarbolDebeRechazarMoverLaRaiz() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.moverSubarbol(1, 2));
    }

    @Test
    void moverSubarbolDebeRechazarDestinoDentroDelPropioSubarbol() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        assertThrows(
                IllegalArgumentException.class,
                () -> arbol.moverSubarbol(4, 5));
    }

    @Test
    void obtenerCaminoDebeIncluirRaizYNodoObjetivo() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();

        List<Integer> camino = new ArrayList<>();
        for (int i = 0; i < arbol.obtenerCamino(6).tamaño(); i++) {
            camino.add(arbol.obtenerCamino(6).obtener(i));
        }

        assertEquals(List.of(1, 4, 5, 6), camino);
    }

    @Test
    void obtenerCaminoDebeSerVacioCuandoNoExiste() {
        ArbolGeneral<Integer> arbol = arbolConMultiplesHijos();

        assertTrue(arbol.obtenerCamino(100).esVacio());
    }

    @Test
    void recorrerSubarbolPreOrdenDebeVisitarSoloDescendientes() {
        ArbolGeneral<Integer> arbol = arbolProfundidadIrregular();
        List<Integer> recorrido = new ArrayList<>();

        arbol.recorrerSubarbolPreOrden(4, recorrido::add);

        assertEquals(List.of(4, 5, 6), recorrido);
    }

    private ArbolGeneral<Integer> arbolConMultiplesHijos() {
        ArbolGeneral<Integer> arbol = new ArbolGeneral<>();
        arbol.agregarRaiz(1);
        arbol.agregarHijo(1, 2);
        arbol.agregarHijo(1, 3);
        arbol.agregarHijo(1, 4);

        return arbol;
    }

    private ArbolGeneral<Integer> arbolProfundidadIrregular() {
        ArbolGeneral<Integer> arbol = new ArbolGeneral<>();
        arbol.agregarRaiz(1);
        arbol.agregarHijo(1, 2);
        arbol.agregarHijo(1, 3);
        arbol.agregarHijo(1, 4);
        arbol.agregarHijo(4, 5);
        arbol.agregarHijo(5, 6);

        return arbol;
    }
}
