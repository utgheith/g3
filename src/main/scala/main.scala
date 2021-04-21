import com.typesafe.config.ConfigFactory
import world._
import ag.given
import ag.*
import geny.Generator

import java.io.{InputStreamReader, Reader}
import java.nio.file.{Files, Paths}
import java.util.concurrent.{CountDownLatch, TimeUnit}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.Try
import scala.jdk.CollectionConverters._
import scala.collection.mutable

import ssh.*

val config = Key {
  val m = Map(
    "app_name" -> "hello app"
  ).asJava
  ConfigFactory.parseMap(m).nn
}

val app_name = Key {
  config.value.getString("app_name").nn
}

val linux1 = Key.withData("ag","linux1") {
  ssh_client("ag", "linux1")
}

val linux3 = Key.withData("ag","linux3") {
  ssh_client("ag", "linux3")
}


object Main {
  def main(args: Array[String]) : Unit = {
    import scala.language.unsafeNulls
    world {
      println(app_name.value)
    }
    //println("hello")

    //"git log".run ( _.foreach(print) )

    //cd("src") {
    //  "ls".run(_.foreach(print))
   // }

    debug {
      run.lines("ls -a").foreach(println)
    }

    debug {
      run.lines("ls").take(1).foreach(println)
    }

    debug {
      run.timeout(1) {
        Try(run.ignore("sleep 5"))
      }
    }


    debug {
      run.show("pwd")
    }

    debug {
      run.show_error {
        Try(run.ignore("gcc"))
      }
    }
    //Try(process("gcc").ignoreOut().showErr().run()) |> println

    //33"------ create file out err ------" |> println
    debug {
      run.create("out.xyz","ls")
    }

    //"------- show ----------" |> println
    debug(run.ignore_error {
      run.show("gcc --version")
    })

    debug(run.show("gcc --version"))

    world {
      debug {
        linux1.value.lines("hostname; date").foreach(println)
        linux3.value.lines("hostname; date").foreach(println)
      }
    }

    debug {
      import html.*
      html() {
        //text("hello")
        body() {
          text {
            "hello"
          }
          table() {
            (1 to 4).foreach { i =>
              tr() {
                if (i > 2) {
                  td() {
                    a(href = "https://google.com", extra = Seq("x" -> "y")) {
                      text("hello")
                    }
                    a() {}
                  }
                }
              }
            }
          }
        }
      }
    } |> println


    //timeout(5000) {
    //  val g = "sleep 1000000".run(identity)
   //   g.foreach(print)
   // }



    //println("ls".run(wd = Paths.get("src").toFile))



  }
}
