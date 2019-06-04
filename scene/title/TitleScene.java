package project.scene.title;

import project.scene.Scene;
import project.system.GameSystem;

public class TitleScene extends Scene {

	private static TitleScene ts;

	public static TitleScene getInstance() {
		if (ts == null)
			ts = new TitleScene();
		return ts;
	}

	private TitleScene() {
		// addComponent(new StaticComponent()); //Title
		// addComponent(new StaticComponent()); //Menu
	}
	
	protected void onLoad() {
		inputMenu();
	}

	public void inputMenu() {
		String s = getInput("Choose menu: ");
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			return;
		}
		switch (i) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		}
	}

}
