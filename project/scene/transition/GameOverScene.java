package project.scene.transition;

import project.scene.Scene;
import project.scene.StaticComponent;

public class GameOverScene extends Scene {

	private static GameOverScene gos;

	public static GameOverScene getInstance() {
		if (gos == null)
			gos = new GameOverScene();
		return gos;
	}

	private GameOverScene() {
		addComponent(new StaticComponent(39, 11, 59, 18,
				new String[] { "  ,ad8888ba,        db        88b           d88 88888888888",
						" d8\"'    `\"8b      d88b       888b         d888 88         ",
						"d8'               d8'`8b      88`8b       d8'88 88         ",
						"88               d8'  `8b     88 `8b     d8' 88 88aaaaa    ",
						"88      88888   d8YaaaaY8b    88  `8b   d8'  88 88\"\"\"\"\"    ",
						"Y8,        88  d8\"\"\"\"\"\"\"\"8b   88   `8b d8'   88 88         ",
						" Y8a.    .a88 d8'        `8b  88    `888'    88 88         ",
						"  `\"Y88888P\" d8'          `8b 88     `8'     88 88888888888",
						"                                                           ",
						"                                                           ",
						"      ,ad8888ba,  8b           d8 88888888888 88888888ba   ",
						"     d8\"'    `\"8b `8b         d8' 88          88      \"8b  ",
						"    d8'        `8b `8b       d8'  88          88      ,8P  ",
						"    88          88  `8b     d8'   88aaaaa     88aaaaaa8P'  ",
						"    88          88   `8b   d8'    88\"\"\"\"\"     88\"\"\"\"88'    ",
						"    Y8,        ,8P    `8b d8'     88          88    `8b    ",
						"     Y8a.    .a8P      `888'      88          88     `8b   ",
						"      `\"Y8888Y\"'        `8'       88888888888 88      `8b  " }));
	}

	protected void onLoad() {
		getInput("Press Enter to return to main menu");
		sm.loadScene("title");
	}

}
