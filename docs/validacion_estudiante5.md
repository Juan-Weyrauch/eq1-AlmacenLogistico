# Integracion del Estudiante 5

Rama: `estudiante5-correcion`.

Base integrada: `origin/main` en `8cb229b` (Estudiantes 3 y 4 incluidos)
y `feature/estudiante5` en `91cb030`.

El cambio local previo de `Deposito` a `Almacen` se preservo en el stash
`bed81ce811b6c4fa76ecfbee5cbee2feafb70aef`, con mensaje
`respaldo-local-antes-estudiante5-correcion`. No se aplico ese renombre:
la clase fisica implementada por el Estudiante 4 es `Deposito`.

## Que se conserva y que se corrige

- Se conservan AVL, arbol general, heap estable, TDAs y tests integrados en main.
- Se conservan los flujos, consultas y PasoPicking del Estudiante 5.
- La consulta de pedidos por sucursal usa `ColaPrioridad.buscar`, sin acceso por indice.
- El alta con stock inicial planifica posiciones habilitadas y capacidad antes de registrar
  el producto. Un alta sin capacidad no deja un producto parcialmente registrado.
- Recepcion y alta comparten la planificacion de capacidad. Las dimensiones se toman
  del producto registrado, aunque una linea utilice otra instancia del mismo codigo.
- Un item activado para stock ubicado rechaza las modificaciones de total sin posicion.
  La API de stock del Hito 1 se conserva para items independientes que todavia no
  entraron en el modelo fisico. En la aplicacion, usar AlmacenLogistico para las altas.
- Las consultas de inventario copian tambien las ubicaciones, sin compartir sus cantidades.
- Una posicion bajo un ancestro inhabilitado no es destino de recepcion ni de picking.
- Mover un sector valida todos los ancestros del destino y no suma nuevamente el contenido
  en los ancestros compartidos. Se rechazan ciclos y codigos duplicados entre hermanos.
- Las posiciones son hojas. Los codigos locales no pueden contener el separador `.`.
- El menu permite crear sectores bajo la raiz, volver a moverlos a ella, consultar su
  contenido/ocupacion y mostrar el deposito por niveles.

## Correspondencia con el reparto

| Parte | Implementacion / verificacion |
|---|---|
| A. Deposito | AlmacenLogistico contiene Deposito y delega operaciones fisicas. |
| B. ItemInventario | Ubicaciones, alta/retiro controlados, copias y validacion de consistencia. |
| C. Recepcion | Plan completo, capacidad acumulada por camino, aplicacion y liberacion de terminal. |
| D. Despacho | Consolidacion de lineas, picking, retiro, desencolado y terminal. |
| E. Picking | PasoPicking; posiciones en preorden; consulta sin modificar stock. |
| F. Inhabilitacion | Planifica fuera del subarbol, reubica y finalmente deshabilita. |
| G. Prioridad | setPrioridad + reordenar; empates conservan secuencia original. |
| H. Consultas | Producto, inventario ordenado, ocupacion/espacio, contenido y picking. |
| I. UI | Funciones originales y submenu Hito 2; opcion 13 muestra el deposito. |
| J. Regresion | Tests anteriores conservados; fixture del servicio agrega capacidad fisica. |

## Ejecutar las pruebas

Desde `almacen`, con JDK 26 y Maven:

```sh
mvn clean verify
```

Solo los escenarios nuevos:

```sh
mvn -Dtest=IntegracionHito2Test,DepositoIntegracionTest,MenuConsolaHito2Test test
```

Las pruebas cubren recepcion distribuida, FIFO de entregas, capacidad acumulada de lineas
repetidas, rechazo sin cambios parciales, picking DFS con productos repetidos, ausencia de
terminal, aumento/disminucion de prioridad, FIFO en empates, movimiento con contenido,
ancestros compartidos y llenos, ciclos, nombres duplicados, inhabilitacion exitosa y fallida,
registro inicial ubicado, copias de consulta y una interaccion real por consola.

La suite antigua del servicio conserva sus aserciones: solo agrega una posicion de
recepcion de 1000 UC en el setup para representar el nuevo requisito fisico.

## Prueba manual desde cero

Ejecutar `mvn exec:java` desde `almacen`.

1. Entrar a **11. Funciones Hito 2**, luego **8. Crear sector**.
2. Dejar la ruta padre vacia (Enter) para crear la zona `A`, capacidad 40 UC.
3. Bajo `A`, crear posiciones `1` y `2`, capacidad 20 UC cada una.
4. Bajo la raiz, crear la zona `B`, capacidad 40 UC; bajo `B`, posicion `1`, 40 UC.
5. Usar **13. Mostrar deposito por niveles**. Las rutas son `A.1`, `A.2`, `B.1`.
6. Registrar producto `P`, espacio unitario 2 UC y stock cero en el submenu Hito 2.
7. Volver al principal, registrar entrega de 12 unidades, asignarla y finalizar descarga.
8. Consultar: stock 12; A.1 ocupa 20 UC; A.2 ocupa 4 UC; A ocupa 24 UC.
9. Registrar un pedido de 3 unidades; consultar picking y despachar. Stock final: 9.
10. Finalizar carga. Mover `A.2` hacia `B`; su ruta pasa a `B.2` y conserva 2 unidades.
11. Inhabilitar `A`. Se reubican las 7 unidades restantes hacia B.1 y A queda vacia.
12. Intentar una entrega que supere la capacidad restante. Debe rechazarse sin cambiar
    stock ni liberar la terminal de descarga.

Para registrar stock inicial positivo por el menu original, primero crear posiciones con
capacidad: el sistema ahora ubica ese stock automaticamente. No hay posiciones ficticias
ni capacidad ilimitada agregada silenciosamente.

## Limites y decisiones de implementacion

- Los planes siguen un criterio voraz determinista por preorden; no buscan una distribucion
  optima entre todas las combinaciones posibles ni una distancia geometrica minima.
- La atomicidad de los flujos se verifica para rechazos de validacion/capacidad sobre el
  modelo en memoria de un solo hilo. No se incorporaron transacciones de base de datos.
- El recorrido del AVL es O(n). Copiar las ubicaciones agrega O(sum(u_i^2)) por el acceso
  por indice de ListaSimple. No atribuir O(n) al costo completo de esa consulta.
- Las pruebas automatizadas no sustituyen la redaccion del informe ni la defensa del equipo.
- La rama queda preparada para revision. Antes de integrarla a un main que haya avanzado,
  actualizar las referencias y volver a ejecutar la suite sobre la nueva combinacion.
