package world

import com.typesafe.config.{Config, ConfigFactory}
import org.scalacheck._

import scala.language.implicitConversions

import ag.*
import ag.given

enum Mode {
  case Test
  case Debug
  case Production
}

class T1 extends Properties("x") {

  val mode = Key {
    Mode.Test
  }

  val config = Key {
    val file =
      """
        |c.x = 100
        |""".stripMargin
    ConfigFactory.parseString(file)
  }

  val a = Key {
    config.value.flatMap(_.getInt("c.x"))
  }

  property("a") = {
    world {
      println(mode.value)
      a.value == 100

    }
  }

}
