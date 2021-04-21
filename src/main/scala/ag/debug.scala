package ag

import scala.quoted.*

inline def show[A](inline v: A): Option[String] = ${ showImpl('v)}
def showImpl[A](expr: Expr[A])(using quotes: Quotes) = {
  import quotes.reflect.Position
  val pos = Position.ofMacroExpansion
  //Expr(pos)
  Expr(pos.sourceCode)
}//Expr(expr.show)

inline def debug[A](inline v: A): Unit = {
  import scala.jdk.CollectionConverters.given
  import scala.language.unsafeNulls
  val s = show(v).getOrElse("<?>")
  val lines: Seq[String] = s.lines.iterator.asScala.map(l => s" $l ").toSeq
  val w = lines.map(_.length).max
  val aligned = lines.map { line =>
    val extra = " " * (w - line.length)
    line + extra
  }
  val edge = "*" * w
  //println()
  (edge +: aligned :+ edge).foreach(x => println(s"*$x*"))

  val out = v

  print("=========> ")
  println(out)
}
