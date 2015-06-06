package kata

import kata.Gen._
import kata.SC.add
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class SCTest extends FlatSpec with Matchers {
	val nonNegativeIntegers: () => Int = () => Gen.inIntRange(0, 1500)

	def separators: () => Char = () => {
		val c = Gen.inCharRange(1.toChar, 255.toChar)()
		if (isValidSeparator(c)) c else separators()
	}

	def isValidSeparator(ch: Char) = !Character.isDigit(ch) && ch != 0.toChar && ch != '[' && ch != '-' && ch != ']'

	"Given an empty string" should "return 0" in
		assert(add("") === 0)

	"Given a non-negative integer" should "return the numbers value if it is non-negative otherwise 0" in
		forAll(nonNegativeIntegers) { n =>
			val result = if (n < 1001) n else 0
			assert(add(n.toString) === result)
		}

	"Given non-negative integers separated with a comma or newline" should "return the sum of all numbers less than 1001" in
		forAll(nonEmptyListOf(nonNegativeIntegers)) { ns =>
			assert(add(mkString(ns, List(",", "\n"))) === ns.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with a single character custom separator" should "return the sum of all numbers less than 1001" in
		forAll(nonEmptyListOf(nonNegativeIntegers), separators) { (ns, sep) =>
			assert(add(s"//$sep\n${ns.mkString(sep.toString)}") === ns.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with multiple multi character custom separator" should "return the sum of all numbers less than 1001" in
		forAll(nonEmptyListOf(nonNegativeIntegers), nonEmptyListOf(nonEmptyListOf(separators))) { (ns, seps) =>
			val ssep = seps.map(_.mkString)
			assert(add(s"//[${ssep.mkString("][")}]\n${mkString(ns, ssep)}") === ns.filter(_ < 1001).sum)
		}

	"Given numbers with at least one negative separated with a comma" should "throw an exception with the negatives in the exception message" in
		forAll(nonEmptyListOf(integers)) { ns =>
			if (ns.exists(_ < 0)) {
				val ex = intercept[IllegalArgumentException] {
					add(ns.mkString(","))
				}
				assert(ex.getMessage === ns.filter(_ < 0).mkString(","))
			}
		}

	def mkString(xs: List[Int], seps: List[String]): String = xs match {
		case Nil => ""
		case y :: Nil => y.toString
		case y1 :: y2 :: ys => y1.toString ++ Gen.oneOf(seps)() ++ mkString(y2 :: ys, seps)
	}
}
