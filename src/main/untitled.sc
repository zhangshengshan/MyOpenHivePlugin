import scala.collection.mutable.Stack

val a = new Stack[Int]()
val b = new Stack[Int]()
a.push(1)
a.push(2)
a.push(3)
a.pop()

a.popAll().foreach(x ⇒ b.push(x))
b.pop()


