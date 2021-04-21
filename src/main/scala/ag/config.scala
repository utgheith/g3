package ag

import com.typesafe.config.Config
import com.typesafe.config.ConfigException.Missing

extension(config: Config|Null) {
  
  inline def / (name: String): Config|Null = try {
    for {
      c <- config
      s <- c.getConfig(name)
    } yield s
    //config.flatMap(_.getConfig(name))
  } catch {
    case _ : Missing => null
  }
  
  inline def $ (name: String): String|Null = try {
    config.flatMap(_.getString(name))
  } catch {
    case _ : Missing => null
  }
}

