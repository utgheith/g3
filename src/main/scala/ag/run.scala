package ag

import java.io.File

case class RunContext(timeout: Long, wd: File|Null, errKind: () => ErrKind) {

  /////////////////
  // Seq[String] //
  /////////////////

  def bytes(s: Seq[String]): geny.Generator[Byte] =
    Builder1(s, errKind(), wd, timeout)

  def chars(s: Seq[String]): geny.Generator[Char] = bytes(s).toChars

  def lines(s: Seq[String]): geny.Generator[String] = chars(s).toLines

  def ignore(s: Seq[String]): Unit =
    Builder0(s, OutKind0.IGNORE, errKind(), wd, timeout).run()

  def show(s: Seq[String]): Unit =
    Builder0(s, OutKind0.SHOW, errKind(), wd, timeout).run()

  def create[F](f: F, s: Seq[String])(using fl: FileLike[F]): Unit =
    Builder0(s, OutKind0.CREATE(fl.toFile(f)), errKind(), wd, timeout).run()


  ////////////
  // String //
  ////////////

  def bytes(s: String): geny.Generator[Byte] = bytes(s.mySplit(' '))

  def chars(s: String): geny.Generator[Char] = chars(s.mySplit(' '))

  def lines(s: String): geny.Generator[String] = lines(s.mySplit(' '))

  def ignore(s: String): Unit = ignore(s.mySplit(' '))

  def show(s: String): Unit = show(s.mySplit(' '))

  def create[F: FileLike](f: F, s: String): Unit = create(f, s.mySplit(' '))

  /////////////////
  // refinements //
  /////////////////

  def timeout[A](to: Long)(f: RunContext ?=> A)(using ctx: RunContext): A = {
    f(using ctx.copy(timeout = to))
  }

  def show_error[A](f: RunContext ?=> A)(using ctx: RunContext): A = {
    f(using ctx.copy(errKind = () => ErrKind.SHOW))
  }

  def ignore_error[A](f: RunContext ?=> A)(using ctx: RunContext): A = {
    f(using ctx.copy(errKind = () => ErrKind.IGNORE))
  }

}

given RunContext = RunContext(Long.MaxValue,null, () => ErrKind.SHOW)

def run(using ctx: RunContext): RunContext = ctx




  


