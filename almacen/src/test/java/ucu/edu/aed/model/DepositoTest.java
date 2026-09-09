package ucu.edu.aed.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ucu.edu.aed.structures.linear.ListaArray;
import ucu.edu.aed.structures.linear.ListaSimple;

class DepositoTest {

    @Test
    void depositoNuevoDebeTenerSoloLaRaiz() {
        Deposito deposito = new Deposito();

        Sector raiz = deposito.buscarSectorPorRuta(null);

        assertEquals("DEPOSITO", raiz.getCodigoLocal());
        assertEquals("", deposito.obtenerRuta(raiz));
    }

    @Test
    void agregarSectorConRutaPadreVaciaDebeAgregarBajoLaRaiz() {
        Deposito deposito = new Deposito();

        deposito.agregarSector("", new Sector("3", "Zona 3", TipoSector.ZONA, 1000));

        assertEquals("3", deposito.buscarSectorPorRuta("3").getCodigoLocal());
    }

    @Test
    void agregarSectorDebeAgregarBajoUnPadreExistente() {
        Deposito deposito = crearDepositoDeEjemplo();

        Sector sector = deposito.buscarSectorPorRuta("3.4.5");

        assertEquals("5", sector.getCodigoLocal());
    }

    @Test
    void agregarSectorDebeRechazarPadreInexistente() {
        Deposito deposito = new Deposito();

        assertThrows(
                IllegalArgumentException.class,
                () -> deposito.agregarSector(
                        "99",
                        new Sector("1", "Pos 1", TipoSector.POSICION, 10)));
    }

    @Test
    void buscarSectorPorRutaDebeRetornarNullParaRutaInvalida() {
        Deposito deposito = crearDepositoDeEjemplo();

        assertNull(deposito.buscarSectorPorRuta("3.99"));
    }

    @Test
    void obtenerRutaDebeReconstruirLaRutaCompleta() {
        Deposito deposito = crearDepositoDeEjemplo();

        Sector sector = deposito.buscarSectorPorRuta("3.4.5");

        assertEquals("3.4.5", deposito.obtenerRuta(sector));
    }

    @Test
    void obtenerOcupacionDebeSumarCantidadPorEspacioUnitario() {
        Deposito deposito = crearDepositoDeEjemplo();
        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");
        Producto producto = new Producto("P001", "Arroz", "desc", 2);

        deposito.agregarUbicacion(new UbicacionStock(producto, posicion, 10));

        assertEquals(20, deposito.obtenerOcupacion("3.4.5"));
        assertEquals(20, deposito.obtenerOcupacion("3.4"));
        assertEquals(20, deposito.obtenerOcupacion("3"));
    }

    @Test
    void obtenerOcupacionDebeSerCeroSinStock() {
        Deposito deposito = crearDepositoDeEjemplo();

        assertEquals(0, deposito.obtenerOcupacion("3.4.5"));
    }

    @Test
    void obtenerEspacioDisponibleDebeRestarOcupacionDeLaCapacidad() {
        Deposito deposito = crearDepositoDeEjemplo();
        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");
        Producto producto = new Producto("P001", "Arroz", "desc", 2);

        deposito.agregarUbicacion(new UbicacionStock(producto, posicion, 10));

        assertEquals(30, deposito.obtenerEspacioDisponible("3.4.5"));
    }

    @Test
    void listarPosicionesHabilitadasDebeExcluirDeshabilitadas() {
        Deposito deposito = crearDepositoDeEjemplo();
        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");

        ListaArray<Sector> habilitadas = deposito.listarPosicionesHabilitadas();
        assertEquals(1, habilitadas.tamaño());

        posicion.deshabilitar();

        habilitadas = deposito.listarPosicionesHabilitadas();
        assertTrue(habilitadas.esVacio());
    }

    @Test
    void obtenerContenidoSectorDebeListarUbicacionesDelSubarbol() {
        Deposito deposito = crearDepositoDeEjemplo();
        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        UbicacionStock ubicacion = new UbicacionStock(producto, posicion, 10);

        deposito.agregarUbicacion(ubicacion);

        ListaSimple<UbicacionStock> contenido = deposito.obtenerContenidoSector("3");

        assertEquals(1, contenido.tamaño());
        assertEquals(ubicacion, contenido.obtener(0));
    }

