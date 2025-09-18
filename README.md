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



# Características da linguagem criada
 
## tipos de variáveis:<br>
- inteiro<br>
- decimal<br>
- texto<br>
- verdadeiroFalso<br>

## retorno de funcao<br>
retorna<br>

## estrutura condicional de controle de fluxo<br>
-se{}<br>
-ouSe{}<br>
-senao{}<br>

## funcoes<br>
criar Nomequalquer(qualquer coisa) {}<br>

## estruturas de repetição<br>
- para{}<br>
- enquanto {}<br>
 
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
#uai<br>
#so (várias linhas)<br>

## leituras teclado<br>
entrada <br>
imprima<br>

# operador logico <br>
e<br>
ou<br>
 
# Exemplos de código na sua linguagem criada e a tradução equivalente.

inteiro Trem_inteiro;<br>
decimal Trem_decimal -> 1.1;<br>
texto Trem_texto -> “oi”;<br>
verdadeiroFalso Trem_vf -> false;<br>

Entrada(“digita um numero”, Trem_inteiro);<br>


criar Imprimir(inteiro Trem_num){<br>
	*uai funcao pra imprimir<br>
Imprima(“numero digitado”, Trem_num);<br>
}<br>
se(Trem_inteiro <> 10 e Trem_inteiro <= 20){<br>
Imprimir(Trem_inteiro);<br>
#uai<br>
 código basico<br>
so#<br>
}<br>

<br>}<br>


