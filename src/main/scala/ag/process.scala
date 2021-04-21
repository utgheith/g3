package ag

import geny.Generator
import Generator._

import java.io.{File, IOException}
import scala.concurrent.duration.Duration
import internal.InputStreamGenerator

import java.util.concurrent.TimeUnit

abstract class CommandException(val msg: String) extends Exception(msg) {
  def pb: ProcessBuilder
}

case class CommandNotFound(pb: ProcessBuilder) extends CommandException(s"can not run ${pb.command()}")

case class CommandFailed(pb: ProcessBuilder, rc: Int) extends CommandException(s"command ${pb.command()} failed, rc = $rc")

case class CommandTimeout(pb: ProcessBuilder) extends CommandException(s"command ${pb.command()} timed out")

case class CommandContext(wd: File, to: Duration)

enum ErrKind {
  case APPEND(f : File)
  case CREATE(f: File)
  case IGNORE
  case SAME
  case SHOW
}

enum OutKind0 {
  case APPEND(f: File)
  case CREATE(f: File)
  case IGNORE
  case SHOW
}

//type ErrKind1 = ErrKind0

case class Builder0(s: Seq[String], outKind: OutKind0, errKind: ErrKind, wd: File|Null, timeout: Long) {
  lazy val pb: ProcessBuilder = {
    import scala.language.unsafeNulls

    var pb = ProcessBuilder(s:_*).
      directory(wd)

    pb = outKind match {
      case OutKind0.APPEND(f) => pb.redirectOutput(ProcessBuilder.Redirect.appendTo(f))
      case OutKind0.CREATE(f) => pb.redirectOutput(ProcessBuilder.Redirect.to(f))
      case OutKind0.IGNORE => pb.redirectOutput(new File("/dev/null"))
      case OutKind0.SHOW => pb.redirectOutput(ProcessBuilder.Redirect.INHERIT)
    }

    pb = errKind match {
      case ErrKind.APPEND(f) => pb.redirectError(ProcessBuilder.Redirect.appendTo(f))
      case ErrKind.CREATE(f) => pb.redirectError(ProcessBuilder.Redirect.to(f))
      case ErrKind.IGNORE => pb.redirectError(new File("/dev/null"))
      case ErrKind.SAME => pb.redirectErrorStream(true)
      case ErrKind.SHOW => pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    }

    pb
  }

  def showErr(): Builder0 =
    if (errKind == ErrKind.SHOW) this else this.copy(errKind = ErrKind.SHOW)

  def run(): Unit = {
    val proc = pb.start().nn

    if (timeout != Long.MaxValue) {
      if (!proc.waitFor(timeout, TimeUnit.MILLISECONDS)) {
        proc.destroy()
      }
    }

    val rc = proc.waitFor()
    if (rc != 0) throw new CommandFailed(pb, rc)
  }


}

case class Builder1(s: Seq[String], errKind: ErrKind, wd: File|Null, timeout: Long) extends geny.Generator[Byte] {
  lazy val pb: ProcessBuilder = {
    import scala.language.unsafeNulls
    var pb = ProcessBuilder(s: _*).
      redirectOutput(ProcessBuilder.Redirect.PIPE)

    if (wd != null) {
      pb = pb.directory(wd)
    }

    pb = errKind match {
      case ErrKind.APPEND(f) => pb.redirectError(ProcessBuilder.Redirect.appendTo(f))
      case ErrKind.CREATE(f) => pb.redirectError(ProcessBuilder.Redirect.to(f))
      case ErrKind.IGNORE => pb.redirectError(new File("/dev/null"))
      case ErrKind.SAME => pb.redirectErrorStream(true)
      case ErrKind.SHOW => pb.redirectError(ProcessBuilder.Redirect.INHERIT)
    }

    pb
  }

  def timeout(to: Long): Builder1 =
    if (to == timeout) this else this.copy(timeout = to)

  def ignoreErr(): Builder1 =
    if (errKind == ErrKind.IGNORE) this else this.copy(errKind = ErrKind.IGNORE)

  def mergeErr(): Builder1 =
    if (errKind == ErrKind.SAME) this else this.copy(errKind = ErrKind.SAME)

  def ignoreOut(): Builder0 =
    Builder0(s,OutKind0.IGNORE,errKind,wd,timeout)

  def showOut(): Builder0 =
    Builder0(s,OutKind0.SHOW,errKind,wd,timeout)

  def createErr[F: FileLike](f: F): Builder1 =
    this.copy(errKind = ErrKind.CREATE(summon[FileLike[F]].toFile(f)))
    
  def createOut[F: FileLike](f: F): Builder0 =
    Builder0(s,OutKind0.CREATE(summon[FileLike[F]].toFile(f)),errKind,wd,timeout)

  override def generate(handleItem: Byte => Action): Action = {
    //val now = System.currentTimeMillis()
    //val limit = now + timeout.min(Long.MaxValue - now)
    val proc = pb.start().nn
    val is = proc.getInputStream.nn

    if (timeout != Long.MaxValue) {
      thread {
        if (!proc.waitFor(timeout,TimeUnit.MILLISECONDS)) {
          proc.destroy()
        }
      }
    }

    val a = try {
      InputStreamGenerator(is)(true).generate(handleItem)
    } finally {
      is.close()
    }


    val rc = proc.waitFor()
    if (rc != 0) throw new CommandFailed(pb, rc)
    a
  }


}

case class Builder2(s: Seq[String], wd: File) {
  def make: ProcessBuilder = {
    import scala.language.unsafeNulls
    val pb = ProcessBuilder(s:_*).
      directory(wd).
      redirectOutput(ProcessBuilder.Redirect.PIPE).
      redirectError(ProcessBuilder.Redirect.PIPE)

    pb
  }
}


extension(pb: ProcessBuilder) {
  def runBytes: geny.Generator[Byte] = new Generator[Byte] {
    override def generate(handleItem: Byte => Action): Action = {
      val proc = pb.start().nn
      val is = proc.getInputStream.nn
      val a = try {
        InputStreamGenerator(is)(true).generate(handleItem)
      } finally {
        is.close()
      }
      val rc = proc.waitFor()
      if (rc != 0) throw new CommandFailed(pb,rc)
      a
    }
  }

  def runChars: geny.Generator[Char] =
    pb.runBytes.toChars
}

extension(s: Seq[String]) {
  def runBytes: geny.Generator[Byte] = {
    Builder1(s,errKind = ErrKind.IGNORE, wd = null, timeout = Long.MaxValue)
  }

  def runChars: geny.Generator[Char] = {
    s.runBytes.toChars
  }

  def runLines: geny.Generator[String] = {
    s.runChars.toLines
  }
}

extension(s: String) {
  def :>[F](rhs: F)(using t: FileLike[F]): Builder0 = {
    Builder0(s.mySplit(' '),OutKind0.CREATE(t.toFile(rhs)),ErrKind.SHOW,null,Long.MaxValue)
  }
  def runBytes: geny.Generator[Byte] = {
    s.mySplit(' ').runBytes
  }

  def runChars: geny.Generator[Char] = {
    s.mySplit(' ').runChars
  }

  def runLines: geny.Generator[String] = {
    s.mySplit(' ').runLines
  }
}




