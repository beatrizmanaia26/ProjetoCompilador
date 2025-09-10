package analisadorLexico;
import java.text.CharacterIterator;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;
import analisadorLexico.LogicOperator.LogicOperator;
import analisadorLexico.MathOperators.MathOperator;
import analisadorLexico.Numbers.DecimalNumbers;
import analisadorLexico.Numbers.IntegerNumber;
import analisadorLexico.ReservedWords.ReservedWords;

public class Lexer {
    private List<Token> tokens;
    private List<AFD> afds;
    private CharacterIterator code;
    private int line;
    
    public Lexer(String code){
        tokens = new ArrayList<>();
        afds = new ArrayList<>();
        this.code = new StringCharacterIterator(code);
        this.line = 1;
        //ao inves disso, fazer metodo set afds e na main passo todos afds
        afds.add(new MathOperator());
        afds.add(new LogicOperator());
        afds.add(new ReservedWords());
        afds.add(new IntegerNumber());
        afds.add(new DecimalNumbers());
        
    }
     
    public void skipWhiteSpace(){
        while(code.current() == ' ' || code.current() == '\n' || code.current() == '\r'){ //quebra de linha (\r \n)
             if (code.current() == '\n') {
                line++;
            }
            code.next(); 
        }
    }
    public void error(){
        throw new RuntimeException("Token not recognized"+ code.current()+ "at line" + line);
    }
    
    public List<Token> getTokens(){
        Token t;
        do{
            skipWhiteSpace();
            t = searchNextToken();
            if (t == null) error(); 
            tokens.add(t); 
        }while(!t.tipo.equals("EOF"));
        return tokens;
    }

    private Token searchNextToken(){
        int position = code.getIndex(); //salva indice de reconhecimento (posicao atual)
        for (AFD afd : afds){
            Token t = afd.evaluate(code);
            if(t != null) return t;
            code.setIndex(position);
        }
        return null;
    }
} 
