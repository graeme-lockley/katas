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

import static java.lang.Character.isDigit;
import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static za.co.no9.pbt.Gen.forAll;

public class SCTest {
    private static Generator<Integer> INTEGERS = IntegerGenerator.from(-2000, 2000);
    private static Generator<Integer> NON_NEGATIVE_INTEGERS = IntegerGenerator.from(0, 2000);
    private static Generator<Character> SEPARATORS = CharacterGenerator.from((char) 1, (char) 255).filter(c -> !isDigit(c) && c != '[' && c != ']');

    @Test
    public void given_an_empty_string_should_return_0() {
        assertEquals(0, add(""));
    }

    @Test
    public void given_a_non_negative_integer_should_return_its_value_if_less_than_1001_otherwise_0() {
        forAll(NON_NEGATIVE_INTEGERS, n ->
                        assertEquals(normaliseInt(n), add(n.toString()))
        );
    }

    @Test
    public void given_non_negative_integers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_numbers_less_than_1001() {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), ns ->
                        assertEquals(
                                sum(ns),
                                add(mkString(ns, Arrays.asList(",", "\n"))))
        );
    }

    @Test
    public void given_non_negative_integers_separated_with_a_single_character_custom_separator_should_return_the_sum_of_all_numbers_less_than_1001() {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), SEPARATORS, (ns, sep) ->
                        assertEquals(
                                sum(ns),
                                add("//" + sep + "\n" + mkString(ns, Collections.singletonList(sep.toString()))))
        );
    }

    @Test
    public void given_non_negative_integers_separated_with_multiple_multi_character_custom_separators_should_return_the_sum_of_all_numbers_less_than_1001() {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), SEPARATORS.nonEmptyList().asString().nonEmptyList(), (ns, seps) ->
                        assertEquals(
                                sum(ns),
                                add("//[" + seps.stream().collect(Collectors.joining("][")) + "]\n" + mkString(ns, seps)))
        );
    }

    @Test
    public void given_numbers_with_at_least_one_negative_should_throw_an_exception_with_the_negative_numbers_in_the_exception_message() {
        forAll(INTEGERS.nonEmptyList().filter(ns -> ns.stream().anyMatch(n -> n < 0)), ns -> {
                    try {
                        add(mkString(ns, Collections.singletonList(",")));
                        fail();
                    } catch (IllegalArgumentException ex) {
                        assertEquals(ns.stream().filter(n -> n < 0).map(Object::toString).collect(Collectors.joining(",")), ex.getMessage());
                    }
                }
        );
    }

    private int sum(List<Integer> ns) {
        return ns.stream().reduce(0, (sum, n) -> sum + normaliseInt(n));
    }

    private int normaliseInt(int n) {
        return n < 1001 ? n : 0;
    }

    private String mkString(List<Integer> ns, List<String> separators) {
        final OneOfGenerator<String> stringOneOfGenerator = OneOfGenerator.from(separators);

        final StringBuilder result = new StringBuilder();
        for (int n : ns) {
            if (result.length() > 0) {
                result.append(stringOneOfGenerator.next());
            }
            result.append(n);
        }
        return result.toString();
    }
}
