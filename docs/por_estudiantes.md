
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

