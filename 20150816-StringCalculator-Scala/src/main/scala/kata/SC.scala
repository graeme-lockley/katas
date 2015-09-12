package kata

import java.lang.Integer.parseInt
import java.util.regex.Pattern.quote

object SC {
	private val SingleCharacterSeparator = "//(.)\n(.*)".r
	private val MultiCharacterSeparator = "//\\[(.+)\\]\n(.*)".r

	def add(input: String): Int = {
		val nums = parse(input)

		if (nums.exists(_ < 0)) 
			throw new IllegalArgumentException(nums.filter(_ < 0).mkString(","))
		else
			nums.filter(_ < 1001).sum
	}

	private def parse(input: String): Array[Int] = parseText(input).map(parseInt)

	private def parseText(input: String): Array[String] = input match {
		case "" => Array() 
		case SingleCharacterSeparator(sep, nums) => nums.split(quote(sep))
		case MultiCharacterSeparator(seps, nums) => nums.split(seps.split("\\]\\[").map(quote).mkString("|"))
		case _ => input.split(",|\n")
	}
}
