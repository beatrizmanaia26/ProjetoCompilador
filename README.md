# 🛠 minasScript

*minasScript* é um compilador escrito em *Java* que traduz uma linguagem criada por nós, Mineires — inspirada no português com sotaque mineiro — para outra linguagem de programação.

## 👥 Integrantes

- Beatriz Manaia Lourenço Berto — RA: 22.125.060-8  
- Mariane Souza Carvalho — RA: 22.123.105-3  
- Rafael Dias Silva Costa — RA: 22.222.039-4  
- Kayky Pires de Paula — RA: 22.222.040-2  

## Expressões Regulares
<br>numeroInteiro = [0-9]+
<br>numeroDecimal = [0-9]+’.’[0-9]+
<br>Texto = “[A-Za-z0-9,&¨@ (tudo)]”’
<br>tipos_dadoInt = ‘inteiro’
<br>tipo_dadoDecimal = ‘decimal’
<br>tipo_dadoVerdadeiroFalso = verdadeiroFalso
<br>tipo_dadoTexto = ‘texto’
<br>identificadores = Trem_[a-zA-Z]+[0-9]*
<br>palavra_reservada_condicionalSe = ‘se’
<br>palavra_reservada_condicionalOuSe = ‘ouSe’
<br>palavra_reservada_condicionalSenao = ‘senao’
<br>palavra_reservada_estruturaPara = ‘para’
<br>palavra_reservada_estruturaEnquanto = ‘lacoEnquanto’
<br>palavra_reservadaEntrada = ‘Entrada’
<br>palavra_reservadaImprima = ‘Imprima’
<br>palavra_reservadaDefinirFuncao = ‘criar’
<br>palavra_reservadaRetornoFuncao = ‘retorna’
<br>palavra_reservadaTrue = ‘true’
<br>palavra_reservadaFalse = ‘false’
<br>operador_logicoE = ‘e’
<br>operador_logicoOu = ‘ou’
<br>operador_logicoNot = ‘!’
<br>palavra_reservadaNomeFuncao = [A-Z][a-z0-9]*
<br>operadorAtribuicao = ‘->’  
<br>operadorDiferente = ‘<>’
<br>operadorIgualdade = ‘<->’
<br>operadorMenor = ‘<’
<br>operadorMaior = ‘>’
<br>operadorMenorIgual = ‘<=’
<br>operadorMaiorigual = ‘>=’
<br>operadorSoma = ‘+’
<br>operadorSubtracao = ‘-’
<br>operadorMultiplicacao = ’*’
<br>operadorPotencia =  ‘^’
<br>oparadorDivisao = ‘/’
<br>comentarioVariasLinhas = #uai... [A-Za-z0-9 , - . : “ (adicionar td)]...so#
<br>fim_linha = ‘;’
<br>aberturaChave = ‘{‘
<br>fecharChave = ‘}’
<br>abreParenteses = ‘(‘
<br>fechaParenteses = ‘)’

# Gramática completa do analisador sintático 

a gramática não pode conter recursividade à esquerda

## Estruturas do código COLOCAR TODAS AS VARIAVEIS (ESQ) com letra maiuscula

<br> consdierar q td linha termina com ;

AJUSTAR TD Q TEM OPERACOES MATEMATICAS P COLOCAR A GLC EXPRESSOESMATEMATICAS (AJUSTAR CONDICAO (CABECALHOPARA (INICIALIZACAO...)))

VER SE TEM DERIVACAO OU FATORACAO P ARRUMAR 

fazer so comparacoes mais simples

condciao so n faz chamada de metodo ex: exto.startsWith("A")

RESOLVER RECURSIVIDADE INDIRETA DO PARAMETROFUNCAO E RESTOPARAMETROFUNCAO

<br> listaComandos -> comando listaComandos | ε 
<br> comando -> seCompleto|para|lacoEnquanto|declarar|atribui|
<br> criarFuncao|chamarFuncao
<br> seCompleto ->se listaOuSe senaoOpcional
<br> listaOuSe -> ouSe listaOuSe | ε
<br> senaoOpcional -> senão | ε
<br> se -> 'se''('condicao')''{'listaComandosInternos'}'
<br> ouSe -> 'ouSe''('condicao')''{'listaComandosInternos'}'
<br> senao -> 'senao''{'listaComandosInternos'}'
<br> para -> 'para''('cabecalhoPara')''{'listaComandosInternos'}'
<br> lacoEnquanto -> 'lacoEnquanto''('condicao')''{'listaComandosInternos'}' 
<br> listaComandosInternos -> comandoInterno listaComandosInternos | ε
<br> comandoInterno -> se|ouSe|senao|para|lacoEnquanto|declarar|atribui|chamarFuncao|retornar
<br> retornar -> 'retorna' conteudos';'
<br> conteudos -> identificadores|expressoesMatematicas|numero|isBoolean
<br> cabecalhoPara -> inicializacao ";" condicao ";" incremento
<br> inicializacao -> tipoVariavel identificadores "->" conteudos
<br> incremento -> identificadores operacaoIncremento 
<br> operacaoIncremento -> operadorSoma operadorSoma|operadorSubtracao operadorSubtracao 

