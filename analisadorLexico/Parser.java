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

  //VER SE COMO USEI MATCHT E MATCHL TA CERTO
  //VER SE FIZ CERTO EPSULON:Palavra vazia é sair de uma regra sem casar token = return true
  //colocar glc equivalente antes da funcao pra facilitar entendimento
  
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
    if(se()||ouSe()||senao()||para()||lacoEnquanto()||declarar()||atribuicao()||criarFuncao()||chamarFuncao()){
      return true;
    }
    erro("comando"); 
    return false;
  }
  //duvida se o mlhr jeito eh fazer assim mesmo: seousesenao td junto

  private boolean se(){ 
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
     if (matchL("se") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("se");
    return false;
  }

  private boolean ouSe(){
    if(matchL("ouSe") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("ouSe");
    return false;
  }

   private boolean senao(){ 
    if(matchL("senao") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("senao");
    return false;
  }

  private boolean para(){
    if(matchL("para") && matchL("(") && cabecalhoPara() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("para");
    return false;
  }

  private boolean lacoEnquanto(){ 
    if(matchL("enquanto") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("lacoEnquanto");
    return false;
  }

  private boolean atribuicao(){ 
    if(declaraEAtribui()||atribui()){
      return true;
    }
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
    if(comandoInterno() && listaComandosInternos()){
      return true;
    }
    return true; //ε
    //erro("listaComandosInternos");
    //return false;
  }
  
  private boolean cabecalhoPara(){ 
    if(inicializacao() && matchL(";") && condicao() && matchL(";") && incremento()){
      return true;
    }
    erro("listaComandosInternos");
    return false;
  }
  
  private boolean declaraEAtribui(){ 
    if(declaraEAtribuiInteiro()||declaraEAtribuiDecimal()||declaraEAtribuiTexto()||declaraEAtribuiVerdadeiroFalso())
    erro("declaraEAtribui");
    return false;
  }

  private boolean atribui(){ 
    if(identificadores() && matchL("->") && valor()){
        return true;
    }
    erro("atribui");
    return false;
  }

  private boolean declaraEAtribuiInteiro(){ 
    //matchL "inteiro" poderia ser uma funcao inteiro() que casa o token inteiro(palavra reservada na expressao regular tipos_dadoIntmas ja que escrevi ele com '' na glc faz sentido ficar assim
    if(matchL("inteiro") && identificadores() && matchL("->") && inteiro() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiInteiro");
    return false;
  }

  private boolean declaraEAtribuiDecimal(){ 
    if(matchL("decimal") && identificadores() && matchL("->") && decimal() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiDecimal");
    return false;
  }

  private boolean declaraEAtribuiTexto(){ 
    if(matchL("texto") && identificadores() && matchL("->") && texto() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiTexto");
    return false;
  }

  private boolean declaraEAtribuiVerdadeiroFalso(){
    if(matchL("verdadeiroFalso") && identificadores() && matchL("->") && isBoolean() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiVerdadeiroFalso");
    return false;
  }

  private boolean expressoesMatematicas(){
    if(precedenciaInferior()){
      return true;
    }
    erro("expressoesMatematicas");
    return false;
  }

  private boolean declarar(){
    if(declararInteiro()||declararDecimal()||declararTexto()||declararVerdadeiroFalso()){
      return true;
    }
    erro("declarar");
    return false;
  }

  private boolean declararInteiro(){ 
    if(matchL("inteiro") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararInteiro");
    return false;
  }

  private boolean declararDecimal(){ 
    if(matchL("decimal") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararDecimal");
    return false;
  }

  private boolean declararTexto(){ 
    if(matchL("texto") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararTexto");
    return false;
  }

  private boolean declararVerdadeiroFalso(){ 
    if(matchL("verdadeiroFalso") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararVerdadeiroFalso");
    return false;
  }

  private boolean valor(){ 
    if(numero()||texto()||isBoolean()||identificadores()||expressoesMatematicas()){
      return true;
    }
    erro("valor");
    return false;
  }
  private boolean numero(){ 
    if(decimal()||inteiro()){
      return true;
    }
    erro("numero");
    return false;
  }

  private boolean comandoInterno(){
    if(se()||ouSe()||senao()||para()||lacoEnquanto()||declarar()||atribuicao()||chamarFuncao()||retornar()){
      return true;
    }
    erro("comandoInterno"); 
    return false;
  }
  private boolean retornar(){ 
    if(matchL("retorna") && (identificadores()||expressoesMatematicas()||numero()) && matchL(";")){
      return true;
    }
    erro("retornar");
    return false;
  }

  private boolean inicializacao(){ 
    if(tipoVariavel() && identificadores() && matchL("->") && (numero()||identificadores()||chamarFuncao()||expressoesMatematicas())){
      return true;
    }
    erro("inicializacao");
    return false;
  }

  private boolean precedenciaInferior(){
    if(precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    erro("precedenciaInferior");
    return false;
  }

  private boolean precedenciaIntermediaria(){ 
    erro("precedenciaIntermediaria");
    return false;
  }

  private boolean precedenciaInferiorDerivada(){ 
    if(matchL("+") && precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    if(matchL("-") && precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    return true; //ε
  }

  //incremento -> identificadores operacaoIncremento | identificadores operacaoIncremento numero|identificadores | identficiadores operacaoIncremento expressoesMatematicas
  private boolean incremento(){ 
    erro("incremento");
    return false;
  }

  private boolean tipoVariavel(){ 
    if(matchL("inteiro")||matchL("decimal")||matchL("texto")||matchL("verdadeiroFalso")){
      return true;
    }
    erro("tipoVariavel");
    return false;
  }

  private boolean operacaoIncremento(){ 
    if(matchL("++")||matchL("--")||matchL("+=")||matchL("-=")|| matchL("*=")||matchL("/=")){
      return true;
    }
    erro("operacaoIncremento");
    return false;
  }



  //match t de tokens para expressao regular
  private boolean decimal(){ 
    if(matchT("DECIMAL")){
      return true;
    }
    erro("decimal");
    return false;
  }

  private boolean inteiro(){ 
    if(matchT("INTEGER")){
      return true;
    }
    erro("inteiro");
    return false;
  }

  private boolean identificadores(){ 
    if(matchT("IDENTIFIER")){
      return true;
    }
    erro("identificadores");
    return false;
  }
  private boolean texto(){ 
    if(matchT("TEXT")){
      return true;
    }
    erro("texto");
    return false;
  }
  private boolean isBoolean(){
    if(matchT("PALAVRA_RESERVADA") && (token.lexema.equals("true")||token.lexema.equals("false"))){
      return true;
    }
    erro("boolean");
    return false;
  }
}