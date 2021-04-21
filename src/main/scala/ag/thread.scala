package ag

import scala.concurrent.{Future, Promise}
import scala.util.Try

def thread[A](f: => A): Future[A] = {
  val p = Promise[A]()

  val t = new Thread() {
    override def run(): Unit = p.complete(Try(f))
  }

  t.setDaemon(true)
  t.start()

  p.future
}
