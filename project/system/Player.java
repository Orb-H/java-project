package project.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.cards.Card;
import project.util.StringUtils;

public class Player {

	public int x, y, hp, mp, shield;
	public int cnt_atk = 0, cnt_def = 0;
	boolean[] hand = new boolean[24];

	private GameSystem gs;

	public Player(int sy, int sx) {
		for (int i = 0; i < 24; ++i)
			getHand()[i] = false;
		x = sx;
		y = sy;
		hp = mp = 100;
		getCardFromDeck(4);

		gs = GameSystem.getInstance();
	}

	public void nextStage(int sy, int sx) {
		x = sx;
		y = sy;
		hp = mp = 100;
		getCardFromDeck(1);
	}

	public void getCardFromDeck(int cnt) {
		Random gen = new Random();
		int drawCard;
		for (int i = 0; i < cnt; ++i) {
			do {
				drawCard = gen.nextInt(24);
			} while (hand[drawCard]);
			if (drawCard == 4 || drawCard == 6 || drawCard == 8 || drawCard == 16 || drawCard == 18) {
				if (GameSystem.mode)
					hand[drawCard] = true;
				else
					--i;
			} else
				hand[drawCard] = true;
		}
	}

	public double calcavgCost() {
		double cost = 0;
		for (int i = 0; i < 24; ++i) {
			if (hand[i])
				cost += Card.cards.get(i + 6).getCost();
		}
		return cost / 4;
	}

	public void mulligan() {
		int errCode = 0;
		String b = gs.getInput("mulligan true/false : ");
		if (b.equals("true") || b.equals("True") || b.equals("TRUE") || b.equals("t") || b.equals("T")) {
			do {
				errCode = 0;
				String inputStr = gs.getInput("Submit cards' number to change with ascending order : ");
				int[] removedCard = StringUtils.Split2Int(inputStr, " ");
				if (removedCard == null) {
					errCode = 5;
					continue;
				}
				// Verifying removed card list
				for (int i = 0; i < removedCard.length - 1; ++i)
					for (int j = i + 1; j < removedCard.length; ++j)
						if (removedCard[i] == removedCard[j]) {
							System.out.println("You cannot remove the same card more than once");
							errCode = 4;
							break;
						}
				for (int i = 0; i < removedCard.length && errCode == 0; ++i) {
					if (removedCard[i] == 0) {
						System.out.println("You cannot remove \"0: The Fool card\" : Player Mulligan Error");
						errCode = 2;
						break;
					} else if (removedCard[i] > 24 || removedCard[i] < 0) {
						System.out.println("Card number should be in bound of 0-23 : Player Mulligan Error");
						errCode = 3;
						break;
					} else if (!hand[removedCard[i]]) {
						System.out.printf("You don't have %d card in your hand : Player Mulligan Error\n",
								removedCard[i]);
						errCode = 1;
						break;
					}
				}
				if (errCode == 0) {
					getCardFromDeck(removedCard.length);
					for (int i = 0; i < removedCard.length; ++i)
						hand[removedCard[i]] = false;
				}
			} while (errCode != 0);
			// System.out.println("Result of Mulligan");
			// show();
			// System.out.println();
		}
	}

	public List<Card> getCards() {
		List<Card> c = new ArrayList<>();
		c.addAll(Card.basicCards);

		for (int i = 0; i < 24; ++i) {
			if (hand[i]) {
				c.add(Card.cards.get(i + 6));
			}
		}

		return c;
	}

	public boolean[] getHand() {
		return hand;
	}

}
