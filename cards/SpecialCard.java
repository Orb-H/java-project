package project.cards;

import project.system.*;

public class SpecialCard extends Card {
    public SpecialCard(String name, int num, String[] ascii, String description, int deal, int cost) {
        super(CardType.SPECIAL, name, num, ascii, description, deal, cost);
    }

    public boolean inBound(int dy, int dx) {
        if (super.number == 8) {
            return dy == 0;
        }
        if (super.number == 10) {
            return dy == 0 && (dx == 0 || dx == 1);
        }
        if (super.number == 16) {
            return dx == 0;
        }
        if(super.number==17){
            return dy==0 && dx==0;
        }
        return true;
    }

    public void act(int caster) {
        int dmg;
        if (caster < 2) {
            Player ply = GameSystem.gs.getPlayer(caster);
            AI[] ai;
            if (GameSystem.mode) ai = new AI[]{GameSystem.gs.getAI(2), GameSystem.gs.getAI(3)};
            else ai = new AI[]{GameSystem.gs.getAI(2)};
            for (int i = 0; i < ai.length; ++i) {
                if (inBound(ai[i].y - ply.y, ai[i].x - ply.x)) {
                    if (number != 10) {
                        dmg = deal + ai[i].shield;
                        if (dmg < 0) dmg = 0;
                    } else dmg = (Math.random() < 0.5 ? 80 : -30);
                    ai[i].hp -= dmg;
                    if (number != 0) ai[i].cnt_def++;
                }
            }
            ply.mp -= cost;
            if (number != 0) ply.cnt_atk++;
        } else {
            AI ai = GameSystem.gs.getAI(caster);
            Player[] ply;
            if (GameSystem.mode) ply = new Player[]{GameSystem.gs.getPlayer(0), GameSystem.gs.getPlayer(1)};
            else ply = new Player[]{GameSystem.gs.getPlayer(0)};
            for (int i = 0; i < ply.length; ++i) {
                if (inBound(ply[i].y - ai.y, ply[i].x - ai.x)) {
                    if (number != 10) {
                        dmg = deal + ply[i].shield;
                        if (dmg < 0) dmg = 0;
                    } else dmg = (Math.random() < 0.5 ? 80 : -30);
                    ply[i].hp -= dmg;
                    if (number != 0) ply[i].cnt_def++;
                }
            }
            ai.mp -= cost;
            if (number != 0) ai.cnt_atk++;
        }
    }

    public void act(int caster, int sy, int sx){
        if (caster < 2) {
            Player ply = GameSystem.gs.getPlayer(caster);
            AI[] ai;
            if (GameSystem.mode) ai = new AI[]{GameSystem.gs.getAI(2), GameSystem.gs.getAI(3)};
            else ai = new AI[]{GameSystem.gs.getAI(2)};
            for (int i = 0; i < ai.length; ++i) {
                if (inBound(ai[i].y - sy, ai[i].x - sx)) {
                    ai[i].hp -= deal;
                    ai[i].cnt_def++;
                }
            }
            ply.mp -= cost;
            ply.cnt_atk++;
        } else {
            AI ai = GameSystem.gs.getAI(caster);
            Player[] ply;
            if (GameSystem.mode) ply = new Player[]{GameSystem.gs.getPlayer(0), GameSystem.gs.getPlayer(1)};
            else ply = new Player[]{GameSystem.gs.getPlayer(0)};
            for (int i = 0; i < ply.length; ++i) {
                if (inBound(ply[i].y - sy, ply[i].x - sx)) {
                    ply[i].hp -= deal;
                    ply[i].cnt_def++;
                }
            }
            ai.mp -= cost;
            ai.cnt_atk++;
        }
    }
}
