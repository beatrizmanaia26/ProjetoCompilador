package analisadorLexico.IncrementDecrementOperators;

import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

public class 
IncrementDecrementOperator extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        char current = code.current();

        // Verifica se é '+' ou '-'
        if (current == '+' || current == '-') {
            char symbol = current;
            code.next(); // avança para o próximo caractere

            // Se o próximo caractere for o mesmo (++, --)
            if (code.current() == symbol) {
                code.next();
                if (symbol == '+') {
                    return new Token("INCREMENT", "++");
                } else {
                    return new Token("DECREMENT", "--");
                }
            }

            // Se o próximo caractere for '=', então é += ou -=
            if (code.current() == '=') {
                code.next();
                if (symbol == '+') {
                    return new Token("PLUS_ASSIGN", "+=");
                } else {
                    return new Token("MINUS_ASSIGN", "-=");
                }
            }

            // Se não formar ++, --, += ou -=, retrocede um passo
            code.previous(); 
            return null; // permite que outro AFD trate (+ ou -)
        }

        return null;
    }
    
}
