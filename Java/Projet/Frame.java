import javax.swing.JFrame;
import java.awt.BorderLayout; 

public class Frame extends JFrame {
    
    private Diagram diag;
    private Panel pan;
    private Display dis;
    
    public Frame() {
	this.setTitle("Clustering");
	this.setSize(1000, 600);
	this.setLocationRelativeTo(null);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.pan = new Panel();
	this.pan.setFrame(this);
	this.dis = new Display();
	this.dis.setFrame(this);
	this.getContentPane().add(pan, BorderLayout.SOUTH);
	this.getContentPane().add(dis, BorderLayout.CENTER);
    }
    
    public void display() {
	this.setVisible(true);
    }
    
    public void setDiagram(Diagram d) {
	this.diag = d;
	this.pan.setDiagram(this.diag);
	this.dis.setDiagram(this.diag);
    }
}
