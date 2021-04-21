package ag.internal

import java.io.{IOException, Reader}
import geny.Generator
import Generator.*

class ReaderGenerator(val r: Reader)(keep_going: => Boolean) extends Generator[Char] {
  def generate(h: Char => Action): Action = try {
    var arr = Array.ofDim[Char](1024)
    var n = 1
    var a : Action = Continue
    while (keep_going && (n > 0) && (a == Continue)) {
      n = try {
        r.read(arr)
      } catch {
        case _ : IOException =>
          return a
      }
      var i = 0
      while (keep_going && (i < n) && (a == Continue)) {
        val ch = arr(i)
        //println(s"$ch ${ch.toInt}")
        a = h(arr(i))
        i += 1
      }
    }
    a
  } finally {
    r.close()
  }
}
