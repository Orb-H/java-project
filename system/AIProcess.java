package project.system;

import java.util.concurrent.Callable;
import java.util.List;

import project.cards.*;

public class AIProcess implements Callable<int[]> {
    ProcessLock Lock;
    AI ai;

    public AIProcess(AI p) {
        ai = p;
    }

    public int[] call() {
        //busy waiting
        while (Lock.checkAILock()) ;
        if (!ai.is2v2) {
            switch (ai.stage) {
                case 1:
                    Lock.EndAITurn();
                    return Stage1AI();
                case 2:
                    Lock.EndAITurn();
                    return Stage2AI();
                case 3:
                    Lock.EndAITurn();
                    return Stage3AI();
            }
        }
        System.out.println("Wrong Stage Value : AI Process Error");
        return null;
    }

    protected int[] Stage1AI() {
        double averageCost = 0;
        List<Card> cardList = GameSystem.gs.getCardList();
        //change non-attack cards
        ai.mulligan(new int[]{7, 8, 10, 16, 17, 19, 21, 22, 23});
        for (int i = 0; i < 24; ++i) {
            if (ai.hand[i]) averageCost += cardList.get(i).getCost();
        }
        averageCost /= 2;
        if (ai.mp < averageCost)
            return Avoid();
        return CalcMaxDmg();
    }

    protected int[] Stage2AI() {
        return new int[]{-1, -1, -1};
    }

    protected int[] Stage3AI() {
        return new int[]{-1, -1, -1};
    }

    protected int[] Avoid() {
        int dy = ai.y - ai.y_player1, dx = ai.x - ai.x_player1;
        int dist = Math.abs(dy) + Math.abs(dx);
        double chance = Math.random();
        if (dist == 0) {
            if (chance < 0.125) return new int[]{0, 2, 5};
            if (chance < 0.25) return new int[]{0, 3, 5};
            if (chance < 0.375) return new int[]{1, 2, 5};
            if (chance < 0.5) return new int[]{1, 3, 5};
            if (chance < 0.625) return new int[]{2, 0, 5};
            if (chance < 0.75) return new int[]{2, 1, 5};
            if (chance < 0.875) return new int[]{3, 0, 5};
            if (chance < 1) return new int[]{3, 1, 5};
        }
        if (dist == 1) {
            if (dy == 0) {
                if (dx == -1) {
                    if (chance < 0.5) return new int[]{0, 2, 5};
                    if (chance < 1) return new int[]{0, 3, 5};
                }
                if (chance < 0.5) return new int[]{1, 2, 5};
                if (chance < 1) return new int[]{1, 3, 5};
            }
            if (dy == -1) {
                if (chance < 0.25) return new int[]{1, 4, 5};
                if (chance < 0.5) return new int[]{0, 4, 5};
                if (chance < 0.75) return new int[]{1, 3, 5};
                if (chance < 1) return new int[]{0, 3, 5};
            }
            if (dy == 1) {
                if (chance < 0.25) return new int[]{1, 4, 5};
                if (chance < 0.5) return new int[]{0, 4, 5};
                if (chance < 0.75) return new int[]{1, 2, 5};
                if (chance < 1) return new int[]{0, 2, 5};
            }
        }
        if (dy < 0 && dx > 0) {
            if (chance < 0.5) return new int[]{1, 2, 5};
            if (chance < 1) return new int[]{2, 1, 5};
        }
        if (dy >= 0 && dx > 0) {
            if (chance < 0.5) return new int[]{1, 3, 5};
            if (chance < 1) return new int[]{3, 1, 5};
        }
        if (dy >= 0 && dx <= 0) {
            if (chance < 0.5) return new int[]{0, 3, 5};
            if (chance < 1) return new int[]{3, 0, 5};
        }
        if (dy < 0 && dx <= 0) {
            if (chance < 0.5) return new int[]{0, 2, 5};
            if (chance < 1) return new int[]{2, 0, 5};
        }
        System.out.println("System AI Avoid Error");
        return null;
    }

    protected int[] CalcMaxDmg() {
        int[] row = new int[4];
        int t = 0, col = ai.mp / 5, dy = ai.y_player1 - ai.y, dx = ai.x_player1 - ai.x;
        int[][][] dp = new int[4][col][3];

        for (int i = 0; i < 24; ++i) if (ai.hand[i]) row[t++] = i;
    }

    protected int[] CalcMaxDmg(boolean[] predict) {
    }
}
