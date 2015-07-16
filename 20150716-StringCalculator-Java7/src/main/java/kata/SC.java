package kata;

/*
    INPUT ::= EOS
            | NUMBER { ( ',' | '\n' ) NUMBER } EOS
            | '/' '/' INPUT_TAIL EOS

    INPUT_TAIL ::= SEPARATOR '\n' NUMBER { SEPARATOR NUMBER }
                 | '[' SEPARATORS ']' { '[' SEPARATORS ']' } '\n' NUMBER { SEPARATORS NUMBER }

    NUMBER ::= NEGATIVE_NUMBER
             | POSITIVE_NUMBER

    NEGATIVE_NUMBER ::= '-' POSITIVE_NUMBER
    POSITIVE_NUMBER ::= DIGIT {DIGIT}

    SEPARATORS ::= SEPARATOR { SEPARATOR }

    DIGIT = '0' | ... \ '9'
    SEPARATOR = (0C | ... | 255C) - ('0' | ... | '9' | '[' | ']' | '-')
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kata.Lexer.EOS;

public class SC {
    private Lexer lexer;

    public SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) throws ParseException {
        return new SC(new Lexer(input)).parseINPUT();
    }

    private int parseINPUT() throws ParseException {
        List<Integer> negatives = new ArrayList<>();
        int result;

        if (lexer.head() == EOS) {
            result = 0;
        } else if (lexer.head() == '-' || Character.isDigit(lexer.head())) {
            result = parseNUMBER(negatives);

            while (lexer.head() == ',' || lexer.head() == '\n') {
                lexer.skip();
                result += parseNUMBER(negatives);
            }
        } else if (lexer.head() == '/') {
            lexer.skip();
            lexer.match('/');
            result = parseINPUT_TAIL(negatives);
        } else {
            throw lexer.parseException("Expected '-' or a digit or a '/'");
        }

        if (lexer.head() == EOS) {
            if (negatives.isEmpty()) {
                return result;
            } else {
                throw new IllegalArgumentException(mkString(negatives));
            }
        } else {
            throw lexer.parseException("Expected EOS");
        }
    }

    private int parseINPUT_TAIL(List<Integer> negatives) throws ParseException {
        if (isSEPARATOR(lexer.head())) {
            char separator = parseSEPARATOR();
            lexer.match('\n');

            int result = parseNUMBER(negatives);

            while (lexer.head() == separator) {
                char newSeparator = parseSEPARATOR();
                if (separator != newSeparator) {
                    throw lexer.parseException("Expected " + separator + " but found " + newSeparator);
                }
                result += parseNUMBER(negatives);
            }
            return result;
        } else if (lexer.head() == '[') {
            Set<String> separators = new HashSet<>();

            lexer.skip();
            separators.add(parseSEPARATORS());
            lexer.match(']');

            while (lexer.head() == '[') {
                lexer.skip();
                separators.add(parseSEPARATORS());
                lexer.match(']');
            }

            lexer.match('\n');

            int result = parseNUMBER(negatives);

            while (isSEPARATOR(lexer.head())) {
                String newSeparator = parseSEPARATORS();
                if (!separators.contains(newSeparator)) {
                    throw lexer.parseException("Unrecognised separator " + newSeparator);
                }
                result += parseNUMBER(negatives);
            }
            return result;
        } else {
            throw lexer.parseException("Expected separator or '['");
        }
    }

    private String mkString(List<Integer> negatives) {
        StringBuilder sb = new StringBuilder();
        for (int negative : negatives) {
            sb.append(',').append(negative);
        }
        return sb.substring(1);
    }

    private int parseNUMBER(List<Integer> negatives) throws ParseException {
        if (lexer.head() == '-') {
            int result = parseNEGATIVE_NUMBER();
            negatives.add(result);
            return result;
        } else if (Character.isDigit(lexer.head())) {
            int result = parsePOSITIVE_NUMBER();
            return result < 1001 ? result : 0;
        } else {
            throw lexer.parseException("Expected '-' or a digit");
        }
    }

    private int parseNEGATIVE_NUMBER() throws ParseException {
        if (lexer.head() == '-') {
            lexer.skip();
            return -parsePOSITIVE_NUMBER();
        } else {
            throw lexer.parseException("Expected '-'");
        }
    }

    private int parsePOSITIVE_NUMBER() throws ParseException {
        int result = parseDIGIT();
        while (Character.isDigit(lexer.head())) {
            result = result * 10 + parseDIGIT();
        }
        return result;
    }

    private String parseSEPARATORS() throws ParseException {
        if (isSEPARATOR(lexer.head())) {
            StringBuilder result = new StringBuilder();
            result.append(lexer.head());
            lexer.skip();
            while (isSEPARATOR(lexer.head())) {
                result.append(lexer.head());
                lexer.skip();
            }
            return result.toString();
        } else {
            throw lexer.parseException("Expected separator");
        }
    }

    private int parseDIGIT() throws ParseException {
        if (Character.isDigit(lexer.head())) {
            int result = (int) lexer.head() - (int) '0';
            lexer.skip();
            return result;
        } else {
            throw lexer.parseException("Expected digit");
        }
    }

    private char parseSEPARATOR() throws ParseException {
        if (isSEPARATOR(lexer.head())) {
            char ch = lexer.head();
            lexer.skip();
            return ch;
        } else {
            throw lexer.parseException("Expected a separator");
        }
    }

    private boolean isSEPARATOR(char ch) {
        return !Character.isDigit(ch) && ch != '[' && ch != ']' && ch != '-' && ch != EOS;
    }
}

