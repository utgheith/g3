package ag

import geny.Generator
import Generator.Continue

extension(g: Generator[Char]) {
  def toLines: Generator[String] = new Generator[String] {
    val sb = StringBuilder()
    override def generate(handleString: String => Generator.Action): Generator.Action = {
      g.generate {
        case '\n' =>
          val s = sb.toString
          sb.clear()
          handleString(s)
        case ch =>
          sb.append(ch)
          Continue
      }
    }
  }
}

extension(g: Generator[Byte]) {
  def toChars: Generator[Char] = g.map(_.toChar)
  //def toLines: Generator[String] = g.toChars.toLines
}