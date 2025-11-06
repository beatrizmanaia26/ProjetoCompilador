import java.util.Scanner;
public class CodigoTraduzido {
public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);
System.out.print("---------requisitos do projeto realizados em codigo----------\n"); 
int Trem_contador = 1; 
int Trem_soma = 0; 
boolean Trem_v = true; 
boolean Trem_f = false; 
double Trem_d = 12.5; 
int Trem_i2 = 4+3; 
String Trem_n = "Digite um numero"; 
System.out.print(Trem_n); 
int Trem_input = scanner.nextInt(); 
double Trem_expressao = (((Trem_i2/2)+(Trem_d*8))-1)Math.pow(1,3); 
System.out.print("expressao matematica grande " + Trem_expressao); 
System.out.print("---Contagem crescente: (evidencia para)---\n"); 
for(int Trem_i = 1;Trem_i<=5;Trem_i++){System.out.print("Número: " + Trem_i); 
}
System.out.print("---verifica se m-níumero é par (evidencia ifs encadeados e função)---\n"); 
VerificarPar(Trem_input); 
System.out.print("---soma dos primeiros 10 numeros (evidencia lacoEnquanto)---\n"); 
while(Trem_contador<=10){Trem_soma = Trem_soma+Trem_contador; 
Trem_contador = Trem_contador+1; 
}
System.out.print("Soma dos primeiros 10 números: " + Trem_soma); 
System.out.print("---tabuada(evidencia lacos encadeados)---\n"); 
TabuadaCompleta(); 
}


public static boolean VerificarPar(int Trem_input){ 
int Trem_resto = Trem_input-((Trem_input/2)*2); 
if(Trem_input<20){System.out.print("numero digitado eh menor que 20 entao verificarei se é par"); 
if(Trem_resto==0){System.out.print(" eh par " + Trem_input + "\n"); 
return Trem_v; 
}
else{System.out.print("nao eh par " + Trem_input + "\n"); 
return Trem_f; 
}
}
else if(Trem_input>20){System.out.print("maior que 20, nao farei conta para ver se é par"); 
}
} 
public static void TabuadaCompleta(){ 
for(int Trem_tabuada = 1;Trem_tabuada<=10;Trem_tabuada++){System.out.print("\nTabuada do " + Trem_tabuada + ":"); 
for(int Trem_multiplicador = 1;Trem_multiplicador<=10;Trem_multiplicador++){int Trem_resultado = Trem_tabuada*Trem_multiplicador; 
System.out.print(Trem_tabuada + " x " + Trem_multiplicador + " = " + Trem_resultado); 
}
}
} 
}
