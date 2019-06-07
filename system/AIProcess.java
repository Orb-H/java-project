package project.system;

import java.util.concurrent.Callable;

import project.cards.*;
import project.util.StringUtils;

public class AIProcess implements Callable<int[]> {
    ProcessLock Lock;
    int plyInfo;
    AI ai;
    Player[] ply;

    public AIProcess(int ainum, ProcessLock b) {
        Lock = b;
        plyInfo = ainum;
    }

    public int[] call() {
        //busy waiting
        while (Lock.checkAILock()) ;
        ai = GameSystem.gs.getAI(plyInfo);
        if (GameSystem.mode) {
            ply = new Player[]{GameSystem.gs.getPlayer(0), GameSystem.gs.getPlayer(1)};
        } else {
            ply = new Player[]{GameSystem.gs.getPlayer(0)};
        }
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
        System.out.println("Wrong Stage Value : AI Process Error");
        return null;
    }

    protected int[] Stage1AI() {
        int[][] op1, op2 = {{-1}, null};
        //change non-attack cards
        ai.mulligan(new int[]{10, 17, 19, 21, 22, 23});
        if (ai.mp < ai.calcavgCost())
            return Avoid(false, false, false);
        op1 = CalcMaxDmg(ply[0].y, ply[0].x);
        if (GameSystem.mode) op2 = CalcMaxDmg(ply[1].y, ply[1].x);
        return op1[0][0] > op2[0][0] ? op1[1] : op2[1];
    }

    protected int[] Stage2AI() {
        int predict1, predict2 = 0, benefit;
        int[][] op1, op2 = {{-1}, null};
        //change non-attack cards
        ai.mulligan(new int[]{10, 17, 19, 21, 22, 23});
        if (ai.mp < ai.calcavgCost())
            return Avoid(false, false, false);
        predict1 = PredictMaxDmg(ai.predictedHand1, 0);
        op1 = CalcMaxDmg(ply[0].y, ply[0].x);
        if (GameSystem.mode) {
            op2 = CalcMaxDmg(ply[1].y, ply[1].x);
            predict2 = PredictMaxDmg(ai.predictedHand2, 1);
        }

        benefit = op1[0][0] - predict1;
        if (GameSystem.mode) benefit = benefit + op2[0][0] - predict2;

        if (benefit < 0) return Avoid(false, false, false);

        if (op1[0][0] > op2[0][0]) {
            return op1[1] != null ? op1[1] : Avoid(false, false, false);
        } else {
            return op2[1] != null ? op2[1] : Avoid(false, false, false);
        }
    }

    protected int[] Stage3AI() {
        double averageCost=ai.calcavgCost();
        int predict1 = 0, predict2 = 0, benefit;
        int[][] op1 = {{0}, null}, op2 = {{0}, null};
        ai.mulligan(new int[]{6});
        if (ply[0].hp > 0) {
            predict1 = PredictMaxDmg(ply[0].hand, 0);
            op1 = CalcMaxDmg(ply[0].y, ply[0].x);
        }
        if (GameSystem.mode && ply[1].hp > 0) {
            op2 = CalcMaxDmg(ply[1].y, ply[1].x);
            predict2 = PredictMaxDmg(ply[1].hand, 1);
        }
        benefit = op1[0][0] - predict1;
        if (GameSystem.mode) benefit = benefit + op2[0][0] - predict2;
        if (benefit < -20 && ai.mp < averageCost) return Avoid(ai.hand[19], ai.hand[22], ai.hand[23]);
        if (op1[0][0]==0 && op2[0][0]==0 && ai.mp > 80) return Close(ai.hand[22], ai.hand[23]);

        if (op1[0][0] > op2[0][0]) {
            return (op1[1] != null) ? op1[1] : Avoid(ai.hand[19], ai.hand[22], ai.hand[23]);
        } else {
            return (op2[1] != null) ? op2[1] : Avoid(ai.hand[19], ai.hand[22], ai.hand[23]);
        }
    }

