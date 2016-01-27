package kata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;
import static kata.Lexer.EOS;

/*
    INPUT ::= EOS
            | '/' '/' CUSTOM_TAIL EOS
            | INTEGER { ',' INTEGER } EOS
    CUSTOM_TAIL ::= SEPARATOR '\n' INTEGER { SEPARATOR INTEGER }
                  | '[' SEPARATORS ']' { '[' SEPARATORS ']' } '\n' INTEGER { SEPARATORS INTEGER }
    INTEGER ::= POSITIVE_INT | NEGATIVE_INT
    POSITIVE_INT ::= DIGIT { DIGIT }
    NEGATIVE_INT ::= '-' POSITIVE_INT
    DIGIT ::= '0' | ... | '9'
    SEPARATORS ::= SEPARATOR { SEPARATOR }
    SEPARATOR ::= 1C | ... | 255C - ('0' | ... \ '9')
 */
public class SC {
    private final Lexer lexer;

    public SC(String input) {
        lexer = new Lexer(input);
    }

    public static int add(String input) {
        return new SC(input).parseINPUT();
    }

    private int parseINPUT() {
        List<Integer> negatives = new ArrayList<>();
        int result = 0;

        if (nextChar() != EOS) {
            if (nextChar() == '/') {
                skipChar();
                match('/');
                result = parseCUSTOM_TAIL(negatives);
            } else if (nextChar() == '-' || isDigit(nextChar())) {
                result = parseINTEGER(negatives);
                while (nextChar() == ',' || nextChar() == '\n') {
                    skipChar();
                    result += parseINTEGER(negatives);
                }
            } else {
                throw error("Expected '-', digit or EOS");
            }
        }
        match(EOS);
        if (negatives.isEmpty()) {
            return result;
        } else {
            throw new IllegalArgumentException(negatives.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }
    }

    private int parseCUSTOM_TAIL(List<Integer> negatives) {
        if (isSeparator(nextChar())) {
            char separator = parseSEPARATOR();
            match('\n');
            int result = parseINTEGER(negatives);
            while (isSeparator(nextChar())) {
                char nextSeparator = parseSEPARATOR();
                if (separator != nextSeparator) {
                    throw error("Separators do not match");
                }
                result += parseINTEGER(negatives);
            }

            return result;
        } else if (nextChar() == '[') {
            Set<String> separators = new HashSet<>();
            skipChar();
            separators.add(parseSEPARATORS());
            match(']');
            while (nextChar() == '[') {
                skipChar();
                separators.add(parseSEPARATORS());
                match(']');
            }

            match('\n');
            int result = parseINTEGER(negatives);
            while (isSeparator(nextChar())) {
                String nextSeparator = parseSEPARATORS();
                if (!separators.contains(nextSeparator)) {
                    throw error("Separators do not match");
                }
                result += parseINTEGER(negatives);
            }

            return result;
        } else {
            throw error("Expected '[' or separator");
        }
    }

    private String parseSEPARATORS() {
        if (isSeparator(nextChar())) {
            StringBuilder sb = new StringBuilder();
            sb.append(nextChar());
            skipChar();
            while (isSeparator(nextChar())) {
                sb.append(nextChar());
                skipChar();
            }
            return sb.toString();
        } else {
            throw error("Expected separator");
        }
    }

    private char parseSEPARATOR() {
        if (isSeparator(nextChar())) {
            char result = nextChar();
            skipChar();
            return result;
        } else {
            throw error("Separator character expected");
        }
    }

    private boolean isSeparator(char ch) {
        return !isDigit(ch) && ch != EOS && ch != '[' && ch != ']';
    }

    private int parseINTEGER(List<Integer> negatives) {
        if (isDigit(nextChar())) {
            int result = parsePOSITIVE_INT();
            return result < 1001 ? result : 0;
        } else if (nextChar() == '-') {
            skipChar();
            int result = -parsePOSITIVE_INT();
            negatives.add(result);
            return result;
        } else {
            throw error("Expected '-' or digit");
        }
    }

    private IllegalArgumentException error(String message) {
        return lexer.error(message);
    }

    private int parsePOSITIVE_INT() {
        int result = parseDIGIT();

        while (isDigit(nextChar())) {
            result = result * 10 + parseDIGIT();
        }

        return result;
    }

    private int parseDIGIT() {
        if (isDigit(nextChar())) {
            int result = ((int) nextChar() - (int) '0');
            skipChar();
            return result;
        } else {
            throw error("Expected digit");
        }
    }

    private void skipChar() {
        lexer.skipChar();
    }

    private char nextChar() {
        return lexer.nextChar();
    }

    private void match(char ch) {
        if (nextChar() == ch) {
            skipChar();
        } else {
            throw error("Expected " + ch + " (" + ((int) ch) + ")");
        }
    }
}
