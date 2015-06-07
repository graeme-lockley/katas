package kata

object Gen {
	def inRange(min: Double, max: Double): Double = (Math.random() * (max - min)) + min

	def inIntRange(min: Int, max: Int): Int = Math.round(inRange(min, max)).toInt

	def inCharRange: (Char, Char) => () => Char = (min, max) => () => inRange(min.toDouble, max.toDouble).toChar

	val integers: () => Int = () => inIntRange(Int.MinValue, Int.MaxValue)

	def listOf[T]: (() => T) => () => List[T] = (gen) => () => (1 to inIntRange(0, 15)).toList.map(x => gen())

	def nonEmptyListOf[T]: (() => T) => () => List[T] = (gen) => () => (1 to inIntRange(1, 15)).toList.map(x => gen())

	def oneOf[T]: List[T] => () => T = items => () => items(inIntRange(0, items.length - 1))

	def forAll[T] = (gen: () => T) => (func: T => Unit) => 1 to 1000 foreach { _ => func(gen()) }

	def forAll[T1, T2] = (gen1: () => T1, gen2: () => T2) => (func: (T1, T2) => Unit) => 1 to 1000 foreach { _ => func(gen1(), gen2()) }
}
