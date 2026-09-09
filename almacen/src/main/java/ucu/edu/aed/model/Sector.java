package ucu.edu.aed.model;

import ucu.edu.aed.structures.linear.ListaSimple;
import ucu.edu.aed.tda.linear.TDALista;

public class Sector {

    private final String codigoLocal;
    private final String nombre;
    private final TipoSector tipo;
    private final int capacidad;
    private boolean habilitado;
    private final TDALista<UbicacionStock> ubicaciones;

    public Sector(String codigoLocal, String nombre, TipoSector tipo, int capacidad) {
        if (codigoLocal == null || codigoLocal.isBlank()) {
            throw new IllegalArgumentException(
                    "Sector: codigoLocal no puede ser nulo o vacio");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "Sector: nombre no puede ser nulo o vacio");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "Sector: tipo no puede ser nulo");
        }

        if (capacidad < 0) {
            throw new IllegalArgumentException(
                    "Sector: capacidad no puede ser negativa");
        }

        this.codigoLocal = codigoLocal;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.habilitado = true;
        this.ubicaciones = new ListaSimple<>();
    }

    public String getCodigoLocal() {
        return this.codigoLocal;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoSector getTipo() {
        return this.tipo;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

    public boolean estaHabilitado() {
        return this.habilitado;
    }

    public void habilitar() {
        this.habilitado = true;
    }

    public void deshabilitar() {
        this.habilitado = false;
    }

    public void agregarUbicacionStock(UbicacionStock ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "Sector: ubicacion en el metodo 'agregarUbicacionStock' es nula");
        }

        this.ubicaciones.agregar(ubicacion);
    }

    public boolean removerUbicacionStock(UbicacionStock ubicacion) {
        return this.ubicaciones.remover(ubicacion);
    }

    public TDALista<UbicacionStock> obtenerUbicacionesStock() {
        return this.ubicaciones;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "codigoLocal='" + this.codigoLocal + '\'' +
                ", nombre='" + this.nombre + '\'' +
                ", tipo=" + this.tipo +
                ", capacidad=" + this.capacidad +
                ", habilitado=" + this.habilitado +
                '}';
    }
}