<br> inicio correcao "condicao" (ver se da certo e se n tem recursao ou fatoracao a esquerda):
<br> condicao -> (condicao) | identificadores|negacaoCondicao|condicaoComparacoesBasicas 

<br> condicao -> identificadores condicao’ | negacaoCondicao condicao’ | expressoesMatematicas condicao’| condicaoComparacoesBasicas condicao’
<br> condicao’ -> operacao condição condicao’| ε
<br> comparacoesBasicas -> identificadores|numero operacao valoresOperacao 
<br> condicaoComparacoesBasicas ->  comparacoesBasicas || !identificadores
<br> valoresOperacao -> identificadores|numero|boolean
<br> negacaoCondicao -> '!'condicao
<br> operacao -> operacaoRelacional|operacaoLogica
<br> operacaoRelacional -> operadorDiferente|operadorIgualdade|operadorMenorIgual|operadorMaiorigual
<br> operacaoLogica -> operador_logicoE|operador_logicoOu|operador_logicoNot
<br> expressoesMatematicas -> precedenciaInferior 
<br> precedenciaInferior -> precedenciaIntermediaria precedenciaInferior'
<br> precedenciaInferior' -> '+'precedenciaIntermediaria precedenciaInferior' | '-'precedenciaIntermediaria precedenciaInferior' | ε
<br> precedenciaIntermediaria -> precedenciaAlta precedenciaIntermediaria' 
<br> precedenciaIntermediaria' -> '*' precedenciaAlta precedenciaIntermediaria' | '/'precedenciaAlta precedenciaIntermediaria' | ε
<br> precedenciaAlta -> precedenciaSuperior precedenciaAlta'
<br> precedenciaAlta' -> '^'precedenciaSuperior precedenciaAlta' | ε
<br> precedenciaSuperior -> identificadores|numero|'('expressoesMatematicas')'
<br> atribui -> identificadores '->' valor ';'
<br> declaracao-> tipoVariavel identificadores (';' | '->' valor ';')
<br> numero -> numeroDecimal|numeroInteiro 
<br> boolean -> true|false 
<br> criarFuncao -> 'criar' palavra_reservadaNomeFuncao'('argumentosFuncao')''{'listaComandosInternos'}'
<br> argumentosFuncao -> ε|parametrosFuncao
<br> parâmetroFuncao -> parâmetro emComumParametro
<br> emComumParametro -> ε | ‘,’ parametroFuncao emComumParametro
<br> parametro -> tipoVariavel identificadores
<br> tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto
<br> chamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')' ';'
<br> inicioChamarFuncao -> inicioChamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima
<br> argumentosChamada -> ε | valor restoArgumentosChamada
<br> valor -> numero|texto|boolean|identificadores|expressoesMatematicas|condicaoComparacoesBasicas
<br> restoArgumentosChamada -> ε | ',' valor restoArgumentosChamada


<br> comentario nao precisa pq na linguagem so vai gera o executzvel de outrs ling nao um codigo p ler q precise de comentario, entao nao passa comentario pro token.

<br> permitir ifs encadeados e lacos encadeados (com "comando" permite)

# Analisador semantico

-como comparo string
-coloquei valor = ao tipo que declarei...

# Como executar o compilador

DESCREVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

# Características da linguagem criada

# Sobre declarar e atribuir (declaracao())

- quando declaramos e atribuimos (ao mesmo tempo) uma variavel, podemos: <br>
- atribuir numeros (inteiro,decima), booleano (true, false), identificadores <br>
- expressoesMatematicas de qualquer tamanho<br>
- comparacoesbasicas (comparar 2 coisas apenas, com quaquer operador, ex !Trem_a, 2 < 3, Trem_a ou Trem_b...), nao da para comparar muitas coisas ao mesmo tempo
- nao da para misturar eles (ex: quando declarar, atribuir expressoes matematicas e comparacoes basicas (ex: verdadeiroFalso Trem_a -> (2.3 +4) > 2; verdadeiroFalso Trem_a -> (2.3 +4) <= Trem_b;))<br>

# Em condicao()

- da para escrever varias comparacoes (ex: se(Trem_a < 2 ou Trem_b <> Trem_c e Trem_d <-> 5){}), porem o resultado estara errado se fizer dessa forma pois nao tem parenteses para determinar a ordem de comparacoes


## tipos de variáveis:<br>
- inteiro<br>
- decimal<br>
- texto<br>
- verdadeiroFalso<br>

## retorno de funcao<br>
retorna<br>

## estrutura condicional de controle de fluxo<br>
-se(){}<br>
-ouSe(){}<br>
-senao{}<br>

## funcoes<br>
criar Nomequalquer(qualquer coisa){}<br>

## estruturas de repetição<br>
- para{}<br>
- lacoEnquanto {}<br>
 
## atribuicao<br>
  ->  <br>
## operador relacional<br>
  <>  diferente<br>
  <->  igualdade<br>
  <=  menor ou igual<br>
  >=  maior ou igual<br>
  <  menor<br>
  >  maior<br>

