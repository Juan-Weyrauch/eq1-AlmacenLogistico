
# ESTUDIANTE 1 — BASE DEL HITO 2
## Responsabilidad más esencial

Este integrante construye la base que fija los contratos del proyecto.

**Su trabajo debe integrarse primero.**

Nadie debería comenzar a modificar clases compartidas de Hito 2 antes de que el primer PR de este integrante esté aprobado.

## Rama sugerida

```text
feature/hito2-base
```

---

## Etapa 1A — Congelar la base compartida

### Tareas

- [ ] Incorporar las interfaces oficiales del Hito 2 proporcionadas por la cátedra.
- [ ] Confirmar firmas de:
  - árbol binario;
  - árbol de búsqueda;
  - árbol general;
  - recorridos;
  - cualquier interfaz de heap que haya sido proporcionada.
- [ ] No modificar arbitrariamente esas interfaces.
- [ ] Definir criterios comunes de nulls, duplicados y comparadores.
- [ ] Unificar `LineaEntrega` y `LineaPedido`.

Crear:

```text
LineaProducto.java
```

Modificar:

```text
EntregaProveedor.java
PedidoReabastecimiento.java
AlmacenLogistico.java
MenuConsola.java
tests afectados
```

Eliminar cuando compile todo:

```text
LineaEntrega.java
LineaPedido.java
```

### Definition of Done 1A

- [ ] El proyecto vuelve a compilar.
- [ ] Todos los tests antiguos siguen pasando.
- [ ] Ya existe una única `LineaProducto`.
- [ ] Las interfaces oficiales del Hito 2 están en el repositorio.
- [ ] Este PR se mergea antes de abrir el desarrollo paralelo.

---

## Etapa 1B — Árbol binario

Implementar la estructura base requerida por la interfaz oficial.

Responsabilidades típicas:

```text
raíz
insertar/establecer según TDA
buscar
altura
cantidad de nodos si TDA lo solicita
preOrder
inOrder
postOrder
porNiveles
```

El recorrido por niveles debe utilizar:

```text
Cola
```

del Hito 1.

No utilizar `java.util.Queue`.

---

## Etapa 1C — Árbol binario de búsqueda

Implementar sobre la base anterior si la jerarquía de interfaces lo permite.

Debe soportar:

```text
insertar
buscar
eliminar
mínimo/máximo si TDA los solicita
recorridos
```

Regla de duplicados:

```text
no insertar claves duplicadas
```

o exactamente la política definida por la interfaz/cátedra.

### Eliminación

Debe comprender y testear:

```text
caso 1: hoja
caso 2: un hijo
caso 3: dos hijos
```

Para dos hijos:

```text
sucesor inOrder
```

o predecesor, pero todo el grupo debe utilizar y defender la misma decisión.

---

## Tests de Estudiante 1

Crear tests específicos para:

```text
ArbolBinario
ArbolBinarioBusqueda
recorridos
eliminaciones
árbol degenerado
por niveles usando Cola
```

---

## Qué NO debe tocar Estudiante 1 después del PR base

Una vez mergeada la base:

```text
Inventario.java
ColaPrioridad.java
Deposito.java
AlmacenLogistico.java
MenuConsola.java
```

quedan bajo responsabilidad de otros integrantes.

Esto reduce conflictos.

---

## Sección del informe

Estudiante 1 escribe:

- diferencia entre TDA e implementación;
- árbol binario;
- ABB;
- invariantes;
- complejidades;
- eliminación;
- recorridos.

---

# ESTUDIANTE 2 — AVL + INVENTARIO + OPTIMIZACIÓN
## Rama sugerida

Crear después del merge del Estudiante 1:

```text
feature/hito2-avl-inventario
```

---

## Parte A — AVL

Implementar:

```text
ArbolAVL<T>
```

sobre ABB si las interfaces permiten reutilización mediante herencia.

La relación conceptual:

```text
AVL IS-A ABB
```

sí es válida.

Debe implementar:

```text
factor de balance
rotación izquierda
rotación derecha
LL
RR
LR
RL
rebalanceo después de insertar
rebalanceo después de eliminar
```

No copiar todo el ABB si puede reutilizarse limpiamente.

Pero tampoco forzar herencia si las interfaces oficiales no lo permiten.

---

## Parte B — Migrar Inventario

Cambiar:

```text
ListaArray<ItemInventario>
```

por:

```text
ArbolAVL<ItemInventario>
```

El criterio de orden es:

```text
item.getProducto().getCodigo()
```

Mantener la API externa de `Inventario` tanto como sea posible:

```text
registrarProducto()
buscarItem()
obtenerStock()
aumentarStock()
disminuirStock()
hayStock()
getItems()
```

