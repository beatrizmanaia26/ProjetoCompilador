import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class RelationalOperators extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder word = new StringBuilder();

        if (code.current() == '>') {
            word.append('>');
            code.next();

            if (code.current() == '=') { 
                word.append('=');
                code.next();

                return new Token("OP_REL", word.toString());
            } else {
                return new Token("OP_REL", word.toString());
            }
            
        }

        if (code.current() == '<') {
            word.append('<');
            code.next();

            if (code.current() == '>') { 
                word.append('>');
                code.next();

                return new Token("OP_REL", word.toString());

            } else if (code.current() == '=') {
                word.append('=');
                code.next();
                
                return new Token("OP_REL", word.toString());

            } else if (code.current() == '-') { 
                word.append('-');
                code.next();

                if (code.current() == '>') {
                    word.append('>');
                    code.next();
                    
                    return new Token("OP_REL", word.toString());
                }
            } else { 
                return new Token("OP_REL", word.toString());
            }
        }

        return null;
    }
}
