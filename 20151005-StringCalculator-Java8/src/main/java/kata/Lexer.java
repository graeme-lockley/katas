package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private final String input;
    private final int inputLength;
    private int index = 0;

    public Lexer(String input) {
        this.input = input;
        this.inputLength = input.length();
    }

    public char next() {
        return index < inputLength ? input.charAt(index): EOS;
    }

    public void skip() {
        index += 1;
    }

    public RuntimeException error(String message) {
        String prefix = input.substring(0, Math.min(index, inputLength));
        String suffix = input.substring(Math.min(index, inputLength - 1));

        return new RuntimeException("Parsing error: " + message + ": " + prefix + " >>> " + suffix);
    }
}
