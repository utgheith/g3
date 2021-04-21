package world

import com.typesafe.config.Config

import scala.reflect.Typeable$package.Typeable

trait Key[+A] {
  def factory(w: World): A
  def value(using u: World): A = {
    u.get(this)
  }
}

class DataKey[+A,+D : Typeable](val d: D, f: World => A) extends Key[A] {
  def factory(w: World): A = f(w)

  override def hashCode: Int = this.getClass.hashCode ^ d.hashCode

  override def equals(other: Any): Boolean = other match {
    case rhs: DataKey[_,D] => rhs.d == d
    case _ => false
  }
}

object Key {
  def apply[A](f: World ?=> A): Key[A] = new Key {
    def factory(w: World): A = {
      f(using w)
    }
  }

  def withData[A,D](d: D)(f: World ?=> A): Key[A] = {
    DataKey(d,w => f(using w))
  }
}

