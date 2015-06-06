package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private final String input;
    private int index = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public char head() {
        return index >= input.length() ? EOS : input.charAt(index);
    }

    public void skip() {
        index += 1;
    }

    public ParseException exception(String message) {
        return new ParseException("Error: " + message + ": " + input.substring(0, index <= input.length() ? index : input.length()) + " >>>>> " + (index < input.length() ? input.substring(index) : ""));
    }
}
