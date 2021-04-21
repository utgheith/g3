package html

class A(attrs: Seq[(String,String)]) extends TaggedNode("a",attrs) with Element {
  type ChildType = Element

}

def a(href: String|Null = null, extra: Seq[(String,String)] = Seq())(f: A ?=> Any)(using p: TaggedNode)(using ev: A <:< p.ChildType): A = {
  val attrs = (if (href != null) Seq("href" -> href) else Seq()) ++ extra
  val me = new A(attrs)
  
  f(using me)
  p.add(ev(me))
  me
}
