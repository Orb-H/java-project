package project.system;

import java.util.concurrent.Callable;

import project.cards.*;
import project.util.StringUtils;

public class AIProcess implements Callable<int[]> {
    ProcessLock Lock;

    public int[] call() {
        //busy waiting
        while (Lock.checkAILock()) ;
        if (!GameSystem.mode) {
            switch (GameSystem.ai.stage) {
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
        GameSystem.ai.mulligan(new int[]{7, 10, 17, 19, 21, 22, 23});
        averageCost = GameSystem.ai.calcavgCost();
        if (GameSystem.ai.mp < averageCost)
            return Avoid(false, false, false);
        return CalcMaxDmg();
    }

    protected int[] Stage2AI() {
        double averageCost = 0;
        int predict, benefit, t = 0;
        int[] op;
        //change non-attack cards
        GameSystem.ai.mulligan(new int[]{7, 10, 17, 19, 21, 22, 23});
        averageCost = GameSystem.ai.calcavgCost();
        if (GameSystem.ai.mp < averageCost)
            return Avoid(false, false, false);
        predict = PredictMaxDmg(GameSystem.ai.predictedHand, GameSystem.ai.y - GameSystem.player.y, GameSystem.ai.x - GameSystem.player.x);
        op = CalcMaxDmg();
        if(op==null) System.out.println("***********null op was returned");
        for (int i = 0; i < 3; ++i) t += Card.cards.get(op[i]).getDeal();
        benefit = t - predict;
        if (benefit < 0) return Avoid(false, false, false);
        return op;
    }

    protected int[] Stage3AI() {
        double averageCost = 0;
        int predict, benefit, t = 0;
        int[] op;
        GameSystem.ai.mulligan(new int[]{7});
        averageCost = GameSystem.ai.calcavgCost();
        if (GameSystem.ai.mp < averageCost)
            return Avoid(GameSystem.ai.hand[19], GameSystem.ai.hand[22], GameSystem.ai.hand[23]);
        predict = PredictMaxDmg(GameSystem.ai.predictedHand, GameSystem.ai.y - GameSystem.player.y, GameSystem.ai.x - GameSystem.player.x);
        if (predict == 0) predict = 25;
        op = CalcMaxDmg();
        if (op == null) return Avoid(GameSystem.ai.hand[19], GameSystem.ai.hand[22], GameSystem.ai.hand[23]);
        for (int i = 0; i < 3; ++i) t += Card.cards.get(op[i]).getDeal();
        if (t == 0 && GameSystem.ai.mp - GameSystem.player.mp > 40)
            return Close(GameSystem.ai.hand[22], GameSystem.ai.hand[23]);
        benefit = t - predict;
        if (benefit < 0) return Avoid(GameSystem.ai.hand[19], GameSystem.ai.hand[22], GameSystem.ai.hand[23]);
        return op;
    }

    protected int[] Avoid(boolean hasHeal, boolean hasLDash, boolean hasRDash) {
        int dy = GameSystem.ai.y - GameSystem.player.y, dx = GameSystem.ai.x - GameSystem.player.x;
        int dist = Math.abs(dy) + Math.abs(dx), right = (hasRDash ? 29 : 1), left = (hasLDash ? 28 : 0);
        double chance = Math.random();
        if (dist == 0) {
            if (chance < 0.125) return new int[]{left, 2, 5};
            if (chance < 0.25) return new int[]{left, 3, 5};
            if (chance < 0.375) return new int[]{right, 2, 5};
            if (chance < 0.5) return new int[]{right, 3, 5};
            if (chance < 0.625) return new int[]{2, left, 5};
            if (chance < 0.75) return new int[]{2, right, 5};
            if (chance < 0.875) return new int[]{3, left, 5};
            if (chance < 1) return new int[]{3, right, 5};
        }
        if (dist == 1) {
            if (dy == 0) {
                if (dx == -1) {
                    if (chance < 0.5) return new int[]{left, 2, 5};
                    if (chance < 1) return new int[]{left, 3, 5};
                }
                if (chance < 0.5) return new int[]{right, 2, 5};
                if (chance < 1) return new int[]{right, 3, 5};
            }
            if (dy == -1) {
                if (chance < 0.25) return new int[]{right, 4, 5};
                if (chance < 0.5) return new int[]{left, 4, 5};
                if (chance < 0.75) return new int[]{right, 3, 5};
                if (chance < 1) return new int[]{left, 3, 5};
            }
            if (dy == 1) {
                if (chance < 0.25) return new int[]{1, 4, 5};
                if (chance < 0.5) return new int[]{0, 4, 5};
                if (chance < 0.75) return new int[]{1, 2, 5};
                if (chance < 1) return new int[]{0, 2, 5};
            }
        }
        if (dy < 0 && dx > 0) {
            if (chance < 0.5)
                return new int[]{1, 2, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{2, 1, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        if (dy >= 0 && dx > 0) {
            if (chance < 0.5)
                return new int[]{1, 3, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{3, 1, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        if (dy >= 0 && dx <= 0) {
            if (chance < 0.5)
                return new int[]{0, 3, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{3, 0, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        if (dy < 0 && dx <= 0) {
            if (chance < 0.5)
                return new int[]{0, 2, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{2, 0, (GameSystem.ai.hp < 70 && GameSystem.ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        System.out.println("System AI Avoid Error");
        return null;
    }

    protected int[] Close(boolean hasLDash, boolean hasRDash) {
        int dy = GameSystem.ai.y - GameSystem.player.y, dx = GameSystem.ai.x - GameSystem.player.x;
        int dist = Math.abs(dy) + Math.abs(dx), right = (hasRDash ? 29 : 1), left = (hasLDash ? 28 : 0);
        double chance = Math.random();
        if (dist == 0) {
            if (chance < 0.125) return new int[]{left, 2, 5};
            if (chance < 0.25) return new int[]{left, 3, 5};
            if (chance < 0.375) return new int[]{right, 2, 5};
            if (chance < 0.5) return new int[]{right, 3, 5};
            if (chance < 0.625) return new int[]{2, left, 5};
            if (chance < 0.75) return new int[]{2, right, 5};
            if (chance < 0.875) return new int[]{3, left, 5};
            if (chance < 1) return new int[]{3, right, 5};
        }
        if (dist == 1) {
            if (dy == 0) {
                if (dx == -1) {
                    if (chance < 0.5) return new int[]{left, 2, 5};
                    if (chance < 1) return new int[]{left, 3, 5};
                }
                if (chance < 0.5) return new int[]{right, 2, 5};
                if (chance < 1) return new int[]{right, 3, 5};
            }
            if (dy == -1) {
                if (chance < 0.25) return new int[]{right, 4, 5};
                if (chance < 0.5) return new int[]{left, 4, 5};
                if (chance < 0.75) return new int[]{right, 3, 5};
                if (chance < 1) return new int[]{left, 3, 5};
            }
            if (dy == 1) {
                if (chance < 0.25) return new int[]{1, 4, 5};
                if (chance < 0.5) return new int[]{0, 4, 5};
                if (chance < 0.75) return new int[]{1, 2, 5};
                if (chance < 1) return new int[]{0, 2, 5};
            }
        }
        if (dy < 0 && dx > 0) {
            if (chance < 0.5) return new int[]{1, 2, (GameSystem.ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{2, 1, (GameSystem.ai.mp > 90) ? 4 : 5};
        }
        if (dy >= 0 && dx > 0) {
            if (chance < 0.5) return new int[]{1, 3, (GameSystem.ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{3, 1, (GameSystem.ai.mp > 90) ? 4 : 5};
        }
        if (dy >= 0 && dx <= 0) {
            if (chance < 0.5) return new int[]{0, 3, (GameSystem.ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{3, 0, (GameSystem.ai.mp > 90) ? 4 : 5};
        }
        if (dy < 0 && dx <= 0) {
            if (chance < 0.5) return new int[]{0, 2, (GameSystem.ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{2, 0, (GameSystem.ai.mp > 90) ? 4 : 5};
        }
        System.out.println("System AI Avoid Error");
        return null;
    }

    protected int[] ranMove() {
        boolean[] chk = new boolean[4];
        int chance;
        int[] op = new int[3];
        chk[0] = chk[1] = chk[2] = chk[3] = false;
        if (GameSystem.ai.mp == 100) {
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
        int col = GameSystem.ai.mp / 5, dy = GameSystem.player.y - GameSystem.ai.y, dx = GameSystem.player.x - GameSystem.ai.x, r = 0;
        int[] cost = new int[4];
        int[] deal = new int[4];
        int[] idx_card = new int[4];
        int[] op = new int[3];
        int[][] dp;
        boolean isStar = false;
        String[][] used;

        for (int i = 1; i < 24; ++i)
            if (GameSystem.ai.hand[i] && Card.cards.get(i + 6).inBound(dy, dx))
                if (i == 17)
                    isStar = true;
                else idx_card[r++] = i + 6;

        if (r == 0) {
            if (isStar && GameSystem.ai.mp == 100)
                return new int[]{17, 4, 5};
            else
                return ranMove();
        }


        used = new String[r][col];
        dp = new int[r][col];
        for (int i = 0; i < r; ++i)
            for (int j = 0; j < col; ++j) {
                used[i][j] = "";
                dp[i][j] = 0;
            }

        for (int i = 0; i < r; ++i) {
            int temp = Card.cards.get(idx_card[i]).getCost() / 5;
            cost[i] = (temp > 0) ? temp : 0;
            deal[i] = Card.cards.get(idx_card[i]).getDeal();
        }
        for (int i = cost[0]; i < col; ++i) {
            dp[0][i] = deal[0];
            used[0][i] += idx_card[0] + " ";
        }

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
        if (used[r - 1][col - 1].equals("")) return null;
        int[] temp = StringUtils.Split2Int(used[r - 1][col - 1], " ");
        for (int i = 0; i < temp.length; ++i) op[i] = temp[i];
        if (op[1] == 0) {
            op[1] = 4;
            op[2] = 5;
        } else if (op[2] == 0)
            op[2] = 5;
        return op;
    }

    protected int PredictMaxDmg(boolean[] predict, int dy, int dx) {
        int col = GameSystem.ai.mp / 5, r = 0;
        int[] cost = new int[4];
        int[] deal = new int[4];
        int[] idx_card = new int[4];
        int[][] dp;

        for (int i = 0; i < 24; ++i)
            if (predict[i] && Card.cards.get(i + 6).inBound(dy, dx))
                idx_card[r++] = i;

        if (r == 0)
            return 0;

        dp = new int[r][col];
        for (int i = 0; i < r; ++i)
            for (int j = 0; j < col; ++j) {
                dp[i][j] = 0;
            }

        for (int i = 0; i < r; ++i) {
            int temp = Card.cards.get(idx_card[i] + 6).getCost() / 5;
            cost[i] = (temp > 0) ? temp : 0;
            deal[i] = Card.cards.get(idx_card[i] + 6).getDeal();
        }
        for (int i = cost[0]; i < col; ++i) dp[0][i] = deal[0];

        for (int i = 1; i < r; ++i) {
            for (int j = cost[i]; j < col; ++j) {
                if (deal[i] + dp[i - 1][j - cost[i]] > dp[i - 1][j]) {
                    dp[i][j] = deal[i] + dp[i - 1][j - cost[i]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[r - 1][col - 1];
    }
}
