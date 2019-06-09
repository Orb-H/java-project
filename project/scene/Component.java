package project.scene;

import project.util.Point;
import project.util.StringUtils;

public abstract class Component {

	protected char[][] toRender;
	protected Point loc;
	protected Point size;

	protected Component(int locX, int locY, int sizeX, int sizeY) {
		loc = new Point(locX, locY);
		size = new Point(sizeX, sizeY);
	}

	protected Component(int locX, int locY, int sizeX, int sizeY, String[] s) {
		this(locX, locY, sizeX, sizeY);
		setRender(sizeX, sizeY, s);
	}

	public void setRender(int x, int y, String[] s) {
		toRender = new char[y][];
		for (int i = 0; i < y; i++) {
			toRender[i] = StringUtils.alignString(s[i], x);
		}
	}

	public char[][] render() {
		return toRender;
	}

	public char charAt(int x, int y) {
		try {
			return toRender[y - loc.getY()][x - loc.getX()];
		} catch (Exception ex) {
			return 0;
		}
	}

	public Point getLoc() {
		return loc;
	}

	public Point getSize() {
		return size;
	}

}
