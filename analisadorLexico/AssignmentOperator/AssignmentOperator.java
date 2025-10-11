package analisadorLexico.AssignmentOperator;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Lexer;
import analisadorLexico.Token;

public class AssignmentOperator extends AFD {

    @Override
    public Token evaluate(CharacterIterator code,Lexer lexer){
        char c = code.current();

        if (c == '-') {
            String op = "" + c;
            code.next();

            if (code.current() == '>') { 
                op += '>';
                code.next();

                return new Token("OP_ATR", op);
            }
        }

        return null;
    }
}