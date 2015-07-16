package kata;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kata.Gen.forAll;
import static kata.SC.add;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SCTest {
    private Generator<Integer> integers = new IntegerInRange(-2000, 2000);
    private Generator<Integer> nonNegativeIntegers = new IntegerInRange(0, 2000);
    private Generator<List<Integer>> nonEmptyListOfIntegers = new NonEmptyListOf<>(integers);
    private Generator<List<Integer>> nonEmptyListOfNonNegativeIntegers = new NonEmptyListOf<>(nonNegativeIntegers);
    private Generator<Character> separators = new FilterGenerator<Character>(new CharacterGenerator()) {
        @Override
        protected boolean filter(Character result) {
            return Character.isDigit(result) || result == '-' || result == '[' || result == ']' || result == (char) 0;
        }
    };
    private Generator<List<String>> nonEmptyListOfStringSeparators = new NonEmptyListOf<>(new AppendGenerator(new NonEmptyListOf<>(separators)));

    @Test
    public void given_a_blank_should_return_0() throws Exception {
        assertEquals(0, add(""));
    }

    @Test
    public void given_a_non_negative_integer_should_return_its_value_if_less_than_1001_otherwise_0() throws Exception {
        forAll(nonNegativeIntegers, new Function<Integer>() {
            public void test(Integer n) throws Exception {
                assertEquals(normaliseInt(n), add(n.toString()));
            }
        });
    }

    @Test
    public void given_non_negative_integers_separated_with_a_comma_or_newline_should_return_the_sum_of_all_values_less_than_1001() throws Exception {
        forAll(nonEmptyListOfNonNegativeIntegers, new Function<List<Integer>>() {
            public void test(List<Integer> ns) throws Exception {
                assertEquals(sum(ns), add(mkString(ns, Arrays.asList(",", "\n"))));
            }
        });
    }

    @Test
    public void given_non_negative_integers_separated_by_a_custom_single_character_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(nonEmptyListOfNonNegativeIntegers, separators, new Function2<List<Integer>, Character>() {
            public void test(List<Integer> ns, Character sep) throws Exception {
                assertEquals(sum(ns), add("//" + sep + "\n" + mkString(ns, sep.toString())));
            }
        });
    }

    @Test
    public void given_non_negative_integers_separated_by_multiple_multi_character_separator_should_return_the_sum_of_all_less_than_1001() throws Exception {
        forAll(nonEmptyListOfNonNegativeIntegers, nonEmptyListOfStringSeparators, new Function2<List<Integer>, List<String>>() {
            public void test(List<Integer> ns, List<String> seps) throws Exception {
                assertEquals(sum(ns), add("//[" + mkString(seps, "][") + "]\n" + mkString(ns, seps)));
            }
        });
    }

    @Test
    public void given_integers_with_at_least_one_negative_should_throw_an_exception_with_the_negative_numbers_in_the_exception_message() throws Exception {
        forAll(new FilterGenerator<List<Integer>>(nonEmptyListOfIntegers) {
            @Override
            protected boolean filter(List<Integer> ns) {
                for (int n : ns) {
                    if (n < 0) {
                        return false;
                    }
                }
                return true;
            }
        }, new Function<List<Integer>>() {
            @Override
            public void test(List<Integer> ns) throws Exception {
                try {
                    add(mkString(ns, ","));
                    fail();
                } catch (IllegalArgumentException ex) {
                    StringBuilder sb = new StringBuilder();
                    for (int n : ns) {
                        if (n < 0) {
                            sb.append(",").append(n);
                        }
                    }
                    assertEquals(sb.substring(1), ex.getMessage());
                }
            }
        });
    }

    private <T> String mkString(List<T> ns, String separator) {
        return mkString(ns, Collections.singletonList(separator));
    }

    private <T> String mkString(List<T> ns, List<String> separators) {
        Generator<Integer> indexGenerator = new IntegerInRange(0, separators.size());
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder();
        for (T n : ns) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(separators.get(indexGenerator.next()));
            }
            sb.append(n);
        }
        return sb.toString();
    }

    private int sum(List<Integer> ns) {
        int result = 0;
        for (int n : ns) {
            result += normaliseInt(n);
        }
        return result;
    }

    private int normaliseInt(int n) {
        return n < 1001 ? n : 0;
    }
}

