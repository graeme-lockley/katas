package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private final String input;
    private int index = 0;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
    }

    public char head() {
        return index >= input.length() ? EOS : input.charAt(index);
    }

    public void skip() {
        index += 1;
    }

    public ParseException parseException(String message) {
        String prefix = input.substring(0, index <= input.length() ? index : input.length());
        String suffix = input.substring(index < input.length() ? index : input.length() - 1);
        return new ParseException("Error: " + message + ": " + prefix + " >>>>> " + suffix);
    }

    public void match(char ch) throws ParseException {
        if (head() == ch) {
            skip();
        } else {
            throw parseException("Expected " + ch);
        }
    }
}
