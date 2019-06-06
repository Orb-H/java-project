package project.cards;

import java.util.Arrays;
import java.util.List;

import project.util.Point;
import project.system.GameSystem;

public class AttackCard extends Card {

    List<Point> range;

    public AttackCard(String name, int num, String[] ascii, String description, int deal, int cost, Point[] range) {
        super(CardType.ATTACK, name, num, ascii, description, deal, cost);
        this.range = Arrays.asList(range);
    }

    public boolean inBound(int dy, int dx) {
        return range.contains(new Point(dx, dy));
    }

    @Override
    public void act(int caster) {
        int dmg;
        if (caster == 1) {
            if (inBound(GameSystem.player.y - GameSystem.ai.y, GameSystem.player.x - GameSystem.ai.x)) {
                dmg = deal + GameSystem.player.shield;
                GameSystem.player.hp -= dmg > 0 ? dmg : 0;
                GameSystem.player.cnt_def++;
            }
            GameSystem.ai.mp -= cost;
            GameSystem.ai.cnt_atk++;
        } else {
            if (inBound(GameSystem.ai.y - GameSystem.player.y, GameSystem.ai.x - GameSystem.player.x)) {
                dmg = deal + GameSystem.ai.shield;
                GameSystem.ai.hp -= dmg > 0 ? dmg : 0;
                GameSystem.ai.cnt_def++;
            }
            GameSystem.player.mp -= cost;
            GameSystem.player.cnt_atk++;
        }
    }

}
