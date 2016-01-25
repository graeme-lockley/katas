package kata;

import org.junit.Test;
import za.co.no9.pbt.CharacterGenerator;
import za.co.no9.pbt.Generator;
import za.co.no9.pbt.IntegerGenerator;
import za.co.no9.pbt.OneOfGenerator;

import java.util.Arrays;
import java.util.List;

import static java.lang.Character.isDigit;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertEquals;
import static kata.SC.add;
import static za.co.no9.pbt.Gen.forAll;

public class SCTest {
    private static final Generator<Integer> INTEGERS = IntegerGenerator.from(-5000, 5000);
    private static final Generator<Character> SEPARATORS = CharacterGenerator.from((char) 1, (char) 255).filter(c -> !isDigit(c));

    @Test
    public void given_an_empty_string_should_return_0() throws Exception {
        assertEquals(0, add(""));
    }

    @Test
    public void given_a_number_should_return_its_value() throws Exception {
        forAll(INTEGERS, n -> {
            assertEquals((int) n, add(n.toString()));
        });
    }

    @Test
    public void given_numbers_separated_with_a_comma_or_newline_should_return_the_sum() throws Exception {
        forAll(INTEGERS.nonEmptyList(), ns -> {
            assertEquals(sum(ns), add(mkString(ns, Arrays.asList(",", "\n"))));
        });
    }

    @Test
    public void given_numbers_separated_with_a_single_character_custom_separator_should_return_the_sum() throws Exception {
        forAll(INTEGERS.nonEmptyList(), SEPARATORS, (ns, sep) -> {
            assertEquals(sum(ns), add("//" + sep + "\n" + mkString(ns, singletonList(sep.toString()))));
        });
    }

    private String mkString(List<Integer> ns, List<String> seps) {
        Generator<String> sepsGen = OneOfGenerator.from(seps);

        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for (Integer n : ns) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(sepsGen.next());
            }
            result.append(n);
        }
        return result.toString();
    }

    private int sum(List<Integer> ns) {
        return ns.stream().reduce(0, (sum, n) -> sum + n);
    }
}
