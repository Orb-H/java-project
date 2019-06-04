package project.cards;
import java.util.Arrays;
import java.util.List;

import project.util.Point;

public class AttackCard extends Card {

	List<Point> range;

	public AttackCard(String name, int num, String[] ascii, String description, int deal, int cost, Point[] range) {
		super(CardType.ATTACK, name, num, ascii, description, deal, cost);
		this.range = Arrays.asList(range);
	}

	@Override
	public void act() {

	}

}
