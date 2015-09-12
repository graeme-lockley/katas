package kata;

public class Lexer {
    public static final char EOS = (char) 0;

    private String input;
    private int index;

    public Lexer(String input) {
        this.index = 0;
        this.input = input;
    }


    public char next() {
        if (index >= input.length()) {
            return EOS;
        } else {
            return input.charAt(index);
        }
    }

    public char skip() {
        char next = next();
        index += 1;
        return next;
    }

    public RuntimeException error(String message) {
        String prefix = input.substring(0, Math.min(index, input.length()));
        String suffix = input.substring(Math.min(index, input.length() - 1));

        return new RuntimeException(message + ": " + prefix + " >>>> " + suffix);
    }
}
