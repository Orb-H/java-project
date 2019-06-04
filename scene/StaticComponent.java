package project.scene;

public class StaticComponent extends Component {

	protected StaticComponent(int locX, int locY, int sizeX, int sizeY) {
		super(locX, locY, sizeX, sizeY);
	}

	public StaticComponent(int locX, int locY, int sizeX, int sizeY, String[] s) {
		super(locX, locY, sizeX, sizeY, s);
	}

}
