package kata

import kata.SC.add
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class SCTest extends FlatSpec with PropertyChecks with Matchers {
	def inRange: (Double, Double) => () => Double = (min: Double, max: Double) => () => Math.random() * (max - min) + min

	def inIntRange: (Int, Int) => () => Int = (min, max) => () => inRange(min.toDouble, max.toDouble)().toInt

	def inCharRange: (Char, Char) => () => Char = (min, max) => () => inRange(min.toDouble, max.toDouble)().toChar

	def integers: () => Int = inIntRange(Int.MinValue, Int.MaxValue)

	def nonNegativeIntegers: () => Int = inIntRange(0, 1500)

	def separators: () => Char = () => {
		val c = inCharRange(1.toChar, 255.toChar)()
		if (isValidSeparator(c)) c else separators()
	}

	def stringSeparators: () => String = () => nonEmptyListOf(separators)().mkString

	def nonEmptyListOf[T]: (() => T) => () => List[T] = (gen) => () => (1 to inIntRange(1, 15)()).toList.map(x => gen())

	def oneOf[T]: List[T] => () => T = items => () => items(inIntRange(0, items.length)())

	def forall[T] = (gen: () => T) => (func: T => Unit) => 1 to 1000 foreach { _ => func(gen()) }

	def forall[T1, T2] = (gen1: () => T1, gen2: () => T2) => (func: (T1, T2) => Unit) => 1 to 1000 foreach { _ => func(gen1(), gen2()) }

	"Given an empty string" should "return 0" in
		assert(add("") === 0)

	"Given a non-negative integer" should "return its value if less than 1001 otherwise should return 0" in
		forall(nonNegativeIntegers) { x =>
			val result = if (x < 1001) x else 0
			assert(add(x.toString) === result)
		}

	"Given non-negative integers separated with a comma" should "return the sum of the numbers less than 1001" in
		forall(nonEmptyListOf(nonNegativeIntegers)) { xs =>
			assert(add(xs.mkString(",")) === xs.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with a comma or newline" should "return the sum of the numbers less than 1001" in
		forall(nonEmptyListOf(nonNegativeIntegers)) { xs =>
			assert(add(mkString(xs, List(",", "\n"))) === xs.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with a custom single character" should "return the sum of the numbers less than 1001" in
		forall(nonEmptyListOf(nonNegativeIntegers), separators) { (xs, sep) =>
			assert(add(s"//$sep\n${xs.mkString(sep.toString)}") == xs.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with a custom multiple-character separator" should "return the sum of the numbers less than 1001" in
		forall(nonEmptyListOf(nonNegativeIntegers), stringSeparators) { (xs, sep) =>
			assert(add(s"//[$sep]\n${xs.mkString(sep.toString)}") == xs.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with multiple multiple-character custom separator" should "return the sum of the numbers less than 1001" in
		forall(nonEmptyListOf(nonNegativeIntegers), nonEmptyListOf(stringSeparators)) { (xs, seps) =>
			assert(add(s"//[${seps.mkString("][")}]\n${mkString(xs, seps)}") == xs.filter(_ < 1001).sum)
		}

	"Given integers separated with a comma and at least a single negative number" should "throw an exception" in {
		forall(nonEmptyListOf(integers)) { xs =>
			if (xs.exists(_ < 0)) {
				val ex = intercept[IllegalArgumentException] {
					add(s"${xs.mkString(",")}")
				}
				assert(ex.getMessage === xs.filter(_ < 0).mkString(","))
			}
		}
	}

	def filterSeparators(sep: String) = sep.filter(c => isValidSeparator(c))

	def isValidSeparator(ch: Char) = !Character.isDigit(ch) && ch != 0.toChar && ch != '[' && ch != ']'

	def mkString(xs: List[Int], seps: List[String]): String = xs match {
		case Nil => ""
		case y :: Nil => y.toString
		case y1 :: y2 :: ys => y1.toString ++ oneOf(seps)() ++ mkString(y2 :: ys, seps)
	}

	def sum(xs: List[Int]): Int = xs.filter(_ < 1001).sum
}
