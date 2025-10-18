package analisadorLexico;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import analisadorLexico.AssignmentOperator.AssignmentOperator;
import analisadorLexico.Comment.Comment;
import analisadorLexico.Delimiters.Delimiters;
import analisadorLexico.FunctionName.FunctionName;
import analisadorLexico.Identifiers.Identifiers;
import analisadorLexico.LogicOperator.LogicOperator;
import analisadorLexico.MathOperators.MathOperator;
import analisadorLexico.Numbers.DecimalNumber;
import analisadorLexico.Numbers.IntegerNumber;
import analisadorLexico.RelationalOperators.RelationalOperators;
import analisadorLexico.ReservedWords.ReservedWords;
import analisadorLexico.Text.Text;

public class Lexer {
    private final String source;
    private final List<Token> tokens;
    private final List<AFD> afds;
    private final CharacterIterator code;
    private int line = 1;
    private int column = 1;

    public Lexer(String code) {
        this.source = code;
        this.tokens = new ArrayList<>();
        this.afds = new ArrayList<>();
        this.code = new StringCharacterIterator(code);

        // Inicializa os AFDs
        afds.add(new AssignmentOperator());
        afds.add(new RelationalOperators());
        afds.add(new LogicOperator());
        afds.add(new Identifiers());
        afds.add(new Comment());
        afds.add(new MathOperator());
        afds.add(new ReservedWords());
        afds.add(new FunctionName());
        afds.add(new IntegerNumber());
        afds.add(new DecimalNumber());
        afds.add(new Text());
        afds.add(new Delimiters());

        for (AFD afd : afds) {
            afd.setSource(this.source);
        }
    }

    

    public void skipWhiteSpace() {
        while (code.current() == ' ' || code.current() == '\n' || code.current() == '\r' || code.current() == '\t') {
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
            t = searchNextToken();
            if (t == null) {
                handleUnrecognizedToken();
            } else {
                tokens.add(t);
            }
        } while (t == null || !t.tipo.equals("EOF"));
        return tokens;
    }

    private void handleUnrecognizedToken() {
        char invalidChar = code.current();
        if (invalidChar == CharacterIterator.DONE) return;

        int idx = code.getIndex();
        int[] lc = computeLineColumn(idx);
        String lineText = extractLineText(lc[0]);

        code.next();
        column++;

        throw new LexicalException(
            String.format("Token não reconhecido \"%c\"", invalidChar),
            lc[0], lc[1], lineText
        );
    }

    private Token searchNextToken() {
        int startIndex = code.getIndex();

        for (AFD afd : afds) {
            int before = code.getIndex();
            Token t = afd.evaluate(code);
            int after = code.getIndex();

            if (t != null) {
                column += (after - before);
                return t;
            }

            code.setIndex(before); // restaura se não reconheceu nada
        }

        code.setIndex(startIndex);
        return null;
    }

    /** Calcula linha e coluna a partir do índice global */
    private int[] computeLineColumn(int index) {
        int l = 1, c = 1;
        for (int i = 0; i < index && i < source.length(); i++) {
            if (source.charAt(i) == '\n') {
                l++;
                c = 1;
            } else {
                c++;
            }
        }
        return new int[]{l, c};
    }

    /** Retorna o texto da linha especificada */
    private String extractLineText(int targetLine) {
        String[] lines = source.split("\n", -1);
        if (targetLine - 1 >= 0 && targetLine - 1 < lines.length) {
            return lines[targetLine - 1];
        }
        return "";
    }

    public int getLine() {
        return line;
    }

    public void incrementLine() {
        line++;
    }
}
