# Roadmap de cierre — Almacén Logístico

## Fase 1 — Trabajo paralelo

### Integrante 1 — Modelo de dominio

**Archivos bajo su responsabilidad**
- `Producto.java`
- `Proveedor.java`
- `Sucursal.java`
- `LineaEntrega.java`
- `LineaPedido.java`
- `EntregaProveedor.java`
- `PedidoReabastecimiento.java`

**Checklist**
- [ ] Implementar todos los constructores.
- [ ] Implementar todos los getters.
- [ ] Inicializar `ListaSimple` en entregas y pedidos.
- [ ] Implementar `agregarLinea()`.
- [ ] Implementar `getPrioridad()` usando la cantidad de clientes de la sucursal.
- [ ] Implementar correctamente `getTipoOperacion()`.
- [ ] Validar IDs/nombres nulos cuando corresponda.
- [ ] Rechazar cantidades de líneas `<= 0`.
- [ ] Agregar `toString()` donde facilite la interfaz de consola.
- [ ] Crear pruebas unitarias de estas clases.
- [ ] Documentar para el informe las decisiones del modelo.

**Definition of Done:** ninguna clase de este bloque contiene `UnsupportedOperationException` ni `A implementar`.

---

### Integrante 2 — Inventario y terminales

**Archivos bajo su responsabilidad**
- `ItemInventario.java`
- `Inventario.java`
- `TerminalCarga.java`

**Checklist**
- [ ] Implementar `ItemInventario`.
- [ ] Impedir stock negativo.
- [ ] Implementar alta de productos.
- [ ] Decidir y controlar productos duplicados por código.
- [ ] Implementar búsqueda de productos.
- [ ] Implementar `obtenerStock()`.
- [ ] Implementar `aumentarStock()`.
- [ ] Implementar `disminuirStock()`.
- [ ] Implementar `hayStock()`.
- [ ] Implementar estados `LIBRE`, `OCUPADA` y `DESHABILITADA`.
- [ ] Impedir asignar una operación a una terminal no libre.
- [ ] Definir qué ocurre al intentar deshabilitar una terminal ocupada.
- [ ] Crear pruebas de inventario y transiciones de terminal.
- [ ] Documentar invariantes y complejidades.

**Definition of Done:** inventario y terminales pueden utilizarse independientemente y tienen pruebas de casos borde.

---

### Integrante 3 — Lógica central del almacén

**Archivo principal**
- `AlmacenLogistico.java`

**Checklist**
- [ ] Implementar constructor e inicializar todas las estructuras.
- [ ] Implementar `registrarTerminal()`.
- [ ] Implementar `registrarProducto()`.
- [ ] Implementar `registrarLlegadaProveedor()`.
- [ ] Implementar `buscarTerminalLibre()`.
- [ ] Implementar `asignarProximaEntrega()`.
- [ ] No retirar una entrega de la cola si no existe terminal disponible.
- [ ] Implementar `finalizarDescarga()`.
- [ ] Aumentar stock exclusivamente al finalizar una descarga.
- [ ] Implementar `registrarPedidoReabastecimiento()`.
- [ ] Configurar `ColaPrioridad` según `PedidoReabastecimiento.getPrioridad()`.
- [ ] Implementar `despacharProximoPedido()`.
- [ ] Comprobar stock de todas las líneas antes de descontar ninguna.
- [ ] Descontar stock al despachar para reservar los productos.
- [ ] No retirar el pedido si no puede procesarse.
- [ ] Implementar `finalizarCarga()` únicamente con las responsabilidades correspondientes.
- [ ] Validar que la operación de la terminal sea del tipo correcto.
- [ ] Implementar getters pendientes.

### Cinco operaciones/consultas del Desafío 2

- [ ] `productosConStockBajo(int umbral)`
- [ ] `cantidadTotalUnidadesEnInventario()`
- [ ] `buscarEntregaPendientePorProveedor(String proveedorId)`
- [ ] `buscarPedidoPendientePorSucursal(String sucursalId)`
- [ ] `contarTerminalesPorEstado(EstadoTerminal estado)`

