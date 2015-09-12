package kata

import kata.SC.add
import org.scalatest.FlatSpec

class SCTest extends FlatSpec {
	"given an empty string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return is value" in {
		assert(add("123") === 123)
	}

	"given a number greater than 1000" should "return 0" in {
		assert(add("122233") === 0)
	}

	"given non-negative numbers separated with a comma or newline" should "return the sum of all numbers less than 1001" in {
		assert(add("1,3\n1001") === 4)
	}

	"given non-negative numbers separated with a single character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//;\n1;3;1001") === 4)
	}

	"given non-negative numbers separated with a single regex character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//*\n1*3*1001") === 4)
	}

	"given non-negative numbers separated with multiple multi-character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//[;;;][***]\n1***3;;;1001") === 4)
	}

	"given numbers with at least one negative number" should "throw an exception with the negative numbers in the exception message" in {
		val ex = intercept[IllegalArgumentException] {
			add("-1,2,-3,4")
		}
		assert(ex.getMessage === "-1,-3")
	}
}
