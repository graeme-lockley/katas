package kata

import kata.SC.add
import org.scalatest.FlatSpec

class SCTest extends FlatSpec {
	"given a blank string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return its value" in {
		assert(add("123") === 123)
	}

	"given non-negative numbers separated with a comma or newline" should "return the sum of all less than 1001" in {
		assert(add("1,2\n1001") === 3)
	}

	"given non-negative numbers separated with a single character separator" should "return the sum of all less than 1001" in {
		assert(add("//;\n1;2;1001") === 3)
	}

	"given non-negative numbers separated with a single regex character separator" should "return the sum of all less than 1001" in {
		assert(add("//*\n1*2*1001") === 3)
	}

	"given non-negative numbers separated with a single multi-character separator" should "return the sum of all less than 1001" in {
		assert(add("//[***]\n1***2***1001") === 3)
	}

	"given non-negative numbers separated with multiple multi-character separators" should "return the sum of all less than 1001" in {
		assert(add("//[***][===]\n1===2***1001") === 3)
	}

	"given numbers with at least a single negative number" should "throw an exeception with the negatives values in the exception message" in {
		val ex = intercept[IllegalArgumentException] {
			add("1,-2,3,-4")
		}
		assert(ex.getMessage === "-2,-4")
	}
}
