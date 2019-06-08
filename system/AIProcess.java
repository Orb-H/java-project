package project.system;

import java.util.concurrent.Callable;

import project.cards.*;
import project.util.StringUtils;

public class AIProcess implements Callable<int[]> {
    ProcessLock Lock;
    int plyInfo;
    AI ai;
    Player[] ply;
    protected GameSystem gs;

    public AIProcess(int ainum, ProcessLock b) {
        Lock = b;
        plyInfo = ainum;
        gs=GameSystem.getInstance();
    }

    public int[] call() {
        //busy waiting
        while (Lock.checkAILock()) ;
        ai = gs.getAI(plyInfo);
        if (GameSystem.mode) {
            ply = new Player[]{gs.getPlayer(0), gs.getPlayer(1)};
        } else {
            ply = new Player[]{gs.getPlayer(0)};
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
        //a
        if (ai.mp < ai.calcavgCost())
            return Avoid(false, false, false);
        op1 = CalcMaxDmg(ply[0].y, ply[0].x);
        if (GameSystem.mode) op2 = CalcMaxDmg(ply[1].y, ply[1].x);
        return op1[0][0] > op2[0][0] ? op1[1] : op2[1];
    }

    protected int[] Stage2AI() {
        int predict1, predict2 = 0, benefit;
        int[][] op1, op2 = {{-1}, null};
        //안쓸 카드들을 멀리건으로 돌려준다.
        ai.mulligan(new int[]{10, 17, 19, 21, 22, 23});
        //현재 mp가 카드들의 평균mp 보다 작으면 회피 명령을 반환한다.
        if (ai.mp < ai.calcavgCost())
            return Avoid(false, false, false);
        //PredictMaxDmg는 ai 의 predictedHand를 이용해서 ai 맞을 예상 데미지를 반환한다.
        predict1 = PredictMaxDmg(ai.predictedHand1, 0);
        //CalcMaxDmg는 플레이어의 위치를 입력하면 해당위치에 가장많은 데미지를 주는 op[1][] 명령과 대미지 op[0][0]을 반환
        op1 = CalcMaxDmg(ply[0].y, ply[0].x);
        if (GameSystem.mode) {
            op2 = CalcMaxDmg(ply[1].y, ply[1].x);
            predict2 = PredictMaxDmg(ai.predictedHand2, 1);
        }
        //benefit에 내가 줄수 있는 최대 대미지와 내가 받는 최대 대미지의 차이를 저장한다.
        benefit = op1[0][0] - predict1;
        if (GameSystem.mode) benefit = benefit + op2[0][0] - predict2;
        //benefit이 음수면 ai 가 항상 손해 보기 때문에 도망간다.
        if (benefit < 0) return Avoid(false, false, false);
        //그외의 경우에는 player1에게 주는 대미지 op1[0][0]과 player2에게 주는 대미지 op2[0][0]을 반환해서 더 쎈 쪽을 반환
        if (op1[0][0] > op2[0][0]) {
            return op1[1] != null ? op1[1] : Avoid(false, false, false);
        } else {
            return op2[1] != null ? op2[1] : Avoid(false, false, false);
        }
    }

    protected int[] Stage3AI() {
        double averageCost = ai.calcavgCost();
        int predict1 = 0, predict2 = 0, benefit;
        int[][] op1 = {{0}, null}, op2 = {{0}, null};
        ai.mulligan(new int[]{6}); //6번 카드는 안쓸거라서 멀리건 으로 제거 해준다.
        /*
        플레이어가 죽지 않았으면 해당 플레이어에 대한 내가 받는 예측 대미지, 내가 줄수 있는 op 배열을 계산 한다.
        state3에는 predictHand를 쓰지않고 실제 player의 hand를 가지고 예측을 해서 예측 정확도를 올린다.
        */
        if (ply[0].hp > 0) {
            predict1 = PredictMaxDmg(ply[0].hand, 0);
            op1 = CalcMaxDmg(ply[0].y, ply[0].x);
        }
        if (GameSystem.mode && ply[1].hp > 0) {
            op2 = CalcMaxDmg(ply[1].y, ply[1].x);
            predict2 = PredictMaxDmg(ply[1].hand, 1);
        }
        //benefit 계산
        benefit = op1[0][0] - predict1;
        try {
            if (GameSystem.mode) benefit = benefit + op2[0][0] - predict2;
        } catch (NullPointerException ex) { //null pointer 예외 처리
            benefit = benefit - predict2;
        }
        //20만큼 손해를 보고 내 mp가 평균보다 작으면 회피를 한다.
        if (benefit < -20 && ai.mp < averageCost) return Avoid(ai.hand[19], ai.hand[22], ai.hand[23]);
        //양쪽에게 줄 수있는 대미지가 0이고 내 mp가 80 초과면 상대에거 접근한다.
        if (op1[0][0] == 0 && op2[0][0] == 0 && ai.mp > 80) return Close(ai.hand[22], ai.hand[23]);

        //그 외의 경우에는 op[1]을 반환하고 만약 null이면 회피를 한다.
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
            return new int[]{4, 5, (int) (chance * 4)};
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
        //제한된 mp, 단 한장만 사용가능한 카드는 01knapsack 과 동일한 문제이므로 dp로 최대 값을 계산해서 반환해준다.
        //dp의 열 col은 현재 mp를 5로 나눈 값이다. (mp와 cost는 어차피 5의 배수이기 때문)
        int col = ai.mp / 5, dy = ty - ai.y, dx = tx - ai.x, r = 0;
        int[] cost = new int[4];
        int[] deal = new int[4];
        int[] idx_card = new int[4];
        int[] op = new int[3];
        int[][] dp;
        String[][] used;

        //플레이어의 위치 ty, tx 에 닿는 공격카드를 idx_card 배열에 넣어준다.
        for (int i = 1; i < 24; ++i)
            if (ai.hand[i] && Card.cards.get(i + 6).inBound(dy, dx))
                idx_card[r++] = i + 6;
        //ty,tx 에 닿는 공격 카드가 ai의 패에 없고 mp가 100이고 star 카드가 있으면 star를 사용, 그 외에는 random move
        if (r == 0) {
            if (ai.hand[17] && ai.mp == 100)
                return new int[][]{{0}, {17, 4, 5}};
            else
                return new int[][]{{0}, ranMove()};
        }

        //used는 dp로 사용된 카드들을 tracking 하는 string 배열이고 dp는 dp를 돌릴 이차원 정수 배열
        //초기화 해준다.
        used = new String[r][col];
        dp = new int[r][col];
        for (int i = 0; i < r; ++i)
            for (int j = 0; j < col; ++j) {
                used[i][j] = "";
                dp[i][j] = 0;
            }
        //각 카드별로 cost와 deal을 미리 저장해둔다. cost의 경우는 전부 5의 배수이므로 5로 나눠준다.
        for (int i = 0; i < r; ++i) {
            int temp = Card.cards.get(idx_card[i]).getCost() / 5;
            cost[i] = (temp > 0) ? temp : 0;
            deal[i] = Card.cards.get(idx_card[i]).getDeal();
        }
        //dp와 used에 0번 카드를 사용하도록 초기화
        for (int i = cost[0]; i < col; ++i) {
            dp[0][i] = deal[0];
            used[0][i] += idx_card[0] + " ";
        }

        //dp를 돌린다.
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

        //dp를 돌려서 사용되는 카드가 없으면 used[r-1][col-1]=="" 그냥 {0, null}을 반환
        if (used[r - 1][col - 1].equals("")) return new int[][]{{0}, null};
        int[] temp = StringUtils.Split2Int(used[r - 1][col - 1], " ");

        //dp 돌려서 의미있는 결과가 나왔으면 used를 쪼개서 int[] 배열로 만든다.
        for (int i = 0; i < temp.length; ++i) op[i] = temp[i];
        //카드를 1장만 사용하면 그 뒤에는 guard, charge 를 넣어준다.
        if (op[1] == 0) {
            op[1] = 4;
            op[2] = 5;
        }
        //카드를 2장만 사용하면 그 뒤에는 charge를 넣어준다.
        else if (op[2] == 0)
            op[2] = 5;
        return new int[][]{{dp[r - 1][col - 1]}, op};
    }

    protected int PredictMaxDmg(boolean[] predict, int idx) {
        //CalcMaxDmg와 동일하게 dp를 이용해서 내가 받는 대미지만 계산
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
