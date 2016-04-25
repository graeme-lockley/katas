package kata

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class SCTest {
    @Test fun given_a_blank_string_should_return_0() {
        assertEquals(0, add(""))
    }

    @Test fun given_a_number_should_return_its_value() {
        assertEquals(5, add("5"))
    }

    @Test fun given_numbers_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("1,2,1001,3"))
    }

    @Test fun given_numbers_separated_with_comma_or_newline_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("1,2\n3"))
    }

    @Test fun given_numbers_separated_with_a_single_character_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("//;\n1;2001;2;23334;3"))
    }

    @Test fun given_numbers_separated_with_a_single_regex_character_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("//*\n1*2*30301*3"))
    }

    @Test fun given_numbers_with_at_least_one_negative_show_throw_an_exception_with_the_negatives_in_the_exception_message() {
        try {
            add("1,-2,3,-4")
            fail()
        } catch(e: IllegalArgumentException) {
            assertEquals("-2,-4", e.message)
        }
    }

    @Test fun given_number_separated_with_a_single_multi_character_separator_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("//[***]\n1***2001***2***23334***3"))
    }

    @Test fun given_number_separated_with_multiple_multi_character_separator_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("//[***][===]\n1===2001***2===23334***3"))
    }
}
