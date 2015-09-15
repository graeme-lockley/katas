package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private final String input;
    private int index;
    private final int inputLength;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
        this.inputLength = input.length();
    }

    public char head() {
        if (index < inputLength) {
            return input.charAt(index);
        } else {
            return EOS;
        }

    }

    public RuntimeException error(String message) {
        String prefix = input.substring(0, Math.min(index, inputLength));
        String suffix = input.substring(Math.min(index, inputLength - 1));

        return new RuntimeException("Parse error: " + message + ": " + prefix + " >>> " + suffix);
    }

    public void skip() {
        index += 1;
    }
}
