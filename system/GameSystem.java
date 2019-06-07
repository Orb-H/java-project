package project.system;

import java.util.Scanner;

import project.cards.Card;
import project.ranking.Ranking;
import project.ranking.Score1v1;
import project.ranking.Score2v2;
import project.scene.SceneManager;
import project.scene.battle.FieldScene;
import project.scene.battle.SelectScene;
import project.scene.highscore.HighScoreScene;
import project.scene.register.RegisterScene;
import project.scene.title.TitleScene;
import project.scene.transition.*;
import project.util.StringUtils;

public class GameSystem {

    private Player player1, player2;
    private AI ai1, ai2;

    public static GameSystem gs;
    private SceneManager sceneManager;
    private Ranking[] rankings;

    private Scanner in;

    public static boolean mode = false;// false for 1v1, true for 2v2. not for ranking

    public static GameSystem getInstance() {
        if (gs == null)
            gs = new GameSystem();
        return gs;
    }

    private GameSystem() {
        in = new Scanner(System.in);
        rankings = new Ranking[]{new Ranking<Double>("1v1.rank", "1v1", Score1v1.class),
                new Ranking<Double>("2v2.rank", "2v2", Score2v2.class)};
    }

    public void onEnable() {
        for (Ranking r : rankings) {
            r.onEnable();
        }

        sceneManager = SceneManager.getInstance();
        sceneManager.addScene("title", TitleScene.getInstance());
        sceneManager.addScene("ending", EndingScene.getInstance());
        sceneManager.addScene("gameover", GameOverScene.getInstance());
        sceneManager.addScene("win", WinScene.getInstance());
        sceneManager.addScene("highscore", HighScoreScene.getInstance());
        sceneManager.addScene("register", RegisterScene.getInstance());
        sceneManager.addScene("select", SelectScene.getInstance());
        sceneManager.addScene("field", FieldScene.getInstance());

        sceneManager.loadScene("title");
    }

    public void onDisable() {
        for (Ranking r : rankings) {
            r.onDisable();
        }
        System.exit(0);
    }

    public String getInput(String s) {
        System.out.print(s);
        return in.nextLine();
    }

    public Ranking getRanking(boolean b) {// false for 1v1, true for 2v2
        return rankings[b ? 1 : 0];
    }

    public static void main(String[] args) {
        GameSystem gs = GameSystem.getInstance();
        //gs.onEnable();
        String temp = gs.getInput("is 2v2 mode ? : ");
        if (temp.equals("true") || temp.equals("t")) mode = true;
        //Example of 1vs1 mod with stage 1
        gs.player1 = new Player(1, 0);
        gs.ai1 = new AI(1, (mode ? 4 : 3), 3);
        if (mode) {
            gs.player2 = new Player(3, 0);
            gs.ai2 = new AI(3, 4, 3);
        }

        ProcessLock sharedLock = new ProcessLock();
        int[] player1OP = null, ai1OP = null, player2OP = null, ai2OP = null;
        int turn = 0;

        //test for getCardFromDeck and Mulligan, show player's hand methods
        System.out.println("---Player 1's hand---");
        gs.player1.show();
        gs.player1.mulligan();
        if (mode) {
            System.out.println("---Player 2's hand----");
            gs.player2.show();
            gs.player2.mulligan();
        }
        gs.showStatus(turn++);
        //Test for turn playing
        while (!gs.checkGameEnded()) {
            PlayerProcess PlayerDecision = null;
            AIProcess AIDecision = null;
            if (gs.player1.hp > 0) {
                PlayerDecision = new PlayerProcess(0, sharedLock);
                player1OP = PlayerDecision.call();
            }
            if (gs.ai1.hp > 0) {
                AIDecision = new AIProcess(2, sharedLock);
                ai1OP = AIDecision.call();
            }
            if (mode && gs.player2.hp > 0) {
                PlayerDecision = new PlayerProcess(1, sharedLock);
                player2OP = PlayerDecision.call();
            }
            if (mode && gs.ai2.hp > 0) {
                AIDecision = new AIProcess(3, sharedLock);
                ai2OP = AIDecision.call();
            }
            //Player's card operation
            System.out.printf("Player 1's OP: %d %d %d\n", player1OP[0], player1OP[1], player1OP[2]);
            if (mode) System.out.printf("Player 2's OP: %d %d %d\n", player2OP[0], player2OP[1], player2OP[2]);

            //AI's card operation
            System.out.printf("AI 1: %d %d %d\n", ai1OP[0], ai1OP[1], ai1OP[2]);
            if (mode) System.out.printf("AI 2: %d %d %d\n", ai2OP[0], ai2OP[1], ai2OP[2]);

            if (mode) gs.play_turn(new int[][]{player1OP, player2OP, ai1OP, ai2OP});
            else gs.play_turn(new int[][]{player1OP, ai1OP});

            gs.showStatus(turn++);
            System.out.println();
        }

        if (gs.player1.hp <= 0 && gs.player2.hp <= 0) System.out.println("AI WIN");
        else if (gs.ai1.hp <= 0 && gs.ai2.hp <= 0) System.out.println("Player WIN");
        else System.out.println("Draw..");
    }

