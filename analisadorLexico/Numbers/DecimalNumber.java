package analisadorLexico.Numbers;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.LexicalException;
import analisadorLexico.Token;

//esse inclui int e float (separar em 2)
public class DecimalNumber extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        int start = code.getIndex();
        int curIndex = start;
        StringBuilder number = new StringBuilder();

        boolean hasDigitsBeforeDot = false;
        boolean hasDot = false;
        boolean hasDigitsAfterDot = false;

        // Parte inteira opcional
        while (Character.isDigit(code.current())) {
            hasDigitsBeforeDot = true;
            number.append(code.current());
            code.next();
            curIndex++;
        }

        // Verifica se há ponto
        if (code.current() == '.') {
            hasDot = true;
            number.append('.');
            code.next();
            curIndex++;

            // Parte fracionária obrigatória após o ponto
            while (Character.isDigit(code.current())) {
                hasDigitsAfterDot = true;
                number.append(code.current());
                code.next();
                curIndex++;
            }
        }

        // Se tem ponto e pelo menos um dígito em algum lado
        if (hasDot && (hasDigitsBeforeDot || hasDigitsAfterDot)) {
                    
            if (isTokenSeparator(code)) {
                if (!hasDigitsBeforeDot &&hasDigitsAfterDot) {
                    number.insert(0, '0');
                } else if (hasDigitsBeforeDot && !hasDigitsAfterDot) {
                    number.append('0');
                }
                curIndex++;
                return new Token("DECIMAL", number.toString());
            } else {
                int[] lc = computeLineColumn(curIndex);
                String lineText = extractLineText(lc[0]);

                throw new LexicalException("Numero decimal invalido.", lc[0], lc[1], lineText);
            }
        }

        // Se não formou decimal válido, volta o ponteiro
        code.setIndex(start);
        return null;
    }

}