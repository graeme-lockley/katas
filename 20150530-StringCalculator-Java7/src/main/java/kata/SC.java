package kata;

/*
  GRAMMAR:

        INPUT :== EOS | CUSTOMINTS EOS | INTS EOS
        CUSTOMINTS :== '/' '/' CUSTOMINTSTAIL
        CUSTOMINTSTAIL :== SEPARATOR '\n INT { SEPARATOR INT }
                         | SEPARATORS { SEPARATORS } '\n INT { SEPARATORS INT }
        INTS :== INT { ',' INT }
        INT :== PINT | NINT
        PINT :== DIGIT {DIGIT}
        NINT :== '-' PINT
        DIGIT :== '0' | ... | '9'

        SEPARATORS :== '[' SEPARATOR { SEPARATOR } ']'
        SEPARATOR :== (char(1) | .. | char(255)) - ('0' | ... | '9'| '[' | ']' )
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SC {
    private LexerStream lexer;

    public SC(LexerStream lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) throws ParseException {
        return new SC(new LexerStream(input)).parseINPUT();
    }

    private int parseINPUT() throws ParseException {
        int result;
        List<Integer> negatives = new ArrayList<>();

        if (lexer.head() == LexerStream.EOS) {
            result = 0;
        } else if (lexer.head() == '/') {
            result = parseCUSTOMINTS(negatives);
        } else if (lexer.head() == '-' || Character.isDigit(lexer.head())) {
            result = parseINTS(negatives);
        } else {
            throw lexer.exception("Expected '-' or a digit");
        }
        match(LexerStream.EOS);

        if (negatives.isEmpty()) {
            return result;
        } else {
            throw new IllegalArgumentException(format(negatives));
        }
    }

    private int parseCUSTOMINTS(List<Integer> negatives) throws ParseException {
        match('/');
        match('/');
        return parseCUSTOMINTSTAIL(negatives);
    }

    private int parseCUSTOMINTSTAIL(List<Integer> negatives) throws ParseException {
        if (isNextSeparator()) {
            char ch = parseSEPARATOR();
            match('\n');
            int result = parseINT(negatives);
            while (lexer.head() == ch) {
                lexer.popHead();
                result += parseINT(negatives);
            }
            return result;
        } else if (lexer.head() == '[') {
            Set<String> separators = new HashSet<>();
            match('[');
            separators.add(parseSEPARATORS());
            match(']');
            while (lexer.head() == '[') {
                match('[');
                separators.add(parseSEPARATORS());
                match(']');
            }
            match('\n');
            int result = parseINT(negatives);
            while (isNextSeparator()) {
                String nextSeparator = parseSEPARATORS();
                if (!separators.contains(nextSeparator)) {
                    throw new ParseException("Invalid separator: expected " + separators + " but found" + nextSeparator);
                }
                result += parseINT(negatives);
            }
            return result;
        } else {
            throw lexer.exception("Expected a separator or a '['");
        }
    }

    private String parseSEPARATORS() throws ParseException {
        StringBuilder separators = new StringBuilder();
        separators.append(parseSEPARATOR());
        while (isNextSeparator()) {
            separators.append(parseSEPARATOR());
        }
        return separators.toString();
    }

    private char parseSEPARATOR() throws ParseException {
        if (isNextSeparator()) {
            char sep = lexer.head();
            lexer.popHead();
            return sep;
        } else {
            throw lexer.exception("Expected a valid separator character");
        }
    }

    private boolean isNextSeparator() {
        return lexer.head() >= (char) 1 && lexer.head() <= (char) 255 && !Character.isDigit(lexer.head()) && lexer.head() != '[' && lexer.head() != ']';
    }

    private int parseINTS(List<Integer> negatives) throws ParseException {
        int result = parseINT(negatives);

        while (lexer.head() == ',' || lexer.head() == '\n') {
            lexer.popHead();
            result += parseINT(negatives);
        }

        return result;
    }

    private int parseINT(List<Integer> negatives) throws ParseException {
        if (lexer.head() == '-') {
            int number = parseNINT();
            negatives.add(number);
            return number;
        } else {
            int number = parsePINT();
            return number <= 1000 ? number : 0;
        }
    }

    private int parseNINT() throws ParseException {
        match('-');
        return -parsePINT();
    }

    private int parsePINT() {
        int result = parseDIGIT();
        while (Character.isDigit(lexer.head())) {
            result = result * 10 + parseDIGIT();
        }
        return result;
    }

    private int parseDIGIT() {
        int result = (int) lexer.head() - (int) '0';
        lexer.popHead();
        return result;
    }

    private String format(List<Integer> negatives) {
        StringBuilder sb = new StringBuilder();
        for (int negative : negatives) {
            sb.append(',').append(negative);
        }
        return sb.substring(1);
    }

    private void match(char matchChar) throws ParseException {
        if (lexer.head() == matchChar) {
            lexer.popHead();
        } else {
            throw lexer.exception("Expected " + (matchChar == LexerStream.EOS ? "EOS" : Character.toString(matchChar)));
        }
    }
}
