package analisadorLexico.LogicOperator;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

public class LogicOperator extends AFD{
    @Override
    public Token evaluate(CharacterIterator code) {
        if (code.current() == 'e') {
            code.next();
            return new Token("OPR_LOGIC", "e");
        } 
        else if (code.current() == 'o') {
            code.next();
            if (code.current() == 'u') {
                code.next();
                return new Token("OPR_LOGIC", "ou");
            }
        } 
        return null;
    }
}