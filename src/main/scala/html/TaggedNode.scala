package html

import scala.collection.mutable

abstract class TaggedNode(tag: String, attrs: Seq[(String,String)]) extends Node {
  type ChildType <: Node
  private val children = mutable.Buffer[ChildType]()
  //val attributes = mutable.Map(attrs:_*)

  def add(n: ChildType): Unit = {
    children.append(n)
  }

  override def toHtml(lines: mutable.Buffer[String], indent: String): Unit = {
    val as =
      if (attrs.isEmpty) "" else
        attrs.map { case (k,v) =>
          s"$k=\"$v\""
        }.mkString(" "," ","")
    lines.append(s"$indent<$tag$as>")
    children.foreach(_.toHtml(lines,s"  $indent"))
    lines.append(s"$indent</$tag>")
  }


}
