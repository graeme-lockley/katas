package kata

import java.util.regex.Pattern.quote as quoteRegex

fun add(input: String): Int {
    val numbers = parse(input)

    if (numbers.any { it < 0 })
        throw IllegalArgumentException(numbers.filter { it < 0 }.joinToString(","))
    else
        return numbers.filter { it < 1001 }.fold(0) { sum, item -> sum + item }
}

private fun parse(input: String): List<Int> = tokenize(input).map { Integer.parseInt(it) }

private fun tokenize(input: String): List<String> =
        when {
            input.isEmpty() -> listOf()
            input.startsWith("//[") -> {
                val indexOfNewline = input.indexOf("\n")
                val numbers = input.substring(indexOfNewline + 1)
                val separator = input.substring(3, indexOfNewline - 1).split("][").map { quoteRegex(it) }.joinToString("|").toRegex()

                numbers.split(separator)
            }
            input.startsWith("//") -> input.substring(4).split(input.substring(2, 3))
            else -> input.split(",|\n".toRegex())
        }
