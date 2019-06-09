package project.cards;

import project.system.AI;
import project.system.GameSystem;
import project.system.Player;

public class HealCard extends Card {

	public HealCard(String name, int number, String[] ascii, String description, int deal, int cost) {
		super(CardType.HEAL, name, number, ascii, description, deal, cost);
	}

	public boolean inBound(int dx, int dy) {
		return true;
	}

	public void act(int caster) {
		if (caster < 2) {
			Player ply = ss.getPlayer(caster);
			if (number == 6) {
				Player target = ss.getPlayer(1 - caster);
				target.hp = (target.hp - deal > 100) ? 100 : target.hp - deal;
			} else
				ply.hp = (ply.hp - deal > 100) ? 100 : ply.hp - deal;
			ply.mp = (100 > ply.mp - cost) ? ply.mp - cost : 100;
		} else {
			AI ai = ss.getAI(caster);
			if (number == 6) {
				AI target = ss.getAI(5 - caster);
				target.hp = (target.hp > 100 + deal) ? 100 : target.hp - deal;
			} else
				ai.hp = (ai.hp > 100 + deal) ? 100 : ai.hp - deal;
			ai.mp = (ai.mp - cost > 100) ? 100 : ai.mp - cost;
		}
	}

	public void act(int caster, int sy, int sx) {
	}

}
