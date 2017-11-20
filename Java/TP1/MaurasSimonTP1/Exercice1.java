import java.util.Scanner;

public class Exercice1 {
    
    private static Scanner sc;

    private static boolean isPrime(long n) {
	boolean res = true;
	for (int i=2; i*i<=n; i++)
	    if (n % i == 0)
		res = false;
	return res;
    }
    
    private static void q_1_1() {
	long n = sc.nextInt();
	if (isPrime(n))
	    System.out.println("Est premier");
	else 
	    System.out.println("N'est pas premier");
	
	if (isPrime(10)) System.out.println("Erreur !!");
	if (!isPrime(101)) System.out.println("Erreur !!");
	if (isPrime(81)) System.out.println("Erreur !!");
    }

    private static long[] factor(long n) {
	int taille = (int)Math.ceil(Math.log(n)/Math.log(2));
	long[] res = new long[taille];
	for (int i=0; i<taille; i++) res[i] = 1;

	int indice = 0;
	long diviseur = 2;
	while (n > 1 && diviseur * diviseur <= n) {
	    if (n % diviseur == 0) {
		n /= diviseur;
		res[indice++] = diviseur;
	    } else {
		diviseur++;
	    }
	}
	if (n > 1) res[indice] = n;
				   
	return res;
    }
    private static void printIntList(long[] t) {
	System.out.print(t[0]);
	for (int i=1; i<t.length; i++) {
	    System.out.print(" * ");
	    System.out.print(t[i]);
	}
    }
    
    private static void printFactorization(long n) {
	long[] f = factor(n);
	for (int i=0; i<f.length && f[i] != 1;) {
	    int j = i;
	    while (j < f.length && f[j] == f[i]) j++;
	    
	    System.out.print(f[i]);
	    if (i != j-1) {
		System.out.print(" ^ ");
		System.out.print(j-i);
	    }
	    if (j < f.length && f[j] != 1)
		System.out.print(" * ");
	    i = j;
	}
	System.out.println();
    }

    private static void q_1() {
	printFactorization(sc.nextInt());
	printFactorization(27);
	printFactorization(25008);
	printFactorization(1900988);
	printFactorization(87178291199l);
    }

    public static void main(String[] args) {
	sc = new Scanner(System.in);

	//q_1_1();
	q_1();
    }
}