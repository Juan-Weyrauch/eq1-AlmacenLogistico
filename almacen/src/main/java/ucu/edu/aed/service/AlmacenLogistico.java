package ucu.edu.aed.service;

import ucu.edu.aed.model.*;
import ucu.edu.aed.structures.linear.Cola;
import ucu.edu.aed.structures.linear.ColaPrioridad;
import ucu.edu.aed.structures.linear.ListaArray;
import ucu.edu.aed.structures.linear.ListaSimple;

import java.util.Comparator;

public class AlmacenLogistico {

    private Inventario inventario;

    private ListaArray<TerminalCarga> terminales;

    private Cola<EntregaProveedor> entregasPendientes;

    private ColaPrioridad<PedidoReabastecimiento> pedidosPendientes;


    private Deposito deposito;

    /*
     *
     * Guarda el último recorrido de picking generado durante un despacho
     * exitoso para que la interfaz pueda mostrarlo posteriormente.
     */
    private ListaArray<PasoPicking> ultimoRecorridoPicking;

    public AlmacenLogistico() {
        this.inventario = new Inventario();
        this.terminales = new ListaArray<>();
        this.entregasPendientes = new Cola<>();
        this.pedidosPendientes = new ColaPrioridad<>(
                Comparator.comparingInt(PedidoReabastecimiento::getPrioridad));


        this.deposito = new Deposito();
        this.ultimoRecorridoPicking = new ListaArray<>();
    }

    private TerminalCarga buscarTerminalPorNumero(int numero) {
        for (int i = 0; i < terminales.tamaño(); i++) {
            TerminalCarga terminal = terminales.obtener(i);
            if (terminal.getNumero() == numero) {
                return terminal;
            }
        }
        return null;
    }

    public void registrarTerminal(TerminalCarga terminal) {
        if (terminales.contiene(terminal)) {
            throw new IllegalArgumentException("Terminal ya registrada " + terminal.getNumero());
        }
        terminales.agregar(terminal);
    }

    public void registrarProducto(Producto producto, int stockInicial) {
        if (producto == null || stockInicial < 0) {
            throw new IllegalArgumentException("Producto nulo o stock inicial negativo");
        }
        if (inventario.buscarItem(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("El producto ya esta registrado");
        }
        // Validar todo antes del alta: nunca queda un producto registrado a medias.
        ListaArray<AsignacionPlanificada> plan = new ListaArray<>();
        if (!planificarIngreso(producto, stockInicial, plan,
                new ListaArray<>(), obtenerPosicionesHabilitadasEnPreOrden())) {
            throw new IllegalStateException("No hay capacidad para ubicar el stock inicial");
        }
        inventario.registrarProducto(producto, 0);
        inventario.buscarItem(producto.getCodigo()).activarStockUbicado();
        aplicarPlanDescarga(plan);
    }

    public void registrarLlegadaProveedor(EntregaProveedor entrega) {

        entregasPendientes.agregar(entrega);
    }

    public TerminalCarga asignarProximaEntrega() {
        if (entregasPendientes.esVacio()) {
            return null;
        }
        TerminalCarga terminal = buscarTerminalLibre();
        if (terminal == null) {
            return null;
        }
        EntregaProveedor entrega = entregasPendientes.quitaDeCola();
        terminal.asignarOperacion(entrega);
        return terminal;
    }

    public void finalizarDescarga(int numeroTerminal) {
        TerminalCarga terminal = buscarTerminalPorNumero(numeroTerminal);
        if (terminal == null) {
            throw new IllegalArgumentException("No existe la terminal " + numeroTerminal);
        }

        OperacionLogistica operacion = terminal.getOperacionActual();
        if (operacion == null || operacion.getTipoOperacion() != TipoOperacion.DESCARGA) {
            throw new IllegalStateException("La terminal " + numeroTerminal + " no tiene una descarga en curso");
        }

        /*
         *
         * La firma del método se mantiene. Lo que cambia es el flujo interno:
         * ya no se aumenta únicamente el stock general, porque ahora cada
         * unidad recibida debe quedar asociada a una posición física.
         *
         * Primero se planifica la descarga completa. Si no existe capacidad
         * suficiente para toda la entrega, no se modifica nada.
         */
        EntregaProveedor entrega = (EntregaProveedor) operacion;
        ListaArray<AsignacionPlanificada> plan = planificarDescarga(entrega);

        if (plan == null) {
            throw new IllegalStateException(
                    "No existe capacidad suficiente para almacenar toda la entrega");
        }

        aplicarPlanDescarga(plan);
        terminal.liberar();
    }

    public void registrarPedidoReabastecimiento(PedidoReabastecimiento pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser null");
        }
        pedidosPendientes.poneEnCola(pedido);
    }

