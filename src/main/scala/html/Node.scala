package html

import collection.mutable

trait Node {

  def toHtml(lines: mutable.Buffer[String], indent: String): Unit

  def toHtml(): Seq[String] = {
    val buf = mutable.Buffer[String]()
    toHtml(buf,"")
    buf.toSeq
  }

  override def toString(): String = {
    toHtml().mkString("\n")
  }

}
