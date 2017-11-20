import java.io.*;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.ListIterator;

public class Huffman {

    private static String inputString;

    private static String outputString;
    
    private static int[] frequencies;
    
    private static String[] code;
    
    private static ArbreBinaire<Character> tree;
    
    private static PriorityQueue<HuffmanTree> q;

    private static void fromFile(String filename) throws IOException {
	inputString = "";
	try {
	    File in = new File(filename);
	    Scanner sc = new Scanner(in);
	    while (sc.hasNextLine())
		inputString += sc.nextLine() + "\n";
	    sc.close();
 	} catch (FileNotFoundException c) {
	    System.out.println("Fichier d'entrée inexistant");
	}
    }

    private static void computeFrenquencies() {
	frequencies = new int[26];
	for (int i=0; i<inputString.length(); i++) {
	    Character c = inputString.charAt(i);
	    if ('a' <= c && c <= 'z') {
		frequencies[(int)c - (int)'a']++;
	    }
	}
    }
    
    private static void buildTree() {	
     	q = new PriorityQueue<HuffmanTree>(); 
	for (int i=0; i<26; i++) {
	    if (frequencies[i] > 0)
		q.add(new HuffmanTree((char)('a' + i), frequencies[i]));
	}

	while (q.size() >= 2) {
	    HuffmanTree a = q.poll();
	    HuffmanTree b = q.poll();
	    q.add(new HuffmanTree(a, b));
	}
	
	if (q.size() >= 1)
	    tree = q.poll().getTree();
    }
    

    private static void chemin(ArbreBinaire<Character> a, LinkedList<String> l, String s) {
	if (a.getFilsGauche() == null && a.getFilsDroit() == null) {
	    l.add(a.getValeur() + s);
	}

	if (a.getFilsGauche() != null) chemin(a.getFilsGauche(), l, s + "0");
	if (a.getFilsDroit() != null) chemin(a.getFilsDroit(), l, s + "1");
    }

    private static void buildCode() {
	LinkedList<String> l = new LinkedList<String>();
	if (tree != null) chemin(tree, l, "");
	ListIterator<String> it = l.listIterator();
	code = new String[26];
	while (it.hasNext()) {
	    String tmp = it.next();
	    code[tmp.charAt(0) - 'a'] = tmp.substring(1);
	}
    }
    
    private static void translate() {
	outputString = "";
	for (int i=0; i<inputString.length(); i++) {
	    char c = inputString.charAt(i);
	    if ('a' <= c && c <= 'z')
		outputString += code[c - 'a'];
	}
    }

    private static void print() {
	//System.out.println(outputString);
	for (int i=0; i<26; i++)
	    System.out.println(code[i]);
    }
    
    public static void main(String[] args) {
	if (args.length == 1) {
	    try {
		fromFile(args[0]);
		computeFrenquencies();
		buildTree();
		buildCode();
		translate();
		print();
	    } catch (IOException e) {
		System.out.println("Error...");
	    }
	} else {
	    System.out.println("Wrong number of arguments :");
	    for (int i=0; i<args.length; i++)
		System.out.println(args[i]);
	}
    }
}