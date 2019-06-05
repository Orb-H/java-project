package project.system;

import java.util.concurrent.Callable;
import java.util.List;

import project.cards.*;
import project.util.StringUtils;

public class AIProcess implements Callable<int[]> {
    ProcessLock Lock;
    List<Card> cardList;
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
        //change non-attack cards
        ai.mulligan(new int[]{7, 8, 10, 16, 17, 19, 21, 22, 23});
        ai.show(cardList);
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

    protected int[] Close() {
        return new int[]{-1, -1, -1};
    }

    protected int[] ranMove() {
        boolean[] chk = new boolean[4];
        int chance;
        int[] op = new int[3];
        chk[0] = chk[1] = chk[2] = chk[3] = false;
        if (ai.mp == 100) {
            for (int i = 0; i < 3; ++i) {
                do {
                    chance = (int) (Math.random() * 4);
                } while (chk[chance]);
                chk[chance] = true;
                op[i] = chance;
            }
        } else {
            for (int i = 0; i < 2; ++i) {
                do {
                    chance = (int) (Math.random() * 4);
                } while (chk[chance]);
                chk[chance] = true;
                op[i] = chance;
            }
            op[2] = 5;
        }
        return op;
    }

    protected int[] CalcMaxDmg() {
        int col = ai.mp / 5, dy = ai.y_player1 - ai.y, dx = ai.x_player1 - ai.x, r = 0;
        int[] cost = new int[4];
        int[] deal = new int[4];
        int[] idx_card = new int[4];
        int[] op = new int[3];
        int[][] dp;
        String[][] used;

        for (int i = 0; i < 24; ++i)
            if (ai.hand[i] && cardList.get(i + 6).inBound(dy, dx))
                idx_card[r++] = i;

        if (r == 0)
            return ranMove();

        used = new String[r][col];
        dp = new int[r][col];
        for (int i = 0; i < r; ++i)
            for (int j = 0; j < col; ++j) {
                used[i][j] = "";
                dp[i][j] = 0;
            }

        for (int i = 0; i < r; ++i) {
            int temp = cardList.get(idx_card[i] + 6).getCost() / 5;
            cost[i] = (temp > 0) ? temp : 0;
            deal[i] = cardList.get(idx_card[i] + 6).getDeal();
        }
        dp[0][cost[0]] = deal[0];
        used[0][cost[0]]+=idx_card[0];

        for (int i = 1; i < r; ++i) {
            for (int j = cost[i]; j < col; ++j) {
                if (deal[i] + dp[i - 1][j - cost[i]] > dp[i - 1][j]) {
                    dp[i][j] = deal[i] + dp[i - 1][j - cost[i]];
                    used[i][j] = used[i - 1][j - cost[i]] + idx_card[i] + " ";
                } else {
                    dp[i][j] = dp[i - 1][j];
                    used[i][j] = used[i - 1][j];
                }
            }
        }
        int[] temp = StringUtils.Split2Int(used[r - 1][col - 1], " ");
        if (temp.length > 4) {
            System.out.println("AI CalcMaxDmg Error");
            return null;
        }
        for (int i = 0; i < temp.length; ++i) op[i] = temp[i] + 6;
        if (op[1] == 0) {
            op[1] = 4;
            op[2] = 5;
        } else if (op[2] == 0)
            op[2] = 5;
        return op;
    }

    protected int[] CalcMaxDmg(boolean[] predict) {
        return new int[]{-1, -1, -1};
    }
}
