import java.util.LinkedList;

public class ArbreBinaire<T> {
    
    private ArbreBinaire<T> filsGauche;
    private ArbreBinaire<T> filsDroit;
    private T valeur;
    
    ArbreBinaire() {
	filsDroit = null;
	filsGauche = null;
	//valeur = new T();
    }

    ArbreBinaire(T v) {
	filsDroit = null;
	filsGauche = null;
	valeur = v;
    }

    ArbreBinaire(ArbreBinaire<T> g, ArbreBinaire<T> d, T v) {
	filsDroit = d;
	filsGauche = g;
	valeur = v;
    }

    public ArbreBinaire<T> getFilsDroit() {
	return this.filsDroit;
    }

    public ArbreBinaire<T> getFilsGauche() {
	return this.filsGauche;
    }
    
    public T getValeur() {
	return this.valeur;
    }

    public int hauteur() {
	int d = (filsDroit == null) ? 0 : filsDroit.hauteur();
	int g = (filsGauche == null) ? 0 : filsGauche.hauteur();
	return (d < g) ? g : d;
    }
    
    public LinkedList<T> parcoursLargeur() {
	LinkedList<T> res = new LinkedList<T>();
	LinkedList<ArbreBinaire<T>> en_cours = new LinkedList<ArbreBinaire<T>>();
	en_cours.add(this);
	while (en_cours.peek() != null) {
	    ArbreBinaire<T> act = en_cours.poll();
	    if (act.getFilsGauche() != null)
		en_cours.add(act.getFilsGauche());
	    if (act.getFilsDroit() != null)
		en_cours.add(act.getFilsDroit());
	    res.add(act.getValeur());
	}
	return res;
    }

    public LinkedList<T> parcoursPrefixe() {
	LinkedList<T> res = new LinkedList<T>();
	parcoursPrefixe(res);
	return res;
    }

    public void parcoursPrefixe(LinkedList<T> l) {
	l.add(valeur);
	if (filsGauche != null) filsGauche.parcoursPrefixe(l);
	if (filsDroit != null) filsDroit.parcoursPrefixe(l);
    }
}
