package kata;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.quote;

public class SC {
    public static int add(String input) {
        if (parse(input).anyMatch(n -> n < 0)) {
            throw new IllegalArgumentException(parse(input).filter(n -> n < 0).map(Object::toString).collect(Collectors.joining(",")));
        } else {
            return parse(input).filter(n -> n < 1001).reduce(0, (accumulator, n) -> accumulator + n);
        }
    }

    private static Stream<Integer> parse(String input) {
        if (input.isEmpty()) {
            return tokenize("0", ",");
        } else if (input.startsWith("//[")) {
            final int indexOfNewline = input.indexOf('\n');
            final String separatorsRegex = Stream.of(input.substring(3, indexOfNewline - 1).split("\\]\\[")).sorted((x, y) -> y.length() - x.length()).map(Pattern::quote).collect(Collectors.joining("|"));

            return tokenize(input.substring(indexOfNewline + 1), separatorsRegex);
        } else if (input.startsWith("//")) {
            return tokenize(input.substring(4), quote(input.substring(2, 3)));
        } else {
            return tokenize(input, ",|\n");
        }
    }

    private static Stream<Integer> tokenize(String numbers, String separatorsRegex) {
        return Stream.of(numbers.split(separatorsRegex)).map(Integer::parseInt);
    }
}
