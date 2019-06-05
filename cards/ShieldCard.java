package project.cards;

public class ShieldCard extends Card {

	int cost;
	int shield;

	public ShieldCard(String name, int num, String[] ascii, String description, int deal, int cost) {
		super(CardType.SHIELD, name, num, ascii, description, deal, cost);
	}

	public boolean inBound(int dy, int dx) {
		return true;
	}

	@Override
	public void act() {

	}

}
