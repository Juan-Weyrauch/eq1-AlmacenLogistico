package ucu.edu.aed;

import ucu.edu.aed.model.TerminalCarga;
import ucu.edu.aed.service.AlmacenLogistico;
import ucu.edu.aed.ui.MenuConsola;

/**
 * Punto de entrada de la aplicacion de gestion del almacen logistico.
 */
public class Main {

    /** Cantidad de terminales disponibles al iniciar la aplicacion. */
    private static final int CANTIDAD_TERMINALES_INICIALES = 4;

    private Main() {
        // Evita instanciar la clase de entrada de la aplicacion.
    }

    /**
     * Inicializa el almacen, registra las terminales iniciales y ejecuta el menu.
     *
     * @param args argumentos de linea de comandos; no se utilizan
     */
    public static void main(String[] args) {
        AlmacenLogistico almacen = new AlmacenLogistico();
        registrarTerminalesIniciales(almacen);

        MenuConsola menu = new MenuConsola(almacen);
        menu.ejecutar();
    }

    /**
     * Registra las terminales iniciales del sistema.
     *
     * @param almacen almacen en el que se registraran las terminales
     */
    private static void registrarTerminalesIniciales(AlmacenLogistico almacen) {
        for (int numero = 1; numero <= CANTIDAD_TERMINALES_INICIALES; numero++) {
            almacen.registrarTerminal(new TerminalCarga(numero));
        }
    }
}