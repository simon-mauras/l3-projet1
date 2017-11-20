import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ListIterator;
import java.util.ArrayList;

public class Display extends JPanel {
    
    private Frame frame;
    private Diagram diag;
    private double scale;
    private double x0;
    private double y0;

    public Display() {
	super();
	scale = 1;
	x0 = 0;
	y0 = 0;
	ZoomListener zoom = new ZoomListener();
	DragListener drag = new DragListener(); 
        addMouseWheelListener(zoom);
        addMouseListener(drag);
        addMouseMotionListener(drag);
    }
    
    public void paintComponent(Graphics g) {
	g.setColor(Color.RED);
	for (int i=0; i<diag.centers.length; i++) {
	    double x = this.getWidth()/2 + scale * (diag.centers[i].getX() - x0);
	    double y = this.getHeight()/2 - scale * (diag.centers[i].getY() - y0);
	    g.drawOval((int)x-3, (int)y-3, 6, 6);
	}
	g.setColor(Color.BLACK);
	for (int i=0; i<diag.points.length; i++) {
	    double x = this.getWidth()/2 + scale * (diag.points[i].getX() - x0);
	    double y = this.getHeight()/2 - scale * (diag.points[i].getY() - y0);
	    g.drawLine((int)x-2, (int)y-2, (int)x+2, (int)y+2);
	    g.drawLine((int)x+2, (int)y-2, (int)x-2, (int)y+2);
	}

	if (diag.displayCenters && diag.centers.length > 0) {
	    g.setColor(Color.RED);
	    for (int i=0; i<diag.points.length; i++) {
		double dMin = Double.POSITIVE_INFINITY;
		int ind = -1;
		for (int j=0; j<diag.centers.length; j++) {
		    double d = (diag.centers[j].getX() - diag.points[i].getX())
			* (diag.centers[j].getX() - diag.points[i].getX())
			+ (diag.centers[j].getY() - diag.points[i].getY())
			* (diag.centers[j].getY() - diag.points[i].getY());
		    if (d < dMin) {
			ind = j;
			dMin = d;
		    }
		}
		double xa = this.getWidth()/2 + scale*(diag.points[i].getX() - x0);
		double ya = this.getHeight()/2 - scale*(diag.points[i].getY() - y0);
		double xb = this.getWidth()/2 + scale*(diag.centers[ind].getX() - x0);
		double yb = this.getHeight()/2 - scale*(diag.centers[ind].getY() - y0);
		g.drawLine((int)xa, (int)ya, (int)xb, (int)yb);
	    }
	}

	if (diag.displayDelaunay) {
	    g.setColor(Color.BLUE);
	    for (int i=0; i<diag.centers.length; i++) {
		ListIterator<Integer> it = diag.triangles.get(i).listIterator();
		double x1 = this.getWidth()/2 + scale * (diag.centers[i].getX() - x0);
		double y1 = this.getHeight()/2 - scale * (diag.centers[i].getY() - y0);
		while (it.hasNext()) {
		    int j = it.next();
		    double x2 = this.getWidth()/2 + scale * (diag.centers[j].getX() - x0);
		    double y2 = this.getHeight()/2 - scale * (diag.centers[j].getY() - y0);
		    g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
		}
	    }
	}

	if (diag.displayVoronoi) {
	    g.setColor(Color.RED);
	    ArrayList<Polygon> polygons =
		diag.computeVoronoi(x0 - this.getWidth() / 2 / scale,
				    x0 + this.getWidth() / 2 / scale,
				    y0 - this.getHeight() / 2 / scale,
				    y0 + this.getHeight() / 2 / scale);
	    for (int i=0; i<polygons.size(); i++) { 
		int[] x = new int[polygons.get(i).size()];
		int[] y = new int[polygons.get(i).size()];
		ListIterator<Point> it = polygons.get(i).getPoints().listIterator();
		for (int j=0; j<polygons.get(i).size(); j++) {
		    Point act = it.next();
		    x[j] = (int)(this.getWidth()/2 + scale*(act.getX() - x0));
		    y[j] = (int)(this.getHeight()/2 - scale*(act.getY() - y0));
		}
		g.drawPolygon(x, y, polygons.get(i).size());
	    }
	}
    }

    public void setDiagram(Diagram d) {
	diag = d;
    }

    public void setFrame(Frame f) {
	this.frame = f;
    }

    class ZoomListener implements MouseWheelListener {
	public void mouseWheelMoved(MouseWheelEvent e) {
	    int s = e. getWheelRotation();
	    for (int i=0; i<s; i++)
		scale *= 1.05;
	    for (int i=s; i<0; i++)
		scale /= 1.05;
	    frame.repaint();
	}
    }

    class DragListener implements MouseListener, MouseMotionListener {

	private int x, y;

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
	    x = e.getX();
	    y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
	    int _x = e.getX();
	    int _y = e.getY();
	    x0 += (x - _x) / scale;
	    y0 -= (y - _y) / scale;
	    x = _x;
	    y = _y;
	    frame.repaint();
	}
    }
}
