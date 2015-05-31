package kata

import java.util.regex.Pattern.quote

object SC {
	val SingleSeparator = "//(.)\n(.*)".r
	val MultiSeparator = "//\\[(.*)\\]\n(.*)".r

	def add(input: String): Int = {
		val ns = (input match {
			case "" => Array[String]()
			case SingleSeparator(sep, nums) => nums.split(quote(sep))
			case MultiSeparator(seps, nums) => nums.split(seps.split("\\]\\[").sorted.reverseMap(quote).mkString("|"))
			case _ => input.split(",|\n")
		}).map(_.toInt)

		if (ns.exists(_ < 0)) throw new IllegalArgumentException(ns.filter(_ < 0).mkString(",")) else ns.filter(_ < 1001).sum
	}
}
