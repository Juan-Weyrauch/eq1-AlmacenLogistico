# Plan técnico y reparto de tareas — Hito 2
## Proyecto Integrador: Almacén Logístico
### Equipo de 5 integrantes

**Baseline revisada:** entrega final del Hito 1, rama `main`, commit `a9d8924`.  
**Entrega indicada por la cátedra:** 09/09/2026.  
**Objetivo de este documento:** definir una arquitectura única, evitar una reescritura innecesaria y repartir el trabajo para que los cinco integrantes puedan desarrollar, probar y defender el proyecto con la menor cantidad posible de conflictos de integración.

---

# 1. Decisión estratégica general

La estrategia recomendada es:

```text
NO rehacer el Hito 1 desde cero
        │
        ▼
hacer un refactor mínimo y controlado
        │
        ▼
implementar las nuevas estructuras jerárquicas
        │
        ▼
integrarlas al modelo existente
        │
        ▼
optimizar la búsqueda de inventario con AVL
        │
        ▼
completar testing + experimento + informe + defensa
```

El Hito 2 exige explícitamente que la solución **extienda lo construido en el Hito 1 sin romper su comportamiento**.

Por eso, el objetivo no debe ser conseguir “el modelo OO perfecto”, sino una solución:

- correcta;
- entendible;
- justificable;
- testeable;
- integrada con el Hito 1;
- viable dentro del plazo.

---

# 2. Requisitos obligatorios del Hito 2

El enunciado se divide en tres desafíos.

## Desafío 1 — Construir estructuras jerárquicas

Se deben implementar, respetando las interfaces proporcionadas por la cátedra:

- Árbol binario.
- Árbol binario de búsqueda.
- Árbol binario de búsqueda balanceado.
- Árbol general n-ario.
- Montículo binario / cola de prioridad.
- Recorridos:
  - preorden;
  - inorden;
  - postorden;
  - por niveles.

El recorrido por niveles debe utilizar estructuras lineales desarrolladas en el Hito 1.

Cada estructura debe probar:

- estructura vacía;
- un único nodo;
- árbol degenerado;
- árbol balanceado;
- inserciones;
- eliminaciones;
- búsqueda;
- recorridos;
- casos borde.

Para árboles de búsqueda deben probarse específicamente todos los casos de eliminación:

```text
eliminar hoja
eliminar nodo con un hijo
eliminar nodo con dos hijos
eliminar raíz
elemento inexistente
```

> IMPORTANTE: en el ZIP actual del Hito 1 todavía no aparecen las nuevas interfaces de árboles del Hito 2. Antes de implementar, deben incorporar **exactamente las interfaces entregadas por la cátedra** y adaptar los nombres/firma de métodos de este plan a esas interfaces. No conviene inventar un TDA paralelo si ya fue proporcionado.

---

# 3. Modelo general propuesto

La arquitectura objetivo será:

```text
                              AlmacenLogistico
                                     │
          ┌──────────────────────────┼─────────────────────────┐
          │                          │                         │
          ▼                          ▼                         ▼
      Inventario                  Deposito                Operaciones
          │                          │                         │
          ▼                          ▼              ┌──────────┴─────────┐
 AVL<ItemInventario>        ArbolGeneral<Sector>    │                    │
          │                          │              ▼                    ▼
          ▼                          ▼       EntregaProveedor    PedidoReabastecimiento
   ItemInventario                 Sector                                  │
          │                          │                                     ▼
          │                    UbicacionStock                           Sucursal
          │                          ▲
          └──────────────────────────┘


Pedidos pendientes
       │
       ▼
ColaPrioridad implementada con HEAP binario


Entregas pendientes
       │
       ▼
Cola FIFO del Hito 1
```

---

# 4. Decisiones de diseño que quedan congeladas

Estas decisiones deberían acordarse en equipo **antes de que cada estudiante abra su rama**.

## 4.1 El almacén TIENE un árbol general

Correcto:

```java
class Deposito {
    private ArbolGeneral<Sector> estructura;
}
```

Incorrecto:

```java
class Deposito extends ArbolGeneral<Sector>
```

Un depósito **no es** un árbol general. Utiliza un árbol general para representar su organización física.

---

