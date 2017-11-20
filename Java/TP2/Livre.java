public class Livre {

    private String titre;
    private Auteur auteur;
    private double prix;
    private int enStock;
    private int nbVendus;
    
    public Livre() {
	titre = "";
	auteur = new Auteur();
	prix = 0;
	enStock = nbVendus = 0;
    }
    
    public Livre(String t, Auteur a) {
	titre = t;
	auteur = a;
	prix = 0;
	enStock = nbVendus = 0;
    }
    
    public int getNbVendus() {
	return nbVendus;
    }

    public String toString() {
	return titre + ", " + auteur.getName();
    }

    public boolean enStock(int n) {
	return enStock >= n;
    }
    
    public void imprimer(int n) {
	enStock += n;
    }

    public void vendre(int n) throws StockVideException {
	if (!enStock(n)) {
	    vendre(enStock);
	    throw new StockVideException();
	}
	enStock -= n;
	nbVendus += n;
    }
}
