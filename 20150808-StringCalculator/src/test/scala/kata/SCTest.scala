package kata

import org.scalatest.FlatSpec
import kata.SC.add

class SCTest extends FlatSpec {
	"An empty string" should "return 0" in {
		assert(add("") === 0)
	}

	"An number" should "return its value" in {
		assert(add("4") === 4)
	}

	"A number larger than 1000" should "return 0" in {
		assert(add("1001") === 0)
	}

	"Many numbers separated by a comman with at least one negative number" should "throw an exception with the all the negative numbers in the exception message" in {
		val ex = intercept[IllegalArgumentException] {
			add("-1,2,-3,4,-5")
		}
		assert(ex.getMessage === "-1,-3,-5")
	}


	"Non-negative numbers separated by a comma" should "return the sum of all less than 1001" in {
		assert(add("10,2,1003") === 12)
	}

	"Non-negative numbers separated by a comma and/or newline" should "return the sum of all less than 1001" in {
		assert(add("10,2\n1003") === 12)
	}

	"Non-negative numbers separated by a single character custome separator" should "return the sum of all less than 1001" in {
		assert(add("//;\n10;2;1003") == 12)
	}

	"Non-negative numbers separated by a single regular expression character custome separator" should "return the sum of all less than 1001" in {
		assert(add("//*\n10*2*1003") == 12)
	}

	"Non-negative numbers separated by multiple multi-character custome separator" should "return the sum of all less than 1001" in {
		assert(add("//[---][===]\n10===2---1003") == 12)
	}
}
