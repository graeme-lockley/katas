package kata

import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec


val ListOfIntegers =
        Gen.list(Gen.int())

val NonNegativeIntegers =
        Gen.nats()

val ListOfNonNegativeIntegers =
        Gen.list(NonNegativeIntegers)

val Separators =
        Gen.choose(0, 127).map { it.toChar() }.filter { !it.isDigit() && it != '[' }

val StringSeparators =
        Gen.string().filter { it.isNotBlank() && it.all { !it.isDigit() && it != '[' } }

val ListOfStringSeparators =
        Gen.list(StringSeparators).filter { it.isNotEmpty() }


class SCTest : StringSpec({
    "blank should return 0" {
        add("").shouldBe(0)
    }

    "given a non-negative integer should return its value" {
        assertAll(NonNegativeIntegers) { n ->
            add(n.toString()).shouldBe(n)
        }
    }

    "given non-negative integers separated with a comma or newline should return the sum" {
        assertAll(ListOfNonNegativeIntegers) { ns ->
            add(ns.mkString(listOf(",", "\n"))).shouldBe(ns.sum())
        }
    }

    "given non-negative integers separated with a single custom character should return the sum" {
        assertAll(ListOfNonNegativeIntegers, Separators) { ns, sep ->
            add("//$sep\n${ns.joinToString(sep.toString())}").shouldBe(ns.sum())
        }
    }

    "given non-negative integers separated with multiple multiple character should return the sum" {
        assertAll(ListOfNonNegativeIntegers, ListOfStringSeparators) { ns, seps ->
            add("//[${seps.joinToString("][")}]\n${ns.mkString(seps)}").shouldBe(ns.sum())
        }
    }

    "given integers with at least one negative should throw an exception with all of the negatives in the message" {
        assertAll(ListOfIntegers.filter { it.any { it < 0 } }) { ns ->
            val exception = shouldThrow<IllegalArgumentException> { add(ns.joinToString(",")) }

            exception.message.shouldBe(ns.filter { it < 0 }.joinToString(","))
        }
    }
})


fun List<Int>.mkString(separators: List<String>): String =
        this.mkString(Gen.from(separators))

fun List<Int>.mkString(separators: Gen<String>): String =
        when {
            this.isEmpty() ->
                ""

            this.size == 1 ->
                this[0].toString()

            else ->
                this[0].toString() + separators.random().first() + this.drop(1).mkString(separators)
        }
