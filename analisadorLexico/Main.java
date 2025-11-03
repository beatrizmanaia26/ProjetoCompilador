package analisadorLexico;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import analisadorSintatico.Parser;

public class Main{
    public static void main(String[] args){
        String code = "";

        String nomeArquivo = "./analisadorLexico/script.txt";

        try {
            code = lerArquivo(nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
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

    public static String lerArquivo(String caminhoArquivo) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(caminhoArquivo));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}