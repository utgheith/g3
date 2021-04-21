package html

class Tr(attrs: Seq[(String,String)]) extends TaggedNode("tr",attrs) {
  type ChildType = Td
}

def tr(attrs: (String,String)*)(f: Tr ?=> Any)(using p:TaggedNode)(using ev : Tr <:< p.ChildType): Tr = {
  val me = Tr(attrs)
  f(using me)
  p.add(ev(me))
  me
}
