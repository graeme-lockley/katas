package kata;

import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.regex.Pattern.quote;
import static java.util.stream.Collectors.joining;

public class SC {
    public static int add(String input) {
//        System.out.println("**[" + input + "]**");

        if (parse(input).anyMatch(n -> n < 0)) {
            throw new IllegalArgumentException(parse(input).filter(n -> n < 0).map(Object::toString).collect(joining(",")));
        } else {
            return parse(input).filter(n -> n < 1001).reduce(0, (accumulator, n) -> accumulator + n);
        }
    }

    private static Stream<Integer> parse(String input) {
        if (input.isEmpty()) {
            return Collections.<Integer>emptyList().stream();
        } else if (input.startsWith("//[")) {
            final int indexOfNewline = input.indexOf('\n');

            final String numbers = input.substring(indexOfNewline + 1);
            final String separatorRegex = Stream.of(input.substring(3, indexOfNewline - 1).split("\\]\\[")).sorted((x, y) -> y.length() - x.length()).map(Pattern::quote).collect(joining("|"));

            return tokenize(numbers, separatorRegex);
        } else if (input.startsWith("//")) {
            final String numbers = input.substring(4);
            final String separators = input.substring(2, 3);

            return tokenize(numbers, quote(separators));
        } else {
            return tokenize(input, ",|\n");
        }
    }

    private static Stream<Integer> tokenize(String numbers, String separatorRegex) {
        return Stream.of(numbers.split(separatorRegex)).map(Integer::parseInt);
    }
}
