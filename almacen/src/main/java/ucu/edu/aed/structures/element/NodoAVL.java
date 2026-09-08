package ucu.edu.aed.structures.element;

import ucu.edu.aed.tda.element.TDAElemento;

public class NodoAVL<T> extends Nodo<T> implements TDAElemento<T>{

    private int altura;

    public NodoAVL(T dato) {
        super(dato);
        this.altura = 1;
    }

    @Override
    public int altura() {
        return altura;
    }

    private static <T> int alturaDe(TDAElemento<T> nodo) {
        if (nodo == null) return 0;
        return nodo.altura();
    }

    private void actualizarAltura() {
        altura = 1 + Math.max(alturaDe(getHijoIzquierdo()), alturaDe(getHijoDerecho()));
    }

    private int factorBalance() {
        return alturaDe(getHijoIzquierdo()) - alturaDe(getHijoDerecho());
    }

    @SuppressWarnings("unchecked")
    public TDAElemento<T> insertar(Comparable<T> nuevoDato, boolean[] insertado) {

        int comparacion = nuevoDato.compareTo(getDato());

        if (comparacion == 0) return this;

        if (comparacion < 0) {

            if (getHijoIzquierdo() == null) {
                setHijoIzquierdo(new NodoAVL<>((T) nuevoDato));
                insertado[0] = true;

            } else {
                TDAElemento<T> nuevoSubarbol = ((NodoAVL<T>) getHijoIzquierdo()).insertar(nuevoDato, insertado);
                setHijoIzquierdo(nuevoSubarbol);
            }

        } else {

            if (getHijoDerecho() == null) {
                setHijoDerecho(new NodoAVL<>((T) nuevoDato));
                insertado[0] = true;

            } else {
                TDAElemento<T> nuevoSubarbol = ((NodoAVL<T>) getHijoDerecho()).insertar(nuevoDato, insertado);
                setHijoDerecho(nuevoSubarbol);
            }
        }

        actualizarAltura();
        return balancear();
    }

    @SuppressWarnings("unchecked")
    public TDAElemento<T> eliminar(Comparable<T> criterioBusqueda, boolean[] removido) {

        int comparacion = criterioBusqueda.compareTo(getDato());

        if (comparacion < 0) {

            if (getHijoIzquierdo() != null) {
                TDAElemento<T> nuevoSubarbol = ((NodoAVL<T>) getHijoIzquierdo()).eliminar(criterioBusqueda, removido);
                setHijoIzquierdo(nuevoSubarbol);
            }

        } else if (comparacion > 0) {

            if (getHijoDerecho() != null) {
                TDAElemento<T> nuevoSubarbol = ((NodoAVL<T>) getHijoDerecho()).eliminar(criterioBusqueda, removido);
                setHijoDerecho(nuevoSubarbol);
            }

        } else {
            removido[0] = true;

            if (getHijoIzquierdo() == null && getHijoDerecho() == null) return null;

            if (getHijoIzquierdo() == null) return getHijoDerecho();

            if (getHijoDerecho() == null) return getHijoIzquierdo();

            NodoAVL<T> sucesor = (NodoAVL<T>) getHijoDerecho();

            while (sucesor.getHijoIzquierdo() != null) sucesor = (NodoAVL<T>) sucesor.getHijoIzquierdo();
            setDato(sucesor.getDato());

            TDAElemento<T> nuevoDerecho = ((NodoAVL<T>) getHijoDerecho()).eliminar((Comparable<T>) sucesor.getDato(), new boolean[]{false});
            setHijoDerecho(nuevoDerecho);
        }

        actualizarAltura();
        return balancear();
    }

    private NodoAVL<T> balancear() {

        int factor = factorBalance();

        if (factor > 1) {
            NodoAVL<T> izquierdo = (NodoAVL<T>) getHijoIzquierdo();

            if (izquierdo.factorBalance() < 0) setHijoIzquierdo(izquierdo.rotarIzquierda());
            return rotarDerecha();
        }

        if (factor < -1) {
            NodoAVL<T> derecho = (NodoAVL<T>) getHijoDerecho();

            if (derecho.factorBalance() > 0) setHijoDerecho(derecho.rotarDerecha());
            return rotarIzquierda();
        }

        return this;
    }

    private NodoAVL<T> rotarDerecha() {
        NodoAVL<T> nuevaRaiz = (NodoAVL<T>) getHijoIzquierdo();

        TDAElemento<T> subarbol = nuevaRaiz.getHijoDerecho();
        setHijoIzquierdo(subarbol);

        nuevaRaiz.setHijoDerecho(this);

        actualizarAltura();
        nuevaRaiz.actualizarAltura();

        return nuevaRaiz;
    }

    private NodoAVL<T> rotarIzquierda() {
        NodoAVL<T> nuevaRaiz = (NodoAVL<T>) getHijoDerecho();

        TDAElemento<T> subarbol = nuevaRaiz.getHijoIzquierdo();
        setHijoDerecho(subarbol);

        nuevaRaiz.setHijoIzquierdo(this);

        actualizarAltura();
        nuevaRaiz.actualizarAltura();

        return nuevaRaiz;
    }
}