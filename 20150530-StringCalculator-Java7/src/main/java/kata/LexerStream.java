package kata;

public class LexerStream {
    public static final char EOS = (char) 0;

    private final String input;
    private int index;

    public LexerStream(String input) {
        this.input = input;
        this.index = 0;
    }

    public char head() {
        if (index >= input.length()) {
            return EOS;
        }
        else {
            return input.charAt(index);
        }
    }

    public void popHead() {
        index += 1;
    }

    public int position() {
        return index;
    }

    public ParseException exception(String message) {
        return new ParseException(position() + ": " + message + ":  Found '" + head() + "' (" + (int) head() + "):   " + input.substring(0, index) + " >>>>>  " + input.substring(index));
    }
}
