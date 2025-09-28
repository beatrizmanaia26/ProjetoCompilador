package analisadorLexico;
import java.text.CharacterIterator;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;

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
    private List<Token> tokens;
    private List<AFD> afds;
    private CharacterIterator code;
    private int line;
    
    public Lexer(String code){
        tokens = new ArrayList<>();
        afds = new ArrayList<>();
        this.code = new StringCharacterIterator(code);

        //ao inves disso, fazer metodo set afds e na main passo todos afds
        afds.add(new AssignmentOperator());
        afds.add(new RelationalOperators());
        afds.add(new LogicOperator());
        afds.add(new Identifiers());
        afds.add(new Comment());
        afds.add(new MathOperator());
        afds.add(new ReservedWords());
        afds.add(new IntegerNumber());
        afds.add(new DecimalNumber());
        afds.add(new Text());
        afds.add(new FunctionName());
        afds.add(new Delimiters());
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
        throw new RuntimeException("Token não reconhecido "+ code.current()+ "na linha " + line + " no índice " + code.getIndex());
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

    public int getLine() {
    return line;
    }

    public void incrementLine() {
        line++;
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
