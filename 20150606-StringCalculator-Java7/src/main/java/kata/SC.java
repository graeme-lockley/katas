package kata;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    INPUT :== EOS
            | NUM { ',' NUM } EOS
            | '/' '/' INPUTTAIL
    INPUTTAIL :== SEPARATOR '\n' NUM { SEPARATOR NUM } EOS
                | '[' MULTISEPARATOR ']' { '[' MULTISEPARATOR ']' } '\n' NUM { MULTISEPARATOR NUM } EOS
    NUM :== NEGNUM
          | POSNUM
    NEGNUM = '-' POSNUM
    POSNUM = DIGIT { DIGIT }
    MULTISEPARATOR = SEPARATOR { SEPARATOR }
    DIGIT = '0' | ... | '9'
    SEPARATOR = any - ('0', ..., '9', 0C, '[', '-')
 */
public class SC {
    private Lexer lexer;

    public static int add(String input) throws ParseException {
        return new SC(new Lexer(input)).add();
    }

    public SC(Lexer lexer) {
        this.lexer = lexer;
    }

    public int add() throws ParseException {
        return parseINPUT();
    }

    private int parseINPUT() throws ParseException {
        List<Integer> negs = new ArrayList<>();
        int result;

        if (lexer.head() == Lexer.EOS) {
            result = 0;
        } else if (lexer.head() == '-' || Character.isDigit(lexer.head())) {
            result = parseNUM(negs);
            while (lexer.head() == ',' || lexer.head() == '\n') {
                lexer.skip();
                result += parseNUM(negs);
            }
        } else if (lexer.head() == '/') {
            lexer.skip();
            match('/');

            result = parseINPUTTAIL(negs);
        } else {
            throw lexer.exception("Expected '-', '/' or digit");
        }

        if (lexer.head() == Lexer.EOS) {
            if (negs.isEmpty()) {
                return result;
            } else {
                throw new IllegalArgumentException(mkString(negs));
            }
        } else {
            throw lexer.exception("Expected EOS");
        }
    }

    private int parseINPUTTAIL(List<Integer> negs) throws ParseException {

        if (firstOfSEPARATOR(lexer.head())) {
            char separator = parseSEPARATOR();
            match('\n');

            int result = parseNUM(negs);
            while (firstOfSEPARATOR(lexer.head())) {
                char nextSEPARATOR = parseSEPARATOR();
                if (separator == nextSEPARATOR) {
                    result += parseNUM(negs);
                } else {
                    throw lexer.exception("Unknown separator");
                }
            }
            return result;
        } else if (lexer.head() == '[') {
            Set<String> separators = new HashSet<>();

            lexer.skip();
            separators.add(parseMULTISEPARATOR());
            match(']');

            while (lexer.head() == '[') {
                lexer.skip();
                separators.add(parseMULTISEPARATOR());
                match(']');
            }
            match('\n');

            int result = parseNUM(negs);
            while (firstOfSEPARATOR(lexer.head())) {
                String nextSEPARATOR = parseMULTISEPARATOR();
                if (separators.contains(nextSEPARATOR)) {
                    result += parseNUM(negs);
                } else {
                    throw lexer.exception("Unknown separator");
                }
            }
            return result;
        } else {
            throw lexer.exception("Expected separator or '['");
        }
    }

    private String parseMULTISEPARATOR() throws ParseException {
        StringBuilder result = new StringBuilder();

        if (firstOfSEPARATOR(lexer.head())) {
            result.append(lexer.head());
            lexer.skip();
            while (firstOfSEPARATOR(lexer.head())) {
                result.append(lexer.head());
                lexer.skip();
            }
            return result.toString();
        } else {
            throw lexer.exception("Expected separator");
        }
    }

    private String mkString(List<Integer> negs) {
        StringBuilder sb = new StringBuilder();
        for (Integer neg : negs) {
            sb.append(',').append(neg);
        }
        return sb.substring(1);
    }

    private char parseSEPARATOR() throws ParseException {
        if (firstOfSEPARATOR(lexer.head())) {
            char result = lexer.head();
            lexer.skip();
            return result;
        } else {
            throw lexer.exception("Expected separator");
        }
    }

    private boolean firstOfSEPARATOR(char ch) {
        return !Character.isDigit(ch) && ch != Lexer.EOS && ch != '[' && ch != '-' && ch != ']';
    }

    private int parseNUM(List<Integer> negs) throws ParseException {
        if (lexer.head() == '-') {
            return parseNEGNUM(negs);
        } else if (Character.isDigit(lexer.head())) {
            int result = parsePOSNUM();
            return result < 1001 ? result : 0;
        } else {
            throw lexer.exception("Expected '-' or digit");
        }
    }

    private int parseNEGNUM(List<Integer> negs) throws ParseException {
        if (lexer.head() == '-') {
            lexer.skip();
            int result = -parsePOSNUM();
            negs.add(result);
            return result;
        } else {
            throw lexer.exception("Expected '-'");
        }
    }

    private int parsePOSNUM() throws ParseException {
        if (Character.isDigit(lexer.head())) {
            int result = parseDIGIT();
            while (Character.isDigit(lexer.head())) {
                result = result * 10 + parseDIGIT();
            }
            return result;
        } else {
            throw lexer.exception("Expected digit");
        }
    }

    private int parseDIGIT() throws ParseException {
        if (Character.isDigit(lexer.head())) {
            int result = ((int) lexer.head()) - ((int) '0');
            lexer.skip();
            return result;
        } else {
            throw lexer.exception("Expected digit");
        }
    }

    private void match(char c) throws ParseException {
        if (lexer.head() == c) {
            lexer.skip();
        } else {
            throw lexer.exception("Expected " + c);
        }
    }
}
