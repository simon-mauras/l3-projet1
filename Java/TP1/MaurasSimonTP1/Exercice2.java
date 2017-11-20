import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Exercice2 {
    
    private static Scanner sc;

    private static int nbOccurences(String s, String p) {
	int res = 0;
	for (int i=0; i+p.length()<=s.length(); i++) {
	    boolean ok = true;
	    for (int j=0; j<p.length(); j++)
		if (s.charAt(i+j) != p.charAt(j))
		    ok = false;
	    if (ok) res++;
	}
	return res;
    }

    private static void q_2_1() {
	String s = sc.nextLine();
	String p = sc.nextLine();
	System.out.println(nbOccurences(s, p));
	
	if (nbOccurences("aabaaaa", "aa") != 5)
	    System.out.println("Erreur !!");
	if (nbOccurences("chaine petite",
			 "chaine beaucoup beaucoup beaucoup trop longue") != 0)
	    System.out.println("Erreur !!");
	if (nbOccurences("GATACCATAGACA", "GA?") != 1)
	    System.out.println("Erreur");
    }

    private static int nbOccurencesWithWildcard(String s, String p) {
	int res = 0;
	for (int i=0; i+p.length()<=s.length(); i++) {
	    boolean ok = true;
	    for (int j=0; j<p.length(); j++)
		if (s.charAt(i+j) != p.charAt(j)
		    && p.charAt(j) != '?')
		    ok = false;
	    if (ok) res++;
	}
	return res;
    }

    private static void q_2_2() {
	String s = sc.nextLine();
	String p = sc.nextLine();
	System.out.println(nbOccurencesWithWildcard(s, p));

	if (nbOccurencesWithWildcard("GATACAGACA", "GA?") != 2)
	    System.out.println("Erreur !!");
	if (nbOccurencesWithWildcard("AGCGATACA", "A?") != 3)
	    System.out.println("Erreur !!");
    }

    // O(n) en moyenne, O(nk) dans le pire des cas ("aaaaaaaaaa...aa")
    private static String maxPattern(String s, int k)
    {
	if (k > s.length()) {
	    System.out.println("k est trop petit...");
	    return "";
	} else {
	    int base = 256;
	    int prime = 1000003;
	    int hash = 0;
	    int pow = 1;
	    for (int i=k-1; i>=0; i--) {
		hash += (int)s.charAt(i) * pow;
		hash %= prime;
		pow = (pow * base) % prime;
	    }

	    int[] nbOccurences = new int[s.length()];
	    ArrayList<LinkedList<Integer>> hashtbl = new ArrayList<LinkedList<Integer>>(prime);
	    for (int i=0; i<prime; i++)
	    	hashtbl.add(new LinkedList<Integer>());
	    
	    int res = 0;
	    for (int i=0; i+k-1<s.length(); i++) {

		//System.out.println(hash);

		ListIterator<Integer> it = hashtbl.get(hash).listIterator();
		boolean trouve = false;
		
		while (!trouve && it.hasNext()) {
		    int ind = it.next();
		    boolean ok = true;
		    for (int j=0; j<k; j++)
		        if (s.charAt(i+j) != s.charAt(ind+j))
			    ok = false;
		    if (ok) {
			trouve = true;
			nbOccurences[ind]++;
			if (nbOccurences[ind] > nbOccurences[res])
			    res = ind;
		    }
		}
		
		if (!trouve) {
		    hashtbl.get(hash).add(i);
		    nbOccurences[i] = 1;
		    if (nbOccurences[i] > nbOccurences[res])
			res = i;
		}
		
		
		if (i+k<s.length()) 
		    hash = (hash * base
			    + base * prime - pow * (int)s.charAt(i)
			    + (int)s.charAt(i+k)) % prime;
		
	    }
	    return s.substring(res, res+k);
	}
    }

    private static void q_2_3() {
	
	String s = sc.nextLine();
	int k = sc.nextInt();
	System.out.println(maxPattern(s, k));

	if (!maxPattern("ababababababababababababababababababa", 5).equals("ababa"))
	    System.out.println("Erreur !");
	if (!maxPattern("111111111111111111111111111111111111111111111222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222333333333333333333333333333333333333333333333333333333333333333333344444444444444444444444444444444444444444444444444444444444444444555555555555555555555555555555555555555555555555566666666666666666666666666666666666666666666666666777777777777777777777777777777777777777788888888888888888888888888888888888888888888888888999999999999999999999999999999999", 20).equals("22222222222222222222"))
	    System.out.println("Erreur !");
    }
    
    public static void main(String[] args) {
	sc = new Scanner(System.in);	
	
	//q_2_1();
	//q_2_2();
	q_2_3();
    }
}
