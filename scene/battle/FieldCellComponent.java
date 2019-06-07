package project.scene.battle;

import project.scene.DynamicComponent;

public class FieldCellComponent extends DynamicComponent {

	private static final String[][] render = new String[][] {
			new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" },
			new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" },
			new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" },
			new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" },
			new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" } };

	protected FieldCellComponent(int locX, int locY, int num) {
		super(locX, locY, 15, 6);

		setCharacter(num);
	}

	public void setCharacter(int num) {
		changeRender(render[num]);
	}

}