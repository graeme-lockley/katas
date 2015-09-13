package kata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

/*
    INPUT ::= EOS
            | INTEGER { ( ',' | '\n' ) INTEGER } EOS
            | '/' '/' INPUT_TAIL EOS

    INPUT_TAIL ::= SEPARATOR '\n' INTEGER { SEPARATOR INTEGER }
                 | '[' SEPARATORS ']' { '[' SEPARATORS ']' } '\n' INTEGER { SEPARATORS INTEGER }

    INTEGER ::= NEGATIVE_INTEGER
              | POSITIVE_INTEGER

    NEGATIVE_INTEGER ::= '-' POSITIVE_INTEGER

    POSITIVE_INTEGER ::= DIGIT { DIGIT }

    DIGIT ::= '0' | ... | '9'

    SEPARATORS ::= SEPARATOR { SEPARATOR }

    SEPARATOR ::= ( 1C | ... | 255C ) - ( '0' | ... | '9' | '[' | ']' )
 */

public class SC {
    private Lexer lexer;

    public SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) {
        return new SC(new Lexer(input)).INPUT();
    }

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
            lexer.match('/');
            result = INPUT_TAIL(negatives);
        } else {
            throw lexer.parseException("Expected '-', digit, '/' or EOS");
        }

        lexer.match(Lexer.EOS);
        if (negatives.isEmpty()) {
            return result;
        } else {
            throw new IllegalArgumentException(negatives.stream().map(Object::toString).collect(Collectors.joining(",")));
        }
    }

    private int INPUT_TAIL(List<Integer> negatives) {
        int result;

        if (isSeparator()) {
            char separator = SEPARATOR();
            lexer.match('\n');
            result = INTEGER(negatives);
            while (isSeparator()) {
                char nextSeparator = SEPARATOR();
                if (separator == nextSeparator) {
                    result += INTEGER(negatives);
                } else {
                    throw lexer.parseException("The separator " + nextSeparator + " is not the defined separator");
                }
            }
        } else if (head() == '[') {
            skip();
            Set<String> separators = new HashSet<>();
            separators.add(SEPARATORS());
            lexer.match(']');

            while (head() == '[') {
                skip();
                separators.add(SEPARATORS());
                lexer.match(']');
            }

            lexer.match('\n');
            result = INTEGER(negatives);
            while (isSeparator()) {
                String nextSeparator = SEPARATORS();
                if (separators.contains(nextSeparator)) {
                    result += INTEGER(negatives);
                } else {
                    throw lexer.parseException("The separator " + nextSeparator + " is not a defined separator");
                }
            }
        } else {
            throw lexer.parseException("Expected '[' or a separator");
        }
        return result;
    }

    private String SEPARATORS() {
        StringBuilder result = new StringBuilder();
        if (isSeparator()) {
            result.append(head());
            skip();
            while (isSeparator()) {
                result.append(head());
                skip();
            }
            return result.toString();
        } else {
            throw lexer.parseException("Expected a separator character");
        }
    }

    private char SEPARATOR() {
        if (isSeparator()) {
            char result = head();
            skip();
            return result;
        } else {
            throw lexer.parseException("The character " + head() + " is not a valid separator");
        }
    }

    private int INTEGER(List<Integer> negatives) {
        if (head() == '-') {
            int result = NEGATIVE_INTEGER();
            negatives.add(result);
            return result;
        } else if (isDigit(head())) {
            int result = POSITIVE_INTEGER();
            return result < 1001 ? result : 0;
        } else {
            throw lexer.parseException("Expected '-' or digit");
        }
    }

    private int NEGATIVE_INTEGER() {
        if (head() == '-') {
            skip();
            return -POSITIVE_INTEGER();
        } else {
            throw lexer.parseException("Expected '-'");
        }
    }

    private int POSITIVE_INTEGER() {
        if (isDigit(head())) {
            int result = DIGIT();
            while (isDigit(head())) {
                result = result * 10 + DIGIT();
            }
            return result;
        } else {
            throw lexer.parseException("Expected digit");
        }
    }

    private int DIGIT() {
        if (isDigit(head())) {
            int result = ((int) head()) - ((int) '0');
            skip();
            return result;
        } else {
            throw lexer.parseException("Expected digit");
        }
    }

    private boolean isSeparator() {
        return !isDigit(head()) && head() != Lexer.EOS && head() != '[' && head() != ']';
    }

    private char head() {
        return lexer.head();
    }

    private void skip() {
        lexer.skip();
    }
}
