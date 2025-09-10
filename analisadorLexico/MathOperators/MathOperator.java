import java.text.CharacterIterator;

public class MathOperator extends AFD{

    @Override
    public Token evaluate(CharacterIterator code){
        switch (code.current()){
            case '+': //fazer case * - /...
                code.next(); 
                return new Token("PLUS","+"); 
            case CharacterIterator.DONE:
                return new Token("EOF", "$"); //$ indica fim do arquivo 
            default:
                return null;
        }
    }
}
