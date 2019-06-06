package project.cards;

import project.system.GameSystem;

public class HealCard extends Card {

	public HealCard(String name,int num, String[] ascii, String description, int deal, int cost) {
		super(CardType.HEAL, name, num, ascii, description, deal, cost);
	}

	public boolean inBound(int dy, int dx){
		return true;
	}

	@Override
	public void act(int caster) {
		if(caster==1){
			GameSystem.ai.hp-=deal;
			if (GameSystem.ai.hp>100) GameSystem.ai.hp=100;
			GameSystem.ai.mp-=cost;
		}
		else{
			GameSystem.player.hp-=deal;
			if(GameSystem.player.hp>100) GameSystem.player.hp=100;
			GameSystem.player.mp-=cost;
		}
	}

}
