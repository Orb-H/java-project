package project.cards;

public class HealCard extends Card {

	public HealCard(String name,int num, String[] ascii, String description, int deal, int cost) {
		super(CardType.HEAL, name, num, ascii, description, deal, cost);
	}

	public boolean inBound(int dy, int dx){
		return true;
	}

	@Override
	public void act() {

	}

}
