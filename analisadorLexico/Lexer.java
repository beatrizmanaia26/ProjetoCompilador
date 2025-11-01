package analisadorLexico;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.ArrayList;

import analisadorLexico.AssignmentOperator.AssignmentOperator;
import analisadorLexico.Comment.Comment;
import analisadorLexico.Delimiters.Delimiters;
import analisadorLexico.FunctionName.FunctionName;
import analisadorLexico.Identifiers.Identifiers;
import analisadorLexico.IncrementDecrementOperators.IncrementDecrementOperator;
import analisadorLexico.LogicOperator.LogicOperator;
import analisadorLexico.MathOperators.MathOperator;
import analisadorLexico.Numbers.DecimalNumber;
import analisadorLexico.Numbers.IntegerNumber;
import analisadorLexico.RelationalOperators.RelationalOperators;
import analisadorLexico.ReservedWords.ReservedWords;
import analisadorLexico.Text.Text;

public class Lexer {
    private List<Token> tokens;
    private List<AFD> afds;
    private CharacterIterator code;
    private int line = 1;
    private int column = 1;

    public Lexer(String code) {
        tokens = new ArrayList<>();
        afds = new ArrayList<>();
        this.code = new StringCharacterIterator(code);

        // Inicializa os AFDs
        afds.add(new AssignmentOperator());
        afds.add(new RelationalOperators());
        afds.add(new Identifiers());
        afds.add(new Comment());
        afds.add(new IncrementDecrementOperator());
        afds.add(new MathOperator());
        afds.add(new ReservedWords());
        afds.add(new LogicOperator());
        afds.add(new FunctionName());
        afds.add(new DecimalNumber());
        afds.add(new IntegerNumber());
        afds.add(new Text());
        afds.add(new Delimiters());
    }

    // Ignora espaços, quebras de linha e tabulações
    public void skipWhiteSpace() {
        while (code.current() == ' ' || code.current() == '\n' || 
               code.current() == '\r' || code.current() == '\t') {
            if (code.current() == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            code.next();
        }
    }

    public List<Token> getTokens() {
        Token t;
        do {
            skipWhiteSpace();

            if (code.current() == CharacterIterator.DONE) {
                tokens.add(new Token("EOF", "$"));
                break;
            }

            t = searchNextToken();
            if (t == null) {
                handleUnrecognizedToken();
            } else {
                tokens.add(t);
            }

        } while (code.current() != CharacterIterator.DONE);

        return tokens;
    }

    private void handleUnrecognizedToken() {
        char invalidChar = code.current();

        // Se chegou ao fim do código, não faz nada
        if (invalidChar == CharacterIterator.DONE) {
            return;
        }

        int errorLine = line;
        int errorIndex = code.getIndex();

        // Avança o ponteiro (para não travar o lexer)
        code.next();
        column++;

        throw new LexicalException(String.format(
            "Erro léxico: token não reconhecido \"%c\" na linha %d, índice %d",
            invalidChar, errorLine, errorIndex
        ));
    }

    private Token searchNextToken() {
        int startIndex = code.getIndex();

        for (AFD afd : afds) {
            int before = code.getIndex();
            Token t = afd.evaluate(code);

            if (t != null) {
                // Atualiza coluna com base no avanço do ponteiro
                column += (code.getIndex() - before);
                return t;
            }

            // se AFD falhou completamente, volta o ponteiro
            code.setIndex(before);
        }

        // Nenhum AFD reconheceu o token
        code.setIndex(startIndex);
        return null;
    }

    public int getLine() {
        return line;
    }

    public void incrementLine() {
        line++;
    }
}
