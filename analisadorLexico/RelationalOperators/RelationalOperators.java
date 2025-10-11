package analisadorLexico.RelationalOperators;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

public class RelationalOperators extends AFD {

    @Override
    public Token evaluate(CharacterIterator code,Lexer lexer) {
        StringBuilder word = new StringBuilder();

        if (code.current() == '>') {
            word.append('>');
            code.next();

            if (code.current() == '=') { 
                word.append('=');
                code.next();

                // retorna >=
                return new Token("OP_REL", word.toString());
            } else {
                // retorna >
                return new Token("OP_REL", word.toString());
            }
            
        }

        if (code.current() == '<') {
            word.append('<');
            code.next();

            if (code.current() == '>') { 
                word.append('>');
                code.next();

                // retorna <>
                return new Token("OP_REL", word.toString());

            } else if (code.current() == '=') {
                word.append('=');
                code.next();
                
                // retorna <=
                return new Token("OP_REL", word.toString());

            } else if (code.current() == '-') { 
                word.append('-');
                code.next();

                if (code.current() == '>') {
                    word.append('>');
                    code.next();
                    
                    // retorna <->
                    return new Token("OP_REL", word.toString());
                }
            } else { 
                // retorna <
                return new Token("OP_REL", word.toString());
            }
        }

        return null;
    }
}