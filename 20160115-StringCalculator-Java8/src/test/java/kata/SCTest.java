package kata;

import org.junit.Test;
import za.co.no9.pbt.CharacterGenerator;
import za.co.no9.pbt.Generator;
import za.co.no9.pbt.IntegerGenerator;
import za.co.no9.pbt.OneOfGenerator;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static za.co.no9.pbt.Gen.forAll;

public class SCTest {
    private Generator<Integer> INTEGERS = IntegerGenerator.from(-2000, 2000);
    private Generator<Integer> NON_NEGATIVE_INTEGERS = IntegerGenerator.from(0, 2000);
    private Generator<Character> SEPARATORS = CharacterGenerator.from((char) 0, (char) 255).filter(c -> !Character.isDigit(c) && c != '-' && c != '\n' && c != '[');
    private Generator<String> STRING_SEPARATORS = SEPARATORS.nonEmptyList().asString();

    @Test
    public void given_a_blank_string_returns_0() throws Exception {
        assertEquals(0, add(""));
    }

    @Test
    public void given_a_non_negative_integer_should_return_its_value_if_less_than_1001_otherwise_0() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS, n ->
                        assertEquals(normalise(n), add(n.toString()))
        );
    }

    @Test
    public void given_non_negative_integers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), ns ->
                        assertEquals(expectedSum(ns), add(join(ns, asList(",", "\n"))))
        );
    }

    @Test
    public void given_non_negative_integers_separated_with_a_single_character_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), SEPARATORS, (ns, sep) ->
                        assertEquals(expectedSum(ns), add("//" + sep.toString() + "\n" + join(ns, singletonList(sep.toString()))))
        );
    }

    @Test
    public void given_non_negative_integers_separated_with_multiple_multicharacter_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), STRING_SEPARATORS.nonEmptyList(), (ns, seps) ->
                        assertEquals(expectedSum(ns), add("//[" + seps.stream().collect(Collectors.joining("][")) + "]\n" + join(ns, seps)))
        );
    }

    @Test
    public void given_integers_with_at_least_one_negative_should_throw_an_exception_with_all_of_the_negatives_in_the_exception_message() throws Exception {
        forAll(INTEGERS.nonEmptyList().filter(ns -> ns.stream().anyMatch(n -> n < 0)), ns -> {
            try {
                add(join(ns, asList(",", "\n")));
                fail();
            } catch (IllegalArgumentException ex) {
                assertEquals(ns.stream().filter(n -> n < 0).map(Object::toString).collect(Collectors.joining(",")), ex.getMessage());
            }
        });
    }

    private String join(List<Integer> ns, List<String> separators) {
        Generator<String> separatorGenerator = OneOfGenerator.from(separators);
        StringBuilder sb = new StringBuilder();

        for (int n : ns) {
            if (sb.length() > 0) {
                sb.append(separatorGenerator.next());
            }
            sb.append(n);
        }
        return sb.toString();
    }

    private int expectedSum(List<Integer> ns) {
        return ns.stream().reduce(0, (accumulator, n) -> accumulator + normalise(n));
    }

    private int normalise(int n) {
        return n < 1001 ? n : 0;
    }
}
