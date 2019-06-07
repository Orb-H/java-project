package project.scene.battle;

import project.scene.DynamicComponent;

public class BarComponent extends DynamicComponent {

	private boolean direction = false;// false for right-to-left accumulation, true for left-to-right accumulation

	protected BarComponent(int locX, int locY, boolean direction) {
		super(locX, locY, 52, 3);
		this.direction = direction;

		setAmount(100);
	}

	public void setAmount(int amount) {
//		String[] s = new String[3];
//		s[0] = "┌──────────────────────────────────────────────────┐";
//		s[2] = "└──────────────────────────────────────────────────┘";
//
//		if (amount == 100) {
//			s[1] = "│##################################################│";
//			if (!direction) {
//				s[1] = s[1].replaceFirst("#####", " 100 ");
//			} else {
//				s[1] = s[1].replaceFirst("#####", " 001 ");
//				s[1] = new StringBuffer(s[1]).reverse().toString();
//			}
//		} else if (amount == 0) {
//			s[1] = "│                                                  │";
//			s[1].replaceFirst("###", " 0 ");
//			if (!direction) {
//				s[1] = new StringBuffer(s[1]).reverse().toString();
//			}
//		} else {
//			// amount = 100 - amount;
//			int line = Math.round(((float) 100 - amount) * 0.5f);
//			StringBuilder upper = new StringBuilder(s[0]);
//			StringBuilder lower = new StringBuilder(s[2]);
//
//			upper.setCharAt(direction ? 50 - line : (line + 1), '┬');
//			lower.setCharAt(direction ? 50 - line : (line + 1), '┴');
//
//			StringBuilder middle = new StringBuilder("│##################################################│");
//			for (int i = 1; i < line + 1; i++) {
//				middle.setCharAt(i, ' ');
//			}
//			middle.setCharAt(line + 1, '│');
//			for (int i = line + 2; i < 51; i++) {
//				middle.setCharAt(i, '#');
//			}
//
//			StringBuilder num = new StringBuilder("" + amount);
//			if (direction)
//				num = num.reverse();
//
//			num = num.insert(0, ' ');
//			num = num.append(' ');
//
//			middle = middle.replace(1, 4 + (amount / 10 > 0 ? 1 : 0), num.toString());
//			if (direction)
//				middle = middle.reverse();
//
//			s[1] = middle.toString();
//			s[0] = upper.toString();
//			s[2] = lower.toString();
//		}
//
//		changeRender(s);
	}

}
