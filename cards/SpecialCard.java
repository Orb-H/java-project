package project.cards;

import project.system.GameSystem;

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
        return true;
    }

    public void act(int caster) {
        int dmg;
        if (caster == 1) {
            if (inBound(GameSystem.player.y - GameSystem.ai.y, GameSystem.player.x - GameSystem.ai.x)) {
                if (number != 10) {
                    dmg = deal + GameSystem.player.shield;
                    if (dmg < 0) dmg = 0;
                } else dmg = (Math.random() < 0.5 ? 80 : -30);
                GameSystem.player.hp -= dmg;
                if(number!=0) GameSystem.player.cnt_def++;
            }
            GameSystem.ai.mp-=cost;
            if(number!=0) GameSystem.ai.cnt_atk++;
        } else {
            if (inBound(GameSystem.ai.y - GameSystem.player.y, GameSystem.ai.x - GameSystem.player.x)) {
                if (number != 10) {
                    dmg = deal + GameSystem.ai.shield;
                    if (dmg < 0) dmg = 0;
                } else dmg = (Math.random() < 0.5 ? 80 : -30);
                GameSystem.ai.hp -= dmg;
                if(number!=0) GameSystem.ai.cnt_def++;
            }
            GameSystem.player.mp-=cost;
            if(number!=0) GameSystem.player.cnt_atk++;
        }
    }
}
