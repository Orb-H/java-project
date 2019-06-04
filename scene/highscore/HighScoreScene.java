package project.scene.highscore;

import project.scene.Scene;
import project.scene.StaticComponent;

public class HighScoreScene extends Scene {

	private static HighScoreScene hss;

	public static HighScoreScene getInstance() {
		if (hss == null)
			hss = new HighScoreScene();
		return hss;
	}

	private HighScoreScene() {
		super();
		addComponent(new StaticComponent(3, 2, 134, 8, new String[] {
				" ad88888ba    ,ad8888ba,   ,ad8888ba,   88888888ba  88888888888    88888888ba    ,ad8888ba,        db        88888888ba  88888888ba,  ",
				"d8\"     \"8b  d8\"'    `\"8  d8\"'    `\"8b  88      \"8b 88             88      \"8b  d8\"'    `\"8b      d88b       88      \"8b 88      `\"8b ",
				"Y8,         d8'          d8'        `8b 88      ,8P 88             88      ,8P d8'        `8b    d8'`8b      88      ,8P 88        `8b",
				"`Y8aaaaa,   88           88          88 88aaaaaa8P' 88aaaaa        88aaaaaa8P' 88          88   d8'  `8b     88aaaaaa8P' 88         88",
				"  `\"\"\"\"\"8b, 88           88          88 88\"\"\"\"88'   88\"\"\"\"\"        88\"\"\"\"\"\"8b, 88          88  d8YaaaaY8b    88\"\"\"\"88'   88         88",
				"        `8b Y8,          Y8,        ,8P 88    `8b   88             88      `8b Y8,        ,8P d8\"\"\"\"\"\"\"\"8b   88    `8b   88         8P",
				"Y8a     a8P  Y8a.    .a8  Y8a.    .a8P  88     `8b  88             88      a8P  Y8a.    .a8P d8'        `8b  88     `8b  88      .a8P ",
				" \"Y88888P\"    `\"Y8888Y\"'   `\"Y8888Y\"'   88      `8b 88888888888    88888888P\"    `\"Y8888Y\"' d8'          `8b 88      `8b 88888888Y\"'  " }));
		// addComponent(new DynamicComponent());
	}

}
