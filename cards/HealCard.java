package project.cards;

import project.system.*;

public class HealCard extends Card {

    public HealCard(String name, int num, String[] ascii, String description, int deal, int cost) {
        super(CardType.HEAL, name, num, ascii, description, deal, cost);
    }

    public boolean inBound(int dy, int dx) {
        return true;
    }

    @Override
    public void act(int caster) {
        if (caster < 2) {
            Player ply = gs.getPlayer(caster);
            if (number == 6) {
                Player target = gs.getPlayer(1 - caster);
                target.hp = (target.hp - deal > 100) ? 100 : target.hp - deal;
            } else
                ply.hp = (ply.hp - deal > 100) ? 100 : ply.hp - deal;
            ply.mp = (100 > ply.mp - cost) ? ply.mp - cost : 100;
        } else {
            AI ai = gs.getAI(caster);
            if (number == 6) {
                AI target = gs.getAI(5 - caster);
                target.hp = (target.hp > 100 + deal) ? 100 : target.hp - deal;
            } else
                ai.hp = (ai.hp > 100 + deal) ? 100 : ai.hp - deal;
            ai.mp = (ai.mp - cost > 100) ? 100 : ai.mp - cost;
        }
    }

    public void act(int caster, int sy, int sx) {
    }
}