**Definition of Done:** puede ejecutarse de principio a fin una entrega de proveedor y un pedido de sucursal.

---

### Integrante 4 — Calidad de estructuras y testing

**Responsabilidad principal**
Biblioteca `structures/` + suite de pruebas.

**Checklist**
- [ ] Corregir validación de índice de `Conjunto.agregar(index, elem)`.
- [ ] Normalizar `this.frente`, `this.finalCola`, `this.size`, etc. en `ColaCircular`.
- [ ] Eliminar comentarios informales/de desarrollo.
- [ ] Crear `ListaArrayTest`.
- [ ] Crear `ListaSimpleTest`.
- [ ] Crear `ListaDobleTest`.
- [ ] Crear `ListaCircularTest`.
- [ ] Crear `ListaCircularDobleTest`.
- [ ] Crear `PilaTest`.
- [ ] Crear `ColaTest`.
- [ ] Crear `ColaPrioridadTest`.
- [ ] Probar estructura vacía.
- [ ] Probar un elemento.
- [ ] Probar múltiples elementos.
- [ ] Probar inserción inicial, media y final.
- [ ] Probar eliminación inicial, media y final.
- [ ] Probar búsquedas.
- [ ] Probar índices inválidos.
- [ ] Probar `vaciar()` y reutilización.
- [ ] Probar prioridad.
- [ ] Probar FIFO cuando dos elementos tienen igual prioridad.
- [ ] Crear pruebas de integración del almacén cuando Integrantes 1–3 terminen.
- [ ] Sustituir/eliminar `MyFirstTest`.

### Build / repositorio

- [ ] Confirmar versión Java exigida por el curso.
- [ ] Ajustar `pom.xml`.
- [ ] Eliminar dependencias sin utilizar.
- [ ] Crear `.gitignore`.
- [ ] Excluir `target/`.
- [ ] Actualizar README con instrucciones de compilación y ejecución.

**Definition of Done:** `mvn test` debe ser el filtro obligatorio antes de integrar cualquier cambio.

---

### Integrante 5 — Aplicación, optimización y cierre documental

**Archivos principales**
- `MenuConsola.java`
- `Main.java`
- Experimento del Desafío 3
- Integración del informe

### Aplicación

- [ ] Crear `AlmacenLogistico` desde `Main`.
- [ ] Registrar terminales iniciales.
- [ ] Ejecutar `MenuConsola`.
- [ ] Implementar ciclo principal del menú.
- [ ] Registrar productos.
- [ ] Registrar entregas.
- [ ] Registrar pedidos.
- [ ] Asignar entregas.
- [ ] Finalizar descargas.
- [ ] Despachar pedidos.
- [ ] Finalizar cargas.
- [ ] Ejecutar las cinco consultas.
- [ ] Manejar entradas inválidas sin finalizar el programa.

### Desafío 3

**Optimización propuesta: búsqueda de productos en inventario**

- [ ] Medir búsqueda secuencial actual: `O(n)`.
- [ ] Mantener los ítems ordenados por `Producto.codigo`.
- [ ] Implementar búsqueda binaria sobre `ListaArray`.
- [ ] Determinar complejidad final de búsqueda: `O(log n)`.
- [ ] Explicar el trade-off: insertar ordenadamente continúa teniendo costo `O(n)`.
- [ ] Ejecutar experimentos con diferentes tamaños de inventario.
- [ ] Repetir búsquedas suficientes veces para obtener mediciones comparables.
- [ ] Registrar resultados.
- [ ] Crear tabla con implementación original vs optimizada.
- [ ] Escribir conclusión del experimento.

**Definition of Done:** existe evidencia cuantitativa de la mejora, no solamente análisis teórico.

---

# Fase 2 — Integración

- [ ] Integrar primero Modelo.
- [ ] Integrar Inventario y Terminales.
- [ ] Integrar `AlmacenLogistico`.
- [ ] Ejecutar toda la suite de tests.
- [ ] Corregir errores antes de integrar UI.
- [ ] Integrar `MenuConsola` y `Main`.
- [ ] Ejecutar un escenario completo manual.
- [ ] Integrar optimización.
- [ ] Volver a ejecutar todos los tests.

