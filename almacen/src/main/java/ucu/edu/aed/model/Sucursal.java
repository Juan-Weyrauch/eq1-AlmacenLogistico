package ucu.edu.aed.model;

public class Sucursal {
    private String id;
    private String nombre;
    private int cantidadClientes;

    public Sucursal(String id, String nombre, int cantidadClientes) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadClientes = cantidadClientes;
    }
}
