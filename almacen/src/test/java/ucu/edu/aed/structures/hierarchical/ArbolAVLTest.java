package ucu.edu.aed.structures.hierarchical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ucu.edu.aed.tda.element.TDAElemento;

import static org.junit.jupiter.api.Assertions.*;

class AVLArbolTest {

    private ArbolAVL<Integer> arbol;

    @BeforeEach
    void setUp() {
        arbol = new ArbolAVL<>();
    }

// Casos Bordes
    @Test
    void arbolNuevoEstaVacio() {
        assertTrue(arbol.esVacio());
        assertEquals(0, arbol.cantidadNodos());
    }

    @Test
    void insertarNullDevuelveFalse() {
        assertFalse(arbol.insertar(null));
        assertTrue(arbol.esVacio());
    }

    @Test
    void eliminarNullDevuelveFalse() {
        arbol.insertar(10);
        assertFalse(arbol.eliminar(null));
        assertEquals(1, arbol.cantidadNodos());
    }

    @Test
    void eliminarEnArbolVacioDevuelveFalse() {
        assertFalse(arbol.eliminar(5));
    }

    @Test
    void buscarEnArbolVacioDevuelveNull() {
        assertNull(arbol.buscar(5));
    }

    @Test
    void insertarDuplicadoNoAgregaYDevuelveFalse() {
        arbol.insertar(10);
        assertFalse(arbol.insertar(10));
        assertEquals(1, arbol.cantidadNodos());
    }

    @Test
    void eliminarElementoInexistenteDevuelveFalse() {
        arbol.insertar(10);
        arbol.insertar(5);
        assertFalse(arbol.eliminar(999));
        assertEquals(2, arbol.cantidadNodos());
    }

    @Test
    void eliminarUnicoElementoDejaArbolVacio() {
        arbol.insertar(10);
        assertTrue(arbol.eliminar(10));
        assertTrue(arbol.esVacio());
    }

    //Casos normales

    @Test
    void insertarUnSoloElemento() {
        assertTrue(arbol.insertar(10));
        assertFalse(arbol.esVacio());
        assertEquals(1, arbol.cantidadNodos());
        assertEquals(10, arbol.obtenerRaiz().getDato());
        assertEquals(1, arbol.obtenerRaiz().altura());
    }

    @Test
    void inOrderDevuelveElementosOrdenados() {
        int[] valores = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
        for (int v : valores) {
            arbol.insertar(v);
        }

        List<Integer> resultado = new ArrayList<>();
        arbol.inOrder(resultado::add);

        List<Integer> esperado = new ArrayList<>();
        for (int v : valores) {
            esperado.add(v);
        }
        esperado.sort(Integer::compareTo);

        assertEquals(esperado, resultado);
    }

    //Rotaciones

    @Test
    void insercionCausaLL() {
        arbol.insertar(30);
        arbol.insertar(20);
        arbol.insertar(10); // dispara LL

        assertEquals(20, arbol.obtenerRaiz().getDato());
        assertEquals(2, arbol.obtenerRaiz().altura());
        assertEquals(10, arbol.obtenerRaiz().getHijoIzquierdo().getDato());
        assertEquals(30, arbol.obtenerRaiz().getHijoDerecho().getDato());
    }

    @Test
    void insercionCausaRR() {
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30); // dispara RR

