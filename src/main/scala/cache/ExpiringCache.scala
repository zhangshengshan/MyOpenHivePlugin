package cache
import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.collection.mutable

class ExpiringCache(timeout: Long, unit: TimeUnit) {
  private val cache: mutable.Map[String, String] = mutable.Map.empty
  private val scheduler: ScheduledExecutorService =
    Executors.newScheduledThreadPool(1)

  def put(key: String, value: String): Unit = {
    cache.put(key, value)
    val runnable = new Runnable {
      def run(): Unit = cache.remove(key)
    }
    scheduler.schedule(runnable, timeout, unit)
  }

  def exists(key: String): Boolean = {
    cache.keys.exists(x => x.contains(key.toString))
  }

  def getSimilar(key: String): List[String] = {
    cache.keys.filter(x => x.contains(key.toString)).toList
  }

  def isEmpty(): Boolean = cache.isEmpty
  def get(key: String): Option[String] = cache.get(key)
}