    protected int[] Avoid(boolean hasHeal, boolean hasLDash, boolean hasRDash) {
        int tmp, dist, dy, dx;
        int right = (hasRDash ? 29 : 1), left = (hasLDash ? 28 : 0);
        double chance = Math.random();
        dist = Math.abs(ply[0].y - ai.y) + Math.abs(ply[0].x - ai.x);
        dy = ai.y - ply[0].y;
        dx = ai.x - ply[0].x;
        if (GameSystem.mode) {
            tmp = Math.abs(ply[1].y - ai.y) + Math.abs(ply[1].x - ai.x);
            if (dist > tmp) {
                dist = tmp;
                dy = ai.y - ply[1].y;
                dx = ai.x - ply[1].x;
            }
        }
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
                return new int[]{1, 2, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{2, 1, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        if (dy >= 0 && dx > 0) {
            if (chance < 0.5)
                return new int[]{1, 3, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{3, 1, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        if (dy >= 0 && dx <= 0) {
            if (chance < 0.5)
                return new int[]{0, 3, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{3, 0, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        if (dy < 0 && dx <= 0) {
            if (chance < 0.5)
                return new int[]{0, 2, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
            if (chance < 1)
                return new int[]{2, 0, (ai.hp < 70 && ai.mp > 60 && hasHeal) ? 25 : 5};
        }
        System.out.println("System AI Avoid Error");
        return null;
    }

    protected int[] Close(boolean hasLDash, boolean hasRDash) {
        int tmp, dist, dy, dx;
        int right = (hasRDash ? 29 : 1), left = (hasLDash ? 28 : 0);
        double chance = Math.random();
        dist = Math.abs(ply[0].y - ai.y) + Math.abs(ply[0].x - ai.x);
        dy = ply[0].y - ai.y;
        dx = ply[0].x - ai.x;
        if (GameSystem.mode) {
            tmp = Math.abs(ply[1].y - ai.y) + Math.abs(ply[1].x - ai.x);
            if (dist > tmp) {
                dist = tmp;
                dy = ply[1].y - ai.y;
                dx = ply[1].x - ai.x;
            }
        }

        if (dist == 0) {
            return new int[] {4, 5, (int)(chance*4)};
        }
        if (dist == 1) {
            if (dy == 0) {
                if (dx == -1) return new int[]{left, 4, 5};
                return new int[]{right, 4, 5};
            }
            if (dy == -1) {
                return new int[]{2, 4, 5};
            }
            if (dy == 1) {
                return new int[]{3, 4, 5};
            }
        }
        if (dy < 0 && dx > 0) {
            if (chance < 0.5) return new int[]{1, 2, (ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{2, 1, (ai.mp > 90) ? 4 : 5};
        }
        if (dy >= 0 && dx > 0) {
            if (chance < 0.5) return new int[]{1, 3, (ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{3, 1, (ai.mp > 90) ? 4 : 5};
        }
        if (dy >= 0 && dx <= 0) {
            if (chance < 0.5) return new int[]{0, 3, (ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{3, 0, (ai.mp > 90) ? 4 : 5};
        }
        if (dy < 0 && dx <= 0) {
            if (chance < 0.5) return new int[]{0, 2, (ai.mp > 90) ? 4 : 5};
            if (chance < 1) return new int[]{2, 0, (ai.mp > 90) ? 4 : 5};
        }
        System.out.println("System AI Close Error");
        return null;
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

    protected int[][] CalcMaxDmg(int ty, int tx) {
        int col = ai.mp / 5, dy = ty - ai.y, dx = tx - ai.x, r = 0;
        int[] cost = new int[4];
        int[] deal = new int[4];
        int[] idx_card = new int[4];
        int[] op = new int[3];
        int[][] dp;
        String[][] used;

        for (int i = 1; i < 24; ++i)
            if (ai.hand[i] && Card.cards.get(i + 6).inBound(dy, dx))
                idx_card[r++] = i + 6;

        if (r == 0) {
            if (ai.hand[17] && ai.mp == 100)
                return new int[][]{{0}, {17, 4, 5}};
            else
                return new int[][]{{0}, ranMove()};
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
        if (used[r - 1][col - 1].equals("")) return new int[][]{{0}, null};
        int[] temp = StringUtils.Split2Int(used[r - 1][col - 1], " ");
        for (int i = 0; i < temp.length; ++i) op[i] = temp[i];
        if (op[1] == 0) {
            op[1] = 4;
            op[2] = 5;
        } else if (op[2] == 0)
            op[2] = 5;
        return new int[][]{{dp[r - 1][col - 1]}, op};
    }

    protected int PredictMaxDmg(boolean[] predict, int idx) {
        int col = ply[idx].mp / 5, dy = ai.y - ply[idx].y, dx = ai.x - ply[idx].x, r = 0;
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
