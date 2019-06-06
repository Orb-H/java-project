package project.scene;

public class DynamicComponent extends Component {

	public DynamicComponent(int locX, int locY, int sizeX, int sizeY) {
		super(locX, locY, sizeX, sizeY);
	}

	public DynamicComponent(int locX, int locY, int sizeX, int sizeY, String[] s) {
		super(locX, locY, sizeX, sizeY, s);
	}

	public void changeRender(String[] s) {
		setRender(size.getX(), size.getY(), s);
	}

}
