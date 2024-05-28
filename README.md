# Proyecto de Investigación - Primer Bimestre

![image](https://github.com/ikestrella/PAvanzada/assets/115649439/cb035063-6ffd-47dc-8595-255a40cfabe0)

## Concurrencia en Scala

La actividad consiste en realizar una búsqueda de información sobre los modelos de programación concurrente que utilizan otros lenguajes de programación y comprender sus similitudes y diferencias con el modelo que usa el lenguaje que está estudiando (Java, que se basa en Threads). El lenguaje asignado es Scala, en el cual se hará la multiplicación de dos matrices usando hilos.

### Comparativa de Concurrencia: Java vs. Scala

En Java, los hilos se gestionan explícitamente mediante la creación de instancias de la clase `Thread` o mediante la implementación de la interfaz `Runnable`. La gestión de hilos en Java es más manual y requiere un control más detallado sobre la creación, inicio y sincronización de los hilos.

En comparación, Scala proporciona una abstracción más alta a través de `Future`, lo que simplifica la programación concurrente al manejar la creación y la sincronización de hilos de manera implícita.

### Ejemplo de Multiplicación de Matrices en Scala con Future

Para ilustrar la concurrencia en Scala, vamos a implementar un código de multiplicación de matrices utilizando `Future`. Primero, importamos las librerías necesarias:

```scala
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
```

Dadas las matrices:

```scala
val a = Array(
  Array(1, 2, 3),
  Array(0, 5, 2)
)

val b = Array(
  Array(1, 2),
  Array(4, 5),
  Array(7, 8)
)
```

Creamos la función para realizar la multiplicación, la cual retorna un `Array[Array[Int]]` que es el resultado:

```scala
def multiplyMatrices(a: Array[Array[Int]], b: Array[Array[Int]]): Array[Array[Int]] = {
  val rowsA = a.length
  val rowsB = b.length
  val colsA = a(0).length
  val colsB = b(0).length

  val resultado = Array.ofDim[Int](rowsA, colsB)

  if (colsA == rowsB) {
    val futures = for {
      i <- (0 until rowsA)
      j <- (0 until colsB)
    } yield Future {
      var sum = 0
      for (k <- 0 until colsA) {
        sum += a(i)(k) * b(k)(j)
      }
      resultado(i)(j) = sum
    }
    // Espera que se completen los hilos
    Await.result(Future.sequence(futures), Duration.Inf)
  }
  resultado
}
```

Las variables `rowsA`, `rowsB`, `colsA` y `colsB` definen las dimensiones de las matrices, y `resultado` es la matriz que contendrá el resultado de la multiplicación.

Después, verificamos si la columna de la matriz `A` es igual a la fila de la matriz `B`, ya que esto es necesario para realizar la multiplicación de matrices.

```scala
val futures = for {
  i <- (0 until rowsA)
  j <- (0 until colsB)
} yield Future {
  var sum = 0
  for (k <- 0 until colsA) {
    sum += a(i)(k) * b(k)(j)
  }
  resultado(i)(j) = sum
}
Await.result(Future.sequence(futures), Duration.Inf)
```

Dentro de esta condición, creamos una variable `futures` que contendrá los hilos encargados de realizar la multiplicación. Una vez realizada la multiplicación, los valores se asignan a la variable `resultado`.



## Como solucionar problemas de hilos en Scala.


### Data Race Condition.


En Scala, las condiciones de carrera se pueden mitigar mediante el uso de estructuras inmutables y el modelo de actores (Actors). Los actores encapsulan el estado y solo se comunican a través de mensajes, asegurando que no haya acceso concurrente a variables compartidas.

### Deadlock.


Los interbloqueos se pueden evitar en Scala mediante la programación funcional y el uso de Futures combinados con estructuras inmutables. Los Futures no requieren bloqueo explícito y permiten la composición de tareas asincrónicas sin necesidad de bloquear recursos.

### Livelock.


El livelock se puede mitigar en Scala mediante el diseño cuidadoso de algoritmos concurrentes y evitando dependencias circulares en el paso de mensajes. Los actores en Akka pueden ayudar a prevenir el livelock mediante el uso de patrones de manejo de mensajes que aseguren el progreso.

### Resource Starvation.


La inanición de recursos se puede mitigar en Scala usando ExecutionContext correctamente configurados y evitando bloqueos prolongados. Scala permite definir ExecutionContext personalizados que pueden gestionar la asignación de recursos de manera más equitativa.

### **Priority Inversion**.


La inversión de prioridad se puede abordar en Scala asegurando que los hilos de alta prioridad no se bloqueen innecesariamente esperando hilos de baja prioridad. Aunque Scala no proporciona mecanismos directos para manejar la inversión de prioridad, el uso de actores y Futures puede ayudar a evitar este problema al diseñar aplicaciones concurrentes de manera que minimicen las dependencias entre tareas de diferentes prioridades.

## Conclusión

Ambos lenguajes permiten la programación concurrente, pero utilizan modelos diferentes. Scala, con su uso de `Future` y `ExecutionContext`, proporciona una manera más simplificada y abstracta para manejar la concurrencia, mientras que Java ofrece un control más explícito y detallado mediante el uso directo de hilos y ejecutores (`Executors`).
