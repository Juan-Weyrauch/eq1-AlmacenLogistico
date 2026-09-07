package ucu.edu.aed.structures.element;

import java.util.function.Consumer;

import ucu.edu.aed.tda.element.TDAElemento;

public class Nodo<T> implements TDAElemento<T> {
    private T dato;
    private TDAElemento<T> hijoIzquierdo;
    private TDAElemento<T> hijoDerecho;

    // permite crear un elemento con hijos nulos.
    // o sea, tambien podes poner (x, null, null). Pero facilita la creacion
    // de un elemento haciendo "new Elemento(x)".
    public Nodo(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "Dato en 'constructor' de la clase Elemento es null");
        }
        this.dato = dato;
    }

    public Nodo(T dato, TDAElemento<T> hijoIzquierdo,
            TDAElemento<T> hijoDerecho) {
        this(dato);
        this.hijoIzquierdo = hijoIzquierdo;
        this.hijoDerecho = hijoDerecho;
    }

    @Override
    public void setHijoIzquierdo(TDAElemento<T> hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    @Override
    public void setHijoDerecho(TDAElemento<T> hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }

    @Override
    public TDAElemento<T> getHijoIzquierdo() {
        return this.hijoIzquierdo;
    }

    @Override
    public TDAElemento<T> getHijoDerecho() {
        return this.hijoDerecho;
    }

    @Override
    public T getDato() {
        return this.dato;
    }

    @Override
    public void setDato(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException(
                    "Nodo: Dato en 'setDato' de la clase Elemento es null");
        }
        this.dato = dato;
    }

    @Override
    public TDAElemento<T> buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "Nodo: criterioBusqueda en el metodo 'buscar' es nulo");
        }

        int criterio = criterioBusqueda.compareTo(this.dato);

        if (criterio == 0) {
            return this;
        } else if (criterio < 0) {
            if (this.getHijoIzquierdo() != null) {
                return this.getHijoIzquierdo().buscar(criterioBusqueda);
            }
        } else {
            if (this.getHijoDerecho() != null) {
                return this.getHijoDerecho().buscar(criterioBusqueda);
            }
        }

        return null;
    }

    @Override
    public TDAElemento<T> eliminar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "Nodo: criterioBusqueda en el metodo 'eliminar' es nulo");
        }

        int criterio = criterioBusqueda.compareTo(this.dato);

        // Este caso tendría que ser manejado por la clase que contiene
        // la raíz del árbol.
        if (criterio == 0) {
            throw new IllegalStateException(
                    "Nodo: La eliminacion de la raiz debe ser manejada por el arbol");
        }

        return eliminarRecursivo(this, criterioBusqueda);
    }

    private TDAElemento<T> eliminarRecursivo(TDAElemento<T> nodoActual,
            Comparable<T> criterioBusqueda) {

        int criterio = criterioBusqueda.compareTo(nodoActual.getDato());

        if (criterio < 0) {

            TDAElemento<T> hijo = nodoActual.getHijoIzquierdo();

            if (hijo == null) {
                return null;
            }

            // Encontramos el nodo que queremos eliminar
            if (criterioBusqueda.compareTo(hijo.getDato()) == 0) {
                return eliminarHijo(nodoActual, hijo, true);
            }

            // Todavía no lo encontramos: seguimos bajando
            return eliminarRecursivo(hijo, criterioBusqueda);

        } else {

            TDAElemento<T> hijo = nodoActual.getHijoDerecho();

            if (hijo == null) {
                return null;
            }

            // Encontramos el nodo que queremos eliminar
            if (criterioBusqueda.compareTo(hijo.getDato()) == 0) {
                return eliminarHijo(nodoActual, hijo, false);
            }

            // Todavía no lo encontramos: seguimos bajando
            return eliminarRecursivo(hijo, criterioBusqueda);
        }
    }

    private TDAElemento<T> eliminarHijo(
            TDAElemento<T> padre,
            TDAElemento<T> nodoEliminar,
            boolean esHijoIzquierdo) {

        TDAElemento<T> izquierdo = nodoEliminar.getHijoIzquierdo();
        TDAElemento<T> derecho = nodoEliminar.getHijoDerecho();

        TDAElemento<T> reemplazo;

        // CASO 1:
        // No tiene hijo izquierdo.
        // Esto incluye:
        // - hoja
        // - solamente hijo derecho
        if (izquierdo == null) {

            reemplazo = derecho;

            // CASO 2:
            // Tiene hijo izquierdo pero no derecho.
        } else if (derecho == null) {

            reemplazo = izquierdo;

            // CASO 3:
            // Tiene ambos hijos.
        } else {

            // Buscamos el menor del subárbol derecho
            TDAElemento<T> padreSucesor = nodoEliminar;
            TDAElemento<T> sucesor = derecho;

            while (sucesor.getHijoIzquierdo() != null) {
                padreSucesor = sucesor;
                sucesor = sucesor.getHijoIzquierdo();
            }

            /*
             * Si el sucesor NO es el hijo derecho inmediato,
             * tenemos que quitarlo de su posición original.
             */
            if (padreSucesor != nodoEliminar) {

                padreSucesor.setHijoIzquierdo(
                        sucesor.getHijoDerecho());

                sucesor.setHijoDerecho(derecho);
            }

            sucesor.setHijoIzquierdo(izquierdo);

            reemplazo = sucesor;
        }

        // El padre deja de apuntar al nodo eliminado
        // y empieza a apuntar al reemplazo.
        if (esHijoIzquierdo) {
            padre.setHijoIzquierdo(reemplazo);
        } else {
            padre.setHijoDerecho(reemplazo);
        }

        // Dejamos completamente desconectado al nodo eliminado.
        nodoEliminar.setHijoIzquierdo(null);
        nodoEliminar.setHijoDerecho(null);

        return nodoEliminar;
    }

    @Override
    public boolean esHoja() {
        return this.getHijoDerecho() == null && this.getHijoIzquierdo() == null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean insertar(Comparable<T> nuevoDato) {
        if (nuevoDato == null) {
            throw new IllegalArgumentException(
                    "Nodo: nuevoDato en el metodo 'insertar' es nulo");
        }

        int comparacion = nuevoDato.compareTo(this.dato);

        if (comparacion == 0) {
            // no permite duplicados.
            return false;

        } else if (comparacion < 0) {
            if (this.getHijoIzquierdo() != null) {
                return this.getHijoIzquierdo().insertar(nuevoDato);
            }

            this.setHijoIzquierdo(createNode((T) nuevoDato));
            return true;

        } else {
            if (this.getHijoDerecho() != null) {
                return this.getHijoDerecho().insertar(nuevoDato);
            }

            this.setHijoDerecho(createNode((T) nuevoDato));
            return true;
        }
    }

    // capaz que ayuda a la legibilidad?
    private TDAElemento<T> createNode(T data) {
        return new Nodo<>(data);
    }

    @Override
    public void inOrder(Consumer<TDAElemento<T>> consumidor) {
        validarConsumidor(consumidor, "inOrder");


        // hijo Izquierdo -> this.dato -> hijo Derecho
        if (this.getHijoIzquierdo() != null) {
            this.getHijoIzquierdo().inOrder(consumidor);
        }
        consumidor.accept(this);
        if (this.getHijoDerecho() != null) {
            this.getHijoDerecho().inOrder(consumidor);
        }
    }

    @Override
    public void preOrder(Consumer<TDAElemento<T>> consumidor) {
        validarConsumidor(consumidor, "preOrder");

        // this.dato -> hijo Izquierdo -> hijo Derecho
        consumidor.accept(this);
        if (this.getHijoIzquierdo() != null) {
            this.getHijoIzquierdo().preOrder(consumidor);
        }
        if (this.getHijoDerecho() != null) {
            this.getHijoDerecho().preOrder(consumidor);
        }
    }

    @Override
    public void postOrder(Consumer<TDAElemento<T>> consumidor) {
        validarConsumidor(consumidor, "Nodo: postOrder");

        // hijo Izquierdo -> hijo Derecho -> this.dato
        if (this.getHijoIzquierdo() != null) {
            this.getHijoIzquierdo().postOrder(consumidor);
        }
        if (this.getHijoDerecho() != null) {
            this.getHijoDerecho().postOrder(consumidor);
        }
        consumidor.accept(this);
    }

    @Override
    public int cantidadHojas() {
        // la recursion se encarga de la suma, je.
        if (this.esHoja()) {
            return 1;
        }

        int cantidad = 0;

        if (this.getHijoIzquierdo() != null) {
            cantidad += this.getHijoIzquierdo().cantidadHojas();
        }
        if (this.getHijoDerecho() != null) {
            cantidad += this.getHijoDerecho().cantidadHojas();
        }

        return cantidad;
    }

    @Override
    public int cantidadNodosInternos() {
        return this.cantidadNodos() - this.cantidadHojas();
    }

    @Override
    public int cantidadNodos() {
        int cantidad = 1; // arranca en 1 por el this

        if (this.getHijoIzquierdo() != null) {
            cantidad += this.getHijoIzquierdo().cantidadNodos();
        }

        if (this.getHijoDerecho() != null) {
            cantidad += this.getHijoDerecho().cantidadNodos();
        }

        return cantidad;
    }

    @Override
    public int altura() {
        int alturaIzquierda = 0;
        int alturaDerecha = 0;

        if (this.getHijoIzquierdo() != null) {
            alturaIzquierda = this.getHijoIzquierdo().altura();
        }

        if (this.getHijoDerecho() != null) {
            alturaDerecha = this.getHijoDerecho().altura();
        }

        return 1 + Math.max(alturaIzquierda, alturaDerecha); // camino más largo del nodo a una hoja.
    }

    @Override
    public int obtenerNivel(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) {
            throw new IllegalArgumentException(
                    "Nodo: criterioBusqueda en el metodo 'obtenerNivel' es nulo");
        }

        int comparacion = criterioBusqueda.compareTo(this.dato);

        // Encontramos el nodo.
        // El nivel relativo respecto de sí mismo es 0.
        if (comparacion == 0) {
            return 0;
        }

        int nivelHijo;

        if (comparacion < 0) {
            if (this.getHijoIzquierdo() == null) {
                return -1;
            }

            nivelHijo = this.getHijoIzquierdo()
                    .obtenerNivel(criterioBusqueda);

        } else {
            if (this.getHijoDerecho() == null) {
                return -1;
            }

            nivelHijo = this.getHijoDerecho()
                    .obtenerNivel(criterioBusqueda);
        }

        // Si el hijo tampoco lo encontró, propagamos el -1.
        if (nivelHijo == -1) {
            return -1;
        }

        // Cada vez que la recursión vuelve hacia arriba,
        // agregamos un nivel.
        return 1 + nivelHijo;
    }

    private void validarConsumidor(Consumer<?> consumidor, String metodo) {
        if (consumidor == null) {
            throw new IllegalArgumentException(
                    "Nodo: consumidor en el metodo '" + metodo + "' es nulo");
        }
    }
}