## operador matemático<br>
soma +<br>
subtracao -<br>
vezes *<br>
potencia ^<br>
...so# (várias linhas)<br>

## leituras teclado<br>
entrada <br>
imprima<br>

# operador logico <br>
e<br>
ou<br>
!(not)<br>

# Exemplos de código na sua linguagem criada e a tradução equivalente.

inteiro Trem_inteiro;<br>
decimal Trem_decimal -> 1.1;<br>
texto Trem_texto -> “oi”;<br>
verdadeiroFalso Trem_vf -> false;<br>
Entrada(“digita um numero”, Trem_inteiro);<br>


criar Imprimir(inteiro Trem_num){<br>
Imprima(“numero digitado”, Trem_num);<br>
}<br>
Se(Trem_inteiro <> 10 e Trem_inteiro <= 20){<br>
Imprimir(Trem_inteiro);<br>
#uai<br>
 código basico<br>
so#<br>
}<br>

<br>}<br>



inteiro Trem_num -> 77;<br>
inteiro Trem_inteiro;<br>
decimal Trem_decimal -> 1.1;<br>
texto Trem_texto -> "oi";<br>
verdadeiroFalso Trem_vf -> true;<br>

Entrada("digite um numero", Trem_inteiro);<br>

criar Imprimir(inteiro Trem_num){<br>
    Imprima("número digitado", Trem_num);<br>
}<br>
se (Trem_inteiro <> 10 e Trem_inteiro <= 20){<br>
    para (inteiro Trem_x -> 1; Trem_x <= 5; Trem_x++){<br>
        Imprima(Trem_x)<br>
    }<br>
    Imprimir(Trem_inteiro);<br>
}<br>
senao{<br>
    retorna false;<br>
}<br>
} <br>

decimal Trem_limiteSaque -> 2000.00;<br>
decimal Trem_transferencia -> 2.5;<br>
inteiro Trem_maxTentativasSenha -> 3;<br>

criar Main(){<br>
    texto Trem_nome -> "Beatriz";<br>
    texto Trem_numeroConta -> "12345-6";<br>
    decimal Trem_saldo -> 16000.00;<br>
    texto Trem_senhaCorreta -> "1234"<br>
    verdadeiroFalso Trem_userLogado -> false;<br>
    inteiro Trem_tentativasSenha -> 0;<br>
    Imprima("bem vindo");<br>

    lacoEnquanto(!Trem_userLogado e Trem_tentativasSenha < Trem_maxTentativasSenha){<br>
        texto Trem_senha;<br>
        Imprima("Digite a senha", Trem_senhaDigitada);<br>

        se(Trem_senhaDigitada <-> Trem_senhaCorreta){<br>
        Trem_userLogado -> true;<br>
           Imprima("login realizado");<br>
        }<br>
    
        senao{<br>
            Trem_tentativasSenha -> Trem_tentativasSenha + 1;<br>
            Imprima("senha incorreta, tentativas restantes" Trem_maxTentativasSenha -  Trem_tentativasSenha);<br>
            se(Trem_tentativasSenha >= Trem_maxTentativasSenha){<br>
                Imprima("conta bloqueada por excesso de tentativas");<br>
            }<br>
        }<br>
    } <br>
   verdadeiroFalso Trem_sistemaAtivo -> true;<br>
   lacoEnquanto(Trem_sistemaAtivo e Trem_userLogado){<br>
        Imprima(" MENU PRINCIPAL");<br>
        Imprima("Cliente: ", Trem_nome);<br>
        Imprima("Conta: ", Trem_numeroConta);<br>
        Imprima("Saldo: R$ ",Trem_saldo);<br>
        Imprima("1 - Saque");<br>
        Imprima("2 - Depósito");<br>
        Imprima("3 - Transferência");<br>
        Imprima("4 - Extrato");<br>
        Imprima("5 - Alterar Senha");<br>
        Imprima("0 - Sair");<br>
        Imprima("Escolha uma opção: ");<br>
        inteiro Trem_opcao;<br>
        Entrada("Digite a opcao ", Trem_opcao);<br>
        se(Trem_opcao <-> 1){<br>
        Imprima("Digite o valor do saque");<br>
        decimal Trem_valorSaque;<br>
        se(Trem_valorSaque > 0 e Trem_valorSaque <= Trem_saldo){<br>
            se(Trem_valorSaque <= Trem_limiteSaque){<br>
                Trem_saldo -> Trem_valorSaque - Trem_saldo;<br>
                Imprima("Saque  realizado com sucesso no valor de R$ ", Trem_valorSaque);<br>
                Imprima("Novo saldo: R$ " Trem_saldo);<br>
            }<br>
            senao{<br>
                Imprima("Valor excede o limite de saque de R$", Trem_limiteSaque);<br>
            }<br>
        }<br>
    }<br>
}<br>

#uai...<br>
1hello"<br>
...so#<br>
"helloO" <br>


## Exemplo 3

<br> elaborar um codigo facil (exs basicos introducao a computacao)