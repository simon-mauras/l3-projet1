public class HuffmanTree implements Comparable<HuffmanTree> {
    
    private ArbreBinaire<Character> tree;
    private int frequency;
    
    public HuffmanTree(char c, int f) {
	tree = new ArbreBinaire<Character>(c);
	frequency = f;
    }

    public HuffmanTree(HuffmanTree a, HuffmanTree b) {
	tree = new ArbreBinaire<Character>(a.getTree(), b.getTree(), '_');
	frequency = a.getFrequency() + b.getFrequency();
    }

    public int getFrequency() {
	return this.frequency;
    }

    public ArbreBinaire<Character> getTree() {
	return this.tree;
    }

    public int compareTo(HuffmanTree h) {
	return frequency - h.getFrequency();
    }
}