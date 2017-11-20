import java.util.Scanner;

public class Exercice0 {

    private static Scanner sc;

    private static void q_0_1() {
	int n = sc.nextInt();
	int m = sc.nextInt();
	System.out.println(n*m);
    }

    private static void q_0_2() {
	int n = sc.nextInt();
	int res = 1;
	for (int i=1; i<=n; i++)
	    res = res * i;
	System.out.println(res);
    }

    private static void q_0_3() {
	String s = sc.nextLine();
	int res = 0;
	for (int i=0; i<s.length(); i++)
	    if (s.charAt(i) == 'a')
		res++;
	
	System.out.println(res);
	System.out.println(s.length());
    }
        
    private static void q_0_4() {
	int n = sc.nextInt();
	switch (n) {
	case 0:
	    System.out.println("Zero");
	    break;
	case 1:
	    System.out.println("Un");
	    break;
	case 2:
	    System.out.println("Deux");
	    break;
	case 3:
	    System.out.println("Trois");
	    break;
	case 4:
	    System.out.println("Quatre");
	    break;
	case 5:
	    System.out.println("Cinq");
	    break;
	case 6:
	    System.out.println("Six");
	    break;
	case 7:
	    System.out.println("Sept");
	    break;
	case 8:
	    System.out.println("Huit");
	    break;
	case 9:
	    System.out.println("Neuf");
	    break;
	case 10:
	    System.out.println("Dix");
	    break;
	default:
	    System.out.println("Nombre trop grand...");
	    break;
	}
    }
    
    public static void main(String[] args) {
	sc = new Scanner(System.in);
	
	//q_0_1();
	//q_0_2();
	//q_0_3();
	//q_0_4();
    }
}