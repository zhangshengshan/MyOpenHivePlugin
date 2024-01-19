package cache
import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.collection.mutable

class ExpiringCache[K, V](timeout: Long, unit: TimeUnit) {
  private val cache: mutable.Map[K, V] = mutable.Map.empty
  private val scheduler: ScheduledExecutorService =
    Executors.newScheduledThreadPool(1)

  def put(key: K, value: V): Unit = {
    cache.put(key, value)
    val runnable = new Runnable {
      def run(): Unit = cache.remove(key)
    }
    scheduler.schedule(runnable, timeout, unit)
  }

  def isEmpty(): Boolean = cache.isEmpty

  def get(key: K): Option[V] = cache.get(key)
}
