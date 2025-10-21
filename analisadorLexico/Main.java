package analisadorLexico;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            List<Token> tokens = lexer.getTokens();
            tokens.forEach(System.out::println);
        } catch (LexicalException e) {
        System.err.println(e.getMessage());
        }

    }

    public static String lerArquivo(String caminhoArquivo) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(caminhoArquivo));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}