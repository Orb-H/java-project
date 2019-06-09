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
				new String[] { "��������������L��������������", "��             ��", "��   //        ��", "��  / ��������������  ��",
						"��  \\ ��������������  ��", "��   \\\\        ��", "��             ��", "��             ��",
						"��   GO LEFT   ��", "������������������������������" },
				"Move one cell leftward", 0)); // 0
		cards.add(new MoveCard("Right move", -1,
				new String[] { "��������������R��������������", "��             ��", "��        \\\\   ��", "��  �������������� \\  ��",
						"��  �������������� /  ��", "��        //   ��", "��             ��", "��             ��", "��  GO RIGHT   ��",
						"������������������������������" },
				"Move one cell rightward", 1)); // 1
		cards.add(new MoveCard("Up move", -1,
				new String[] { "��������������U��������������", "��             ��", "��     /\\      ��", "��    /  \\     ��",
						"��   //����\\\\    ��", "��     ����      ��", "��     ����      ��", "��             ��", "��    GO UP    ��",
						"������������������������������" },
				"Move one cell upward", 2)); // 2
		cards.add(new MoveCard("Down move", -1,
				new String[] { "��������������D��������������", "��             ��", "��     ����      ��", "��     ����      ��",
						"��   \\\\����//    ��", "��    \\  /     ��", "��     \\/      ��", "��             ��",
						"��   GO DOWN   ��", "������������������������������" },
				"Move one cell downward", 3)); // 3
		cards.add(new ShieldCard("Guard", -1,
				new String[] { "��������������G��������������", "��      .      ��", "��  .-'' ''-.  ��", "��  ��   ��   ��  ��",
						"��  �� ���������� ��  ��", "��   \\  ��  /   ��", "��    \\ �� /    ��", "��     '-'     ��", "��    GUARD    ��",
						"������������������������������" },
				"Reduces 15 damage in this turn", -15, 0)); // 4
		cards.add(new HealCard("Charge", -1,
				new String[] { "��������������C��������������", "��       /     ��", "��      /��     ��", "��     / ��     ��",
						"��    /  ������   ��", "��    ������  /   ��", "��      �� /    ��", "��      ��/     ��", "�� MANA CHARGE ��",
						"������������������������������" },
				"Heals 15 mana", 0, -15)); // 5
		for (int i = 0; i < 6; i++) {
			basicCards.add(cards.get(i));
		}

		// Additional Cards
		cards.add(new SpecialCard("0: The Fool", 0,
				new String[] { "��������������0��������������", "�� ������ ���� ������  ��", "�� ������ ���� ������  ��", "��  ����������������   ��",
						"��  ����������������   ��", "�� ������ ���� ������  ��", "�� ������.''.������  ��", "��    '..'     ��", "��  THE FOOL   ��",
						"������������������������������" },
				"Pass this turn with 15% chance, and heal 10 mana", 0, -10)); // 6
		cards.add(new AttackCard("I: The Magician", 1,
				new String[] { "��������������I��������������", "��    \\___/    ��", "��   .'   '.   ��", "��  /       \\  ��",
						"��  \\       /  ��", "��   '._ _.'   ��", "��      ��      ��", "��     ������     ��", "��THE MAGICIAN ��",
						"������������������������������" },
				"Damages to entities in a square range with length 3, centered in player", 15, 15,
				new Point[] { Point.x_1y_1, Point.x_1y0, Point.x_1y1, Point.x0y_1, Point.x0y0, Point.x0y1, Point.x1y_1,
						Point.x1y0, Point.x1y1 })); // 7
		cards.add(new AttackCard("II: The High Priestess", 2,
				new String[] { "������������II��������������", "��     _.._    ��", "��   .' .-'`   ��", "��  /  /       ��",
						"��  |  |       ��", "��  \\  \\       ��", "��   '._'-._   ��", "��      ```    ��", "��HIGH PRIESTESS",
						"������������������������������" },
				"Damages to entities in short cross(length 1) around player", 25, 30,
				new Point[] { Point.x_1y0, Point.x_1y1, Point.x0y_1, Point.x0y1, Point.x0y0 })); // 8
		cards.add(new AttackCard("III: The Empress", 3,
				new String[] { "������������III������������", "��     ___     ��", "��   .'   '.   ��", "��  /       \\  ��",
						"��  \\       /  ��", "��   '._ _.'   ��", "��      ��      ��", "��     ������     ��", "�� THE EMPRESS ��",
						"������������������������������" },
				"Damages to entities in short X-shaped range(length 1) around player", 25, 30,
				new Point[] { Point.x_1y_1, Point.x_1y1, Point.x1y_1, Point.x1y1, Point.x0y0 })); // 9
		cards.add(new AttackCard("IV: The Emperor", 4,
				new String[] { "������������IV��������������", "��             ��", "��  .��   ��.  ��", "�� /   \\ /   \\ ��",
						"��      Y      ��", "��      ��      ��", "��      ��      ��", "��             ��", "�� THE EMPEROR ��",
						"������������������������������" },
				"Damages to entities in long X-shaped range(length 2) around player", 45, 80,
				new Point[] { Point.x_2y_2, Point.x_1y_1, Point.x_2y2, Point.x_1y1, Point.x2y_2, Point.x1y_1,
						Point.x2y2, Point.x1y1, Point.x0y0 })); // 10
		cards.add(new AttackCard("V: The Hierophant", 5,
				new String[] { "��������������V��������������", "�� ''.     .'' ��", "��    '...'    ��", "��   .'   '.   ��",
						"��  /       \\  ��", "��  \\       /  ��", "��   '.   .'   ��", "��     '''     ��", "��THE HIEROPHANT",
						"������������������������������" },
				"Damages to entities in front of player, including diagonal direction, and including player's location",
				40, 40, new Point[] { Point.x0y0, Point.x1y_1, Point.x1y0, Point.x1y1 })); // 11
		cards.add(new HealCard("VI: The Lovers", 6,
				new String[] { "������������VI��������������", "�� ���������������������� ��", "�� ������ ������ ������ ��", "��   �� �� �� ��   ��",
						"��   �� �� �� ��   ��", "��   �� �� �� ��   ��", "�� ������ ������ ������ ��", "�� ���������������������� ��", "�� THE LOVERS  ��",
						"������������������������������" },
				"Heals team ally", -30, 50)); // 12
		cards.add(new ShieldCard("VII: The Chariot", 7,
				new String[] { "������������VII������������", "��             ��", "��  ..-''''-.  ��", "�� /  \\      ' ��",
						"�� \\../   /''\\ ��", "�� .      \\  / ��", "��  '-....-''  ��", "��             ��",
						"�� THE CHARIOT ��", "������������������������������" },
				"Reduces 100 damage in this turn", -100, 30)); // 13
		cards.add(new SpecialCard("VIII: Strength", 8,
				new String[] { "����������VIII������������", "��    /''\\     ��", "��   ��    ��    ��", "��   _\\  /     ��",
						"��  /  \\ ��     ��", "��  \\../ ��     ��", "��        \\_/  ��", "��             ��",
						"��  STRENGTH   ��", "������������������������������" },
				"Attacks all entities which locates in same y-value", 20, 25)); // 14
		cards.add(new AttackCard("IX: The Hermit", 9,
				new String[] { "������������IX��������������", "��    .  .     ��", "�� \\ / ��/ ��    ��", "��  ��  ��  ��/\\  ��",
						"��  ��  ��  ��  �� ��", "��  ��  ��  �� /  ��", "��  ��  ��  ��/   ��", "��        ��    ��", "�� THE HERMIT  ��",
						"������������������������������" },
				"Damage to entities located 2 cell front", 45, 60,
				new Point[] { Point.x_2y_1, Point.x_2y0, Point.x_2y1, Point.x2y_1, Point.x2y0, Point.x2y1 })); // 15
		cards.add(new SpecialCard("X: Wheel of fortune", 10,
				new String[] { "��������������X��������������", "�� -'''\\   ��   ��", "�� \\    |  ��   ��", "��     /   ��   ��",
						"��    /    ��   ��", "��   '���������������� ��", "��         ��   ��", "��   WHEEL OF  ��", "��   FORTUNE   ��",
						"������������������������������" },
				"Either damages to entities front of player with 80% change, or  heal to entities with 20% change", 80,
				50)); // 16
		cards.add(new AttackCard("XI: Justice", 11,
				new String[] { "������������XI��������������", "��             ��", "��    .'''.    ��", "��   /     \\   ��",
						"��  ��       ��  ��", "��   \\     /   ��", "�� ������'   '������ ��", "�� ���������������������� ��", "��   JUSTICE   ��",
						"������������������������������" },
				"Damage to entities in H-shaped range around player", 20, 30, new Point[] { Point.x_1y_1, Point.x1y_1,
						Point.x_1y0, Point.x0y0, Point.x1y0, Point.x_1y1, Point.x1y1 })); // 17
		cards.add(new AttackCard("XII: Hanged man", 12,
				new String[] { "������������XII������������", "��  ^   ^   ^  ��", "��  ��   ��   ��  ��", "��  ��   ��   ��  ��",
						"��   \\  ��  /   ��", "��    '������'    ��", "��     ������     ��", "��      ��      ��", "��THE HANGED MAN",
						"������������������������������" },
				"Damage to entities upper 3 cells around player", 25, 20,
				new Point[] { Point.x_1y_1, Point.x0y_1, Point.x1y_1, Point.x0y0 })); // 18
		cards.add(new AttackCard("XIII: Death", 13,
				new String[] { "����������XIII������������", "��    .  .     ��", "�� \\ / ��/ ��    ��", "��  ��  ��  ��    ��",
						"��  ��  ��  ��    ��", "��  ��  ��  ��    ��", "��  ��  ��  ��    ��", "��        ����������", "��    DEATH    ��",
						"������������������������������" },
				"Damage to entities which position is the same with player", 80, 100, new Point[] { Point.x0y0 })); // 19
		cards.add(new AttackCard("XIV: Temperance", 14,
				new String[] { "������������XIV������������", "��      ___    ��", "��        /\\   ��", "��   -.  /     ��",
						"��     'X.     ��", "��     /  '-   ��", "��    /        ��", "��             ��", "��  TEMPERANCE ��",
						"������������������������������" },
				"Damage to entities front of player", 35, 30, new Point[] { Point.x_1y0, Point.x1y0 })); // 20
		cards.add(new AttackCard("XV: The Devil", 15,
				new String[] { "������������XV��������������", "��             ��", "��'.   .'.     ��", "��  \\ /  '     ��",
						"��   Y   �� . . ��", "��        X   ����", "��     ..' ' ' ��", "��             ��", "��  THE DEVIL  ��",
						"������������������������������" },
				"Damage to entities downward of player", 30, 45,
				new Point[] { Point.x_1y0, Point.x1y0, Point.x_1y1, Point.x0y1, Point.x1y1 })); // 21
		cards.add(new SpecialCard("XVI: The Tower", 16,
				new String[] { "������������XVI������������", "��       ������   ��", "��        /��   ��", "��   ..../     ��",
						"��  /    \\     ��", "�� ��      ��    ��", "��  \\    /     ��", "��   ''''      ��", "�� THE TOWER   ��",
						"������������������������������" },
				"Attack all entities which locates in same x-value", 15, 20)); // 22
		cards.add(new SpecialCard("XVII: The Star", 17,
				new String[] { "����������XVII������������", "��             ��", "��             ��", "��.-'\\.-'\\.-'\\ ��",
						"��             ��", "��.-'\\.-'\\.-'\\ ��", "��             ��", "��             ��",
						"��  THE STAR   ��", "������������������������������" },
				"Attack position wherever you want", 99, 100)); // 23
		cards.add(new AttackCard("XVIII: The Moon", 18,
				new String[] { "����������XVIII����������", "�� -.       .- ��", "��   \\     /   ��", "��    ��   ��    ��",
						"��  ������������������  ��", "��    ��   ��    ��", "��   /     \\   ��", "�� -'       '- ��", "��  THE MOON   ��",
						"������������������������������" },
				"Attacks all entities which Manhattan distance is even and smaller than 3", 15, 45,
				new Point[] { Point.x0y_2, Point.x1y_1, Point.x_1y_1, Point.x_2y0, Point.x2y0, Point.x0y0, Point.x_1y1,
						Point.x_1y1, Point.x0y2 })); // 24
		cards.add(new HealCard("XIX: The Sun", 19,
				new String[] { "������������XIX������������", "��             ��", "��   .'''''.   ��", "��  /       \\  ��",
						"�� ��    O    �� ��", "��  \\       /  ��", "��   '.....'   ��", "��             ��", "��   THE SUN   ��",
						"������������������������������" },
				"Heads 30 hp", -30, 60)); // 25
		cards.add(new AttackCard("XX: Judgement", 20,
				new String[] { "������������XX��������������", "�� ��������������.    ��", "�� ��       '.  ��", "�� ��       .'  ��",
						"�� ��������������'    ��", "�� ��           ��", "�� ��           ��", "�� ������������������   ��", "��  JUDGEMENT  ��",
						"������������������������������" },
				"Damage to entities in shape �� around player", 20, 30, new Point[] { Point.x_1y_1, Point.x0y_1,
						Point.x1y_1, Point.x0y0, Point.x_1y1, Point.x0y1, Point.x1y1 })); // 26
		cards.add(new SpecialCard("XXI: The World", 21,
				new String[] { "������������XXI������������", "��  ������        ��", "��   �� .''.    ��", "��   ��'    :   ��",
						"��   ��    .'   ��", "��   ��  .'     ��", "��      '..    ��", "��             ��", "��  THE WORLD  ��",
						"������������������������������" },
				"Attack entire map", 15, 60)); // 27
		cards.add(new MoveCard("XXII: Left dash", 22,
				new String[] { "����������XXII������������", "��             ��", "��  // //      ��", "�� /����/������������  ��",
						"�� \\����\\������������  ��", "��  \\\\ \\\\      ��", "��             ��", "��             ��",
						"��  LEFT DASH  ��", "������������������������������" },
				"Move two cells leftward", 0)); // 28
		cards.add(new MoveCard("XXIII: Right dash", 23,
				new String[] { "����������XXIII����������", "��             ��", "��     \\\\ \\\\   ��", "�� ������������\\����\\  ��",
						"�� ������������/����/  ��", "��     // //   ��", "��             ��", "��             ��", "�� RIGHT DASH  ��",
						"������������������������������" },
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
