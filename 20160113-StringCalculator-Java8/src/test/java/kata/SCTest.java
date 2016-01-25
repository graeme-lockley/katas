package kata;

import org.junit.Test;
import za.co.no9.pbt.CharacterGenerator;
import za.co.no9.pbt.Generator;
import za.co.no9.pbt.IntegerGenerator;
import za.co.no9.pbt.OneOfGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static za.co.no9.pbt.Gen.forAll;

public class SCTest {
    private static final Generator<Integer> INTEGERS = IntegerGenerator.from(-2000, 2000);
    private static final Generator<Integer> NON_NEGATIVE_INTEGERS = IntegerGenerator.from(0, 2000);
    private static final Generator<Character> SEPARATORS = CharacterGenerator.from((char) 33, (char) 127).filter(c -> !Character.isDigit(c) && c != '-' && c != '[' && c != ']');
    private static final Generator<String> STRING_SEPARATORS = SEPARATORS.nonEmptyList().asString();

    @Test
    public void given_an_empty_string_should_return_0() throws Exception {
        assertEquals(0, add(""));
    }

    @Test
    public void given_a_non_negative_integer_should_return_its_value_if_less_than_1001_otherwise_0() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS, n -> assertEquals(normalize(n), add(n.toString())));
    }

    @Test
    public void given_non_negative_integers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), ns -> assertEquals(sum(ns), add(join(ns, Arrays.asList(",", "\n")))));
    }

    @Test
    public void given_non_negative_integers_separated_with_a_single_character_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), SEPARATORS, (ns, sep) -> assertEquals(sum(ns), add("//" + sep.toString() + "\n" + join(ns, Collections.singletonList(sep.toString())))));
    }

    @Test
    public void given_non_negative_integers_separated_with_multiple_multicharacter_custom_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(NON_NEGATIVE_INTEGERS.nonEmptyList(), STRING_SEPARATORS.nonEmptyList(), (ns, seps) -> assertEquals(sum(ns), add("//[" + seps.stream().collect(joining("][")) + "]\n" + join(ns, seps))));
    }

    @Test
    public void given_integers_with_at_least_one_negative_separated_with_a_comma_should_throw_an_exception_with_the_negatives_in_the_exception_message() throws Exception {
        forAll(INTEGERS.nonEmptyList().filter(ns -> ns.stream().anyMatch(n -> n < 0)), ns -> {
            try {
                add(join(ns, Collections.singletonList(",")));
                fail();
            } catch (IllegalArgumentException ex) {
                assertEquals(ns.stream().filter(n -> n < 0).map(Object::toString).collect(joining(",")), ex.getMessage());
            }
        });
    }

    private String join(List<Integer> ns, List<String> separators) {
        Generator<String> separatorGen = OneOfGenerator.from(separators);

        StringBuilder sb = new StringBuilder();
        for (int n : ns) {
            if (sb.length() > 0) {
                sb.append(separatorGen.next());
            }
            sb.append(n);
        }
        return sb.toString();
    }

    private int sum(List<Integer> ns) {
        return ns.stream().reduce(0, (accumulator, n) -> accumulator + normalize(n));
    }

    private int normalize(int n) {
        return n < 1001 ? n : 0;
    }
}