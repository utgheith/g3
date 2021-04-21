package html

import scala.collection.mutable

class Text(str: String) extends Element {

  def toHtml(lines: mutable.Buffer[String], indent: String): Unit = {
    str.linesIterator.foreach(s => lines.append(s"$indent$s"))
  }

}

def text(s: String)(using p: TaggedNode)(using ev : Text <:< p.ChildType): Text = {
  val me = new Text(s)
  p.add(ev(me))
  me
}

