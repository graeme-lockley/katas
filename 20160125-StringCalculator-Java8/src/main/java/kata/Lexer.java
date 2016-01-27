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

    public IllegalArgumentException error(String message) {
        String prefix = input.substring(0, Math.min(index, input.length()));
        String suffix = input.substring(Math.min(index, input.length() - 1));

        return new IllegalArgumentException("Error: " + message + "\n" + prefix + " >>> " + suffix);
    }

    public char nextChar() {
        return index < inputLength ? input.charAt(index) : EOS;
    }

    public void skipChar() {
        index += 1;
    }
}
