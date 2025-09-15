# 🛠️ minasScript

**minasScript** é um compilador escrito em **Java** que traduz uma linguagem criada por nós, Mineires — inspirada no português com sotaque mineiro — para outra linguagem de programação.

## 👥 Integrantes

- Beatriz Manaia Lourenço Berto — RA: 22.125.060-8  
- Mariane Souza Carvalho — RA: 22.123.105-3  
- Rafael Dias Silva Costa — RA: 22.222.039-4  
- Kayky Pires de Paula — RA: 22.222.040-2  

## Expressões Regulares
 numeroInteiro = [0-9]+
numeroDecimal = [0-9]+’.’[0-9]+
Texto = “[A-Za-z0-9,*&¨@ (tudo)]*”’
tipos_dadoInt = ‘inteiro’
tipo_dadoDecimal = ‘decimal’
tipo_dadoVerdadeiroFalso = verdadeiroFalso
tipo_dadoTexto = ‘texto’
identificadores =  Trem_[a-zA-Z]+[0-9]*
palavra_reservada_condicionalSe = ‘se’
palavra_reservada_condicionalOuSe = ‘ouSe’
palavra_reservada_condicionalSenao = ‘senao’
palavra_reservada_estruturaPara = ‘para’
palavra_reservada_estruturaEnquanto = ‘enquanto’
palavra_reservadaEntrada = ‘entrada’
palavra_reservadaImprima = ‘imprima’
palavra_reservadaDefinirFuncao = ‘criar’
palavra_reservadaRetornoFuncao = ‘retorna’
operador_logicoE = ‘e’
operador_logicoOu = ‘ou’
palavra_reservadaNomeFuncao = [A-Z][a-z0-9]*
operadorAtribuicao = ‘ ->’  
 operadorDiferente = ‘<>’
 operadorIgualdade = ‘<->’
operadorMenorIgual = ‘<=’
operadorMaiorigual = ‘>=’
operadorSoma = ‘+’
operadorSubtracao = ‘-’
operadorMultiplicacao = ’*’
operadorPotencia =  ‘^’
oparadorDivisao = ‘/’
comentarioVariasLinhas = #uai [A-Za-z0-9 , - . : “ (adicionar td)]so#
fim_linha = ‘;’
aberturaChave = ‘{‘
fecharChave = ‘}’
abreParenteses = ‘(‘
fechaParenteses = ‘)’


## Palavras Reservadas

Condicional: se, ouSe, senao
<br>Laços: para, enquanto
<br>Funções: criar, retorna
<br>Entrada/Saída: entrada, imprima

## Operadores
Atribuição:         ->
<br>Igualdade:          <->
<br>Diferença:          <>
<br>Menor ou igual:     <=
<br>Maior ou igual:     >=
<br>Soma:               +
<br>Subtração:          -
<br>Multiplicação:      *
<br>Potência:           ^
<br>Divisão:            /
<br>Lógicos:            e, ou

## Delimitadores
Chaves:             { }
<br>Parênteses:         ( )
<br>Fim de linha:       ;

## leituras teclado
entrada 
imprima


## Comentários
#uai ... so#      


## Exemplo código:
inteiro Trem_inteiro;
<br>decimal Trem_decimal -> 1.1;
<br>texto Trem_texto -> "oi";
<br>verdadeiroFalso Trem_vf -> false;
<br>
<br>entrada("digita um numero", Trem_inteiro);
<br>
<br>criar Imprimir(inteiro Trem_num) {
  <br>#uai funcao pra imprimir
  <br>imprima("numero digitado", Trem_num);
}
<br>
<br>se(Trem_inteiro <> 10 e Trem_inteiro <= 20) {
  <br>imprimir(Trem_inteiro);
  <br>#uai
  <br>código basico
  <br>so#
<br>}