De este modo `AlmacenLogistico` no necesita reescribirse completamente.

---

## Listado ordenado

Crear operación:

```text
listarInventarioOrdenado()
```

basada en:

```text
inOrder
```

Debe devolver una estructura propia del proyecto.

---

## Complejidades que debe documentar

```text
buscar       O(log n)
insertar     O(log n)
eliminar     O(log n)
inOrder      O(n)
espacio AVL  O(n)
```

Comparar contra Hito 1:

```text
buscar lineal O(n)
```

---

## Parte C — Desafío 3

Estudiante 2 es responsable del experimento de búsqueda.

### Baseline

Conservar como referencia:

```text
commit a9d8924
```

y la búsqueda lineal original de `Inventario`.

### Crear pruebas experimentales

Preferentemente bajo:

```text
src/test/java/.../experiment/
```

o carpeta claramente identificada.

No mezclar benchmark con lógica productiva.

Registrar resultados en CSV o Markdown.

### Casos

- [ ] búsqueda existente;
- [ ] peor caso lineal;
- [ ] inexistente;
- [ ] distintos tamaños;
- [ ] múltiples repeticiones.

---

## Tests de Estudiante 2

- [ ] LL.
- [ ] RR.
- [ ] LR.
- [ ] RL.
- [ ] múltiples inserciones.
- [ ] eliminación y rebalanceo.
- [ ] inOrder ordenado.
- [ ] inventario vacío.
- [ ] registro.
- [ ] duplicados.
- [ ] búsqueda.
- [ ] stock.
- [ ] listado ordenado.

---

## Archivos principales

```text
structures/ArbolAVL.java
model/Inventario.java
tests de AVL
tests de Inventario
experimento Desafío 3
```

Evitar modificar:

```text
AlmacenLogistico.java
MenuConsola.java
ColaPrioridad.java
Deposito.java
```

---

## Sección del informe

Estudiante 2 escribe:

- AVL;
- balance;
- rotaciones;
- inventario;
- búsqueda por código;
- listado ordenado;
- comparación Hito 1 vs Hito 2;
- metodología y resultados del Desafío 3.

---

# ESTUDIANTE 3 — HEAP + PRIORIDAD DE PEDIDOS
## Rama sugerida

Después del PR base:

```text
feature/hito2-heap-prioridad
```

---

## Parte A — Reemplazar `ColaPrioridad`

Eliminar la herencia:

```java
extends ListaSimple<T>
```

Implementar la cola de prioridad mediante:

```text
heap máximo
+
ListaArray propia
```

Mantener siempre que sea posible la API existente:

```text
frente()
poneEnCola()
quitaDeCola()
buscar()
tamaño()
esVacio()
vaciar()
```

Si la cátedra exige una clase `MonticuloBinario`, utilizar ese nombre/contrato.

---

## Parte B — Operaciones internas

Implementar:

```text
swap
padre
hijoIzquierdo
hijoDerecho
heapifyUp
heapifyDown
```

No utilizar:

```text
PriorityQueue
```

de Java.

---

## Parte C — Empates FIFO

Preservar comportamiento del Hito 1:

```text
misma prioridad
→ sale primero el pedido más antiguo
```

Puede utilizarse una secuencia interna de inserción.

La secuencia no debe quedar expuesta al dominio salvo que sea necesario.

---

## Parte D — Modificación de prioridad

Adaptar `PedidoReabastecimiento` para que la prioridad pueda modificarse mientras está pendiente.

Propuesta:

```text
prioridad mutable
```

inicializada desde el criterio del Hito 1.

Agregar al heap una operación segura del estilo:

```text
reordenar(elemento)
```

o la que resulte compatible con el TDA oficial.

Proceso:

```text
1. localizar pedido O(n)
2. cambiar prioridad
3. heapifyUp o heapifyDown O(log n)
```

No introducir un mapa/índice adicional.

---

## Tests de Estudiante 3

- [ ] heap vacío;
- [ ] un pedido;
- [ ] múltiples prioridades;
- [ ] extracción en orden correcto;
- [ ] empates FIFO;
- [ ] subir prioridad;
- [ ] bajar prioridad;
- [ ] prioridad del máximo;
- [ ] vaciar y reutilizar;
- [ ] búsqueda;
- [ ] casos nulos según TDA.

---

## Archivos principales

```text
structures/ColaPrioridad.java
tda/TDAColaPrioridad.java únicamente si la interfaz oficial exige adaptación
model/PedidoReabastecimiento.java
tests correspondientes
```

No modificar:

```text
AlmacenLogistico.java
MenuConsola.java
Inventario.java
Deposito.java
```

