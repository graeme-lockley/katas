package kata;

/*
    INPUT ::= EOS
            | INTEGER { ( ',' | '\n' ) INTEGER } EOS
            | '/' '/' INPUT_TAIL EOS

    INPUT_TAIL ::= SEPARATOR '\n' INTEGER { SEPARATOR INTEGER }
                 | '[' SEPARATORS ']' { '[' SEPARATORS ']' } '\n' INTEGER { SEPARATORS INTEGER }

    INTEGER ::= [ '-' ] DIGIT { DIGIT }

    DIGIT ::= '0' | ... | '9'

    SEPARATORS ::= SEPARATOR { SEPARATOR }

    SEPARATOR ::= ( 1C | ... | 255C ) \ ( '0' | ... \ '9' | '[' | ']' )

    EOS ::= 0C
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

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
        List<Integer> negatives = new ArrayList<>();

        if (head() == Lexer.EOS) {
            result = 0;
        } else if (head() == '-' || isDigit(head())) {
            result = INTEGER(negatives);

            while (head() == ',' || head() == '\n') {
                skip();
                result += INTEGER(negatives);
            }
        } else if (head() == '/') {
            skip();
            match('/');
            result = INPUT_TAIL(negatives);
        } else {
            throw error("Expected EOS, '-' or digit");
        }

        if (negatives.isEmpty()) {
            match(Lexer.EOS);
            return result;
        } else {
            throw new IllegalArgumentException(negatives.stream().map(Object::toString).collect(Collectors.joining(",")));
        }
    }

    private int INPUT_TAIL(List<Integer> negatives) {
        int result;

        if (isSeparator(head())) {
            char separator = SEPARATOR();
            match('\n');

            result = INTEGER(negatives);

            while (isSeparator(head())) {
                char usedSeparator = SEPARATOR();
                if (separator == usedSeparator) {
                    result += INTEGER(negatives);
                } else {
                    throw error("Unknown separator " + usedSeparator);
                }
            }
        } else if (head() == '[') {
            skip();
            Set<String> separators = new HashSet<>();
            separators.add(SEPARATORS());
            match(']');

            while (head() == '[') {
                skip();
                separators.add(SEPARATORS());
                match(']');
            }

            match('\n');
            result = INTEGER(negatives);

            while (isSeparator(head())) {
                String usedSeparator = SEPARATORS();
                if (separators.contains(usedSeparator)) {
                    result += INTEGER(negatives);
                } else {
                    throw error("Unknown separator " + usedSeparator);
                }
            }

        } else {
            throw error("Expected separator or '['");
        }
        return result;
    }

    private String SEPARATORS() {
        if (isSeparator(head())) {
            StringBuilder result = new StringBuilder();
            result.append(head());
            skip();
            while (isSeparator(head())) {
                result.append(head());
                skip();
            }
            return result.toString();
        } else {
            throw error("Expected separator");
        }

    }

    private char SEPARATOR() {
        if (isSeparator(head())) {
            char result = head();
            skip();
            return result;
        } else {
            throw error("Expected separator");
        }
    }

    private int INTEGER(List<Integer> negatives) {
        if (head() == '-' || isDigit(head())) {
            boolean isNegative = false;

            if (head() == '-') {
                isNegative = true;
                skip();
            }

            int result = DIGIT();
            while (isDigit(head())) {
                result = result * 10 + DIGIT();
            }

            if (isNegative) {
                result = -result;
                negatives.add(result);
            } else if (result > 1000) {
                result = 0;
            }

            return result;
        } else {
            throw error("Expected '-' or digit");
        }
    }

    private int DIGIT() {
        if (isDigit(head())) {
            int result = ((int) head()) - ((int) '0');
            skip();
            return result;
        } else {
            throw error("Expected digit");
        }
    }

    private RuntimeException error(String message) {
        return lexer.error(message);
    }

    private void match(char c) {
        if (head() == c) {
            skip();
        } else {
            throw error("Expected " + c + " (" + ((int) c) + ")");
        }
    }

    private void skip() {
        lexer.skip();
    }

    private char head() {
        return lexer.head();
    }

    public boolean isSeparator(char c) {
        return c != Lexer.EOS && !isDigit(c) && c != '[' && c != ']';
    }
}

