package analisadorLexico.LogicOperator;
import java.text.CharacterIterator;

import analisadorLexico.AFD;
import analisadorLexico.Token;

public class LogicOperator extends AFD{
    @Override
    public Token evaluate(CharacterIterator code) {
        if (code.current() == ',') {
            code.next();
            return new Token("Virgula", ",");
        } 
        else if (code.current() == ';') {
            code.next();
            return new Token("FimDeInstrucao", ";");
        } 
        else if (code.current() == '-') {
            int pos = code.getIndex();
            code.next();
            if (code.current() == '>') {
                code.next();
                return new Token("OperadorDeAtribuicao", "->");
            } else {
                // Volta para o caractere '-' se não for '>'
                code.setIndex(pos);
            }
        }
        return null;
    }
}