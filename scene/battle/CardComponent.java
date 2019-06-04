package project.scene.battle;

import project.cards.Card;
import project.scene.Component;

public class CardComponent extends Component {

	Card c;

	protected CardComponent(int locX, int locY, int sizeX, int sizeY) {
		super(locX, locY, sizeX, sizeY);
	}

}
