package kata

import org.scalatest.FlatSpec
import kata.SC.add

class SCTest extends FlatSpec {
	"given an empty string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return its value" in {
		assert(add("123") === 123)
	}

	"given a number greater than 1000" should "return 0" in {
		assert(add("12345") === 0)
	}

	"given non-negative numbers separated with a comma or newline" should "return the sum of all numbers less than 1001" in {
		assert(add("1,2\n1002") === 3)
	}

	"given non-negative numbers separated with a single character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//;\n1;2;1002") === 3)
	}

	"given non-negative numbers separated with a single regex character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//*\n1*2*1002") === 3)
	}

	"given non-negative numbers separated with a single multi-character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//[***]\n1***2***1002") === 3)
	}

	"given non-negative numbers separated with multiple multi-character custom separator" should "return the sum of all numbers less than 1001" in {
		assert(add("//[***][---]\n1---2***1002") === 3)
	}

	"given numbers with at least on negative" should "throw an exception with the negative numbers in the exception message" in {
		val ex = intercept[IllegalArgumentException] {
			add("-1,2,-3,4")
		}
		assert(ex.getMessage === "-1,-3")
	}
}