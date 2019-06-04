package project.cards;

import project.util.StringUtils;

public abstract class Card {
	protected int CardNumber; //-1 is Start Cards, others are drafted cards.
	protected final CardType type;
	protected String name;
	protected char[][] ascii;
	protected String description;
	protected final int priority;

	protected int deal;
	protected int cost;

	Card(CardType type, String name, int number, String[] ascii, String description, int deal, int cost) {
		this(type);
		this.name = name;
		CardNumber=number;
		for (int i = 0; i < ascii.length; i++) {
			this.ascii[i] = StringUtils.alignString(ascii[i]);
		}
		this.description = description;
		this.deal = deal;
		this.cost = cost;
	}

	private Card(CardType type) {
		this.type = type;
		this.priority = type.ordinal();
	}

	public String getName(){ return name; }

	public abstract void act();

}
