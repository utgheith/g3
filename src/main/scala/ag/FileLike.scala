package ag

import java.io.File
import java.nio.file.Path 

trait FileLike[A] {
  def toFile(a :A) : File
}

given FileLike[String] with {
  def toFile(s: String) = new File(s)
}

given FileLike[Path] with {
  def toFile(p: Path) = p.toFile.nn
}

given FileLike[File] with {
  def toFile(f: File) = f
}
