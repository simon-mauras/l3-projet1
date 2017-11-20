import java.lang.Comparable;

public class Point implements Comparable<Point> {
    
    private double x, y;

    public Point() {
	this.x = 0;
	this.y = 0;
    }
    
    public Point(double _x, double _y) {
	this.x = _x;
	this.y = _y;
    }

    public void setCoords(double _x, double _y) {
	this.x = _x;
	this.y = _y;
    }

    public double getX() {
	return this.x;
    }
    
    public double getY() {
	return this.y;
    }
    
    public int compareTo(Point p) {
	if (p.x != this.x) // Comparison between doubles ?
	    return (p.x < this.x) ? 1 : -1;
	return (p.y < this.y) ? 1 : -1;
    }
}