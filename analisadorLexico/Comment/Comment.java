package analisadorLexico.Comment;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class Comment extends AFD {

    private int line;

    @Override
    public Token evaluate(CharacterIterator code) {
        this.line = 1;
        if (code.current() == '#') {
            code.next();

            if (!readPrefixSufix(code, "uai...")) {
                throw new RuntimeException("Erro léxico: comentário iniciado de forma incorreta (esperado '#uai...').");
            }

            StringBuilder comment = new StringBuilder("#uai...");

            while (code.current() != CharacterIterator.DONE) {
                char c = code.current();
                comment.append(c);
                code.next();

                if (c == '\n') {
                    line++;  
                }
                
                if (c == '.') {
                    if (readPrefixSufix(code, "..so#")) {
                        comment.append("..so#");
                        return new Token("COMMENT", comment.toString());
                    }
                }
            }

            throw new RuntimeException("Erro léxico: comentário não fechado corretamente (esperado '...so#')." + " na linha " + line + "no índice " + code.getIndex());
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
