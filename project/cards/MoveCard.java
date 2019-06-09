package project.cards;

import project.system.AI;
import project.system.GameSystem;
import project.system.Player;

public class MoveCard extends Card {

	int direction = 0;// left,right,up,down

	public MoveCard(String name, int number, String[] ascii, String description, int direction) {
		super(CardType.MOVE, name, number, ascii, description, 0, 0);
		this.direction = direction;
	}

	public boolean inBound(int dx, int dy) {
		return true;
	}

	public void act(int caster) {
		System.out.println(caster + " " + direction + " " + number);// FIXME
		if (caster < 2) {
			Player ply = ss.getPlayer(caster);
			if (direction == 0 && ply.x > 0)
				ply.x -= 1;
			else if (direction == 1 && ply.x < 3)
				ply.x += 1;
			else if (direction == 2 && ply.y > 0)
				ply.y -= 1;
			else if (direction == 3 && ply.y < (GameSystem.mode ? 4 : 2))
				ply.y += 1;
			else if (number == 22 && ply.x > 0)
				ply.x -= (ply.x == 1 ? 1 : 2);
			else if (number == 23 && ply.x < 3)
				ply.x += (ply.x == 2) ? 1 : 2;
		} else {
			AI ai = ss.getAI(caster);
			if (direction == 0 && ai.x > 0)
				ai.x -= 1;
			else if (direction == 1 && ai.x < 3)
				ai.x += 1;
			else if (direction == 2 && ai.y > 0)
				ai.y -= 1;
			else if (direction == 3 && ai.y < (GameSystem.mode ? 4 : 2))
				ai.y += 1;
			else if (number == 22 && ai.x > 0)
				ai.x -= (ai.x == 1 ? 1 : 2);
			else if (number == 23 && ai.x < 3)
				ai.x += (ai.x == 2 ? 1 : 2);
		}
	}

	public void act(int caster, int sy, int sx) {
	}

}
