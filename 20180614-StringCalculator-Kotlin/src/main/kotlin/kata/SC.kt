package kata


fun add(input: String): Int {
    val numbers =
            tokenize(input).map { it.toInt() }

    if (numbers.any { it < 0 })
        throw IllegalArgumentException(numbers.filter { it < 0 }.joinToString(","))
    else
        return numbers.sum()
}


private fun tokenize(input: String) =
        when {
            input.startsWith("//[") -> {
                val indexOfNewline =
                        input.indexOf("\n")

                val separators =
                        Regex(input
                                .take(indexOfNewline - 1).drop(3)
                                .split("][")
                                .sortedDescending()
                                .joinToString("|") { Regex.escape(it) })

                split(input.drop(indexOfNewline + 1), separators)
            }

            input.startsWith("//") ->
                split(input.drop(4), input[2].toString())

            else ->
                split(input, Regex("[,\n]"))
        }


private fun split(input: String, separators: String) =
        split(input, Regex(Regex.escape(separators)))


private fun split(input: String, separators: Regex) =
        when {
            input.isBlank() ->
                listOf()

            else ->
                input.split(separators)
        }
