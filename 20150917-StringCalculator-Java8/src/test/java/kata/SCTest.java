package kata;

import org.junit.Test;

import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SCTest {
    @Test
    public void given_a_blank_should_return_0() throws Exception {
        assertEquals(0, add(""));
    }

    @Test
    public void give_a_number_should_return_its_value() throws Exception {
        assertEquals(123, add("123"));
    }

    @Test
    public void give_numbers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_than_1001() throws Exception {
        assertEquals(6, add("1,2\n3,1002"));
    }

    @Test
    public void give_numbers_separated_with_a_single_character_should_return_the_sum() throws Exception {
        assertEquals(6, add("//*\n1*2*3"));
    }

    @Test
    public void give_numbers_separated_with_a_multiple_multi_character_should_return_the_sum() throws Exception {
        assertEquals(6, add("//[*=][----]\n1----2*=3"));
    }

    @Test
    public void give_numbers_with_at_least_one_negative_should_throw_an_exception_with_all_of_the_negatives_in_the_message() throws Exception {
        try {
            add("1,-2,3,-4");
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("-2,-4", ex.getMessage());
        }
    }
}