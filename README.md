# 🛠 minasScript

*minasScript* é um compilador escrito em *Java* que traduz uma linguagem criada por nós, Mineires — inspirada no português com sotaque mineiro — para Java.

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
<br>palavra_reservadaNomeFuncao = [A-Z][a-z_0-9]*
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
<br>virgula = ‘,’
<br>aberturaChave = ‘{‘
<br>fecharChave = ‘}’
<br>abreParenteses = ‘(‘
<br>fechaParenteses = ‘)’

# Gramática completa do analisador sintático 

a gramática não pode conter recursividade à esquerda (direta ou indireta) nem fatoração a esquerda.

## Estruturas do código

fazer só comparacoes mais simples

<br> listaComandos -> comando listaComandos | ε 
<br> comando -> seCompleto|para|lacoEnquanto|declararcao|atribui|
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
<br> comandoInterno -> se|ouSe|senao|para|lacoEnquanto|declaracao|atribui|chamarFuncao|retornar
<br> retornar -> 'retorna' conteudos';'
<br> conteudos -> identificadores|expressoesMatematicas|numero|isBoolean
<br> cabecalhoPara -> inicializacao ";" condicao ";" incremento
<br> inicializacao -> tipoVariavel identificadores "->" conteudos
<br> incremento -> identificadores operacaoIncremento 
<br> operacaoIncremento -> operadorSoma operadorSoma|operadorSubtracao operadorSubtracao 
<br> condicao -> identificadores condicao’ | negacaoCondicao condicao’ | expressoesMatematicas condicao’| condicaoComparacoesBasicas condicao’
<br> condicao’ -> operacao condição condicao’| ε
<br> comparacoesBasicas -> identificadores|numero operacao valoresOperacao 
<br> condicaoComparacoesBasicas ->  comparacoesBasicas | !identificadores
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
<br> atribui ->  identificadores '->' valor ';'
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
<br> chamarFuncaoSemFim -> palavra_reservadaNomeFuncao|Entrada|Imprima '('argumentosChamada')'
<br> inicioChamarFuncao -> inicioChamarFuncao -> palavra_reservadaNomeFuncao|Entrada|Imprima
<br> argumentosChamada -> ε | valor restoArgumentosChamada
<br> valor -> numero|texto|boolean|identificadores|expressoesMatematicas|condicaoComparacoesBasicas|chamarFuncaoSemFim
<br> restoArgumentosChamada -> ε | ',' valor restoArgumentosChamada


<br> comentario nao precisa pq na linguagem so vai gera o executzvel de outrs ling nao um codigo p ler q precise de comentario, entao nao passa comentario pro token.

<br> permitir ifs encadeados e laços encadeados (com "comando" permite)

# Analisador Semântico

- DESCREVER EXATAMENTE OQ FAZ!!!!!!!!!!!!!!!

-comparei valores do mesmo tipo?<br>
- todas as variaveis que usei ja foram declaradas?<br>
- coloquei valor = ao tipo que declarei...<br>
- Verificação de tipos (a mais importante)<br>
- Declaração e uso de variáveis (escopo)<br>
- Compatibilidade em operações<br>
- Chamada de funções (número e tipos de parâmetros)<br>
- Retorno de funções<br>

DIRECIONAMENTO CHARLES DESCRIÇÃO PROJETO: <br>
– Verificar se uma variável foi declara.<br>
– Verificar se os tipos de dados de uma expressão são iguais.<br>
– Verificar o escopo da variável.<br>


# Como executar o compilador 

 - Fazer download do zip do projeto. <br>
 - Escrever um código em minasScript no “script.txt”.<br>
 - Rodar a Main.java que está na pasta analisadorLexico(no terminal aparecera os token, arvore e se o código está sintaticamente correto ou incorreto, bem como o nome do arquivo com a tradução do código).<br>
  - O arquivo com o código traduzido está em CodigoTraduzido.java, execute esse código e veja rodar corretamente.<br>

# Características da linguagem criada

## Detalhes da gramática:

### Sobre declarar e atribuir (declaracao())

