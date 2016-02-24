package kata

import kata.SC.add

class SCTest extends org.scalatest.FlatSpec {
	"given a blank string" should "return 0" in {
		assert(add("") === 0)
	}

	"given a number" should "return its value" in {
		assert(add("123") == 123)
	}

	"given multiple numbers separated with a comma" should "return the sum of all numbers less than 1001" in {
		assert(add("1,1001,2,2001,3,3001") == 6)
	}

	"given multiple numbers separated with a comma and newline" should "return the sum" in {
		assert(add("1,2\n3") == 6)
	}

	"given numbers separated with a custom single character separator" should "return the sum" in {
	  assert(add("//;\n1;2;3") == 6)
	}

	"given numbers separated with a custom single regex character separator" should "return the sum" in {
	  assert(add("//*\n1*2*3") == 6)
	}

	"given numbers separated with a custom multi-character separator" should "return the sum" in {
	  assert(add("//[**]\n1**2**3") == 6)
	}

	"given numbers separated with multiple custom multi-character separator" should "return the sum" in {
	  assert(add("//[**][---]\n1---2**3") == 6)
	}

	"given numbers with at least one negative" should "throw an exception with the negatives in the exception message" in {
		val ex = intercept[java.lang.IllegalArgumentException] {
			add("-1,2,-3,4,-5")
		}
		assert(ex.getMessage === "-1,-3,-5")
	}
}
