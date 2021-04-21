package ag

import scala.collection.mutable

def timed[A](f: => A): (Long,A) = {
  val now = System.currentTimeMillis()
  val a = f
  (System.currentTimeMillis() - now, a)
}

extension[A](a: A) {
  def |>[B](f: A => B): B = f(a)
}

extension(s: String) {
  def mySplit(split_char : Char): IndexedSeq[String] = {
    var out = mutable.Buffer[String]()
    val sb = StringBuilder()

    enum State {
      case SKIP
      case ACC
    }

    import State._

    var i = 0
    var state = SKIP

    while (i < s.size) {
      val ch = s(i)
      state match {
        case SKIP =>
          if (ch == split_char) {
            i += 1
          } else {
            state = ACC
          }
        case ACC =>
          if (ch == split_char) {
            out.append(sb.toString)
            sb.clear()
            i += 1
            state = SKIP
          } else {
            sb.append(ch)
            i += 1
          }
      }
    }

    if (sb.length > 0) {
      out.append(sb.toString)
    }

    out.toIndexedSeq
  }
}
