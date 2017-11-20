import java.util.ListIterator;
import java.util.LinkedList;
import java.lang.Math;

public class Polygon {
    private LinkedList<Point> pts;
    private final double epsilon = 1e-8;
    
    public Polygon() {
	this.pts = new LinkedList<Point>();
    }
    
    public Polygon(LinkedList<Point> l) {
	this.pts = new LinkedList<Point>(l);
	this.pts.addLast(pts.getFirst());
    }

    public int size() {
	if (this.pts.size() > 0)
	    return this.pts.size() - 1;
	return 0;
    }

    public LinkedList<Point> getPoints() {
	return this.pts;
    }

    // ax + by < c
    public void intersect(double a, double b, double c) {
	ListIterator<Point> it = pts.listIterator();
	if (it.hasNext()) {
	    Point last = it.next();
	    while (it.hasNext()) {
		Point act = it.next();
		double _a = act.getY() - last.getY();
		double _b = last.getX() - act.getX();
		double _c = _a * act.getX() + _b * act.getY();
		double det = a * _b - _a * b;
		if (det != 0) { // Avoid NaN results, no need to use epsilon...
		    double x = (c * _b - _c * b) / det;
		    double y = (a * _c - _a * c) / det;
		    if (Math.min(act.getX(), last.getX()) < x + epsilon &&
			Math.max(act.getX(), last.getX()) > x - epsilon) {
			if (Math.min(act.getY(), last.getY()) < y + epsilon &&
			    Math.max(act.getY(), last.getY()) > y - epsilon) {
			    it.previous();
			    it.add(new Point(x, y));
			    it.next();
			}
		    }
		}
		last = act;
	    }

	}
	it = pts.listIterator();
	while (it.hasNext()) {
	    Point act = it.next();
	    // Epsilon -> Don't remove points we just add...
	    if (a * act.getX() + b * act.getY() > c + epsilon) {
		it.remove();
	    }
	}
	
	if (pts.peek() != null) {    
	    if (Math.abs(pts.getFirst().getX() - pts.getLast().getX()) > epsilon ||
		Math.abs(pts.getFirst().getY() - pts.getLast().getY()) > epsilon) {
		pts.addLast(pts.getFirst());
	    }
	}
    }
}