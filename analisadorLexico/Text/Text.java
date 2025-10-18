package analisadorLexico.Text;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;
import analisadorLexico.LexicalException;

public class Text extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder sb = new StringBuilder();

        // só processa se começar com aspas duplas
        if (code.current() == '"') {
            int startIndex = code.getIndex(); // posição da aspa inicial
            int curIndex = startIndex;
            code.next();

            while (code.current() != CharacterIterator.DONE) {
                
                if (code.current() == '"') {
                    code.next(); // consome a aspa final
                    curIndex++;
                    return new Token("TEXT", sb.toString());
                }

                sb.append(code.current());
                code.next();
                curIndex++;
            }
            // chegou ao fim sem fechar as aspas
            if (code.current() != '"') {
                int[] lc = computeLineColumn(curIndex);
                String lineText = extractLineText(lc[0]);

                throw new LexicalException("String não fechada corretamente.", lc[0], lc[1], lineText);

            } 
        }

        return null;

    }

    // --- Métodos auxiliares para localizar posição e linha ---
    private int[] computeLineColumn(int index) {
        int line = 1, column = 1;
        if (source == null) return new int[]{line, column};
        for (int i = 0; i < index && i < source.length(); i++) {
            if (source.charAt(i) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        return new int[]{line, column};
    }

    private String extractLineText(int targetLine) {
        if (source == null) return "";
        String[] lines = source.split("\n", -1);
        if (targetLine - 1 >= 0 && targetLine - 1 < lines.length) {
            return lines[targetLine - 1];
        }
        return "";
    }
}