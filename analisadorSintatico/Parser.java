package analisadorSintatico;  

import analisadorLexico.Token;  
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
  List<Token> tokens;
  Token token;
  Map<String, Set<String>> firsts = new HashMap<>();
  Map<String, Set<String>> follows = new HashMap<>();
  Tree tree; 
  int contadorErro;

  public Parser(List<Token> tokens) {
    this.tree = new Tree(new Node("PROGRAM")); 
    this.tokens = tokens;
    inicializarFirsts();
    inicializarFollows();
  }

  /*
  -verificar apos tradução se: 
  A parte de expressões envolvendo os operadores matemáticos deve ser realizada de maneira correta, respeitando a precedência.
  -verificar se da para ler da tela com Entrada e imprimir no console com Imprima() 

   */
   public void main() {
    token = getNextToken();
    //como saber se td realmente fica dentro da main????????????????????????
    header();
    Node root = new Node("listaComando");
    tree.setRoot(root);
    if (listaComandos(root) && matchT("EOF",root) &&  contadorErro == 0){
      footer();
      System.out.println("Sintaticamente correto");
      //tree.preOrder();//imprime em pre ordem
      //tree.printCode(); //imprimeas folhas(codigo)
      tree.printTree(); //imprimea arvore
    }else{
      erro("main");
      System.out.println("Sintaticamente Incorreto! Programa caiu em " + contadorErro + " regras de erro(s) sintático(s)");
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

  // --------------------USADO QUANDO SÓ É ANALISE SINTÁTICA, SEM AARVORE DE DERIVACAO-----------------

  /*
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

   */
  
  //REGRAS DA GRAMÁTICA

  //verificar como tava declararetribuir, decarar e atribuicao (commit antes) deu trocar declaracao p ver se da p declarar qqr coisa!!!!!!!!!!

  //VER SE FIZ CERTO EPSULON:Palavra vazia é sair de uma regra sem casar token = return true (follow)
  //ONDE NAO TEM RETURN FALSE (E SIM RETURN TRUE)  //testar passar coisa incorreta para ver se aceita (EX: Aargumentoschamada, senaoopcional, listaouse...)
  //VE SE DA P OTIMIZA ALGUM CODIGO (ex: restoArgumentosChamada())


  //site que calcula first e follow: REFAZER FIST CONSIDERANDO EPSULON !!!!!!!!!!!!!!!


  // listaComandos -> comando listaComandos | ε
  /*
   * simbulos           first                                                 follow
   * listaComandos      comando (regra,ent first é firsts dessa regra), ε        $
   */
  private boolean listaComandos(Node root){
    Node listaComandoNode = root.addNode("listaComandos");

    //se tiver Entrada no codigo adiciona isso: traduz("import java.util.Scanner;");
    //traduz("public class CodigoExemplo{");

    if(first("comando") && comando(listaComandoNode) && listaComandos(listaComandoNode)){
      return true;
    }
    //follow
    if(follow("listaComandos")){
      return true; //ε
    }
    else{
      erro("listaComandos");
      contadorErro++;
      return false;
    }
  }

  //comando -> seCompleto|para|lacoEnquanto|declaracao|atribui|criarFuncao|chamarFuncao
  /*
   * simbulos     first
   * comando     so regra, ent first é firsts dessas regras: (seCompleto,para,lacoEnquanto,declarar,atribuicao,criarFuncao,chamarFuncao)
   */
  private boolean comando(Node root){
    Node comandoNode = root.addNode("comando");
    if((first("declaracao") && declaracao(comandoNode))||(first("seCompleto") && seCompleto(comandoNode))||
    (first("para") && para(comandoNode))||(first("lacoEnquanto") && lacoEnquanto(comandoNode))||
    (first("atribui") && atribui(comandoNode))||(first("criarFuncao") && criarFuncao(comandoNode))||
    (first("chamarFuncao") && chamarFuncao(comandoNode))){
      return true;
    }
    erro("comando"); 
    contadorErro++;
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
  private boolean declaracao(Node root){
    Node declaracaoNode = root.addNode("declaracao");
    if(first("declaracao") && tipoVariavel(declaracaoNode) && identificadores(declaracaoNode)) { //uso first declaracao pq é igual ao first de tipovariavel, ai n tenho que criar outra regra pros mesmos firsts
      // Pode ser: tipo var; OU tipo var -> valor;
      if(matchL(";",";", declaracaoNode)) {
        return true; // declaracao simples
      } 
      else if(matchL("->","=",declaracaoNode) && valor(declaracaoNode) && matchL(";",";",declaracaoNode)) {
        return true; // declaracao com atribuicao
      }
    }
    erro("declaracao");
    contadorErro++;
    return false;
  }

  //seCompleto ->se listaOuSe senaoOpcional
  /*
   * simbulos            first                                              
   *  seCompleto          "se"
   */
  private boolean seCompleto(Node root){
    Node seCompletoNode = root.addNode("seCompleto");
      if(first("se")&& se(seCompletoNode) && listaOuSe(seCompletoNode) && senaoOpcional(seCompletoNode)){
        return true;
      }
    return false; // n tem erro especifico pq a regra especifcia (se, ouse e senao) darao o erro
  }

  //listaOuSe > ouSe listaOuSe | ε
   /*
   * simbulos     first
   * listaOuSe    ε, first é firsts dessa regra: ouSe
   */
  private boolean listaOuSe(Node root){
    Node listaOuSeNode = root.addNode("listaOuSe");
    if(first("ouSe") && ouSe(listaOuSeNode) && listaOuSe(listaOuSeNode)){
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
  private boolean senaoOpcional(Node root){
    Node senaoOpcionalNode = root.addNode("senaoOpcional");
    if(first("senao") && senao(senaoOpcionalNode)){
      return true;
    }
    return true;//ε
  }

  //se -> 'se''('condicao')''{'listaComandosInternos'}'
  /*
   * simbulos         first
   * se                se
  */
  private boolean se(Node root){ 
    Node seNode = root.addNode("se");
     if (matchL("se","if",seNode) && matchL("(","(",seNode) && condicao(seNode) && matchL(")",")",seNode) &&
      matchL("{","{",seNode) &&
      listaComandosInternos(seNode) && matchL("}","}",seNode)){
        return true;
     }
    erro("se");
    contadorErro++;
    return false;
  }
 
  //ouSe -> 'ouSe''('condicao')''{'listaComandosInternos'}'
  /*
   * simbulos         first
   * ouSe             ouSe
   */
  private boolean ouSe(Node root){
    Node ouSeNode = root.addNode("ouSe");
    if(matchL("ouSe","else if",ouSeNode) && matchL("(","(",ouSeNode) && condicao(ouSeNode) && matchL(")",")",ouSeNode) &&
     matchL("{","{",ouSeNode) &&
     listaComandosInternos(ouSeNode) && matchL("}","}",ouSeNode)){
        return true;
     }
    erro("ouSe");
    contadorErro++;
    return false;
  }

  //senao -> 'senao''{'listaComandosInternos'}'
   /*
   * simbulos       first
   * senao          senao
   */
  private boolean senao(Node root){ 
    Node senaoNode = root.addNode("senao");
    if(matchL("senao","else",senaoNode) && matchL("{","{",senaoNode) &&
     listaComandosInternos(senaoNode) && matchL("}","}",senaoNode)){
      return true;
     }
    erro("senao");
    contadorErro++;
    return false;
  }

  //para -> 'para''('cabecalhoPara')''{'listaComandosInternos'}'
   /*
   * simbulos     first
   * para          para
   */
  private boolean para(Node root){
    Node paraNode = root.addNode("para");
    if(matchL("para","for",paraNode) && matchL("(","(",paraNode) && cabecalhoPara(paraNode) &&
     matchL(")",paraNode) && matchL("{",paraNode) && listaComandosInternos(paraNode) && matchL("}",paraNode)){
      return true;
     }
    erro("para");
    contadorErro++;
    return false;
  }

  //lacoEnquanto -> 'lacoEnquanto''('condicao')''{'listaComandosInternos'}'
  /*
   * simbulos         first
   * lacoenquanto     lacoenquanto
   */
  private boolean lacoEnquanto(Node root){ 
    Node lacoEnquantoNode = root.addNode("lacoEnquanto");
    if(matchL("lacoEnquanto",lacoEnquantoNode) && matchL("(",lacoEnquantoNode) && condicao(lacoEnquantoNode) && matchL(")",lacoEnquantoNode) &&
     matchL("{",lacoEnquantoNode) && listaComandosInternos(lacoEnquantoNode) && matchL("}",lacoEnquantoNode)){
        return true;
     }
    erro("lacoEnquanto");
    contadorErro++;
    return false;
  }


  //criarFuncao -> 'criar' palavra_reservadaNomeFuncao'('argumentosFuncao')''{'listaComandosInternos'}'
  /*
   * simbulos            first
   * criarFuncao         criar
   */
  private boolean criarFuncao(Node root){ 
    Node criarFuncaoNode = root.addNode("criarFuncao");
    if(matchL("criar",criarFuncaoNode) && palavraReservadaNomeFuncao(criarFuncaoNode) && matchL("(",criarFuncaoNode) && argumentosFuncao(criarFuncaoNode) &&
     matchL(")",criarFuncaoNode) && matchL("{",criarFuncaoNode) && listaComandosInternos(criarFuncaoNode)  && matchL("}",criarFuncaoNode)){
      return true;
    }
    erro("criarFuncao");
    contadorErro++;
    return false;
  }

  //chamarFuncao -> inicioChamarFuncao '('argumentosChamada')' ';'
  /*
   * simbulos            first
   * chamarFuncao          "entrada","imprima", first é firsts dessa regra palavraReservadaNomeFuncao
   */
  private boolean chamarFuncao(Node root) {
    Node chamarFuncaoNode = root.addNode("chamarFuncao");
    if (first("chamarFuncao") && inicioChamarFuncao(chamarFuncaoNode) && matchL("(",chamarFuncaoNode) && argumentosChamada(chamarFuncaoNode) &&
     matchL(")",chamarFuncaoNode) && matchL(";",chamarFuncaoNode)) {
      return true;
    }
    erro("chamarFuncao");
    contadorErro++;
    return false;
}

  //chamarFuncaoSemFim -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')' 
  //usada quando desejamos atribui o resultado de uma funcao a uma declaracao ou chamar o resultado de uma funcao em outra funcao....
  private boolean chamarFuncaoSemFim(Node root) {
    Node chamarFuncaoSemFimNode = root.addNode("chamarFuncaoSemFim");
    if (first("chamarFuncao") && inicioChamarFuncao(chamarFuncaoSemFimNode) && matchL("(",chamarFuncaoSemFimNode) &&
     argumentosChamada(chamarFuncaoSemFimNode) && matchL(")",chamarFuncaoSemFimNode)) {
      return true;
    }
    erro("chamarFuncao");
    contadorErro++;
    return false;
}

  //inicioChamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima
   /*
   * simbulos            first
   * inicioChamarFuncao          "entrada","imprima", first é firsts dessa regra palavraReservadaNomeFuncao
   */
  private boolean inicioChamarFuncao(Node root){
    Node inicioChamarFuncaoNode = root.addNode("inicioChamarFuncao");
    if((first("palavraReservadaNomeFuncao") && palavraReservadaNomeFuncao(inicioChamarFuncaoNode))|| matchL("Entrada",inicioChamarFuncaoNode)||
    matchL("Imprima",inicioChamarFuncaoNode)){
    return true;
   }
    erro("inicioChamarFuncao");
    contadorErro++;
    return false;
  }
  //argumentosChamada -> ε | valor restoArgumentosChamada
   /*
   * simbulos            first
   * argumentosChamada   ε, first é first dessa regra: valor
   */
  private boolean argumentosChamada(Node root){
    Node argumentosChamadaNode = root.addNode("argumentosChamada");
    if(first("valor") && valor(argumentosChamadaNode) && restoArgumentosChamada(argumentosChamadaNode)){
      return true;
    }
    return true;//ε
  }

  //restoArgumentosChamada -> ε | ',' valor restoArgumentosChamada
   /*
   * simbulos                   first
   * restoArgumentosChamada    ε,','
   */
  private boolean restoArgumentosChamada(Node root){
    Node restoArgumentosChamadaNode = root.addNode("restoArgumentosChamada");
    if(matchL(",",restoArgumentosChamadaNode) && valor(restoArgumentosChamadaNode) && restoArgumentosChamada(restoArgumentosChamadaNode)){
      return true;
    }
    return true; //ε
  }

  //argumentosFuncao -> ε|parametrosFuncao
   /*
   * simbulos               first
   * argumentosFuncao      ε, first é first dessa regra: parametrosFuncao
   */
  private boolean argumentosFuncao(Node root){
    Node argumentosFuncaoNode = root.addNode("argumentosFuncao");
      if(first("declaracao") && parametroFuncao(argumentosFuncaoNode)){
        return true;
      }
    return true;//ε
  }

  //parametroFuncao -> parâmetro emComumParametro
  /*
   * simbulos            first
   * parametroFuncao    first é first dessa regra: parametro
   */
  private boolean parametroFuncao(Node root){
    Node parametroFuncaoNode = root.addNode("parametroFuncao");
    if(first("declaracao") && parametro(parametroFuncaoNode) && emComumParametro(parametroFuncaoNode)){
      return true;
    }
    erro("parametroFuncao");
    contadorErro++;
    return false;
  }

  //emComumParametro -> ε | ‘,’ parametroFuncao emComumParametro
   /*
   * simbulos            first
   * emComumParametro    ε , ‘,’ 
   */
  private boolean emComumParametro(Node root){
    Node emComumParametroNode = root.addNode("emComumParametro");
    if(matchL(",",emComumParametroNode) && parametroFuncao(emComumParametroNode) && emComumParametro(emComumParametroNode)){
      return true;
    }
    return true; //ε 
  }

  //parametro -> tipoVariavel identificadores
  /*
   * simbulos            first
   * parametro         first é first dessa regra: tipoVariavel
   */
  private boolean parametro(Node root){
    Node parametroNode = root.addNode("parametro");
    if(first("declaracao") && tipoVariavel(parametroNode) && identificadores(parametroNode)){
      return true;
    }
    erro("parametro");
    contadorErro++;
    return false;
  }

  //condicao -> identificadores condicao’ | negacaoCondicao condicao’ | expressoesMatematicas condicao’| condicaoComparacoesBasicas condicao’
   /*
   * simbulos     first
   * condicao  first é first dessas regras: identfificadores, negacaocondicao, expressoesMatematicas, condicaoComparacoesBasicas
   */

  //implementação do lookahead para pegar o prox token e distinguir se é identificadores, expressoesMatematicas ou condicoesComparacoesBasicas
  private boolean condicao(Node root) {
    Node condicaoNode = root.addNode("condicao");
    // Negação
    if (token != null && "!".equals(token.lexema)) {
        return negacaoCondicao(condicaoNode) && condicaoDerivada(condicaoNode);
    }
    // Token IDENTIFIER - lookahead melhorado
    if (token != null && "IDENTIFIER".equals(token.tipo)) {
      // Lookahead mais inteligente: verifica se há operadores matemáticos antes de relacionais
      if (!tokens.isEmpty()) {
        // Procura por operadores matemáticos OU relacionais nos próximos tokens
        boolean encontrouMatematico = false;
        boolean encontrouRelacional = false;
      
        for (int i = 0; i < Math.min(3, tokens.size()); i++) {
          Token nextToken = tokens.get(i);
          String lexema = nextToken.lexema;
          if (Set.of("+", "-", "*", "/", "^").contains(lexema)) {
            encontrouMatematico = true;
            break;
          }
          if (Set.of("<", ">", "<>", "<->", "<=", ">=").contains(lexema)) {
            encontrouRelacional = true;
            break;
          }
        }
        // Se encontrou operador matemático, é expressão matemática
        if (encontrouMatematico) {
          return expressoesMatematicas(condicaoNode) && condicaoDerivada(condicaoNode);
        }
        // Se encontrou operador relacional, é comparação básica
        else if (encontrouRelacional) {
          return condicaoComparacoesBasicas(condicaoNode) && condicaoDerivada(condicaoNode);
        }
      }
      // Se não encontrou nenhum operador, é identificador sozinho
      return identificadores(condicaoNode) && condicaoDerivada(condicaoNode);
    }
    // expressões matemáticas que começam com (
    if (token != null && "(".equals(token.lexema)) {
      return expressoesMatematicas(condicaoNode) && condicaoDerivada(condicaoNode);
    }
    // números - verifica se são parte de comparações
    if (token != null && (token.tipo.equals("INTEGER") || token.tipo.equals("DECIMAL"))) {
      // Lookahead para ver se é número sozinho ou parte de comparação
      if (!tokens.isEmpty()) {
        Token nextToken = tokens.get(0);
        if (firsts.get("operacaoRelacional").contains(nextToken.lexema)) {
          return condicaoComparacoesBasicas(condicaoNode) && condicaoDerivada(condicaoNode);
        }
      }
      // Se não tem operador relacional, trata como expressão matemática
      return expressoesMatematicas(condicaoNode) && condicaoDerivada(condicaoNode);
    }
    // outros casos de comparações básicas
    if (first("condicaoComparacoesBasicas") && condicaoComparacoesBasicas(condicaoNode) && condicaoDerivada(condicaoNode)) {
      return true;
    }
    erro("condicao");
    contadorErro++;
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
  private boolean condicaoDerivada(Node root){
    Node condicaoDerivadaNode = root.addNode("condicaoDerivada");
    if(first("operacao") && operacao(condicaoDerivadaNode) && condicao(condicaoDerivadaNode) && condicaoDerivada(condicaoDerivadaNode)){
      return true;
    }
    return true; //ε
  }


  //condicaoComparacoesBasicas ->  comparacoesBasicas || !identificadores
  /*
   * simbulos                       first
   * condicaoComparacoesBasicas    first é ! e first dessa regra: comparacoesBasicas
   */
  private boolean condicaoComparacoesBasicas(Node root){
    Node condicaoComparacoesBasicasNode = root.addNode("condicaoComparacoesBasicas");
    if((first("condicaoComparacoesBasicas") && comparacoesBasicas(condicaoComparacoesBasicasNode))|| (matchL("!",condicaoComparacoesBasicasNode) &&
     identificadores(condicaoComparacoesBasicasNode))){
      return true;
    }
    erro("condicaoComparacoesBasicas");
    contadorErro++;
    return false;
  }

  //comparacoesBasicas -> identificadores|numero operacao valoresOperacao 
  /*
   * simbulos                       first
   * comparacoesBasicas    first é first dessas regras: identificadores, numero
   */
  private boolean comparacoesBasicas(Node root){
    Node comparacoesBasicasNode = root.addNode("comparacoesBasicas");
    if(((first("identificadores") && identificadores(comparacoesBasicasNode))||(first("numero") && numero(comparacoesBasicasNode))) &&
     operacao(comparacoesBasicasNode) && valoresOperacao(comparacoesBasicasNode)){
      return true;
    }
    erro("comparacoesBasicas");
    contadorErro++;
    return false;
  }

  //valoresOperacao -> identificadores|numero|boolean
   /*
   * simbulos          first
   * valoresOperacao   first é first dessas regras: identificadores, numero, isBoolean
   */
  private boolean valoresOperacao(Node root){ //funcoes atomicas(verificam diretamente tokens), n preciso colocar first
    Node valoresOperacaoNode = root.addNode("valoresOperacao");
    if(first("identificadores") && identificadores(valoresOperacaoNode)|| first("numero") && numero(valoresOperacaoNode)|| first("isBoolean") &&
     isBoolean(valoresOperacaoNode)){
      return true;
    }
    erro("valoresOperacao");
    contadorErro++;
    return false;
  }

  //negacaoCondicao -> '!'condicao
   /*
   * simbulos          first
   * negacaoCondicao    !
   */
  private boolean negacaoCondicao(Node root){
    Node negacaoCondicaoNode = root.addNode("negacaoCondicao");
    if(matchL("!",negacaoCondicaoNode) && condicao(negacaoCondicaoNode)){
      return true;
    }
    erro("negacaoCondicao");
    contadorErro++;
    return false;
  }

  //operacao -> operacaoRelacional|operacaoLogica
   /*
   * simbulos              first
   * operacao            first é first dessas regras: operacaoRelacional, operacaoLogica
   */
  private boolean operacao(Node root){
    Node operacaoNode = root.addNode("operacao");
    if(first("operacaoRelacional") && operacaoRelacional(operacaoNode) || first("operacaoLogica") && operacaoLogica(operacaoNode)){
      return true;
    }
    erro("operacao");
    contadorErro++;
    return false;
  }

  //operacaoRelacional -> operadorDiferente|operadorIgualdade|operadorMenorIgual|operadorMaiorigual
    /*
   * simbulos              first
   * operacaoRelacional    <>,<->,<=,>=
   */
  private boolean operacaoRelacional(Node root){
    Node operacaoRelacionalNode = root.addNode("operacaoRelacional");
    if(matchL("<",operacaoRelacionalNode)|| matchL(">",operacaoRelacionalNode) || matchL("<>",operacaoRelacionalNode) ||
     matchL("<->",operacaoRelacionalNode)|| matchL("<=",operacaoRelacionalNode)|| matchL(">=",operacaoRelacionalNode)){
      return true;
    }
    erro("operadorRelacional");
    contadorErro++;
    return false;
  }

  //operacaoLogica -> operador_logicoE|operador_logicoOu|operador_logicoNot
    /*
   * simbulos              first
   * operacaoLogica       e,ou,!
   */
  private boolean operacaoLogica(Node root){
    Node operacaoLogicaNode = root.addNode("operacaoLogica");
    if(matchL("e",operacaoLogicaNode)||matchL("ou",operacaoLogicaNode)||matchL("!",operacaoLogicaNode)){
      return true;
    }
    erro("operacaoLogica");
    contadorErro++;
    return false;
  }

  //listaComandosInternos -> comandoInterno listaComandosInternos | ε
  /*
   * simbulos                 first                                              
   * listaComandosInternos     ε, first é first dessa regra: comandointerno
   */
  private boolean listaComandosInternos(Node root){ 
    if(first("comandoInterno")) {
      Node listaComandosInternosNode = root.addNode("listaComandosInternos");
      if(comandoInterno(listaComandosInternosNode) && listaComandosInternos(listaComandosInternosNode)){
          return true;
      }
      return false;
    }
    // ε - não adiciona nó na árvore quando é vazio
  return true;
}
  
  //cabecalhoPara -> inicializacao ";" condicao ";" incremento
   /*
   * simbulos              first
   * cabecalhoPara        first é first dessa regra: inicializacao = first tipovariael = first delcaracao
   */
  private boolean cabecalhoPara(Node root){ 
    Node cabecalhoParaNode = root.addNode("cabecalhoPara");
    if(first("declaracao") && inicializacao(cabecalhoParaNode) && matchL(";",cabecalhoParaNode) && condicao(cabecalhoParaNode) && 
    matchL(";",cabecalhoParaNode) && incremento(cabecalhoParaNode)){
      return true;
    }
    erro("listaComandosInternos");
    contadorErro++;
    return false;
  }

  //atribui -> identificadores operadorAtribuicao valor ';'
   /*
   * simbulos          first
   * atribui           first é first dessa regra: identificadores
   *                   
   */
  private boolean atribui(Node root){ 
    Node atribuiNode = root.addNode("atribui");
    if(first("identificadores") && identificadores(atribuiNode) && matchL("->",atribuiNode) && valor(atribuiNode) && matchL(";",atribuiNode)){
        return true;
    }
    erro("atribui");
    contadorErro++;
    return false;
  }

  //expressoesMatematicas -> precedenciaInferior
    /*
   * simbulos                first                                           
   * expressoesMatematicas   first é first dessa regra:precedenciaInferior
   */
  private boolean expressoesMatematicas(Node root){
    Node expressoesMatematicasNode = root.addNode("expressoesMatematicas");
    if(first("precedenciaSuperior") && precedenciaInferior(expressoesMatematicasNode)){
  //    System.out.println("dei match em expressoesMatematicas");
      return true;
    }
    erro("expressoesMatematicas");
    contadorErro++;
    return false;
  }

 //valor -> numero|texto|boolean|identificadores|expressoesMatematicas|condicaoComparacoesBasicas|chamarFuncao
    /*
   * simbulos       first
   * valor          first é first dessas regras: numero,texto,isBoolean, identificadores,expressoesMatematicas
   */
  private boolean valor(Node root){ 
    Node valorNode = root.addNode("valor");
    //tokens é a lista com os proximos tokens             //quando quero chamar funcao e tem texto no comeco // argumento da chamada (ultimo argumento)// quando tem mais argumentos
    if(tokens.get(0).lexema.equals(";") || token.tipo.equals("TEXT") || tokens.get(0).lexema.equals(")") ||  tokens.get(0).lexema.equals(",") ){//se proximo token da lista for ; é declaracao simples e pode ser numero/texto/isboolean/identificadores
      if((first("numero") && numero(valorNode))||(first("texto") && texto(valorNode))||(first("isBoolean") && isBoolean(valorNode))||
      (first("identificadores") && identificadores(valorNode))){
        return true;
      }
    }
    else if (firsts.get("operacao").contains(token.lexema) || firsts.get("operacao").contains(tokens.get(0).lexema)){//se token atual for algum dos tokens do first operacao ou prox token for algum dos tokens do first de operacao
      return condicaoComparacoesBasicas(valorNode);
    }
    else if(token.tipo.equals("FUNCTION_NAME")){
      return chamarFuncaoSemFim(valorNode);
    }
    else if(firsts.get("validaExpressao").contains(token.lexema) || firsts.get("validaExpressao").contains(tokens.get(0).lexema)){//se token atual for algum dos tokens do first precedenciasuperior (representa expressoesmatematicas) ou prox token for algum dos tokens do first de precedenciasuperior
      return expressoesMatematicas(valorNode);
    }
    erro("valor");
    contadorErro++;
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
  private boolean numero(Node root){ 
    Node numeroNode = root.addNode("numero");
    if(first("decimal") && decimal(numeroNode)||first("inteiro") && inteiro(numeroNode)){
      return true;
    }
    erro("numero");
    contadorErro++;
    return false;
  }

  //comandoInterno -> se|ouSe|senao|para|lacoEnquanto|atribuicao|chamarFuncao|retornar
  /*
   * simbulos          first
   * comandoInterno    first é first dessas regras:seCompleto,para,lacoEnquanto,declaracao,atribui,chamarFuncao,retornar
   */
  private boolean comandoInterno(Node root){
    Node comandoInternoNode = root.addNode("comandoInterno");
    if((first("se")&& seCompleto(comandoInternoNode))||(first("para") && para(comandoInternoNode))||
    (first("lacoEnquanto") && lacoEnquanto(comandoInternoNode))||(first("declaracao") && declaracao(comandoInternoNode))||
    (first("atribui") && atribui(comandoInternoNode))||(first("chamarFuncao") && chamarFuncao(comandoInternoNode))||
    (first("retornar") && retornar(comandoInternoNode))){
      return true;
    }
    erro("comandoInterno"); 
    contadorErro++;
    return false;
  }
  
  //retornar -> 'retorna' tiposRetorno';'
    /*
   * simbulos     first
   * retornar    retorna
   */
  private boolean retornar(Node root){ 
    Node retornarNode = root.addNode("retornar");
    if(matchL("retorna",retornarNode) && conteudos(retornarNode) && matchL(";",retornarNode)){
      return true;
    }
    erro("retornar");
    contadorErro++;
    return false;
  }

  //conteudos -> identificadores|expressoesMatematicas|numero|isBoolean
  /*
   * simbulos          first
   * conteudos      first é first dessas regras: identificadores, expressoesMatematicas,numero
   */
  private boolean conteudos(Node root){ 
    Node conteudosNode = root.addNode("conteudos");
    if((first("identificadores") && identificadores(conteudosNode))||(first("numero") && numero(conteudosNode))||
    (first("expressoesMatematicas") && expressoesMatematicas(conteudosNode))||first("isBoolean") && isBoolean(conteudosNode)){
      return true;
    }
    erro("conteudos");
    contadorErro++;
    return false;
    }

  //inicializacao -> tipoVariavel identificadores "->" conteudos
  /*
   * simbulos         first
   * inicializacao    first é first dessa regra: tipoVariavel
   */
  private boolean inicializacao(Node root){ 
    Node inicializacaoNode = root.addNode("inicializacao");
    if(first("declaracao") && tipoVariavel(inicializacaoNode) && identificadores(inicializacaoNode) && matchL("->",inicializacaoNode) &&
    conteudos(inicializacaoNode)){
      return true;
    }
    erro("inicializacao");
    contadorErro++;
    return false;
  }

  //precedenciaInferior -> precedenciaIntermediaria precedenciaInferior'
  /*
   * simbulos               first
   * precedenciaInferior    first é first dessa regra:precedenciaIntermediaria = first("precedenciaSuperior")
   */
  private boolean precedenciaInferior(Node root){
    Node precedenciaInferiorNode = root.addNode("precedenciaInferior");
    if(first("precedenciaSuperior") && precedenciaIntermediaria(precedenciaInferiorNode) && precedenciaInferiorDerivada(precedenciaInferiorNode)){
      return true;
    }
    erro("precedenciaInferior");
    contadorErro++;
    return false;
  }

  //precedenciaIntermediaria -> precedenciaAlta precedenciaIntermediaria'
    /*
   * simbulos                     first
   * precedenciaIntermediaria     first é first dessa regra:precedenciaAlta = first("precedenciaSuperior")
   */
  private boolean precedenciaIntermediaria(Node root){
    Node precedenciaIntermediariaNode = root.addNode("precedenciaIntermediaria");
    if(first("precedenciaSuperior") && precedenciaAlta(precedenciaIntermediariaNode) && precedenciaIntermediariaDerivada(precedenciaIntermediariaNode)){
      return true;
    }
    erro("precedenciaIntermediaria");
    contadorErro++;
    return false;
  }

  //precedenciaInferior' -> '+'precedenciaIntermediaria precedenciaInferior' | '-'precedenciaIntermediaria precedenciaInferior' | ε
  /*
   * simbulos               first
   * precedenciaInferior'    "+", "-", ε
   */
  private boolean precedenciaInferiorDerivada(Node root){
    Node precedenciaInferiorDerivadaNode = root.addNode("precedenciaInferiorDerivada");
    if(matchL("+",precedenciaInferiorDerivadaNode) && precedenciaIntermediaria(precedenciaInferiorDerivadaNode) &&
     precedenciaInferiorDerivada(precedenciaInferiorDerivadaNode)){
      return true;
    }
    //ou
    if(matchL("-",precedenciaInferiorDerivadaNode) && precedenciaIntermediaria(precedenciaInferiorDerivadaNode) && 
    precedenciaInferiorDerivada(precedenciaInferiorDerivadaNode)){
      return true;
    }
    return true; //ε
  }

  //precedenciaAlta -> precedenciaSuperior precedenciaAlta'
  /*
  * simbulos            first
  * precedenciaAlta     first é first dessa regra: precedenciaSuperior
  */
  private boolean precedenciaAlta(Node root){
    Node precedenciaAltaNode = root.addNode("precedenciaAlta");
    if(first("precedenciaSuperior") && precedenciaSuperior(precedenciaAltaNode) && precedenciaAltaDerivada(precedenciaAltaNode)){
      return true;
    }
    erro("precedenciaAlta");
    contadorErro++;
    return false;
  }

  //precedenciaIntermediaria' -> '*' precedenciaAlta precedenciaIntermediaria' | /precedenciaAlta precedenciaIntermediaria' | ε
    /*
   * simbulos                    first
   * precedenciaIntermediaria'    "*", "/", ε
   */
  private boolean precedenciaIntermediariaDerivada(Node root){
    Node precedenciaIntermediariaDerivadaNode = root.addNode("precedenciaIntermediariaDerivada");
    if(matchL("*",precedenciaIntermediariaDerivadaNode) && precedenciaAlta(precedenciaIntermediariaDerivadaNode) &&
     precedenciaIntermediariaDerivada(precedenciaIntermediariaDerivadaNode)){
      return true;
    }
    //ou
    if(matchL("/",precedenciaIntermediariaDerivadaNode) && precedenciaAlta(precedenciaIntermediariaDerivadaNode) &&
     precedenciaIntermediariaDerivada(precedenciaIntermediariaDerivadaNode)){
      return true;
    }
    return true; //ε
  }

  //precedenciaSuperior -> identificadores|numero|'('expressoesMatematicas')'
    /*
   * simbulos                     first
    * precedenciaSuperior       "(", first é first dessa regra: identificadores,numero
   */
  private boolean precedenciaSuperior(Node root){
    Node precedenciaSuperiorNode = root.addNode("precedenciaSuperior");
    if((first("identificadores") && identificadores(precedenciaSuperiorNode))||(first("numero") && numero(precedenciaSuperiorNode))||
     ((matchL("(",precedenciaSuperiorNode) && expressoesMatematicas(precedenciaSuperiorNode) && matchL(")",precedenciaSuperiorNode)))){
      return true;
    }
    erro("precedenciaSuperior");
    contadorErro++;
    return false;
  }

  //precedenciaAlta' -> '^'precedenciaSuperior precedenciaAlta' | ε
     /*
   * simbulos             first
   * precedenciaAlta'    "^",ε
   */
  private boolean precedenciaAltaDerivada(Node root){
    Node precedenciaAltaDerivadaNode = root.addNode("precedenciaAltaDerivada");
    if(matchL("^",precedenciaAltaDerivadaNode) && precedenciaSuperior(precedenciaAltaDerivadaNode) && precedenciaAltaDerivada(precedenciaAltaDerivadaNode)){
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
  private boolean incremento(Node root){ 
    Node incrementoNode = root.addNode("incremento");
    if((first("identificadores") && identificadores(incrementoNode) && operacaoIncremento(incrementoNode))){
      return true;
    }
    erro("incremento");
    contadorErro++;
    return false;
  }

  //operacaoIncremento -> operadorSoma operadorSoma|operadorSubtracao operadorSubtracao
  //é melhor reutilizar os token que já existem de +,-
  private boolean operacaoIncremento(Node root){ 
    Node operacaoIncrementoNode = root.addNode("operacaoIncremento");
    // Lookahead para ver se são dois operadores iguais
    if(!tokens.isEmpty() && token.lexema.equals(tokens.get(0).lexema)) {  
      // "++"
      if(token.lexema.equals("+")) {
        if(matchL("+", operacaoIncrementoNode) && matchL("+", operacaoIncrementoNode)) {
          return true;
        }
      }
      //"--"
      else if(token.lexema.equals("-")) {
        if(matchL("-", operacaoIncrementoNode) && matchL("-", operacaoIncrementoNode)) {
          return true;
        }
      }
    }
    erro("operacaoIncremento");
    contadorErro++;
    return false;
}

  //tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto 
  /*
   * simbulos        first
  * tipoVariavel    inteiro,decimal, texto, verdadeiroFalso
   */
  private boolean tipoVariavel(Node root){
    Node tipoVariavelNode = root.addNode("tipoVariavel");
    if(matchL("inteiro",tipoVariavelNode)||matchL("decimal",tipoVariavelNode)||matchL("texto",tipoVariavelNode)||
    matchL("verdadeiroFalso",tipoVariavelNode)){
      return true;
    }
    erro("tipoVariavel");
    contadorErro++;
    return false;
  }

  //match t de tokens para expressao regular 

  //usa matcht quando o lexema nao é sempre igual, ai valida pelo tipo
  /*
   * simbulos     first
   * decimal       DECIMAL
   */
  private boolean decimal(Node root){ 
    Node decimalNode = root.addNode("decimal");
    if(matchT("DECIMAL",token.lexema, decimalNode )){
      return true;
    }
    erro("decimal");
    contadorErro++;
    return false;
  }

   /*
   * simbulos     first
   * inteiro       INTEIRO
   */
  private boolean inteiro(Node root){ 
    Node inteiroNode = root.addNode("inteiro");
    if(matchT("INTEGER",token.lexema,inteiroNode)){
      return true;
    }
    erro("inteiro");
    contadorErro++;
    return false;
  }

    /*
   * simbulos            first
   * identificadores     IDENTIFIER
   */
  private boolean identificadores(Node root){ 
    Node identificadoresNode = root.addNode("identificadores");
    if(matchT("IDENTIFIER",token.lexema,identificadoresNode)){
      return true;
    }
    erro("identificadores");
    contadorErro++;
    return false;
  }
  
  /*
   * simbulos     first
   * texto        TEXT
   */
  private boolean texto(Node root){ 
    Node textoNode = root.addNode("texto");
    if(matchT("TEXT",token.lexema,textoNode)){
      return true;
    }
    erro("texto");
    contadorErro++;
    return false;
  }
  
  //boolean -> true|false
    /*
   * simbulos     first
   * boolean      true, false
   */
  private boolean isBoolean(Node root){
    Node isBooleanNode = root.addNode("isBoolean");
    if(matchL("true",isBooleanNode)||matchL("false",isBooleanNode)){
      return true;
    }
    erro("boolean");
    contadorErro++;
    return false;
  }

  /*
   * simbulos                      first
   * palavraReservadaNomeFuncao    FUNCTION_NAME
   */
  private boolean palavraReservadaNomeFuncao(Node root){
    Node palavraReservadaNomeFuncaoNode = root.addNode("palavraReservadaNomeFuncao");
    if(matchT("FUNCTION_NAME",palavraReservadaNomeFuncaoNode)){
      return true;
    }
    erro("palavraReservadaNomeFuncao");
    contadorErro++;
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

  private void inicializarFollows() {
    follows.put("listaComandos", Set.of("$"));
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

  private boolean follow(String regra) {
    if (token == null) return false; //evita null pointer exception
    
    Set<String> followSet = follows.get(regra);
    if (followSet == null) {
        // Regra sem FIRST definido - não tenta
        System.out.println("FOLLOW não definido para: " + regra);
        return false;
    }
    // Verifica se token atual está no FIRST
    boolean matches = followSet.contains(token.lexema) || followSet.contains(token.tipo);
    
    return matches;
  }

  //-------------------------------ARVORE DE DERIVACAO----------------------------------

  //sobrecargas
  private boolean matchT(String tipo, Node node){
    if(token.tipo.equals(tipo)){
      node.addNode(token.lexema);
      token = getNextToken();
      return true;
    }
    return false;
  }

   private boolean matchT(String tipo, String newcode, Node node){
    if(token.tipo.equals(tipo)){
      traduz(newcode);
      node.addNode(token.lexema);
      token = getNextToken();
      return true;
    }
    return false;
  }

    private boolean matchL(String tipo, Node node){
    if(token.lexema.equals(tipo)){
      node.addNode(token.lexema);
      token = getNextToken();
      return true;
    }
    return false;
  }

   private boolean matchL(String tipo, String newcode, Node node){
    if(token.lexema.equals(tipo)){
      traduz(newcode);
      node.addNode(token.lexema);
      token = getNextToken();
      return true;
    }
    return false;
  }


  //-------------------------------PARA TRADUÇÃO DO MINEIRES PARA JAVA------------------------

  //sobrecargas 
  private boolean matchL(String palavra, String newcode){
    if(token.lexema.equals(palavra)){
      traduz(newcode);
      token = getNextToken();
      return true;
    }
    return false;
  }

   private boolean matchT(String palavra, String newcode){
    if(token.tipo.equals(palavra)){
      traduz(newcode);
      token = getNextToken();
      return true;
    }
    return false;
  }

  private void traduz(String code){
    System.out.print(code);
  }

  public void header(){
    System.out.println("import java.util.Scanner;");
    System.out.println("public class Code{");
    System.out.println("public static void mai(String[]args){");
  }

  public void footer(){
    System.out.println("}");
    System.out.println("}");
  }

  //????????????????
  private void gerarCodigoJava(String lexema) {
    switch(lexema) {
        case "se": System.out.print("if "); break;
        case "entao": System.out.print(" "); break;
        case "senao": System.out.print("else "); break;
        case "->": System.out.print(" = "); break;
        case "e": System.out.print(" && "); break;
        case "ou": System.out.print(" || "); break;
        default: System.out.print(lexema + " "); break;
    }
}
  
}