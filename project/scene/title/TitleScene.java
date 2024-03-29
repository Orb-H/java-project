package project.scene.title;

import project.scene.BorderComponent;
import project.scene.Scene;
import project.scene.StaticComponent;
import project.scene.battle.FieldScene;
import project.scene.battle.SelectScene;
import project.scene.highscore.HighScoreScene;
import project.system.GameSystem;

public class TitleScene extends Scene {

	private static TitleScene ts;

	public static TitleScene getInstance() {
		if (ts == null)
			ts = new TitleScene();
		return ts;
	}

	private TitleScene() {
		addComponent(new BorderComponent(13, 12, 44, 28));
		addComponent(new StaticComponent(15, 13, 40, 26,
				new String[] { "┌──────────────────────────────────────┐",
						"│      / / .'  .' /  \\ '.  '. \\ \\      │", "│    .'./.'  .'  / /\\ \\  '.  '.\\.'.    │",
						"│  .'  \\'.  .   / /  \\ \\   .  .'/  '.  │", "│.'  .' \\ '. './ /    \\ \\.' .' / '.  '.│",
						"│  .'/   \\  '.  /      \\  .'  /   \\'.  │", "│.' /     \\   '/   /\\   \\'   /     \\ '.│",
						"│  /   /\\  \\    './  \\.'    /  /\\   \\  │",
						"│ /   /  \\  \\    /|  |\\    /  /  \\   \\ │", "│/   /  .' .'\\  / |  | \\  /'. '.  \\   \\│",
						"│   / .' .'   \\/  |..|  \\/   '. '. \\   │", "│  /.' .'     /--.'  '.--\\     '. '.\\  │",
						"│ .' .'      /  /      \\  \\      '. '. │", "│ '. '.      \\  \\      /  /      .' .' │",
						"│  \\'. '.     \\--'.  .'--/     .' .'/  │", "│   \\ '. '.   /\\  |''|  /\\   .' .' /   │",
						"│\\   \\  '. './  \\ |  | /  \\.' .'  /   /│", "│ \\   \\  /  /    \\|  |/    \\  \\  /   / │",
						"│  \\   \\/  /    .'\\  /'.    \\  \\/   /  │", "│'. \\     /   .\\   \\/   /.   \\     / .'│",
						"│  '.\\   /  .'  \\      /  '.  \\   /.'  │", "│'.  '. / .' .'\\ \\    / /'. '. \\ .'  .'│",
						"│  '.  /.'  '   \\ \\  / /   '  '.\\  .'  │", "│    '.'\\ '. '.  \\ \\/ /  .'  .'/'.'    │",
						"│      \\ \\  '. '. \\  / .'  .' / /      │", "└──────────────────────────────────────┘" }));
		addComponent(new BorderComponent(85, 12, 44, 28));
		addComponent(new StaticComponent(87, 13, 40, 26,
				new String[] { "┌──────────────────────────────────────┐",
						"│      / / .'  .' /  \\ '.  '. \\ \\      │", "│    .'./.'  .'  / /\\ \\  '.  '.\\.'.    │",
						"│  .'  \\'.  .   / /  \\ \\   .  .'/  '.  │", "│.'  .' \\ '. './ /    \\ \\.' .' / '.  '.│",
						"│  .'/   \\  '.  /      \\  .'  /   \\'.  │", "│.' /     \\   '/   /\\   \\'   /     \\ '.│",
						"│  /   /\\  \\    './  \\.'    /  /\\   \\  │",
						"│ /   /  \\  \\    /|  |\\    /  /  \\   \\ │", "│/   /  .' .'\\  / |  | \\  /'. '.  \\   \\│",
						"│   / .' .'   \\/  |..|  \\/   '. '. \\   │", "│  /.' .'     /--.'  '.--\\     '. '.\\  │",
						"│ .' .'      /  /      \\  \\      '. '. │", "│ '. '.      \\  \\      /  /      .' .' │",
						"│  \\'. '.     \\--'.  .'--/     .' .'/  │", "│   \\ '. '.   /\\  |''|  /\\   .' .' /   │",
						"│\\   \\  '. './  \\ |  | /  \\.' .'  /   /│", "│ \\   \\  /  /    \\|  |/    \\  \\  /   / │",
						"│  \\   \\/  /    .'\\  /'.    \\  \\/   /  │", "│'. \\     /   .\\   \\/   /.   \\     / .'│",
						"│  '.\\   /  .'  \\      /  '.  \\   /.'  │", "│'.  '. / .' .'\\ \\    / /'. '. \\ .'  .'│",
						"│  '.  /.'  '   \\ \\  / /   '  '.\\  .'  │", "│    '.'\\ '. '.  \\ \\/ /  .'  .'/'.'    │",
						"│      \\ \\  '. '. \\  / .'  .' / /      │", "└──────────────────────────────────────┘" }));

		addComponent(new BorderComponent(46, 19, 50, 27));
		addComponent(new StaticComponent(48, 20, 46, 25, new String[] {
				"┌────────────────────────────────────────────┐", "│                                            │",
				"│           __            __    __   __      │", "│    │   │ │  \\  │\\  /│  /  \\  │  \\ │        │",
				"│    │   │ ├──'  │ \\/ │ │    │ │  │ ├──      │", "│    │ . │ │     │    │  \\__/  │__/ │__      │",
				"│  __     __   __            __    __   __   │", "│    │      │ │  \\  │\\  /│  /  \\  │  \\ │     │",
				"│ ┌──┘   ┌──┘ ├──'  │ \\/ │ │    │ │  │ ├──   │", "│ │___ . │___ │     │    │  \\__/  │__/ │__   │",
				"│  __       __   __   __   __    __   __     │", "│    │   │ │  \\ /    /    /  \\  │  \\ │       │",
				"│  ──┤   │ ├──' └──┐ │   │    │ ├──' ├──     │", "│  __│ . │ │    __/  \\__  \\__/  │  \\ │__     │",
				"│         __   __   __   __   __    __   __  │", "│  /        │ │  \\ /    /    /  \\  │  \\ │    │",
				"│ /__│   ┌──┘ ├──' └──┐ │   │    │ ├──' ├──  │", "│    │ . │___ │    __/  \\__  \\__/  │  \\ │__  │",
				"│  __     __     _ _ _ _                     │", "│ │      │   \\ /  │   │                      │",
				"│ └──┐   ├──  X   │   │                      │", "│ ___│ . │__ / \\ _│_  │                      │",
				"│                                            │", "│                                            │",
				"└────────────────────────────────────────────┘" }));

		addComponent(new StaticComponent(29, 4, 82, 6, new String[] {
				" _  _______ _   _  _____      ____  ______     _______       _____   ____ _______ ",
				"| |/ /_   _| \\ | |/ ____|    / __ \\|  ____|   |__   __|/\\   |  __ \\ / __ \\__   __|",
				"| ' /  | | |  \\| | |  __    | |  | | |__         | |  /  \\  | |__) | |  | | | |   ",
				"|  <   | | | . ` | | |_ |   | |  | |  __|        | | / /\\ \\ |  _  /| |  | | | |   ",
				"| . \\ _| |_| |\\  | |__| |   | |__| | |           | |/ ____ \\| | \\ \\| |__| | | |   ",
				"|_|\\_\\_____|_| \\_|\\_____|    \\____/|_|           |_/_/    \\_\\_|  \\_\\\\____/  |_|   " }));
	}

	protected void onLoad() {
		inputMenu();
	}

	public void inputMenu() {
		while (true) {
			String s = getInput("Choose menu: ");
			int i;
			try {
				i = Integer.parseInt(s);
			} catch (Exception e) {
				continue;
			}
			switch (i) {
			case 1:
				GameSystem.mode = false;
				SelectScene.getInstance().changeMode();
				FieldScene.getInstance().changeMode();
				SelectScene.getInstance().init(1);
				gs.resetScore();
				sm.loadScene("select");
				break;
			case 2:
				GameSystem.mode = true;
				SelectScene.getInstance().changeMode();
				FieldScene.getInstance().changeMode();
				SelectScene.getInstance().init(1);
				gs.resetScore();
				sm.loadScene("select");
				break;
			case 3:
				HighScoreScene.getInstance().setMode(false);
				sm.loadScene("highscore");
				break;
			case 4:
				HighScoreScene.getInstance().setMode(true);
				sm.loadScene("highscore");
				break;
			case 5:
				System.exit(0);
			}
		}
	}

}