    public void play_turn(int[][] pOP) {
        int[] order;
        String str;
        int[][] StarPoint = new int[4][];
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < pOP.length; ++k) {
                if (pOP[k][i] == 23) {
                    if (mode) {
                        if (k < 2) {
                            str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
                            StarPoint[k] = StringUtils.Split2Int(str, ",");
                        } else {
                            double ch = Math.random();
                            if (ch < 0.2) StarPoint[k] = new int[]{player1.y, player1.x + 1};
                            else if (ch < 0.4) StarPoint[k] = new int[]{player1.y + 1, player1.x};
                            else if (ch < 0.6) StarPoint[k] = new int[]{player1.y, player1.x - 1};
                            else if (ch < 0.8) StarPoint[k] = new int[]{player1.y - 1, player1.x};
                            else StarPoint[k] = new int[]{player1.y, player1.x};
                        }
                    } else {
                        if (k == 0) {
                            str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
                            StarPoint[k] = StringUtils.Split2Int(str, ",");
                        } else {
                            double ch = Math.random();
                            if (ch < 0.2) StarPoint[k] = new int[]{player1.y, player1.x + 1};
                            else if (ch < 0.4) StarPoint[k] = new int[]{player1.y + 1, player1.x};
                            else if (ch < 0.6) StarPoint[k] = new int[]{player1.y, player1.x - 1};
                            else if (ch < 0.8) StarPoint[k] = new int[]{player1.y - 1, player1.x};
                            else StarPoint[k] = new int[]{player1.y, player1.x};
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < pOP.length; ++k) {
                if (Math.random() < 0.15) {
                    if (mode) {
                        if (k == 0 && player1.hand[0])
                            System.out.printf("Player 1's %dth card was changed by FOOL CARD\n", i + 1);
                        else if (k == 1 && player2.hand[0])
                            System.out.printf("Player 2's %dth card was changed by FOOL CARD\n", i + 1);
                        else if (k == 2 && ai1.hand[0])
                            System.out.printf("AI 1's %dth card was changed by FOOL CARD\n", i + 1);
                        else if (k == 3 && ai2.hand[0])
                            System.out.printf("AI 2's %dth card was changed by FOOL CARD\n", i + 1);
                    } else {
                        if (k == 0 && player1.hand[0])
                            System.out.printf("Player %dth card was changed by FOOL CARD\n", i + 1);
                        else if (k == 1 && ai1.hand[0])
                            System.out.printf("AI %dth card was changed by FOOL CARD\n", i + 1);
                    }
                }
            }

            if (6 <= pOP[0][i]) {
                if (!ai1.predictedHand1[pOP[0][i] - 6]) ai1.predictedHand1[pOP[0][i] - 6] = true;
                if (mode && !ai2.predictedHand1[pOP[0][i] - 6]) ai2.predictedHand1[pOP[0][i] - 6] = true;
            }

            if (mode && 6 <= pOP[1][i]) {
                if (!ai1.predictedHand2[pOP[1][i] - 6]) ai1.predictedHand2[pOP[1][i] - 6] = true;
                if (!ai2.predictedHand2[pOP[1][i] - 6]) ai2.predictedHand2[pOP[1][i] - 6] = true;
            }

            if (mode) order = getOrder(new int[]{pOP[0][i], pOP[1][i], pOP[2][i], pOP[3][i]});
            else order = getOrder(new int[]{pOP[0][i], pOP[1][i]});

            for (int k = 0; k < order.length; ++k) {
                int caster;
                if (order.length == 4) caster = order[k];
                else caster = order[k] * 2;
                if (pOP[order[k]][i] == 23)
                    Card.cards.get(pOP[order[k]][i]).act(caster, StarPoint[order[k]][0], StarPoint[order[k]][1]);
                else
                    Card.cards.get(pOP[order[k]][i]).act(caster);
            }
            player1.shield = ai1.shield = 0;
            if (mode) player2.shield = ai2.shield = 0;
            showStatus(i + 900);
        }
        player1.mp = (player1.mp > 85 ? 100 : player1.mp + 15);
        ai1.mp = (ai1.mp > 85 ? 100 : ai1.mp + 15);
        if (mode) {
            player2.mp = (player2.mp > 85 ? 100 : player2.mp + 15);
            ai2.mp = (ai2.mp > 85 ? 100 : ai2.mp + 15);
        }
    }

    public int[] getOrder(int[] op) {
        int[] prio;
        if (op.length == 2) prio = new int[]{0, 1};
        else prio = new int[]{0, 1, 2, 3};
        for (int i = 0; i < op.length - 1; ++i) {
            for (int j = i + 1; j < op.length; ++j) {
                if (Card.cards.get(op[i]).type.ordinal() > Card.cards.get(op[j]).type.ordinal()) {
                    int t = op[i];
                    op[i] = op[j];
                    op[j] = t;
                    t = prio[i];
                    prio[i] = prio[j];
                    prio[j] = t;
                }
            }
        }
        return prio;
    }

    public void showStatus(int t) {
        System.out.printf("Turn : %d\n", t);
        if (!mode) {
            System.out.printf("Player = HP: %d, MP: %d ------ AI = HP: %d, MP: %d\n", player1.hp, player1.mp, ai1.hp, ai1.mp);
            System.out.printf("Player : y %d, x %d ------ AI : y %d, x %d\n", player1.y, player1.x, ai1.y, ai1.x);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; ++j) {
                    if (i == player1.y && j == player1.x) System.out.print("P ");
                    else if (i == ai1.y && j == ai1.x) System.out.print("A ");
                    else System.out.print("O ");
                }
                System.out.println();
            }
            System.out.println();
        } else {
            System.out.printf("Player 1 = HP: %d, MP: %d ------ AI 1 = HP: %d, MP: %d\n", player1.hp, player1.mp, ai1.hp, ai1.mp);
            System.out.printf("Player 1 : y %d, x %d ------ AI 1 : y %d, x %d\n", player1.y, player1.x, ai1.y, ai1.x);
            System.out.printf("Player 2 = HP: %d, MP: %d ------ AI 2 = HP: %d, MP: %d\n\n", player2.hp, player2.mp, ai2.hp, ai2.mp);
            System.out.printf("Player 2 : y %d, x %d ------ AI 2 : y %d, x %d\n", player2.y, player2.x, ai2.y, ai2.x);
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    if (i == player1.y && j == player1.x && player1.hp > 0) System.out.print("P ");
                    else if (i == player2.y && j == player2.x && player2.hp > 0) System.out.print("W ");
                    else if (i == ai1.y && j == ai1.x && ai1.hp > 0) System.out.print("A ");
                    else if (i == ai2.y && j == ai2.x && ai2.hp > 0) System.out.print("B ");
                    else System.out.print("O ");
                }
                System.out.println();
            }
        }
    }

    boolean checkGameEnded() {
        if (mode) {
            return (player1.hp <= 0 && player2.hp <= 0) || (ai1.hp <= 0 && ai2.hp <= 0);
        }
        return player1.hp <= 0 || ai1.hp <= 0;
    }

    public Player getPlayer(int va) {
        switch (va) {
            case 0:
                return GameSystem.gs.player1;
            case 1:
                return GameSystem.gs.player2;
        }
        return null;
    }

    public AI getAI(int va) {
        switch (va) {
            case 2:
                return GameSystem.gs.ai1;
            case 3:
                return GameSystem.gs.ai2;
        }
        return null;
    }
}
