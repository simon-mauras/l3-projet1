import java.io.File;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel extends JPanel {
    
    private Frame frame;
    private Diagram diag;
    private JButton buttonLoad;
    private JTextField textfieldK;
    private JLabel labelK;
    private JButton buttonInit;
    private JButton buttonKMeans;
    private JCheckBox buttonCenters;
    private JCheckBox buttonDelaunay;
    private JCheckBox buttonVoronoi;

    public Panel() {
	super();
	this.buttonLoad = new JButton("Charger un fichier");
	this.labelK = new JLabel("K = ");
	this.textfieldK = new JTextField("0", 3);
	this.buttonInit = new JButton("K centres aléatoires");
	this.buttonKMeans = new JButton("K-Moyennes");
	this.buttonCenters = new JCheckBox("Centres");
	this.buttonDelaunay = new JCheckBox("Delaunay");
	this.buttonVoronoi = new JCheckBox("Voronoï");
	
	buttonLoad.addActionListener(new FilechooserListener());
	buttonInit.addActionListener(new InitListener());
	buttonKMeans.addActionListener(new KMeansListener());
	CheckBoxListener l = new CheckBoxListener();
	buttonCenters.addActionListener(l);
	buttonDelaunay.addActionListener(l);
	buttonVoronoi.addActionListener(l);

	this.setLayout(new FlowLayout());
	this.add(this.buttonLoad);
	this.add(this.labelK);
	this.add(this.textfieldK);
	this.add(this.buttonInit);
	this.add(this.buttonKMeans);
	this.add(this.buttonCenters);
	this.add(this.buttonDelaunay);
	this.add(this.buttonVoronoi);
    }
    
    public void paintComponent(Graphics g) {
	g.setColor(Color.LIGHT_GRAY);
	int h = this.getSize().height;
	int w = this.getSize().width;
	g.fillRect(0, 0, w, h);
    }

    public void setDiagram(Diagram d) {
	this.diag = d;
	buttonCenters.setSelected(diag.displayCenters);
	buttonVoronoi.setSelected(diag.displayVoronoi);
	buttonDelaunay.setSelected(diag.displayDelaunay);
    }
    
    public void setFrame(Frame f) {
	this.frame = f;
    }

    class FilechooserListener implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
	    JFileChooser dialogue = new JFileChooser(new File("."));
	    if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		diag.loadFile(dialogue.getSelectedFile());
	    }
	    if (frame != null)
		frame.repaint();
	}
    }

   class InitListener implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
	    try {
		int k = Integer.parseInt(textfieldK.getText());
		diag.chooseCenters(k < 0 ? 0 : k);
		diag.computeDelaunay();
		if (frame != null)
		    frame.repaint();
	    } catch (Exception exc) { }
	}
    }

   class KMeansListener implements ActionListener {
       public Timer timer;
       public void actionPerformed(ActionEvent e) {
	   ActionListener reload = new ActionListener() {
		   public void actionPerformed(ActionEvent evt) {
		       if (diag.kMeansIteration() < 1e-2) {
			   timer.stop();
			   buttonKMeans.setEnabled(true);
		       }
		       diag.computeDelaunay();
		       frame.repaint();
		   }
	       };
	   
	   if (diag.centers != null && diag.centers.length > 0) {
	       buttonKMeans.setEnabled(false);
	       timer = new Timer(1000, reload);
	       timer.start();
	   }
       }
   }

   class CheckBoxListener implements ActionListener {
       public void actionPerformed(ActionEvent e) {
	   diag.displayCenters = buttonCenters.isSelected();
	   diag.displayVoronoi = buttonVoronoi.isSelected();
	   diag.displayDelaunay = buttonDelaunay.isSelected();
	   frame.repaint();
       }
   }
}