## 4.2 No crear una clase para cada nivel físico

NO implementar:

```text
Zona.java
Pasillo.java
Estanteria.java
Bandeja.java
Posicion.java
```

El enunciado aclara que las ramas tienen profundidad variable y llama **sector** a cualquier subdivisión, independientemente de su nivel.

Por lo tanto se utilizará:

```text
Sector
+
ArbolGeneral<Sector>
```

Ejemplo:

```text
Depósito
├── Cámara de frío
│   └── Pasillo 4
│       └── Estantería 2
│           └── Bandeja 5
│               ├── Posición A
│               └── Posición B
│
└── Recepción
    ├── Posición R1
    └── Posición R2
```

La profundidad puede cambiar entre ramas sin modificar clases.

---

## 4.3 No utilizar un AVL diferente para sector, pasillo, estantería y bandeja

Evitar:

```text
AVL<Sector>
   ↓
AVL<Pasillo>
   ↓
AVL<Estanteria>
   ↓
AVL<Bandeja>
   ↓
AVL<Producto>
```

Eso:

- fija la jerarquía;
- dificulta mover subárboles;
- complica la defensa;
- multiplica estructuras y clases;
- contradice la profundidad variable requerida.

Se utilizará un único:

```text
ArbolGeneral<Sector>
```

para la estructura física.

El AVL se reserva para donde realmente aporta valor:

```text
Inventario
    ↓
AVL<ItemInventario>
```

---

# 5. Identificación de ubicaciones

Se mantiene la buena idea del equipo de representar una ubicación mediante una ruta jerárquica.

Ejemplo:

```text
3.4.2.5
```

podría representar:

```text
Sector 3
  ↓
Pasillo 4
  ↓
Estantería 2
  ↓
Posición/Bandeja 5
```

La ruta también puede imprimirse como:

```text
3/4/2/5
```

o:

```text
3-4-2-5
```

El formato debe ser único en todo el proyecto.

## Regla fundamental

**La ubicación NO forma parte del ID del producto.**

Incorrecto:

```text
Producto 3456
=
Sector 3
Pasillo 4
Bandeja 5
Producto 6
```

porque si el producto se mueve, cambiaría su identidad.

Correcto:

```text
Producto:
P006

Ubicación:
3.4.2.5
```

Si se mueve:

```text
Producto:
P006

Nueva ubicación:
8.2.1
```

`P006` nunca cambia.

---

# 6. Clase `Sector`

La clase representa cualquier subdivisión física.

Propuesta mínima:

```java
Sector
├── codigoLocal
├── nombre
├── tipo
├── capacidad
└── habilitado
```

Puede agregarse un enum simple:

```java
TipoSector {
    ZONA,
    PASILLO,
    ESTANTERIA,
    BANDEJA,
    POSICION,
    OTRO
}
```

Este enum es **descriptivo**, no impone una jerarquía.

Debe ser válido tener:

```text
ZONA → POSICION
```

sin pasar por pasillo/estantería/bandeja.

También:

```text
ZONA → PASILLO → ESTANTERIA → BANDEJA → POSICION
```

---

# 7. Capacidad del depósito

El enunciado exige definir y justificar una unidad de capacidad.

## Propuesta

Utilizar una unidad abstracta:

```text
UC = Unidad de Capacidad
```

Cada `Producto` tendrá:

```java
espacioUnitario
```

Por ejemplo:

```text
Producto P001
espacioUnitario = 2 UC
```

Si una posición almacena:

```text
10 unidades de P001
```

su ocupación es:

```text
10 × 2 UC = 20 UC
```

Esto permite que distintos productos consuman diferente cantidad de espacio.

## Compatibilidad con Hito 1

Para no romper constructores existentes, puede mantenerse:

```java
Producto(String codigo, String nombre, String descripcion)
```

y hacer que utilice:

```text
espacioUnitario = 1
```

Además se agrega un constructor nuevo:

```java
Producto(
    String codigo,
    String nombre,
    String descripcion,
    int espacioUnitario
)
```

---

# 8. Ocupación: no almacenar un valor duplicado al principio

Para minimizar errores, la primera versión **no debería mantener una ocupación cacheada** en cada sector.

La ocupación se calcula recorriendo el subárbol:

