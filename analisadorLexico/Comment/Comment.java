package analisadorLexico.Comment;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.LexicalException;
import analisadorLexico.Token;

public class Comment extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder comment = new StringBuilder();
        int start = code.getIndex();
        int curIndex = start;

        if (code.current() == '#') {
            code.next();
            curIndex++;

            if (!readPrefixSufix(code, "uai...")) {
                // comentário de linha
                while (code.current() != '\n') {
                    comment.append(code.current());
                    code.next();
                    curIndex++;
                }

                return new Token("COMMENT", comment.toString());

            } else {
                curIndex += 5; // avançar o índice para após "uai..."
                
                // comentário de bloco
                while (!readPrefixSufix(code, "...sô#")) {
                    comment.append(code.current());
                    code.next();
                    curIndex++;

                    if (code.current() == CharacterIterator.DONE) {
                        int[] lc = computeLineColumn(start);
                        String lineText = extractLineText(lc[0]);

                        throw new LexicalException("Erro lexico: comentario de bloco nao fechado corretamente (esperado '...sô#' antes do fim da proxima instrucao valida)", lc[0], lc[1], lineText);
                    }
                }

                return new Token("COMMENT", comment.toString());

            }

        }

        return null;
    }

    private boolean readPrefixSufix(CharacterIterator code, String expected) {
        for (char e : expected.toCharArray()) {
            if (code.current() != e) {
                return false;
            }
            code.next();
        }
        return true;
    }

}
