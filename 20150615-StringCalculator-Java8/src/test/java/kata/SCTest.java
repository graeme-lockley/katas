package kata;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static kata.Gen.*;
import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SCTest {

    private static final List<String> DEFAULT_SEPARATORS = Arrays.asList(",", "\n");

    @Test
    public void should_return_0_when_passed_an_empty_string() throws Exception {
        assertEquals(add(""), 0);
    }

    @Test
    public void should_return_the_value_given_a_non_negative_integer_if_the_value_is_less_than_1001_otherwise_0() throws Exception {
        forAll(nonNegativeIntegers, n -> {
            int result = n < 1001 ? n : 0;
            assertEquals(add(Integer.toString(n)), result);
        });
    }

    @Test
    public void should_return_the_sum_of_all_less_than_1001_given_comma_or_newline_separated_non_negative_integers() throws Exception {
        forAll(nonEmptyListOf(nonNegativeIntegers), ns -> {
            assertEquals(add(mkString(ns, DEFAULT_SEPARATORS)), sum(ns));
        });
    }

    @Test
    public void should_return_the_sum_of_all_less_than_1001_given_non_negative_integers_separated_by_a_single_custom_character_separator() throws Exception {
        forAll(nonEmptyListOf(nonNegativeIntegers), separatorCharacters, (ns, sep) -> {
            assertEquals(add("//" + sep + "\n" + ns.stream().map(Object::toString).collect(Collectors.joining(sep.toString()))), sum(ns));
        });
    }

    @Test
    public void should_return_the_sum_of_all_less_than_1001_given_non_negative_integers_separated_by_a_multiple_custom_string_separator() throws Exception {
        forAll(nonEmptyListOf(nonNegativeIntegers), nonEmptyListOf(separatorStrings), (ns, seps) -> {
            assertEquals(add("//[" + seps.stream().map(Object::toString).collect(Collectors.joining("][")) + "]\n" + mkString(ns, seps)), sum(ns));
        });
    }

    @Test
    public void should_throw_an_exception_if_a_negative_number_is_passed() throws Exception {
        forAll(nonEmptyListOf(integers), ns -> {
            if (ns.stream().anyMatch(x -> x < 0)) {
                try {
                    add(mkString(ns, DEFAULT_SEPARATORS));
                    fail("Should have thrown an exception");
                } catch (IllegalArgumentException ex) {
                    assertEquals(ns.stream().filter(x -> x < 0).map(Object::toString).collect(Collectors.joining(",")), ex.getMessage());
                }
            }
        });
    }

    private String mkString(List<Integer> ns, List<String> seps) {
        StringBuilder result = null;

        for (int n : ns) {
            if (result == null) {
                result = new StringBuilder();
            } else {
                result.append(oneOf(seps).get());
            }
            result.append(n);
        }
        return result == null ? "" : result.toString();
    }

    private Supplier<Character> separatorCharacters = characters(c -> c >= (char) 32 && c <= (char) 120 && !Character.isDigit(c) && c != '-' && c != '[');

    private Supplier<String> separatorStrings = () -> nonEmptyListOf(separatorCharacters).get().stream().map(Object::toString).collect(Collectors.joining());

    private int sum(List<Integer> ns) {
        return ns.stream().filter(x -> x < 1001).reduce(0, ((x, y) -> x + y));
    }
}
