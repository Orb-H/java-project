package project.cards;

import project.system.AI;
import project.system.GameSystem;
import project.system.Player;

public class SpecialCard extends Card {

	public SpecialCard(String name, int num, String[] ascii, String description, int deal, int cost) {
		super(CardType.SPECIAL, name, num, ascii, description, deal, cost);
	}

	public boolean inBound(int dy, int dx) {
		if (super.number == 8) {
			return dy == 0;
		}
		if (super.number == 10) {
			return dy == 0 && (dx == 0 || dx == 1);
		}
		if (super.number == 16) {
			return dx == 0;
		}
		if (super.number == 17) {
			return dy == 0 && dx == 0;
		}
		return true;
	}

	public void act(int caster) {
		int dmg;
		if (caster < 2) {
			Player ply = ss.getPlayer(caster);
			AI[] ai;
			if (GameSystem.mode)
				ai = new AI[] { ss.getAI(2), ss.getAI(3) };
			else
				ai = new AI[] { ss.getAI(2) };
			for (int i = 0; i < ai.length; ++i) {
				if (inBound(ai[i].y - ply.y, ai[i].x - ply.x)) {
					if (number != 10) {
						dmg = deal + ai[i].shield;
						if (dmg < 0)
							dmg = 0;
					} else
						dmg = (Math.random() < 0.5 ? 80 : -30);
					ai[i].hp -= dmg;
					if (number != 0)
						ai[i].cnt_def++;
				}
			}
			ply.mp = (ply.mp - cost > 100) ? 100 : ply.mp - cost;
			if (number != 0)
				ply.cnt_atk++;
		} else {
			AI ai = ss.getAI(caster);
			Player[] ply;
			if (GameSystem.mode)
				ply = new Player[] { ss.getPlayer(0), ss.getPlayer(1) };
			else
				ply = new Player[] { ss.getPlayer(0) };
			for (int i = 0; i < ply.length; ++i) {
				if (inBound(ply[i].y - ai.y, ply[i].x - ai.x)) {
					if (number != 10) {
						dmg = deal + ply[i].shield;
						if (dmg < 0)
							dmg = 0;
					} else
						dmg = (Math.random() < 0.5 ? 80 : -30);
					ply[i].hp -= dmg;
					if (number != 0)
						ply[i].cnt_def++;
				}
			}
			ai.mp = (ai.mp - cost > 100) ? 100 : ai.mp - cost;
			if (number != 0)
				ai.cnt_atk++;
		}
	}

	public void act(int caster, int sy, int sx) {
		if (caster < 2) {
			Player ply = ss.getPlayer(caster);
			AI[] ai;
			if (GameSystem.mode)
				ai = new AI[] { ss.getAI(2), ss.getAI(3) };
			else
				ai = new AI[] { ss.getAI(2) };
			for (int i = 0; i < ai.length; ++i) {
				if (inBound(ai[i].y - sy, ai[i].x - sx)) {
					ai[i].hp -= deal;
					ai[i].cnt_def++;
				}
			}
			ply.mp -= cost;
			ply.cnt_atk++;
		} else {
			AI ai = ss.getAI(caster);
			Player[] ply;
			if (GameSystem.mode)
				ply = new Player[] { ss.getPlayer(0), ss.getPlayer(1) };
			else
				ply = new Player[] { ss.getPlayer(0) };
			for (int i = 0; i < ply.length; ++i) {
				if (inBound(ply[i].y - sy, ply[i].x - sx)) {
					ply[i].hp -= deal;
					ply[i].cnt_def++;
				}
			}
			ai.mp -= cost;
			ai.cnt_atk++;
		}
	}

}