```text
ocupacion(sector)
=
suma de UbicacionStock de todas las posiciones descendientes
```

Ventajas:

- no existe riesgo de que `ocupacion` quede desincronizada;
- mover sectores no requiere actualizar múltiples contadores;
- la implementación es más fácil de comprender y defender;
- el costo depende del tamaño del subárbol, no de todo el inventario.

Complejidad:

```text
O(k)
```

donde `k` es el número de nodos/ubicaciones dentro del sector consultado.

Una caché de ocupación puede considerarse **optimización opcional posterior**, únicamente si toda la solución ya funciona.

---

# 9. `UbicacionStock`

El Hito 2 necesita representar una relación nueva:

```text
PRODUCTO
   │
   │ cantidad
   ▼
POSICIÓN
```

Se propone:

```java
UbicacionStock
├── Producto producto
├── Sector posicion
└── int cantidad
```

Ejemplo:

```text
P006
60 unidades
en Sector/Posición 3.4.2.5
```

La misma instancia de `UbicacionStock` puede ser referenciada desde:

```text
ItemInventario
```

y desde:

```text
Sector POSICION
```

Esto evita duplicar cantidades.

Invariante principal:

```text
cantidad > 0
```

---

# 10. `ItemInventario`

Debe seguir existiendo.

En el Hito 2 evolucionará conceptualmente hacia:

```text
ItemInventario
├── Producto producto
├── int stockTotal
└── ListaSimple<UbicacionStock> ubicaciones
```

Ejemplo:

```text
ItemInventario P006
├── stockTotal = 100
└── ubicaciones
    ├── 3.4.2.5 → 60
    └── 3.4.2.6 → 40
```

Invariante:

```text
stockTotal
=
suma de cantidades de todas sus UbicacionStock
```

Todos los cambios deben pasar por métodos de `ItemInventario`/`Inventario`.

Evitar que `AlmacenLogistico` modifique directamente el campo `stock`.

---

# 11. Inventario mediante AVL

Actualmente el Hito 1 usa:

```text
ListaArray<ItemInventario>
```

y `buscarItem()` hace una búsqueda lineal:

```text
O(n)
```

En Hito 2 se reemplaza la representación interna:

```text
Inventario
    ↓
ArbolAVL<ItemInventario>
```

ordenado por:

```text
Producto.codigo
```

## Operaciones

```text
buscar producto          O(log n)
registrar producto       O(log n)
eliminar producto        O(log n)
listar ordenado          O(n) mediante inOrder
```

El AVL debe permanecer balanceado después de inserciones y eliminaciones.

Invariante AVL:

```text
|altura(izquierda) - altura(derecha)| <= 1
```

Rotaciones que deben existir:

```text
LL
RR
LR
RL
```

---

# 12. No implementar ahora la búsqueda binaria pendiente del Hito 1

En el Hito 1 había quedado pendiente optimizar:

```text
ListaArray + búsqueda lineal
```

mediante búsqueda binaria.

No vale la pena implementar esa optimización ahora como solución productiva.

El Hito 2 exige utilizar las nuevas estructuras y el AVL resuelve el mismo problema de forma más adecuada:

```text
Hito 1
ListaArray
O(n)

vs.

Hito 2
AVL
O(log n)
```

Esta comparación será además el experimento principal del Desafío 3.

---

# 13. Árbol general del depósito

Representación:

```text
Deposito
    ↓
ArbolGeneral<Sector>
```

La implementación genérica del árbol debería utilizar las estructuras propias del Hito 1.

Ejemplo conceptual:

```java
NodoGeneral<T>
├── T dato
└── ListaSimple<NodoGeneral<T>> hijos
```

Si las interfaces oficiales definen otra representación, respetar las interfaces.

## Operaciones mínimas requeridas por el dominio

El `Deposito` debe ofrecer operaciones del dominio, sin exponer nodos directamente:

```text
agregarSector(rutaPadre, sector)
buscarSector(ruta)
moverSector(rutaOrigen, rutaDestino)
obtenerOcupacion(ruta)
obtenerEspacioDisponible(ruta)
listarPosicionesHabilitadas()
obtenerRuta(sector)
recorrerPreOrden(...)
recorrerPorNiveles(...)
```

