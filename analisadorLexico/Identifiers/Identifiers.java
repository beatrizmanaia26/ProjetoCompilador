package analisadorLexico.Identifiers;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;


public class Identifiers extends AFD{
	@Override
	public Token evaluate(CharacterIterator code,Lexer lexer) {
		// Verifica se começa com 'Trem_'
		int start = code.getIndex();
		String prefix = "Trem_";
		boolean matches = true;
		for (int i = 0; i < prefix.length(); i++) {
			if (code.current() != prefix.charAt(i)) {
				matches = false;
				break;
			}
			code.next();
		}
		if (matches && (Character.isLetterOrDigit(code.current()) || code.current() == '_')) {
			String palavra = prefix + readWord(code);
			if (isTokenSeparator(code)) {
				return new Token("IDENTIFIER", palavra);
			}
		} else {
			// Volta o ponteiro caso não seja 'Trem_'
			code.setIndex(start);
		}
		return null;
	}

	private String readWord(CharacterIterator code) {
		StringBuilder palavra = new StringBuilder();
		while (Character.isLetterOrDigit(code.current()) || code.current() == '_') {
			palavra.append(code.current());
			code.next();
		}
		return palavra.toString();
	}
}