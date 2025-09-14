package analisadorLexico.Comment;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class Comment extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        if (code.current() == '#') {
            code.next();

            if (!readPrefixSufix(code, "uai...")) {
                code.previous(); 
                return null;
            }

            StringBuilder comment = new StringBuilder("#uai...");

            while (code.current() != CharacterIterator.DONE) {
                char c = code.current();
                comment.append(c);
                code.next();

                if (c == '.') {
                    if (readPrefixSufix(code, "..so#")) {
                        comment.append("..so#");
                        return new Token("COMMENT", comment.toString());
                    }
                }
            }

            throw new RuntimeException("Erro léxico: comentário não fechado corretamente (esperado '...so#').");
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
