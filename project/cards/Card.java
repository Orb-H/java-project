package project.cards;

import java.util.ArrayList;
import java.util.List;

import project.scene.battle.SelectScene;
import project.system.GameSystem;
import project.util.Point;

public abstract class Card {

	public static final List<Card> cards = new ArrayList<>();
	public static final List<Card> basicCards = new ArrayList<>();

	protected int number; // -1 is Start Cards, others are drafted cards.
	protected final CardType type;
	protected String name;
	protected String[] ascii;
	protected String description;
	protected final int priority;

	protected int deal;
	protected int cost;

	protected GameSystem gs;
	protected SelectScene ss;

	Card(CardType type, String name, int number, String[] ascii, String description, int deal, int cost) {
		this(type);
		this.name = name;
		this.number = number;
		this.ascii = ascii;
		this.description = description;
		this.deal = deal;
		this.cost = cost;

		gs = GameSystem.getInstance();
		ss = SelectScene.getInstance();
	}

	private Card(CardType type) {
		this.type = type;
		this.priority = type.ordinal();
	}

	static {
		// Starting Cards
		cards.add(new MoveCard("Left move", -1,
				new String[] { "忙式式式式式式L式式式式式式忖", "弛             弛", "弛   //        弛", "弛  / 式式式式式式式  弛",
						"弛  \\ 式式式式式式式  弛", "弛   \\\\        弛", "弛             弛", "弛             弛",
						"弛   GO LEFT   弛", "戌式式式式式式式式式式式式式戎" },
				"Move one cell leftward", 0)); // 0
		cards.add(new MoveCard("Right move", -1,
				new String[] { "忙式式式式式式R式式式式式式忖", "弛             弛", "弛        \\\\   弛", "弛  式式式式式式式 \\  弛",
						"弛  式式式式式式式 /  弛", "弛        //   弛", "弛             弛", "弛             弛", "弛  GO RIGHT   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Move one cell rightward", 1)); // 1
		cards.add(new MoveCard("Up move", -1,
				new String[] { "忙式式式式式式U式式式式式式忖", "弛             弛", "弛     /\\      弛", "弛    /  \\     弛",
						"弛   //弛弛\\\\    弛", "弛     弛弛      弛", "弛     弛弛      弛", "弛             弛", "弛    GO UP    弛",
						"戌式式式式式式式式式式式式式戎" },
				"Move one cell upward", 2)); // 2
		cards.add(new MoveCard("Down move", -1,
				new String[] { "忙式式式式式式D式式式式式式忖", "弛             弛", "弛     弛弛      弛", "弛     弛弛      弛",
						"弛   \\\\弛弛//    弛", "弛    \\  /     弛", "弛     \\/      弛", "弛             弛",
						"弛   GO DOWN   弛", "戌式式式式式式式式式式式式式戎" },
				"Move one cell downward", 3)); // 3
		cards.add(new ShieldCard("Guard", -1,
				new String[] { "忙式式式式式式G式式式式式式忖", "弛      .      弛", "弛  .-'' ''-.  弛", "弛  弛   弛   弛  弛",
						"弛  弛 式式托式式 弛  弛", "弛   \\  弛  /   弛", "弛    \\ 弛 /    弛", "弛     '-'     弛", "弛    GUARD    弛",
						"戌式式式式式式式式式式式式式戎" },
				"Reduces 15 damage in this turn", -15, 0)); // 4
		cards.add(new HealCard("Charge", -1,
				new String[] { "忙式式式式式式C式式式式式式忖", "弛       /     弛", "弛      /弛     弛", "弛     / 弛     弛",
						"弛    /  戌式式   弛", "弛    式式忖  /   弛", "弛      弛 /    弛", "弛      弛/     弛", "弛 MANA CHARGE 弛",
						"戌式式式式式式式式式式式式式戎" },
				"Heals 15 mana", 0, -15)); // 5
		for (int i = 0; i < 6; i++) {
			basicCards.add(cards.get(i));
		}

		// Additional Cards
		cards.add(new SpecialCard("0: The Fool", 0,
				new String[] { "忙式式式式式式0式式式式式式忖", "弛 忙式忖 忙忖 忙式忖  弛", "弛 戌忖弛 弛弛 弛忙戎  弛", "弛  弛戌式戎戌式戎弛   弛",
						"弛  弛忙式忖忙式忖弛   弛", "弛 忙戎弛 弛弛 弛戌忖  弛", "弛 戌式戎.''.戌式戎  弛", "弛    '..'     弛", "弛  THE FOOL   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Pass this turn with 15% chance, and heal 10 mana", 0, -10)); // 6
		cards.add(new AttackCard("I: The Magician", 1,
				new String[] { "忙式式式式式式I式式式式式式忖", "弛    \\___/    弛", "弛   .'   '.   弛", "弛  /       \\  弛",
						"弛  \\       /  弛", "弛   '._ _.'   弛", "弛      弛      弛", "弛     式托式     弛", "弛THE MAGICIAN 弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damages to entities in a square range with length 3, centered in player", 15, 15,
				new Point[] { Point.x_1y_1, Point.x_1y0, Point.x_1y1, Point.x0y_1, Point.x0y0, Point.x0y1, Point.x1y_1,
						Point.x1y0, Point.x1y1 })); // 7
		cards.add(new AttackCard("II: The High Priestess", 2,
				new String[] { "忙式式式式式II式式式式式式忖", "弛     _.._    弛", "弛   .' .-'`   弛", "弛  /  /       弛",
						"弛  |  |       弛", "弛  \\  \\       弛", "弛   '._'-._   弛", "弛      ```    弛", "弛HIGH PRIESTESS",
						"戌式式式式式式式式式式式式式戎" },
				"Damages to entities in short cross(length 1) around player", 25, 30,
				new Point[] { Point.x_1y0, Point.x_1y1, Point.x0y_1, Point.x0y1, Point.x0y0 })); // 8
		cards.add(new AttackCard("III: The Empress", 3,
				new String[] { "忙式式式式式III式式式式式忖", "弛     ___     弛", "弛   .'   '.   弛", "弛  /       \\  弛",
						"弛  \\       /  弛", "弛   '._ _.'   弛", "弛      弛      弛", "弛     式托式     弛", "弛 THE EMPRESS 弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damages to entities in short X-shaped range(length 1) around player", 25, 30,
				new Point[] { Point.x_1y_1, Point.x_1y1, Point.x1y_1, Point.x1y1, Point.x0y0 })); // 9
		cards.add(new AttackCard("IV: The Emperor", 4,
				new String[] { "忙式式式式式IV式式式式式式忖", "弛             弛", "弛  .��   ��.  弛", "弛 /   \\ /   \\ 弛",
						"弛      Y      弛", "弛      弛      弛", "弛      弛      弛", "弛             弛", "弛 THE EMPEROR 弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damages to entities in long X-shaped range(length 2) around player", 45, 80,
				new Point[] { Point.x_2y_2, Point.x_1y_1, Point.x_2y2, Point.x_1y1, Point.x2y_2, Point.x1y_1,
						Point.x2y2, Point.x1y1, Point.x0y0 })); // 10
		cards.add(new AttackCard("V: The Hierophant", 5,
				new String[] { "忙式式式式式式V式式式式式式忖", "弛 ''.     .'' 弛", "弛    '...'    弛", "弛   .'   '.   弛",
						"弛  /       \\  弛", "弛  \\       /  弛", "弛   '.   .'   弛", "弛     '''     弛", "弛THE HIEROPHANT",
						"戌式式式式式式式式式式式式式戎" },
				"Damages to entities in front of player, including diagonal direction, and including player's location",
				40, 40, new Point[] { Point.x0y0, Point.x1y_1, Point.x1y0, Point.x1y1 })); // 11
		cards.add(new HealCard("VI: The Lovers", 6,
				new String[] { "忙式式式式式VI式式式式式式忖", "弛 忙式式式式式式式式式忖 弛", "弛 戌式忖 忙式忖 忙式戎 弛", "弛   弛 弛 弛 弛   弛",
						"弛   弛 弛 弛 弛   弛", "弛   弛 弛 弛 弛   弛", "弛 忙式戎 戌式戎 戌式忖 弛", "弛 戌式式式式式式式式式戎 弛", "弛 THE LOVERS  弛",
						"戌式式式式式式式式式式式式式戎" },
				"Heals team ally", -30, 50)); // 12
		cards.add(new ShieldCard("VII: The Chariot", 7,
				new String[] { "忙式式式式式VII式式式式式忖", "弛             弛", "弛  ..-''''-.  弛", "弛 /  \\      ' 弛",
						"弛 \\../   /''\\ 弛", "弛 .      \\  / 弛", "弛  '-....-''  弛", "弛             弛",
						"弛 THE CHARIOT 弛", "戌式式式式式式式式式式式式式戎" },
				"Reduces 100 damage in this turn", -100, 30)); // 13
		cards.add(new SpecialCard("VIII: Strength", 8,
				new String[] { "忙式式式式VIII式式式式式忖", "弛    /''\\     弛", "弛   弛    弛    弛", "弛   _\\  /     弛",
						"弛  /  \\ 弛     弛", "弛  \\../ 弛     弛", "弛        \\_/  弛", "弛             弛",
						"弛  STRENGTH   弛", "戌式式式式式式式式式式式式式戎" },
				"Attacks all entities which locates in same y-value", 20, 25)); // 14
		cards.add(new AttackCard("IX: The Hermit", 9,
				new String[] { "忙式式式式式IX式式式式式式忖", "弛    .  .     弛", "弛 \\ / 弛/ 弛    弛", "弛  乓  乓  弛/\\  弛",
						"弛  弛  弛  乓  弛 弛", "弛  弛  弛  弛 /  弛", "弛  弛  弛  弛/   弛", "弛        仳    弛", "弛 THE HERMIT  弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities located 2 cell front", 45, 60,
				new Point[] { Point.x_2y_1, Point.x_2y0, Point.x_2y1, Point.x2y_1, Point.x2y0, Point.x2y1 })); // 15
		cards.add(new SpecialCard("X: Wheel of fortune", 10,
				new String[] { "忙式式式式式式X式式式式式式忖", "弛 -'''\\   弛   弛", "弛 \\    |  弛   弛", "弛     /   弛   弛",
						"弛    /    弛   弛", "弛   '式式式式式托式式 弛", "弛         弛   弛", "弛   WHEEL OF  弛", "弛   FORTUNE   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Either damages to entities front of player with 80% change, or  heal to entities with 20% change", 80,
				50)); // 16
		cards.add(new AttackCard("XI: Justice", 11,
				new String[] { "忙式式式式式XI式式式式式式忖", "弛             弛", "弛    .'''.    弛", "弛   /     \\   弛",
						"弛  弛       弛  弛", "弛   \\     /   弛", "弛 式式式'   '式式式 弛", "弛 式式式式式式式式式式式 弛", "弛   JUSTICE   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities in H-shaped range around player", 20, 30, new Point[] { Point.x_1y_1, Point.x1y_1,
						Point.x_1y0, Point.x0y0, Point.x1y0, Point.x_1y1, Point.x1y1 })); // 17
		cards.add(new AttackCard("XII: Hanged man", 12,
				new String[] { "忙式式式式式XII式式式式式忖", "弛  ^   ^   ^  弛", "弛  弛   弛   弛  弛", "弛  弛   弛   弛  弛",
						"弛   \\  弛  /   弛", "弛    '式托式'    弛", "弛     式托式     弛", "弛      弛      弛", "弛THE HANGED MAN",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities upper 3 cells around player", 25, 20,
				new Point[] { Point.x_1y_1, Point.x0y_1, Point.x1y_1, Point.x0y0 })); // 18
		cards.add(new AttackCard("XIII: Death", 13,
				new String[] { "忙式式式式XIII式式式式式忖", "弛    .  .     弛", "弛 \\ / 弛/ 弛    弛", "弛  乓  乓  弛    弛",
						"弛  弛  弛  弛    弛", "弛  弛  弛  弛    弛", "弛  弛  弛  弛    弛", "弛        戌式式ˇ弛", "弛    DEATH    弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities which position is the same with player", 80, 100, new Point[] { Point.x0y0 })); // 19
		cards.add(new AttackCard("XIV: Temperance", 14,
				new String[] { "忙式式式式式XIV式式式式式忖", "弛      ___    弛", "弛        /\\   弛", "弛   -.  /     弛",
						"弛     'X.     弛", "弛     /  '-   弛", "弛    /        弛", "弛             弛", "弛  TEMPERANCE 弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities front of player", 35, 30, new Point[] { Point.x_1y0, Point.x1y0 })); // 20
		cards.add(new AttackCard("XV: The Devil", 15,
				new String[] { "忙式式式式式XV式式式式式式忖", "弛             弛", "弛'.   .'.     弛", "弛  \\ /  '     弛",
						"弛   Y   弛 . . 弛", "弛        X   弛弛", "弛     ..' ' ' 弛", "弛             弛", "弛  THE DEVIL  弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities downward of player", 30, 45,
				new Point[] { Point.x_1y0, Point.x1y0, Point.x_1y1, Point.x0y1, Point.x1y1 })); // 21
		cards.add(new SpecialCard("XVI: The Tower", 16,
				new String[] { "忙式式式式式XVI式式式式式忖", "弛       式式忖   弛", "弛        /弛   弛", "弛   ..../     弛",
						"弛  /    \\     弛", "弛 弛      弛    弛", "弛  \\    /     弛", "弛   ''''      弛", "弛 THE TOWER   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Attack all entities which locates in same x-value", 15, 20)); // 22
		cards.add(new SpecialCard("XVII: The Star", 17,
				new String[] { "忙式式式式XVII式式式式式忖", "弛             弛", "弛             弛", "弛.-'\\.-'\\.-'\\ 弛",
						"弛             弛", "弛.-'\\.-'\\.-'\\ 弛", "弛             弛", "弛             弛",
						"弛  THE STAR   弛", "戌式式式式式式式式式式式式式戎" },
				"Attack position wherever you want", 99, 100)); // 23
		cards.add(new AttackCard("XVIII: The Moon", 18,
				new String[] { "忙式式式式XVIII式式式式忖", "弛 -.       .- 弛", "弛   \\     /   弛", "弛    弛   弛    弛",
						"弛  式式托式式式托式式  弛", "弛    弛   弛    弛", "弛   /     \\   弛", "弛 -'       '- 弛", "弛  THE MOON   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Attacks all entities which Manhattan distance is even and smaller than 3", 15, 45,
				new Point[] { Point.x0y_2, Point.x1y_1, Point.x_1y_1, Point.x_2y0, Point.x2y0, Point.x0y0, Point.x_1y1,
						Point.x_1y1, Point.x0y2 })); // 24
		cards.add(new HealCard("XIX: The Sun", 19,
				new String[] { "忙式式式式式XIX式式式式式忖", "弛             弛", "弛   .'''''.   弛", "弛  /       \\  弛",
						"弛 弛    O    弛 弛", "弛  \\       /  弛", "弛   '.....'   弛", "弛             弛", "弛   THE SUN   弛",
						"戌式式式式式式式式式式式式式戎" },
				"Heads 30 hp", -30, 60)); // 25
		cards.add(new AttackCard("XX: Judgement", 20,
				new String[] { "忙式式式式式XX式式式式式式忖", "弛 忙式式式式式式.    弛", "弛 弛       '.  弛", "弛 弛       .'  弛",
						"弛 戍式式式式式式'    弛", "弛 弛           弛", "弛 弛           弛", "弛 戌式式式式式式式式   弛", "弛  JUDGEMENT  弛",
						"戌式式式式式式式式式式式式式戎" },
				"Damage to entities in shape 俬 around player", 20, 30, new Point[] { Point.x_1y_1, Point.x0y_1,
						Point.x1y_1, Point.x0y0, Point.x_1y1, Point.x0y1, Point.x1y1 })); // 26
		cards.add(new SpecialCard("XXI: The World", 21,
				new String[] { "忙式式式式式XXI式式式式式忖", "弛  式托式        弛", "弛   弛 .''.    弛", "弛   戍'    :   弛",
						"弛   弛    .'   弛", "弛   弛  .'     弛", "弛      '..    弛", "弛             弛", "弛  THE WORLD  弛",
						"戌式式式式式式式式式式式式式戎" },
				"Attack entire map", 15, 60)); // 27
		cards.add(new MoveCard("XXII: Left dash", 22,
				new String[] { "忙式式式式XXII式式式式式忖", "弛             弛", "弛  // //      弛", "弛 /式式/式式式式式式  弛",
						"弛 \\式式\\式式式式式式  弛", "弛  \\\\ \\\\      弛", "弛             弛", "弛             弛",
						"弛  LEFT DASH  弛", "戌式式式式式式式式式式式式式戎" },
				"Move two cells leftward", 0)); // 28
		cards.add(new MoveCard("XXIII: Right dash", 23,
				new String[] { "忙式式式式XXIII式式式式忖", "弛             弛", "弛     \\\\ \\\\   弛", "弛 式式式式式式\\式式\\  弛",
						"弛 式式式式式式/式式/  弛", "弛     // //   弛", "弛             弛", "弛             弛", "弛 RIGHT DASH  弛",
						"戌式式式式式式式式式式式式式戎" },
				"Move two cells rightward", 1)); // 29
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public int getDeal() {
		return deal;
	}

	public String[] getAscii() {
		return ascii;
	}

	public int getPriority() {
		return priority;
	}

	public abstract boolean inBound(int dx, int dy);

	public abstract void act(int caster);

	public abstract void act(int caster, int sy, int sx);

}
