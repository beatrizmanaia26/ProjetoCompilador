package analisadorLexico;
import java.util.HashMap;
import java.util.HashSet;
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

  //verificar como tava declararetribuir, decarar e atribuicao (commit antes) deu trocar declaracao p ver se da p declarar qqr coisa!!!!!!!!!!
  //verificar se coisas que terminam com ; ta certo ex inicializacao (conv deep)

  //fazer que qnd passa lista de tokens pro sintatico nao passa nenhum token de comentario

  //VER SE FIZ CERTO EPSULON:Palavra vazia é sair de uma regra sem casar token = return true (follow)
  //VERIFICAR SE FIZ TD GLC CORRETA (ex: terminar com ;) //
  //ONDE NAO TEM RETURN FALSE (E SIM RETURN TRUE)  //testar passar coisa incorreta para ver se aceita (EX: Aargumentoschamada, senaoopcional, listaouse...)
  //VE SE DA P OTIMIZA ALGUM CODIGO (ex: estoArgumentosChamada())


  //site que calcula first e follow: REFAZER FIST CONSIDERANDO EPSULON !!!!!!!!!!!!!!!


  // listaComandos -> comando listaComandos | ε
  /*
   * simbulos           first                                                 follow
   * listaComandos      comando (regra,ent first é firsts dessa regra), ε        $
   */
  private boolean listaComandos(){
    System.out.println("entrou no listacomandos");
    if(first("comando") && comando() && listaComandos()){
      System.out.println("deu match listacomandos");
      return true;
    }
    return true; //ε
  }

  //comando -> seCompleto|para|lacoEnquanto|declaracao|atribui|criarFuncao|chamarFuncao
  /*
   * simbulos     first
   * comando     so regra, ent first é firsts dessas regras: (seCompleto,para,lacoEnquanto,declarar,atribuicao,criarFuncao,chamarFuncao)
   */
  private boolean comando(){
    System.out.println("entrou no comando");
    if((first("declaracao") && declaracao())||(first("seCompleto") && seCompleto())||
    (first("para") && para())||(first("lacoEnquanto") && lacoEnquanto())||
    (first("atribui") && atribui())||(first("criarFuncao") && criarFuncao())||
    (first("chamarFuncao") && chamarFuncao())){
      System.out.println("deu match no comando");
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
    System.out.println("entrou no declaracao");
    if(first("declaracao") && tipoVariavel() && identificadores()) { //uso first declaracao pq é igual ao first de tipovariavel, ai n tenho que criar outra regra pros mesmos firsts
      // Pode ser: tipo var; OU tipo var -> valor;
      if(matchL(";")) {
         return true; // declaracao simples
      } 
      else if(matchL("->") && valor() && matchL(";")) {
        return true; // declaracao com atribuicao
      }
    }
    erro("declaracao");
    return false;
  }

  //seCompleto ->se listaOuSe senaoOpcional
  /*
   * simbulos            first                                              
   *  seCompleto          "se"
   */
  private boolean seCompleto(){
    System.out.println("entrou no seCompleto");
    if(first("se")&& se() && listaOuSe() && senaoOpcional()){
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
    if(first("ouSe") && ouSe() && listaOuSe()){
      return true;
    }
    return true;//ε
  }
 
  //senaoOpcional -> senão |  ε
   /*
   * simbulos            first
   * senaoOpcional       ε, first é firsts dessa regra: senao 
   *                     ε, ""senao"
   */
  private boolean senaoOpcional(){
    System.out.println("entrou no senaoOpcional");
    if(first("senao") && senao()){
      System.out.println("deu match no senaoOpcional");
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
    System.out.println("entrou no se");
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
    System.out.println("entrou no senao");
    if(matchL("senao") && matchL("{") && listaComandosInternos() && matchL("}")){
        System.out.println("deu match no senao");
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
    System.out.println("entrou no para");
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
    System.out.println("entrou em lacoEnquanto");
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
    System.out.println("entrou criarfuncao");
    if(matchL("criar") && palavraReservadaNomeFuncao() && matchL("(") && argumentosFuncao()&& matchL(")") && matchL("{") && listaComandosInternos()  && matchL("}")){
      return true;
    }
    erro("criarFuncao");
    return false;
  }

  //chamarFuncao -> inicioChamarFuncao '('argumentosChamada')' ';'
  /*
   * simbulos            first
   * chamarFuncao          "entrada","imprima", first é firsts dessa regra palavraReservadaNomeFuncao
   */
  private boolean chamarFuncao() {
    System.out.println("entrou chamarFuncao");
  if (first("chamarFuncao") && inicioChamarFuncao() && matchL("(") && argumentosChamada() && matchL(")") && matchL(";")
  ) {
    System.out.println("deu match em chamar funcao");
    return true;
  }
  erro("chamarFuncao");
  return false;
}

  //inicioChamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima
   /*
   * simbulos            first
   * inicioChamarFuncao          "entrada","imprima", first é firsts dessa regra palavraReservadaNomeFuncao
   */
  private boolean inicioChamarFuncao(){
    System.out.println("entrou inicioChamarFuncao");
    if((first("palavraReservadaNomeFuncao") && palavraReservadaNomeFuncao())|| matchL("Entrada")||matchL("Imprima")){
      System.out.println("deu match em inicioChamarFuncao");
    return true;
   }
    erro("inicioChamarFuncao");
    return false;
  }
  //argumentosChamada -> ε | valor restoArgumentosChamada
   /*
   * simbulos            first
   * argumentosChamada   ε, first é first dessa regra: valor
   */
  private boolean argumentosChamada(){
     System.out.println("entrei em argumentosChamada");
    if(first("valor") && valor() && restoArgumentosChamada()){
      return true;
    }
    return true;//ε
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
      if(first("declaracao") && parametroFuncao()){
        return true;
      }
    return true;//ε
  }

  //parametroFuncao -> parâmetro emComumParametro
  /*
   * simbulos            first
   * parametroFuncao    first é first dessa regra: parametro
   */
  private boolean parametroFuncao(){
    if(first("declaracao") && parametro() && emComumParametro()){
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
    if(first("declaracao") && tipoVariavel() && identificadores()){
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

  //implementação do lookahead para pegar o prox token e distinguir se é identificadores, expressoesMatematicas ou condicoesComparacoesBasicas
  private boolean condicao(){ 
  // Negação
    if (token != null && "!".equals(token.lexema)) {
      return negacaoCondicao() && condicaoDerivada();
    }
    // Token IDENTIFIER --> faz lookahead direto aqui (em comum para varias situações)
    if (token != null && "IDENTIFIER".equals(token.tipo)) { 
      // LOOKAHEAD DIRETO: ver próximo token sem consumir
      if (!tokens.isEmpty()) {
        Token nextToken = tokens.get(0);
        // se próximo token é operador RELACIONAL, é comparação
        if (firsts.get("operacaoRelacional").contains(nextToken.lexema)) {
          return condicaoComparacoesBasicas() && condicaoDerivada();
        }
        // se próximo token é operador MATEMÁTICO, é expressão
        if (Set.of("+", "-", "*", "/", "^").contains(nextToken.lexema)) {
          return expressoesMatematicas() && condicaoDerivada();
        }
      }
      // se não é nenhum dos casos acima, é identificador sozinho
      return identificadores() && condicaoDerivada();
    }
    // expressões matemáticas que começam com (
    if (token != null && "(".equals(token.lexema)) {
        return expressoesMatematicas() && condicaoDerivada();
    }
    // numeros (também podem iniciar comparações)
    if (token != null && (token.tipo.equals("INTEGER") || token.tipo.equals("DECIMAL"))) {
      return condicaoComparacoesBasicas() && condicaoDerivada();
    }
    
    erro("condicao");
    return false;
   /*
    if((first("condicaoComparacoesBasicas") && condicaoComparacoesBasicas() && condicaoDerivada())||
    (first("identificadores") && identificadores() && condicaoDerivada())||
    (first("negacaoCondicao") && negacaoCondicao() && condicaoDerivada())||
    (first("expressoesMatematicas") && expressoesMatematicas() && condicaoDerivada())){
      System.out.println("deu match na condicao");
      return true;
    }
    erro("condicao");
    return false;
    */
  }

  //condicao’ -> operacao condição condicao’| ε ????????????????? ACHO Q N FAZ SENTIDO
  /*
   * simbulos     first
   * condicao’   first é first dessa regra: operacao
   */
  private boolean condicaoDerivada(){
    System.out.println("entrou em condicaoDerivada");
    if(first("operacao") && operacao() && condicao() && condicaoDerivada()){
      System.out.println("deu match em condicao derivada");
      return true;
    }
    return true; //ε
  }


  //condicaoComparacoesBasicas ->  comparacoesBasicas || !identificadores
  /*
   * simbulos                       first
   * condicaoComparacoesBasicas    first é ! e first dessa regra: comparacoesBasicas
   */
  private boolean condicaoComparacoesBasicas(){
    System.out.println("entrou em condicaoComparacoesBasicas");
    if((first("condicaoComparacoesBasicas") && comparacoesBasicas())|| (matchL("!") && identificadores()) ){
      return true;
    }
    erro("condicaoComparacoesBasicas");
    return false;
  }

  //comparacoesBasicas -> identificadores|numero operacao valoresOperacao 
  /*
   * simbulos                       first
   * comparacoesBasicas    first é first dessas regras: identificadores, numero
   */
  private boolean comparacoesBasicas(){
    System.out.println("entrou em comparacoesBasicas");
    if(((first("identificadores") && identificadores())||(first("numero") && numero())) && operacao() && valoresOperacao()){
      return true;
    }
    erro("comparacoesBasicas");
    return false;
  }

  //valoresOperacao -> identificadores|numero|boolean
   /*
   * simbulos          first
   * valoresOperacao   first é first dessas regras: identificadores, numero, isBoolean
   */
  private boolean valoresOperacao(){ //funcoes atomicas(verificam diretamente tokens), n preciso colocar first
    if(first("identificadores") && identificadores()|| first("numero") && numero()|| first("isBoolean") && isBoolean()){
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
    System.out.println("entrou na negacaoCondicao");
    if(matchL("!") && condicao()){
      System.out.println("deu match em negacaoCondicao");
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
    System.out.println("entrou na operacao");
    if(first("operacaoRelacional") && operacaoRelacional() || first("operacaoLogica") && operacaoLogica()){
      System.out.println("deu match na operacao");
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
    System.out.println("entrou na operacaoRelacional");
    if(matchL("<")|| matchL(">") || matchL("<>") || matchL("<->")|| matchL("<=")|| matchL(">=")){
      System.out.println("deu match na operacao relacional");
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
    System.err.println("entrou em listaComandosInternos");
    if(first("comandoInterno") && comandoInterno() && listaComandosInternos()){
      return true;
    }
    return true; //ε
  }
  
  //cabecalhoPara -> inicializacao ";" condicao ";" incremento
   /*
   * simbulos              first
   * cabecalhoPara        first é first dessa regra: inicializacao = first tipovariael = first delcaracao
   */
  private boolean cabecalhoPara(){ 
    System.out.println("entrou no cabecalhoPara");
    if(first("declaracao") && inicializacao() && matchL(";") && condicao() && matchL(";") && incremento()){
      System.out.println("dei match em cabecalhopara");
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
  private boolean atribui(){  // AJUSTAR PRA DEFINIR QUAL DAS REGRAS VAI ENTRAR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    System.out.println("entrou em atribui");
    if(first("identificadores") && identificadores() && matchL("->") && valor() && matchL(";")){
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
    System.out.println("entrei em expressoesMatematicas");
   // System.out.println("TOKEN atual expMat"+token);
   // System.out.println("TOKENS atual expMat"+tokens);
    if(first("precedenciaSuperior") && precedenciaInferior()){
      System.out.println("dei match em expressoesMatematicas");
      return true;
    }
    erro("expressoesMatematicas");
    return false;
  }

 //valor -> numero|texto|boolean|identificadores|expressoesMatematicas|condicaoComparacoesBasicas
    /*
   * simbulos       first
   * valor          first é first dessas regras: numero,texto,isBoolean, identificadores,expressoesMatematicas
   */
  private boolean valor(){ 
  //  System.out.println("entrei em valor");
  //  System.out.println("TOKENSSSSS"+tokens);
  //  System.out.println("TOKEN"+token);

    //tokens é a lista com os proximos tokens             //quando quero chamar funcao e tem texto no comeco // argumento da chamada (ultimo argumento)// quando tem mais argumentos
    if(tokens.get(0).lexema.equals(";") || token.tipo.equals("TEXT") || tokens.get(0).lexema.equals(")") ||  tokens.get(0).lexema.equals(",") ){//se proximo token da lista for ; é declaracao simples e pode ser numero/texto/isboolean/identificadores
       System.out.println("entrou na opcao basica em valor()");
      if((first("numero") && numero())||(first("texto") && texto())||(first("isBoolean") && isBoolean())||(first("identificadores") && identificadores())){
        System.out.println("deu match nas opcoes basicas de valor");
        return true;
      }
    }
    else if (firsts.get("operacao").contains(token.lexema) || firsts.get("operacao").contains(tokens.get(0).lexema)){//se token atual for algum dos tokens do first operacao ou prox token for algum dos tokens do first de operacao
      System.out.println("entrou na opcao condicaoComparacoesBaiscas em valor()");
      return condicaoComparacoesBasicas();
    }
    else if(firsts.get("validaExpressao").contains(token.lexema) || firsts.get("validaExpressao").contains(tokens.get(0).lexema)){//se token atual for algum dos tokens do first precedenciasuperior (representa expressoesmatematicas) ou prox token for algum dos tokens do first de precedenciasuperior
      System.out.println("entrou na opcao expressoesMatematicas em valor()");
      return expressoesMatematicas();
    }
    else{
      return false;
    }
    erro("valor");
    return false;
  }
    /* 
     if((first("numero") && numero())||(first("texto") && texto())||(first("isBoolean") && isBoolean())||
    (first("identificadores") && identificadores())||( first("precedenciaSuperior") && expressoesMatematicas())||
    (first("condicaoComparacoesBasicas") && condicaoComparacoesBasicas())){
      System.out.println("deu match em valor");
      return true;
    }
    erro("valor");
    return false;
  }
  */

  //numero -> numeroDecimal|numeroInteiro
   /*
   * simbulos         first
   * numero           first é first dessas regras: decimal, inteiro
   */
  private boolean numero(){ 
    if(first("decimal") && decimal()||first("inteiro") && inteiro()){
      return true;
    }
    erro("numero");
    return false;
  }

  //comandoInterno -> se|ouSe|senao|para|lacoEnquanto|atribuicao|chamarFuncao|retornar
  /*
   * simbulos          first
   * comandoInterno    first é first dessas regras:seCompleto,para,lacoEnquanto,declaracao,atribui,chamarFuncao,retornar
   */
  private boolean comandoInterno(){
    System.out.println("entrou em comandoInterno");
    if((first("se")&& seCompleto())||(first("para") && para())||
    (first("lacoEnquanto") && lacoEnquanto())||(first("declaracao") && declaracao())||
    (first("atribui") && atribui())||(first("chamarFuncao") && chamarFuncao())||(first("retornar") && retornar())){
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
    System.out.println("entrou em retornar");
    if(matchL("retorna") && conteudos() && matchL(";")){
      System.out.println("deu match em retornar");
      return true;
    }
    erro("retornar");
    return false;
  }

  //conteudos -> identificadores|expressoesMatematicas|numero|isBoolean
  /*
   * simbulos          first
   * conteudos      first é first dessas regras: identificadores, expressoesMatematicas,numero
   */
  private boolean conteudos(){ 
    System.out.println("entrei em conteudos");
    if((first("identificadores") && identificadores())||(first("numero") && numero())||
    (first("expressoesMatematicas") && expressoesMatematicas())||first("isBoolean") && isBoolean()){
      System.out.println("dei match em conteudos");
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
    System.out.println("entrou na inicializacao"); 
    if(first("declaracao") && tipoVariavel() && identificadores() && matchL("->") && conteudos()){
    //  System.out.println("dei match em inicializacao");
      return true;
    }
    erro("inicializacao");
    return false;
  }

  //precedenciaInferior -> precedenciaIntermediaria precedenciaInferior'
  /*
   * simbulos               first
   * precedenciaInferior    first é first dessa regra:precedenciaIntermediaria = first("precedenciaSuperior")
   */
  private boolean precedenciaInferior(){
    System.out.println("entrei em precedenciaInferior");
  //  System.out.println("TOKEN em precedenciaInferior"+token);
   // System.out.println("TOKENS em precedenciaInferior"+tokens);
    if(first("precedenciaSuperior") && precedenciaIntermediaria() && precedenciaInferiorDerivada()){
      System.out.println("dei match em precedenciaInferior");
      return true;
    }
    erro("precedenciaInferior");
    return false;
  }

  //precedenciaIntermediaria -> precedenciaAlta precedenciaIntermediaria'
    /*
   * simbulos                     first
   * precedenciaIntermediaria     first é first dessa regra:precedenciaAlta = first("precedenciaSuperior")
   */
  private boolean precedenciaIntermediaria(){
    System.out.println("entrei em precedenciaIntermediaria");
   // System.out.println("TOKEN em precedenciaIntermediaria"+token);
   // System.out.println("TOKENS em precedenciaIntermediaria"+tokens);
    if(first("precedenciaSuperior") && precedenciaAlta() && precedenciaIntermediariaDerivada()){
      System.out.println("dei match em precedenciaIntermediaria");
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
    System.out.println("entrei em precedenciaInferiorDerivada");
   // System.out.println("TOKEN em precedenciaInferiorDerivada"+token);
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
    System.out.println("entrei em precedenciaAlta");
 //   System.out.println("TOKEN em precedenciaAlta"+token);
 //   System.out.println("TOKENS em precedenciaAlta"+tokens);
    if(first("precedenciaSuperior") && precedenciaSuperior() && precedenciaAltaDerivada()){
    System.out.println("dei match em precedenciaAlta");
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
    System.out.println("entrei em precedenciaIntermediariaDerivada");
   // System.out.println("TOKEN em precedenciaIntermediariaDerivada"+token);
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
    System.out.println("entrei em precedenciaSuperior");
  //  System.out.println("TOKEN em precedenciaSuperior"+token);
  //  System.out.println("TOKENS em precedenciaSuperior"+tokens);
    if((first("identificadores") && identificadores())||(first("numero") && numero())|| ((matchL("(") && expressoesMatematicas() && matchL(")")))){
      System.out.println("dei match em precedenciaSuperior");
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
    System.out.println("entrei em precedenciaAltaDerivada");
  //  System.out.println("TOKEN em precedenciaAltaDerivada"+token);
    if(matchL("^") && precedenciaSuperior() && precedenciaAltaDerivada()){
      return true;
    }
    return true;//ε
  }

  //incremento -> identificadores '->' expressoesMatematicas
  //incremento -> identificadores operacaoIncremento 
  /*
   * simbulos        first
    * incremento     first é first dessa regra: identificadores
   */
  private boolean incremento(){ 
    System.out.println("entrei em incremento");
    if((first("identificadores") && identificadores() && operacaoIncremento()
    )){
      System.out.println("dei match em incrmeento");
      return true;
    }
    erro("incremento");
    return false;
  }

  //operacaoIncremento -> operadorSoma operadorSoma|operadorSubtracao operadorSubtracao
  //é melhor reutilizar os token que já existem de +,-
  private boolean operacaoIncremento(){ 
    System.out.println("entrei em operacaoIncremento");
    if((tokens.get(0).lexema.equals("+") || (tokens.get(0).lexema.equals("-"))) &&
    (firsts.get("validaIncremento").contains(token.lexema))){
       System.out.println("dei match em operacaoIncremento");
      // Consome o primeiro operador
      matchL(token.lexema);
      // Consome o segundo operador  
      matchL(token.lexema);
      return true;
    }
    erro("operacaoIncremento");
    return false;
  }

  //tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto 
  /*
   * simbulos        first
  * tipoVariavel    inteiro,decimal, texto, verdadeiroFalso
   */
  private boolean tipoVariavel(){
    System.out.println("entrou tipovariavel");
    if(matchL("inteiro")||matchL("decimal")||matchL("texto")||matchL("verdadeiroFalso")){
      System.out.println("deu algum match em tipo variavel");
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
    System.out.println("entrei no identificadores");
    if(matchT("IDENTIFIER")){
      System.out.println("dei match em identificadores");
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
    System.out.println("entrou em texto");
    if(matchT("TEXT")){
      System.out.println("deu match em texto");
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
    System.out.println("entrou palavrReservadaNomeFuncao");
    if(matchT("FUNCTION_NAME")){
      return true;
    }
    erro("palavraReservadaNomeFuncao");
    return false;
  }

  private void inicializarFirsts() {
    // FIRST dos comandos
    //key = nome funcao, e1 = first

    //só aplico first no codigo quando nas opcoes de uma regra sao varias regras e quando entro nessas regras não tem terminais

    //firsts para comando:
    firsts.put("comando", Set.of("inteiro","decimal","texto","verdadeiroFalso","se","para","lacoEnquanto","IDENTIFIER","criar","FUNCTION_NAME","Entrada","Imprima"));
    //firsts para regras dentro de comando:
    firsts.put("declaracao", Set.of("inteiro","decimal","texto","verdadeiroFalso"));
    firsts.put("seCompleto", Set.of("se"));
    firsts.put("para", Set.of("para"));
    firsts.put("lacoEnquanto", Set.of("lacoEnquanto"));
    firsts.put("atribui", Set.of("IDENTIFIER"));
    firsts.put("criarFuncao", Set.of("criar"));
    firsts.put("chamarFuncao", Set.of("FUNCTION_NAME","Entrada","Imprima"));
    //firsts valor
    firsts.put("valor", Set.of("DECIMAL","INTEGER","TEXT","true","false","IDENTIFIER","("));
    //firsts de cada rgra dentro da regra valor()
    firsts.put("texto", Set.of("TEXT"));
    firsts.put("isBoolean", Set.of("true","false"));
    //PROBLEMA, ESSES FIRSTS EM COISAS EM COMUM:
    firsts.put("numero", Set.of("DECIMAL","INTEGER"));
    firsts.put("identificadores", Set.of("IDENTIFIER"));
    firsts.put("condicaoComparacoesBasicas", Set.of("IDENTIFIER", "DECIMAL","INTEGER")); 
    firsts.put("precedenciaSuperior", Set.of("(", "IDENTIFIER","DECIMAL","INTEGER"));
     //firsts operacao:
    firsts.put("operacao", Set.of("<",">","<>","<->","<=",">=","e","ou","!"));
    //firsts operacaoRelacional:
    firsts.put("operacaoRelacional", Set.of("<",">","<>","<->","<=",">="));
    //firsts operacaoLogica:
    firsts.put("operacaoLogica", Set.of("e","ou","!"));
    //firsts comandoInterno:
    firsts.put("comandoInterno", Set.of("se","para","lacoEnquanto","inteiro","decimal","texto","verdadeiroFalso","IDENTIFIER","FUNCTION_NAME","Entrada","Imprima","retorna"));

    // firsts basicos:
    firsts.put("se", Set.of("se"));
    firsts.put("ouSe", Set.of("ouSe"));
    firsts.put("senao", Set.of("senao"));
    firsts.put("palavraReservadaNomeFuncao", Set.of("FUNCTION_NAME"));
    firsts.put("retornar", Set.of("retorna"));
    firsts.put("negacaoCondicao", Set.of("!"));
    firsts.put("decimal", Set.of("DECIMAL"));
    firsts.put("inteiro", Set.of("INTEGER"));

    //proximos tokens de expressoes matematicas para verificar se lista de tokens possue algum deles (nao é first, porem vou colocar aqui pra facilitar e n ter q criar outra hash)
    firsts.put("validaExpressao", Set.of("(","+","-","*","/","^"));

    //apenas para validar operacaoIncremento (nao é first, porem vou colocar aqui pra facilitar e n ter q criar outra hash)
    firsts.put("validaIncremento", Set.of("+","-"));
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