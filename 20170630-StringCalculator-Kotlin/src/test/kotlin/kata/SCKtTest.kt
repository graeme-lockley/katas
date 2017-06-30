package kata

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.fail


typealias Generator<T> = () -> T


fun randomInt(min: Int, max: Int): Generator<Int> =
        fun() = min + (Math.random() * (max - min) + 0.5).toInt()


fun <T> forall(gen: Generator<T>, test: (T) -> Unit) {
    for (lp: Int in 1..1_000)
        test(gen())
}


fun <T, U> forall(genT: Generator<T>, genU: Generator<U>, test: (T, U) -> Unit) {
    for (lp: Int in 1..1_000)
        test(genT(), genU())
}


fun <T> listOfGenerator(size: Generator<Int>, element: Generator<T>): Generator<List<T>> =
        fun() = (1..size()).map { element() }


fun <T> oneOfGenerator(items: List<T>): Generator<T> =
        fun() = items[randomInt(0, items.size - 1)()]

fun <T> filterGenerator(gen: Generator<T>, test: (T) -> Boolean): Generator<T> =
        fun(): T {
            val value = gen()
            return if (test(value)) {
                value
            } else {
                filterGenerator(gen, test)()
            }
        }

fun <S, T> mapGenerator(gen: Generator<S>, f: (S) -> T): Generator<T> =
        fun() = f(gen())


class SCKtTest {
    val INTEGERS =
            randomInt(-1_100, 1_100)

    val NON_NEGATIVE_INTEGER =
            randomInt(0, 1_100)

    val LIST_OF_INTEGERS =
            listOfGenerator(randomInt(0, 10), INTEGERS)

    val LIST_OF_NON_NEGATIVE_INTEGERS =
            listOfGenerator(randomInt(0, 10), NON_NEGATIVE_INTEGER)

    val LIST_OF_INTEGERS_WITH_A_NEGATIVE =
            filterGenerator(LIST_OF_INTEGERS, fun(values) = values.any { it < 0 })

    val SEPARATORS =
            oneOfGenerator((0..65_535).map { it.toChar() }.filter { !Character.isDigit(it) && it != '-' && it != '[' && it != 10.toChar() }.map { it.toString() })

    val MULTIPLE_SEPARATORS =
            mapGenerator(listOfGenerator(randomInt(1, 10), SEPARATORS), fun(seps) = seps.joinToString(""))

    val LIST_OF_MULTIPLE_SEPARATORS =
            listOfGenerator(randomInt(1, 10), MULTIPLE_SEPARATORS)

    @Test
    fun given_a_blank_string_should_return_0() {
        assertEquals(0, add(""))
    }

    @Test
    fun given_a_non_negative_integer_should_return_its_value_if_less_equal_1000_else_0() {
        forall(NON_NEGATIVE_INTEGER) {
            val result = if (it <= 1000) it else 0
            assertEquals(result, add(it.toString()))
        }
    }


    @Test
    fun given_non_negative_integers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_equal_1000() {
        forall(LIST_OF_NON_NEGATIVE_INTEGERS) {
            val input = mkString(it, oneOfGenerator(listOf(",", "\n")))
            assertEquals(it.filter { it <= 1000 }.sum(), add(input))
        }
    }


    @Test
    fun given_non_negative_integers_separated_with_a_single_custom_character_should_return_the_sum_of_all_less_equal_1000() {
        forall(LIST_OF_NON_NEGATIVE_INTEGERS, SEPARATORS) { values, separator ->
            val input = "//" + separator + "\n" + values.joinToString(separator)
            assertEquals(values.filter { it <= 1000 }.sum(), add(input))
        }
    }


    @Test
    fun given_non_negative_integers_separated_with_multiple_custom_multiple_character_should_return_the_sum_of_all_less_equal_1000() {
        forall(LIST_OF_NON_NEGATIVE_INTEGERS, LIST_OF_MULTIPLE_SEPARATORS) { values, separators ->
            val input = "//[" + separators.joinToString("][") + "]\n" + mkString(values, oneOfGenerator(separators))
            assertEquals(values.filter { it <= 1000 }.sum(), add(input))
        }
    }


    @Test
    fun given_integers_with_at_least_one_negative_should_throw_an_exception() {
        forall(LIST_OF_INTEGERS_WITH_A_NEGATIVE) {
            try {
                add(it.joinToString(","))
                fail("Exception expected")
            } catch (e: IllegalArgumentException) {
                assertEquals(it.filter { it < 0 }.joinToString(", "), e.message)
            }
        }
    }


    fun mkString(values: List<Int>, separator: Generator<String>): String =
            when {
                values.isEmpty() -> ""
                values.size == 1 -> values[0].toString()
                else -> values[0].toString() + separator() + mkString(values.drop(1), separator)
            }
}
