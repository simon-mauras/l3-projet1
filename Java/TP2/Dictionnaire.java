public class Dictionnaire extends Livre {
    private String langue;
    private int nbMots;

    public Dictionnaire() {
	super();
	langue = "";
	nbMots = 0;
    }
    
    public Dictionnaire(String l) {
	super("Dictionnaire de " + l, new Auteur());
	langue = l;
	nbMots = 0;
    }
    
    public void setNbMots(int n) {
	nbMots = n;
    }
}
