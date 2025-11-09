import java.util.Scanner;
public class CodigoTraduzido {
static Scanner scanner = new Scanner(System.in);
public static void main(String[] args) {
double Trem_limiteSaque = 2000.00; 
double Trem_transferencia = 2.5; 
int Trem_maxTentativasSenha = 3; 
String Trem_nome = "Beatriz"; 
String Trem_numeroConta = "12345-6"; 
double Trem_saldo = 16000.00; 
int Trem_senhaCorreta = 1234; 
boolean Trem_userLogado = false; 
int Trem_tentativasSenha = 0; 
System.out.print("bem vindo\n"); 
while(Trem_tentativasSenha<Trem_maxTentativasSenha){String Trem_senha; 
System.out.print("Digite a senha"); 
int Trem_senhaDigitada = scanner.nextInt(); 
if(Trem_senhaDigitada==Trem_senhaCorreta){Trem_userLogado = true; 
System.out.print("login realizado\n"); 
}
else{Trem_tentativasSenha = Trem_tentativasSenha+1; 
int Trem_result = Trem_maxTentativasSenha-Trem_tentativasSenha; 
System.out.print("senha incorreta, tentativas restantes\n" + Trem_result); 
if(Trem_tentativasSenha>=Trem_maxTentativasSenha){System.out.print("conta bloqueada por excesso de tentativas"); 
}
}
}
boolean Trem_sistemaAtivo = true; 
while(Trem_sistemaAtivo&&Trem_userLogado){System.out.print(" MENU PRINCIPAL\n"); 
System.out.print("\nCliente: " + Trem_nome); 
System.out.print("\nConta: " + Trem_numeroConta); 
System.out.print("\nSaldo: R$ " + Trem_saldo); 
System.out.print("\n1 - Saque"); 
System.out.print("\n0 - Sair"); 
System.out.print("\nEscolha uma opcao:"); 
System.out.print("\nDigite a opcao"); 
int Trem_opcao = scanner.nextInt(); 
if(Trem_opcao==1){System.out.print("\nDigite o valor do saque"); 
double Trem_valorSaque = scanner.nextDouble(); 
if(Trem_valorSaque>0&&Trem_valorSaqueTrem<=Trem_saldo){if(Trem_valorSaque<=Trem_limiteSaque){Trem_saldo = Trem_valorSaque-Trem_saldo; 
System.out.print("Saque  realizado com sucesso no valor de R$ " + Trem_valorSaque); 
System.out.print("Novo saldo: R$ " + Trem_saldo); 
}
else{System.out.print("Valor excede o limite de saque de R$" + Trem_limiteSaque); 
}
}
}
else if(Trem_opcao==0){System.out.print("tchau"); 
Trem_sistemaAtivo = false; 
}
}
}
}
