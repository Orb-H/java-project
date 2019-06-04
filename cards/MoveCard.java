package project.cards;

public class MoveCard extends Card {

	int direction = 0;// 0 for up, 1 for right, 2 for down, 3 for west

	public MoveCard(String name, int num, String[] ascii, String description) {
		super(CardType.MOVE, name, num, ascii, description, 0, 0);
	}

	@Override
	public void act() {

	}

}
