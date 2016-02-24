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
		val SingleCharacterPattern = "//(.)\n(.+)".r
		val MultiCharacterPattern = "//\\[(.+)\\]\n(.+)".r
	
		input match {
			case "" => split("0", Array(","))
			case SingleCharacterPattern(sep, nums) => split(nums, Array(sep))
			case MultiCharacterPattern(seps, nums) => split(nums, seps.split("\\]\\["))
			case _ => split(input, Array(",", "\n"))
		}
	}

	private def split(content: String, seps: Array[String]): Array[String] =
		content.split(seps.map(quote).mkString("|"))
}
