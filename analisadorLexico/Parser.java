package analisadorLexico;
import java.util.List;

public class Parser {
  List<Token> tokens;
  Token token;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

   public void main() {
    token = getNextToken();
    if (listaComandos() && matchT("EOF")){
      System.out.println("Sintaticamente correto");
    }else{
      erro("main");
    }
    /* extra: token = getNextToken();
    if (lacoEnquanto() && matchT("EOF")){
      System.out.println("Sintaticamente correto");
    }else{
      erro("main");
    }*/ 
  }

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

  //REGRAS DA GRAMÁTICA

  // listaComandos -> comando listaComandos | ε
  private boolean listaComandos(){
    if(token == null || token.tipo.equals("EOF")){
      return true; //ε
    }
    if(comando() && listaComandos()){
      return true;
    }
    erro("listaComandos");
    return false;
  }
  private boolean comando(){
    if(seOuSeSenao()||para()||lacoEnquanto()||atribuicao()||criarFuncao()||chamarFuncao()){
      return true;
    }
    erro("comando"); 
    return false;
  }
  //duvida se o mlhr jeito eh fazer assim mesmo: seousesenao td junto

  private boolean seOuSeSenao(){ 
    /*
     * pode: 
     * se(){} ouse(){} senao(){} 
     * ou
     * se(){} senao(){}
     * ou
     * se(){} ouSe(){}
     * ou se(){} senao(){}
     * ............
     */
    erro("SeOuSeSenao");
    return false;
  }

  private boolean para(){ 
    erro("para");
    return false;
  }
  

  private boolean lacoEnquanto(){ 
    erro("lacoEnquanto");
    return false;
  }

  private boolean atribuicao(){ 
    erro("atribuicao");
    return false;
  }

  private boolean criarFuncao(){ 
    erro("criarFuncao");
    return false;
  }

  private boolean chamarFuncao(){ 
    erro("chamarFuncao");
    return false;
  }

  //arrumar recursividade na glc
  private boolean condicao(){ 
    erro("condicao");
    return false;
  }

  //continuacao das glc de condicao

  private boolean listaComandosInternos(){ 
    erro("listaComandosInternos");
    return false;
  }
  
  private boolean cabecalhoPara(){ 
    erro("listaComandosInternos");
    return false;
  }
  


}
