package kata


fun add(input: String): Int {
    val numbers = parse(input)

    if (numbers.any { it < 0 })
        throw IllegalArgumentException(numbers.filter { it < 0 }.joinToString(", "))
    else
        return numbers.filter { it <= 1000 }.sum()
}


private fun parse(input: String): List<Int> =
        tokenize(input).map { it.toInt() }


private fun tokenize(input: String): List<String> =
        when {
            input.startsWith("//[") -> {
                val indexOfNewline = input.indexOf('\n')
                val separator = input.substring(3, indexOfNewline - 1)
                        .split("][")
                        .sortedBy { -it.length }
                        .map { Regex.escape(it) }
                        .joinToString("|")
                        .toRegex()
                split(input.substring(indexOfNewline + 1), separator)
            }
            input.startsWith("//") -> split(input.substring(4), Regex.escape(input.substring(2, 3)).toRegex())
            else -> split(input, "[,\n]".toRegex())
        }


private fun split(input: String, separator: Regex) =
        when {
            input.isEmpty() -> listOf<String>()
            else -> input.split(separator)
        }
