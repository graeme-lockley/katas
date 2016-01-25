package kata;

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
            return sum(parse(input).filter(n -> n < 1001));
        }
    }

    public static Stream<Integer> parse(String input) {
        if (input.isEmpty()) {
            return tokenize("0", ",");
        } else if (input.startsWith("//[")) {
            final int indexOfNewline = input.indexOf('\n');
            final String numbers = input.substring(indexOfNewline + 1);
            final String separators = input.substring(3, indexOfNewline - 1);

            return tokenize(numbers, Stream.of(separators.split("\\]\\[")).sorted((x, y) -> y.length() - x.length()).map(Pattern::quote).collect(joining("|")));
        } else if (input.startsWith("//")) {
            final String numbers = input.substring(4);
            final String separator = input.substring(2, 3);

            return tokenize(numbers, quote(separator));
        } else {
            return tokenize(input, ",|\n");
        }
    }

    private static Stream<Integer> tokenize(String numbers, String regexSeparator) {
        return Stream.of(numbers.split(regexSeparator)).map(Integer::parseInt);
    }

    private static int sum(Stream<Integer> numbers) {
        return numbers.reduce(0, (accumulator, n) -> accumulator + n);
    }
}