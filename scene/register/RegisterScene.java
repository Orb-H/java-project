package project.scene.register;

import project.scene.Scene;
import project.scene.StaticComponent;
import project.system.GameSystem;

public class RegisterScene extends Scene {

	private static RegisterScene rs;

	public static RegisterScene getInstance() {
		if (rs == null)
			rs = new RegisterScene();
		return rs;
	}

	private RegisterScene() {
		addComponent(new StaticComponent(35, 4, 73, 6,
				new String[] { " _   _ ________          __  _____  ______ _____ ____  _____  _____  _ _ ",
						"| \\ | |  ____\\ \\        / / |  __ \\|  ____/ ____/ __ \\|  __ \\|  __ \\| | |",
						"|  \\| | |__   \\ \\  /\\  / /  | |__) | |__ | |   | |  | | |__) | |  | | | |",
						"| . ` |  __|   \\ \\/  \\/ /   |  _  /|  __|| |   | |  | |  _  /| |  | | | |",
						"| |\\  | |____   \\  /\\  /    | | \\ \\| |___| |___| |__| | | \\ \\| |__| |_|_|",
						"|_| \\_|______|   \\/  \\/     |_|  \\_\\______\\_____\\____/|_|  \\_\\_____/(_|_)" }));
		addComponent(new StaticComponent(24, 15, 97, 6, new String[] {
				" ______ _   _ _______ ______ _____   __     ______  _    _ _____    _   _          __  __ ______ ",
				"|  ____| \\ | |__   __|  ____|  __ \\  \\ \\   / / __ \\| |  | |  __ \\  | \\ | |   /\\   |  \\/  |  ____|",
				"| |__  |  \\| |  | |  | |__  | |__) |  \\ \\_/ / |  | | |  | | |__) | |  \\| |  /  \\  | \\  / | |__   ",
				"|  __| | . ` |  | |  |  __| |  _  /    \\   /| |  | | |  | |  _  /  | . ` | / /\\ \\ | |\\/| |  __|  ",
				"| |____| |\\  |  | |  | |____| | \\ \\     | | | |__| | |__| | | \\ \\  | |\\  |/ ____ \\| |  | | |____ ",
				"|______|_| \\_|  |_|  |______|_|  \\_\\    |_|  \\____/ \\____/|_|  \\_\\ |_| \\_/_/    \\_\\_|  |_|______|" }));
	}

	protected void onLoad() {
		inputName();
	}

	public void inputName() {
		String s = gs.getInput("Enter your name(Max 6 characters)");
		while (s.length() > 6 || s.length() <= 0) {
			gs.getInput("Name should be 1 to 6 characters!");
			s = gs.getInput("Enter your name(Max 6 characters)");
		}

		gs.getRanking(GameSystem.mode).saveIfHigher(s, 0.0);// Point should be added
	}

}
