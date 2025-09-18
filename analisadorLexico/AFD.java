package analisadorLexico;
import java.text.CharacterIterator;

public abstract class AFD {
    
    public abstract Token evaluate(CharacterIterator code); 

    //simbolo para parar reconhecimento do token e finaliza cm caracteres que leu
    public boolean isTokenSeparator(CharacterIterator code){
        return code.current() == ' ' ||
            code.current() == '+' ||
            code.current() == '-' ||
            code.current() == '*' ||
            code.current() == '/' ||
            code.current() == '(' ||
            code.current() == ')' ||
            code.current() == ',' ||
            code.current() == ';' ||
            code.current() == '\n' ||
            code.current() == CharacterIterator.DONE; //chega no fim do arquivo/codigo 
    }
}
