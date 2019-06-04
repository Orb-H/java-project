package project.util;


public class Point {

	public static final Point x_1y_1 = new Point(-1, -1);
	public static final Point x_1y0 = new Point(-1, 0);
	public static final Point x_1y1 = new Point(-1, 1);
	public static final Point x0y_2=new Point(0, -2);
	public static final Point x0y_1 = new Point(0, -1);
	public static final Point x0y0 = new Point();
	public static final Point x0y1 = new Point(0, 1);
	public static final Point x0y2 = new Point(0, 2);
	public static final Point x1y_1 = new Point(1, -1);
	public static final Point x1y0 = new Point(1, 0);
	public static final Point x1y1 = new Point(1, 1);
	public static final Point x_2y_2 = new Point(-2, -2);
	public static final Point x_2y2 = new Point(-2, 2);
	public static final Point x2y_2 = new Point(2, -2);
	public static final Point x2y2 = new Point(2, 2);
	public static final Point x_2y_1 = new Point(-2, -1);
	public static final Point x_2y0 = new Point(-2, 0);
	public static final Point x_2y1 = new Point(-2, 1);
	public static final Point x2y_1 = new Point(2, -1);
	public static final Point x2y0 = new Point(2, 0);
	public static final Point x2y1 = new Point(2, 1);

	private int x;
	private int y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		setX(x);
		setY(y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Point negate() {
		return new Point(-x, -y);
	}

	public Point add(Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public Point subtract(Point p) {
		return add(p.negate());
	}

	public Point multiply(int i) {
		return new Point(i * x, i * y);
	}

}
