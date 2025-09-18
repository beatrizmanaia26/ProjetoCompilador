package analisadorLexico.Delimiters;

import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class Delimiters extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        switch (code.current()) {
            case '{':
                code.next();
                return new Token("ABR_CHV", "{");
            case '}':
                code.next();
                return new Token("FCH_CHV", "}");
            case '(':
                code.next();
                return new Token("ABR_PAR", "(");
            case ')':
                code.next();
                return new Token("FCH_PAR", ")");
            case ';':
                code.next();
                return new Token("END_LINE", ";");
            default:
                return null;
        }
    }
}