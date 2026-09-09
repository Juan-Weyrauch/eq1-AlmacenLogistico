package ucu.edu.aed.model;

import java.util.function.Consumer;

import ucu.edu.aed.structures.hierarchical.ArbolGeneral;
import ucu.edu.aed.structures.linear.ListaArray;
import ucu.edu.aed.structures.linear.ListaSimple;
import ucu.edu.aed.tda.linear.TDALista;

public class Deposito {

    private static final String SEPARADOR_RUTA = ".";
    private static final String CODIGO_RAIZ = "DEPOSITO";

    private final ArbolGeneral<Sector> estructura;

    public Deposito() {
        this.estructura = new ArbolGeneral<>();
        this.estructura.agregarRaiz(
                new Sector(CODIGO_RAIZ, "Deposito", TipoSector.ZONA, Integer.MAX_VALUE));
    }

    public void agregarSector(String rutaPadre, Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException(
                    "Deposito: sector en el metodo 'agregarSector' es nulo");
        }

        Sector padre = this.buscarSectorPorRuta(rutaPadre);

        if (padre == null) {
            throw new IllegalArgumentException(
                    "Deposito: no existe el sector padre indicado por la ruta");
        }

        boolean agregado =
                this.estructura.agregarHijo(this.criterioPorIdentidad(padre), sector);

        if (!agregado) {
            throw new IllegalStateException(
                    "Deposito: no se pudo agregar el sector");
        }
    }

    public Sector buscarSectorPorRuta(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            return this.estructura.obtenerRaiz();
        }

        String[] segmentos = ruta.split("\\" + SEPARADOR_RUTA);
        Sector actual = this.estructura.obtenerRaiz();

        for (String segmento : segmentos) {
            if (actual == null) {
                return null;
            }

            actual = this.estructura.buscarHijoDirecto(
                    this.criterioPorIdentidad(actual),
                    this.criterioPorCodigo(segmento));
        }

        return actual;
    }

    public String obtenerRuta(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException(
                    "Deposito: sector en el metodo 'obtenerRuta' es nulo");
        }

        TDALista<Sector> camino =
                this.estructura.obtenerCamino(this.criterioPorIdentidad(sector));

        if (camino.esVacio()) {
            throw new IllegalArgumentException(
                    "Deposito: el sector no pertenece a este deposito");
        }

        StringBuilder ruta = new StringBuilder();

        for (int i = 1; i < camino.tamaño(); i++) {
            if (ruta.length() > 0) {
                ruta.append(SEPARADOR_RUTA);
            }

            ruta.append(camino.obtener(i).getCodigoLocal());
        }

        return ruta.toString();
    }

    public void moverSector(String rutaOrigen, String rutaDestino) {
        Sector origen = this.buscarSectorPorRuta(rutaOrigen);
        Sector destino = this.buscarSectorPorRuta(rutaDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Deposito: origen/destino no existen");
        }

        if (!destino.estaHabilitado()) {
            throw new IllegalArgumentException(
                    "Deposito: el sector destino no esta habilitado");
        }

        int ocupacionOrigen = this.obtenerOcupacion(rutaOrigen);
        int espacioDisponibleDestino = this.obtenerEspacioDisponible(rutaDestino);

        if (ocupacionOrigen > espacioDisponibleDestino) {
            throw new IllegalArgumentException(
                    "Deposito: el sector destino no tiene capacidad suficiente");
        }

        boolean movido = this.estructura.moverSubarbol(
                this.criterioPorIdentidad(origen),
                this.criterioPorIdentidad(destino));

        if (!movido) {
            throw new IllegalStateException(
                    "Deposito: no se pudo mover el sector");
        }
    }

    public int obtenerOcupacion(String ruta) {
        Sector sector = this.buscarSectorPorRuta(ruta);

        if (sector == null) {
            throw new IllegalArgumentException(
                    "Deposito: no existe el sector indicado por la ruta");
        }

        int[] ocupacion = {0};

        this.estructura.recorrerSubarbolPreOrden(
                this.criterioPorIdentidad(sector),
                s -> {
                    TDALista<UbicacionStock> ubicaciones = s.obtenerUbicacionesStock();

                    for (int i = 0; i < ubicaciones.tamaño(); i++) {
                        UbicacionStock u = ubicaciones.obtener(i);
                        ocupacion[0] += u.getCantidad() * u.getProducto().getEspacioUnitario();
                    }
                });

        return ocupacion[0];
    }

    public int obtenerEspacioDisponible(String ruta) {
        Sector sector = this.buscarSectorPorRuta(ruta);

        if (sector == null) {
            throw new IllegalArgumentException(
                    "Deposito: no existe el sector indicado por la ruta");
        }

        return sector.getCapacidad() - this.obtenerOcupacion(ruta);
    }

    public ListaArray<Sector> listarPosicionesHabilitadas() {
        ListaArray<Sector> resultado = new ListaArray<>();

        this.estructura.preOrden(sector -> {
            if (sector.getTipo() == TipoSector.POSICION && sector.estaHabilitado()) {
                resultado.agregar(sector);
            }
        });

        return resultado;
    }

    public ListaSimple<UbicacionStock> obtenerContenidoSector(String ruta) {
        Sector sector = this.buscarSectorPorRuta(ruta);

        if (sector == null) {
            throw new IllegalArgumentException(
                    "Deposito: no existe el sector indicado por la ruta");
        }

        ListaSimple<UbicacionStock> resultado = new ListaSimple<>();

        this.estructura.recorrerSubarbolPreOrden(
                this.criterioPorIdentidad(sector),
                s -> {
                    TDALista<UbicacionStock> ubicaciones = s.obtenerUbicacionesStock();

                    for (int i = 0; i < ubicaciones.tamaño(); i++) {
                        resultado.agregar(ubicaciones.obtener(i));
                    }
                });

        return resultado;
    }

    public ListaSimple<Sector> obtenerCamino(String ruta) {
        Sector sector = this.buscarSectorPorRuta(ruta);

        if (sector == null) {
            throw new IllegalArgumentException(
                    "Deposito: no existe el sector indicado por la ruta");
        }

        TDALista<Sector> camino =
                this.estructura.obtenerCamino(this.criterioPorIdentidad(sector));

        ListaSimple<Sector> resultado = new ListaSimple<>();

        for (int i = 0; i < camino.tamaño(); i++) {
            resultado.agregar(camino.obtener(i));
        }

        return resultado;
    }

    public void recorrerPorNiveles(Consumer<Sector> consumidor) {
        this.estructura.porNiveles(consumidor);
    }

    public void agregarUbicacion(UbicacionStock ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "Deposito: ubicacion en el metodo 'agregarUbicacion' es nula");
        }

        ubicacion.getPosicion().agregarUbicacionStock(ubicacion);
    }

    public void removerUbicacion(UbicacionStock ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "Deposito: ubicacion en el metodo 'removerUbicacion' es nula");
        }

        ubicacion.getPosicion().removerUbicacionStock(ubicacion);
    }

    private Comparable<Sector> criterioPorCodigo(String codigo) {
        return otro -> otro.getCodigoLocal().equals(codigo) ? 0 : 1;
    }

    private Comparable<Sector> criterioPorIdentidad(Sector sector) {
        return otro -> otro == sector ? 0 : 1;
    }
}
