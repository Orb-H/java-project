package project.cards;

import project.system.*;

public class MoveCard extends Card {

    int direction = -1;// 0 for left, 1 for right, 2 for up, 3 for down

    public MoveCard(String name, int num, int dir, String[] ascii, String description) {
        super(CardType.MOVE, name, num, ascii, description, 0, 0);
        direction = dir;
    }

    public boolean inBound(int dy, int dx) {
        return true;
    }

    @Override
    public void act(int caster) {
        if (caster < 2) {
            Player ply = GameSystem.gs.getPlayer(caster);
            if (direction == 0 && ply.x > 0) ply.x -= 1;
            else if (direction == 1 && ply.x < (GameSystem.mode ? 4 : 3)) ply.x += 1;
            else if (direction == 2 && ply.y > 0) ply.y -= 1;
            else if (direction == 3 && ply.y < (GameSystem.mode ? 4 : 2)) ply.y += 1;
            else if (number == 22 && ply.x > 1) ply.x -= (ply.x == 1 ? 1 : 2);
            else if (number == 23) {
                if (!GameSystem.mode && ply.x < 3) ply.x += (ply.x == 2) ? 1 : 2;
                else if (GameSystem.mode && ply.x < 4) ply.x += (ply.x == 3) ? 1 : 2;
            }
        } else {
            AI ai = GameSystem.gs.getAI(caster);
            if (direction == 0 && ai.x > 0) ai.x -= 1;
            else if (direction == 1 && ai.x < (GameSystem.mode ? 4 : 3)) ai.x += 1;
            else if (direction == 2 && ai.y > 0) ai.y -= 1;
            else if (direction == 3 && ai.y < (GameSystem.mode ? 4 : 2)) ai.y += 1;
            else if (number == 22 && ai.x > 1) ai.x -= (ai.x == 1 ? 1 : 2);
            else if (number == 23) {
                if (!GameSystem.mode && ai.x < 3) ai.x += (ai.x == 2 ? 1 : 2);
                else if (GameSystem.mode && ai.x < 4) ai.x += (ai.x == 3) ? 1 : 2;
            }
        }
    }

    public void act(int caster, int sy, int sx) {
    }
}