---

# 14. Movimiento de sectores

El Hito 2 exige mover un sector completo con todo su contenido.

Eso debe modelarse como:

```text
mover subárbol
```

Ejemplo:

```text
ANTES

Depósito
├── Sector 3
│   └── Estantería X
└── Sector 8


DESPUÉS

Depósito
├── Sector 3
└── Sector 8
    └── Estantería X
```

No se copian productos uno por uno.

Se mueve el subárbol.

## Validaciones

Antes de mover:

- origen existe;
- destino existe;
- origen no es la raíz;
- destino no pertenece al propio subárbol que se intenta mover;
- el sector destino está habilitado;
- el movimiento no viola capacidades.

---

# 15. Inhabilitación de sectores

El requisito no consiste solamente en:

```text
sector.habilitado = false
```

Al inhabilitar un sector debe reubicarse la mercadería.

## Estrategia recomendada

Implementar primero un **plan de reubicación** sin modificar estado.

```text
1. obtener todas las UbicacionStock del subárbol
2. buscar posiciones habilitadas fuera del sector
3. verificar que existe capacidad para TODO
4. construir plan
5. si el plan es completo → aplicar cambios
6. marcar sector como inhabilitado
```

Si no existe espacio suficiente:

```text
NO modificar nada
```

Esto evita estados parciales.

La coordinación final debe hacerla `AlmacenLogistico`, utilizando:

```text
Deposito
+
Inventario
```

---

# 16. Recepción de proveedores ubicada

Se conserva la cola FIFO del Hito 1.

Flujo:

```text
EntregaProveedor
        ↓
cola FIFO
        ↓
terminal libre
        ↓
descarga
        ↓
seleccionar posiciones habilitadas
        ↓
crear/aumentar UbicacionStock
        ↓
aumentar stock
```

La descarga no debe registrar stock parcialmente si no existe capacidad para guardar la entrega completa.

Primero:

```text
planificar ubicaciones
```

Después:

```text
aplicar
```

---

# 17. Despacho de pedidos ubicado

El pedido continúa saliendo de la cola de prioridad.

Nuevo flujo:

```text
PedidoReabastecimiento
        ↓
heap
        ↓
validar stock total
        ↓
obtener ubicaciones de productos
        ↓
generar recorrido de picking
        ↓
retirar stock de posiciones
        ↓
asignar terminal
```

El inventario debe saber de qué ubicaciones puede retirarse cada producto.

---

# 18. Recorrido de picking

Se propone utilizar:

```text
DFS / preorden
```

sobre:

```text
ArbolGeneral<Sector>
```

Criterio:

> terminar de visitar un sector y sus descendientes antes de pasar a otro sector.

Esto evita alternar innecesariamente entre ramas.

Ejemplo:

```text
Depósito
├── Zona A
│   ├── A1
│   └── A2
└── Zona B
    ├── B1
    └── B2
```

Orden:

```text
A
A1
A2
B
B1
B2
```

El sistema genera pasos solamente para posiciones que contienen productos del pedido.

No se afirma que sea la distancia física mínima absoluta, porque el enunciado no proporciona coordenadas/distancias.

La justificación es:

```text
la jerarquía física se utiliza como aproximación de proximidad
y el recorrido evita regresar a una rama ya completada.
```

---

# 19. Recorrido por niveles

Debe utilizar la `Cola` propia del Hito 1.

Uso principal sugerido:

```text
mostrar estructura del depósito por niveles
```

También sirve para pruebas y consultas jerárquicas.

Complejidad:

```text
O(n)
```

para recorrer todos los nodos.

---

# 20. Cola de prioridad mediante heap binario

La implementación actual:

```java
ColaPrioridad<T> extends ListaSimple<T>
```

debe reemplazarse.

No es una buena relación de herencia porque una cola de prioridad no es una lista.

## Nueva representación

Internamente:

```text
ColaPrioridad
      ↓
montículo binario máximo
      ↓
ListaArray propia
```

Índices:

```text
padre(i)        = (i - 1) / 2
hijoIzquierdo   = 2*i + 1
hijoDerecho     = 2*i + 2
```

Complejidades esperadas:

```text
frente / máximo       O(1)
insertar              O(log n)
extraer máximo        O(log n)
```

