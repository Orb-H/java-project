package project.cards;

import project.system.GameSystem;

public class ShieldCard extends Card {
	public ShieldCard(String name, int num, String[] ascii, String description, int deal, int cost) {
		super(CardType.SHIELD, name, num, ascii, description, deal, cost);
	}

	public boolean inBound(int dy, int dx) {
		return true;
	}

	@Override
	public void act(int caster) {
		if(caster==1){
			GameSystem.ai.shield=deal;
			GameSystem.ai.mp-=cost;
		}
		else{
			GameSystem.player.shield=deal;
			GameSystem.player.mp-=cost;
		}
	}

}
