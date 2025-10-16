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

  //fazer que qnd passa lista de tokens pro sintatico nao passa nenhum token de comentario

  //VER SE COMO USEI MATCHT E MATCHL TA CERTO
  //VER SE FIZ CERTO EPSULON:Palavra vazia é sair de uma regra sem casar token = return true
  //VERIFICAR SE FIZ TD GLC CORRETA (ex: terminar com ;)
  //ONDE NAO TEM RETURN FALSE (E SIM RETURN TRUE)  //testar passar coisa incorreta para ver se aceita (EX: Aargumentoschamada, senaoopcional, listaouse...)
  //VE SE DA P OTIMIZA ALGUM CODIGO (ex: estoArgumentosChamada())

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

  //comando -> se|ouSe|senao|para|lacoEnquanto|declarar|atribuicao|criarFuncao|chamarFuncao
  private boolean comando(){
    if(seCompleto()||para()||lacoEnquanto()||declarar()||atribuicao()||criarFuncao()||chamarFuncao()){
      return true;
    }
    erro("comando"); 
    return false;
  }
  
  //seCompleto ->se listaOuSe senaoOpcional
  private boolean seCompleto(){
    if(se() && listaOuSe() && senaoOpcional()){
      return true;
    }
    return false; // n tem erro especifico pq a regra especifcia (se, ouse e senao) darao o erro
  }

  //listaOuSe > ouSe listaOuSe | e
  private boolean listaOuSe(){
    if(ouSe() && listaOuSe()){
      return true;
    }
    else{
      return true;//ε
    }
  }

  private boolean senaoOpcional(){
    if(senao()){
      return true;
    }
    return true;//ε
  }
  //se -> 'se''('condicao')''{'listaComandosInternos'}'
  private boolean se(){ 
     if (matchL("se") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("se");
    return false;
  }

  //ouSes -> 'ouSe''('condicao')''{'listaComandosInternos'}' ouSes | ε 
  private boolean ouSe(){
    if(matchL("ouSe") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("ouSe");
    return false;
  }

  //senao -> 'senao''{'listaComandosInternos'}'
  private boolean senao(){ 
    if(matchL("senao") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("senao");
    return false;
  }

  //para -> 'para''('cabecalhoPara')''{'listaComandosInternos'}'
  private boolean para(){
    if(matchL("para") && matchL("(") && cabecalhoPara() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("para");
    return false;
  }

  //lacoEnquanto -> 'lacoEnquanto''('condicao')''{'listaComandosInternos'}'
  private boolean lacoEnquanto(){ 
    if(matchL("enquanto") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("lacoEnquanto");
    return false;
  }

  //atribuicao -> declaraEAtribui|atribui
  private boolean atribuicao(){ 
    if(declaraEAtribui()||atribui()){
      return true;
    }
    erro("atribuicao");
    return false;
  }

  //criarFuncao -> 'criar' palavra_reservadaNomeFuncao'('argumentosFuncao')''{'listaComandosInternos'}'
  private boolean criarFuncao(){ 
    if(matchL("criar") && palavraReservadaNomeFuncao() && matchL("(") && argumentosFuncao()&& matchL(")") && matchL("{") && listaComandosInternos()  && matchL("}")){
      return true;
    }
    erro("criarFuncao");
    return false;
  }

  //chamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')' ';'
  private boolean chamarFuncao(){ 
    if ((palavraReservadaNomeFuncao()|| matchL("Entrada")||matchL("Imprima")) && matchL("(") && argumentosChamada() && matchL(")") &&  matchL(";")  ){
      return true;
    }
    erro("chamarFuncao");
    return false;
  }

  //argumentosChamada -> ε | valor restoArgumentosChamada
  private boolean argumentosChamada(){
    if (token != null && token.lexema.equals(")")) {
      return true; //n usei matchL pq ele pega o proximo token
    }//ε
    if(valor() && restoArgumentosChamada()){
      return true;
    }
    erro("argumentosChamada");
    return false;
  }

  //restoArgumentosChamada -> ε | ',' valor restoArgumentosChamada
  private boolean restoArgumentosChamada(){
    if(matchL(",") && valor() && restoArgumentosChamada()){
      return true;
    }
    return true; //ε
  }

  //argumentosFuncao -> ε|parametrosFuncao
  private boolean argumentosFuncao(){
      if(parametroFuncao()){
        return true;
      }
    return true;
  }

  //COLOCAR PARAMETROFUNCAO E RESTOPARAMETROFUNCAO!!!!!!!!!!!!!!!!!!!!!!!
  private boolean parametroFuncao(){
    return false;
  }

  //parametro -> tipoVariavel identificadores
  private boolean parametro(){
    if(tipoVariavel() && identificadores()){
      return true;
    }
    erro("parametro");
    return false;
  }

  //condicao -> identificadores|negacaoCondicao|condicaoComparacoesBasicas 
  private boolean condicao(){ 
    erro("condicao");
    return false;
  }

  //negacaoCondicao -> '!'condicao
  private boolean negacaoCondicao(){
    if(matchL("!") && condicao()){
      return true;
    }
    erro("negacaoCondicao");
    return false;
  }

  //operacao -> operacaoRelacional|operacaoLogica
  private boolean operacao(){
    if(operacaoRelacional() || operacaoLogica()){
      return true;
    }
    erro("operacao");
    return false;
  }

  //operacaoRelacional -> operadorDiferente|operadorIgualdade|operadorMenorIgual|operadorMaiorigual
  private boolean operacaoRelacional(){
    if(matchL("<>")&& matchL("<->")|| matchL("<=")|| matchL(">=")){
      return true;
    }
    erro("operadorRelacional");
    return false;
  }

  //operacaoLogica -> operador_logicoE|operador_logicoOu|operador_logicoNot
  private boolean operacaoLogica(){
    if(matchL("e")||matchL("ou")||matchL("!")){
      return true;
    }
    erro("operacaoLogica");
    return false;
  }

  //continuacao das glc de condicao

  //listaComandosInternos -> comandoInterno listaComandosInternos | ε
  private boolean listaComandosInternos(){ 
    if(comandoInterno() && listaComandosInternos()){
      return true;
    }
    return true; //ε
    //erro("listaComandosInternos");
    //return false;
  }
  
  //cabecalhoPara -> inicializacao ";" condicao ";" incremento
  private boolean cabecalhoPara(){ 
    if(inicializacao() && matchL(";") && condicao() && matchL(";") && incremento()){
      return true;
    }
    erro("listaComandosInternos");
    return false;
  }
  
  //declaraEAtribui -> declaraEAtribuiInteiro|declaraEAtribuiDecimal|declaraEAtribuiTexto|declaraEAtribuiVerdadeiroFalso
  private boolean declaraEAtribui(){ 
    if(declaraEAtribuiInteiro()||declaraEAtribuiDecimal()||declaraEAtribuiTexto()||declaraEAtribuiVerdadeiroFalso()){
      return true;
    }
    erro("declaraEAtribui");
    return false;
  }

  //atribui -> identificadores operadorAtribuicao valor ';'
  private boolean atribui(){ 
    if(identificadores() && matchL("->") && valor()){
        return true;
    }
    erro("atribui");
    return false;
  }

  //declaraEAtribuiInteiro -> 'inteiro' identificadores operadorAtribuicao numeroInteiro ';'
  private boolean declaraEAtribuiInteiro(){ 
    //matchL "inteiro" poderia ser uma funcao inteiro() que casa o token inteiro(palavra reservada na expressao regular tipos_dadoIntmas ja que escrevi ele com '' na glc faz sentido ficar assim
    if(matchL("inteiro") && identificadores() && matchL("->") && inteiro() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiInteiro");
    return false;
  }

  //declaraEAtribuiDecimal -> 'decimal' identificadores operadorAtribuicao numeroDecimal ';'
  private boolean declaraEAtribuiDecimal(){ 
    if(matchL("decimal") && identificadores() && matchL("->") && decimal() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiDecimal");
    return false;
  }

  //declaraEAtribuiTexto -> 'texto' identificadores operadorAtribuicao texto ';'
  private boolean declaraEAtribuiTexto(){ 
    if(matchL("texto") && identificadores() && matchL("->") && texto() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiTexto");
    return false;
  }

  //declaraEAtribuiVerdadeiroFalso -> 'verdadeiroFalso' identificadores operadorAtribuicao boolean ';'
  private boolean declaraEAtribuiVerdadeiroFalso(){
    if(matchL("verdadeiroFalso") && identificadores() && matchL("->") && isBoolean() && matchL(";")){
      return true;
    }
    erro("declaraEAtribuiVerdadeiroFalso");
    return false;
  }

  //expressoesMatematicas -> precedenciaInferior
  private boolean expressoesMatematicas(){
    if(precedenciaInferior()){
      return true;
    }
    erro("expressoesMatematicas");
    return false;
  }

  //declarar -> declararInteiro|declararDecimal|declararTexto|declararVerdadeiroFalso
  private boolean declarar(){
    if(declararInteiro()||declararDecimal()||declararTexto()||declararVerdadeiroFalso()){
      return true;
    }
    erro("declarar");
    return false;
  }

  //declararInteiro -> 'inteiro' identificadores ';'
  private boolean declararInteiro(){ 
    if(matchL("inteiro") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararInteiro");
    return false;
  }

  //declararDecimal -> 'decimal' identificadores ';'
  private boolean declararDecimal(){ 
    if(matchL("decimal") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararDecimal");
    return false;
  }

  //declararTexto -> 'texto' identificadores ';'
  private boolean declararTexto(){ 
    if(matchL("texto") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararTexto");
    return false;
  }

  //declararVerdadeiroFalso -> 'verdadeiroFalso' identificadores ';'
  private boolean declararVerdadeiroFalso(){ 
    if(matchL("verdadeiroFalso") && identificadores() && matchL(";")){
      return true;
    }
    erro("declararVerdadeiroFalso");
    return false;
  }
  
 //valor -> numero|texto|boolean|identificadores|expressoesMatematicas
  private boolean valor(){ 
    if(numero()||texto()||isBoolean()||identificadores()||expressoesMatematicas()){
      return true;
    }
    erro("valor");
    return false;
  }

  //numero -> numeroDecimal|numeroInteiro
  private boolean numero(){ 
    if(decimal()||inteiro()){
      return true;
    }
    erro("numero");
    return false;
  }

  //comandoInterno -> se|ouSe|senao|para|lacoEnquanto|atribuicao|chamarFuncao|retornar
  private boolean comandoInterno(){
    if(seCompleto()||para()||lacoEnquanto()||declarar()||atribuicao()||chamarFuncao()||retornar()){
      return true;
    }
    erro("comandoInterno"); 
    return false;
  }
  
  //retornar -> palavra_reservadaRetornoFuncao 
  private boolean retornar(){ 
    if(matchL("retorna") && (identificadores()||expressoesMatematicas()||numero()) && matchL(";")){
      return true;
    }
    erro("retornar");
    return false;
  }

  //inicializacao -> tipoVariavel identificadores "->" numero|identificadores|chamarFuncao|expressoesMatematicas
  private boolean inicializacao(){ 
    if(tipoVariavel() && identificadores() && matchL("->") && (numero()||identificadores()||chamarFuncao()||expressoesMatematicas())){
      return true;
    }
    erro("inicializacao");
    return false;
  }

  //precedenciaInferior -> precedenciaIntermediaria precedenciaInferior'
  private boolean precedenciaInferior(){
    if(precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    erro("precedenciaInferior");
    return false;
  }

  //precedenciaIntermediaria -> precedenciaAlta precedenciaIntermediaria'
  private boolean precedenciaIntermediaria(){
    if(precedenciaAlta() && precedenciaIntermediariaDerivada()){
      return true;
    }
    erro("precedenciaIntermediaria");
    return false;
  }

  //precedenciaInferior' -> '+'precedenciaIntermediaria precedenciaInferior' | '-'precedenciaIntermediaria precedenciaInferior' | ε
  private boolean precedenciaInferiorDerivada(){ 
    if(matchL("+") && precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    //ou
    if(matchL("-") && precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    return true; //ε
  }

  //precedenciaAlta -> precedenciaSuperior precedenciaAlta'
  private boolean precedenciaAlta(){
    if(precedenciaSuperior() && precedenciaAltaDerivada()){
      return true;
    }
    erro("precedenciaAlta");
    return false;
  }

  //precedenciaIntermediaria' -> '*' precedenciaAlta precedenciaIntermediaria' | /precedenciaAlta precedenciaIntermediaria' | ε
  private boolean precedenciaIntermediariaDerivada(){
    if(matchL("*") && precedenciaAlta() && precedenciaIntermediariaDerivada()){
      return true;
    }
    //ou
    if(matchL("/") && precedenciaAlta() && precedenciaIntermediariaDerivada()){
      return true;
    }
    return true; //ε
  }

  //precedenciaSuperior -> identificadores|numero|'('expressoesMatematicas')'
  private boolean precedenciaSuperior(){
    if(identificadores()||numero()|| (matchL("(") && expressoesMatematicas() && matchL(")"))){
      return true;
    }
    erro("precedenciaSuperior");
    return false;
  }

  //precedenciaAlta' -> '^'precedenciaSuperior precedenciaAlta' | ε
  private boolean precedenciaAltaDerivada(){
    if(matchL("^") && precedenciaAltaDerivada()){
      return true;
    }
    return true;
  }

  //incremento -> identificadores '->' expressoesMatematicas
  private boolean incremento(){ 
    if(identificadores() && matchL("->") && expressoesMatematicas()){
      return true;
    }
    erro("incremento");
    return false;
  }

  //tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto 
  private boolean tipoVariavel(){ 
    if(matchL("inteiro")||matchL("decimal")||matchL("texto")||matchL("verdadeiroFalso")){
      return true;
    }
    erro("tipoVariavel");
    return false;
  }


  //match t de tokens para expressao regular 

  //usa matcht quando o lexema nao é sempre igual, ai valida pelo tipo
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
  
  //boolean -> true|false
  private boolean isBoolean(){
    if(matchL("true")||matchL("false")){
      return true;
    }
    erro("boolean");
    return false;
  }

  private boolean palavraReservadaNomeFuncao(){
    if(matchT("FUNCTION_NAME")){
      return true;
    }
    erro("palavraReservadaNomeFuncao");
    return false;
  }

}