---

# 21. Modificar prioridad de un pedido ya registrado

`PedidoReabastecimiento` debe permitir modificar su prioridad.

Propuesta:

```text
prioridad
```

como dato mutable del pedido, inicializado originalmente con el criterio utilizado en el Hito 1.

Al modificar:

```text
si aumenta prioridad
    → heapifyUp

si disminuye prioridad
    → heapifyDown
```

Para encontrar el pedido dentro del arreglo del heap puede realizarse una búsqueda lineal:

```text
O(n)
```

y luego reordenar:

```text
O(log n)
```

Total:

```text
O(n)
```

Esto es aceptable y mucho más simple que agregar otro índice auxiliar.

Debe documentarse el trade-off.

---

# 22. Empates de prioridad

El Hito 1 preservaba FIFO para elementos con la misma prioridad.

Conviene mantenerlo para no romper comportamiento.

El heap puede guardar internamente:

```text
dato
prioridad
secuenciaDeLlegada
```

Comparación:

```text
1. mayor prioridad
2. si empatan → menor secuencia / más antiguo
```

La estructura auxiliar puede ser privada dentro de `ColaPrioridad`.

No es necesario crear una nueva entidad de dominio.

---

# 23. Refactor mínimo del modelo antes del Hito 2

## Hacer

Unificar:

```text
LineaEntrega
LineaPedido
```

en:

```text
LineaProducto
```

porque ambas contienen esencialmente:

```text
Producto
cantidad
```

Este cambio debe hacerse **antes de abrir las ramas paralelas**, porque toca varias clases compartidas.

---

## No hacer ahora

No eliminar en esta etapa:

```text
Proveedor
TipoOperacion
```

Aunque son candidatos a simplificación, el beneficio es menor que el riesgo de romper integración.

Pueden quedar como deuda técnica documentada.

---

# 24. Modelo final aproximado

```text
model/
├── Producto.java
├── ItemInventario.java
├── Inventario.java
├── UbicacionStock.java
├── Sector.java
├── TipoSector.java
├── Deposito.java
│
├── LineaProducto.java
├── EntregaProveedor.java
├── PedidoReabastecimiento.java
├── Proveedor.java
├── Sucursal.java
│
├── TerminalCarga.java
├── EstadoTerminal.java
├── OperacionLogistica.java
└── TipoOperacion.java
```

No agregar:

```text
Zona.java
Pasillo.java
Estanteria.java
Bandeja.java
Posicion.java
```

---

# 25. Estructuras finales aproximadas

Los nombres exactos deben adaptarse a las interfaces oficiales.

```text
tda/
├── TDAArbolBinario.java
├── TDAArbolBinarioBusqueda.java
├── TDAArbolGeneral.java
├── TDAColaPrioridad.java
└── ... interfaces oficiales

structures/
├── ArbolBinario.java
├── ArbolBinarioBusqueda.java
├── ArbolAVL.java
├── ArbolGeneral.java
├── ColaPrioridad.java   ← implementación mediante heap
│
├── ListaArray.java
├── ListaSimple.java
├── Cola.java
├── ...
```

No utilizar para las operaciones principales:

```text
TreeMap
TreeSet
PriorityQueue
ArrayList/LinkedList como reemplazo de las estructuras propias
```

---

# 26. Cinco consultas/operaciones del Desafío 2

Se recomienda fijar estas cinco como las consultas oficiales del informe.

## Consulta 1 — Buscar producto por código

Estructura:

```text
AVL
```

Complejidad:

```text
O(log n)
```

Hito 1:

```text
O(n)
```

---

## Consulta 2 — Listar inventario ordenado por código

Estructura:

```text
AVL + inOrder
```

Complejidad:

```text
O(n)
```

No se requiere ordenar una lista posteriormente.

---

## Consulta 3 — Obtener ocupación y espacio disponible de un sector

Estructura:

```text
ArbolGeneral
```

Se recorre solamente el subárbol indicado.

Complejidad:

```text
O(k)
```

`k` = tamaño del subárbol/mercadería contenida.

---

## Consulta 4 — Listar mercadería contenida en un sector

Estructura:

```text
ArbolGeneral + DFS
```

