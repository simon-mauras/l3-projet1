import java.io.*;
import java.util.Scanner;

public class Test {
    
    public static void conway(String conwayEntree,
			      String conwaySortie)
	throws IOException {
	try {
	    File entree = new File (conwayEntree);
	    Scanner sc = new Scanner(entree);
	    int n = sc.nextInt();
	    sc.close();
	
	    String c = "1";
	    while (n-- > 0) {
		String tmp = "";
		for (int i=0; i<c.length(); ) {
		    int j = i;
		    while (j<c.length()
			   && c.charAt(i) == c.charAt(j)) j++;
		    tmp += String.valueOf(j-i);
		    tmp += c.charAt(i);
		    i = j;
	        }
		c = tmp;
	    }
	    
	    File sortie = new File(conwaySortie);
	    FileWriter fWriter = new FileWriter(sortie);
	    PrintWriter pWriter = new PrintWriter(fWriter);
	    pWriter.println(c);
	    pWriter.close();
	} catch (FileNotFoundException c) {
	    System.out.println("Fichier d'entrée inexistant");
	}
    }

    public static void main(String[] args) throws IOException {
	Auteur a = new Auteur("George", "Martin");
	a.setEmail("george.martin@email.com");
	a.setAge(66);

	Livre l = new Livre("A game of thrones", a);
	try {
	    l.imprimer(10);
	    l.vendre(4);
	    l.imprimer(6);
	    l.vendre(10);
	    System.out.println(l.getNbVendus());
	    l.vendre(4); // Exception...
	    System.out.println("Ne sera pas execute...");
	} catch (StockVideException e) {
	    System.out.println("Truc muche");
	}

	System.out.println(l.getNbVendus());
	System.out.println("------------------------");
	
	Dictionnaire d = new Dictionnaire("Francais");
	d.setNbMots(50000);
	System.out.println(d.toString());
	System.out.println("------------------------");
	
	conway("conway.in", "conway.out");
    }
    
}
