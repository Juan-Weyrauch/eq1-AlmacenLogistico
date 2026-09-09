package ucu.edu.aed.structures.linear;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import ucu.edu.aed.tda.linear.TDAColaPrioridad;

/**
 * Cola de prioridad basada en un heap máximo genérico (Heap<Entrada<T>>).
 */
public class ColaPrioridad<T> implements TDAColaPrioridad<T> {

    private static final class Entrada<T> {
        private final T dato;
        private final long secuencia;

        private Entrada(T dato, long secuencia) {
            this.dato = dato;
            this.secuencia = secuencia;
        }
    }

    private final Heap<Entrada<T>> monticulo;
    private long contadorSecuencia;

    public ColaPrioridad(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        Comparator<Entrada<T>> comparadorInterno = (a, b) -> {
            int resultado = comparator.compare(a.dato, b.dato);
            if (resultado != 0) {
                return resultado;
            }
            // Misma prioridad, se queda el mas antiguo
            return Long.compare(b.secuencia, a.secuencia);
        };
        this.monticulo = new Heap<>(comparadorInterno);
        this.contadorSecuencia = 0L;
    }

    @Override
    public T frente() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola de prioridad está vacía");
        }
        return monticulo.frente().dato;
    }

    @Override
    public boolean poneEnCola(T dato) {
        checkElementNull(dato);
        monticulo.poneEnCola(new Entrada<>(dato, contadorSecuencia));
        contadorSecuencia++;
        return true;
    }

    @Override
    public T quitaDeCola() {
        if (esVacio()) {
            throw new NoSuchElementException("La cola de prioridad está vacía");
        }
        return monticulo.quitaDeCola().dato;
    }

    public T buscar(Predicate<T> criterio) {
        if (criterio == null) {
            throw new IllegalArgumentException(
                    "Predicate cannot be null");
        }
        Entrada<T> encontrada =
                monticulo.buscar(entrada -> criterio.test(entrada.dato));

        return encontrada == null ? null : encontrada.dato;
    }

    /**
     * Reacomoda un elemento ya presente en la cola después de que su
     * prioridad haya sido modificada externamente (por ejemplo, con
     * PedidoReabastecimiento.setPrioridad()). Localiza el elemento por
     * identidad (==), no por equals(), porque buscamos ese objeto exacto
     * que ya está encolado, no uno "igual" según alguna noción de
     * igualdad del dominio.
     *
     * Orden de uso correcto: primero mutar la prioridad del elemento,
     * después llamar a este método. Si se llama antes, el heap va a
     * reordenar usando el valor viejo.
     *
     * @throws NoSuchElementException si el elemento no está en la cola
     */
    public void reordenar(T elemento) {
        checkElementNull(elemento);
        monticulo.reordenar(entrada -> entrada.dato == elemento);
    }

    public int tamaño() {
        return monticulo.tamaño();
    }

    public boolean esVacio() {
        return monticulo.esVacio();
    }

    public void vaciar() {
        monticulo.vaciar();
        contadorSecuencia = 0L;
    }

    private void checkElementNull(T elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Input element cannot be null");
        }
    }
}