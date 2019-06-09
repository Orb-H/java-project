package project.scene.transition;

import project.scene.Scene;
import project.scene.StaticComponent;
import project.scene.battle.SelectScene;

public class WinScene extends Scene {

	private static WinScene ws;

	private int nextStage = 1;

	public static WinScene getInstance() {
		if (ws == null)
			ws = new WinScene();
		return ws;
	}

	private WinScene() {
		addComponent(new StaticComponent(49, 11, 40, 18,
				new String[] { "8b        d8 ,ad8888ba,   88        88  ", " Y8,    ,8P d8\"'    `\"8b  88        88  ",
						"  Y8,  ,8P d8'        `8b 88        88  ", "   \"8aa8\"  88          88 88        88  ",
						"    `88'   88          88 88        88  ", "     88    Y8,        ,8P 88        88  ",
						"     88     Y8a.    .a8P  Y8a.    .a8P  ", "     88      `\"Y8888Y\"'    `\"Y8888Y\"'   ",
						"                                        ", "                                        ",
						" I8,        8        ,8I 88 888b      88", " `8b       d8b       d8' 88 8888b     88",
						"  \"8,     ,8\"8,     ,8\"  88 88 `8b    88", "   Y8     8P Y8     8P   88 88  `8b   88",
						"   `8b   d8' `8b   d8'   88 88   `8b  88", "    `8a a8'   `8a a8'    88 88    `8b 88",
						"     `8a8'     `8a8'     88 88     `8888", "      `8'       `8'      88 88      `888" }));
	}

	protected void onLoad() {
		getInput("Press Enter to proceed to stage " + nextStage);
		SelectScene.getInstance().init(nextStage);
		sm.loadScene("select");
	}

	public void setNext(int n) {
		nextStage = n;
	}

}