- só é possivel usar corretamente a "Entrada" se estivrmos declarando e atribuindo (para fazer a traducao certa) ex:decimal Trem_int2 -> Entrada();<br>
- Quando declaramos e atribuímos (ao mesmo tempo) uma variável, podemos: <br>
- atribuir números (inteiro,decimal), booleano (true, false), identificadores <br>
- "expressoesMatematicas" de qualquer tamanho<br>
- "comparacoesBasicas" (comparar 2 coisas apenas, com quaquer operador, ex !Trem_a, 2 < 3, Trem_a ou Trem_b...), nao da para comparar muitas coisas ao mesmo tempo. <br>
- Não da para misturar eles (ex: quando declarar, atribuir "expressoesMatematicas" e "comparacoesBasicas" (ex: verdadeiroFalso Trem_a -> (2.3 +4) > 2; verdadeiroFalso Trem_a -> (2.3 +4) <= Trem_b;))<br>

### Em condicao

- Ideia inicial era na glc de condicao ter (condicao), permitir "()" (condicao -> (condicao) | identificadores|negacaoCondicao|condicaoComparacoesBasicas (nao usado)), porem retiramos isso <br>
- "condicao" não faz chamada de metodo e nem ()<br>
- Da para escrever várias comparacoes (ex: se(Trem_a < 2 ou Trem_b <> Trem_c e Trem_d <-> 5){}), porém o resultado estara errado se fizer dessa forma pois não tem parenteses para determinar a ordem de comparações<br>
- Em "condicao" posso comparar varias coisas (expressoesmatematicas de qualquer tamanho com número/"expressoesMatematicas" de qualquer tamanho, identificadores...)<br>
- Se for usar expressao matematica, apos a operacao só pode ter os "tipos" que estao em condicao, ou seja, nao pode comparar diretamente expressao amteamtica com valor (ex:(2+3<8))

## Sobre funcões:

- permiti na gramatica colocar varias coisas dentro da chamada de entrada mesmo que só vamos usar com () vazio e passando 1 argumento do usuario (n tava pensando no java qnd fiz)
- toda criacao de funcao fica fora da main e toda chamada de funcao fica dentro da main, poré, se eu chamar uma função dentro da criação de outra, ela aparece dentro da função.
- bug: ocorre apenas com funcao sem retorno, se ela é a primeira funcao declarada e as outras tem retorno, o void eh sobrescrito pelo retorno das coisas, então funcao sem retorno declara sempre por ultimo (pois se tiver alguma funcqao com retorno apos ela, seu retorno sera igual ao que a precede)

## potencia

