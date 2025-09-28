package analisadorLexico.Text;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

public class Text extends AFD {
	@Override
	public Token evaluate(CharacterIterator code) {
		if (code.current() == '"') {
			code.next(); // pula o " inicial
			StringBuilder palavra = new StringBuilder();
			while (code.current() != '"' && code.current() != CharacterIterator.DONE) {
				palavra.append(code.current());
				code.next();
			}
			if (code.current() == '"') {
				code.next(); // pula o " final
				if (isTokenSeparator(code)) {
					return new Token("TEXT", palavra.toString());
				}
			}
		}
		return null;
	}
}
