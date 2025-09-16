package analisadorLexico;
import java.util.List;

public class Main{
    public static void main(String[] args){
     /* Token num = new Token("NUM","2");
        Token op = new Token("op_mat","+");
        Token num2 = new Token("NUM","5");
        System.out.println(num);
        System.out.println(op);
        System.out.println(num2);
     */ 
        String code = "criar Inteiro() criar Texto9 \n";
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.getTokens();

        for(Token t: tokens){
            System.out.println(t);
        }
      
    }
}