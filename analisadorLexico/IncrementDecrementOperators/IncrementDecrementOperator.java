package analisadorLexico.IncrementDecrementOperators;

import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

public class 
IncrementDecrementOperator extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char c = code.current();

        // Incremento "++"
        if (c == '+') {
            code.next();
            if (code.current() == '+') {
                code.next();
                return new Token("OPE_INCR", "++");
            } else {
                code.previous(); // volta para o '+' inicial
                return null; // deixa outro AFD lidar com operador '+'
            }
        }

        // Decremento "--"
        if (c == '-') {
            code.next();
            if (code.current() == '-') {
                code.next();
                return new Token("OPE_DECR", "--");
            } else {
                code.previous(); // volta para o '-' inicial
                return null; // deixa outro AFD lidar com operador '-'
            }
        }

        return null;
    }
    
}
