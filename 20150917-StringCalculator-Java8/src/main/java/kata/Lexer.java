package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private final String input;
    private int index;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
    }

    public char head() {
        return index < input.length() ? input.charAt(index) : EOS;
    }

    public void skip() {
        index += 1;
    }

    public RuntimeException error(String message) {
        String prefix = input.substring(0, Math.min(index, input.length()));
        String suffix = input.substring(Math.min(index, input.length() - 1));

        return new RuntimeException("Parsing error: " + message + ": " + prefix + " >>>> " + suffix);
    }
}
