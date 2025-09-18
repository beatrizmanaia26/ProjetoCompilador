package analisadorLexico;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
     /* Token num = new Token("NUM","2");
        Token op = new Token("op_mat","+");
        Token num2 = new Token("NUM","5");
        System.out.println(num);
        System.out.println(op);
        System.out.println(num2);
     */ 
        String code = "";
        
      
        String nomeArquivo = "C:\\ProjetoCompilador\\analisadorLexico\\arquivo.txt"; // Substitua pelo caminho do seu arquivo
        File arquivo = new File(nomeArquivo);
        Scanner scanner = null;

        try {
            scanner = new Scanner(arquivo); 

            while (scanner.hasNextLine()) { 
                String linha = scanner.nextLine(); 
                code += linha; 
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro: O arquivo não foi encontrado.");
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close(); 
            }
        }

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.getTokens();

        for(Token t: tokens){
            System.out.println(t);
        }
    }
}