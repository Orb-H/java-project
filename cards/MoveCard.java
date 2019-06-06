package project.cards;

import project.system.GameSystem;

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
        if (caster == 1) {
            if (direction == 0 && GameSystem.ai.x > 0) GameSystem.ai.x -= 1;
            else if (direction == 1 && GameSystem.ai.x < 3) GameSystem.ai.x += 1;
            else if (direction == 2 && GameSystem.ai.y > 0) GameSystem.ai.y -= 1;
            else if (direction == 3 && GameSystem.ai.y < 2) GameSystem.ai.y += 1;
            else if (number == 22 && GameSystem.ai.x > 1) GameSystem.ai.x -= (GameSystem.ai.x == 1 ? 1 : 2);
            else if (number == 23 && GameSystem.ai.x < 3) GameSystem.ai.x += (GameSystem.ai.x == 2 ? 1 : 2);
        } else {
            if (direction == 0 && GameSystem.player.x > 0) GameSystem.player.x -= 1;
            else if (direction == 1 && GameSystem.player.x < 3) GameSystem.player.x += 1;
            else if (direction == 2 && GameSystem.player.y > 0) GameSystem.player.y -= 1;
            else if (direction == 3 && GameSystem.player.y < 2) GameSystem.player.y += 1;
            else if (number == 22 && GameSystem.player.x > 1)
                GameSystem.player.x -= (GameSystem.player.x == 1 ? 1 : 2);
            else if (number == 23 && GameSystem.player.x < 3)
                GameSystem.player.x += (GameSystem.player.x == 2 ? 1 : 2);
        }
    }

}
