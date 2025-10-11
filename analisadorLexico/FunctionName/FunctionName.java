package analisadorLexico.FunctionName;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

public class FunctionName extends AFD{
    
    @Override
    public Token evaluate(CharacterIterator code,Lexer lexer){
        int startPosition = code.getIndex();

        if (!Character.isUpperCase(code.current())) { // delimitador de nome funcao tem que começar com letra maiúscula
            return null;
        }

        StringBuilder word = new StringBuilder();
        char c = code.current();
        
        while ((c = code.current()) != CharacterIterator.DONE && (Character.isLetterOrDigit(c) || c == '_')){ // Continua lendo enquanto for letra, número ou _
            word.append(c);
            c = code.next();
        }

        if (isTokenSeparator(code)) {
            return new Token("FUNCTION_NAME", word.toString());
        }
        code.setIndex(startPosition);
        throw new RuntimeException( "Nome de função incorreto, deve começar com letra maiúscula mas está '"
        + word.toString() );
    }
}
