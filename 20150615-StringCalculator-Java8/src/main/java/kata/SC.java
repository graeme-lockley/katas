package kata;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.quote;

public class SC {
    public static int add(String input) {
        if (parse(input).anyMatch(x -> x < 0)) {
            throw new IllegalArgumentException(parse(input).filter(x -> x < 0).map(Object::toString).collect(Collectors.joining(",")));
        } else {
            return parse(input).filter(x -> x < 1001).reduce(0, (x, y) -> x + y);
        }
    }

    private static Stream<Integer> parse(String input) {
        if (input.isEmpty()) {
            return Stream.of();
        } else if (input.startsWith("//[")) {
            int indexOfNewline = input.indexOf('\n');

            List<String> seps = Arrays.asList(input.substring(3, indexOfNewline - 1).split("\\]\\["));
            seps.sort(Comparator.<String>reverseOrder());
            String regex = seps.stream().map(Pattern::quote).collect(Collectors.joining("|"));

            return parse(input.substring(indexOfNewline + 1), regex);
        } else if (input.startsWith("//")) {
            return parse(input.substring(4), quote(input.substring(2, 3)));
        } else {
            return parse(input, ",|\n");
        }
    }

    private static Stream<Integer> parse(String source, String separatorRegex) {
        return Stream.of(source.split(separatorRegex)).map(Integer::parseInt);
    }
}

