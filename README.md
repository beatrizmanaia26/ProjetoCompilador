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
<br>identificadores =  Trem_[a-zA-Z]+[0-9]*
<br>palavra_reservada_condicionalSe = ‘se’
<br>palavra_reservada_condicionalOuSe = ‘ouSe’
<br>palavra_reservada_condicionalSenao = ‘senao’
<br>palavra_reservada_estruturaPara = ‘para’
<br>palavra_reservada_estruturaEnquanto = ‘enquanto’
<br>palavra_reservadaEntrada = ‘entrada’
<br>palavra_reservadaImprima = ‘imprima’
<br>palavra_reservadaDefinirFuncao = ‘criar’
<br>palavra_reservadaRetornoFuncao = ‘retorna’
<br>operador_logicoE = ‘e’
<br>operador_logicoOu = ‘ou’
<br>palavra_reservadaNomeFuncao = [A-Z][a-z0-9]*
<br>operadorAtribuicao = ‘ ->’  
<br> operadorDiferente = ‘<>’
<br> operadorIgualdade = ‘<->’
<br>operadorMenorIgual = ‘<=’
<br>operadorMaiorigual = ‘>=’
<br>operadorSoma = ‘+’
<br>operadorSubtracao = ‘-’
<br>operadorMultiplicacao = ’*’
<br>operadorPotencia =  ‘^’
<br>oparadorDivisao = ‘/’
<br>comentarioVariasLinhas = #uai [A-Za-z0-9 , - . : “ (adicionar td)]so#
<br>fim_linha = ‘;’
<br>aberturaChave = ‘{‘
<br>fecharChave = ‘}’
<br>abreParenteses = ‘(‘
<br>fechaParenteses = ‘)’


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
<br>entrada 
<br>imprima


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