Se visitan solamente las posiciones descendientes del sector.

No se recorre todo el inventario.

---

## Consulta 5 — Generar recorrido de picking de un pedido

Estructura:

```text
ArbolGeneral + DFS/preorden
```

Resultado:

```text
posición
producto
cantidad a retirar
```

en el orden de visita.

---

## Consulta opcional 6 — Ubicaciones de un producto

```text
AVL
↓
ItemInventario
↓
ListaSimple<UbicacionStock>
```

Costo:

```text
O(log n + u)
```

`u` = cantidad de ubicaciones de ese producto.

---

# 27. Desafío 3 — Optimización elegida

La operación elegida debe ser:

```text
buscar producto por código
```

## Referencia real del Hito 1

En el commit:

```text
a9d8924
```

`Inventario` utiliza:

```text
ListaArray<ItemInventario>
```

y:

```java
buscarItem(...)
```

realiza una búsqueda lineal.

Complejidad:

```text
O(n)
```

## Hito 2

```text
AVL<ItemInventario>
```

Complejidad:

```text
O(log n)
```

---

# 28. Experimento recomendado

Comparar exactamente la implementación lineal entregada en Hito 1 contra la nueva implementación AVL.

Tamaños sugeridos:

```text
100
1.000
10.000
100.000
```

Si el entorno no soporta cómodamente el último tamaño, utilizar los tamaños que permitan mediciones consistentes y documentarlo.

Casos:

```text
elemento cercano al inicio
elemento intermedio
elemento al final / peor caso lineal
elemento inexistente
```

Medición:

```java
System.nanoTime()
```

No medir una única búsqueda.

Realizar múltiples búsquedas por ejecución y varias repeticiones.

Registrar:

- tamaño;
- caso;
- cantidad de repeticiones;
- tiempo total;
- tiempo promedio;
- implementación.

La versión lineal debe ser una copia fiel o ejecución de la implementación realmente entregada, no una versión artificialmente empeorada.

---

# 29. Testing obligatorio global

Cada estudiante es responsable de los tests de su propia implementación.

No debe existir un “estudiante de testing” que pruebe todo lo que los demás no probaron.

La integración final agrega pruebas cruzadas.

## Árbol binario / ABB

- vacío;
- un nodo;
- inserciones;
- búsqueda;
- recorridos;
- degenerado;
- eliminación hoja;
- eliminación un hijo;
- eliminación dos hijos;
- eliminación raíz;
- inexistente.

## AVL

Además:

- LL;
- RR;
- LR;
- RL;
- altura después de múltiples inserciones;
- balance después de eliminar;
- búsqueda;
- inOrder ordenado.

## Árbol general

- raíz sola;
- múltiples hijos;
- profundidad irregular;
- búsqueda por ruta;
- mover hoja;
- mover subárbol;
- impedir ciclos;
- preorden;
- postorden;
- por niveles.

## Heap

- vacío;
- un elemento;
- inserción;
- extracción;
- máximo;
- prioridades iguales;
- cambio de prioridad hacia arriba;
- cambio de prioridad hacia abajo;
- vaciar y reutilizar.

## Dominio

- capacidad suficiente;
- capacidad insuficiente;
- producto en una posición;
- producto distribuido;
- reubicación;
- sector deshabilitado;
- movimiento de sector;
- recepción completa;
- despacho completo;
- no permitir modificaciones parciales.

---

# 30. Reparto de tareas — cinco estudiantes

---

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

---

# 31. Dependencias entre estudiantes

```text
                        ESTUDIANTE 1
                    BASE + ABB + contratos
                              │
                 ┌────────────┼────────────┐
                 │            │            │
                 ▼            ▼            ▼
           ESTUDIANTE 2  ESTUDIANTE 3  ESTUDIANTE 4
              AVL           HEAP       ÁRBOL GENERAL
           INVENTARIO     PRIORIDAD       DEPÓSITO
                 │            │            │
                 └────────────┼────────────┘
                              ▼
                        ESTUDIANTE 5
                          INTEGRACIÓN
```

---

# 32. Orden recomendado de integración

## Merge 1

```text
Estudiante 1 — Etapa 1A
```

Interfaces + `LineaProducto` + baseline compilando.

---

