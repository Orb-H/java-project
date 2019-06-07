package project.scene.battle;

import project.scene.DynamicComponent;

public class ProfileComponent extends DynamicComponent {

	protected ProfileComponent(int locX, int locY, int num) {
		super(locX, locY, 10, 6);

		switch (num) {
		case 0:
			changeRender(new String[] { "忙式式式式式式式式忖", "弛        弛", "弛        弛", "弛        弛", "弛        弛",
					"戌式式式式式式式式戎" });
			break;
		case 1:
			changeRender(new String[] { "忙式式式式式式式式忖", "弛        弛", "弛        弛", "弛        弛", "弛        弛",
					"戌式式式式式式式式戎" });
			break;
		case 2:
			changeRender(new String[] { "忙式式式式式式式式忖", "弛        弛", "弛        弛", "弛        弛", "弛        弛",
					"戌式式式式式式式式戎" });
			break;
		case 3:
			changeRender(new String[] { "忙式式式式式式式式忖", "弛        弛", "弛        弛", "弛        弛", "弛        弛",
					"戌式式式式式式式式戎" });
			break;
		case 4:
			changeRender(new String[] { "忙式式式式式式式式忖", "弛        弛", "弛        弛", "弛        弛", "弛        弛",
					"戌式式式式式式式式戎" });
			break;
		}
	}

}
