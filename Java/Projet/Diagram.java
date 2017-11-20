import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.lang.Math;

public class Diagram {

    public Point[] points;
    public Point[] centers;
    public ArrayList<LinkedList<Integer>> triangles;

    public boolean displayCenters;
    public boolean displayDelaunay;
    public boolean displayVoronoi;

    public Diagram() {
	displayCenters = true;
	displayDelaunay = false;
	displayVoronoi = true;
	points = new Point[0];
	centers = new Point[0];
	triangles = new ArrayList<LinkedList<Integer>>(0);
    }
    
    public void loadFile(File f) {
	try {
	    Scanner sc = new Scanner(f);
	    int n = sc.nextInt();
	    points = new Point[n];
	    for (int i=0; i<n; i++)
		points[i] = new Point(sc.nextInt(), sc.nextInt());
	    sc.close();
	    triangles = new ArrayList<LinkedList<Integer>>(n);
	    for (int i=0; i<n; i++)
		triangles.add(new LinkedList<Integer>());
	} catch (IOException e) {
	    System.out.println("Erreur...\n");
	}
    }
    
    public void chooseCenters(int k) {
	if (k > points.length)
	    k = points.length;
	Point tmp[] = new Point[points.length];
	for (int i=0; i<points.length; i++)
	    tmp[i] = points[i];

	Random rand = new Random();
	for (int i=0; i<tmp.length; i++) {
	    int ind = i + rand.nextInt(tmp.length - i);
	    if (0 > ind || ind >= tmp.length)
		System.out.println(ind);
	    Point p = tmp[i];
	    tmp[i] = tmp[ind];
	    tmp[ind] = p;
	}

	centers = new Point[k];
	for (int i=0; i<k; i++)
	    centers[i] = new Point(tmp[i].getX(), tmp[i].getY());
    }

    public double kMeansIteration() {
	int nb[] = new int[centers.length];
	double sumX[] = new double[centers.length];
	double sumY[] = new double[centers.length];

	for (int j=0; j<centers.length; j++) {
	    nb[j] = 0;
	    sumX[j] = sumY[j] = 0;
	}

	for (int i=0; i<points.length; i++) {
	    double minDist = Double.POSITIVE_INFINITY;
	    int ind = -1;
	    for (int j=0; j<centers.length; j++) {
		double d = (points[i].getX() - centers[j].getX())
		    * (points[i].getX() - centers[j].getX())
		    + (points[i].getY() - centers[j].getY())
		    * (points[i].getY() - centers[j].getY());
		if (d < minDist) {
		    minDist = d;
		    ind = j;
		}
	    }
	    nb[ind]++;
	    sumX[ind] += points[i].getX();
	    sumY[ind] += points[i].getY();
	}
	
	double res = 0;
	for (int j=0; j<centers.length; j++) {
	    double x = sumX[j] / (double)nb[j];
	    double y = sumY[j] / (double)nb[j];
	    double d = (x - centers[j].getX()) * (x - centers[j].getX())
		+ (y - centers[j].getY()) * (y - centers[j].getY());
	    if (d > res)
		res = d;
	    centers[j].setCoords(x, y);
	}
	
	computeDelaunay();

	return Math.sqrt(res);
    }
    
    private boolean inCircle(Point d, Point a, Point b, Point c) {
	double m11 = a.getX() - d.getX();
	double m21 = b.getX() - d.getX();
	double m31 = c.getX() - d.getX();
	double m12 = a.getY() - d.getY();
	double m22 = b.getY() - d.getY();
	double m32 = c.getY() - d.getY();
	double m13 = a.getX() * a.getX() - d.getX() * d.getX()
	    + a.getY() * a.getY() - d.getY() * d.getY();
	double m23 = b.getX() * b.getX() - d.getX() * d.getX()
	    + b.getY() * b.getY() - d.getY() * d.getY();
	double m33 = c.getX() * c.getX() - d.getX() * d.getX()
	    + c.getY() * c.getY() - d.getY() * d.getY();
	double determinant =
	    m11 * m22 * m33
	    + m21 * m32 * m13
	    + m12 * m23 * m31
	    - m31 * m22 * m13
	    - m21 * m12 * m33
	    - m11 * m32 * m23;
	return (determinant > 0);
    }
    
