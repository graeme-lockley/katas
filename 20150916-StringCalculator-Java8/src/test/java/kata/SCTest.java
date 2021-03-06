package kata;

import org.junit.Test;

import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SCTest {
    @Test
    public void given_an_empty_string_should_return_0() {
        assertEquals(0, add(""));
    }

    @Test
    public void given_an_number_should_return_its_value() {
        assertEquals(123, add("123"));
    }

    @Test
    public void given_a_numbers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_numbers_less_than_1001() {
        assertEquals(6, add("1,2\n3,1003"));
    }

    @Test
    public void given_a_numbers_separated_with_a_single_character_should_return_the_sum() {
        assertEquals(6, add("//*\n1*2*3"));
    }

    @Test
    public void given_a_numbers_separated_with_a_single_multi_character_should_return_the_sum() {
        assertEquals(6, add("//[***]\n1***2***3"));
    }

    @Test
    public void given_a_numbers_separated_with_a_multiple_multi_character_should_return_the_sum() {
        assertEquals(6, add("//[***][===]\n1===2***3"));
    }

    @Test
    public void given_a_numbers_with_at_least_one_negative_should_throw_an_exception_with_all_of_the_negatives_in_the_exception_message() {
        try {
            add("1,-2,3,-4");
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("-2,-4", ex.getMessage());
        }
    }
}
