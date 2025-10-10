package analisadorLexico.ReservedWords;
import java.text.CharacterIterator;
import analisadorLexico.AFD;
import analisadorLexico.Token;

public class ReservedWords extends AFD{

    private String[] reservedWords = {
        "se", "ouSe","senao","para","lacoEnquanto","criar","retorna",
         "inteiro","decimal", "texto", "verdadeiroFalso",
         "true", "false","Imprima","Entrada"
    };

    @Override
    public Token evaluate(CharacterIterator code){
        int startPosition = code.getIndex(); 

        if (!Character.isLetter(code.current())) {
            return null;
        }
         StringBuilder word = new StringBuilder(); //constroi palavra para comparar com palavrasreservadas
         char c = code.current();
         while((c = code.current()) != CharacterIterator.DONE && Character.isLetter(c)){
            word.append(c);
            c = code.next();
            
        }
        String strWord = word.toString();
        //itera pela lista de palavras reservadas pra ver se é igual ao que foi digitado
        for (String reserved : reservedWords){
            if(reserved.equals(strWord)){
                return new Token("PALAVRA_RESERVADA", strWord);
            }

        }
        code.setIndex(startPosition); //se n for reservada volta pro começo
        return null; // Permite que outros avaliadores tentem reconhecer o token
    }
}