package ag

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

trait DurationLike[A] {
  def toDuration(a: A): Duration
}

given DurationLike[Duration] with {
  def toDuration(d: Duration) = d
}

given DurationLike[Int] with {
  def toDuration(d: Int) = Duration(d,TimeUnit.MILLISECONDS)
}

given DurationLike[Long] with {
  def toDuration(d: Long) = Duration(d,TimeUnit.MILLISECONDS)
}
