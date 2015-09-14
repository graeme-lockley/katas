package kata;

import org.junit.Test;

import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SCTest {
    @Test
    public void given_a_blank_string_should_return_0() {
        assertEquals(0, add(""));
    }

    @Test
    public void given_a_non_negative_number_less_than_1001_should_return_its_value() {
        assertEquals(3, add("3"));
    }

    @Test
    public void given_a_non_negative_number_greater_than_1000_should_return_0() {
        assertEquals(0, add("1003"));
    }

    @Test
    public void given_non_negative_numbers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_than_1001() {
        assertEquals(3, add("1,2\n1003"));
    }

    @Test
    public void given_non_negative_numbers_separated_with_a_single_custom_separator_should_return_the_sum_of_all_less_than_1001() {
        assertEquals(3, add("//*\n1*2*1003"));
    }

    @Test
    public void given_non_negative_numbers_separated_with_multiple_multi_character_custom_separator_should_return_the_sum_of_all_less_than_1001() {
        assertEquals(3, add("//[---][@@@@@]\n1---2---1003"));
    }

    @Test
    public void given_a_numbers_with_at_least_one_negative_numbe_should_throw_an_exception_with_all_of_the_negatives_in_the_exception_message() {
        try {
            add("1,-2,3,-4,5");
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("-2,-4", ex.getMessage());
        }
    }
}