        assertEquals(20, arbol.obtenerRaiz().getDato());
        assertEquals(2, arbol.obtenerRaiz().altura());
        assertEquals(10, arbol.obtenerRaiz().getHijoIzquierdo().getDato());
        assertEquals(30, arbol.obtenerRaiz().getHijoDerecho().getDato());
    }

    @Test
    void insercionCausaLR() {
        arbol.insertar(30);
        arbol.insertar(10);
        arbol.insertar(20); // dispara LR

        assertEquals(20, arbol.obtenerRaiz().getDato());
        assertEquals(10, arbol.obtenerRaiz().getHijoIzquierdo().getDato());
        assertEquals(30, arbol.obtenerRaiz().getHijoDerecho().getDato());
    }

    @Test
    void insercionCausaRL() {
        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(20); // dispara RL

        assertEquals(20, arbol.obtenerRaiz().getDato());
        assertEquals(10, arbol.obtenerRaiz().getHijoIzquierdo().getDato());
        assertEquals(30, arbol.obtenerRaiz().getHijoDerecho().getDato());
    }

    //Balance con inserciones

    @Test
    void insertarEnOrdenCrecienteMantieneAlturaLogaritmica() {
        for (int i = 1; i <= 15; i++) arbol.insertar(i);


        assertTrue(arbol.obtenerRaiz().altura() <= 4, "Altura obtenida: " + arbol.obtenerRaiz().altura());
        assertEquals(15, arbol.cantidadNodos());
        assertTrue(esAVLBalanceado(arbol.obtenerRaiz()));
    }

    //Eliminación

    @Test
    void eliminarHoja() {
        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);

        assertTrue(arbol.eliminar(10));
        assertNull(arbol.buscar(10));
        assertEquals(2, arbol.cantidadNodos());
    }

    @Test
    void eliminarNodoConUnHijo() {
        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(5); // queda como único hijo izquierdo de 10

        assertTrue(arbol.eliminar(10));
        assertNull(arbol.buscar(10));
        assertNotNull(arbol.buscar(5));
        assertNotNull(arbol.buscar(20));
        assertNotNull(arbol.buscar(30));
        assertEquals(3, arbol.cantidadNodos());
    }

    @Test
    void eliminarNodoConDosHijos() {
        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(25);
        arbol.insertar(40);

        assertTrue(arbol.eliminar(30));
        assertNull(arbol.buscar(30));
        assertNotNull(arbol.buscar(25));
        assertNotNull(arbol.buscar(40));
        assertEquals(4, arbol.cantidadNodos());
    }

    @Test
    void eliminarRaizConDosHijos() {
        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);

        assertTrue(arbol.eliminar(20));
        assertNull(arbol.buscar(20));
        assertEquals(2, arbol.cantidadNodos());

        List<Integer> resultado = new ArrayList<>();
        arbol.inOrder(resultado::add);
        assertEquals(List.of(10, 30), resultado);
    }

    @Test
    void eliminarCausaRebalanceo() {
        arbol.insertar(30);
        arbol.insertar(20);
        arbol.insertar(40);
        arbol.insertar(10);
        arbol.insertar(25);
        arbol.insertar(35);
        arbol.insertar(45);
        arbol.insertar(5);

        arbol.eliminar(45);
        arbol.eliminar(35);
        arbol.eliminar(40);

        assertTrue(esAVLBalanceado(arbol.obtenerRaiz()));
    }

    @Test
    void eliminarTodosLosElementosDejaArbolVacio() {
        int[] valores = {50, 30, 70, 20, 40, 60, 80};
        for (int v : valores) {
            arbol.insertar(v);
        }

        for (int v : valores) {
            assertTrue(arbol.eliminar(v));
        }

        assertTrue(arbol.esVacio());
        assertEquals(0, arbol.cantidadNodos());
    }

    //Para saber si está balanceado

    private boolean esAVLBalanceado(TDAElemento<Integer> nodo) {
        if (nodo == null) {
            return true;
        }

        int alturaIzq = 0;
        if (nodo.getHijoIzquierdo() != null) {
            alturaIzq = nodo.getHijoIzquierdo().altura();
        }

        int alturaDer = 0;
        if (nodo.getHijoDerecho() != null) {
            alturaDer = nodo.getHijoDerecho().altura();
        }

        if (Math.abs(alturaIzq - alturaDer) > 1) {
            return false;
        }

        return esAVLBalanceado(nodo.getHijoIzquierdo()) && esAVLBalanceado(nodo.getHijoDerecho());
    }
}