    public TerminalCarga despacharProximoPedido() {
        if (pedidosPendientes.esVacio()) {
            return null;
        }

        TerminalCarga terminal = buscarTerminalLibre();

        if (terminal == null) {
            return null;
        }

        PedidoReabastecimiento pedido = this.pedidosPendientes.frente();
        ListaSimple<LineaProducto> lineas = pedido.getLineas();

        /*
         * Se valida el pedido completo antes de modificar el inventario.
         * Esto también contempla que un mismo producto aparezca
         * en varias líneas.
         */
        if (!hayStockSuficienteParaPedido(lineas)) {
            return null;
        }

        /*
         * Además de validar el stock total, ahora se genera un plan de picking
         * con las posiciones físicas desde las cuales retirar cada producto.
         *
         * generarRecorridoPicking no modifica el inventario. De esta manera,
         * si el pedido no puede completarse con el stock ubicado, no se produce
         * una modificación parcial.
         */

        ListaArray<PasoPicking> picking = generarRecorridoPicking(pedido);

        if (picking == null) {
            return null;
        }

        /*
         * Antes de comenzar a retirar mercadería se verifica nuevamente todo
         * el plan. Así evitamos empezar a modificar el inventario si alguna
         * ubicación ya no coincide con lo planificado.
         */
        validarPickingAplicable(picking);
        aplicarPicking(picking);

        this.pedidosPendientes.quitaDeCola();
        terminal.asignarOperacion(pedido);
        this.ultimoRecorridoPicking = copiarPasos(picking);

        return terminal;
    }

    private boolean hayStockSuficienteParaPedido(
            ListaSimple<LineaProducto> lineas) {

        for (int i = 0; i < lineas.tamaño(); i++) {

            LineaProducto lineaActual = lineas.obtener(i);
            String codigoActual = lineaActual.getProducto().getCodigo();

            boolean productoYaVerificado = false;

            for (int j = 0; j < i; j++) {

                LineaProducto lineaAnterior = lineas.obtener(j);

                if (lineaAnterior
                        .getProducto()
                        .getCodigo()
                        .equals(codigoActual)) {

                    productoYaVerificado = true;
                    break;
                }
            }

            if (productoYaVerificado) {
                continue;
            }

            int cantidadTotalRequerida = 0;

            for (int j = i; j < lineas.tamaño(); j++) {

                LineaProducto linea = lineas.obtener(j);

                if (linea
                        .getProducto()
                        .getCodigo()
                        .equals(codigoActual)) {

                    cantidadTotalRequerida += linea.getCantidad();
                }
            }

            if (!this.inventario.hayStock(
                    lineaActual.getProducto(),
                    cantidadTotalRequerida)) {

                return false;
            }
        }

        return true;
    }

    public void finalizarCarga(int numeroTerminal) {
        TerminalCarga terminal = buscarTerminalPorNumero(numeroTerminal);
        if (terminal == null) {
            throw new IllegalArgumentException("No existe la terminal " + numeroTerminal);
        }

        OperacionLogistica operacion = terminal.getOperacionActual();
        if (operacion == null || operacion.getTipoOperacion() != TipoOperacion.CARGA) {
            throw new IllegalStateException("La terminal " + numeroTerminal + " no tiene una carga en curso");
        }

        terminal.liberar();
    }

    public TerminalCarga buscarTerminalLibre() {
        for (int i = 0; i < terminales.tamaño(); i++) {
            TerminalCarga terminal = terminales.obtener(i);
            if (terminal.getEstado() == EstadoTerminal.LIBRE) {
                return terminal;
            }
        }
        return null;
    }

    public int cantidadTotalUnidadesEnInventario() {
        int total = 0;
        ListaArray<ItemInventario> items = inventario.getItems();
        for (int i = 0; i < items.tamaño(); i++) {
            total += items.obtener(i).getStock();
        }
        return total;
    }

