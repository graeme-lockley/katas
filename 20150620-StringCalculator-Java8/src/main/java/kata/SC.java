package kata;

/*
    INPUT ::= EOS
            | INTEGER { (',' | '\n') INTEGER } EOS
            | '/' '/' INPUT_TAIL EOS

    INPUT_TAIL ::= SEPARATOR '\n' INTEGER { SEPARATOR INTEGER }
                 | '[' SEPARATORS ']' { '[' SEPARATORS ']' } '\n' INTEGER { SEPARATORS INTEGER }

    INTEGER ::= POSITIVE_INTEGER
              | NEGATIVE_INTEGER

    NEGATIVE_INTEGER ::= '-' POSITIVE_INTEGER
    POSITIVE_INTEGER ::= DIGIT { DIGIT }

    DIGIT ::= '0' | ... | '9'
    SEPARATOR ::= (1C | ... | 255C) - ('0' | ... | '9' | '[' | ']' | '-')
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;

public class SC {
    private Lexer lexer;

    public SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) {
        return new SC(new Lexer(input)).parseINPUT();
    }

    private int parseINPUT() {
        List<Integer> negatives = new ArrayList<>();
        int result;

        if (lexer.head() == Lexer.EOS) {
            result = 0;
        } else if (lexer.head() == '-' || isDigit(lexer.head())) {
            result = parseINTEGER(negatives);

            while (lexer.head() == ',' || lexer.head() == '\n') {
                lexer.next();
                result += parseINTEGER(negatives);
            }
        } else if (lexer.head() == '/') {
            lexer.next();
            match('/');
            result = parseINPUT_TAIL(negatives);
        } else {
            throw lexer.parseError("Expected EOS or '-' or digit");
        }

        if (lexer.head() == Lexer.EOS) {
            if (negatives.isEmpty()) {
                return result;
            } else {
                throw new IllegalArgumentException(negatives.stream().map(Object::toString).collect(Collectors.joining(",")));
            }
        } else {
            throw lexer.parseError("Expected EOS");
        }
    }

    private int parseINPUT_TAIL(List<Integer> negatives) {
        if (isStartOfSEPARATOR()) {
            char sep = parseSEPARATOR();
            match('\n');
            int result = parseINTEGER(negatives);

            while (isStartOfSEPARATOR()) {
                char nextSep = parseSEPARATOR();

                if (sep != nextSep) {
                    throw lexer.parseError("Expected separator " + sep + " (" + ((int) sep) + ") but found " + nextSep + " (" + ((int) nextSep) + ")");
                }

                result += parseINTEGER(negatives);
            }

            return result;
        } else if (lexer.head() == '[') {
            lexer.next();
            Set<String> seps = new HashSet<>();
            seps.add(parseSEPARATORS());
            match(']');

            while (lexer.head() == '[') {
                lexer.next();
                seps.add(parseSEPARATORS());
                match(']');
            }
            match('\n');

            int result = parseINTEGER(negatives);

            while (isStartOfSEPARATOR()) {
                String nextSep = parseSEPARATORS();

                if (!seps.contains(nextSep)) {
                    throw lexer.parseError("Unknown separator " + nextSep);
                }

                result += parseINTEGER(negatives);
            }

            return result;
        } else {
            throw lexer.parseError("Expected a '[' or separator");
        }
    }

    private char parseSEPARATOR() {
        if (isStartOfSEPARATOR()) {
            char result = lexer.head();
            lexer.next();
            return result;
        } else {
            throw lexer.parseError("Expected a separator");
        }
    }

    private String parseSEPARATORS() {
        if (isStartOfSEPARATOR()) {
            StringBuilder result = new StringBuilder();
            result.append(parseSEPARATOR());
            while (isStartOfSEPARATOR()) {
                result.append(parseSEPARATOR());
            }
            return result.toString();
        } else {
            throw lexer.parseError("Expected a separator");
        }
    }

    private boolean isStartOfSEPARATOR() {
        return !isDigit(lexer.head()) && lexer.head() != Lexer.EOS && lexer.head() != '[' && lexer.head() != ']' && lexer.head() != '-';
    }

    private int parseINTEGER(List<Integer> negatives) {
        if (isDigit(lexer.head())) {
            int result = parsePOSITIVE_INTEGER();
            return result < 1001 ? result : 0;
        } else if (lexer.head() == '-') {
            int result = parseNEGATIVE_INTEGER();
            negatives.add(result);
            return result;
        } else {
            throw lexer.parseError("Expected '-' or digit");
        }
    }

    private int parsePOSITIVE_INTEGER() {
        if (isDigit(lexer.head())) {
            int result = (int) lexer.head() - (int) '0';
            lexer.next();
            while (isDigit(lexer.head())) {
                result = result * 10 + (int) lexer.head() - (int) '0';
                lexer.next();
            }
            return result;
        } else {
            throw lexer.parseError("Expected digit");
        }
    }

    private int parseNEGATIVE_INTEGER() {
        if (lexer.head() == '-') {
            lexer.next();
            return -parsePOSITIVE_INTEGER();
        } else {
            throw lexer.parseError("Expected '-'");
        }
    }

    private void match(char c) {
        if (lexer.head() == c) {
            lexer.next();
        } else {
            throw lexer.parseError("Exepected " + c + "(" + ((int) c) + ")");
        }
    }
}