## Merge 2

```text
Estudiante 1 — árboles binarios
```

---

## Merges paralelos posteriores

Pueden entrar en cualquier orden mientras no rompan main:

```text
Estudiante 2 — AVL + Inventario
Estudiante 3 — Heap
Estudiante 4 — Árbol general + Depósito
```

Cada PR debe pasar todos los tests existentes.

---

## Merge final funcional

```text
Estudiante 5 — Integración
```

Después:

```text
QA grupal
```

---

# 33. Regla de Git para evitar repetir los problemas del Hito 1

Cada rama tiene archivos dueños.

No modificar un archivo perteneciente a otro integrante “porque es un cambio chico”.

Si hace falta:

```text
1. avisar al responsable;
2. hacer el cambio en la rama del responsable;
3. o esperar su merge;
4. luego actualizar la propia rama.
```

Evitar que tres ramas modifiquen simultáneamente:

```text
AlmacenLogistico.java
Inventario.java
MenuConsola.java
```

---

# 34. Definition of Done de cada PR

Un PR no se considera terminado porque “compila”.

Debe cumplir:

- [ ] Implementación completa.
- [ ] Tests propios.
- [ ] Casos borde.
- [ ] Complejidad documentada.
- [ ] Sin `TODO`.
- [ ] Sin métodos “A implementar”.
- [ ] Sin estructuras Java prohibidas para operaciones principales.
- [ ] Sin romper tests anteriores.
- [ ] Autor puede explicar cada método.
- [ ] Al menos otro integrante revisó el código.

---

# 35. Checklist final del proyecto

## Estructuras

- [ ] Árbol binario.
- [ ] ABB.
- [ ] AVL.
- [ ] Árbol general.
- [ ] Heap.
- [ ] Preorden.
- [ ] Inorden.
- [ ] Postorden.
- [ ] Por niveles con Cola propia.

## Depósito

- [ ] Estructura variable.
- [ ] Agregar sectores.
- [ ] Buscar rutas.
- [ ] Mover sectores.
- [ ] Capacidad.
- [ ] Ocupación.
- [ ] Habilitación.
- [ ] Inhabilitación con reubicación.

## Mercadería

- [ ] Producto independiente de ubicación.
- [ ] UbicacionStock.
- [ ] Producto en varias posiciones.
- [ ] Alta.
- [ ] Baja.
- [ ] Reubicación.

## Inventario

- [ ] AVL.
- [ ] Buscar por código.
- [ ] Listar ordenado.
- [ ] Mantener stock.
- [ ] Mantener ubicaciones.

## Recepción

- [ ] Cola FIFO.
- [ ] Terminal.
- [ ] Selección de posiciones.
- [ ] Validación de capacidad.
- [ ] Sin actualizaciones parciales.

## Despacho

- [ ] Heap.
- [ ] Prioridad.
- [ ] Cambio de prioridad.
- [ ] Validación de stock.
- [ ] Picking.
- [ ] Retiro por posiciones.
- [ ] Sin actualizaciones parciales.

## Consultas

- [ ] Cinco consultas justificadas.
- [ ] Tres mejoran el comportamiento respecto al Hito 1.
- [ ] Complejidades documentadas.

## Desafío 3

- [ ] Baseline Hito 1 real.
- [ ] AVL Hito 2.
- [ ] Datos crecientes.
- [ ] Peor caso.
- [ ] Repeticiones.
- [ ] Resultados.
- [ ] Conclusión.

## Testing

- [ ] Vacíos.
- [ ] Un elemento.
- [ ] Degenerado.
- [ ] Balanceado.
- [ ] Eliminaciones ABB.
- [ ] Rotaciones AVL.
- [ ] Heap.
- [ ] Árbol general.
- [ ] Capacidad.
- [ ] Movimiento.
- [ ] Recepción.
- [ ] Despacho.
- [ ] Reubicación.
- [ ] Regresión Hito 1.

## Código

- [ ] Buscar `TODO`.
- [ ] Buscar `UnsupportedOperationException` no intencional.
- [ ] Buscar código muerto.
- [ ] Revisar casts.
- [ ] Revisar nulls.
- [ ] Revisar duplicación.
- [ ] Ejecutar suite completa.

## Informe

