package project.scene.battle;

import project.cards.Card;
import project.scene.DynamicComponent;

public class CardComponent extends DynamicComponent {

	protected CardComponent(int locX, int locY, Card c) {
		super(locX, locY, 15, 10);
		setCard(c);
	}

	public void setCard(Card c) {
		if (c == null) {
			changeRender(new String[] { "忙式式式式式式式式式式式式式忖", "弛             弛", "弛             弛", "弛             弛",
					"弛             弛", "弛             弛", "弛             弛", "弛             弛", "弛             弛",
					"戌式式式式式式式式式式式式式戎" });
		} else {
			changeRender(c.getAscii());
		}
	}

}
