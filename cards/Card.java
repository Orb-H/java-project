package project.cards;

import java.util.ArrayList;
import java.util.List;

import project.util.Point;
import project.util.StringUtils;

public abstract class Card {

    public static final List<Card> cards = new ArrayList<>();

    protected int number; // -1 is Start Cards, others are drafted cards.
    public final CardType type;
    protected String name;
    protected String[] ascii;
    protected String description;
    protected final int priority;

    protected int deal;
    protected int cost;

    Card(CardType type, String name, int number, String[] ascii, String description, int deal, int cost) {
        this(type);
        this.name = name;
        this.number = number;
        this.ascii = ascii;
        this.description = description;
        this.deal = deal;
        this.cost = cost;
    }

    private Card(CardType type) {
        this.type = type;
        this.priority = type.ordinal();
    }

    static {
        // Starting Cards
        cards.add(new MoveCard("Left move", -1, 0, new String[]{}, "Move one cell leftward")); // 0
        cards.add(new MoveCard("Right move", -1, 1, new String[]{}, "Move one cell rightward")); // 1
        cards.add(new MoveCard("Up move", -1, 2, new String[]{}, "Move one cell upward")); // 2
        cards.add(new MoveCard("Down move", -1, 3, new String[]{}, "Move one cell downward")); // 3
        cards.add(new ShieldCard("Guard", -1, new String[]{}, "Reduces 15 damage in this turn", -15, 0)); // 4
        cards.add(new HealCard("Charge", -1, new String[]{}, "Heals 15 mana", 0, -15)); // 5

        // Additional Cards
        cards.add(new SpecialCard("0: The Fool", 0, new String[]{}, "Pass this turn with 15% chance, and heal 10 mana",
                0, -10)); // 6
        cards.add(new AttackCard("I: The Magician", 1, new String[]{},
                "Damages to entities in a square range with length 3, centered in player", 15, 15,
                new Point[]{Point.x_1y_1, Point.x_1y0, Point.x_1y1, Point.x0y_1, Point.x0y0, Point.x0y1, Point.x1y_1,
                        Point.x1y0, Point.x1y1})); // 7
        cards.add(new AttackCard("II: The High Priestess", 2, new String[]{},
                "Damages to entities in short cross(length 1) around player", 25, 30,
                new Point[]{Point.x_1y0, Point.x1y1, Point.x0y_1, Point.x0y1, Point.x0y0})); // 8
        cards.add(new AttackCard("III: The Empress", 3, new String[]{},
                "Damages to entities in short X-shaped range(length 1) around player", 25, 30,
                new Point[]{Point.x_1y_1, Point.x_1y1, Point.x1y_1, Point.x1y1, Point.x0y0})); // 9
        cards.add(new AttackCard("IV: The Emperor", 4, new String[]{},
                "Damages to entities in long X-shaped range(length 2) around player", 45, 80,
                new Point[]{Point.x_2y_2, Point.x_1y_1, Point.x_2y2, Point.x_1y1, Point.x2y_2, Point.x1y_1,
                        Point.x2y2, Point.x1y1, Point.x0y0})); // 10
        cards.add(new AttackCard("V: The Hierophant", 5, new String[]{},
                "Damages to entities in front of player, including diagonal direction, and including player's location",
                40, 40, new Point[]{Point.x0y0, Point.x1y_1, Point.x1y0, Point.x1y1})); // 11
        cards.add(new HealCard("VI: The Lovers", 6, new String[]{}, "Heals team ally", -30, 50)); // 12
        cards.add(new ShieldCard("VII: The Chariot", 7, new String[]{}, "Reduces 100 damage in this turn", -100, 30)); // 13
        cards.add(new SpecialCard("VIII: Strength", 8, new String[]{},
                "Attacks all entities which locates in same y-value", 20, 25)); // 14
        cards.add(new AttackCard("IX: The Hermit", 9, new String[]{}, "Damage to entities located 2 cell front", 45,
                60, new Point[]{Point.x_2y_1, Point.x_2y0, Point.x_2y1, Point.x2y_1, Point.x2y0, Point.x2y1})); // 15
        cards.add(new SpecialCard("X: Wheel of fortune", 10, new String[]{},
                "Either penetrate to entities front of player with 60% change, or  heal to entities with 20% change", 80,
                50)); // 16
        cards.add(new AttackCard("XI: Justice", 11, new String[]{},
                "Damage to entities in H-shaped range around player", 20, 30, new Point[]{Point.x_1y_1, Point.x1y_1,
                Point.x_1y0, Point.x0y0, Point.x1y0, Point.x_1y1, Point.x1y1})); // 17
        cards.add(
                new AttackCard("XII: Hanged man", 12, new String[]{}, "Damage to entities upper 3 cells around player",
                        25, 20, new Point[]{Point.x_1y_1, Point.x0y_1, Point.x1y_1, Point.x0y0})); // 18
        cards.add(new AttackCard("XIII: Death", 13, new String[]{},
                "Damage to entities which position is the same with player", 80, 100, new Point[]{Point.x0y0})); // 19
        cards.add(new AttackCard("XIV: Temperance", 14, new String[]{}, "Damage to entities front of player", 35, 30,
                new Point[]{Point.x_1y0, Point.x1y0})); // 20
        cards.add(new AttackCard("XV: The Devil", 15, new String[]{}, "Damage to entities downward of player", 30, 45,
                new Point[]{Point.x_1y0, Point.x1y0, Point.x_1y1, Point.x0y1, Point.x1y1})); // 21
        cards.add(new SpecialCard("XVI: The Tower", 16, new String[]{},
                "Attack all entities which locates in same x-value", 15, 20)); // 22
        cards.add(new SpecialCard("XVII: The Star", 17, new String[]{}, "Attack position wherever you want", 99, 100)); // 23
        cards.add(new AttackCard("XVIII: The Moon", 18, new String[]{},
                "Attacks all entities which Manhattan distance is even and smaller than 3", 15, 45,
                new Point[]{Point.x0y_2, Point.x1y_1, Point.x_1y_1, Point.x_2y0, Point.x2y0, Point.x0y0, Point.x_1y1,
                        Point.x_1y1, Point.x0y2})); // 24
        cards.add(new HealCard("XIX: The Sun", 19, new String[]{}, "Heads 30 hp", -30, 60)); // 25
        cards.add(new AttackCard("XX: Judgement", 20, new String[]{}, "Damage to entities in shape Íï around player",
                20, 30, new Point[]{Point.x_1y_1, Point.x0y_1, Point.x1y_1, Point.x0y0, Point.x_1y1, Point.x0y1,
                Point.x1y1})); // 26
        cards.add(new SpecialCard("XXI: The World", 21, new String[]{}, "Attack entire map", 15, 60)); // 27
        cards.add(new MoveCard("XXII: Left dash", 22, -1, new String[]{}, "Move two cells leftward")); // 28
        cards.add(new MoveCard("XXIII: Right dash", 23, -1, new String[]{}, "Move two cells rightward")); // 29
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

    public abstract boolean inBound(int dx, int dy);

    public abstract void act(int caster);
    public abstract void act(int caster, int sy, int sx);
}
