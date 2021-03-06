package kata

import java.lang.Integer.parseInt
import java.util.regex.Pattern.quote

object SC {
	def add(input: String): Int = {
		val nums = parse(input)

		if (nums.exists(_ < 0))
			throw new IllegalArgumentException(nums.filter(_ < 0).mkString(","))
		else
			nums.filter(_ < 1001).sum
	}

	private def parse(input: String): Array[Int] = tokenize(input).map(parseInt)

	private def tokenize(input: String): Array[String] = {
		val SingleCharacterCustomPattern = "//(.)\n(.+)".r
		val MultipleMultiCharacterCustomPattern = "//\\[(.+)\\]\n(.+)".r

		input match {
			case "" => Array[String]()
			case SingleCharacterCustomPattern(sep, numbers) => numbers.split(quote(sep))
			case MultipleMultiCharacterCustomPattern(seps, numbers) => numbers.split(seps.split("\\]\\[").map(quote).mkString("|"))
			case _ => input.split(",|\n")
		}
	}
}
