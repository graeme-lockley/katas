package kata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

public class SC {
    private Lexer lexer;

    private SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) {
        return new SC(new Lexer(input)).INPUT();
    }

    /*
        INPUT ::= EOS
                | INTEGER { ( ',' | '\n' ) INTEGER } EOS
                | '/' '/' INPUT_TAIL EOS
    */
    private int INPUT() {
        List<Integer> negatives = new ArrayList<>();

        int result;
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

        if (head() == Lexer.EOS) {
            if (negatives.isEmpty()) {
                return result;
            } else {
                throw new IllegalArgumentException(negatives.stream().map(Object::toString).collect(Collectors.joining(",")));
            }
        } else {
            throw error("Expected EOS");
        }
    }

    /*
        INPUT_TAIL ::= SEPARATOR '\n' INTEGER { SEPARATOR INTEGER }
                    | '[' SEPARATORS ']' '\n' INTEGER { SEPARATORS INTEGER }
    */
    private int INPUT_TAIL(List<Integer> negatives) {
        int result;

        if (isSeparator(head())) {
            char separator = SEPARATOR();
            match('\n');

            result = INTEGER(negatives);

            while (isSeparator(head())) {
                char matchedSeparator = SEPARATOR();
                if (separator == matchedSeparator) {
                    result += INTEGER(negatives);
                } else {
                    throw error("Unknown separator " + matchedSeparator);
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
                String matchedSeparators = SEPARATORS();
                if (separators.contains(matchedSeparators)) {
                    result += INTEGER(negatives);
                } else {
                    throw error("Unknown separator " + matchedSeparators);
                }
            }
        } else {
            throw error("Expected separator or '['");

        }
        return result;
    }

    /*
        SEPARATORS ::= SEPARATOR { SEPARATOR }
     */
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

    /*
        SEPARATOR ::= ( 1C | ... | 255C ) \ ( '0' | ... | '9' | '[' | ']' )
     */
    private char SEPARATOR() {
        if (isSeparator(head())) {
            char result = head();
            skip();
            return result;
        } else {
            throw error("Separator expected");
        }
    }

    /*
           INTEGER ::= [ '-' ] DIGIT { DIGIT }
     */
    private int INTEGER(List<Integer> negatives) {
        boolean isNegative;

        if (head() == '-') {
            skip();
            isNegative = true;
        } else {
            isNegative = false;
        }

        int result = DIGIT();
        while (isDigit(head())) {
            result = result * 10 + DIGIT();
        }

        if (isNegative) {
            negatives.add(-result);
        } else if (result > 1000) {
            result = 0;
        }

        return isNegative ? -result : result;
    }

    /*
        DIGIT ::= '0' | ... | '9'
     */
    private int DIGIT() {
        if (isDigit(head())) {
            int result = ((int) head()) - ((int) '0');
            skip();
            return result;
        } else {
            throw error("Expected digit");
        }
    }

    private boolean isSeparator(char c) {
        return c != Lexer.EOS && !isDigit(c) && c != '[' && c != ']';
    }

    private void match(char c) {
        if (head() == c) {
            skip();
        } else {
            throw error("Expected " + c + " (" + ((int) c) + ")");
        }
    }

    private char head() {
        return lexer.head();
    }

    private void skip() {
        lexer.skip();
    }

    private RuntimeException error(String message) {
        return lexer.error(message);
    }
}
