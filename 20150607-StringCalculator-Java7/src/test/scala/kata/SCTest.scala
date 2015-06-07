package kata

import kata.Gen._
import kata.SC.add
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class SCTest extends FlatSpec with Matchers {
	val nonNegativeIntegers: () => Int = () => inIntRange(0, 1500)

	def separators: () => Char = () => {
		val c = inCharRange(1.toChar, 255.toChar)()
		if (isValidSeparator(c)) c else separators()
	}

	def isValidSeparator(ch: Char) = !Character.isDigit(ch) && ch != '[' && ch != ']' && ch != '-'

	"Given an empty string" should "return 0" in {
		add("") should equal(0)
	}

	"Given a non-negative integer" should "return the numbers value if less than 1001 otherwise 0" in
		forAll(nonNegativeIntegers) { n =>
			val result = if (n < 1001) n else 0
			add(n.toString) should equal(result)
		}

	"Given non-negative integers separated with a comma or newline" should "return the sum of all numbers less than 1001" in
		forAll(nonEmptyListOf(nonNegativeIntegers)) { ns =>
			add(mkString(ns, List(",", "\n"))) should equal(ns.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with a single character custom separator" should "return the sum of all numbers less than 1001" in
		forAll(nonEmptyListOf(nonNegativeIntegers), separators) { (ns, sep) =>
			add(s"//$sep\n${ns.mkString(sep.toString)}") should equal(ns.filter(_ < 1001).sum)
		}

	"Given non-negative integers separated with a multiple multi character custom separator" should "return the sum of all numbers less than 1001" in
		forAll(nonEmptyListOf(nonNegativeIntegers), nonEmptyListOf(nonEmptyListOf(separators))) { (ns, seps) =>
			val sseps = seps.map(_.mkString)
			add(s"//[${sseps.mkString("][")}]\n${mkString(ns, sseps)}") should equal(ns.filter(_ < 1001).sum)
		}

	"Given numbers with at least one negative separated with a comma" should "throw an exception with the negatives in the exception message" in
		forAll(nonEmptyListOf(integers)) { ns =>
			if (ns.exists(_ < 0)) {
				val ex = intercept[IllegalArgumentException] {
					add(s"${ns.mkString(",")}")
				}
				ex.getMessage should equal(ns.filter(_ < 0).mkString(","))
			}
		}

	def mkString(xs: List[Int], seps: List[String]): String = xs match {
		case Nil => ""
		case y :: Nil => y.toString
		case y1 :: y2 :: ys => y1.toString ++ Gen.oneOf(seps)() ++ mkString(y2 :: ys, seps)
	}
}
