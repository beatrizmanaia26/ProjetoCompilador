package analisadorLexico.Numbers;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

//esse inclui int e float (separar em 2)
public class DecimalNumber extends AFD{

    @Override
    public Token evaluate(CharacterIterator code){
        if(Character.isDigit(code.current())){
            String number = readNumber(code);
            if(code.current() == '.'){
                number += '.';
                code.next();
                number += readNumber(code);
            }
            if (isTokenSeparator(code)){
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