    @Test
    void obtenerCaminoDebeIncluirTodosLosAncestrosIncluidaLaRaiz() {
        Deposito deposito = crearDepositoDeEjemplo();

        ListaSimple<Sector> camino = deposito.obtenerCamino("3.4.5");

        assertEquals(4, camino.tamaño());
        assertEquals("DEPOSITO", camino.obtener(0).getCodigoLocal());
        assertEquals("3", camino.obtener(1).getCodigoLocal());
        assertEquals("4", camino.obtener(2).getCodigoLocal());
        assertEquals("5", camino.obtener(3).getCodigoLocal());
    }

    @Test
    void agregarUbicacionYRemoverUbicacionDebenDelegarEnElSector() {
        Deposito deposito = crearDepositoDeEjemplo();
        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        UbicacionStock ubicacion = new UbicacionStock(producto, posicion, 10);

        deposito.agregarUbicacion(ubicacion);
        assertEquals(1, posicion.obtenerUbicacionesStock().tamaño());

        deposito.removerUbicacion(ubicacion);
        assertTrue(posicion.obtenerUbicacionesStock().esVacio());
    }

    @Test
    void recorrerPorNivelesDebeVisitarTodosLosSectores() {
        Deposito deposito = crearDepositoDeEjemplo();
        List<String> codigos = new ArrayList<>();

        deposito.recorrerPorNiveles(sector -> codigos.add(sector.getCodigoLocal()));

        assertEquals(List.of("DEPOSITO", "3", "8", "4", "5"), codigos);
    }

    @Test
    void moverSectorDebeReubicarSubarbolYPreservarContenido() {
        Deposito deposito = crearDepositoDeEjemplo();
        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        deposito.agregarUbicacion(new UbicacionStock(producto, posicion, 10));

        deposito.moverSector("3.4", "8");

        assertNull(deposito.buscarSectorPorRuta("3.4"));
        assertEquals("4", deposito.buscarSectorPorRuta("8.4").getCodigoLocal());
        assertEquals("5", deposito.buscarSectorPorRuta("8.4.5").getCodigoLocal());
        assertEquals("8.4.5", deposito.obtenerRuta(posicion));
        assertEquals(20, deposito.obtenerOcupacion("8.4.5"));
    }

    @Test
    void moverSectorDebeRechazarDestinoDeshabilitado() {
        Deposito deposito = crearDepositoDeEjemplo();
        deposito.buscarSectorPorRuta("8").deshabilitar();

        assertThrows(
                IllegalArgumentException.class,
                () -> deposito.moverSector("3.4", "8"));
    }

    @Test
    void moverSectorDebeRechazarCapacidadInsuficiente() {
        Deposito deposito = crearDepositoDeEjemplo();
        deposito.agregarSector("", new Sector("9", "Zona chica", TipoSector.ZONA, 5));

        Sector posicion = deposito.buscarSectorPorRuta("3.4.5");
        Producto producto = new Producto("P001", "Arroz", "desc", 2);
        deposito.agregarUbicacion(new UbicacionStock(producto, posicion, 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> deposito.moverSector("3.4", "9"));
    }

    @Test
    void moverSectorDebeRechazarOrigenORutasInexistentes() {
        Deposito deposito = crearDepositoDeEjemplo();

        assertThrows(
                IllegalArgumentException.class,
                () -> deposito.moverSector("3.99", "8"));
    }

    private Deposito crearDepositoDeEjemplo() {
        Deposito deposito = new Deposito();

        deposito.agregarSector("", new Sector("3", "Zona 3", TipoSector.ZONA, 1000));
        deposito.agregarSector("3", new Sector("4", "Pasillo 4", TipoSector.PASILLO, 500));
        deposito.agregarSector("3.4", new Sector("5", "Posicion 5", TipoSector.POSICION, 50));
        deposito.agregarSector("", new Sector("8", "Zona 8", TipoSector.ZONA, 200));

        return deposito;
    }
}
