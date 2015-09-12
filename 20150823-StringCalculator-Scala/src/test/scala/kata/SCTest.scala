package kata

import org.scalatest.FlatSpec
import kata.SC.add

class SCTest extends FlatSpec {
	"given an empty string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number less than 1001" should "return its value" in {
		assert(add("123") === 123)
	}

	"given a number greater than 1000" should "return 0" in {
		assert(add("1234") === 0)
	}

	"given non-negative numbers separated with a comma or newline" should "return the sum of all numbers less than 1001" in {
		assert(add("1,2\n1003") === 3)
	}

	"given non-negative numbers separated with a single character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//;\n1;2;1003") == 3)
	}

	"given non-negative numbers separated with a single regex character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//*\n1*2*1003") == 3)
	}

	"given numbers with at least a single negative" should "throw an exception with all of the negative numbers in the message" in {
		val ex = intercept[IllegalArgumentException] {
			add("-1,2,-3,4")
		}
		assert(ex.getMessage === "-1,-3")
	}

	"given non-negative numbers separated with multiple multi-character separator" should " return the sum of all numbers less than 1001" in {
		assert(add("//[---][***]\n1***2---1003") === 3)
	}
}
