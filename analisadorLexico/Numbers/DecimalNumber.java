package analisadorLexico.Numbers;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

//esse inclui int e float (separar em 2)
public class DecimalNumber extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        int start = code.getIndex();
        StringBuilder number = new StringBuilder();

        boolean hasDigitsBeforeDot = false;
        boolean hasDot = false;
        boolean hasDigitsAfterDot = false;

        // Parte inteira opcional
        while (Character.isDigit(code.current())) {
            hasDigitsBeforeDot = true;
            number.append(code.current());
            code.next();
        }

        // Verifica se há ponto
        if (code.current() == '.') {
            hasDot = true;
            number.append('.');
            code.next();

            // Parte fracionária obrigatória após o ponto
            while (Character.isDigit(code.current())) {
                hasDigitsAfterDot = true;
                number.append(code.current());
                code.next();
            }
        }

        // Se tem ponto e pelo menos um dígito em algum lado
        if (hasDot && (hasDigitsBeforeDot || hasDigitsAfterDot)) {
            if (isTokenSeparator(code)) {
                return new Token("DECIMAL", number.toString());
            } else {
                return null;
            }
        }

        // Se não formou decimal válido, volta o ponteiro
        code.setIndex(start);
        return null;
    }
}