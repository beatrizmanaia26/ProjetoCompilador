package analisadorLexico.Numbers;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

//esse inclui int e float (separar em 2)
public class IntegerNumber extends AFD{

    @Override
    public Token evaluate(CharacterIterator code){
        if(Character.isDigit(code.current())){
            String number = readNumber(code);
            if (isTokenSeparator(code)){
                return new Token("INTEGER", number);
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
