package project.scene.battle;

import project.scene.DynamicComponent;

public class FieldCellComponent extends DynamicComponent {

	int num = 0;

	protected FieldCellComponent(int locX, int locY, int num) {
		super(locX, locY, 15, 6);

		this.num = num;
		switch (num) {
		case 0:
			changeRender(new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" });
			break;
		case 1:
			changeRender(new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" });
			break;
		case 2:
			changeRender(new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" });
			break;
		case 3:
			changeRender(new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" });
			break;
		case 4:
			changeRender(new String[] { "������������������������������", "��             ��", "��             ��", "��             ��",
					"��             ��", "������������������������������" });
			break;
		}
	}

}
