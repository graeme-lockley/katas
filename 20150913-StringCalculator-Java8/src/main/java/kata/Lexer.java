package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private final String input;
    private final int inputLength;
    private int index;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
        this.inputLength = input.length();
    }

    public char head() {
        return index < inputLength ? input.charAt(index) : EOS;
    }

    public RuntimeException parseException(String message) {
        String prefix = input.substring(0, Math.min(inputLength, index));
        String suffix = input.substring(Math.min(index, inputLength - 1));

        throw new RuntimeException("Parse Error: " + message + ": " + prefix + "  >>> " + suffix);
    }

    public void match(char c) {
        if (head() == c) {
            skip();
        } else {
            throw parseException("Expected " + c + " (" + ((int) c) + ")");
        }
    }

    public void skip() {
        index += 1;
    }
}
