package project.cards;

import project.system.*;

public class ShieldCard extends Card {
    public ShieldCard(String name, int num, String[] ascii, String description, int deal, int cost) {
        super(CardType.SHIELD, name, num, ascii, description, deal, cost);
    }

    public boolean inBound(int dy, int dx) {
        return true;
    }

    @Override
    public void act(int caster) {
        if (caster < 2) {
            Player ply = GameSystem.gs.getPlayer(caster);
            ply.shield = deal;
            ply.mp -= cost;
        } else {
            AI ai = GameSystem.gs.getAI(caster);
            ai.shield = deal;
            ai.mp -= cost;
        }
    }

    public void act(int caster, int sy, int sx){}
}
