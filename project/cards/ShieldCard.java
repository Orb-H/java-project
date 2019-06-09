package project.cards;

import project.system.AI;
import project.system.Player;

public class ShieldCard extends Card {

	public ShieldCard(String name, int number, String[] ascii, String description, int deal, int cost) {
		super(CardType.SHIELD, name, number, ascii, description, deal, cost);
	}

	public boolean inBound(int dx, int dy) {
		return true;
	}

	public void act(int caster) {
		if (caster < 2) {
			Player ply = ss.getPlayer(caster);
			ply.shield = deal;
			ply.mp -= cost;
		} else {
			AI ai = ss.getAI(caster);
			ai.shield = deal;
			ai.mp -= cost;
		}
	}

	public void act(int caster, int sy, int sx) {
	}

}
