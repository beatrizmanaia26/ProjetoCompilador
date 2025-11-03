package analisadorLexico;
import java.text.CharacterIterator;

public abstract class AFD {
    
    protected String source;
    public abstract Token evaluate(CharacterIterator code); 

    //simbolo para parar reconhecimento do token e finaliza cm caracteres que leu
    public boolean isTokenSeparator(CharacterIterator code){
        return code.current() == ' ' ||
            code.current() == '+' ||
            code.current() == '-' ||
            code.current() == '*' ||
            code.current() == '/' ||
            code.current() == '^' ||
            code.current() == '(' ||
            code.current() == ')' ||
            code.current() == ',' ||
            code.current() == '<' ||
            code.current() == '>' ||
            code.current() == ';' ||
            code.current() == '\n' ||
            code.current() == '\r' ||
            code.current() == CharacterIterator.DONE; //chega no fim do arquivo/codigo 
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int[] computeLineColumn(int index) {
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

    public String extractLineText(int targetLine) {
        if (source == null) return "";
        String[] lines = source.split("\n", -1);
        if (targetLine - 1 >= 0 && targetLine - 1 < lines.length) {
            return lines[targetLine - 1];
        }
        return "";
    }
}
