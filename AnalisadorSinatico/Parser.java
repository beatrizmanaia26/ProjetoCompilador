import java.util.List;

public class Parser {

  List<Token> tokens;
  Token token;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public void main() {
    token = getNextToken();
    if (ifelse() && matchT("EOF")){
      System.out.println("Sintaticamente correto");
    }else{
      erro("main");
    }
  }
  /*
    public void main(){
    token = getNextToken();
    if(ifelse())
      if (token.lexema.equals("$"))
        System.out.println("sintaticamente correto");
  }
  */

  public Token getNextToken() {
    if (tokens.size() > 0) 
      return tokens.remove(0);
    return null;
  }

  private void erro(String regra) {
    System.out.println("-------------- Regra: " + regra);
    System.out.println("token inválido: " + token);
    System.out.println("------------------------------");
  }

  private boolean ifelse() {
    if (matchL("if") && //casar lexema
        condicao() && 
        matchL("then") && 
        expressao() && 
        matchL("else") &&  
        expressao())
      return true;

    erro("ifelse");
    return false;
    //condicao inteira ja retorna true ou false
  }

  private boolean condicao() {
    if (id() && operador() && num())
      return true;

    erro("condicao");
    return false;
  }

  private boolean expressao() {
    if (id() && operadorAtribuicao() && num())
      return true;

    erro("expressao");
    return false;
  }

  private boolean operador() {
    if (matchL(">") ||  matchL("<") || matchL("==")) 
      return true;

    erro("operador");
    return false;
  }

  private boolean operadorAtribuicao() {
    if (matchL("=")) 
      return true;

    erro("operadorAtribuicao");
    return false;
  }

  private boolean id() {
    if (matchT("id")) //casar token
      return true;

    erro("id");
    return false;
  }

  private boolean num() {
    if (matchT("num"))
      return true;

    erro("num");
    return false;
  }

  private boolean matchT(String tipo){
    if (token.tipo.equals(tipo)){
      token = getNextToken();
      return true;
    }
    return false;
  }

  private boolean matchL(String lexema){
    if (token.lexema.equals(lexema)){
      token = getNextToken();
      return true;
    }
    return false;
  }

}
