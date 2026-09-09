package ucu.edu.aed.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import ucu.edu.aed.service.AlmacenLogistico;
import static org.junit.jupiter.api.Assertions.*;

class MenuConsolaHito2Test {
    @Test
    void usuarioPuedeCrearPrimerSectorEnRaizYMostrarDeposito() {
        InputStream entradaAnterior = System.in;
        PrintStream salidaAnterior = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        String entrada = String.join("\n", "11", "8", "", "R", "Recepcion", "5", "100",
                "13", "0", "1", "P", "Producto", "Descripcion", "4", "0") + "\n";
        AlmacenLogistico almacen = new AlmacenLogistico();
        try {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
            new MenuConsola(almacen).ejecutar();
        } finally {
            System.setIn(entradaAnterior);
            System.setOut(salidaAnterior);
        }
        assertNotNull(almacen.buscarSector("R"));
        assertTrue(almacen.buscarProducto("P").stockCoincideConUbicaciones());
        assertEquals(4, almacen.obtenerOcupacionSector("R"));
        String texto = salida.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("(raiz)"));
        assertTrue(texto.contains("Recepcion"));
        assertFalse(texto.contains("No se pudo completar"));
    }
}