- [ ] Descripción de solución.
- [ ] Qué se conservó del Hito 1.
- [ ] Qué se reemplazó.
- [ ] Invariantes.
- [ ] Complejidades.
- [ ] Decisiones del modelo.
- [ ] Desafío 3.
- [ ] Experimento.
- [ ] Resultados.
- [ ] Uso de IA.

## Defensa

Cada integrante debe poder explicar:

```text
por qué se eligió cada estructura;
cuál es su invariante;
qué complejidad tiene;
qué ocurre en peor caso;
qué métodos modifican la estructura;
qué cambiaría si se modifica un requisito.
```

---

# 36. Argumentos de defensa principales

## ¿Por qué árbol general?

Porque la estructura física:

```text
tiene cantidad variable de hijos
+
profundidad variable
```

y mover un sector equivale naturalmente a mover un subárbol.

---

## ¿Por qué AVL para inventario?

Porque se necesita:

```text
búsqueda eficiente por código
+
orden natural por código
+
garantía O(log n) en peor caso
```

Un ABB común puede degenerarse.

---

## ¿Por qué heap para pedidos?

Porque la operación principal es:

```text
obtener/eliminar el pedido de mayor prioridad
```

y el heap ofrece:

```text
máximo O(1)
inserción O(log n)
extracción O(log n)
```

---

## ¿Por qué no poner ubicación dentro del código de producto?

Porque:

```text
identidad != ubicación
```

La mercadería puede reubicarse sin dejar de ser el mismo producto.

---

## ¿Por qué no Zona/Pasillo/Estantería/Bandeja como clases distintas?

Porque el enunciado exige profundidad variable y define genéricamente cualquier subdivisión como `Sector`.

Una sola entidad evita fijar una jerarquía que el problema dice que puede variar.

---

## ¿Por qué no reescribir todo el Hito 1?

Porque:

```text
Hito 2 = extensión
```

y ya existen funcionalidades correctas que deben conservarse:

```text
inventario
terminales
FIFO proveedores
reabastecimiento
```

Un refactor incremental permite mejorar las estructuras sin reintroducir innecesariamente todos los bugs ya resueltos.

---

# 37. Prioridades si el equipo se queda sin tiempo

## PRIORIDAD 1 — Obligatorio

```text
estructuras del Desafío 1
AVL inventario
árbol general depósito
heap pedidos
ubicaciones
capacidad
búsqueda producto
recepción/despacho
tests básicos
```

## PRIORIDAD 2 — Obligatorio para cerrar correctamente

```text
mover sector
inhabilitar + reubicar
cinco consultas
cambio de prioridad
experimento
```

## PRIORIDAD 3 — Cierre

```text
UI completa
más consultas
mejoras estéticas
refactors opcionales
```

## NO HACER si pone en riesgo la entrega

```text
reescribir todo el modelo
eliminar Proveedor
eliminar TipoOperacion
cambiar nombres masivamente
agregar índices auxiliares complejos
cachear ocupaciones antes de que funcione
crear AVL anidados por cada nivel físico
```

---

# 38. Resultado esperado

Al finalizar, el sistema debería poder demostrar un escenario como este:

```text
1. Crear estructura física de profundidad variable.
2. Registrar productos.
3. Registrar una entrega.
4. Tomar la entrega FIFO.
5. Asignarla a una terminal.
6. Descargarla en posiciones con capacidad.
7. Buscar el producto por código usando AVL.
8. Mostrar todas sus ubicaciones.
9. Crear pedidos de varias sucursales.
10. Modificar la prioridad de uno.
11. Obtener el pedido máximo desde el heap.
12. Generar recorrido de picking.
13. Retirar stock por posiciones.
14. Mover un sector completo.
15. Verificar que su contenido se movió con él.
16. Inhabilitar un sector y reubicar su mercadería.
17. Consultar ocupación de un sector.
18. Listar inventario ordenado.
19. Ejecutar el experimento lineal vs AVL.
20. Ejecutar toda la suite de tests.
```

Si ese escenario funciona, está testeado y los cinco integrantes pueden explicarlo, el proyecto cubre de forma coherente el objetivo del Hito 2 sin necesidad de rehacer desde cero la solución del Hito 1.
