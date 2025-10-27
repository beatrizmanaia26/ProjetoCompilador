package analisadorLexico;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
  List<Token> tokens;
  Token token;
  Map<String, Set<String>> firsts = new HashMap<>();

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
    inicializarFirsts();
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


  //site que calcula first e follow: REFAZER FIST CONSIDERANDO EPSULON !!!!!!!!!!!!!!!


  // listaComandos -> comando listaComandos | ε
  /*
   * simbulos           first
   * listaComandos      comando (regra,ent first é firsts dessa regra), ε
   */
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

  //IMPLEMENTAR FIRST SÓ AQUI: E VE SE JA RESOLVE PROBLEMA!!!!!!!!!!!!!!!!!!!

  //comando -> seCompleto|para|lacoEnquanto|declarar|atribuicao|criarFuncao|chamarFuncao
  /*
   * simbulos     first
   * comando     so regra, ent first é firsts dessas regras: (seCompleto,para,lacoEnquanto,declarar,atribuicao,criarFuncao,chamarFuncao)
   */
  private boolean comando(){
    if(first("declaracao") && declaracao()||first("seCompleto") && seCompleto()||
    first("para") && para()||first("lacoEnquanto") && lacoEnquanto()||
    first("atribui") && atribui()||first("criarFuncao") && criarFuncao()||
    first("chamarFuncao") && chamarFuncao()){
      return true;
    }
    erro("comando"); 
    return false;
  }
  
  
  //PROBLEMA PQ DECLARAR E DECLARAREATRIBUIR (opcao dentro e atribuicao) tem os mesmos firsts ent melhor solucao é juntar os 2 em um codigo só
  // Juntar declarar e declaraEAtribui em uma única regra

  // declaração -> tipoVariavel identificadores (';' | '->' valor ';')
  /*
   * simbulos     first
   * declaracao   so regra, ent first é firsts dessa regra: tipoVariavel
   *               inteiro,decimal, texto, verdadeiroFalso
   */
  private boolean declaracao(){
      if(tipoVariavel() && identificadores()) {
          // Pode ser: tipo var; OU tipo var -> valor;
          if(matchL(";")) {
              return true; // declaracao simples
          } else if(matchL("->") && valor() && matchL(";")) {
              return true; // declaracao com atribuicao
          }
      }
      return false;
  }

  //seCompleto ->se listaOuSe senaoOpcional
  /*
   * simbulos     first
   * seCompleto    só regra ent first é firsts dessa regra: (se)
   *               "se"
   */
  private boolean seCompleto(){
    if(se() && listaOuSe() && senaoOpcional()){
      return true;
    }
    return false; // n tem erro especifico pq a regra especifcia (se, ouse e senao) darao o erro
  }

  //listaOuSe > ouSe listaOuSe | ε
   /*
   * simbulos     first
   * listaOuSe    ε, first é firsts dessa regra: ouSe
   */
  private boolean listaOuSe(){
    if(ouSe() && listaOuSe()){
      return true;
    }
    else{
      return true;//ε
    }
  }
 
  //senaoOpcional -> senão |  ε
   /*
   * simbulos            first
   * senaoOpcional       ε, first é firsts dessa regra: senao
   */
  private boolean senaoOpcional(){
    if(senao()){
      return true;
    }
    return true;//ε
  }

  //se -> 'se''('condicao')''{'listaComandosInternos'}'
  /*
   * simbulos         first
   * se                se
   */
  private boolean se(){ 
     if (matchL("se") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("se");
    return false;
  }
 
  //ouSe -> 'ouSe''('condicao')''{'listaComandosInternos'}'
  /*
   * simbulos         first
   * ouSe             ouSe
   */
  private boolean ouSe(){
    if(matchL("ouSe") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("ouSe");
    return false;
  }

  //senao -> 'senao''{'listaComandosInternos'}'
   /*
   * simbulos       first
   * senao          senao
   */
  private boolean senao(){ 
    if(matchL("senao") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("senao");
    return false;
  }

  //para -> 'para''('cabecalhoPara')''{'listaComandosInternos'}'
   /*
   * simbulos     first
   * para          para
   */
  private boolean para(){
    if(matchL("para") && matchL("(") && cabecalhoPara() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("para");
    return false;
  }

  //lacoEnquanto -> 'lacoEnquanto''('condicao')''{'listaComandosInternos'}'
  /*
   * simbulos         first
   * lacoenquanto     lacoenquanto
   */
  private boolean lacoEnquanto(){ 
    if(matchL("lacoEnquanto") && matchL("(") && condicao() && matchL(")") && matchL("{") && listaComandosInternos() && matchL("}")){
        return true;
     }
    erro("lacoEnquanto");
    return false;
  }


  //criarFuncao -> 'criar' palavra_reservadaNomeFuncao'('argumentosFuncao')''{'listaComandosInternos'}'
  /*
   * simbulos            first
   * criarFuncao         criar
   */
  private boolean criarFuncao(){ 
    if(matchL("criar") && palavraReservadaNomeFuncao() && matchL("(") && argumentosFuncao()&& matchL(")") && matchL("{") && listaComandosInternos()  && matchL("}")){
      return true;
    }
    erro("criarFuncao");
    return false;
  }

  //chamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')' ';'
  /*
   * simbulos            first
   * chamarFuncao          "entrada","imprima", first é firsts dessa regra palavraReservadaNomeFuncao
   */
  private boolean chamarFuncao(){ 
    if ((palavraReservadaNomeFuncao()|| matchL("Entrada")||matchL("Imprima")) && matchL("(") && argumentosChamada() && matchL(")") &&  matchL(";")  ){
      return true;
    }
    erro("chamarFuncao");
    return false;
  }

  //argumentosChamada -> ε | valor restoArgumentosChamada
   /*
   * simbulos            first
   * argumentosChamada   ε, first é first dessa regra: valor
   */
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
   /*
   * simbulos                   first
   * restoArgumentosChamada    ε,','
   */
  private boolean restoArgumentosChamada(){
    if(matchL(",") && valor() && restoArgumentosChamada()){
      return true;
    }
    return true; //ε
  }

  //argumentosFuncao -> ε|parametrosFuncao
   /*
   * simbulos               first
   * argumentosFuncao      ε, first é first dessa regra: parametrosFuncao
   */
  private boolean argumentosFuncao(){
      if(parametroFuncao()){
        return true;
      }
    return true;
  }

  //parametroFuncao -> parâmetro emComumParametro
  /*
   * simbulos            first
   * parametroFuncao    first é first dessa regra: parametro
   */
  private boolean parametroFuncao(){
    if(parametro() && emComumParametro()){
      return true;
    }
    erro("parametroFuncao");
    return false;
  }

  //emComumParametro -> ε | ‘,’ parametroFuncao emComumParametro
   /*
   * simbulos            first
   * emComumParametro    ε , ‘,’ 
   */
  private boolean emComumParametro(){
    if(matchL(",") && parametroFuncao() && emComumParametro()){
      return true;
    }
    return true; //ε 
  }

  //parametro -> tipoVariavel identificadores
  /*
   * simbulos            first
   * parametro         first é first dessa regra: tipoVariavel
   */
  private boolean parametro(){
    if(tipoVariavel() && identificadores()){
      return true;
    }
    erro("parametro");
    return false;
  }

  //condicao -> identificadores condicao’ | negacaoCondicao condicao’ | expressoesMatematicas condicao’| condicaoComparacoesBasicas condicao’
   /*
   * simbulos     first
   * condicao  first é first dessas regras: identfificadores, negacaocondicao, expressoesMatematicas, condicaoComparacoesBasicas
   */
  private boolean condicao(){ 
    if(identificadores() && condicaoDerivada()||negacaoCondicao() && condicaoDerivada()||
    expressoesMatematicas() && condicaoDerivada()||condicaoComparacoesBasicas() && condicaoDerivada()){
      return true;
    }
    erro("condicao");
    return false;
  }

  //condicao’ -> operacao condição condicao’| ε
  /*
   * simbulos     first
   * condicao’   first é first dessa regra: operacao
   */
  private boolean condicaoDerivada(){
    if(operacao() && condicao() && condicaoDerivada()){
      return true;
    }
    return true; //ε
  }

  //condicaoComparacoesBasicas ->  identificadores|numero operacao valoresOperacao
  /*
   * simbulos                       first
   * condicaoComparacoesBasicas    first é first dessas regras: identificadores, numero
   */
  private boolean condicaoComparacoesBasicas(){
    if(identificadores() ||(numero() && operacao() && valoresOperacao())){
      return true;
    }
    erro("condicaoComparacoesBasicas");
    return false;
  }

  //valoresOperacao -> identificadores|numero|boolean
   /*
   * simbulos          first
   * valoresOperacao   first é first dessas regras: identificadores, numero, isBoolean
   */
  private boolean valoresOperacao(){
    if(identificadores()|| numero()|| isBoolean()){
      return true;
    }
    erro("valoresOperacao");
    return false;
  }

  //negacaoCondicao -> '!'condicao
   /*
   * simbulos          first
   * negacaoCondicao    !
   */
  private boolean negacaoCondicao(){
    if(matchL("!") && condicao()){
      return true;
    }
    erro("negacaoCondicao");
    return false;
  }

  //operacao -> operacaoRelacional|operacaoLogica
   /*
   * simbulos              first
   * operacao            first é first dessas regras: operacaoRelacional, operacaoLogica
   */
  private boolean operacao(){
    if(operacaoRelacional() || operacaoLogica()){
      return true;
    }
    erro("operacao");
    return false;
  }

  //operacaoRelacional -> operadorDiferente|operadorIgualdade|operadorMenorIgual|operadorMaiorigual
    /*
   * simbulos              first
   * operacaoRelacional    <>,<->,<=,>=
   */
  private boolean operacaoRelacional(){
    if(matchL("<>") || matchL("<->")|| matchL("<=")|| matchL(">=")){
      return true;
    }
    erro("operadorRelacional");
    return false;
  }

  //operacaoLogica -> operador_logicoE|operador_logicoOu|operador_logicoNot
    /*
   * simbulos              first
   * operacaoLogica       e,ou,!
   */
  private boolean operacaoLogica(){
    if(matchL("e")||matchL("ou")||matchL("!")){
      return true;
    }
    erro("operacaoLogica");
    return false;
  }

  //listaComandosInternos -> comandoInterno listaComandosInternos | ε
  /*
   * simbulos                 first
   * listaComandosInternos     ε, first é first dessa regra: comandointerno
   */
  private boolean listaComandosInternos(){ 
    if(comandoInterno() && listaComandosInternos()){
      return true;
    }
    return true; //ε
  }
  
  //cabecalhoPara -> inicializacao ";" condicao ";" incremento
   /*
   * simbulos              first
   * cabecalhoPara        first é first dessa regra: inicializacao
   */
  private boolean cabecalhoPara(){ 
    if(inicializacao() && matchL(";") && condicao() && matchL(";") && incremento()){
      return true;
    }
    erro("listaComandosInternos");
    return false;
  }

  //atribui -> identificadores operadorAtribuicao valor ';'
   /*
   * simbulos          first
   * atribui           first é first dessa regra: identificadores
   *                   
   */
  private boolean atribui(){ 
    if(identificadores() && matchL("->") && valor()){
        return true;
    }
    erro("atribui");
    return false;
  }
  //expressoesMatematicas -> precedenciaInferior
    /*
   * simbulos                first
   * expressoesMatematicas   first é first dessa regra:precedenciaInferior
   */
  private boolean expressoesMatematicas(){
    if(precedenciaInferior()){
      return true;
    }
    erro("expressoesMatematicas");
    return false;
  }

 //valor -> numero|texto|boolean|identificadores|expressoesMatematicas
    /*
   * simbulos       first
   * valor          first é first dessas regras: numero,texto,isBoolean, identificadores,expressoesMatematicas
   */
  private boolean valor(){ 
    if(numero()||texto()||isBoolean()||identificadores()||expressoesMatematicas()){
      return true;
    }
    erro("valor");
    return false;
  }

  //numero -> numeroDecimal|numeroInteiro
   /*
   * simbulos         first
   * numero           first é first dessas regras: decimal, inteiro
   */
  private boolean numero(){ 
    if(decimal()||inteiro()){
      return true;
    }
    erro("numero");
    return false;
  }

  //comandoInterno -> se|ouSe|senao|para|lacoEnquanto|atribuicao|chamarFuncao|retornar
  /*
   * simbulos         first
   * comandoInterno    first é first dessas regras:seCompleto,para,lacoEnquanto,declarar,atribuicao,chamarFuncao,retornar
   */
  private boolean comandoInterno(){
    if(seCompleto()||para()||lacoEnquanto()||declaracao()||atribui()||chamarFuncao()||retornar()){
      return true;
    }
    erro("comandoInterno"); 
    return false;
  }
  
  //retornar -> 'retorna' tiposRetorno';'
    /*
   * simbulos     first
   * retornar    retorna
   */
  private boolean retornar(){ 
    if(matchL("retorna") && conteudos() && matchL(";")){
      return true;
    }
    erro("retornar");
    return false;
  }

  //conteudos -> identificadores|expressoesMatematicas|numero
  /*
   * simbulos          first
   * conteudos      first é first dessas regras: identificadores, expressoesMatematicas,numero
   */
  private boolean conteudos(){ 
    if(identificadores() || expressoesMatematicas()|| numero()){
      return true;
    }
    erro("conteudos");
    return false;
    }
  //inicializacao -> tipoVariavel identificadores "->" conteudos
  /*
   * simbulos         first
   * inicializacao    first é first dessa regra: tipoVariavel
   */
  private boolean inicializacao(){ 
    if(tipoVariavel() && identificadores() && matchL("->") && conteudos()){
      return true;
    }
    erro("inicializacao");
    return false;
  }

  //precedenciaInferior -> precedenciaIntermediaria precedenciaInferior'
  /*
   * simbulos               first
   * precedenciaInferior    first é first dessa regra:precedenciaIntermediaria
   */
  private boolean precedenciaInferior(){
    if(precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      return true;
    }
    erro("precedenciaInferior");
    return false;
  }

  //precedenciaIntermediaria -> precedenciaAlta precedenciaIntermediaria'
    /*
   * simbulos                     first
   * precedenciaIntermediaria     first é first dessa regra:precedenciaAlta
   */
  private boolean precedenciaIntermediaria(){
    if(precedenciaAlta() && precedenciaIntermediariaDerivada()){
      return true;
    }
    erro("precedenciaIntermediaria");
    return false;
  }

  //precedenciaInferior' -> '+'precedenciaIntermediaria precedenciaInferior' | '-'precedenciaIntermediaria precedenciaInferior' | ε
  /*
   * simbulos               first
   * precedenciaInferior'    "+", "-", ε
   */
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
  /*
  * simbulos            first
  * precedenciaAlta     first é first dessa regra: precedenciaSuperior
  */
  private boolean precedenciaAlta(){
    if(precedenciaSuperior() && precedenciaAltaDerivada()){
      return true;
    }
    erro("precedenciaAlta");
    return false;
  }

  //precedenciaIntermediaria' -> '*' precedenciaAlta precedenciaIntermediaria' | /precedenciaAlta precedenciaIntermediaria' | ε
    /*
   * simbulos                    first
   * precedenciaIntermediaria'    "*", "/", ε
   */
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
    /*
   * simbulos                     first
    * precedenciaSuperior       "(", first é first dessa regra: identificadores,numero
   */
  private boolean precedenciaSuperior(){
    if(identificadores()||numero()|| (matchL("(") && expressoesMatematicas() && matchL(")"))){
      return true;
    }
    erro("precedenciaSuperior");
    return false;
  }

  //precedenciaAlta' -> '^'precedenciaSuperior precedenciaAlta' | ε
     /*
   * simbulos             first
   * precedenciaAlta'    "^",ε
   */
  private boolean precedenciaAltaDerivada(){
    if(matchL("^") && precedenciaAltaDerivada()){
      return true;
    }
    return true;
  }

  //incremento -> identificadores '->' expressoesMatematicas
  /*
   * simbulos        first
    * incremento     first é first dessa regra: identificadores
   */
  private boolean incremento(){ 
    if(identificadores() && matchL("->") && expressoesMatematicas()){
      return true;
    }
    erro("incremento");
    return false;
  }

  //tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto 
  /*
   * simbulos        first
  * tipoVariavel    inteiro,decimal, texto, verdadeiroFalso
   */
  private boolean tipoVariavel(){ 
    if(matchL("inteiro")||matchL("decimal")||matchL("texto")||matchL("verdadeiroFalso")){
      return true;
    }
    erro("tipoVariavel");
    return false;
  }

  //match t de tokens para expressao regular 

  //usa matcht quando o lexema nao é sempre igual, ai valida pelo tipo
  /*
   * simbulos     first
   * decimal       DECIMAL
   */
  private boolean decimal(){ 
    if(matchT("DECIMAL")){
      return true;
    }
    erro("decimal");
    return false;
  }

   /*
   * simbulos     first
   * inteiro       INTEIRO
   */
  private boolean inteiro(){ 
    if(matchT("INTEGER")){
      return true;
    }
    erro("inteiro");
    return false;
  }

    /*
   * simbulos            first
   * identificadores     IDENTIFIER
   */
  private boolean identificadores(){ 
    if(matchT("IDENTIFIER")){
      return true;
    }
    erro("identificadores");
    return false;
  }
  
  /*
   * simbulos     first
   * texto        TEXT
   */
  private boolean texto(){ 
    if(matchT("TEXT")){
      return true;
    }
    erro("texto");
    return false;
  }
  
  //boolean -> true|false
    /*
   * simbulos     first
   * boolean      true, false
   */
  private boolean isBoolean(){
    if(matchL("true")||matchL("false")){
      return true;
    }
    erro("boolean");
    return false;
  }

  /*
   * simbulos                      first
   * palavraReservadaNomeFuncao    FUNCTION_NAME
   */
  private boolean palavraReservadaNomeFuncao(){
    if(matchT("FUNCTION_NAME")){
      return true;
    }
    erro("palavraReservadaNomeFuncao");
    return false;
  }

  private void inicializarFirsts() {
    // FIRST dos comandos
    //key = nome funcao, e1 = first
    //firsts para comando:
    firsts.put("declaracao", Set.of("inteiro","decimal","texto","verdadeiroFalso"));
    firsts.put("seCompleto", Set.of("se"));
    firsts.put("para", Set.of("para"));
    firsts.put("lacoEnquanto", Set.of("lacoEnquanto"));
    firsts.put("atribui", Set.of("IDENTIFIERS"));
    firsts.put("criarFuncao", Set.of("criar"));
    firsts.put("chamarFuncao", Set.of("FUNCTION_NAME","Entrada","Imprima"));

    // firsts basicos
    firsts.put("ouSe", Set.of("ouSe"));
    firsts.put("senao", Set.of("senao"));
  }

  private boolean first(String regra) {
    if (token == null) return false; //evita null pointer exception
    
    Set<String> firstSet = firsts.get(regra);
    if (firstSet == null) {
        // Regra sem FIRST definido - não tenta
        System.out.println("FIRST não definido para: " + regra);
        return false;
    }
    // Verifica se token atual está no FIRST
    boolean matches = firstSet.contains(token.lexema) || firstSet.contains(token.tipo);
    
    return matches;
  }

}