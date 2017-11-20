import java.util.LinkedList;
import java.util.ListIterator;

public class Test {
    
    private static void printList(LinkedList<Integer> l) {
	ListIterator<Integer> it = l.listIterator();
	while (it.hasNext()) {
	    System.out.print(it.next().toString());
	    System.out.print(", ");
	}

	System.out.println();
    }

    public static void main(String[] args) {
	GrandNombre a = new GrandNombre(101010101);
	System.out.println(a.toString());
	a.multInt(99);
	System.out.println(a.toString());
	a.addInt(1);
	System.out.println(a.toString());
	
	GrandNombre f = new GrandNombre();
	f.setFactoriel(100);
	System.out.println(f.toString());
	ArbreBinaire<Integer> abr =
	    new ArbreBinaire<Integer>(new ArbreBinaire<Integer>(new ArbreBinaire<Integer>(4),
								new ArbreBinaire<Integer>(5), 2),
				      new ArbreBinaire<Integer>(new ArbreBinaire<Integer>(6),
								new ArbreBinaire<Integer>(7), 3), 1);
	printList(abr.parcoursLargeur());
	printList(abr.parcoursPrefixe());
    }
}
