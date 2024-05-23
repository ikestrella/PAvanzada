import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

def multiplyMatrices(a: Array[Array[Int]], b: Array[Array[Int]]): Array[Array[Int]] = {
  val rowsA = a.length
  val rowsB = b.length
  val colsA = a(0).length
  val colsB = b(0).length

  val resultado = Array.ofDim[Int](rowsA, colsB)

  if (colsA == rowsB){
    val futures = for
      i <- (0 until rowsA)
      j <- (0 until colsB)
    yield Future:
      var sum = 0
      for k <- (0 until colsA) do
        sum += a(i)(k) * b(k)(j)
      resultado(i)(j) = sum
    // Espera que se completen los hilos
    Await.result(Future.sequence(futures), Duration.Inf)
  }

  resultado
}


val a = Array(
  Array(1, 2, 3),
  Array(0, 5, 2)
)

val b = Array(
  Array(1, 2),
  Array(4, 5),
  Array(7, 8)
)



val resultado = multiplyMatrices(b, a)

// Print the resultado
resultado.foreach(row =>println(row.mkString("\t")))
    
