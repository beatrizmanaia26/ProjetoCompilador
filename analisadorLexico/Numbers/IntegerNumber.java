package analisadorLexico.Numbers;

import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.LexicalException;
import analisadorLexico.Token;

public class IntegerNumber extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        // se não começa com dígito, não é numero
        if (code.current() == CharacterIterator.DONE || !Character.isDigit(code.current())) {
            return null;
        }

        StringBuilder number = new StringBuilder();
        int start = code.getIndex();
        int curIndex = start;

        while (Character.isDigit(code.current())) {
            number.append(code.current());
            code.next();
            curIndex++;
        }

        if (isTokenSeparator(code)) {
            return new Token("INTEGER", number.toString());
        } else if (code.current() == '.') {
            // pode ser um decimal, então retorna null para o próximo AFD tentar
            code.setIndex(start);
            return null;
        } else {
            int[] lc = computeLineColumn(curIndex);
            String lineText = extractLineText(lc[0]);

            throw new LexicalException("Numero inteiro invalido.", lc[0], lc[1], lineText);
        }        
    }
}
