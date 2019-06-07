package project.scene.battle;

import project.scene.DynamicComponent;

public class FieldCellComponent extends DynamicComponent {

	private static final String[][] render = new String[][] {
			new String[] { "忙式式式式式式式式式式式式式忖", "弛             弛", "弛             弛", "弛             弛",
					"弛             弛", "戌式式式式式式式式式式式式式戎" },
			new String[] { "忙式式式式式式式式式式式式式忖", "弛             弛", "弛             弛", "弛             弛",
					"弛             弛", "戌式式式式式式式式式式式式式戎" },
			new String[] { "忙式式式式式式式式式式式式式忖", "弛             弛", "弛             弛", "弛             弛",
					"弛             弛", "戌式式式式式式式式式式式式式戎" },
			new String[] { "忙式式式式式式式式式式式式式忖", "弛             弛", "弛             弛", "弛             弛",
					"弛             弛", "戌式式式式式式式式式式式式式戎" },
			new String[] { "忙式式式式式式式式式式式式式忖", "弛             弛", "弛             弛", "弛             弛",
					"弛             弛", "戌式式式式式式式式式式式式式戎" } };

	protected FieldCellComponent(int locX, int locY, int num) {
		super(locX, locY, 15, 6);

		setCharacter(num);
	}

	public void setCharacter(int num) {
		changeRender(render[num]);
	}

}