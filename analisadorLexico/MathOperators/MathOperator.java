package analisadorLexico.MathOperators;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

public class MathOperator extends AFD{

    @Override
    public Token evaluate(CharacterIterator code,Lexer lexer){
        switch (code.current()){
            case '+': 
                code.next(); 
                return new Token("PLUS","+"); 
             case '-': 
                code.next(); 
                return new Token("LESS","-"); 
             case '*': 
                code.next(); 
                return new Token("MULTI","*"); 
             case '^': 
                code.next(); 
                return new Token("POWER","^"); 
             case '/': 
                code.next(); 
                return new Token("DIV","/"); 
            case CharacterIterator.DONE:
                return new Token("EOF", "$"); //$ indica fim do arquivo 
            default:
                return null;
        }
    }
}
