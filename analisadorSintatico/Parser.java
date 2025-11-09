package analisadorSintatico;  

import analisadorLexico.Token;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Parser {
  List<Token> tokens;
  Token token;
  Map<String, Set<String>> firsts = new HashMap<>();
  Map<String, Set<String>> follows = new HashMap<>();
  //set: colecao de elementos unicos, so armazena chaves 
  Set<String> tokensSincronizacao = new HashSet<>();
  Map<String, String> tabelaInformacoesIdentificadores = new HashMap<>();
  /*armazeno nome dos identificadores e seus tipos para, na hora de o retorno de uma funcao ser identificador, eu verifico o tipo dele aqui 
  e traduzo para o tipo certo em java*/
  Tree tree; 
  int contadorErro;
  String tipoScannerAtual = ""; //armazena o valor de tipoDadoOpcionalAtribuicao para determinar o final do scannet.next na traducao
  String separadorArgumentosAtual = ""; 
  /*diferencia se a funcao é imprima:separador dos argumentos é traduzido pra + no java
  Entrada: traudzido para nada (só pega 1 valor por vez)
  qualquer outra funcao, que ai fica , o separador
  */ 

  //para traducao do ^:
  String base;
  String expoente;

  //para separar oq fica dentro e fora da main na hora da tradução
  private boolean dentroDaMain = true;
  private boolean processandoFuncao = false;
  private StringBuilder codigoFuncoes = new StringBuilder();
  private boolean identificadorChamadaDentroCriacao = false; //pq se eu chamar uma funcao dentro da criacao de outra essa chamada nao pode aparecer dentro da main mas sim dentro da fucnao

  //arquivo com codigo taduzido
  private PrintWriter arquivoSaida;
  private String nomeArquivoSaida = "CodigoTraduzido.java";

  public Parser(List<Token> tokens) {
    this.tree = new Tree(new Node("PROGRAM")); 
    this.tokens = tokens;
    inicializarTokensSincronizacao();
    inicializarFirsts();
    inicializarFollows();
    inicializarArquivoSaida();
  }

  /*
   método para pré-processar identificadores baseado no token anterior que utiliza a lista de tokens para popular a hash de identificadores com identificadores 
  que foram declarados (precedidos por tipos: inteiro, decimal, texto, verdadeiroFalso) para ja ter a hash populada conforme foi fazer analise sintatica
   */
  private void preProcessarIdentificadores() {
    for (int i = 0; i < tokens.size(); i++) {
      Token tokenAtual = tokens.get(i);
      // se é um IDENTIFIER
      if ("IDENTIFIER".equals(tokenAtual.tipo)) {
        String identificador = tokenAtual.lexema;
        // verifica se já existe na hash
        if (!tabelaInformacoesIdentificadores.containsKey(identificador)) {
          // procura pelo token anterior que seja um tipo
          if (i > 0) {
            Token tokenAnterior = tokens.get(i - 1);
            // verifica se o token anterior é um tipo válido
            if (firsts.get("declaracao").contains(tokenAnterior.lexema)) {
              String tipo = tokenAnterior.lexema;
              tabelaInformacoesIdentificadores.put(identificador, tipo);
              System.out.println("Adicionado: " + identificador + " -> " + tipo);
            }
          }
        }
      }
    }
  }

  /*
  -verificar apos tradução se: 
  A parte de expressões envolvendo os operadores matemáticos deve ser realizada de maneira correta, respeitando a precedência.
  -verificar se da para ler da tela com Entrada e imprimir no console com Imprima() 
   */
  public void main() {
    tabelaInformacoesIdentificadores.clear();
    preProcessarIdentificadores();
    token = getNextToken();
    headerClasse();
    boolean temCodigoGlobal = verificarSeTemCodigoGlobal(); //verifica se tem código global (fora de funções) antes de criar a main
    if (temCodigoGlobal) {
      headerMain(); // se tem código, gera a main
    }
    Node root = new Node("listaComando");
    tree.setRoot(root);
    
    if (listaComandos(root) && matchT("EOF",root) && contadorErro == 0) {
      // se tinha código global, fecha a main
      if (temCodigoGlobal) {
        arquivoSaida.println("}"); //fechar a main
      }
      // adiciona as funções fora da main
      if (codigoFuncoes.length() > 0) {
        arquivoSaida.println("\n");
        arquivoSaida.print(codigoFuncoes.toString());
      }
      footerClasse();
      System.out.println("Sintaticamente correto");
      //imprimirTabelaIdentificadores(); //debugar (tem todos os identificadores)
      //tree.preOrder();//imprime em pre ordem
      //tree.printCode(); //imprimeas folhas(codigo)
      //exibirTabelaSemantica();
      tree.printTree(); //imprimea arvore
    } else {
      erro("main");
      System.out.println("Sintaticamente Incorreto! Programa caiu em " + contadorErro + " regras de erro(s) sintático(s)");
    }
    fecharArquivoSaida();
    System.out.println("\ncódigo traduzido salvo em: " + nomeArquivoSaida);
}

  public Token getNextToken() {
    if (tokens.size() > 0) 
      return tokens.remove(0);
    return null;
  }

  private void erro(String regra) {
    System.out.println("-------------- Regra: " + regra);
    System.out.println("token inválido: " + token);
    System.out.println("Próximo token: " + tokens.get(0));
    System.out.println("------------------------------");
    contadorErro++;
    recuperacaoPanico();
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
  
  /* --------------------------------------REGRAS DA GRAMÁTICA------------------------------------------------*/

  //EPSULON:Palavra vazia é sair de uma regra sem casar token = return true (pode precisar de follow)
  //site que calcula first e follow: REFAZER FIST CONSIDERANDO EPSULON !!!!!!!!!!!!!!!

  // listaComandos -> comando listaComandos | ε
  /*
   * simbulos           first                                                 follow
   * listaComandos      comando (regra,ent first é firsts dessa regra), ε        $
   */
  private boolean listaComandos(Node root){
    Node listaComandoNode = root.addNode("listaComandos");
    if(first("comando") && comando(listaComandoNode) && listaComandos(listaComandoNode)){
      return true;
    }
    //follow
    if(follow("listaComandos")){
      return true; //ε
    }
    else{
      erro("listaComandos");
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
    return false;
  }
   
  //PROBLEMA PQ DECLARAR E DECLARAREATRIBUIR (opcao dentro e atribuicao) tem os mesmos firsts ent melhor solucao é juntar os 2 em um codigo só
  // Juntar declarar e declaraEAtribui em uma única regra

  // declaracao-> tipoVariavel identificadores (';' | '->'tipoDadoOpcionalAtribuicao valor ';')
  /*
   * simbulos     first
   * declaracao   so regra, ent first é firsts dessa regra: tipoVariavel
   *               inteiro,decimal, texto, verdadeiroFalso
   */
  private boolean declaracao(Node root){
    Node declaracaoNode = root.addNode("declaracao");
    if(first("declaracao") && tipoVariavel(declaracaoNode) && identificadores(declaracaoNode)) { //uso first declaracao pq é igual ao first de tipovariavel, ai n tenho que criar outra regra pros mesmos firsts
    // Pode ser: tipo var; OU tipo var -> valor;

      if (token.lexema.equals(";") || token.lexema.equals("; \n")) {
        registrarVariavel(declaracaoTipoAtual, declaracaoIdentAtual);
      }else if (token.lexema.equals("->") || token.lexema.equals(" = ")) {
        registrarVariavel(declaracaoTipoAtual, declaracaoIdentAtual);
      }
      
      if(matchL(";","; \n", declaracaoNode)) {
        
        return true; // declaracao simples
      }
      else if(matchL("->"," = ",declaracaoNode)&& valor(declaracaoNode) && matchL(";","; \n",declaracaoNode)) {
        //registrarVariavel(declaracaoTipoAtual, declaracaoIdentAtual);//registra variavel na tabela semantica
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
  private boolean seCompleto(Node root){
    Node seCompletoNode = root.addNode("seCompleto");
      //System.out.println("ENTROU NA REGRA SE COMPLETO: " + token.lexema);
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
    //System.out.println("ENTROU NA REGRA SE: " + token.lexema);
     if (matchL("se","if",seNode) && matchL("(","(",seNode) && condicao(seNode) && matchL(")",")",seNode) &&
      matchL("{","{",seNode) &&
      listaComandosInternos(seNode) && matchL("}","}\n",seNode)){
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
  private boolean ouSe(Node root){
    Node ouSeNode = root.addNode("ouSe");
    if(matchL("ouSe","else if",ouSeNode) && matchL("(","(",ouSeNode) && condicao(ouSeNode) && matchL(")",")",ouSeNode) &&
     matchL("{","{",ouSeNode) &&
     listaComandosInternos(ouSeNode) && matchL("}","}\n",ouSeNode)){
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
  private boolean senao(Node root){ 
    Node senaoNode = root.addNode("senao");
    if(matchL("senao","else",senaoNode) && matchL("{","{",senaoNode) &&
     listaComandosInternos(senaoNode) && matchL("}","}\n",senaoNode)){
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
  private boolean para(Node root){
    Node paraNode = root.addNode("para");
    if(matchL("para","for",paraNode) && matchL("(","(",paraNode) && cabecalhoPara(paraNode) &&
     matchL(")",")",paraNode) && matchL("{","{",paraNode) && listaComandosInternos(paraNode) && matchL("}","}\n",paraNode)){
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
  private boolean lacoEnquanto(Node root){ 
    Node lacoEnquantoNode = root.addNode("lacoEnquanto");
    if(matchL("lacoEnquanto","while",lacoEnquantoNode) && matchL("(","(",lacoEnquantoNode) &&
    condicao(lacoEnquantoNode) && matchL(")",")",lacoEnquantoNode) &&
     matchL("{","{",lacoEnquantoNode) && listaComandosInternos(lacoEnquantoNode) && matchL("}","}\n",lacoEnquantoNode)){
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
  private boolean criarFuncao(Node root){
    Node criarFuncaoNode = root.addNode("criarFuncao");
    identificadorChamadaDentroCriacao = true;
    //marcadores pra determinar oq fica dentro e fora da main na traducao
    processandoFuncao = true;
    dentroDaMain = false; // sai da main
    // processa apenas a declaração da função (sem corpo) pq nesse momento ainda nao sabe o tipo de retorno pq esta comecando a ler a criacao da funcao agora
    if(matchL("criar","public static ",criarFuncaoNode)){
      String tipoRetorno = acharTokenDeRetornoFuncao(tokens);//recebe toda a lista de tokens para processar e retornr o tipo de retorno para a traducao para o java 
      traduz(tipoRetorno); 
      tipoRetorno = "";
      if(palavraReservadaNomeFuncao(criarFuncaoNode) && matchL("(","(",criarFuncaoNode) && argumentosFuncao(criarFuncaoNode) &&
      matchL(")",")",criarFuncaoNode)){
        if(matchL("{","{ \n",criarFuncaoNode) && listaComandosInternos(criarFuncaoNode) &&
        matchL("}","} \n",criarFuncaoNode)){
          //restaura após processar a função
          identificadorChamadaDentroCriacao = false;
          processandoFuncao = false;
          dentroDaMain = true;
          return true;
        }
      }
    }
    // Em caso de erro, restaura contexto
    processandoFuncao = false;
    dentroDaMain = true;
    erro("criarFuncao");
    return false;
  }

  //chamarFuncao -> inicioChamarFuncao '('argumentosChamada')' ';'
  /*
   * simbulos            first
   * chamarFuncao          "entrada","imprima", first é firsts dessa regra palavraReservadaNomeFuncao
   */
  private boolean chamarFuncao(Node root) { //sempre tem que chamar funcao dentro da main 
    Node chamarFuncaoNode = root.addNode("chamarFuncao");
    // caso contrário → fica dentro da função atual
    boolean deveIrParaMain = !processandoFuncao && !identificadorChamadaDentroCriacao;
    if (deveIrParaMain) {
      dentroDaMain = true;
    } else {
      dentroDaMain = false;
    }
    if (first("chamarFuncao") && inicioChamarFuncao(chamarFuncaoNode) && matchL("(","(",chamarFuncaoNode) && argumentosChamada(chamarFuncaoNode) &&
     matchL(")",")",chamarFuncaoNode) && matchL(";","; \n",chamarFuncaoNode)) {
      return true;
    }
    erro("chamarFuncao");
    return false;
  }

  //chamarFuncaoSemFim -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')' 
  //usada quando desejamos atribui o resultado de uma funcao a uma declaracao ou chamar o resultado de uma funcao em outra funcao....
  private boolean chamarFuncaoSemFim(Node root) {
    Node chamarFuncaoSemFimNode = root.addNode("chamarFuncaoSemFim");
    // caso contrário → fica dentro da função atual
    boolean deveIrParaMain = !processandoFuncao && !identificadorChamadaDentroCriacao;
    if (deveIrParaMain) {
      dentroDaMain = true;
    } else {
      dentroDaMain = false;
    }
    if (first("chamarFuncao") && inicioChamarFuncao(chamarFuncaoSemFimNode) && matchL("(","(",chamarFuncaoSemFimNode) &&
     argumentosChamada(chamarFuncaoSemFimNode) && matchL(")",")",chamarFuncaoSemFimNode)) {
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
  private boolean inicioChamarFuncao(Node root){
     // SALVA O CONTEXTO também aqui para "Imprima" e "Entrada" (traduzir correto para java)
    if(token != null) {
      separadorArgumentosAtual = ajusteTraducaoArgumentosChamada(token);
    }
    Node inicioChamarFuncaoNode = root.addNode("inicioChamarFuncao");
    if((first("palavraReservadaNomeFuncao") && palavraReservadaNomeFuncao(inicioChamarFuncaoNode))||
    matchL("Entrada","scanner.next"+tipoScannerAtual,inicioChamarFuncaoNode)||
    matchL("Imprima","System.out.print",inicioChamarFuncaoNode)){
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
    if(matchL(",",separadorArgumentosAtual,restoArgumentosChamadaNode) && valor(restoArgumentosChamadaNode) &&
    restoArgumentosChamada(restoArgumentosChamadaNode)){
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
    return false;
  }

  //emComumParametro -> ε | ‘,’ parametroFuncao emComumParametro
   /*
   * simbulos            first
   * emComumParametro    ε , ‘,’ 
   */
  private boolean emComumParametro(Node root){
    Node emComumParametroNode = root.addNode("emComumParametro");
    if(matchL(",",",",emComumParametroNode) && parametroFuncao(emComumParametroNode) && emComumParametro(emComumParametroNode)){
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
    return false;
  }

  //condicao -> identificadores condicao’ | negacaoCondicao condicao’ | expressoesMatematicas condicao’| condicaoComparacoesBasicas condicao’
   /*
   * simbulos     first
   * condicao  first é first dessas regras: identfificadores, negacaocondicao, expressoesMatematicas, condicaoComparacoesBasicas
   */

  //implementação do lookahead para pegar o prox token e distinguir se é identificadores, expressoesMatematicas ou condicoesComparacoesBasicas
  
  String tipoPrimeiraVariavel;
  String nomePrimeiraVariavel;
  private boolean condicao(Node root) {
    Node condicaoNode = root.addNode("condicao");
    
    //System.out.println(token.lexema + " " + token.tipo);
    // --------------------------- MARCAÇÃO DO TOKEN ----------------------------
    if (token.tipo.equals("INTEGER")) {
      tipoPrimeiraVariavel = "inteiro";
    }else if (token.tipo.equals("DECIMAL")){
      tipoPrimeiraVariavel = "decimal";
    }else if(token.tipo.equals("TEXT")){
      tipoPrimeiraVariavel = "texto";
    }else if(token.tipo.equals("PALAVRA_RESERVADA")){
      tipoPrimeiraVariavel = "verdadeiroFalso";
    } else if(token.tipo.equals("IDENTIFIER")){
      nomePrimeiraVariavel = token.lexema;
      System.out.println(token.lexema);
      for(String[] tokens : tabelaSemantica){
        if(nomePrimeiraVariavel.equals(tokens[1])){
          tipoPrimeiraVariavel = tokens[0];
        }
      }
    }
    // --------------------------- MARCAÇÃO DO TOKEN ----------------------------
    //System.out.println(tipoPrimeiraVariavel);
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
      
        for (int i = 0; i < Math.min(3, tokens.size()); i++) {//min pra ter operacao matematica
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
    return false;
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
   
    if((first("condicaoComparacoesBasicas") && comparacoesBasicas(condicaoComparacoesBasicasNode))|| (matchL("!","!",condicaoComparacoesBasicasNode) &&
     identificadores(condicaoComparacoesBasicasNode))){
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
  private boolean comparacoesBasicas(Node root){
    Node comparacoesBasicasNode = root.addNode("comparacoesBasicas");
    if(((first("identificadores") && identificadores(comparacoesBasicasNode))||(first("numero") && numero(comparacoesBasicasNode))) &&
     operacao(comparacoesBasicasNode) && valoresOperacao(comparacoesBasicasNode)){
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
  String tipoSegundaVariavel;
  String nomeSegundaVariavel;

  private boolean valoresOperacao(Node root){ //funcoes atomicas(verificam diretamente tokens), n preciso colocar first
    Node valoresOperacaoNode = root.addNode("valoresOperacao");

    //System.out.println(token.lexema + " " + token.tipo);
    // --------------------------- MARCAÇÃO DO TOKEN ----------------------------
    if (token.tipo.equals("INTEGER")) {
      tipoSegundaVariavel = "inteiro";
    }else if (token.tipo.equals("DECIMAL")){
      tipoSegundaVariavel = "decimal";
    }else if(token.tipo.equals("TEXT")){
      tipoSegundaVariavel = "texto";
    }else if(token.tipo.equals("PALAVRA_RESERVADA")){
      tipoSegundaVariavel = "verdadeiroFalso";
    } else if(token.tipo.equals("IDENTIFIER")){
      nomeSegundaVariavel = token.lexema;
      //System.out.println(token.lexema);
      for(String[] tokens : tabelaSemantica){
        if(nomeSegundaVariavel.equals(tokens[1])){
          tipoSegundaVariavel = tokens[0];
        }
      }
    }
    // --------------------------- MARCAÇÃO DO TOKEN ----------------------------
    System.out.println(tipoPrimeiraVariavel);
    System.out.println(tipoSegundaVariavel);
    if(first("identificadores") && identificadores(valoresOperacaoNode)|| first("numero") && numero(valoresOperacaoNode)|| first("isBoolean") &&
     isBoolean(valoresOperacaoNode)){
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
  private boolean negacaoCondicao(Node root){
    Node negacaoCondicaoNode = root.addNode("negacaoCondicao");
    if(matchL("!","!",negacaoCondicaoNode) && condicao(negacaoCondicaoNode)){
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
  private boolean operacao(Node root){
    Node operacaoNode = root.addNode("operacao");
    if(first("operacaoRelacional") && operacaoRelacional(operacaoNode) || first("operacaoLogica") && operacaoLogica(operacaoNode)){
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
  private boolean operacaoRelacional(Node root){
    Node operacaoRelacionalNode = root.addNode("operacaoRelacional");
    if(matchL("<","<",operacaoRelacionalNode)|| matchL(">",">",operacaoRelacionalNode) || matchL("<>","!=",operacaoRelacionalNode) ||
     matchL("<->","==",operacaoRelacionalNode)|| matchL("<=","<=",operacaoRelacionalNode)|| matchL(">=",">=",operacaoRelacionalNode)){
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
  private boolean operacaoLogica(Node root){
    Node operacaoLogicaNode = root.addNode("operacaoLogica");
    if(matchL("e","&&",operacaoLogicaNode)||matchL("ou","||",operacaoLogicaNode)||matchL("!","!",operacaoLogicaNode)){
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
    if(first("declaracao") && inicializacao(cabecalhoParaNode) && matchL(";",";",cabecalhoParaNode) && condicao(cabecalhoParaNode) && 
    matchL(";",";",cabecalhoParaNode) && incremento(cabecalhoParaNode)){
      return true;
    }
    erro("listaComandosInternos");
    return false;
  }

  //atribui -> identificadores operadorAtribuicao tipoDadoOpcionalAtribuicao valor ';'
   /*
   * simbulos          first
   * atribui           first é first dessa regra: identificadores
   *                   
   */
  private boolean atribui(Node root){ 
    Node atribuiNode = root.addNode("atribui");
    
    if(first("identificadores") && identificadores(atribuiNode) && matchL("->"," = ",atribuiNode) && valor(atribuiNode) && matchL(";","; \n",atribuiNode)){
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
  private boolean expressoesMatematicas(Node root){
    Node expressoesMatematicasNode = root.addNode("expressoesMatematicas");
    if(first("precedenciaSuperior") && precedenciaInferior(expressoesMatematicasNode)){
  //    System.out.println("dei match em expressoesMatematicas");
      return true;
    }
    erro("expressoesMatematicas");
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

    // ---------------------------------- Analise Semantica ------------------------------------------------
    // marcação para ver se a variavel existe na tabela para atribuição
    boolean encontrado = false;
    // percorre a lista de declarações
    for(String[] variaveis : tabelaSemantica) {
      // verifica se o identificador atual existe na tabela
      if (declaracaoIdentAtual.equals(variaveis[1])) {
        encontrado = true;
        // -------------------------------- Tipagem de variavel --------------------------------------------------
        // Verifica o tipo da variavel para atribuição (Tipagem das variaveis)
        if (declaracaoIdentAtual.equals(variaveis[1])) {
          if(token.tipo.equals("INTEGER") && variaveis[0].equals("inteiro") 
            ||(token.tipo.equals("DECIMAL") && variaveis[0].equals("decimal")|| token.tipo.equals("INTEGER") && variaveis[0].equals("decimal"))
            ||token.tipo.equals("TEXT") && variaveis[0].equals("texto")
            ||token.tipo.equals("PALAVRA_RESERVADA") && variaveis[0].equals("verdadeiroFalso")) {
              atribuicaoSomaIdentificador = declaracaoIdentAtual;
              System.out.println(declaracaoIdentAtual + " Passou na atribuicao semantica Tipo -> " + token.tipo);
            //System.out.println(variaveis[0] + " " + token.tipo + " " + declaracaoTipoAtual + " " + variaveis[1]);
          } 
          // caso os tipos estejam diferentes
          else{
              //System.out.println("Tipos diferentes: " + "id -> " + declaracaoIdentAtual + "; tipo -> " + variaveis[0] + "; tipo atribuido -> " + token.tipo);
              erro("Tipos diferentes: " + "id -> " + declaracaoIdentAtual + "; tipo -> " + variaveis[0] + "; tipo atribuido -> " + token.tipo);
              //System.out.println(declaracaoIdentAtual + " " + declaracaoTipoAtual + " " + token.tipo + " " + variaveis[0] + " " + variaveis[1]  + " " +"Errado");
          }
          // -------------------------------- Tipagem de variavel --------------------------------------------------
          // para o for quando encontrar a variavel
          break;
        }
      }
    }
    // -------------------------------- Escopo e existencia de variavel --------------------------------------------------
    // verifica se o identificador existe na tabela de variaveis (Escopo ou não declarado)
    if (!encontrado) {
      erro(declaracaoIdentAtual + " não esta na tabela");
      //System.out.println(declaracaoIdentAtual + " não esta na tabela");
    }
    // -------------------------------- Tipagem de variavel --------------------------------------------------
    // ---------------------------------- Analise Semantica ------------------------------------------------
    
    if(tokens.get(0).lexema.equals(";") || token.tipo.equals("TEXT") || tokens.get(0).lexema.equals(")") ||  tokens.get(0).lexema.equals(",") ){//se proximo token da lista for ; é declaracao simples e pode ser numero/texto/isboolean/identificadores
      
      if((first("numero") && numero(valorNode))||(first("texto") && texto(valorNode))||(first("isBoolean") && isBoolean(valorNode))||
      (first("identificadores") && identificadores(valorNode))){
        
        return true;
      }
    }
    else if (firsts.get("operacao").contains(token.lexema) || firsts.get("operacao").contains(tokens.get(0).lexema)){//se token atual for algum dos tokens do first operacao ou prox token for algum dos tokens do first de operacao
      return condicaoComparacoesBasicas(valorNode);
    }
    else if(token.tipo.equals("FUNCTION_NAME")||token.lexema.equals("Entrada")|| token.lexema.equals("Imprima")){
      return chamarFuncaoSemFim(valorNode);
    }
    else if(firsts.get("validaExpressao").contains(token.lexema) || firsts.get("validaExpressao").contains(tokens.get(0).lexema)){//se token atual for algum dos tokens do first precedenciasuperior (representa expressoesmatematicas) ou prox token for algum dos tokens do first de precedenciasuperior
      return expressoesMatematicas(valorNode);
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
  private boolean numero(Node root){ 
    Node numeroNode = root.addNode("numero");
    
    if(first("decimal") && decimal(numeroNode)||first("inteiro") && inteiro(numeroNode)){
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
  private boolean comandoInterno(Node root){
    Node comandoInternoNode = root.addNode("comandoInterno");
    if((first("se")&& seCompleto(comandoInternoNode))||(first("para") && para(comandoInternoNode))||
    (first("lacoEnquanto") && lacoEnquanto(comandoInternoNode))||(first("declaracao") && declaracao(comandoInternoNode))||
    (first("atribui") && atribui(comandoInternoNode))||(first("chamarFuncao") && chamarFuncao(comandoInternoNode))||
    (first("retornar") && retornar(comandoInternoNode))){
      return true;
    }
    erro("comandoInterno"); 
    return false;
  }
  
  //retornar -> 'retorna' conteudos';'
    /*
   * simbulos     first
   * retornar    retorna
   */
  private boolean retornar(Node root){ 
    Node retornarNode = root.addNode("retornar");
    if(matchL("retorna","return ",retornarNode) && conteudos(retornarNode) && matchL(";","; \n",retornarNode)){
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
  private boolean conteudos(Node root){ 
    //se comecar com ( o (token.lexema) ou tokens.get(0) tiver validaexpressao ´expressao matrematica
    Node conteudosNode = root.addNode("conteudos");
    if(firsts.get("validaExpressao").contains(token.lexema) || firsts.get("validaExpressao").contains(tokens.get(0).lexema)){
      return expressoesMatematicas(conteudosNode);
    }
    if((first("identificadores") && identificadores(conteudosNode))||(first("numero") && numero(conteudosNode))
    ||first("isBoolean") && isBoolean(conteudosNode)){
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
  private boolean inicializacao(Node root){ 
    Node inicializacaoNode = root.addNode("inicializacao");
    if(first("declaracao") && tipoVariavel(inicializacaoNode) && identificadores(inicializacaoNode) && matchL("->"," = ",inicializacaoNode) &&
    conteudos(inicializacaoNode)){
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
  private boolean precedenciaInferior(Node root){
    Node precedenciaInferiorNode = root.addNode("precedenciaInferior");
    if(first("precedenciaSuperior") && precedenciaIntermediaria(precedenciaInferiorNode) && precedenciaInferiorDerivada(precedenciaInferiorNode)){
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
  private boolean precedenciaIntermediaria(Node root){
    Node precedenciaIntermediariaNode = root.addNode("precedenciaIntermediaria");
    if(first("precedenciaSuperior") && precedenciaAlta(precedenciaIntermediariaNode) && precedenciaIntermediariaDerivada(precedenciaIntermediariaNode)){
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
  private boolean precedenciaInferiorDerivada(Node root){
    Node precedenciaInferiorDerivadaNode = root.addNode("precedenciaInferiorDerivada");
    if(matchL("+","+",precedenciaInferiorDerivadaNode) && precedenciaIntermediaria(precedenciaInferiorDerivadaNode) &&
     precedenciaInferiorDerivada(precedenciaInferiorDerivadaNode)){
      return true;
    }
    //ou
    if(matchL("-","-",precedenciaInferiorDerivadaNode) && precedenciaIntermediaria(precedenciaInferiorDerivadaNode) && 
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
    base = token.lexema;
    if(tokens.get(0).lexema.equals("^")){
      expoente = tokens.get(1).lexema;
    }
    Node precedenciaAltaNode = root.addNode("precedenciaAlta");
    if(first("precedenciaSuperior") && precedenciaSuperior(precedenciaAltaNode) && precedenciaAltaDerivada(precedenciaAltaNode)){
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
  private boolean precedenciaIntermediariaDerivada(Node root){
    Node precedenciaIntermediariaDerivadaNode = root.addNode("precedenciaIntermediariaDerivada");
    if(matchL("*","*",precedenciaIntermediariaDerivadaNode) && precedenciaAlta(precedenciaIntermediariaDerivadaNode) &&
     precedenciaIntermediariaDerivada(precedenciaIntermediariaDerivadaNode)){
      return true;
    }
    //ou
    if(matchL("/","/",precedenciaIntermediariaDerivadaNode) && precedenciaAlta(precedenciaIntermediariaDerivadaNode) &&
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
  // nome da variavel armazenada
  String atribuicaoSoma;
  // nome da variavel que realizara a soma
  String atribuicaoSomaIdentificador;
  // tipo da variavel que esta armazenada
  String atribuicaoSomaIdentificadorTipo;

  private boolean precedenciaSuperior(Node root){
    Node precedenciaSuperiorNode = root.addNode("precedenciaSuperior");

    // --------------------------------------- Analisador Semantico para expressões matematicas ----------------------------------------- 
    boolean encontrado = false;
    // verifica se o token é um identificador
    if(token.tipo.equals("IDENTIFIER")){
      // atribui o nome do identificador atual
      atribuicaoSoma = token.lexema;
      // percorrer a tabela semantica
      for(String[] variaveis : tabelaSemantica) {
        // procura o identificador na tabela semantica
        if (atribuicaoSoma.equals(variaveis[1])) {
          String variacel = variaveis[1];
          // atribui o tipo do identificador encontrado
          atribuicaoSomaIdentificadorTipo = variaveis[0];
          encontrado = true;
          //System.out.println("VARIAVEL ENCONTRADA : " + variacel);

          if (variaveis[0].equals("texto")) {
            erro("Tipos incompatíveis para operação entre " + atribuicaoSomaIdentificador + " e " + atribuicaoSoma);
          }
        } 
      }
        if (!encontrado) {
            erro(atribuicaoSoma + " não foi declarada");
            //System.out.println(declaracaoIdentAtual + " não esta na tabela");
          }
      }
      // --------------------------------------- Analisador Semantico para expressões matematicas -----------------------------------------
    

    if((first("identificadores") && identificadores(precedenciaSuperiorNode))||(first("numero") && numero(precedenciaSuperiorNode))||
     ((matchL("(","(",precedenciaSuperiorNode) && expressoesMatematicas(precedenciaSuperiorNode) && matchL(")",")",precedenciaSuperiorNode)))){
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
  private boolean precedenciaAltaDerivada(Node root){
    Node precedenciaAltaDerivadaNode = root.addNode("precedenciaAltaDerivada");
    if(matchL("^","Math.pow(",precedenciaAltaDerivadaNode)){
      traduz(base);
      traduz(",");
      if(precedenciaSuperior(precedenciaAltaDerivadaNode) && precedenciaAltaDerivada(precedenciaAltaDerivadaNode)){
        traduz(")");
        return true;
      }
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
        if(matchL("+","+" ,operacaoIncrementoNode) && matchL("+","+",operacaoIncrementoNode)) {
          return true;
        }
      }
      //"--"
      else if(token.lexema.equals("-")) {
        if(matchL("-", "-", operacaoIncrementoNode) && matchL("-",  "-", operacaoIncrementoNode)) {
          return true;
        }
      }
    }
    erro("operacaoIncremento");
    return false;
}

  //tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto 
  /*
   * simbulos        first
  * tipoVariavel    inteiro,decimal, texto, verdadeiroFalso
   */
  private boolean tipoVariavel(Node root){
    tipoScannerAtual = getTipoScanner(token.lexema);
    Node tipoVariavelNode = root.addNode("tipoVariavel");
    declaracaoTipoAtual = token.lexema;
    if(matchL("inteiro","int ",tipoVariavelNode)||matchL("decimal","double ",tipoVariavelNode)||matchL("texto","String ",tipoVariavelNode)||
    matchL("verdadeiroFalso","boolean ",tipoVariavelNode)){ 
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
  private boolean decimal(Node root){ 

    
    Node decimalNode = root.addNode("decimal");
    //verifica se prox token é ^ - ^se for nao traduz aqui (para token apenas aparecer dentro o math.pow)
    //declaracaoValorAtual = "decimal";
    boolean proximoEhPotencia = !tokens.isEmpty() && tokens.get(0).lexema.equals("^");
   

    if(matchT("DECIMAL",proximoEhPotencia ? "" : token.lexema, decimalNode )){
      return true;
    }
    erro("decimal");
    return false;
  }

   /*
   * simbulos     first
   * inteiro       INTEIRO
   */
  private boolean inteiro(Node root){ 

    
    Node inteiroNode = root.addNode("inteiro");
    //verifica se prox token é ^ - ^se for nao traduz aqui (para token apenas aparecer dentro o math.pow)
    //declaracaoValorAtual = "inteiro";
    boolean proximoEhPotencia = !tokens.isEmpty() && tokens.get(0).lexema.equals("^");
     

    if(matchT("INTEGER", proximoEhPotencia ? "" : token.lexema,inteiroNode)){
      return true;
    }
    erro("inteiro");
    return false;
  }

    /*
   * simbulos            first
   * identificadores     IDENTIFIER
   */

  private boolean identificadores(Node root){ 
    Node identificadoresNode = root.addNode("identificadores");
    //verifica se prox token é ^ - ^se for nao traduz aqui (para token apenas aparecer dentro o math.pow)
    boolean proximoEhPotencia = !tokens.isEmpty() && tokens.get(0).lexema.equals("^");
    declaracaoIdentAtual = token.lexema;
    if(matchT("IDENTIFIER", proximoEhPotencia ? "" : token.lexema, identificadoresNode)){
        return true;
    }
    erro("identificadores");
    return false;
  }

  /*
   * simbulos     first
   * texto        TEXT
   */
  private boolean texto(Node root){ 

    

    Node textoNode = root.addNode("texto");
    //declaracaoValorAtual = "texto";

    if(matchT("TEXT","\""+token.lexema+"\"",textoNode)){
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
  private boolean isBoolean(Node root){

    
    Node isBooleanNode = root.addNode("isBoolean");
    //declaracaoValorAtual = "isBoolean";

    if(matchL("true","true",isBooleanNode)||matchL("false","false",isBooleanNode)){
      return true;
    }
    erro("boolean");
    return false;
  } 
  /*
   * simbulos                      first
   * palavraReservadaNomeFuncao    FUNCTION_NAME
   */
  private boolean palavraReservadaNomeFuncao(Node root){
    separadorArgumentosAtual = ajusteTraducaoArgumentosChamada(token);
    Node palavraReservadaNomeFuncaoNode = root.addNode("palavraReservadaNomeFuncao");
    if(matchT("FUNCTION_NAME",token.lexema,palavraReservadaNomeFuncaoNode)){
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
    firsts.put("expressoesMatematicas", Set.of("(","IDENTIFIER", "DECIMAL","INTEIRO"));
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

  //sobrecargas para parser normal + arvore
  private boolean matchT(String tipo, Node node){
    if(token.tipo.equals(tipo)){
      node.addNode(token.lexema);
      token = getNextToken();
      return true;
    }
    return false;
  }

  private boolean matchL(String tipo, Node node){
    if(token.lexema.equals(tipo)){
      node.addNode(token.lexema);

      if(tipo.equals('{')){
        nivelEscopo++;
      }
      else if (tipo.equals('}')) {
        nivelEscopo--;
      }

      token = getNextToken();
      return true;
    }
    return false;
  }

  //----------------METODOS PARA TRADUÇÃO DO MINEIRES PARA JAVA------------------------

  private boolean matchL(String tipo, String newcode, Node node){
    if(token.lexema.equals(tipo)){
      traduz(newcode);
      node.addNode(token.lexema);

      if (token.lexema.equals("{")) {
        nivelEscopo++;
      }
      else if (token.lexema.equals("}")) {
        removerVariaveisDoNivel(nivelEscopo);
        nivelEscopo--;
      }

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

  private void traduz(String code) {
    if (processandoFuncao && !dentroDaMain) {
      // Se está processando função, o código vai para o StringWriter temporário
      codigoFuncoes.append(code);
    } 
    else {
      // Se está na main, vai direto para o arquivo
      arquivoSaida.print(code);
      arquivoSaida.flush();// Garante que seja escrito imediatamente (limpa buffer)
    }
  }
  
  private void headerClasse() {
    arquivoSaida.println("import java.util.Scanner;");
    arquivoSaida.println("public class CodigoTraduzido {");
    arquivoSaida.println("static Scanner scanner = new Scanner(System.in);");
    arquivoSaida.flush();
  }

  private void headerMain() {
    arquivoSaida.println("public static void main(String[] args) {");
    arquivoSaida.flush();
  }

  private void footerClasse() {
    arquivoSaida.println("}"); 
    arquivoSaida.flush();
  }

  private boolean verificarSeTemCodigoGlobal() {
    // Faz uma análise preliminar para ver se tem comandos que devem estar na main (comandos que não são declarações de função)
    for (Token t : tokens) {
      if (!"criar".equals(t.lexema) && !"FUNCTION_NAME".equals(t.tipo)) {
        return true; //tTem código que deve ir na main
      }
    }
    return false; // apenas funções, não precisa de main
  }

  private String getTipoScanner(String tipo) { //retorna o valor necessario para traduzir Entrada para o tipo especifico de scanner
    switch(token.lexema) {
      case "inteiro": return "Int";
      case "decimal": return "Double";
      case "texto": return "Line";
      case "verdadeiroFalso": return "Boolean";
      default: return "";
    }
  }

  private String ajusteTraducaoArgumentosChamada(Token token) {
    if (token != null) {
        if ("Imprima".equals(token.lexema)) {
            return " + ";
        } else if ("Entrada".equals(token.lexema)) {
            return "";
        } else if ("FUNCTION_NAME".equals(token.tipo)) {
            return ", ";
        }
    }
    return ", "; // Padrão
  }

  /*metodo que recebe toda a lista de tokens e percorre ela para achar o retorna e pegar o token seguinte para na
   funcao ajusteTraducaoTipoRetornoFuncaoComTokendeterminar o tipo de retorno correto para a conversao para o java */
  private String acharTokenDeRetornoFuncao(List<Token> tokens) {
    // percorre todos os tokens procurando por "retorna"
    for (int i = 0; i < tokens.size(); i++) {
      Token tokenAtual = tokens.get(i); 
      //encontrou o token "retorna"
      if ("retorna".equals(tokenAtual.lexema)) {
        // verifica token após "retorna"
        if (i + 1 < tokens.size()) {
          Token tokenAposRetorna = tokens.get(i + 1);
          //valida se é expressao amtematica sem () e se for retorna sempre double
          Token tokenValidaExpMat = tokens.get(i + 2);
          if(firsts.get("validaExpressao").contains(tokenValidaExpMat.lexema)){
            return "double ";
          }
          else{
            // System.out.println("Token após 'retorna': " + tokenAposRetorna); //debug
            return ajusteTraducaoTipoRetornoFuncaoComToken(tokenAposRetorna);
          }
        }
      }
    }
    return "void ";
  }

  // determina o tipo baseado no token após "retorna"
  private String ajusteTraducaoTipoRetornoFuncaoComToken(Token token){
    if (token != null) {
      System.out.println("TOKEN AJUSTE"+token);
      if ("IDENTIFIER".equals(token.tipo)) {
        //le a hash procurando o token.lexema(identififcador) e retorna o tipo equivalente ao identificador para ser o tipo de retorno
        String tipoIdentificador = tabelaInformacoesIdentificadores.get(token.lexema);
        if (tipoIdentificador != null) {
          switch(tipoIdentificador) {
            case "inteiro":
              return "int ";
            case "decimal":
              return "double ";
            case "texto":
              return "String ";
            case "verdadeiroFalso":
              return "boolean ";
          }
        }
      }
      else if ("INTEGER".equals(token.tipo)) {
        return "int ";
      } 
      else if ("DECIMAL".equals(token.tipo) || "ABR_PAR".equals(token.tipo)) {
        return "double ";
      }
      else if ("true".equals(token.lexema) || "false".equals(token.lexema)) {
        return "boolean ";
      }
    }
    return "void "; // Padrão
  } 
  
  
  //metodo para debugar hash de identificadores e seus tipos
  private void imprimirTabelaIdentificadores() {
    System.out.println("----- TABELA DE IDENTIFICADORES----");
    System.out.println("IDENTIFICADOR -> TIPO");
    
    if (tabelaInformacoesIdentificadores.isEmpty()) {
      System.out.println("Tabela vazia - nenhum identificador declarado");
    }
    else {
      for (Map.Entry<String, String> entry : tabelaInformacoesIdentificadores.entrySet()) {
        System.out.println(entry.getKey() + " -> " + entry.getValue());
      }
    }
  }


  //metodos para arquivo onde ficará o cóigo traduzido para java 
      
  private void inicializarArquivoSaida() {
    try {
      arquivoSaida = new PrintWriter(new FileWriter(nomeArquivoSaida));
    } catch (IOException e) {
      System.err.println("Erro ao criar arquivo de saída: " + e.getMessage());
      // Fallback para System.out se não conseguir criar o arquivo
      arquivoSaida = new PrintWriter(System.out);
    }
  }
  // Método para fechar o arquivo
  private void fecharArquivoSaida() {
    if (arquivoSaida != null) {
      arquivoSaida.close();
    }
  }

//----------------------------RECUPERACAO POR PANICO------------------------------------

private void inicializarTokensSincronizacao(){
  tokensSincronizacao.add(";");
  tokensSincronizacao.add("{");
  tokensSincronizacao.add("}");
  tokensSincronizacao.add("(");
  tokensSincronizacao.add(")");
  tokensSincronizacao.add("se");
  tokensSincronizacao.add("ouSe");
  tokensSincronizacao.add("senao");
  tokensSincronizacao.add("para");
  tokensSincronizacao.add("inteiro");
  tokensSincronizacao.add("decimal");
  tokensSincronizacao.add("texto");
  tokensSincronizacao.add("verdadeiroFalso");
  tokensSincronizacao.add("Imprima");
  tokensSincronizacao.add("Entrada");
  tokensSincronizacao.add("lacoEnquanto");
  tokensSincronizacao.add("criar");
  tokensSincronizacao.add("retorna");
}

  private void recuperacaoPanico(){
    System.out.println("INICIANDO RECUPERAÇÃO POR PÂNICO...");
    System.out.println("Token atual no início da recuperação: " + token);
    //para melhor debug:
    int tokensDescartados = 0;
    List<String> tokensDescartadosList = new ArrayList<>();
    //descarta tokens ate achar algum de sincronização
    while(token != null && !token.tipo.equals("EOF") && !tokensSincronizacao.contains(token.lexema)){
      System.out.println("descartando token: " + token.lexema);
      tokensDescartadosList.add(token.lexema + "[" + token.tipo + "]");
      token = getNextToken();
      tokensDescartados++;
    }
    
    //achou token de sincronização
    if (token != null && !token.tipo.equals("EOF")) {
      System.out.println("token de sincronização: " + token.lexema);
      token = getNextToken(); 
      System.out.println("quantidade de tokens descartados: " + tokensDescartados);
      if (!tokensDescartadosList.isEmpty()) {
        System.out.println("lista de tokens descartados: " + String.join(" ->", tokensDescartadosList));
      }
    }
  }
  //como compoilar um arquivo dentro do java (compilar automaticamente o CodigoTraduzido.java)

  private List<String[]> tabelaSemantica = new ArrayList<>(); // tabela para armazenar as declarações
  private int nivelEscopo = 0; // nivel da variavel para identificar o scopo dela

  private String declaracaoTipoAtual; // armazena a declaracao atual para evitar duplicidade
  private String declaracaoIdentAtual;
  // private String //declaracaoValorAtual;
  // private int existeDeclaracaoIgual = 0;
  // private int tiposCompatíveis = 0;
  //private String declaracaoIdentComparacaoTipo;

  // //Verifica se a variavel existe na tabela semantica
  // private void verificarVariavelDeclarada(String tipo, String identificador){

  //   for(String[] declaracoes : tabelaSemantica){
  //     if(declaracoes[1].equals(identificador) && nivelEscopo == Integer.parseInt(declaracoes[2])){
  //       //System.out.println("Erro Semântico: Variável '" + identificador + "' já declarada");
  //       existeDeclaracaoIgual = 1;
  //     }
  //   }
  // }

  // // verifica se os tipos são compatíveis
  // private void verificarTipagemVariavel(String tipo, String identificador){
  //   if(//declaracaoValorAtual.equals(declaracaoTipoAtual)){
  //     //System.out.println("Erro Semântico: Tipo incompatível na atribuição para a variável '" + identificador + "'");
  //     tiposCompatíveis = 1;
  //   }
  // }

  // registra a variavel na tabela semantica
  private void registrarVariavel(String tipo, String identificador) {
    
    System.out.println("Registrando variável: " + "id: " + identificador + ", tipo: " + tipo + ", nível de escopo: " + nivelEscopo); // + " tipo do valor atribuido: " + //declaracaoValorAtual
    // armazena na array a declaracão da variavel
    tabelaSemantica.add(new String[]{tipo, identificador, String.valueOf(nivelEscopo)});
    exibirTabelaSemantica();
    
  }

  private void removerVariaveisDoNivel(int nivel) {
    tabelaSemantica.removeIf(linha -> Integer.parseInt(linha[2]) == nivel);
    exibirTabelaSemantica();
  }

  private void exibirTabelaSemantica(){
    // imprime a tabela semantica
    System.out.println("----- TABELA SEMÂNTICA DE DECLARAÇÕES ----");
    System.out.println("TIPO       IDENTIFICADOR       NÍVEL DE ESCOPO");
    for (String[] entrada : tabelaSemantica) {
        System.out.printf("%-10s %-20s %-15s%n", entrada[0], entrada[1], entrada[2]);
    }
    System.out.println("------------------------------------------");
  }

  
}

