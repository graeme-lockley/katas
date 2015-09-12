package kata

import java.lang.Integer.parseInt
import java.util.regex.Pattern.quote

object SC {
	val SingleCharacterSeparator = "//(.)\n(.*)$".r
	val MultiCharacterSeparator = "//\\[(.+)\\]\n(.*)$".r

	def add(input: String) = {
		val nums = parse(input)

		if (nums.exists(_ < 0)) 
			throw new IllegalArgumentException(nums.filter(_ < 0).mkString(",")) 
		else 
			nums.filter(_ < 1001).sum
	}

	private def parse(input: String) = parseToStringArray(input).map(parseInt)

	private def parseToStringArray(input: String) = input match {
		case "" => Array("0") 
		case SingleCharacterSeparator(sep, vals) => vals.split(quote(sep))
		case MultiCharacterSeparator(seps, vals) => vals.split(seps.split("\\]\\[").map(quote).mkString("|"))
		case _ => input.split(",|\n")
	}
}
