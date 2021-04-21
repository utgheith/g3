package html

class Html(attrs: Seq[(String,String)]) extends TaggedNode("html",attrs) {

  type ChildType = Body

}

def html(attrs: (String,String)*)(f: Html ?=> Any): Html = {
  val me = Html(attrs)
  f(using me)
  me
}
