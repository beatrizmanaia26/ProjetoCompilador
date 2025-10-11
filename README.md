# 🛠️ minasScript

**minasScript** é um compilador escrito em **Java** que traduz uma linguagem criada por nós, Mineires — inspirada no português com sotaque mineiro — para outra linguagem de programação.

## 👥 Integrantes

- Beatriz Manaia Lourenço Berto — RA: 22.125.060-8  
- Mariane Souza Carvalho — RA: 22.123.105-3  
- Rafael Dias Silva Costa — RA: 22.222.039-4  
- Kayky Pires de Paula — RA: 22.222.040-2  

## Expressões Regulares
<br>numeroInteiro = [0-9]+
<br>numeroDecimal = [0-9]+’.’[0-9]+
<br>Texto = “[A-Za-z0-9,*&¨@ (tudo)]*”’
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
<br>operadorMenorIgual = ‘<=’
<br>operadorMaiorigual = ‘>=’
<br>operadorSoma = ‘+’
<br>operadorSubtracao = ‘-’
<br>operadorMultiplicacao = ’*’
<br>operadorPotencia =  ‘^’
<br>operadorDivisao = ‘/’
<br>comentarioVariasLinhas = #uai... [A-Za-z0-9 , - . : “ (adicionar td)]...so#
<br>fim_linha = ‘;’
<br>aberturaChave = ‘{‘
<br>fecharChave = ‘}’
<br>abreParenteses = ‘(‘
<br>fechaParenteses = ‘)’
<br>operadorIncremento++ = ‘++’
<br>operadorIncremento-- = ‘--’
<br>operadorIncremento+= = ‘+=’
<br>operadorIncremento-= = ‘-=’
<br>operadorIncremento*= = ‘*=’
<br>operadorIncremento/= = ‘/=’

# Gramática completa do analisador sintático 

a gramática não pode conter recursividade à esquerda

## Estruturas do código COLOCAR TODAS AS VARIAVEIS (ESQ) com letra maiuscula

VER SE TEM DERIVACAO OU FATORACAO P ARRUMAR 

fazer so comparacoes mais simples

**condciao so n faz chamada de metodo ex: exto.startsWith("A")

<br> listaComandos -> comando listaComandos | ε 
<br> comando -> se|ouSe|senao|para|lacoEnquanto|atribuicao|criarFuncao|chamarFuncao
<br> se -> 'se''('condicao')''{'listaComandosInternos'}'
<br> ouSe -> 'ouSe''('condicao')''{'listaComandosInternos'}'
<br> senao -> 'senao''{'listaComandosInternos'}'
<br> para -> 'para''('cabecalhoPara')''{'listaComandosInternos'}'
<br> lacoEnquanto -> 'lacoEnquanto''('condicao')''{'listaComandosInternos'}' 
<br> listaComandosInternos -> comandoInterno listaComandosInternos | ε
<br> comandoInterno -> se|ouSe|senao|para|lacoEnquanto|atribuicao|chamarFuncao|retornar
<br> retornar -> palavra_reservadaRetornoFuncao identificadores|expressoesMatematicas|numero';'
<br> cabecalhoPara -> inicializacao ";" condicao ";" incremento
<br> inicializacao -> tipoVariavel identificadores "->" numero|identificadores|chamarFuncao|expressoesMatematicas
<br> incremento -> identificadores operacaoIncremento | identificadores operacaoIncremento numero|identificadores | identficiadores operacaoIncremento expressoesMatematicas
<br> operacaoIncremento -> operadorIncremento++|operadorIncremento--|operadorIncremento+=|operadorIncremento-=|operadorIncremento*=|operadorIncremento/=
<br> condicao -> '('condicao')' | identificadores|negacaoCondicao|condicaoComparacoesBasicas (ARRUMAR)

<br>DERIVACAO:

<br>condicao -> (condicao) |identificadores|negacaoCondicao|condicaoComparacoesBasicas 

<br>ε = alpha1 ?????????????????????????????????
<br>identificadores = beta1
<br>negacaoCondicao = beta2
<br>condicaoComparacoesBasica = beta3

<br>condicao -> identificadores condicao' | negacaoCondicao condicao' | condicaoComparacoesBasica condicao'
<br>condicao' -> ε condicao' | ε

