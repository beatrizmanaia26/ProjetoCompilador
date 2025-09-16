package analisadorLexico.FunctionName;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class FunctionName extends AFD{
    private int line;
    
    @Override
    public Token evaluate(CharacterIterator code) {
        int startPosition = code.getIndex();
        this.line = 1;

        if (!Character.isUpperCase(code.current())) { // delimitador de nome funcao tem que começar com letra maiúscula
            return null;
        }

        StringBuilder word = new StringBuilder();
        word.append(code.current());
        char c = code.next();

        while (Character.isLetterOrDigit(c) || c == '_') { // Continua lendo enquanto for letra, número ou _
            word.append(c);
            c = code.next();
        }

        if (isTokenSeparator(code)) {
            return new Token("FUNCTION_NAME", word.toString());
        }
        code.setIndex(startPosition);
        throw new RuntimeException("Nome de função incorreto"+ code.current()+ "na linha" + line +"no índice"+ code.getIndex());
    }
}
