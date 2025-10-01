package analisadorLexico.Numbers;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

//esse inclui int e float (separar em 2)
public class DecimalNumber extends AFD{

    @Override
    public Token evaluate(CharacterIterator code) {
        // caso comece com ponto
        if (code.current() == '.') {
            throw new IllegalArgumentException("Erro: Um número decimal não pode começar com ponto. Função: DecimalNumber");
        }
        if (Character.isDigit(code.current())) {
            String number = readNumber(code);
            // Caso tenha ponto
            if (code.current() == '.') {
                number += '.';
                code.next();
                // caso haja dois pontos seguidos"
                if (code.current() == '.') {
                    throw new IllegalArgumentException("Erro: Dois pontos consecutivos em número decimal. Função: DecimalNumber");
                }
                // Caso termine com ponto
                if (!Character.isDigit(code.current())) {
                    throw new IllegalArgumentException("Erro: Após o ponto deve haver ao menos um dígito inteiro. Função: DecimalNumber");
                }
                number += readNumber(code);
            }
            if (isTokenSeparator(code)) {
                return new Token("DECIMAL", number);
            }
        }
        return null;
    }
    private String readNumber(CharacterIterator code){
        String number = "";
        while(Character.isDigit(code.current())){
            number += code.current();
            code.next();
        }
        return number;
    }
}
