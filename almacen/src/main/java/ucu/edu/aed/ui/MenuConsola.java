package ucu.edu.aed.ui;

import ucu.edu.aed.model.EntregaProveedor;
import ucu.edu.aed.model.EstadoTerminal;
import ucu.edu.aed.model.ItemInventario;
import ucu.edu.aed.model.LineaEntrega;
import ucu.edu.aed.model.LineaPedido;
import ucu.edu.aed.model.PedidoReabastecimiento;
import ucu.edu.aed.model.Producto;
import ucu.edu.aed.model.Proveedor;
import ucu.edu.aed.model.Sucursal;
import ucu.edu.aed.model.TerminalCarga;
import ucu.edu.aed.service.AlmacenLogistico;
import ucu.edu.aed.structures.ListaArray;
import ucu.edu.aed.structures.ListaSimple;

import java.util.Scanner;

/**
 * Proporciona la interfaz de consola para interactuar
 * con el sistema de gestion del almacen logistico.
 */
public class MenuConsola {

    /** Almacen logistico administrado desde el menu. */
    private final AlmacenLogistico almacen;

    /** Scanner utilizado para leer la entrada del usuario. */
    private final Scanner scanner;

    /**
     * Crea un nuevo menu de consola.
     *
     * @param almacen almacen logistico a administrar
     */
    public MenuConsola(AlmacenLogistico almacen) {
        if (almacen == null) {
            throw new IllegalArgumentException("El almacen no puede ser null");
        }

        this.almacen = almacen;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia la ejecucion del menu principal.
     */
    public void ejecutar() {
        boolean continuar = true;

        System.out.println("========================================");
        System.out.println("      SISTEMA DE ALMACEN LOGISTICO");
        System.out.println("========================================");

        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opcion: ");

            try {
                switch (opcion) {
                    case 1:
                        registrarProducto();
                        break;
                    case 2:
                        registrarEntregaProveedor();
                        break;
                    case 3:
                        asignarEntregaProveedor();
                        break;
                    case 4:
                        finalizarDescarga();
                        break;
                    case 5:
                        registrarPedidoReabastecimiento();
                        break;
                    case 6:
                        despacharPedidoReabastecimiento();
                        break;
                    case 7:
                        finalizarCarga();
                        break;
                    case 8:
                        ejecutarConsultas();
                        break;
                    case 9:
                        mostrarInformacionAlmacen();
                        break;
                    case 10:
                        gestionarTerminales();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("Sistema finalizado.");
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("No se pudo completar la operacion: " + e.getMessage());
            }

            System.out.println();
        }
    }

    /**
     * Muestra las opciones disponibles en el menu principal.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("--------------- MENU -------------------");
        System.out.println("1. Registrar producto");
        System.out.println("2. Registrar entrega de proveedor");
        System.out.println("3. Asignar proxima entrega a terminal");
        System.out.println("4. Finalizar descarga");
        System.out.println("5. Registrar pedido de reabastecimiento");
        System.out.println("6. Despachar proximo pedido");
        System.out.println("7. Finalizar carga");
        System.out.println("8. Consultas");
        System.out.println("9. Mostrar estado general del almacen");
        System.out.println("10. Gestionar terminales");
        System.out.println("0. Salir");
        System.out.println("----------------------------------------");
    }

    /**
     * Gestiona el registro de un producto.
     */
    private void registrarProducto() {
        System.out.println("\n--- Registrar producto ---");

        String codigo = leerTextoNoVacio("Codigo: ");
        String nombre = leerTextoNoVacio("Nombre: ");
        System.out.print("Descripcion: ");
        String descripcion = this.scanner.nextLine().trim();
        int stockInicial = leerEnteroNoNegativo("Stock inicial: ");

        Producto producto = new Producto(codigo, nombre, descripcion);
        this.almacen.registrarProducto(producto, stockInicial);

        System.out.println("Producto registrado correctamente.");
    }

    /**
     * Gestiona el registro de una entrega de proveedor.
     */
    private void registrarEntregaProveedor() {
        System.out.println("\n--- Registrar entrega de proveedor ---");

        String idEntrega = leerTextoNoVacio("ID de la entrega: ");
        String idProveedor = leerTextoNoVacio("ID del proveedor: ");
        String nombreProveedor = leerTextoNoVacio("Nombre del proveedor: ");

        Proveedor proveedor = new Proveedor(idProveedor, nombreProveedor);
        EntregaProveedor entrega = new EntregaProveedor(idEntrega, proveedor);

        agregarLineasEntrega(entrega);
        this.almacen.registrarLlegadaProveedor(entrega);

        System.out.println("Entrega registrada en la cola de pendientes.");
    }

    /**
     * Gestiona el registro de un pedido de reabastecimiento.
     */
    private void registrarPedidoReabastecimiento() {
        System.out.println("\n--- Registrar pedido de reabastecimiento ---");

        String idPedido = leerTextoNoVacio("ID del pedido: ");
        String idSucursal = leerTextoNoVacio("ID de la sucursal: ");
        String nombreSucursal = leerTextoNoVacio("Nombre de la sucursal: ");
        int cantidadClientes = leerEnteroNoNegativo("Cantidad de clientes de la sucursal: ");

        Sucursal sucursal = new Sucursal(idSucursal, nombreSucursal, cantidadClientes);
        PedidoReabastecimiento pedido = new PedidoReabastecimiento(idPedido, sucursal);

        agregarLineasPedido(pedido);
        this.almacen.registrarPedidoReabastecimiento(pedido);

        System.out.println(
                "Pedido registrado. Prioridad asignada: " + pedido.getPrioridad());
    }

    /**
     * Gestiona la asignacion de una entrega de proveedor
     * a una terminal disponible.
     */
    private void asignarEntregaProveedor() {
        System.out.println("\n--- Asignar entrega ---");

        if (this.almacen.cantidadEntregasPendientes() == 0) {
            System.out.println("No hay entregas pendientes.");
            return;
        }

        TerminalCarga terminal = this.almacen.asignarProximaEntrega();
        if (terminal == null) {
            System.out.println("No hay terminales libres disponibles.");
            return;
        }

        System.out.println(
                "Entrega " + terminal.getOperacionActual().getId()
                        + " asignada a la terminal " + terminal.getNumero() + ".");
    }

    /**
     * Gestiona la finalizacion de una descarga.
     */
    private void finalizarDescarga() {
        System.out.println("\n--- Finalizar descarga ---");
        int numeroTerminal = leerEnteroPositivo("Numero de terminal: ");

        this.almacen.finalizarDescarga(numeroTerminal);
        System.out.println(
                "Descarga finalizada. El inventario fue actualizado y la terminal quedo libre.");
    }

    /**
     * Gestiona el despacho de un pedido de reabastecimiento.
     */
    private void despacharPedidoReabastecimiento() {
        System.out.println("\n--- Despachar pedido ---");

        if (this.almacen.cantidadPedidosPendientes() == 0) {
            System.out.println("No hay pedidos pendientes.");
            return;
        }

        if (this.almacen.buscarTerminalLibre() == null) {
            System.out.println("No hay terminales libres disponibles.");
            return;
        }

        TerminalCarga terminal = this.almacen.despacharProximoPedido();
        if (terminal == null) {
            System.out.println(
                    "El pedido de mayor prioridad no tiene stock suficiente. "
                            + "Permanece pendiente y el inventario no fue modificado.");
            return;
        }

        System.out.println(
                "Pedido " + terminal.getOperacionActual().getId()
                        + " asignado a la terminal " + terminal.getNumero() + ".");
        System.out.println("El stock correspondiente quedo reservado.");
    }

    /**
     * Gestiona la finalizacion de una carga.
     */
    private void finalizarCarga() {
        System.out.println("\n--- Finalizar carga ---");
        int numeroTerminal = leerEnteroPositivo("Numero de terminal: ");

        this.almacen.finalizarCarga(numeroTerminal);
        System.out.println("Carga finalizada. La terminal quedo libre.");
    }

    /**
     * Ejecuta el submenu correspondiente a las consultas del Desafio 2.
     */
    private void ejecutarConsultas() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- Consultas ---");
            System.out.println("1. Productos con stock bajo");
            System.out.println("2. Cantidad total de unidades en inventario");
            System.out.println("3. Buscar entrega pendiente por proveedor");
            System.out.println("4. Buscar pedido pendiente por sucursal");
            System.out.println("5. Contar terminales por estado");
            System.out.println("0. Volver");

            int opcion = leerEntero("Seleccione una consulta: ");

            switch (opcion) {
                case 1:
                    consultarProductosConStockBajo();
                    break;
                case 2:
                    System.out.println(
                            "Unidades totales en inventario: "
                                    + this.almacen.cantidadTotalUnidadesEnInventario());
                    break;
                case 3:
                    consultarEntregaPendientePorProveedor();
                    break;
                case 4:
                    consultarPedidoPendientePorSucursal();
                    break;
                case 5:
                    consultarTerminalesPorEstado();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    /**
     * Muestra los productos cuyo stock se encuentra por debajo de un umbral.
     */
    private void consultarProductosConStockBajo() {
        int umbral = leerEnteroNoNegativo("Umbral de stock: ");
        ListaSimple<ItemInventario> resultado = this.almacen.productosConStockBajo(umbral);

        if (resultado.esVacio()) {
            System.out.println("No hay productos con stock menor a " + umbral + ".");
            return;
        }

        System.out.println("Productos con stock bajo:");
        for (int i = 0; i < resultado.tamaño(); i++) {
            ItemInventario item = resultado.obtener(i);
            System.out.println(
                    "- " + item.getProducto().getCodigo()
                            + " | " + item.getProducto().getNombre()
                            + " | stock: " + item.getStock());
        }
    }

    /**
     * Busca una entrega pendiente utilizando el identificador del proveedor.
     */
    private void consultarEntregaPendientePorProveedor() {
        String proveedorId = leerTextoNoVacio("ID del proveedor: ");
        EntregaProveedor entrega =
                this.almacen.buscarEntregaPendientePorProveedor(proveedorId);

        if (entrega == null) {
            System.out.println("No se encontro una entrega pendiente para ese proveedor.");
        } else {
            System.out.println("Entrega encontrada: " + entrega);
        }
    }

    /**
     * Busca un pedido pendiente utilizando el identificador de la sucursal.
     */
    private void consultarPedidoPendientePorSucursal() {
        String sucursalId = leerTextoNoVacio("ID de la sucursal: ");
        PedidoReabastecimiento pedido =
                this.almacen.buscarPedidoPendientePorSucursal(sucursalId);

        if (pedido == null) {
            System.out.println("No se encontro un pedido pendiente para esa sucursal.");
        } else {
            System.out.println("Pedido encontrado: " + pedido);
        }
    }

    /**
     * Cuenta las terminales de acuerdo con el estado elegido por el usuario.
     */
    private void consultarTerminalesPorEstado() {
        EstadoTerminal estado = leerEstadoTerminal();
        int cantidad = this.almacen.contarTerminalesPorEstado(estado);

        System.out.println("Terminales en estado " + estado + ": " + cantidad);
    }

    /**
     * Muestra la informacion actual del almacen logistico.
     */
    private void mostrarInformacionAlmacen() {
        System.out.println("\n--- Estado general del almacen ---");

        ListaArray<ItemInventario> items = this.almacen.getInventario().getItems();
        System.out.println("Inventario:");
        if (items.esVacio()) {
            System.out.println("- Sin productos registrados.");
        } else {
            for (int i = 0; i < items.tamaño(); i++) {
                ItemInventario item = items.obtener(i);
                System.out.println(
                        "- " + item.getProducto().getCodigo()
                                + " | " + item.getProducto().getNombre()
                                + " | stock: " + item.getStock());
            }
        }

        ListaArray<TerminalCarga> terminales = this.almacen.getTerminales();
        System.out.println("Terminales:");
        if (terminales.esVacio()) {
            System.out.println("- Sin terminales registradas.");
        } else {
            for (int i = 0; i < terminales.tamaño(); i++) {
                TerminalCarga terminal = terminales.obtener(i);
                String operacion = terminal.getOperacionActual() == null
                        ? "-"
                        : terminal.getOperacionActual().getId();

                System.out.println(
                        "- Terminal " + terminal.getNumero()
                                + " | estado: " + terminal.getEstado()
                                + " | operacion: " + operacion);
            }
        }

        System.out.println(
                "Entregas pendientes: " + this.almacen.cantidadEntregasPendientes());
        System.out.println(
                "Pedidos pendientes: " + this.almacen.cantidadPedidosPendientes());
        System.out.println(
                "Unidades totales en inventario: "
                        + this.almacen.cantidadTotalUnidadesEnInventario());
    }

    /**
     * Permite registrar, habilitar o deshabilitar terminales.
     */
    private void gestionarTerminales() {
        System.out.println("\n--- Gestion de terminales ---");
        System.out.println("1. Registrar terminal");
        System.out.println("2. Deshabilitar terminal");
        System.out.println("3. Habilitar terminal");
        System.out.println("0. Volver");

        int opcion = leerEntero("Seleccione una opcion: ");

        switch (opcion) {
            case 1:
                int numero = leerEnteroPositivo("Numero de la nueva terminal: ");
                this.almacen.registrarTerminal(new TerminalCarga(numero));
                System.out.println("Terminal registrada correctamente.");
                break;
            case 2:
                int numeroDeshabilitar = leerEnteroPositivo("Numero de terminal: ");
                this.almacen.deshabilitarTerminal(numeroDeshabilitar);
                System.out.println("Terminal deshabilitada correctamente.");
                break;
            case 3:
                int numeroHabilitar = leerEnteroPositivo("Numero de terminal: ");
                this.almacen.habilitarTerminal(numeroHabilitar);
                System.out.println("Terminal habilitada correctamente.");
                break;
            case 0:
                break;
            default:
                System.out.println("Opcion invalida.");
        }
    }

    /**
     * Agrega una o mas lineas a una entrega.
     *
     * @param entrega entrega que recibira las lineas
     */
    private void agregarLineasEntrega(EntregaProveedor entrega) {
        boolean agregarOtra;

        do {
            Producto producto = leerProductoRegistrado();
            int cantidad = leerEnteroPositivo("Cantidad entregada: ");

            entrega.agregarLinea(new LineaEntrega(producto, cantidad));
            agregarOtra = leerSiNo("Desea agregar otra linea? (s/n): ");
        } while (agregarOtra);
    }

    /**
     * Agrega una o mas lineas a un pedido.
     *
     * @param pedido pedido que recibira las lineas
     */
    private void agregarLineasPedido(PedidoReabastecimiento pedido) {
        boolean agregarOtra;

        do {
            Producto producto = leerProductoRegistrado();
            int cantidad = leerEnteroPositivo("Cantidad solicitada: ");

            pedido.agregarLinea(new LineaPedido(producto, cantidad));
            agregarOtra = leerSiNo("Desea agregar otra linea? (s/n): ");
        } while (agregarOtra);
    }

    /**
     * Solicita un codigo hasta encontrar un producto registrado.
     *
     * @return producto registrado correspondiente al codigo ingresado
     */
    private Producto leerProductoRegistrado() {
        while (true) {
            String codigo = leerTextoNoVacio("Codigo del producto: ");
            ItemInventario item = this.almacen.getInventario().buscarItem(codigo);

            if (item != null) {
                return item.getProducto();
            }

            System.out.println(
                    "No existe un producto registrado con ese codigo. "
                            + "Registre el producto antes de utilizarlo.");
        }
    }

    /**
     * Lee un estado de terminal desde consola.
     *
     * @return estado seleccionado
     */
    private EstadoTerminal leerEstadoTerminal() {
        while (true) {
            System.out.println("1. LIBRE");
            System.out.println("2. OCUPADA");
            System.out.println("3. DESHABILITADA");

            int opcion = leerEntero("Estado: ");
            switch (opcion) {
                case 1:
                    return EstadoTerminal.LIBRE;
                case 2:
                    return EstadoTerminal.OCUPADA;
                case 3:
                    return EstadoTerminal.DESHABILITADA;
                default:
                    System.out.println("Estado invalido.");
            }
        }
    }

    /**
     * Lee un entero desde consola y repite la solicitud si el valor no es valido.
     *
     * @param mensaje mensaje mostrado al usuario
     * @return entero ingresado
     */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = this.scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    /**
     * Lee un entero mayor que cero.
     *
     * @param mensaje mensaje mostrado al usuario
     * @return entero positivo
     */
    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) {
                return valor;
            }
            System.out.println("El valor debe ser mayor que cero.");
        }
    }

    /**
     * Lee un entero mayor o igual a cero.
     *
     * @param mensaje mensaje mostrado al usuario
     * @return entero no negativo
     */
    private int leerEnteroNoNegativo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= 0) {
                return valor;
            }
            System.out.println("El valor no puede ser negativo.");
        }
    }

    /**
     * Lee un texto no vacio.
     *
     * @param mensaje mensaje mostrado al usuario
     * @return texto ingresado sin espacios laterales
     */
    private String leerTextoNoVacio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = this.scanner.nextLine().trim();

            if (!texto.isBlank()) {
                return texto;
            }

            System.out.println("El valor no puede estar vacio.");
        }
    }

    /**
     * Lee una respuesta afirmativa o negativa.
     *
     * @param mensaje mensaje mostrado al usuario
     * @return true si la respuesta es s; false si es n
     */
    private boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = this.scanner.nextLine().trim().toLowerCase();

            if (respuesta.equals("s") || respuesta.equals("si")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }

            System.out.println("Ingrese 's' o 'n'.");
        }
    }
}