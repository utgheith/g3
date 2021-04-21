package world

import collection.mutable

import scala.jdk.CollectionConverters._

class World extends AutoCloseable {

    private val data = mutable.Map[Key[?],Any]()
    private var closeOrder = List[AutoCloseable]()

    def get[A](key: Key[A]): A = synchronized {
        data.getOrElseUpdate(key,
            {
                val v = key.factory(this)
                v match {
                    case c: AutoCloseable => closeOrder = c +: closeOrder
                    case _ =>
                }
                v
            }
        ).asInstanceOf[A]
    }

    def close(): Unit = synchronized {
        closeOrder.foreach(_.close())
        closeOrder = List()
    }

}

def world[A](f: World ?=> A): A = {
    val w = World()
    try {
        f(using w)
    } finally {
        w.close()
    }
}


/*
val first_name = Key("a") { _ =>
    "ahmed"
}

val last_name = Key("b") { _ =>
    "gheith"
}

val name = Key("c") { _ =>
    Universe.config |> println
    s"${first_name.value} ${last_name.value}"
}



@main def hello: Unit = {
    //os3.read.stream(Paths.get("hello")) |> toString |> println
    //os3.read.stream("hello") |> toString |> println
    //os3.read.lines(Paths.get("hello")).foreach(l => println(s"*** $l"))

    Universe {
        name.value |> println
    }

    //println(g.map(_.toChar).mkString)
    println("Hello world!")
    println(msg)
}

def msg = "I was compiled by Scala 3. :)"
*/