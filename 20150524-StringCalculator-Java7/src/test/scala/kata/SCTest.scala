package kata

import kata.SC.add
import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class SCTest extends FlatSpec with PropertyChecks with Matchers {
	val integers = Gen.oneOf(Int.MinValue, Int.MaxValue)
	val nonNegativeIntegers = Gen.oneOf(0, 1500)
	val listOfIntegers = Gen.listOf(integers)
	val listOfNonNegativeIntegers = Gen.nonEmptyListOf(nonNegativeIntegers)
	val separators = for (c <- Gen.choose(33, 127)) yield c.toChar
	val stringOfSeparators = for (s <- Gen.nonEmptyListOf(separators)) yield s.mkString
	val listOfStringOfSeparators = Gen.nonEmptyListOf(for (s <- Gen.listOf(separators)) yield s.mkString)

	"Given an empty string" should "return 0" in
		assert(add("") === 0)

	"Given a non-negative integer" should "return the value if the number is less than 1001 else 0" in
		forAll(nonNegativeIntegers) { (x) =>
			val result = if (x < 1001) x else 0
			assert(add(x.toString) === result)
		}

	"Given non-negative integers separated by a comma" should "return the sum" in
		forAll(listOfNonNegativeIntegers) { (xs) =>
			assert(add(xs.mkString(",")) === sum(xs))
		}

	"Given non-negative integers separated by a comma or newline" should "return the sum" in
		forAll(listOfNonNegativeIntegers) { (xs) =>
			assert(add(mkString(xs, List(",", "\n"))) === sum(xs))
		}

	"Given non-negative integers separated by a single character custom separator" should "return the sum" in
		forAll(listOfNonNegativeIntegers, separators) { (xs, sep) =>
			if (isValidSeparator(sep)) {
				assert(add(s"//$sep\n${xs.mkString(sep.toString)}") === sum(xs))
			}
		}

	"Given non-negative integers separated by a single multi-character custom separator" should "return the sum" in
		forAll(listOfNonNegativeIntegers, stringOfSeparators) { (xs, sep) =>
			val fsep = filterSeparators(sep)
			if (fsep.length > 0) {
				myAssert(s"//[$sep]\n${xs.mkString(sep.toString)}", sum(xs))
			}
		}

	"Given non-negative integers separated by a multiple multi-character custom separator" should "return the sum" in
		forAll(listOfNonNegativeIntegers, listOfStringOfSeparators) { (xs, seps) =>
			val fseps = seps.map(sep => filterSeparators(sep)).filter(x => x.length > 0)
			if (fseps.nonEmpty) {
				assert(add(s"//[${fseps.mkString("][")}]\n${mkString(xs, fseps)}") === sum(xs))
			}
		}

	"Given non-negative integers separated by a comma or newline with at least one negative" should "throw an exception" in
		forAll(listOfIntegers) { (xs) =>
			whenever(xs.exists(_ < 0)) {
				val ex = intercept[IllegalArgumentException] {
					add(mkString(xs, List(",", "\n")))
				}
				assert(ex.getMessage === xs.filter(_ < 0).mkString(","))
			}
		}

	def filterSeparators(sep: String) = sep.filter(c => isValidSeparator(c))

	def isValidSeparator(c: Int) = {
		val ch = c.toChar
		(ch < '0' || ch > '9') && ch != '[' && (ch < '0' || ch > '9')
	}

	def mkString(xs: List[Int], seps: List[String]): String = xs match {
		case Nil => ""
		case y :: Nil => y.toString
		case y1 :: y2 :: ys => y1.toString ++ Gen.oneOf(seps).sample.get ++ mkString(y2 :: ys, seps)
	}

	def sum(xs: List[Int]): Int = xs.filter(_ < 1001).sum
}
