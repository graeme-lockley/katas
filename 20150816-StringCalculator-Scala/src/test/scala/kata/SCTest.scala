package kata

import org.scalatest.FlatSpec
import kata.SC.add

class SCTest extends FlatSpec {
	"given a blank string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return its value" in {
		assert(add("123") === 123)
	}

	"given numbers with at least one non-negative number" should "throw an exception with the negative numbers in the exception message" in {
		val ex =intercept[IllegalArgumentException] {
			add("-123,10,-23")
		}
		assert(ex.getMessage == "-123,-23")
	}

	"given non-negative numbers separated with a comma or newline" should "return the sum of numbers that are less than 1001" in {
		assert(add("10,5\n1010") === 15)
	}

	"given non-negative numbers separated with a custom single character separator" should "return the sum of numbers that are less than 1001" in {
		assert(add("//;\n10;5;1010") === 15)
	}

	"given non-negative numbers separated with a custom single regex character separator" should "return the sum of numbers that are less than 1001" in {
		assert(add("//*\n10*5*1010") === 15)
	}

	"given non-negative numbers separated with multiple custom multiple character separator" should "return the sum of numbers that are less than 1001" in {
		assert(add("//[***][---]\n10---5***1010") === 15)
	}
}
