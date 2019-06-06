package project.scene.battle;

import project.scene.Scene;

public class SelectScene extends Scene {

	private static SelectScene ss;

	private FieldScene fs;

	public static SelectScene getInstance() {
		if (ss == null)
			ss = new SelectScene();
		return ss;
	}

	private SelectScene() {

	}

}
