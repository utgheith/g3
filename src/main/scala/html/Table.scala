package html

class Table(attrs: Seq[(String,String)]) extends TaggedNode("table",attrs) with Element {

  type ChildType = Tr

}

def table(attrs: (String,String)*)(f: Table ?=> Any)(using p: TaggedNode)(using ev: Table <:< p.ChildType): Table = {
  val me = Table(attrs)
  f(using me)
  p.add(ev(me))
  me
}
