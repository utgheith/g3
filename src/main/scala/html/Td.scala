package html

class Td(attrs: Seq[(String,String)]) extends TaggedNode("td",attrs) {
  type ChildType = Element
}

def td(attrs: (String,String)*)(f: Td ?=> Any)(using p: TaggedNode)(using ev : Td <:< p.ChildType): Td = {
  val me = Td(attrs)
  f(using me)
  p.add(ev(me))
  me
}
