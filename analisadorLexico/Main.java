package analisadorLexico;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main{
    public static void main(String[] args){
        String code = "";
       // String nomeArquivo = "/workspaces/ProjetoCompilador/analisadorLexico/script.txt"; 
        String nomeArquivo = "C:\\ProjetoCompilador\\analisadorLexico\\script.txt"; 
        File arquivo = new File(nomeArquivo);
        Scanner scanner = null;
        try {
            scanner = new Scanner(arquivo); 

            while (scanner.hasNextLine()) { 
                String linha = scanner.nextLine(); 
                code += linha  + "\n"; //quebrar em linhas pra mostrar corretamente a linha do erro
                //System.out.println("Linha lida: " + linha);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Erro: O arquivo não foi encontrado.");
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close(); 
            }
        }

        try {
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.getTokens();//add todos os tokens na lista
            tokens.forEach(System.out::println); 
            // Filtrar comentários para nao enviar para o sintatico
           List<Token> filteredTokens = new ArrayList<>();
            for (Token token : tokens) {
                if (!token.tipo.equals("COMMENT")) {
                    filteredTokens.add(token);
                }
            }
            Parser parser = new Parser(filteredTokens);
            parser.main();
        } catch (LexicalException e) {
        System.err.println(e.getMessage());
    }

    }
}