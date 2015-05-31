package kata

import kata.SC.add
import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.prop.PropertyChecks

class SCTest extends FlatSpec with PropertyChecks with Matchers {
	val integers = Gen.choose(Int.MinValue, Int.MaxValue)
	val listOfIntegers = Gen.listOf(integers)
	val separators = Gen.choose(0.toChar, 125.toChar)
	val stringSeparators = for (c <- Gen.listOf(separators)) yield c.filter(validSeparator).mkString
	val listOfStringSeparators = for (c <- Gen.listOf(stringSeparators)) yield c.filter(_.length > 0)
	val nonNegativeIntegers = Gen.choose(0, Int.MaxValue)
	val listOfNonNegativeIntegers = Gen.listOf(nonNegativeIntegers)

	"Given an empty string" should "return 0" in
		assert(add("") == 0)

	"Given a non-negative integer as a string" should "return the value if less than 1001 otherwise 0" in
		forAll (nonNegativeIntegers) { n => 
			val result = if (n < 1001) n else 0
			assert(add(n.toString) == result) 
		}

	"Given non-negative integers separated by a comma" should "return the sum of those less than 1001" in
		forAll (listOfNonNegativeIntegers) { ns =>
			assert(add(ns.mkString(",")) == sum(ns))
		}

	"Given non-negative integers separated by a comma or newline" should "return the sum of those less than 1001" in
		forAll (listOfNonNegativeIntegers) { ns =>
			assert(add(mkString(ns, List(",", "\n"))) == sum(ns))
		}

	"Given non-negative integers separated by a single character custom separator" should "return the sum of those less than 1001" in
		forAll (listOfNonNegativeIntegers, separators) { (ns, sep) =>
			whenever(ns.length > 0 && validSeparator(sep)) {
				assert(add(s"//$sep\n${ns.mkString(sep.toString)}") == sum(ns))
			}
		}

	"Given non-negative integers separated by a multiple multi-character custom separator" should "return the sum of those less than 1001" in
		forAll (listOfNonNegativeIntegers, listOfStringSeparators) { (ns, seps) =>
			whenever(ns.length > 0 && seps.length > 0) {
				assert(add(s"//[${seps.mkString("][")}]\n${mkString(ns, seps)}") == sum(ns))
			}
		}

	"Given numbers with at least one negative" should "throw an exception with the negatives numbers in the message" in
		forAll(listOfIntegers) { ns =>
			whenever (ns.exists(_ < 0)) {
				val ex = intercept[java.lang.IllegalArgumentException] {
					add(ns.mkString(","))
				}
				assert(ex.getMessage == ns.filter(_ < 0).mkString(","))
			}
		}

	def validSeparator(ch: Char): Boolean = ch != '-' && !Character.isDigit(ch) && ch != '[' && ch != ']' && ch != 10.toChar && ch != 13.toChar

	def sum(ns: List[Int]): Int = ns.filter(_ < 1001).sum

	def mkString(ns: List[Int], seps: List[String]): String = ns match {
		case Nil => ""
		case x::Nil => x.toString
		case x1::xs => x1.toString ++ Gen.oneOf(seps).sample.get ++ mkString(xs, seps)
	}
}