## Escenario obligatorio de prueba

- [ ] Crear varios productos.
- [ ] Crear varias terminales.
- [ ] Deshabilitar al menos una terminal.
- [ ] Registrar dos proveedores.
- [ ] Registrar varias entregas.
- [ ] Comprobar FIFO de entregas.
- [ ] Finalizar una descarga.
- [ ] Comprobar aumento de inventario.
- [ ] Crear tres sucursales con distinta cantidad de clientes.
- [ ] Crear pedidos para las tres.
- [ ] Comprobar prioridad de despacho.
- [ ] Crear dos sucursales con igual prioridad y comprobar FIFO.
- [ ] Intentar despachar un pedido sin stock suficiente.
- [ ] Verificar que el stock no se modifique parcialmente.
- [ ] Ejecutar las cinco consultas definidas.

---

# Fase 3 — Informe

Cada integrante escribe su propia sección; el Integrante 5 solamente unifica formato y elimina contradicciones.

### Integrante 1
- [ ] Modelo del problema.
- [ ] Entidades y relaciones.
- [ ] Decisiones del modelo.

### Integrante 2
- [ ] Inventario.
- [ ] Terminales.
- [ ] Invariantes.

### Integrante 3
- [ ] Flujo de proveedores.
- [ ] Flujo de reabastecimiento.
- [ ] Cinco consultas.
- [ ] Justificación de estructuras elegidas.

### Integrante 4
- [ ] Biblioteca de estructuras.
- [ ] Estrategia de testing.
- [ ] Casos borde.
- [ ] Complejidades principales de las estructuras.

### Integrante 5
- [ ] Desafío 3.
- [ ] Metodología experimental.
- [ ] Resultados.
- [ ] Conclusiones.
- [ ] Registro de uso de IA.
- [ ] Edición final del informe.

---

# Fase 4 — QA final del equipo completo

- [ ] Buscar globalmente `UnsupportedOperationException`.
- [ ] Buscar globalmente `TODO`.
- [ ] Buscar globalmente `A implementar`.
- [ ] Ninguna de esas búsquedas debería encontrar código pendiente.
- [ ] Ejecutar `mvn clean test`.
- [ ] Ejecutar la aplicación desde cero.
- [ ] Revisar que no se utilicen `ArrayList`, `LinkedList`, `Stack`, `Queue`, `PriorityQueue` u otras estructuras equivalentes para resolver las operaciones principales.
- [ ] Revisar todas las complejidades escritas en el informe.
- [ ] Revisar consistencia entre informe y código.
- [ ] Limpiar `target/` y archivos innecesarios de la entrega.
- [ ] Verificar README.
- [ ] Verificar nombres de archivos.
- [ ] Verificar versión de Java.

# Fase 5 — Preparación para la defensa

Todos los integrantes deben poder explicar:

- [ ] Diferencia entre TDA e implementación.
- [ ] Invariantes de cada lista.
- [ ] Diferencia entre lista simple, doble y circular.
- [ ] Por qué la cola de proveedores es FIFO.
- [ ] Cómo funciona internamente `ColaPrioridad`.
- [ ] Qué ocurre cuando dos sucursales tienen la misma prioridad.
- [ ] Por qué no se puede descontar parcialmente un pedido.
- [ ] Complejidad de `buscarTerminalLibre()`.
- [ ] Complejidad de búsqueda de inventario.
- [ ] Trade-off introducido por la optimización.
- [ ] Casos borde utilizados en testing.
- [ ] Una modificación pequeña en cualquier estructura sin ayuda externa.

## Regla de Team

- [ ] Cada archivo tiene un único owner durante la implementación.
- [ ] Nadie modifica archivos de otro integrante sin coordinarlo.
- [ ] Todo merge debe pasar tests.
- [ ] Cada integrante documenta las decisiones de su propio código.
- [ ] Ninguna funcionalidad se considera terminada únicamente porque “compila”.
- [ ] El proyecto solamente está terminado cuando código, tests, experimento, informe y defensa cuentan la misma historia.