import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class EndOfLine extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        if (code.current() == ';') {
            code.next();
            return new Token("END_LINE", ";");
        }
        return null;
    }
}
