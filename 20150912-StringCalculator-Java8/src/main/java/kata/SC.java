package kata;

/*
    INPUT ::= EOS
            | INTEGER { ( ',' | '\n' )  INTEGER } EOS
            | '/' '/' CUSTOM_TAIL

    CUSTOM_TAIL ::= SEPARATOR '\n' INTEGER { SEPARATOR INTEGER } EOS
                  | '[' SEPARATORS ']' { '[' SEPARATORS ']' } '\n' INTEGER { SEPARATORS INTEGER } EOS

    INTEGER ::= NEGATIVE_INTEGER
              | POSITIVE_INTEGER

    NEGATIVE_INTEGER ::= '-' POSITIVE_INTEGER

    POSITIVE_INTEGER ::= DIGIT { DIGIT }

    DIGIT ::= '0' | ... | '9'

    SEPARATORS ::= SEPARATOR { SEPARATOR }
    SEPARATOR ::= (1C | ... | 255C) - ( '-' | '[' | ']' | '0' | ... | '9' )
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SC {
    private Lexer lexer;

    public SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public static int add(String input) {
        System.out.println(input);
        return new SC(new Lexer(input)).parseINPUT();
    }

    private int parseINPUT() {
        List<Integer> negatives = new ArrayList<Integer>();
        int result = 0;

        if (lexer.next() == Lexer.EOS) {
            result = 0;
        } else if (lexer.next() == '-' || Character.isDigit(lexer.next())) {
            result = parseINTEGER(negatives);

            while (lexer.next() == ',' || lexer.next() == ':') {
                lexer.skip();
                result += parseINTEGER(negatives);
            }

            matchCharacter(Lexer.EOS);
        } else if (lexer.next() == '/') {
            lexer.skip();
            matchCharacter('/');

            result = parseCUSTOM_TAIL(negatives);
        } else {
            throw parseException("Expected '-', digit or EOS");
        }

        if (negatives.isEmpty()) {
            return result;
        } else {
            throw new IllegalArgumentException(negatives.stream().map(Object::toString).collect(Collectors.joining(",")));
        }
    }

    private int parseCUSTOM_TAIL(List<Integer> negatives) {
        int result;
        if (isNextSeparator()) {
            char separator = parseSEPARATOR();
            matchCharacter('\n');
            result = parseINTEGER(negatives);

            while (lexer.next() != Lexer.EOS) {
                char nextSeparator = parseSEPARATOR();

                if (separator == nextSeparator) {
                    result += parseINTEGER(negatives);
                } else {
                    throw parseException("The separator " + nextSeparator + " does not match the defined separator");
                }
            }

            matchCharacter(Lexer.EOS);
        } else if (lexer.next() == '[') {
            lexer.skip();
            Set<String> separators = new HashSet<>();

            separators.add(parseSEPARATORS());
            matchCharacter(']');

            while (lexer.next() == '[') {
                lexer.skip();
                separators.add(parseSEPARATORS());
                matchCharacter(']');
            }

            matchCharacter('\n');
            result = parseINTEGER(negatives);

            while (lexer.next() != Lexer.EOS) {
                String nextSeparator = parseSEPARATORS();

                if (separators.contains(nextSeparator)) {
                    result += parseINTEGER(negatives);
                } else {
                    throw parseException("The separator " + nextSeparator + " does not match the defined separators");
                }
            }

            matchCharacter(Lexer.EOS);
        } else {
            throw parseException("Expected valid separator character or '['");
        }
        return result;
    }

    private String parseSEPARATORS() {
        StringBuilder result = new StringBuilder();

        result.append(parseSEPARATOR());
        while (isNextSeparator()) {
            result.append(parseSEPARATOR());
        }

        return result.toString();
    }

    private char parseSEPARATOR() {
        if (isNextSeparator()) {
            char result = lexer.next();
            lexer.skip();
            return result;
        } else {
            throw parseException("Expected valid separator character");
        }
    }

    private boolean isNextSeparator() {
        return lexer.next() != Lexer.EOS && !Character.isDigit(lexer.next()) && lexer.next() != '[' && lexer.next() != ']';
    }

    private int parseINTEGER(List<Integer> negatives) {
        if (lexer.next() == '-') {
            lexer.skip();
            final int result = -parsePOSITIVE_INTEGER();
            if (result < 0) {
                negatives.add(result);
            }
            return result;
        } else if (Character.isDigit(lexer.next())) {
            final int result = parsePOSITIVE_INTEGER();
            return result < 1001 ? result : 0;
        } else {
            throw parseException("Expected '-' or digit");
        }
    }

    private int parsePOSITIVE_INTEGER() {
        int result = parseDIGIT();

        while (Character.isDigit(lexer.next())) {
            result = result * 10 + parseDIGIT();
        }

        return result;
    }

    private int parseDIGIT() {
        if (Character.isDigit(lexer.next())) {
            int result = ((int) lexer.next()) - ((int) '0');
            lexer.skip();
            return result;
        } else {
            throw parseException("Expected digit");
        }
    }

    private void matchCharacter(char character) {
        if (lexer.next() == character) {
            lexer.skip();
        } else {
            throw parseException("Expected " + character);
        }
    }

    private RuntimeException parseException(String message) {
        return lexer.error(message);
    }
}