La integración de la nueva cola será responsabilidad del Estudiante 5.

---

## Sección del informe

Estudiante 3 escribe:

- heap binario;
- representación por arreglo;
- invariantes;
- complejidades;
- prioridad dinámica;
- política de empates;
- comparación conceptual con la lista ordenada del Hito 1.

---

# ESTUDIANTE 4 — ÁRBOL GENERAL + MODELO FÍSICO DEL DEPÓSITO
## Rama sugerida

Después del PR base:

```text
feature/hito2-deposito
```

---

## Parte A — `ArbolGeneral<T>`

Implementar árbol n-ario.

Representación recomendada:

```text
NodoGeneral
├── dato
└── ListaSimple<hijos>
```

El nodo debe quedar encapsulado siempre que las interfaces lo permitan.

Operaciones:

```text
raíz
agregar hijo
buscar
eliminar/subárbol si corresponde al TDA
preOrder
postOrder
porNiveles
buscar camino
mover subárbol
```

No fijar profundidad máxima.

---

## Parte B — `Sector`

Crear:

```text
Sector.java
TipoSector.java
```

Atributos:

```text
codigoLocal
nombre
tipo
capacidad
habilitado
```

Validaciones:

```text
codigo no vacío
capacidad >= 0
```

La decisión de permitir capacidad 0 debe documentarse.

---

## Parte C — `Producto.espacioUnitario`

Agregar:

```text
espacioUnitario
```

manteniendo el constructor antiguo con valor por defecto 1.

Validación:

```text
espacioUnitario > 0
```

---

## Parte D — `UbicacionStock`

Crear:

```text
UbicacionStock.java
```

con:

```text
Producto
Sector posicion
cantidad
```

Validar:

```text
posición != null
producto != null
cantidad > 0
```

---

## Parte E — `Deposito`

Crear:

```text
Deposito.java
```

que contiene:

```text
ArbolGeneral<Sector>
```

No hereda.

Debe implementar:

```text
agregarSector()
buscarSectorPorRuta()
obtenerRuta()
moverSector()
obtenerOcupacion()
obtenerEspacioDisponible()
listarPosicionesHabilitadas()
obtenerContenidoSector()
```

---

## Ocupación

Calcularla recorriendo el subárbol.

Para cada `UbicacionStock`:

```text
cantidad * producto.espacioUnitario
```

No cachear inicialmente.

---

## Capacidad

Antes de almacenar en una posición debe poder validarse toda la ruta:

```text
raíz
→ sector
→ ...
→ posición
```

Ningún sector del camino puede superar su capacidad.

---

## Movimiento

Testear especialmente:

```text
mover hoja
mover subárbol
impedir mover raíz
impedir mover dentro de sí mismo
preservar contenido
```

---

## Tests de Estudiante 4

- [ ] raíz sola;
- [ ] profundidad variable;
- [ ] múltiples hijos;
- [ ] ruta válida;
- [ ] ruta inválida;
- [ ] DFS;
- [ ] BFS con Cola;
- [ ] movimiento;
- [ ] ciclo imposible;
- [ ] posición habilitada/deshabilitada;
- [ ] capacidad;
- [ ] ocupación de subárbol.

---

## Archivos principales

```text
structures/ArbolGeneral.java
model/Sector.java
model/TipoSector.java
model/UbicacionStock.java
model/Deposito.java
model/Producto.java
tests correspondientes
```

No modificar:

```text
AlmacenLogistico.java
MenuConsola.java
Inventario.java
ColaPrioridad.java
```

---

## Sección del informe

Estudiante 4 escribe:

- por qué árbol general;
- profundidad variable;
- representación de sectores;
- rutas;
- capacidad;
- ocupación;
- movimiento de subárboles;
- complejidad de recorridos.

---

# ESTUDIANTE 5 — INTEGRACIÓN DEL ALMACÉN + FLUJOS + UI
## Rama sugerida

Crear después de que AVL, heap y depósito estén suficientemente estabilizados:

```text
feature/hito2-integracion
```

Este integrante es responsable de conectar las piezas.

No debe reimplementar árboles ni heaps.

---

## Parte A — Integrar `Deposito`

Modificar `AlmacenLogistico` para contener:

```text
Inventario
Deposito
Cola<EntregaProveedor>
ColaPrioridad<PedidoReabastecimiento>
Terminales
```

---

## Parte B — Adaptar `ItemInventario`

Agregar:

```text
ListaSimple<UbicacionStock>
```

y métodos controlados para:

```text
agregarUbicacion()
aumentarEnUbicacion()
retirarDeUbicacion()
eliminarUbicacionVacia()
obtenerUbicaciones()
```

