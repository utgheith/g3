package ag.internal

import java.io.InputStream

class InputStreamIterator(is: InputStream) extends Iterator[Byte] {

  private val batch = Array.ofDim[Byte](1024)
  private var n: Int = 0
  private var i: Int = 0

  private def fill(): Unit = {
    n = is.read(batch)
    i = 0
  }

  fill()

  def hasNext(): Boolean = if (i < n) {
    true
  } else if (n < 0) {
    false
  } else {
    fill()
    hasNext()
  }
  
  def next(): Byte = {
    val b = batch(i)
    i += 1
    b
  }
}
