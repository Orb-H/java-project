package project.scene.battle;

import project.scene.Scene;
import project.system.GameSystem;

public class SelectScene extends Scene {

	private static SelectScene ss;

	private FieldScene fs;

	private CardComponent[][] cards;

	public static SelectScene getInstance() {
		if (ss == null)
			ss = new SelectScene();
		return ss;
	}

	private SelectScene() {
		fs = FieldScene.getInstance();

		addComponent(fs.vs);

		cards = new CardComponent[3][6];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				cards[i][j] = new CardComponent(17 + 18 * j, 8 + 10 * i, null);
			}
		}

		addComponent(fs.p1);
		addComponent(fs.p1Health);
		addComponent(fs.p1Mana);

		addComponent(fs.e1);
		addComponent(fs.e1Health);
		addComponent(fs.e1Mana);

		for (int i = 0; i < 3; i++) {
			addComponent(fs.p1Selected[i]);

			for (int j = 0; j < 6; j++) {
				addComponent(cards[i][j]);
			}
		}

		addComponent(fs.separator);
	}

	public void changeMode() {
		if (GameSystem.mode) {
			resize(140, 66);

			addComponent(fs.p2);
			addComponent(fs.p2Health);
			addComponent(fs.p2Mana);

			addComponent(fs.e2);
			addComponent(fs.e2Health);
			addComponent(fs.e2Mana);

			for (int i = 0; i < 3; i++) {
				addComponent(fs.p2Selected[i]);

				for (int j = 0; j < 6; j++) {
					cards[i][j].changeLocation(17 + 18 * j, 14 + 10 * i);
				}
			}
		} else {
			resize(140, 50);

			deleteComponent(fs.p2);
			deleteComponent(fs.p2Health);
			deleteComponent(fs.p2Mana);

			deleteComponent(fs.e2);
			deleteComponent(fs.e2Health);
			deleteComponent(fs.e2Mana);

			for (int i = 0; i < 3; i++) {
				deleteComponent(fs.p2Selected[i]);

				for (int j = 0; j < 6; j++) {
					cards[i][j].changeLocation(17 + 18 * j, 8 + 10 * i);
				}
			}
		}
	}

}
