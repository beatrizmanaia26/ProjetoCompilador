package analisadorLexico.Numbers;

import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class IntegerNumber extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        // se não começa com dígito, não é numero
        if (code.current() == CharacterIterator.DONE || !Character.isDigit(code.current())) {
            return null;
        }

        StringBuilder number = new StringBuilder();

        while (code.current() != CharacterIterator.DONE && Character.isDigit(code.current())) {
            number.append(code.current());
            code.next();
        }

        return new Token("INTEGER", number.toString());
    }
}
