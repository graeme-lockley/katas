package kata

import kata.SC.add

class SCTest extends org.scalatest.FlatSpec {
	"given an empty string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return its value" in {
		assert(add("123") === 123)
	}

	"given multiple numbers" should "return the sum of all numbers less than 1001" in {
		assert(add("1,1001,2,2001,3,3000") === 6)
	}

	"given multiple numbers separated with a comma or newline" should "return the sum" in {
		assert(add("1,2\n3") === 6)
	}

	"given multiple numbers separated with a single multi-character custom separator" should "return the sum" in {
		assert(add("//[***]\n1***2***3") === 6)
	}

	"given multiple numbers separated with multiple multi-character custom separator" should "return the sum" in {
		assert(add("//[***][===]\n1===2***3") === 6)
	}

	"given multiple numbers separated with a single character custom separator" should "return the sum" in {
		assert(add("//;\n1;2;3") === 6)
	}

	"given multiple numbers separated with a single regular expression character custom separator" should "return the sum" in {
		assert(add("//*\n1*2*3") === 6)
	}

	"given multiple numbers with at least one negative" should "throw an exception with all of the negatives in the exception message" in {
		val ex = intercept[java.lang.IllegalArgumentException] {
			add("-1,2,-3,4,-5")
		}
		assert(ex.getMessage === "-1,-3,-5")
	}
}
