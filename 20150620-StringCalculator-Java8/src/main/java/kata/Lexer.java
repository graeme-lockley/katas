package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private int index;
    private String input;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
    }

    public char head() {
        if (index < input.length()) {
            return input.charAt(index);
        } else {
            return EOS;
        }
    }

    public void next() {
        index += 1;
    }

    public RuntimeException parseError(String message) {
        int inputLength = input.length();
        String prefix = input.substring(0, Math.min(index, inputLength));
        String suffix = input.substring(Math.min(index, inputLength - 1), inputLength);

        return new RuntimeException("Parse Error: " + message + ": " + prefix + " >>>> " + suffix);
    }
}
