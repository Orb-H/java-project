package project.scene.highscore;

import project.ranking.Ranking;
import project.scene.Component;
import project.scene.DynamicComponent;
import project.scene.Scene;
import project.scene.StaticComponent;
import project.system.GameSystem;

public class HighScoreScene extends Scene {

	private static HighScoreScene hss;

	Component mode1;
	Component mode2;
	Component score;

	boolean mode = false;

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
		mode1 = new StaticComponent(60, 11, 26, 6,
				new String[] { " __  __      _______   __ ", "/_ | \\ \\    / / ____| /_ |",
						" | |  \\ \\  / / (___    | |", " | |   \\ \\/ / \\___ \\   | |", " | |    \\  /  ____) |  | |",
						" |_|     \\/  |_____/   |_|" });
		mode2 = new StaticComponent(58, 11, 30, 6,
				new String[] { " ___   __      _______   ___  ", "|__ \\  \\ \\    / / ____| |__ \\ ",
						"   ) |  \\ \\  / / (___      ) |", "  / /    \\ \\/ / \\___ \\    / / ",
						" / /_     \\  /  ____) |  / /_ ", "|____|     \\/  |_____/  |____|" });
		score = new DynamicComponent(56, 21, 45, 21);

		loadScore();

		addComponent(mode1);
		addComponent(score);
	}

	public void setMode(boolean b) {// false for 1v1, true for 2v2
		if (b ^ mode) {
			mode = b;
			if (b) {
				deleteComponent(mode1);
				addComponent(mode2);
			} else {
				deleteComponent(mode2);
				addComponent(mode1);
			}
			loadScore();
		}
	}

	protected void onLoad() {
		gs.getInput("Press Enter to return to main menu");
		sm.loadScene("title");
	}

	public void loadScore() {
		Ranking r = gs.getRanking(mode);
		score.setRender(score.getSize().getX(), score.getSize().getY(), r.format());
	}

}
