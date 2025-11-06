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

- Fazer download do zip do projeto;
- abrimos no vsCode 
- rodamos Main.java e gera tokens e resultado "sintaticamente incorreto" ou "sintaticamente correto"

TERMINAR DE DESCREVER COM TRADUCAO + ARVORE E VER DE COMPILAR POR LINHA DE COMANDO !!!!!!!!!!!!!!!!!!!!!!!!!

# Características da linguagem criada

## Detalhes da gramática:

### Sobre declarar e atribuir (declaracao())

- só é possivel usar corretamente a "Entrada" se estivrmos declarando e atribuindo (para fazer a traducao certa) ex:decimal Trem_int2 -> Entrada();
- Quando declaramos e atribuímos (ao mesmo tempo) uma variável, podemos: <br>
- atribuir números (inteiro,decimal), booleano (true, false), identificadores <br>
- "expressoesMatematicas" de qualquer tamanho<br>
- "comparacoesBasicas" (comparar 2 coisas apenas, com quaquer operador, ex !Trem_a, 2 < 3, Trem_a ou Trem_b...), nao da para comparar muitas coisas ao mesmo tempo. <br>
- Nãoo da para misturar eles (ex: quando declarar, atribuir "expressoesMatematicas" e "comparacoesBasicas" (ex: verdadeiroFalso Trem_a -> (2.3 +4) > 2; verdadeiroFalso Trem_a -> (2.3 +4) <= Trem_b;))<br>

### Em condicao

- Ideia inicial era na glc de condicao ter (condicao), permitir "()" (condicao -> (condicao) | identificadores|negacaoCondicao|condicaoComparacoesBasicas (nao usado)), porem retiramos isso <br>
- "condicao" não faz chamada de metodo e nem ()<br>
- Da para escrever várias comparacoes (ex: se(Trem_a < 2 ou Trem_b <> Trem_c e Trem_d <-> 5){}), porém o resultado estara errado se fizer dessa forma pois não tem parenteses para determinar a ordem de comparações<br>
- Em "condicao" posso comparar varias coisas (expressoesmatematicas de qualquer tamanho com número/"expressoesMatematicas" de qualquer tamanho, identificadores...)<br>

## Sobre funcões:

- permiti na gramatica colocar varias coisas dentro da chamada de entrada mesmo que só vamos usar com () vazio e passando 1 argumento do usuario (n tava pensando no java qnd fiz)
- toda criacao de funcao fica fora da main e toda chamada de funcao fica dentro da main, poré, se eu chamar uma função dentro da criação de outra, ela aparece dentro da função.

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

# Exemplos de código na sua linguagem criada e a tradução equivalente.

## exemplo 1:

#uai... esse código mostra todos os encadeamentos possíveis: se dentro de se, para dentro de para, lacoDentroDePara e vice-versa ...so#

criar MostrarTabuada(inteiro Trem_n){<br>
    Imprima("=== Tabuada de ", Trem_n, " ===");<br>
    para(inteiro Trem_i -> 1; Trem_i <= 10; Trem_i++){<br>
        Imprima(Trem_n, " x ", Trem_i, " = ", Trem_n * Trem_i);<br>
    }<br>
}<br>

criar SomarMatriz(){<br>
    #uai... exemplo de para dentro de para ...so#<br>
    inteiro Trem_soma -> 0;<br>
    para(inteiro Trem_linha -> 1; Trem_linha <= 3; Trem_linha++){<br>
        para(inteiro Trem_coluna -> 1; Trem_coluna <= 3; Trem_coluna++){<br>
            Trem_soma -> Trem_soma + (Trem_linha * Trem_coluna);<br>
            Imprima("Linha ", Trem_linha, " Coluna ", Trem_coluna, " Valor ", Trem_linha * Trem_coluna);<br>
        }<br>
    }<br>
    retorna Trem_soma;<br>
}<br>

criar ContagemComCondicoes(){<br>
    #uai... exemplo de lacoEnquanto dentro de para e se encadeado ...so#<br>
    para(inteiro Trem_x -> 1; Trem_x <= 5; Trem_x++){<br>
        inteiro Trem_y -> Trem_x;<br>

        lacoEnquanto(Trem_y >= 0){

            se(Trem_y <-> 0){

                Imprima("x=", Trem_x, " terminou o lacoEnquanto!");

            }

            senao{

                #uai... pra saber se é par sem usar %, divide por 2 e confere se o resultado * 2 é igual ...so#

                inteiro Trem_metade -> Trem_y / 2;

                se(Trem_metade * 2 <-> Trem_y){

                    Imprima("x=", Trem_x, " y=", Trem_y, " (par)");

                }

                senao{

                    Imprima("x=", Trem_x, " y=", Trem_y, " (ímpar)");

                }

            }

            Trem_y -> Trem_y - 1;

        }

    }
}<br>

