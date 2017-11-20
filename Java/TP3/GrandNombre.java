import java.util.LinkedList;
import java.util.ListIterator;

public class GrandNombre {
    
    private LinkedList<Integer> nb;

    public GrandNombre(int n) {
	nb = new LinkedList<Integer>();
	while (n > 0) {
	    nb.add(n % 10);
	    n /= 10;
	}
    }

    public GrandNombre() {
	this(0);
    }

    public String toString() {
	if (nb.size() == 0)
	    return "0";
	
	String res = "";
	ListIterator<Integer> i = nb.listIterator(nb.size());
	while(i.hasPrevious()) {
	    Integer act = i.previous();
	    res += act.toString();
	}

	return res;
    }
    
    public void addInt(int n) {
	
	int retenue = n;
	ListIterator<Integer> i = nb.listIterator();
	while (i.hasNext()) {
	    int tmp = i.next();
	    i.set((retenue + tmp) % 10);
	    retenue = (retenue + tmp) / 10; 
	}
	
	while (retenue > 0) {
	    nb.add(retenue % 10);
	    retenue /= 10;
	}
    }

    public void multInt(int n) {
	
	int retenue = 0;
	ListIterator<Integer> i = nb.listIterator();
	while (i.hasNext()) {
	    int tmp = i.next();
	    i.set((retenue + n*tmp) % 10);
	    retenue = (retenue + n*tmp) / 10; 
	}
	
	while (retenue > 0) {
	    nb.add(retenue % 10);
	    retenue /= 10;
	}
    }

    public void setFactoriel(int n) {
	nb.clear();
	nb.add(1);
	for (int i=2; i<=n; i++)
	    this.multInt(i);
    }
}
