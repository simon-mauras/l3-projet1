import java.lang.Comparable;

public class Angle implements Comparable<Angle> {
    public int id;
    public double angle;
    public double ifEq;

    public int compareTo(Angle a) {
	return this.angle > a.angle ? 1 : -1;
    }
};