Mantener:

```text
stockTotal
=
suma ubicaciones
```

Toda modificación debe mantener el invariante.

---

## Parte C — Recepción ubicada

Adaptar `finalizarDescarga()`.

Antes:

```text
aumentarStock()
```

Ahora:

```text
1. analizar todas las líneas
2. buscar posiciones habilitadas
3. validar capacidad de TODA la entrega
4. construir plan
5. aplicar ubicaciones
6. aumentar stock
7. liberar terminal
```

No permitir:

```text
media entrega guardada + error
```

---

## Parte D — Despacho ubicado

Adaptar `despacharProximoPedido()`.

Proceso:

```text
1. consultar pedido máximo del heap
2. verificar stock completo
3. obtener ubicaciones
4. generar ruta de picking
5. construir plan de retiro
6. aplicar retiros
7. quitar pedido
8. asignar terminal
```

No modificar stock parcialmente si falla alguna línea.

---

## Parte E — Picking

Crear un objeto de resultado solamente si realmente mejora claridad.

Puede ser:

```text
PasoPicking
├── rutaPosicion
├── Producto
└── cantidad
```

Si quieren evitar una clase adicional, puede devolverse una estructura de `UbicacionStock`/datos existente.

Elegir la opción más simple que permita mostrar:

```text
posición → producto → cantidad
```

en orden DFS.

---

## Parte F — Inhabilitación con reubicación

Implementar en `AlmacenLogistico`:

```text
inhabilitarSector(ruta)
```

Flujo:

```text
1. buscar sector
2. obtener mercadería afectada
3. excluir posiciones del mismo subárbol
4. buscar destinos habilitados
5. validar capacidad completa
6. construir plan
7. aplicar reubicaciones
8. marcar sector inhabilitado
```

Si no puede reubicarse todo:

```text
no modificar nada
```

---

## Parte G — Prioridad dinámica

Agregar en `AlmacenLogistico` una operación:

```text
modificarPrioridadPedido(...)
```

que:

```text
encuentra pedido
cambia prioridad
solicita reordenamiento al heap
```

---

## Parte H — Cinco consultas

Integrar oficialmente:

- [ ] buscar producto por código;
- [ ] listar inventario ordenado;
- [ ] ocupación/espacio de sector;
- [ ] mercadería de un sector;
- [ ] generar recorrido de picking.

Agregar opcional:

- [ ] ubicaciones de un producto;
- [ ] posiciones con capacidad disponible;
- [ ] sectores deshabilitados.

---

## Parte I — `MenuConsola`

Agregar opciones mínimas para demostrar:

```text
crear sector
mostrar depósito
registrar producto
registrar entrega
finalizar descarga con ubicación
crear pedido
modificar prioridad
despachar pedido
mostrar recorrido de picking
consultar ocupación
buscar producto
listar inventario ordenado
mover sector
inhabilitar sector
```

No convertir el menú en el lugar donde vive la lógica.

`MenuConsola` solamente:

```text
lee
valida formato básico
llama AlmacenLogistico
muestra resultado
```

---

## Parte J — Integración y regresión

Asegurar que siguen funcionando:

```text
terminales
entregas FIFO
pedidos prioritarios
stock
cargas
descargas
consultas del Hito 1 que todavía tengan sentido
```

---

## Tests de Estudiante 5

Crear tests de integración:

### Escenario 1 — Recepción

```text
producto
→ entrega
→ terminal
→ descarga
→ ubicación
→ stock
→ ocupación
```

### Escenario 2 — Pedido

```text
pedido
→ prioridad
→ stock suficiente
→ picking
→ descuento
→ terminal
```

### Escenario 3 — Prioridad dinámica

```text
pedido A > pedido B
cambiar prioridad B
B pasa a ser máximo
```

### Escenario 4 — Reorganización

```text
mover sector
→ contenido sigue presente
→ nueva ruta correcta
```

### Escenario 5 — Inhabilitación

```text
sector con stock
→ encontrar destinos
→ reubicar
→ sector deshabilitado
```

y caso:

```text
sin capacidad suficiente
→ operación rechazada
→ estado sin cambios
```

---

## Archivos principales

```text
model/ItemInventario.java
service/AlmacenLogistico.java
ui/MenuConsola.java
Main.java
tests de integración
```

No modificar implementaciones de AVL/heap/árbol general salvo bug confirmado y coordinado con su autor.

---

## Sección del informe

Estudiante 5 escribe:

- integración con Hito 1;
- recepción ubicada;
- despacho ubicado;
- picking;
- reorganización;
- inhabilitación;
- consultas;
- pruebas de integración.
