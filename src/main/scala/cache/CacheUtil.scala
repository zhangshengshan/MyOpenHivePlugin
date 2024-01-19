package cache

import java.util.concurrent.TimeUnit

object CacheUtil {
  val cache = new ExpiringCache(1, TimeUnit.MINUTES)
}
