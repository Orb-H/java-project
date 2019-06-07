package project.scene.battle;

import project.scene.DynamicComponent;
import project.scene.Scene;
import project.scene.StaticComponent;
import project.system.GameSystem;

public class FieldScene extends Scene {

	private static FieldScene fs;

	StaticComponent vs;

	ProfileComponent p1;
	BarComponent p1Health;
	BarComponent p1Mana;

	ProfileComponent p2;
	BarComponent p2Health;
	BarComponent p2Mana;

	ProfileComponent e1;
	BarComponent e1Health;
	BarComponent e1Mana;

	ProfileComponent e2;
	BarComponent e2Health;
	BarComponent e2Mana;

	CardComponent[] p1Selected;
	CardComponent[] p2Selected;
	CardComponent[] e1Selected;
	CardComponent[] e2Selected;

	FieldCellComponent[][] field;

	DynamicComponent separator;

	public static FieldScene getInstance() {
		if (fs == null)
			fs = new FieldScene();
		return fs;
	}

	private FieldScene() {
		vs = new StaticComponent(66, 2, 8, 3, new String[] { "     __ ", "| / (_ `", "|/  __) " });
		addComponent(vs);

		p1 = new ProfileComponent(2, 1, 1);
		p2 = new ProfileComponent(2, 7, 3);
		e1 = new ProfileComponent(128, 1, 2);
		e2 = new ProfileComponent(128, 7, 4);

		p1Health = new BarComponent(13, 1, false);
		p1Mana = new BarComponent(13, 4, false);
		p2Health = new BarComponent(13, 7, false);
		p2Mana = new BarComponent(13, 10, false);

		e1Health = new BarComponent(76, 1, true);
		e1Mana = new BarComponent(76, 4, true);
		e2Health = new BarComponent(76, 7, true);
		e2Mana = new BarComponent(76, 10, true);

		p1Selected = new CardComponent[3];
		p2Selected = new CardComponent[3];
		e1Selected = new CardComponent[3];
		e2Selected = new CardComponent[3];

		for (int i = 0; i < 3; i++) {
			p1Selected[i] = new CardComponent(12 + 18 * i, (GameSystem.mode ? 45 : 39), null);
			p2Selected[i] = new CardComponent(12 + 18 * i, 55, null);
			e1Selected[i] = new CardComponent(76 + 18 * i, (GameSystem.mode ? 45 : 39), null);
			e2Selected[i] = new CardComponent(76 + 18 * i, 55, null);
		}

		field = new FieldCellComponent[5][4];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				field[i][j] = new FieldCellComponent(35 + 18 * j, 14 + 6 * i, 0);
			}
		}

		separator = new DynamicComponent(0, 38, 140, 1, new String[] {
				"戍式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式式扣" });

		addComponent(p1);
		addComponent(e1);

		addComponent(p1Health);
		addComponent(p1Mana);
		addComponent(e1Health);
		addComponent(e1Mana);

		for (int i = 0; i < 3; i++) {
			addComponent(p1Selected[i]);
			addComponent(e1Selected[i]);
			for (int j = 0; j < 4; j++) {
				addComponent(field[i][j]);
			}
		}

		addComponent(separator);
	}

	public void init() {

	}

	public void changeMode() {
		if (GameSystem.mode) {
			resize(140, 66);

			addComponent(p2);
			addComponent(e2);

			addComponent(p2Health);
			addComponent(p2Mana);
			addComponent(e2Health);
			addComponent(e2Mana);

			for (int i = 0; i < 3; i++) {
				addComponent(p2Selected[i]);
				addComponent(e2Selected[i]);

				p1Selected[i].changeLocation(12 + 18 * i, 45);
				p2Selected[i].changeLocation(12 + 18 * i, 55);
				e1Selected[i].changeLocation(76 + 18 * i, 45);
				e2Selected[i].changeLocation(76 + 18 * i, 55);
			}

			for (int i = 3; i < 5; i++) {
				for (int j = 0; j < 4; j++) {
					addComponent(field[i][j]);
				}
			}

			separator.changeLocation(0, 44);
		} else {
			resize(140, 50);
			
			deleteComponent(p2);
			deleteComponent(e2);

			deleteComponent(p2Health);
			deleteComponent(p2Mana);
			deleteComponent(e2Health);
			deleteComponent(e2Mana);

			for (int i = 0; i < 3; i++) {
				deleteComponent(p2Selected[i]);
				deleteComponent(e2Selected[i]);

				p1Selected[i].changeLocation(12 + 18 * i, 39);
				p2Selected[i].changeLocation(12 + 18 * i, 49);
				e1Selected[i].changeLocation(76 + 18 * i, 39);
				e2Selected[i].changeLocation(76 + 18 * i, 49);
			}

			for (int i = 3; i < 5; i++) {
				for (int j = 0; j < 4; j++) {
					deleteComponent(field[i][j]);
				}
			}

			separator.changeLocation(0, 38);
		}
	}

}