criar TesteEncadeamentos(){<br>
    Imprima("=== Teste de se encadeado e funções ===");<br>
    inteiro Trem_num;<br>
    Entrada("Digite um número: ", Trem_num);<br>

    se(Trem_num > 0){

        Imprima("Número positivo!");

        #uai... mesma lógica: checa se (Trem_num / 2) * 2 é igual a Trem_num ...so#

        se((Trem_num / 2) * 2 <-> Trem_num){

            Imprima("E também é par!");

        }

        senao{

            Imprima("Mas é ímpar!");

        }

    }

    ouSe(Trem_num < 0){

        Imprima("Número negativo!");

    }

    senao{

        Imprima("Zero detectado!");
        
    }

}<br>

criar Main(){<br>

    Imprima("=== Programa MinasScript ===");

    inteiro Trem_somaTotal -> SomarMatriz();   #uai... chama função com para dentro de para ...so#

    Imprima("Soma total da matriz: ", Trem_somaTotal);

    ContagemComCondicoes();               #uai... usa para + lacoEnquanto + se encadeado ...so#

    TesteEncadeamentos();                 #uai... usa se dentro de se ...so#


    inteiro Trem_valor;

    Entrada("Digite um número para ver a tabuada: ", Trem_valor);

    MostrarTabuada(Trem_valor);           #uai... chama função simples com para ...so#

    Imprima("=== Fim do programa ===");

}<br>

Main();<br>

## exemplo 2:

inteiro Trem_idade -> 18;<br>
inteiro Trem_pontuacao -> 85;<br>
decimal Trem_notaAluno1 -> 3.5;<br>
inteiro Trem_notaAluno2 -> 7;<br>
Decimal Trem_mediaNotasMateriaX -> (((((Trem_notaAluno1^2) + (Trem_notaAluno2^2))*3)/4)-1);<br>

se(Trem_idade >= 18){<br>
    Imprima("Maior de idade");<br>
}<br>
senao{<br>
    Imprima("Menor de idade");<br>
}<br>

se(Trem_pontuacao >= 90){<br>
    Imprima("Nota A");<br>
}<br>
ouSe(Trem_pontuacao >= 80 e Trem_pontuacao < 90){<br>
    Imprima("Nota B");<br>
}<br>
ouSe(Trem_pontuacao >= 70 e Trem_pontuacao < 80){<br>
    Imprima("Nota C");<br>
}<br>
senao{<br>
    Imprima("Nota D");<br>
}<br>

se(Trem_mediaNotasMateriaX > 2){<br>
    Imprima("Passou com media", Trem_mediaNotasMateriaX);<br>
}<br>

## exemplo 3:

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
        Imprima(Trem_x);<br>
    }<br>
    Imprimir(Trem_inteiro);<br>
}<br>
senao{<br>
    retorna false;<br>
}<br>

decimal Trem_limiteSaque -> 2000.00;<br>
decimal Trem_transferencia -> 2.5;<br>
inteiro Trem_maxTentativasSenha -> 3;<br>

criar Main(){<br>
    texto Trem_nome -> "Beatriz";<br>
    texto Trem_numeroConta -> "12345-6";<br>
    decimal Trem_saldo -> 16000.00;<br>
    texto Trem_senhaCorreta -> "1234";<br>
    verdadeiroFalso Trem_userLogado -> false;<br>
    inteiro Trem_tentativasSenha -> 0;<br>
    Imprima("bem vindo");<br>

    lacoEnquanto(!Trem_userLogado e Trem_tentativasSenha < Trem_maxTentativasSenha){

        texto Trem_senha;

        Imprima("Digite a senha", Trem_senhaDigitada);

        se(Trem_senhaDigitada <-> Trem_senhaCorreta){

        Trem_userLogado -> true;

           Imprima("login realizado");

        }
    
        senao{

            Trem_tentativasSenha -> Trem_tentativasSenha + 1;

            Trem_result ->Trem_maxTentativasSenha -  Trem_tentativasSenha;

            Imprima("senha incorreta, tentativas restantes",Trem_result);

            se(Trem_tentativasSenha >= Trem_maxTentativasSenha){

                Imprima("conta bloqueada por excesso de tentativas");

            }

        }

    }

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
                Imprima("Novo saldo: R$ ", Trem_saldo);<br>
            }<br>
            senao{<br>
                Imprima("Valor excede o limite de saque de R$", Trem_limiteSaque);<br>
            }<br>
        }<br>
    }<br>
}<br>
}<br>


# Tradução equivalente para Java: