package kata;

import org.junit.Test;
import za.co.no9.pbt.CharacterGenerator;
import za.co.no9.pbt.Generator;
import za.co.no9.pbt.IntegerGenerator;
import za.co.no9.pbt.OneOfGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static za.co.no9.pbt.Gen.forAll;

public class SCTest {
    private static List<String> DEFAULT_SEPARATORS = Arrays.asList(",", ":");

    private static Generator<Integer> INTEGERS = IntegerGenerator.from(-2000, 2000);
    private static Generator<Integer> NON_NEGATIVE_INTEGERS = IntegerGenerator.from(0, 2000);
    private static Generator<Character> SEPARATORS = CharacterGenerator.from((char) 1, (char) 255).filter(c -> c != '-' && c != '[' && c != ']' && !Character.isDigit(c));

    @Test
    public void given_an_empty_string_should_return_0() throws Exception {
        assertEquals(add(""), 0);
    }

    @Test
    public void given_a_non_negative_number_should_returns_its_value_if_less_than_1001_otherwise_0() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS, n ->
                assertEquals(n < 1001 ? n : 0, add(n.toString())));
    }

    @Test
    public void given_non_negative_numbers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), ns ->
                assertEquals(
                        sum(ns),
                        add(mkString(ns, DEFAULT_SEPARATORS))));
    }

    @Test
    public void given_non_negative_numbers_separated_with_a_single_character_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), SEPARATORS, (ns, sep) ->
                assertEquals(
                        sum(ns),
                        add("//" + sep.toString() + "\n" + mkString(ns, Collections.singletonList(sep.toString())))));
    }

    @Test
    public void given_non_negative_numbers_separated_with_multiple_multicharacter_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), SEPARATORS.nonEmptyList().asString().nonEmptyList(), (ns, seps) ->
                assertEquals(
                        sum(ns),
                        add("//[" + seps.stream().collect(Collectors.joining("][")) + "]\n" + mkString(ns, seps))));
    }

    @Test
    public void given_numbers_with_at_least_one_negative_should_throw_an_exception_with_the_negatives_in_the_exception_message() throws Exception {
        forAll(INTEGERS.nonEmptyList().filter(ns -> ns.stream().anyMatch(n -> n < 0)), ns -> {
            try {
                add(mkString(ns, DEFAULT_SEPARATORS));
                fail();
            } catch (IllegalArgumentException ex) {
                assertEquals(
                        ns.stream().filter(n -> n < 0).map(Object::toString).collect(Collectors.joining(",")),
                        ex.getMessage());
            }
        });
    }

    private int sum(List<Integer> ns) {
        return (int) ns.stream().filter(n -> n < 1001).collect(Collectors.reducing(0, (sum, n) -> sum + n));
    }

    private String mkString(List<Integer> nums, List<String> seps) {
        final Generator<String> separators = OneOfGenerator.from(seps);

        StringBuilder sb = new StringBuilder();
        for (int num : nums) {
            if (sb.length() > 0) {
                sb.append(separators.next());
            }
            sb.append(num);
        }
        return sb.toString();
    }
}
