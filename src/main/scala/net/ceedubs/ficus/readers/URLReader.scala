package net.ceedubs.ficus.readers

import java.net.{MalformedURLException, URI, URL}
import com.typesafe.config.{Config, ConfigException}

trait URLReader {
  implicit val javaURLReader: ValueReader[URL] = new ValueReader[URL] {
    def read(config: Config, path: String): URL = {
      val s = config.getString(path)
      try new URI(s).toURL
      catch {
        case e: MalformedURLException =>
          throw new ConfigException.WrongType(config.origin(), path, "java.net.URL", "String", e)
      }
    }
  }
}

object URLReader extends URLReader
