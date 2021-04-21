package html

class Body(attrs: Seq[(String,String)]) extends TaggedNode("body",attrs) {
  type ChildType = Element
}

def body(attrs: (String,String)*)(f: Body ?=> Any)(using p: TaggedNode)(using ev: Body <:< p.ChildType): Body = {
  val me = Body(attrs)
  f(using me)
  p.add(ev(me))
  me
}

def body(f: Body ?=> Any)(using p: TaggedNode)(using Body <:< p.ChildType): Body = {
  body()(f)
}