<br> condicaoComparacoesBasicas -> expressoesMatematicas | identificadores|numero operacao identificadores|numero|boolean  |  condicao operacao condicao
<br>(esses 2 de cima nao sao problema né?? condicao chama condicaocomparacoesbasicas e condicaocomparaoesbasicas chamar condicoes) ARRUMAR RECURSAO INDIDRETA!!!!!!!!!!!!!!!!!
<br> negacaoCondicao -> '!'condicao
<br> operacao -> operacaoRelacional|operacaoLogica
<br> operacaoRelacional -> operadorDiferente|operadorIgualdade|operadorMenorIgual|operadorMaiorigual
<br> operacaoLogica -> operador_logicoE|operador_logicoOu|operador_logicoNot
<br> expressoesMatematicas -> precedenciaInferior 
<br> precedenciaInferior -> precedenciaIntermediaria precedenciaInferior'
<br> precedenciaInferior' -> '+'precedenciaIntermediaria precedenciaInferior' | '-'precedenciaIntermediaria precedenciaInferior' | ε
<br> precedenciaIntermediaria -> precedenciaAlta precedenciaIntermediaria' 
<br> precedenciaIntermediaria' -> '*' precedenciaAlta precedenciaIntermediaria' | /precedenciaAlta precedenciaIntermediaria' | ε
<br> precedenciaAlta -> precedenciaSuperior precedenciaAlta'
<br> precedenciaAlta' -> '^'precedenciaSuperior precedenciaAlta' | ε
<br> precedenciaSuperior -> identificadores|numero|'('expressoesMatematicas')'
<br> atribuicao -> declaraEAtribui|atribui 
<br> atribui -> identificadores operadorAtribuicao numero|texto|boolean ';'
<br> declaraEAtribui -> declaraEAtribuiInteiro|declaraEAtribuiDecimal|declaraEAtribuiTexto|declaraEAtribuiVerdadeiroFalso
<br> declarar -> declararInteiro|declararDecimal|declararTexto|declararVerdadeiroFalso 
<br> declararInteiro -> 'inteiro' identificadores ';'
<br> declaraEAtribuiInteiro -> 'inteiro' identificadores operadorAtribuicao numeroInteiro ';'
<br> declararDecimal ->  'decimal' identificadores ';' 
<br> declaraEAtribuiDecimal -> 'decimal' identificadores operadorAtribuicao numeroDecimal ';'
<br> declararTexto ->  'texto' identificadores ';'
<br> declaraEAtribuiTexto -> 'texto' identificadores operadorAtribuicao texto ';'
<br> declararVerdadeiroFalso -> 'verdadeiroFalso' identificadores ';' 
<br> declaraEAtribuiVerdadeiroFalso -> 'verdadeiroFalso' identificadores operadorAtribuicao boolean ';'
<br> numero -> numeroDecimal|numeroInteiro 
<br> boolean -> true|false 
<br> operadorRelacional -> operadorIgualdade|operadorMenorIgual|operadorMaiorigual
<br> criarFuncao -> 'criar' palavra_reservadaNomeFuncao'('argumentosFuncao')''{'listaComandosInternos'}'
<br> argumentosFuncao -> ε|parametrosFuncao
<br> parametroFuncao -> parametro restoParametrosFuncao
<br> restoParametrosFuncao -> ε|',' parametroFuncao restoParametrosFuncao ?????????????
<br> parametro -> tipoVariavel identificadores
<br> tipoVariavel -> tipos_dadoInt|tipo_dadoDecimal|tipo_dadoVerdadeiroFalso|tipo_dadoTexto identificadores 
<br> chamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')' ';'
<br> argumentosChamada -> ε | valor restoArgumentosChamada
<br> valor -> numero|texto|boolean|identificadores|expressoesMatematicas
<br> restoArgumentosChamada -> ε | ',' valor restoArgumentosChamada

<br> INFORMAÇÕES GERAIS:

<br> comentario nao precisa pq na linguagem so vai gera o executável de outra linguagem não um código para ler que precise de comentário, então não passa comentário para o token que gera gramática.
<br> permitir ifs encadeados e lacos encadeados (com "comando" permite)
<br> A parte de expressões envolvendo os operadores matemáticos deve ser realizada de maneira correta, respeitando a precedência.

# Analisador semantico

-como comparo string
-coloquei valor = ao tipo que declarei...

# Como executar o compilador

# Características da linguagem criada
 
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
  <=  menor ou igual
  >=  maior ou igual

## operador matemático<br>
soma +<br>
subtracao -<br>
vezes *<br>
potencia ^<br>
divisao /<br>

## comentario<br>
#uai...<br>
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
se(Trem_inteiro <> 10 e Trem_inteiro <= 20){<br>
Imprimir(Trem_inteiro);<br>
#uai<br>
 código basico<br>
so#<br>
}<br>

<br>}<br>
