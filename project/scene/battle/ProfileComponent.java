package project.scene.battle;

import project.scene.DynamicComponent;

public class ProfileComponent extends DynamicComponent {

	protected ProfileComponent(int locX, int locY, int num) {
		super(locX, locY, 10, 6);

		switch (num) {
		case 0:
			changeRender(new String[] { "┌────────┐", "│        │", "│        │", "│        │", "│        │",
					"└────────┘" });
			break;
		case 1:
			changeRender(
					new String[] { "┌────────┐", "│   Λ Λ  │", "│  ( 'x')│", "│  / づづ│", "│~(____) │", "└────────┘" });
			break;
		case 2:
			changeRender(
					new String[] { "┌────────┐", "│  Λ Λ   │", "│(-Λ- )  │", "│へへ \\  │", "│ (____)~│", "└────────┘" });
			break;
		case 3:
			changeRender(new String[] { "┌────────┐", "│  Λ Λ   │", "│ ( 6_6) │", "│ Z    S │", "│ (____) │",
					"└────────┘" });
			break;
		case 4:
			changeRender(new String[] { "┌────────┐", "│  Λ Λ   │", "│(;A; )  │", "│ q q \\  │", "│ (____)~│",
					"└────────┘" });
			break;
		}
	}

}