- para usar potencia o tipo de variavel que recebe tem que ser decimal (pois nao fiz a verificacao para verificar o tipo de variavel que recebe a potencia e se for inteiro adicionar um (int) antes do math.pow

## tipos de variáveis:<br>
- inteiro<br>
- decimal<br>
- texto<br>
- verdadeiroFalso<br>

## retorno de função:<br>
- retorna<br>

## estrutura condicional de controle de fluxo:<br>
- se(condicao){}<br>
- ouSe(condicao){}<br>
- senao{}<br>

## funções:<br>
- criar Nomequalquer(qualquer coisa){}<br>

## estruturas de repetição:<br>
- para(inicializacao;condicao;incremento){}<br>
- lacoEnquanto(condicao){}<br>
 
## atribuição:<br>
- ->  <br>

## operadores relacionais:<br>
- <>  diferente<br>
- <->  igualdade<br>
- <=  menor ou igual<br>
- >=  maior ou igual<br>
- <  menor<br>
- >  maior<br>

## operadores matemáticos:<br>
- soma: +<br>
- subtracao: -<br>
- multiplicação: *<br>
- divisão: / <br>
- potência: ^<br>

## leituras do teclado:<br>
- ler do usuário: Entrada <br>
- imprimir na tela: Imprima <br>

# operadores lógicos: <br>
- e<br>
- ou<br>
- !(not)<br>

# comentário <br>
- #uai... ...so#: comentário<br>

# ADICIONAIS (não solicitados porém feitos):
•	Um tipo de variável a mais<br>
•	Pode criar função <br>
•	Utilizamos operadores lógicos<br>
•	Comentário<br>
•	Um tipo a mais de expressão matemática (potência)<br>


# Exemplos de código na sua linguagem criada e a tradução equivalente.

## exemplo 1:
#uai... esse código mostra todos os encadeamentos possíveis: se dentro de se, para dentro de para, lacoDentroDePara e vice-versa ...so#

criar SomarMatriz(){
    #uai... exemplo de para dentro de para ...so#
    inteiro Trem_soma -> 0;
    para(inteiro Trem_linha -> 1; Trem_linha <= 3; Trem_linha++){
        para(inteiro Trem_coluna -> 1; Trem_coluna <= 3; Trem_coluna++){
            Trem_soma -> Trem_soma + (Trem_linha * Trem_coluna);
            Imprima("Linha ", Trem_linha, " Coluna ", Trem_coluna, " Valor \n", Trem_linha * Trem_coluna);
        }
    }
    retorna Trem_soma;
}

criar ContagemComCondicoes(){
    #uai... exemplo de lacoEnquanto dentro de para e se encadeado ...so#
    para(inteiro Trem_x -> 1; Trem_x <= 5; Trem_x++){
        inteiro Trem_y -> Trem_x;

        lacoEnquanto(Trem_y >= 0){
            se(Trem_y <-> 0){
                Imprima("x=", Trem_x, " terminou o lacoEnquanto!\n");
            }
            senao{
                #uai... pra saber se é par sem usar %, divide por 2 e confere se o resultado * 2 é igual ...so#
                inteiro Trem_metade -> Trem_y / 2;
                se(Trem_metade * 2 <-> Trem_y){
                    Imprima("x=", Trem_x, " y=", Trem_y, " (par)\n");
                }
                senao{
                    Imprima("x=", Trem_x, " y=", Trem_y, " (impar)\n");
                }
            }
            Trem_y -> Trem_y - 1;
        }
    }
}
#uai... esse código mostra todos os encadeamentos possíveis: se dentro de se, para dentro de para, lacoDentroDePara e vice-versa ...so#

criar TesteEncadeamentos(){
    Imprima("=== Teste de se encadeado e funcoes ===\n");
    Imprima("Digite um numero: ");
    inteiro Trem_num -> Entrada();

    se(Trem_num > 0){
        Imprima("Numero positivo!\n");

        #uai... mesma lógica: checa se (Trem_num / 2) * 2 eh igual a Trem_num ...so#
        se((Trem_num / 2) * 2 <-> Trem_num){
            Imprima(Trem_num, " eh par!\n");
        }
        senao{
            Imprima(Trem_num, " eh impar!\n");
        }
    }
    ouSe(Trem_num < 0){
        Imprima("Numero negativo!\n");
    }
    senao{
        Imprima("Zero detectado!\n");
    }
}
criar Main(){
    inteiro Trem_somaTotal -> SomarMatriz();  
    Imprima("Soma total da matriz:\n ", Trem_somaTotal);

    ContagemComCondicoes();             
    TesteEncadeamentos();                
    inteiro Trem_valor;
}
Main();

  

## exemplo 2:
inteiro Trem_idade -> 18;<br>
inteiro Trem_pontuacao -> 85;<br>
decimal Trem_notaAluno1 -> 3.5;<br>
inteiro Trem_notaAluno2 -> 7;<br>
decimal Trem_mediaNotasMateriaX -> (((((Trem_notaAluno1^2) + (Trem_notaAluno2^2))*3)/4)-1);<br>
se(Trem_idade >= 18){<br>
    Imprima("\nMaior de idade");<br>
}<br>
senao{<br>
    Imprima("\nMenor de idade");<br>
}<br>
se(Trem_pontuacao >= 90){<br>
    Imprima("\nNota A");<br>
}<br>
ouSe(Trem_pontuacao >= 80 e Trem_pontuacao < 90){<br>
    Imprima("\nNota B");<br>
}<br>
ouSe(Trem_pontuacao >= 70 e Trem_pontuacao < 80){<br>
    Imprima("\nNota C");<br>
}<br>
senao{<br>
    Imprima("\nNota D");<br>
}<br>
se(Trem_mediaNotasMateriaX > 2){<br>
    Imprima("\nPassou com media", Trem_mediaNotasMateriaX);<br>
}<br>

## exemplo 3:

decimal Trem_limiteSaque -> 2000.00; <br> 
decimal Trem_transferencia -> 2.5;  <br>
inteiro Trem_maxTentativasSenha -> 3;  <br>

texto Trem_nome -> "Beatriz";  <br>
texto Trem_numeroConta -> "12345-6";  <br>
decimal Trem_saldo -> 16000.00;  <br>
inteiro Trem_senhaCorreta -> 1234;  <br>
verdadeiroFalso Trem_userLogado -> false;  <br>
inteiro Trem_tentativasSenha -> 0;  <br>
Imprima("bem vindo\n");  <br>

lacoEnquanto(!Trem_userLogado e Trem_tentativasSenha < Trem_maxTentativasSenha){<br>
    texto Trem_senha;<br>

    Imprima("Digite a senha");

    inteiro Trem_senhaDigitada -> Entrada();

    se(Trem_senhaDigitada <-> Trem_senhaCorreta){

    Trem_userLogado -> true;

        Imprima("login realizado\n");

    }

    senao{

        Trem_tentativasSenha -> Trem_tentativasSenha + 1;

        inteiro Trem_result ->Trem_maxTentativasSenha -  Trem_tentativasSenha;

        Imprima("senha incorreta, tentativas restantes\n",Trem_result);

        se(Trem_tentativasSenha >= Trem_maxTentativasSenha){

            Imprima("conta bloqueada por excesso de tentativas");

        }

    }
}<br>

verdadeiroFalso Trem_sistemaAtivo -> true;  <br>
lacoEnquanto(Trem_sistemaAtivo e Trem_userLogado){ <br> 
    Imprima(" MENU PRINCIPAL\n");  <br>
    Imprima("\nCliente: ", Trem_nome);  <br>
    Imprima("\nConta: ", Trem_numeroConta);  <br>
    Imprima("\nSaldo: R$ ",Trem_saldo);  <br>
    Imprima("\n1 - Saque");   <br>
    Imprima("\n0 - Sair");  <br>
    Imprima("\nEscolha uma opcao:");  <br>
    Imprima("\nDigite a opcao");<br>
    inteiro Trem_opcao ->Entrada();  <br>
    se(Trem_opcao <-> 1){  <br>
    Imprima("\nDigite o valor do saque");  <br>
    decimal Trem_valorSaque -> Entrada();  <br>
    se(Trem_valorSaque > 0 e Trem_valorSaque <= Trem_saldo){  <br>
        se(Trem_valorSaque <= Trem_limiteSaque){  <br>
            Trem_saldo -> Trem_valorSaque - Trem_saldo;  <br>
            Imprima("Saque  realizado com sucesso no valor de R$ ", Trem_valorSaque);  <br>
            Imprima("Novo saldo: R$ ", Trem_saldo);  <br>
        }  <br>
        senao{  <br>
            Imprima("Valor excede o limite de saque de R$", Trem_limiteSaque);  <br>
        }  <br>
    } <br>
    }<br>
     ouSe(Trem_opcao <-> 0){<br>
        Imprima("tchau");<br>
        Trem_sistemaAtivo -> false; <br>
    }<br>   
} <br>
  


## Exemplo 4:

Imprima("---------requisitos do projeto realizados em codigo----------\n");<br>
inteiro Trem_contador -> 1;<br>
inteiro Trem_soma -> 0;<br>
verdadeiroFalso Trem_v -> true;<br>
verdadeiroFalso Trem_f -> false;<br>
decimal Trem_d -> 1.5;<br>
inteiro Trem_i2 -> 1+3;<br>
texto Trem_n -> "Digite um numero";<br>
Imprima(Trem_n);<br>
inteiro Trem_input -> Entrada();<br>
decimal  Trem_grande -> ((Trem_i2/ 2) + (Trem_d * 8)) -1;<br>
Imprima("\n Antes da potencia:",Trem_grande);<br>
decimal Trem_expressao -> Trem_grande^ 3; <br>
Imprima("\n expressao matematica grande\n ",Trem_expressao);<br>

Imprima("\n ---Contagem crescente: (evidencia para)---\n");<br>
para(inteiro Trem_i -> 1; Trem_i <= 5; Trem_i++){<br>
   Imprima("Numero: ", Trem_i);<br>
}<br>

Imprima("\n ---verifica se m-níumero é par (evidencia ifs encadeados e função)---\n");<br>
criar VerificarPar(inteiro Trem_input){<br>
   inteiro Trem_resto -> Trem_input - ((Trem_input / 2) * 2);<br>
   se (Trem_input < 20){<br>
      Imprima("\n numero digitado eh menor que 20 entao verificarei se é par");<br>
      se(Trem_resto <-> 0){<br>
         Imprima("\n eh par ", Trem_input, "\n");<br>
     }<br>
      senao{<br>
         Imprima("\n nao eh par ", Trem_input, "\n");<br>
      }<br>
   }<br>
   ouSe(Trem_input > 20){<br>
      Imprima("maior que 20, nao farei conta para ver se é par");<br>
   }<br>
     retorna true;<br>
}<br>
VerificarPar(Trem_input);<br>

Imprima("\n---soma dos primeiros 10 numeros (evidencia lacoEnquanto)---\n");<br>

lacoEnquanto(Trem_contador <= 10){<br>
   Trem_soma -> Trem_soma + Trem_contador;<br>
   Trem_contador -> Trem_contador + 1;<br>
}<br>
Imprima("\nSoma dos primeiros 10 números: ", Trem_soma);<br>

Imprima("\n---tabuada(evidencia lacos encadeados)---\n");<br>
criar TabuadaCompleta(){<br>
    para(inteiro Trem_tabuada -> 1; Trem_tabuada <= 10; Trem_tabuada++){<br>
        Imprima("\nTabuada do ", Trem_tabuada, ":");<br>
        para(inteiro Trem_multiplicador -> 1; Trem_multiplicador <= 10; Trem_multiplicador++){<br>
            inteiro Trem_resultado -> Trem_tabuada * Trem_multiplicador;<br>
            Imprima(Trem_tabuada, " x ", Trem_multiplicador, " = ", Trem_resultado);<br>
        }<br>
    }<br>
}<br>
TabuadaCompleta();<br>

# Tradução equivalente para Java:

## Exemplo 1:

import java.util.Scanner;<br>
public class CodigoTraduzido {<br>
static Scanner scanner = new Scanner(System.in);<br>
public static void main(String[] args) {<br>
Main(); <br>
}<br>

public static int SomarMatriz(){ <br>
int Trem_soma = 0; <br>
for(int Trem_linha = 1;Trem_linha<=3;Trem_linha++){for(int Trem_coluna = 1;Trem_coluna<=3;Trem_coluna++){Trem_soma = Trem_soma+(Trem_linha*Trem_coluna); <br>
System.out.print("Linha " + Trem_linha + " Coluna " + Trem_coluna + " Valor \n" + Trem_linha*Trem_coluna); <br>
}<br>
}<br>
return Trem_soma; <br>
} <br>
public static void ContagemComCondicoes(){ <br>
for(int Trem_x = 1;Trem_x<=5;Trem_x++){int Trem_y = Trem_x; <br>
while(Trem_y>=0){if(Trem_y==0){System.out.print("x=" + Trem_x + " terminou o lacoEnquanto!\n"); <br>
}<br>
else{int Trem_metade = Trem_y/2; <br>
if(Trem_metade*2==Trem_y){System.out.print("x=" + Trem_x + " y=" + Trem_y + " (par)\n"); <br>
}<br>
else{System.out.print("x=" + Trem_x + " y=" + Trem_y + " (impar)\n"); <br>
}<br>
}<br>
Trem_y = Trem_y-1; <br>
}<br>
}<br>
} <br>
public static void TesteEncadeamentos(){ <br>
System.out.print("=== Teste de se encadeado e funcoes ===\n"); <br>
System.out.print("Digite um numero: "); <br>
int Trem_num = scanner.nextInt(); <br>
if(Trem_num>0){System.out.print("Numero positivo!\n"); <br>
if((Trem_num/2)*2==Trem_num){System.out.print(Trem_num + " eh par!\n"); <br>
}<br>
else{System.out.print(Trem_num + " eh impar!\n"); <br>
}<br>
}<br>
else if(Trem_num<0){System.out.print("Numero negativo!\n"); <br>
}<br>
else{System.out.print("Zero detectado!\n"); <br>
}<br>
} <br>
public static void Main(){ <br>
int Trem_somaTotal = SomarMatriz(); <br>
System.out.print("Soma total da matriz:\n " + Trem_somaTotal); <br>
ContagemComCondicoes(); <br>
TesteEncadeamentos(); <br>
int Trem_valor; <br>
} <br>
}<br>

## Exemplo 2:

import java.util.Scanner;<br>
public class CodigoTraduzido {<br>
static Scanner scanner = new Scanner(System.in);<br>
public static void main(String[] args) {<br>
int Trem_idade = 18; <br>
int Trem_pontuacao = 85; <br>
double Trem_notaAluno1 = 3.5; <br>
int Trem_notaAluno2 = 7; <br>
double Trem_mediaNotasMateriaX = (((((Math.pow(Trem_notaAluno1,2))+(Math.pow(Trem_notaAluno2,2)))*3)/4)-1); <br>
if(Trem_idade>=18){System.out.print("\nMaior de idade"); <br>
}<br>
else{System.out.print("\nMenor de idade"); <br>
}<br>
if(Trem_pontuacao>=90){System.out.print("\nNota A"); <br>
}<br>
else if(Trem_pontuacao>=80&&Trem_pontuacao<90){System.out.print("\nNota B"); <br>
}<br>
else if(Trem_pontuacao>=70&&Trem_pontuacao<80){System.out.print("\nNota C"); <br>
}<br>
else{System.out.print("\nNota D"); <br>
}<br>
if(Trem_mediaNotasMateriaX>2){System.out.print("\nPassou com media" + Trem_mediaNotasMateriaX); <br>
}<br>
}<br>
}<br>

## Exemplo 3:

import java.util.Scanner;<br>
public class CodigoTraduzido {<br>
static Scanner scanner = new Scanner(System.in);<br>
public static void main(String[] args) {<br>
double Trem_limiteSaque = 2000.00; <br>
double Trem_transferencia = 2.5; <br>
int Trem_maxTentativasSenha = 3; <br>
String Trem_nome = "Beatriz"; <br>
String Trem_numeroConta = "12345-6"; <br>
double Trem_saldo = 16000.00; <br>
int Trem_senhaCorreta = 1234; <br>
boolean Trem_userLogado = false; <br>
int Trem_tentativasSenha = 0; <br>
System.out.print("bem vindo\n"); <br>

while(!Trem_userLogado&&Trem_tentativasSenha<Trem_maxTentativasSenha){String Trem_senha;

System.out.print("Digite a senha"); <br>
int Trem_senhaDigitada = scanner.nextInt(); <br>
if(Trem_senhaDigitada==Trem_senhaCorreta){Trem_userLogado = true; <br>
System.out.print("login realizado\n"); <br>
}<br>
else{Trem_tentativasSenha = Trem_tentativasSenha+1; <br>
int Trem_result = Trem_maxTentativasSenha-Trem_tentativasSenha; <br>
System.out.print("senha incorreta, tentativas restantes\n" + Trem_result); <br>
if(Trem_tentativasSenha>=Trem_maxTentativasSenha){System.out.print("conta bloqueada por excesso de tentativas");<br> 
}<br>
}<br>
}<br>
boolean Trem_sistemaAtivo = true; <br>
while(Trem_sistemaAtivo&&Trem_userLogado){System.out.print(" MENU PRINCIPAL\n"); <br>
System.out.print("\nCliente: " + Trem_nome); <br>
System.out.print("\nConta: " + Trem_numeroConta); <br>
System.out.print("\nSaldo: R$ " + Trem_saldo); <br>
System.out.print("\n1 - Saque"); <br>
System.out.print("\n0 - Sair"); <br>
System.out.print("\nEscolha uma opcao:"); <br>
System.out.print("\nDigite a opcao"); <br>
int Trem_opcao = scanner.nextInt(); <br>
if(Trem_opcao==1){System.out.print("\nDigite o valor do saque"); <br>
double Trem_valorSaque = scanner.nextDouble(); <br>
if(Trem_valorSaque>0&&Trem_valorSaque<=Trem_saldo){if(Trem_valorSaque<=Trem_limiteSaque){Trem_saldo = Trem_valorSaque-Trem_saldo; <br>
System.out.print("Saque  realizado com sucesso no valor de R$ " + Trem_valorSaque); <br>
System.out.print("Novo saldo: R$ " + Trem_saldo); <br>
}<br>
else{System.out.print("Valor excede o limite de saque de R$" + Trem_limiteSaque); <br>
}<br>
}<br>
}<br>
else if(Trem_opcao==0){System.out.print("tchau"); <br>
Trem_sistemaAtivo = false; <br>
}<br>
}<br>
}<br>
}<br>


## Exemplo 4:

import java.util.Scanner;<br>
public class CodigoTraduzido {<br>
static Scanner scanner = new Scanner(System.in);<br>
public static void main(String[] args) {<br>
System.out.print("---------requisitos do projeto realizados em codigo----------\n"); <br>
int Trem_contador = 1; <br>
int Trem_soma = 0; <br>
boolean Trem_v = true; <br>
boolean Trem_f = false; <br>
double Trem_d = 1.5; <br>
int Trem_i2 = 1+3; <br>
String Trem_n = "Digite um numero"; <br>
System.out.print(Trem_n); <br>
int Trem_input = scanner.nextInt(); <br>
double Trem_grande = ((Trem_i2/2)+(Trem_d*8))-1; <br>
System.out.print("\n Antes da potencia:" + Trem_grande); <br>
double Trem_expressao = Math.pow(Trem_grande,3); <br>
System.out.print("\n expressao matematica grande\n " + Trem_expressao); <br>
System.out.print("\n ---Contagem crescente: (evidencia para)---\n"); <br>
for(int Trem_i = 1;Trem_i<=5;Trem_i++){System.out.print("Numero: " + Trem_i); <br>
}<br>
System.out.print("\n ---verifica se um numero eh par (evidencia ifs encadeados e funcao)---\n"); <br>
VerificarPar(Trem_input); <br>
System.out.print("\n---soma dos primeiros 10 numeros (evidencia lacoEnquanto)---\n"); <br>
while(Trem_contador<=10){Trem_soma = Trem_soma+Trem_contador; <br>
Trem_contador = Trem_contador+1; <br>
}<br>
System.out.print("\nSoma dos primeiros 10 n meros: " + Trem_soma); <br>
System.out.print("\n---tabuada(evidencia lacos encadeados)---\n"); <br>
TabuadaCompleta(); <br>
}<br>

public static boolean VerificarPar(int Trem_input){ <br>
int Trem_resto = Trem_input-((Trem_input/2)*2); <br>
if(Trem_input<20){System.out.print("\n numero digitado eh menor que 20 entao verificarei se   par"); <br>
if(Trem_resto==0){System.out.print("\n eh par " + Trem_input + "\n"); <br>
}<br>
else{System.out.print("\n nao eh par " + Trem_input + "\n"); <br>
}<br>
}<br>
else if(Trem_input>20){System.out.print("maior que 20, nao farei conta para ver se   par"); <br>
}<br>
return true; <br>
} <br>
public static void TabuadaCompleta(){ <br>
for(int Trem_tabuada = 1;Trem_tabuada<=10;Trem_tabuada++){System.out.print("\nTabuada do " + Trem_tabuada + ":"); <br>
for(int Trem_multiplicador = 1;Trem_multiplicador<=10;Trem_multiplicador++){int Trem_resultado = Trem_tabuada*Trem_multiplicador; <br>
System.out.print(Trem_tabuada + " x " + Trem_multiplicador + " = " + Trem_resultado); <br>
}<br>
}<br>
}<br> 
}<br>
