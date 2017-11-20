public class Auteur {

    private String nom;
    private String prenom;
    private String email;
    private int age;

    public Auteur() {
	nom = "";
	prenom = "";
	email = "";
	age = 0;
    }

    public Auteur(String p, String n) {
	prenom = p;
	nom = nom;
    }

    public String toString() {
	return prenom + " "
	    + nom + ","
	    + email + "("
	    + age + " ans)";
    }
    
    public void setEmail(String e) {
	email = e;
    }

    public void setAge(int a) {
	age = a;
    }
    
    public String getName() {
	return prenom + " " + nom;
    }
}
