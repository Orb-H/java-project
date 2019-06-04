package project.scene;

public class BorderComponent extends StaticComponent {

	public BorderComponent(int locX, int locY, int sizeX, int sizeY) {
		super(locX, locY, sizeX, sizeY);
		String[] s = new String[sizeY];

		String dashes = "";
		String spaces = "";
		for (int i = 0; i < sizeX - 2; i++) {
			dashes += "¦¡";
			spaces += " ";
		}
		s[0] = "¦£" + dashes + "¦¤";
		s[sizeY - 1] = "¦¦" + dashes + "¦¥";
		for (int i = 1; i < sizeY - 1; i++) {
			s[i] = "¦¢" + spaces + "¦¢";
		}

		setRender(sizeX, sizeY, s);
	}

}
