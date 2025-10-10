package analisadorLexico.Numbers;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

//esse inclui int e float (separar em 2)
public class DecimalNumber extends AFD{

    // @Override
    // public Token evaluate(CharacterIterator code){
    //     if(Character.isDigit(code.current())){
    //         String number = readNumber(code);
    //         if(code.current() == '.'){
    //             number += '.';
    //             code.next();
    //             number += readNumber(code);
    //         }
    //         if (isTokenSeparator(code)){
    //             return new Token("DECIMAL", number);
    //         }
    //     }
    //     return null;
    // }
    // private String readNumber(CharacterIterator code){
    //     String number = "";
    //     while(Character.isDigit(code.current())){
    //         number += code.current();
    //         code.next();
    //     }
    //     return number;
    // }

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

            if (code.current() == '.') {
                number.append(code.current());
                code.next();

                // Após o ponto, deve haver pelo menos um dígito
                if (code.current() == CharacterIterator.DONE || !Character.isDigit(code.current())) {
                    return null; // Formato inválido
                }

                while (code.current() != CharacterIterator.DONE && Character.isDigit(code.current())) {
                    number.append(code.current());
                    code.next();
                }
                break; // Número decimal completo
            }
        }

        return new Token("DECIMAL", number.toString());
    }
}