    private void computeTriangulation(int inf, int sup) {
	if (sup - inf <= 3) {
	    for (int i=inf; i<sup; i++)
		for (int j=inf; j<sup; j++)
		    if (i != j)
			triangles.get(i).add(j);
	} else {
	    computeTriangulation(inf, (inf+sup)/2);
	    computeTriangulation((inf+sup)/2, sup);

	    /* Graham algorithm
	    Angle tab[] = new Angle[sup - inf];
	    for (int i=inf; i<sup; i++) {
		tab[i - inf] = new Angle();
		tab[i - inf].id = i;
		tab[i - inf].angle = Math.atan2(centers[i].getY() - centers[inf].getY(),
						centers[i].getX() - centers[inf].getX());
		if (i == inf) tab[i - inf].angle = -100;
	    }
	    
	    Arrays.sort(tab);

	    Stack<Integer> hull = new Stack<Integer>();
	    hull.push(tab[0].id);
	    hull.push(tab[1].id);
	    int i = 2;
	    while (i < sup - inf) {
		int c = tab[i].id;
		int b = hull.pop();
		int a = hull.peek();
		double determinant = (centers[c].getX() - centers[a].getX())
		    * (centers[b].getY() - centers[a].getY())
		    - (centers[c].getY() - centers[a].getY())
		    * (centers[b].getX() - centers[a].getX());
		if (determinant < 0) {
		    hull.push(b);
		    hull.push(c);
		    i++;
		}
	    }

	    int baseLeft = -1, baseRight = -1;
	    while (true) {
		int a = hull.pop();
		int b = hull.peek();
		if (a >= (inf + sup)/2 && b < (inf + sup)/2) {
		    baseLeft = b;
		    baseRight = a;
		    triangles.get(b).add(a);
		    triangles.get(a).add(b);
		    break;
		}
	    }
	    */

	    int baseLeft = inf, baseRight = sup-1;
	    
	    for (int loop=0; loop<sup-inf; loop++) {
		boolean done = true;
		for (int i=inf; i<(inf+sup)/2; i++) {
		    double x1 = centers[baseLeft].getX() - centers[baseRight].getX();
		    double y1 = centers[baseLeft].getY() - centers[baseRight].getY();
		    double x2 = centers[i].getX() - centers[baseRight].getX();
		    double y2 = centers[i].getY() - centers[baseRight].getY();
		    
		    if (i != baseLeft) {
			if (Math.abs(x1 * y2 - x2 * y1) < 1e-8
			    && x2*x2 + y2*y2 < x1*x1 + y1*y1) {
			    baseLeft = i;
			    done = false;
			}
			if (x1 * y2 - x2 * y1 > 0) {
			    baseLeft = i;
			    done = false;
			}
		    }
		}
		for (int i=(inf+sup)/2; i<sup; i++) {
		    double x1 = centers[baseRight].getX() - centers[baseLeft].getX();
		    double y1 = centers[baseRight].getY() - centers[baseLeft].getY();
		    double x2 = centers[i].getX() - centers[baseLeft].getX();
		    double y2 = centers[i].getY() - centers[baseLeft].getY();
		    if (Math.abs(x1 * y2 - x2 * y1) < 1e-8
			&& x2*x2 + y2*y2 < x1*x1 + y1*y1) {
			baseRight = i;
			done = false;
		    }
		    if (i != baseRight && x1 * y2 - x2 * y1 < 0) {
			baseRight = i;
			done = false;
		    }
		    
		}
		if (done) break;
	    }
	    
	    triangles.get(baseLeft).add(baseRight);
	    triangles.get(baseRight).add(baseLeft);

	    while (true) {
		ListIterator<Integer> it = triangles.get(baseLeft).listIterator();
		Angle candLeft = new Angle();
		Angle nextCandLeft = new Angle();
		candLeft.angle     = 2*Math.PI;
		nextCandLeft.angle = 2*Math.PI;
		while (it.hasNext()) {
		    int candidate = it.next();

		    if (candidate == baseRight) continue;

		    double angle =
			Math.atan2(centers[candidate].getY() - centers[baseLeft].getY(),
			      centers[candidate].getX() - centers[baseLeft].getX())
			-Math.atan2(centers[baseRight].getY() - centers[baseLeft].getY(),
			       centers[baseRight].getX() - centers[baseLeft].getX());
		    if (angle < 0)
			angle += 2*Math.PI;
		    if (angle < candLeft.angle) {
			nextCandLeft.angle = candLeft.angle;
			candLeft.angle = angle;
			nextCandLeft.id = candLeft.id;
			candLeft.id = candidate;
		    } else if (angle < nextCandLeft.angle) {
			nextCandLeft.angle = angle;
			nextCandLeft.id = candidate;
		    }
		}
		
		it = triangles.get(baseRight).listIterator();
		Angle candRight = new Angle();
		Angle nextCandRight = new Angle();
		candRight.angle     = 2*Math.PI;
		nextCandRight.angle = 2*Math.PI;
		while (it.hasNext()) {
		    int candidate = it.next();
		    
		    if (candidate == baseLeft) continue;
		    
		    double angle =
			Math.atan2(centers[baseLeft].getY() - centers[baseRight].getY(),
				   centers[baseLeft].getX() - centers[baseRight].getX())
			-Math.atan2(centers[candidate].getY() - centers[baseRight].getY(),
				    centers[candidate].getX() - centers[baseRight].getX());
		    if (angle < 0)
			angle += 2*Math.PI;
		    if (angle < candRight.angle) {
			nextCandRight.angle = candRight.angle;
			candRight.angle = angle;
			nextCandRight.id = candRight.id;
			candRight.id = candidate;
		    } else if (angle < nextCandRight.angle) {
			nextCandRight.angle = angle;
			nextCandRight.id = candidate;
		    }
		}
		
		if (candRight.angle >= Math.PI && candLeft.angle >= Math.PI)
		    break;

		if (candLeft.angle < Math.PI 
		    && nextCandLeft.angle < Math.PI 
		    && inCircle(centers[nextCandLeft.id],
			     centers[baseLeft],
			     centers[baseRight],
			     centers[candLeft.id])) {
		    triangles.get(candLeft.id).removeFirstOccurrence(baseLeft);
		    triangles.get(baseLeft).removeFirstOccurrence(candLeft.id);
		    continue;
		}
		
		if (candRight.angle < Math.PI
		    && nextCandRight.angle < Math.PI
		    && inCircle(centers[nextCandRight.id],
			     centers[baseLeft],
			     centers[baseRight],
			     centers[candRight.id])) {
		    triangles.get(candRight.id).removeFirstOccurrence(baseRight);
		    triangles.get(baseRight).removeFirstOccurrence(candRight.id);
		    continue;
		}

		if (candRight.angle >= Math.PI) {
		    triangles.get(candLeft.id).add(baseRight);
		    triangles.get(baseRight).add(candLeft.id);
		    baseLeft = candLeft.id;
		    continue;
		}
		
		if (candLeft.angle >= Math.PI) {
		    triangles.get(candRight.id).add(baseLeft);
		    triangles.get(baseLeft).add(candRight.id);
		    baseRight = candRight.id;
		    continue;
		}
		
		if (inCircle(centers[candLeft.id],
			     centers[baseLeft],
			     centers[baseRight],
			     centers[candRight.id])) {
		    triangles.get(candLeft.id).add(baseRight);
		    triangles.get(baseRight).add(candLeft.id);
		    baseLeft = candLeft.id;
		} else {
		    triangles.get(candRight.id).add(baseLeft);
		    triangles.get(baseLeft).add(candRight.id);
		    baseRight = candRight.id;
		}
	    }
	}
    }
    
    public ArrayList<Polygon> computeVoronoi(double xMin, double xMax,
					      double yMin, double yMax) {
	ArrayList<Polygon> res = new ArrayList<Polygon>(centers.length);
	LinkedList<Point> screen = new LinkedList<Point>();
	screen.add(new Point(xMin, yMin));
	screen.add(new Point(xMin, yMax));
	screen.add(new Point(xMax, yMax));
	screen.add(new Point(xMax, yMin));
	for (int i=0; i<centers.length; i++) {
	    Polygon p = new Polygon(screen);
	    ListIterator<Integer> it = triangles.get(i).listIterator();
	    while (it.hasNext()) {
		int act = it.next();
		double a = centers[act].getX() - centers[i].getX();
		double b = centers[act].getY() - centers[i].getY();
		double c = a * (centers[act].getX() + centers[i].getX()) / 2
		         + b * (centers[act].getY() + centers[i].getY()) / 2;
		p.intersect(a, b, c);
	    }
	    res.add(p);
	}
	return res;
    }

    public void computeDelaunay() {
	Arrays.sort(centers);
	for (int i=0; i<centers.length; i++)
	    triangles.get(i).clear();
	
	computeTriangulation(0, centers.length);
    }
}

