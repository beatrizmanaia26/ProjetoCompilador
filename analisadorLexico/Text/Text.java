package analisadorLexico.Text;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

public class Text extends AFD {

    private int line;

    @Override
    public Token evaluate(CharacterIterator code, Lexer lexer) {
        this.line = 1;
        if (code.current() == '"') {
            code.next();

			StringBuilder text = new StringBuilder();
			
            while (code.current() != CharacterIterator.DONE) {
                char c = code.current();

				if (c == '"') {
					code.next();
                    return new Token("TEXT", text.toString());
                }

                text.append(c);
                code.next();

                if (c == '\n') {
                    lexer.incrementLine(); 
                }
                
                
            }

            throw new RuntimeException("Erro léxico: Falta fechamento de aspas" + " na linha " + lexer.getLine() + "no índice " + code.getIndex());
        }
        return null;
    }


}
