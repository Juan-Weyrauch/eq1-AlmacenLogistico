# ESTUDIANTE 3 — HEAP + PRIORIDAD DE PEDIDOS
## Rama sugerida

Después del PR base:

```text
feature/hito2-heap-prioridad
```

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
