package ucu.edu.aed.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerminalCargaTest {

    @Test
    void nuevaTerminalIniciaLibre() {
        TerminalCarga terminal = new TerminalCarga(1);

        assertEquals(1, terminal.getNumero());
        assertEquals(EstadoTerminal.LIBRE, terminal.getEstado());
        assertTrue(terminal.estaLibre());
        assertNull(terminal.getOperacionActual());
        assertThrows(IllegalArgumentException.class, () -> new TerminalCarga(0));
    }

    @Test
    void asignarOperacionOcupaTerminalYLiberarLaDejaLibre() {
        TerminalCarga terminal = new TerminalCarga(1);
        OperacionLogistica operacion = operacion("OP1", TipoOperacion.CARGA);

        terminal.asignarOperacion(operacion);

        assertEquals(EstadoTerminal.OCUPADA, terminal.getEstado());
        assertFalse(terminal.estaLibre());
        assertSame(operacion, terminal.getOperacionActual());

        terminal.liberar();

        assertEquals(EstadoTerminal.LIBRE, terminal.getEstado());
        assertTrue(terminal.estaLibre());
        assertNull(terminal.getOperacionActual());
    }

    @Test
    void noPermiteAsignarOperacionSiNoEstaLibre() {
        TerminalCarga terminal = new TerminalCarga(1);
        terminal.asignarOperacion(operacion("OP1", TipoOperacion.CARGA));

        assertThrows(IllegalStateException.class,
                () -> terminal.asignarOperacion(operacion("OP2", TipoOperacion.DESCARGA)));

        terminal.liberar();
        terminal.deshabilitar();

        assertThrows(IllegalStateException.class,
                () -> terminal.asignarOperacion(operacion("OP3", TipoOperacion.CARGA)));
        assertThrows(IllegalArgumentException.class,
                () -> terminal.asignarOperacion(null));
    }

    @Test
    void deshabilitarTerminalOcupadaSeRechazaSinCambiarEstado() {
        TerminalCarga terminal = new TerminalCarga(1);
        OperacionLogistica operacion = operacion("OP1", TipoOperacion.DESCARGA);
        terminal.asignarOperacion(operacion);

        assertThrows(IllegalStateException.class, terminal::deshabilitar);

        assertEquals(EstadoTerminal.OCUPADA, terminal.getEstado());
        assertSame(operacion, terminal.getOperacionActual());
    }

    @Test
    void habilitarYDeshabilitarTerminalLibre() {
        TerminalCarga terminal = new TerminalCarga(1);

        terminal.deshabilitar();
        assertEquals(EstadoTerminal.DESHABILITADA, terminal.getEstado());
        assertFalse(terminal.estaLibre());

        terminal.habilitar();
        assertEquals(EstadoTerminal.LIBRE, terminal.getEstado());
        assertTrue(terminal.estaLibre());
    }

    @Test
    void liberarSoloAplicaATerminalOcupada() {
        TerminalCarga terminal = new TerminalCarga(1);

        assertThrows(IllegalStateException.class, terminal::liberar);

        terminal.deshabilitar();

        assertThrows(IllegalStateException.class, terminal::liberar);
    }

    private OperacionLogistica operacion(String id, TipoOperacion tipoOperacion) {
        return new OperacionDePrueba(id, tipoOperacion);
    }

    private static class OperacionDePrueba implements OperacionLogistica {

        private final String id;
        private final TipoOperacion tipoOperacion;

        OperacionDePrueba(String id, TipoOperacion tipoOperacion) {
            this.id = id;
            this.tipoOperacion = tipoOperacion;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public TipoOperacion getTipoOperacion() {
            return this.tipoOperacion;
        }
    }
}
