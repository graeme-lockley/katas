package kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.regex.Pattern.quote;

public class SC {
    public static int add(String input) {
        return input.isEmpty() ? 0 : sum(from(input));
    }

    public static List<Integer> from(String input) {
        if (input.startsWith("//[")) {
            int indexOfNewline = input.indexOf('\n');
            return from(input.substring(indexOfNewline + 1), assembleSeparatorsRegex(input.substring(3, indexOfNewline - 1)));
        } else if (input.startsWith("//")) {
            return from(input.substring(4), quote(input.substring(2, 3)));
        } else {
            return from(input, ",|\n");
        }
    }

    private static String assembleSeparatorsRegex(String pattern) {
        StringBuilder regex = new StringBuilder();

        String[] regexPattern = pattern.split("\\]\\[");
        Arrays.sort(regexPattern);
        reverse(regexPattern);

        for (String element : regexPattern) {
            regex.append("|").append(quote(element));
        }

        return regex.substring(1);
    }

    public static List<Integer> from(String input, String separatorsRegexPattern) {
        if (input.isEmpty()) {
            return new ArrayList<>();
        } else {
            return from(input.split(separatorsRegexPattern));
        }
    }

    public static List<Integer> from(String[] inputs) {
        List<Integer> result = new ArrayList<>();
        for (String input : inputs) {
            result.add(Integer.parseInt(input));
        }
        return result;
    }

    private static void reverse(String[] input) {
        for (int lp = 0; lp < input.length / 2; lp += 1) {
            String tmp = input[lp];
            input[lp] = input[input.length - lp - 1];
            input[input.length - lp - 1] = tmp;
        }
    }

    public static int sum(List<Integer> nums) {
        int result = 0;
        List<Integer> negatives = new ArrayList<>();
        for (Integer value : nums) {
            if (value < 0) {
                negatives.add(value);
            } else {
                result += value < 1001 ? value : 0;
            }
        }
        if (negatives.isEmpty()) {
            return result;
        } else {
            throw new IllegalArgumentException(mkString(negatives, ","));
        }
    }

    public static String mkString(List<Integer> nums, String sep) {
        StringBuilder sb = new StringBuilder();
        for (Integer num : nums) {
            sb.append(sep).append(num);
        }
        return sb.substring(sep.length());
    }
}
