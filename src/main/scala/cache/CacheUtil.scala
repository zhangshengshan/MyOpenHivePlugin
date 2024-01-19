package cache

import java.util.concurrent.TimeUnit

object CacheUtil {
  val cache = new ExpiringCache[String, String](15, TimeUnit.MINUTES)
}
