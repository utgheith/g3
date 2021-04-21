package ag

type Maybe[A] = A | Null

extension[A] (maybe: Maybe[A]) {

  def toOption: Option[A] = if (maybe != null) Some(maybe) else None
  def toSeq: Seq[A] = if (maybe != null) Seq(maybe) else Seq.empty

  def foreach(f: A => Any): Unit =
    if (maybe != null) f(maybe)

  def map[B](f: A => B): B|Null = 
    if (maybe != null) {
      f(maybe)
    } else {
      null
    }

  def flatMap[B](f: A => Maybe[B]): Maybe[B] =
    if (maybe != null) {
      f(maybe)
    } else {
      null
    }
    
  def getOrElse(other: => A): A =
    if (maybe == null) other else maybe

  def foldLeft[B](init: B)(f: (B,A) => B): B =
    if (maybe != null) {
      f(init,maybe)
    } else {
      init
    }

  def filter(f: A => Boolean): A|Null =
    if (maybe != null) {
      if (f(maybe)) maybe else null
    } else {
      null
    }

  def isEmpty: Boolean =
    maybe == null

  def isFull: Boolean =
    maybe != null

  def get: A = maybe.nn
}