    public int contarTerminalesPorEstado(EstadoTerminal estado) {
        int contador = 0;
        for (int i = 0; i < terminales.tamaño(); i++) {
            if (terminales.obtener(i).getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    public int cantidadEntregasPendientes() {
        return this.entregasPendientes.tamaño();
    }

    public int cantidadPedidosPendientes() {
        return this.pedidosPendientes.tamaño();
    }

    public void deshabilitarTerminal(int numeroTerminal) {
        TerminalCarga terminal = buscarTerminalPorNumero(numeroTerminal);

        if (terminal == null) {
            throw new IllegalArgumentException(
                    "No existe la terminal " + numeroTerminal);
        }

        terminal.deshabilitar();
    }

    public void habilitarTerminal(int numeroTerminal) {
        TerminalCarga terminal = buscarTerminalPorNumero(numeroTerminal);

        if (terminal == null) {
            throw new IllegalArgumentException(
                    "No existe la terminal " + numeroTerminal);
        }

        terminal.habilitar();
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public ListaArray<TerminalCarga> getTerminales() {
        return this.terminales;
    }

    public ListaSimple<ItemInventario> productosConStockBajo(int umbral) {
        ListaSimple<ItemInventario> resultado = new ListaSimple<>();
        ListaArray<ItemInventario> items = inventario.getItems();
        for (int i = 0; i < items.tamaño(); i++) {
            ItemInventario item = items.obtener(i);
            if (item.getStock() < umbral) {
                resultado.agregar(item);
            }
        }
        return resultado;
    }

    public EntregaProveedor buscarEntregaPendientePorProveedor(String proveedorId) {
        for (int i = 0; i < entregasPendientes.tamaño(); i++) {
            EntregaProveedor entrega = entregasPendientes.obtener(i);
            if (entrega.getProveedor().getId().equals(proveedorId)) {
                return entrega;
            }
        }
        return null;
    }

    public PedidoReabastecimiento buscarPedidoPendientePorSucursal(String sucursalId) {
        return pedidosPendientes.buscar(
                pedido -> pedido.getSucursal().getId().equals(sucursalId));
    }

    // =========================================================
    // PRODUCTOS E INVENTARIO
    // =========================================================

    /**
     * Busca un producto por código utilizando la operación de Inventario.
     *
     */
    public ItemInventario buscarProducto(String codigoProducto) {
        return this.inventario.buscarItem(codigoProducto);
    }

    /**
     * Devuelve el inventario en el orden producido por el recorrido inOrder
     * del AVL del inventario.
     *
     */
    public ListaArray<ItemInventario> listarInventarioOrdenado() {
        return this.inventario.getItems();
    }

    /**
     * Devuelve las ubicaciones físicas asociadas a un producto.
     */
    public ListaSimple<UbicacionStock> ubicacionesDeProducto(
            String codigoProducto) {

        ItemInventario item = this.inventario.buscarItem(codigoProducto);

        if (item == null) {
            return new ListaSimple<>();
        }

        return item.obtenerUbicaciones();
    }

    // =========================================================
    // DEPOSITO
    // =========================================================

    /**
     * Agrega un sector delegando la operación en Deposito.
     *
     * AlmacenLogistico no manipula directamente los nodos del árbol general.
     */
    public void agregarSector(String rutaPadre, Sector sector) {
        this.deposito.agregarSector(rutaPadre, sector);
    }

    /**
     * Busca un sector por su ruta dentro del depósito.
     */
    public Sector buscarSector(String ruta) {
        return this.deposito.buscarSectorPorRuta(ruta);
    }

    /**
     * Mueve un sector completo delegando el movimiento del subárbol en
     * Deposito.
     */
    public void moverSector(String rutaOrigen, String rutaDestino) {
        this.deposito.moverSector(rutaOrigen, rutaDestino);
    }

    /**
     * Obtiene la ocupación total de un sector y de su subárbol.
     */
    public int obtenerOcupacionSector(String ruta) {
        return this.deposito.obtenerOcupacion(ruta);
    }

    /**
     * Obtiene el espacio todavía disponible en un sector.
     */
    public int obtenerEspacioDisponibleSector(String ruta) {
        return this.deposito.obtenerEspacioDisponible(ruta);
    }

    /**
     * Obtiene la mercadería contenida dentro de un sector y su subárbol.
     */
    public ListaSimple<UbicacionStock> obtenerMercaderiaSector(
            String ruta) {

        return this.deposito.obtenerContenidoSector(ruta);
    }

    /**
     * Permite a la interfaz mostrar el depósito por niveles.
     *
     * El recorrido jerárquico pertenece a Deposito/ArbolGeneral. Esta clase
     * únicamente recibe cada Sector y lo agrega a una ListaArray propia.
     */
    public ListaArray<Sector> listarSectoresPorNiveles() {
        ListaArray<Sector> resultado = new ListaArray<>();
        this.deposito.recorrerPorNiveles(sector -> resultado.agregar(sector));
        return resultado;
    }

    /**
     * Obtiene la ruta de un sector conocido.
     */
    public String obtenerRutaSector(Sector sector) {
        if (sector == null) {
            throw new IllegalArgumentException(
                    "El sector no puede ser null");
        }

        return this.deposito.obtenerRuta(sector);
    }

    /**
     * Devuelve el depósito utilizado por el sistema.
     */
    public Deposito getDeposito() {
        return this.deposito;
    }

    // =========================================================
    // RECEPCION UBICADA
    // =========================================================

    /**
     * Construye un plan completo para ubicar una entrega.
     *
     * Este método NO modifica el inventario ni el depósito. Primero verifica
     * que todas las líneas puedan ubicarse respetando las capacidades.
     *
     * @return plan completo o null si la entrega no entra completamente
     */
    private ListaArray<AsignacionPlanificada> planificarDescarga(
            EntregaProveedor entrega) {

        ListaArray<AsignacionPlanificada> plan = new ListaArray<>();
        ListaArray<AjusteCapacidad> ajustes = new ListaArray<>();
        ListaArray<Sector> posiciones =
                obtenerPosicionesHabilitadasEnPreOrden();

        ListaSimple<LineaProducto> lineas = entrega.getLineas();

        for (int i = 0; i < lineas.tamaño(); i++) {
            LineaProducto linea = lineas.obtener(i);
            Producto producto = linea.getProducto();

            ItemInventario item =
                    this.inventario.buscarItem(producto.getCodigo());

            if (item == null) {
                throw new IllegalStateException(
                        "El producto "
                                + producto.getCodigo()
                                + " no esta registrado en el inventario");
            }

            validarItemUbicadoParaHito2(item);

            // La identidad es el codigo; las dimensiones son las del catalogo.
            producto = item.getProducto();

            if (!planificarIngreso(producto, linea.getCantidad(), plan, ajustes, posiciones)) {
                return null;
            }
        }

        return plan;
    }

    /** Comparte la reserva virtual de capacidad entre el alta y la descarga. */
    private boolean planificarIngreso(Producto producto, int cantidad,
            ListaArray<AsignacionPlanificada> plan, ListaArray<AjusteCapacidad> ajustes,
            ListaArray<Sector> posiciones) {
        int restante = cantidad;
        for (int i = 0; i < posiciones.tamaño() && restante > 0; i++) {
            Sector posicion = posiciones.obtener(i);
            int disponibles = calcularUnidadesDisponiblesPlanificadas(posicion, producto, ajustes);
            int asignar = Math.min(restante, disponibles);
            if (asignar > 0) {
                plan.agregar(new AsignacionPlanificada(producto, posicion, asignar));
                registrarAjusteEnCamino(posicion,
                        Math.multiplyExact(asignar, producto.getEspacioUnitario()), ajustes);
                restante -= asignar;
            }
        }
        return restante == 0;
    }

    /**
     * Aplica un plan de descarga que ya fue validado completamente.
     */
    private void aplicarPlanDescarga(
            ListaArray<AsignacionPlanificada> plan) {

        for (int i = 0; i < plan.tamaño(); i++) {
            AsignacionPlanificada asignacion = plan.obtener(i);

            ItemInventario item =
                    this.inventario.buscarItem(
                            asignacion.producto.getCodigo());

            UbicacionStock existente =
                    item.buscarUbicacion(asignacion.posicion);

            UbicacionStock ubicacion =
                    item.aumentarEnUbicacion(
                            asignacion.posicion,
                            asignacion.cantidad);

            /*
             * La misma UbicacionStock es referenciada por ItemInventario y
             * Deposito. Solo se agrega al depósito cuando todavía no existía
             * para esa posición.
             */
            if (existente == null) {
                this.deposito.agregarUbicacion(ubicacion);
            }
        }
    }

    // =========================================================
    // HITO 2 - ESTUDIANTE 5
    // PEDIDOS, PRIORIDAD Y PICKING
    // =========================================================

    /**
     * Modifica la prioridad de un pedido pendiente.
     *
     * El Estudiante 5 no implementa el heap: cambia la prioridad del pedido y
     * solicita a ColaPrioridad que restablezca su orden mediante reordenar.
     */
    public void modificarPrioridadPedido(
            String idPedido,
            int nuevaPrioridad) {

        if (idPedido == null || idPedido.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del pedido no puede ser nulo o vacio");
        }

        if (nuevaPrioridad < 0) {
            throw new IllegalArgumentException(
                    "La prioridad no puede ser negativa");
        }

        PedidoReabastecimiento pedido =
                this.pedidosPendientes.buscar(
                        actual -> actual.getId().equals(idPedido));

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "No existe un pedido pendiente con id " + idPedido);
        }

        /*
         * Estas dos operaciones deben ser provistas por el Estudiante 3:
         * PedidoReabastecimiento.setPrioridad(...) y
         * ColaPrioridad.reordenar(...).
         *
         * AlmacenLogistico solamente coordina el cambio de prioridad;
         * no implementa el heap ni sus operaciones internas.
         */
        pedido.setPrioridad(nuevaPrioridad);
        this.pedidosPendientes.reordenar(pedido);
    }

    /**
     * Genera un recorrido de picking sin modificar el inventario.
     *
     * Las posiciones se reciben en el orden de preorden proporcionado por
     * Deposito. El recorrido del árbol NO se implementa en AlmacenLogistico.
     *
     * @return pasos necesarios o null si el pedido no puede completarse
     */
    public ListaArray<PasoPicking> generarRecorridoPicking(
            PedidoReabastecimiento pedido) {

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido no puede ser null");
        }

        ListaArray<RequerimientoProducto> requerimientos =
                consolidarRequerimientos(pedido.getLineas());

        if (requerimientos == null) {
            return null;
        }

        /*
         * Antes de recorrer posiciones se verifica que el stock total de cada
         * producto sea suficiente y que ese stock esté representado mediante
         * ubicaciones físicas.
         */
        for (int i = 0; i < requerimientos.tamaño(); i++) {
            RequerimientoProducto requerimiento =
                    requerimientos.obtener(i);

            if (requerimiento.item.getStock()
                    < requerimiento.cantidadTotal) {

                return null;
            }

            validarItemUbicadoParaHito2(requerimiento.item);
        }

        ListaArray<PasoPicking> pasos = new ListaArray<>();
        ListaArray<Sector> posiciones =
                obtenerPosicionesHabilitadasEnPreOrden();

        /*
         * Se recorren las posiciones en preorden. Una vez visitada una rama,
         * sus posiciones se procesan antes de continuar con la siguiente,
         * según el orden entregado por Deposito.
         */
        for (int i = 0; i < posiciones.tamaño(); i++) {
            Sector posicion = posiciones.obtener(i);

            for (int j = 0; j < requerimientos.tamaño(); j++) {
                RequerimientoProducto requerimiento =
                        requerimientos.obtener(j);

                if (requerimiento.restante == 0) {
                    continue;
                }

                UbicacionStock ubicacion =
                        requerimiento.item.buscarUbicacion(posicion);

                if (ubicacion == null) {
                    continue;
                }

                int retirar = menor(
                        requerimiento.restante,
                        ubicacion.getCantidad());

                if (retirar > 0) {
                    pasos.agregar(
                            new PasoPicking(
                                    this.deposito.obtenerRuta(posicion),
                                    requerimiento.item.getProducto(),
                                    retirar));

                    requerimiento.restante -= retirar;
                }
            }
        }

        for (int i = 0; i < requerimientos.tamaño(); i++) {
            if (requerimientos.obtener(i).restante > 0) {
                return null;
            }
        }

        return pasos;
    }

    /**
     * Genera el picking del próximo pedido prioritario sin modificar el
     * inventario.
     */
    public ListaArray<PasoPicking>
    generarRecorridoPickingProximoPedido() {

        if (this.pedidosPendientes.esVacio()) {
            return new ListaArray<>();
        }

        return generarRecorridoPicking(
                this.pedidosPendientes.frente());
    }

    /**
     * Verifica completamente un plan de picking antes de comenzar a aplicarlo.
     *
     * Esto refuerza la regla de no producir modificaciones parciales.
     */
    private void validarPickingAplicable(
            ListaArray<PasoPicking> picking) {

        for (int i = 0; i < picking.tamaño(); i++) {
            PasoPicking paso = picking.obtener(i);

            Sector posicion =
                    this.deposito.buscarSectorPorRuta(
                            paso.getRutaPosicion());

            ItemInventario item =
                    this.inventario.buscarItem(
                            paso.getProducto().getCodigo());

            if (posicion == null || item == null) {
                throw new IllegalStateException(
                        "El plan de picking referencia datos que ya no existen");
            }

            UbicacionStock ubicacion =
                    item.buscarUbicacion(posicion);

            if (ubicacion == null
                    || ubicacion.getCantidad() < paso.getCantidad()) {

                throw new IllegalStateException(
                        "El plan de picking ya no coincide con el stock ubicado");
            }
        }
    }

    /**
     * Retira físicamente del inventario las cantidades indicadas por un plan
     * de picking ya validado.
     */
    private void aplicarPicking(
            ListaArray<PasoPicking> picking) {

        for (int i = 0; i < picking.tamaño(); i++) {
            PasoPicking paso = picking.obtener(i);

            Sector posicion =
                    this.deposito.buscarSectorPorRuta(
                            paso.getRutaPosicion());

            ItemInventario item =
                    this.inventario.buscarItem(
                            paso.getProducto().getCodigo());

            UbicacionStock ubicacion =
                    item.buscarUbicacion(posicion);

            boolean eliminada =
                    item.retirarDeUbicacion(
                            posicion,
                            paso.getCantidad());

            /*
             * Si ya no queda ninguna unidad del producto en esa posición, se
             * elimina también la referencia mantenida por Deposito.
             */
            if (eliminada) {
                this.deposito.removerUbicacion(ubicacion);
            }
        }
    }

    /**
     * Devuelve una copia de la estructura del último picking realizado.
     */
    public ListaArray<PasoPicking> getUltimoRecorridoPicking() {
        return copiarPasos(this.ultimoRecorridoPicking);
    }

    // =========================================================
    // INHABILITACION Y REUBICACION
    // =========================================================

    /**
     * Inhabilita un sector únicamente si toda la mercadería contenida en ese
     * sector o en sus descendientes puede reubicarse fuera del subárbol.
     *
     * La reubicación primero se planifica completamente. Si no existe
     * capacidad suficiente, no se modifica ninguna ubicación.
     */
    public void inhabilitarSector(String ruta) {
        Sector sector =
                this.deposito.buscarSectorPorRuta(ruta);

        if (sector == null) {
            throw new IllegalArgumentException(
                    "No existe el sector indicado");
        }

        if (!sector.estaHabilitado()) {
            throw new IllegalStateException(
                    "El sector ya se encuentra inhabilitado");
        }

        ListaSimple<Sector> camino =
                this.deposito.obtenerCamino(ruta);

        if (camino.tamaño() <= 1) {
            throw new IllegalArgumentException(
                    "No se puede inhabilitar la raiz del deposito");
        }

        ListaSimple<UbicacionStock> afectadas =
                this.deposito.obtenerContenidoSector(ruta);

        ListaArray<MovimientoPlanificado> plan =
                planificarReubicacion(
                        sector,
                        afectadas);

        if (plan == null) {
            throw new IllegalStateException(
                    "No existe capacidad suficiente para reubicar toda la mercaderia");
        }

        /*
         * Antes de aplicar se comprueba que las ubicaciones de origen siguen
         * teniendo las cantidades utilizadas para construir el plan.
         */
        validarPlanReubicacionAplicable(plan);
        aplicarReubicacion(plan);
        sector.deshabilitar();
    }

    /**
     * Planifica la reubicación de todas las ubicaciones afectadas sin
     * modificar el estado real.
     */
    private ListaArray<MovimientoPlanificado>
    planificarReubicacion(
            Sector sectorInhabilitado,
            ListaSimple<UbicacionStock> afectadas) {

        ListaArray<MovimientoPlanificado> plan =
                new ListaArray<>();

        ListaArray<AjusteCapacidad> ajustes =
                new ListaArray<>();

        ListaArray<Sector> candidatas =
                obtenerPosicionesHabilitadasEnPreOrden();

        for (int i = 0; i < afectadas.tamaño(); i++) {
            UbicacionStock origen =
                    afectadas.obtener(i);

            Producto producto =
                    origen.getProducto();

            ItemInventario item =
                    this.inventario.buscarItem(
                            producto.getCodigo());

            if (item == null) {
                throw new IllegalStateException(
                        "Existe mercaderia de un producto no registrado");
            }

            validarItemUbicadoParaHito2(item);

            int restante = origen.getCantidad();

            /*
             * Primero se simula que toda la mercadería sale de su ubicación
             * original. Esto es necesario cuando origen y destino comparten
             * sectores ancestros cuya capacidad está completa.
             */
            registrarAjusteEnCamino(
                    origen.getPosicion(),
                    -(restante * producto.getEspacioUnitario()),
                    ajustes);

            for (int j = 0;
                 j < candidatas.tamaño() && restante > 0;
                 j++) {

                Sector destino =
                        candidatas.obtener(j);

                /*
                 * Una mercadería perteneciente al sector que será
                 * inhabilitado no puede reubicarse dentro del mismo subárbol.
                 */
                if (esDescendienteOSiMismo(
                        sectorInhabilitado,
                        destino)) {

                    continue;
                }

                int disponibles =
                        calcularUnidadesDisponiblesPlanificadas(
                                destino,
                                producto,
                                ajustes);

                if (disponibles <= 0) {
                    continue;
                }

                int mover =
                        menor(restante, disponibles);

                plan.agregar(
                        new MovimientoPlanificado(
                                producto,
                                origen.getPosicion(),
                                destino,
                                mover));

                registrarAjusteEnCamino(
                        destino,
                        mover * producto.getEspacioUnitario(),
                        ajustes);

                restante -= mover;
            }

            if (restante > 0) {
                return null;
            }
        }

        return plan;
    }

    /**
     * Comprueba el plan completo antes de modificar las ubicaciones reales.
     */
    private void validarPlanReubicacionAplicable(
            ListaArray<MovimientoPlanificado> plan) {

        for (int i = 0; i < plan.tamaño(); i++) {
            MovimientoPlanificado movimiento =
                    plan.obtener(i);

            ItemInventario item =
                    this.inventario.buscarItem(
                            movimiento.producto.getCodigo());

            if (item == null) {
                throw new IllegalStateException(
                        "El plan de reubicacion referencia un producto inexistente");
            }

            UbicacionStock origen =
                    item.buscarUbicacion(
                            movimiento.origen);

            if (origen == null
                    || origen.getCantidad() < movimiento.cantidad) {

                throw new IllegalStateException(
                        "El plan de reubicacion ya no coincide con el inventario");
            }
        }
    }

    /**
     * Aplica un plan de reubicación que ya fue validado.
     *
     * La cantidad total de stock no cambia: se retira de una posición y se
     * agrega la misma cantidad en otra.
     */
    private void aplicarReubicacion(
            ListaArray<MovimientoPlanificado> plan) {

        for (int i = 0; i < plan.tamaño(); i++) {
            MovimientoPlanificado movimiento =
                    plan.obtener(i);

            ItemInventario item =
                    this.inventario.buscarItem(
                            movimiento.producto.getCodigo());

            UbicacionStock origen =
                    item.buscarUbicacion(
                            movimiento.origen);

            boolean origenEliminado =
                    item.retirarDeUbicacion(
                            movimiento.origen,
                            movimiento.cantidad);

            if (origenEliminado) {
                this.deposito.removerUbicacion(origen);
            }

            UbicacionStock destinoAnterior =
                    item.buscarUbicacion(
                            movimiento.destino);

            UbicacionStock destino =
                    item.aumentarEnUbicacion(
                            movimiento.destino,
                            movimiento.cantidad);

            if (destinoAnterior == null) {
                this.deposito.agregarUbicacion(destino);
            }
        }
    }

    // =========================================================
    // HELPERS DE INTEGRACION
    // =========================================================

    /**
     * Obtiene las posiciones habilitadas en el orden de preorden definido por
     * Deposito.
     *
     * El recorrido del árbol general no se implementa aquí. Esta clase
     * únicamente consume el resultado entregado por la estructura jerárquica.
     */
    private ListaArray<Sector>
    obtenerPosicionesHabilitadasEnPreOrden() {

        ListaArray<Sector> posiciones =
                this.deposito.listarPosicionesHabilitadas();

        if (posiciones == null) {
            throw new IllegalStateException(
                    "El deposito no pudo listar sus posiciones habilitadas");
        }

        return posiciones;
    }

    /**
     * Calcula cuántas unidades de un producto pueden agregarse en una posición
     * sin superar la capacidad de ninguno de los sectores que forman su ruta.
     *
     * Los cambios que todavía no fueron aplicados se consideran mediante la
     * estructura temporal AjusteCapacidad.
     */
    private int calcularUnidadesDisponiblesPlanificadas(
            Sector posicion,
            Producto producto,
            ListaArray<AjusteCapacidad> ajustes) {

        ListaSimple<Sector> camino =
                this.deposito.obtenerCamino(
                        this.deposito.obtenerRuta(posicion));

        int espacioDisponible = -1;

        for (int i = 0; i < camino.tamaño(); i++) {
            Sector sector =
                    camino.obtener(i);

            AjusteCapacidad estado =
                    obtenerEstadoCapacidad(
                            ajustes,
                            sector);

            int disponibleSector =
                    sector.getCapacidad()
                            - estado.ocupacionInicial
                            - estado.delta;

            if (espacioDisponible == -1
                    || disponibleSector < espacioDisponible) {

                espacioDisponible =
                        disponibleSector;
            }
        }

        if (espacioDisponible <= 0) {
            return 0;
        }

        return espacioDisponible
                / producto.getEspacioUnitario();
    }

    /**
     * Registra un cambio temporal de ocupación sobre todos los sectores que
     * forman el camino hasta una posición.
     *
     * Un delta positivo representa una entrada y uno negativo una salida.
     */
    private void registrarAjusteEnCamino(
            Sector posicion,
            int deltaEspacio,
            ListaArray<AjusteCapacidad> ajustes) {

        ListaSimple<Sector> camino =
                this.deposito.obtenerCamino(
                        this.deposito.obtenerRuta(posicion));

        for (int i = 0; i < camino.tamaño(); i++) {
            AjusteCapacidad estado =
                    obtenerEstadoCapacidad(
                            ajustes,
                            camino.obtener(i));

            estado.delta += deltaEspacio;
        }
    }

    /**
     * Obtiene el estado temporal de capacidad de un sector.
     *
     * La primera vez se calcula la ocupación real. En consultas posteriores se
     * reutiliza ese valor junto con el delta planificado.
     */
    private AjusteCapacidad obtenerEstadoCapacidad(
            ListaArray<AjusteCapacidad> ajustes,
            Sector sector) {

        AjusteCapacidad estado =
                ajustes.buscar(
                        actual -> actual.sector == sector);

        if (estado != null) {
            return estado;
        }

        int ocupacion =
                this.deposito.obtenerOcupacion(
                        this.deposito.obtenerRuta(sector));

        estado =
                new AjusteCapacidad(
                        sector,
                        ocupacion);

        ajustes.agregar(estado);

        return estado;
    }

    /**
     * Comprueba si un sector pertenece al subárbol de otro sector.
     *
     * Para ello utiliza el camino proporcionado por Deposito; no implementa un
     * nuevo recorrido del árbol.
     */
    private boolean esDescendienteOSiMismo(
            Sector posibleAncestro,
            Sector sector) {

        ListaSimple<Sector> camino =
                this.deposito.obtenerCamino(
                        this.deposito.obtenerRuta(sector));

        for (int i = 0; i < camino.tamaño(); i++) {
            if (camino.obtener(i) == posibleAncestro) {
                return true;
            }
        }

        return false;
    }

    /**
     * Consolida líneas repetidas de un pedido.
     *
     * Por ejemplo, dos líneas P001 x 3 y P001 x 5 se interpretan como un
     * requerimiento total de 8 unidades.
     *
     * Se utiliza ListaArray propia porque durante la planificación predominan
     * el recorrido y el acceso por índice, ambos adecuados para esta
     * estructura.
     */
    private ListaArray<RequerimientoProducto>
    consolidarRequerimientos(
            ListaSimple<LineaProducto> lineas) {

        ListaArray<RequerimientoProducto> resultado =
                new ListaArray<>();

        for (int i = 0; i < lineas.tamaño(); i++) {
            LineaProducto linea =
                    lineas.obtener(i);

            String codigo =
                    linea.getProducto().getCodigo();

            RequerimientoProducto existente =
                    resultado.buscar(
                            requerimiento ->
                                    requerimiento.item
                                            .getProducto()
                                            .getCodigo()
                                            .equals(codigo));

            if (existente == null) {
                ItemInventario item =
                        this.inventario.buscarItem(codigo);

                if (item == null) {
                    return null;
                }

                resultado.agregar(
                        new RequerimientoProducto(
                                item,
                                linea.getCantidad()));
            } else {
                existente.cantidadTotal +=
                        linea.getCantidad();

                existente.restante +=
                        linea.getCantidad();
            }
        }

        return resultado;
    }

    /**
     * Valida la consistencia entre el stock general y sus ubicaciones físicas.
     *
     * Los productos registrados con stock inicial del Hito 1 deben ser
     * migrados a ubicaciones antes de participar en los nuevos flujos físicos.
     */
    private void validarItemUbicadoParaHito2(
            ItemInventario item) {

        if (!item.stockCoincideConUbicaciones()) {
            throw new IllegalStateException(
                    "El producto "
                            + item.getProducto().getCodigo()
                            + " posee stock sin ubicacion fisica. "
                            + "Debe ubicarse antes de operar con el modelo del Hito 2.");
        }
    }

    /**
     * Crea una copia estructural del recorrido de picking.
     */
    private ListaArray<PasoPicking> copiarPasos(
            ListaArray<PasoPicking> origen) {

        ListaArray<PasoPicking> copia =
                new ListaArray<>();

        for (int i = 0; i < origen.tamaño(); i++) {
            copia.agregar(
                    origen.obtener(i));
        }

        return copia;
    }

    /**
     * Devuelve el menor de dos enteros.
     */
    private int menor(
            int primero,
            int segundo) {

        return primero < segundo
                ? primero
                : segundo;
    }

    // =========================================================
    // HITO 2 - ESTUDIANTE 5
    // OBJETOS AUXILIARES PRIVADOS
    // =========================================================

    /**
     * Representa una parte de una descarga que fue asignada temporalmente a
     * una posición.
     */
    private static final class AsignacionPlanificada {

        private final Producto producto;
        private final Sector posicion;
        private final int cantidad;

        private AsignacionPlanificada(
                Producto producto,
                Sector posicion,
                int cantidad) {

            this.producto = producto;
            this.posicion = posicion;
            this.cantidad = cantidad;
        }
    }

    /**
     * Representa una transferencia que todavía no fue aplicada.
     */
    private static final class MovimientoPlanificado {

        private final Producto producto;
        private final Sector origen;
        private final Sector destino;
        private final int cantidad;

        private MovimientoPlanificado(
                Producto producto,
                Sector origen,
                Sector destino,
                int cantidad) {

            this.producto = producto;
            this.origen = origen;
            this.destino = destino;
            this.cantidad = cantidad;
        }
    }

    /**
     * Estado temporal de capacidad utilizado mientras se construye un plan.
     */
    private static final class AjusteCapacidad {

        private final Sector sector;
        private final int ocupacionInicial;
        private int delta;

        private AjusteCapacidad(
                Sector sector,
                int ocupacionInicial) {

            this.sector = sector;
            this.ocupacionInicial =
                    ocupacionInicial;
            this.delta = 0;
        }
    }

    /**
     * Cantidad consolidada que todavía falta retirar de un producto durante la
     * generación de un picking.
     */
    private static final class RequerimientoProducto {

        private final ItemInventario item;
        private int cantidadTotal;
        private int restante;

        private RequerimientoProducto(
                ItemInventario item,
                int cantidad) {

            this.item = item;
            this.cantidadTotal = cantidad;
            this.restante = cantidad;
        }
    }
}
