package kata;

import static java.lang.Character.isDigit;
import static kata.Lexer.EOS;

/*
    INPUT ::= EOS
            | INTEGER { ( ',' | '\n' ) INTEGER } EOS
            | '/' '/' SEPARATOR '\n' INTEGER { SEPARATOR INTEGER } EOS

    INTEGER ::= [ '-' ] DIGIT { DIGIT }

    DIGIT ::= '0' | ... | '9'

    SEPARATOR ::= 1C | ... | 255C \ ( '0' | ... | '9' )
 */
public class SC {
    private final Lexer lexer;

    public SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) {
//        System.out.println(input);
        return new SC(new Lexer(input)).INPUT();
    }

    private int INPUT() {
        int result;
        if (next() == EOS) {
            result = 0;
        } else if (next() == '-' || isDigit(next())) {
            result = INTEGER();

            while (next() == ',' || next() == '\n') {
                skip();
                result = result + INTEGER();
            }
        } else if (next() == '/') {
            skip();
            match('/');
            char separator = SEPARATOR();
            match('\n');
            result = INTEGER();

            while (isSeparator(next())) {
                char matchedSeparator = SEPARATOR();
                if (separator == matchedSeparator) {
                    result = result + INTEGER();
                } else {
                    throw error("Expected the separator " + separator + " but found " + matchedSeparator);
                }
            }
        } else {
            throw error("Expected EOS, '-' or digit");
        }

        match(EOS);
        return result;
    }

    private char SEPARATOR() {
        if (isSeparator(next())) {
            char result = next();
            skip();
            return result;
        } else {
            throw error("Expected a separator");
        }
    }

    private boolean isSeparator(char c) {
        return c != EOS && !isDigit(c);
    }

    private void match(char c) {
        if (next() == c) {
            skip();
        } else {
            throw error("Expected " + c + " (" + ((int) c) + ")");
        }
    }

    private int INTEGER() {
        if (next() == '-' || isDigit(next())) {
            boolean isNegative = false;

            if (next() == '-') {
                isNegative = true;
                skip();
            }

            int result = DIGIT();
            while (isDigit(next())) {
                result = result * 10 + DIGIT();
            }

            return isNegative ? -result : result;
        } else {
            throw error("Expected '-' or digit");
        }
    }

    private int DIGIT() {
        if (isDigit(next())) {
            int result = ((int) next()) - ((int) '0');
            skip();
            return result;
        } else {
            throw error("Expected digit");
        }
    }

    private char next() {
        return lexer.next();
    }

    private void skip() {
        lexer.skip();
    }

    private RuntimeException error(String message) {
        return lexer.error(message);
    }
}
