package project.cards;

import java.util.Arrays;
import java.util.List;

import project.util.Point;
import project.system.*;

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
        Player[] ply;
        AI[] ai;
        if (GameSystem.mode) {
            ply = new Player[]{gs.getPlayer(0), gs.getPlayer(1)};
            ai = new AI[]{gs.getAI(2), gs.getAI(3)};
        } else {
            ply = new Player[]{gs.getPlayer(0)};
            ai = new AI[]{gs.getAI(2)};
        }
        if (caster < 2) {
            for (int i = 0; i < ai.length; ++i) {
                if (inBound(ai[i].y - ply[caster].y, ai[i].x - ply[caster].x)) {
                    dmg = deal + ai[i].shield;
                    ai[i].hp -= dmg > 0 ? dmg : 0;
                    ai[i].cnt_def++;
                }
            }
            ply[caster].mp -= cost;
            ply[caster].cnt_atk++;
        } else {
            for (int i = 0; i < ply.length; ++i) {
                if (inBound(ply[i].y - ai[caster - 2].y, ply[i].x - ai[caster - 2].x)) {
                    dmg = deal + ply[i].shield;
                    ply[i].hp -= dmg > 0 ? dmg : 0;
                    ply[i].cnt_def++;
                }
            }
            ai[caster - 2].mp -= cost;
            ai[caster - 2].cnt_atk++;
        }
    }

    public void act(int caster, int sy, int sx){}
}
