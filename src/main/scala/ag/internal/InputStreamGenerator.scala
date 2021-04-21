package ag.internal

import geny.Generator
import Generator.{Action,Continue, End}

import java.io.{IOException, InputStream}

class InputStreamGenerator(val r: InputStream)(keep_going: => Boolean) extends Generator[Byte] {
  def generate(h: Byte => Action): Action = try {
    var arr = Array.ofDim[Byte](1024)
    var n = 1
    var a : Action = Continue
    while ((n > 0) && (a == Continue)) {
      n = try {
        r.read(arr)
      } catch {
        case _ : IOException =>
          return a
      }
      var i = 0
      while ((i < n) && (a == Continue)) {
        val ch = arr(i)
        //println(s"$ch ${ch.toInt}")
        a = h(arr(i))
        i += 1
        if (!keep_going) a = End
      }
      if (!keep_going) a = End
    }
    a
  } finally {
    // drain the stream
    r.close()
  }
}
