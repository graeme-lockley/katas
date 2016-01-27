package kata;

import org.junit.Test;

import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SCTest {
    @Test
    public void give_a_blank_string_should_return_0() throws Exception {
        assertEquals(0, add(""));
    }

    @Test
    public void given_an_integer_should_return_its_value() throws Exception {
        assertEquals(123, add("123"));
    }

    @Test
    public void given_integers_separated_with_a_comma_should_return_the_sum_of_all_less_than_1001() throws Exception {
        assertEquals(6, add("1,2,3,2001"));
    }

    @Test
    public void given_integers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_than_1001() throws Exception {
        assertEquals(6, add("1,2\n2001\n3"));
    }

    @Test
    public void given_integers_separated_with_a_single_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        assertEquals(6, add("//=\n1=2=2001=3"));
    }
    
    @Test
    public void given_integers_separated_with_a_single_multicharacter_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        assertEquals(6, add("//[==]\n1==2==2001==3"));
    }

    @Test
    public void given_integers_separated_with_multiple_multicharacter_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        assertEquals(6, add("//[==][**]\n1**2==2001**3"));
    }

    @Test
    public void given_integers_with_a_negative_number_should_throw_an_exception_with_the_negatives_in_the_exception_message() throws Exception {
        try {
            add("1,-2,3,-4");
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("-2, -4", ex.getMessage());
        }
    }
}
