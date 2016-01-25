package kata

import org.scalatest.FlatSpec
import kata.SC.add

class SCTest extends FlatSpec {
	"given a blank" should "return 0" in {
		assert(add("") === 0)
	}

	"given a non-negative integer less than 1001" should "return its value" in {
		assert(add("3") === 3)
	}

	"given a non-negative integer greater than 1000" should "return 0" in {
		assert(add("10004") === 0)
	}

	"given non-negative integers separated with a comma or newline" should "return the sum of all numbers less than 1001" in {
		assert(add("10,4\n1006") === 14)
	}

	"given non-negative integers separated with a single character regex custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//*\n10*4*1006") === 14)
	}

	"given non-negative integers separated with a single multi-character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//[***]\n10***4***1006") === 14)
	}

	"blob" should "blob" in {
		assert(add("//[=#][=]\n10=4=#1006") === 14)
	}

	"given non-negative integers separated with multiple multi-character custom separators" should "return the sum of all numbers less than 1001" in {
		assert(add("//[***][---]\n10---4***1006") === 14)
	}

	"given numbers with at least one negative" should "throw an exception with the negatives in the exception message" in {
		val ex = intercept[IllegalArgumentException] {
			add("-1,2,-3,4")
		}

		assert(ex.getMessage === "-1,-3")
	}
}
