package kata

import kata.SC.add
import org.scalatest.FlatSpec

class SCTest extends FlatSpec {
	"given an empty string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return its value" in {
		assert(add("123") === 123)
	}

	"given non-negative numbers separated with a comma or newline" should "return the sum of all numbers less than or equal to 1000" in {
		assert(add("1,2\n1003") === 3)
	}

	"given non-negative numbers separated with a single character custom separator" should "return the sum of all numbers less than or equal to 1000" in {
		assert(add("//;\n1;2;1003") === 3)
	}

	"given non-negative numbers separated with a single regex character custom separator" should "return the sum of all numbers less than or equal to 1000" in {
		assert(add("//*\n1*2*1003") === 3)
	}

	"given non-negative numbers separated with a single multi-character custom separator" should "return the sum of all numbers less than or equal to 1000" in {
		assert(add("//[;;;]\n1;;;2;;;1003") === 3)
	}

	"given non-negative numbers separated with a multiple multi-character custom separator" should "return the sum of all numbers less than or equal to 1000" in {
		assert(add("//[;;;][*=*]\n1*=*2;;;1003") === 3)
	}

	"given numbers with at least one negative number" should "throw an exception with the negatives in the exception message" in {
		val ex = intercept[IllegalArgumentException] {
			add("1,-2,3,-4")
		}
		assert(ex.getMessage === "-2,-4")
	}
}
