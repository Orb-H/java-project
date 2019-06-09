package project.system;

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
import project.scene.transition.EndingScene;
import project.scene.transition.GameOverScene;
import project.scene.transition.WinScene;
import project.util.StringUtils;

import java.util.Scanner;

public class GameSystem {

    private Player player1, player2;
    private AI ai1, ai2;

    private static GameSystem gs;
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
        //��� �Է�
        String temp = gs.getInput("is 2v2 mode ? : ");
        //��� ����
        if (temp.equals("true") || temp.equals("t")) mode = true;
        //ai�� player ��ü ���� �⺻ ���� 3��������
        gs.player1 = new Player(1, 0);
        gs.ai1 = new AI(1, 3, 3);
        //2��2 ���� ��ü 2�� �� ����
        if (mode) {
            gs.player2 = new Player(3, 0);
            gs.ai2 = new AI(3, 3, 3);
        }
        //�÷��̾�� ai������ ���� �������� lock �ɾ��ִ� ��ü
        ProcessLock sharedLock = new ProcessLock();
        //�÷��̾�� ai�� ī�� ������ �����ϴ� int �迭 size�� 3
        int[] player1OP = null, ai1OP = null, player2OP = null, ai2OP = null;
        //���� ��
        int turn = 0;

        //�÷��̾��� �и� �����ְ� �ָ����� ����
        System.out.println("---Player 1's hand---");
        gs.player1.show();
        gs.player1.mulligan();
        if (mode) {
            //2��2 ���� �÷��̾�2�� ���� �и� �����ְ� �ָ����� ����
            System.out.println("---Player 2's hand----");
            gs.player2.show();
            gs.player2.mulligan();
        }
        //���� ���� ���¸� �׷��ִ� �Լ� (UI �߰��Ǹ� �����, ai ������
        gs.showStatus(turn++);

        //������ ������ ���� while�� ������.
        while (!gs.checkGameEnded()) {
            //�÷��̾�� ai�� ī�� ������ �����ϴ� thread ����.
            PlayerProcess PlayerDecision = null;
            AIProcess AIDecision = null;
            //�÷��̾� 1�� ü���� 0���� ũ�� ������ �����Ѵ�.
            if (gs.player1.hp > 0) {
                PlayerDecision = new PlayerProcess(0, sharedLock);
                player1OP = PlayerDecision.call();
            }
            //ai1�� ü���� 0���� ũ�� ������ �����Ѵ�.
            if (gs.ai1.hp > 0) {
                AIDecision = new AIProcess(2, sharedLock);
                ai1OP = AIDecision.call();
            }
            //2��2 ���� �÷��̾� 2�� ü���� 0���� ũ�� ����.
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

            //������ ��� �迭�� ���� �����Ѵ�.
            if (mode) gs.play_turn(new int[][]{player1OP, player2OP, ai1OP, ai2OP});
            else gs.play_turn(new int[][]{player1OP, ai1OP});

            //���� ������ ��� field�� �ѹ� �����ش�.
            gs.showStatus(turn++);
            System.out.println();
        }
        //������ ������ ��,��, ���ºθ� �����ϴ� �κ��̴�.
        if (mode) {
            if (gs.player1.hp <= 0 && gs.player2.hp <= 0) {
                if (gs.ai1.hp <= 0 && gs.ai2.hp <= 0) System.out.println("Draw..");
                else System.out.println("AI WIN");
            } else if (gs.ai1.hp <= 0 && gs.ai2.hp <= 0) System.out.println("Player WIN");
        } else {
            if (gs.player1.hp <= 0) {
                if (gs.ai1.hp <= 0) System.out.println("Draw..");
                else System.out.println("AI WIN");
            } else if (gs.ai1.hp <= 0) System.out.println("Player WIN");
        }
    }

    public void play_turn(int[][] pOP) {
        //�� ��ɾ ������ �迭�� �޴´�. pOP�� ũ��� [2][3] �̰ų� 2:2 ���� [4][3]�� �ȴ�.
        int[] order; //�� �������� ��ɾ� ���� ������ �����ϴ� �迭
        String str;
        int[][] StarPoint = new int[4][]; //���� star ī�带 ����ߴٸ� star�� �������� ��ǥ�� �����ϴ� �迭
        //for������ starī�尡 ��������� �˻��ϰ� �������� star�� ��ǥ�� �Է¹޴´�.
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < pOP.length; ++k) {
                if (pOP[k] == null) continue; //null ��ɾ�� �÷��̾ �׾��ٴ� ���̹Ƿ� �Ѿ��.
                if (pOP[k][i] == 23) { //star�� ��������
                    if (mode) { //2:2 �̸� k = 0,1�� ����� 2,3�� ai�̴�.
                        if (k < 2) {
                            //����ڰ� �� star�� ��ǥ�� �Է¹޴´�.
                            str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
                            StarPoint[k] = StringUtils.Split2Int(str, ",");
                            //��ǥ�Է� ����ó��, �ٽ� �Է¹޴´�.
                            if (StarPoint[k] == null) {
                                --k;
                                continue;
                            }
                        } else {
                            //ai�� star�� ������ �����¿�, ������ġ�� 1/5 Ȯ���� �����ϰ� �ش�.
                            double ch = Math.random();
                            if (ch < 0.2) StarPoint[k] = new int[]{player1.y, player1.x + 1};
                            else if (ch < 0.4) StarPoint[k] = new int[]{player1.y + 1, player1.x};
                            else if (ch < 0.6) StarPoint[k] = new int[]{player1.y, player1.x - 1};
                            else if (ch < 0.8) StarPoint[k] = new int[]{player1.y - 1, player1.x};
                            else StarPoint[k] = new int[]{player1.y, player1.x};
                        }
                    } else {
                        //1:1 ���� 0�� ����� 1�� ai�̴�.
                        if (k == 0) {
                            //����� star ��ǥ�Է�
                            str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
                            StarPoint[k] = StringUtils.Split2Int(str, ",");
                            //����ó��
                            if (StarPoint[k] == null) {
                                --k;
                                continue;
                            }
                        } else {
                            //ai star��ǥ�Է�
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
        //����� �����ϴ� for���̴�. �� 3���� ����� ������ ������ ���� ���� fool card�� �ߵ������� �����Ѵ�.
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < pOP.length; ++k) {
                if (pOP[k] == null) continue; //pOP�� null�̸� ���� �÷��̾�ϱ� ��ŵ
                //2:2�϶� fool card �ߵ����θ� �����Ѵ�.
                if (mode) {
                    /*
                    �� �÷��̾� ���� foolcard�� �ִ��� hand[0], �׸��� �ߵ��Ǵ��� math.random()<0.15�� �����ϰ� �ߵ�������
                    pOP[k][i]�� foolcard = 6 ���� �ٲ��ְ� �޼����� ����Ѵ�.
                    */
                    if (k == 0 && player1.hand[0] && Math.random() < 0.15) {
                        pOP[k][i] = 6;
                        System.out.printf("Player 1's %dth card was changed by FOOL CARD\n", i + 1);
                    } else if (k == 1 && player2.hand[0] && Math.random() < 0.15) {
                        pOP[k][i] = 6;
                        System.out.printf("Player 2's %dth card was changed by FOOL CARD\n", i + 1);
                    } else if (k == 2 && ai1.hand[0] && Math.random() < 0.15) {
                        pOP[k][i] = 6;
                        System.out.printf("AI 1's %dth card was changed by FOOL CARD\n", i + 1);
                    } else if (k == 3 && ai2.hand[0] && Math.random() < 0.15) {
                        pOP[k][i] = 6;
                        System.out.printf("AI 2's %dth card was changed by FOOL CARD\n", i + 1);
                    }
                }
                //1:1 �϶��� �Ȱ��� fool card �ߵ����θ� �����Ѵ�.
                else {
                    if (k == 0 && player1.hand[0] && Math.random() < 0.15) {
                        pOP[k][i] = 6;
                        System.out.printf("Player %dth card was changed by FOOL CARD\n", i + 1);
                    } else if (k == 1 && ai1.hand[0] && Math.random() < 0.15) {
                        pOP[k][i] = 6;
                        System.out.printf("AI %dth card was changed by FOOL CARD\n", i + 1);
                    }
                }
            }
            /*
            fool card �ߵ��� ���������� ai�� predictedhand( �÷��̾��� �и� �����ϴ� �κ�) �� ������Ʈ ���ش�.
            6�̸��� ī����� �⺻ī���̹Ƿ� 6�̻��� ��쿡�� ������ ������Ʈ���ش�.
            */
            if (6 <= pOP[0][i]) {
                //���� predictedHand[pOP[0][i]-6] �� false�� �������� ���ٰ� ������ ī�尡 ���� �������Ƿ� true�� ������Ʈ
                if (!ai1.predictedHand1[pOP[0][i] - 6]) ai1.predictedHand1[pOP[0][i] - 6] = true;
                //2:2 ���� ai2�� predictedHand�� ������Ʈ ���ش�.
                if (mode && !ai2.predictedHand1[pOP[0][i] - 6]) ai2.predictedHand1[pOP[0][i] - 6] = true;
            }
            //���� ������Ʈ ������ player2 �� ��쿡�� �ݺ��ؼ� �����Ѵ�.
            if (mode && 6 <= pOP[1][i]) {
                if (!ai1.predictedHand2[pOP[1][i] - 6]) ai1.predictedHand2[pOP[1][i] - 6] = true;
                if (!ai2.predictedHand2[pOP[1][i] - 6]) ai2.predictedHand2[pOP[1][i] - 6] = true;
            }
            /*
            �� ����� ī�� �ߵ� ������ �������ִ� ���ǹ��̴�.
            pOP[i]==null�̸� �ش� �÷��̾�� �׾����Ƿ� �� �÷��̾��� ����� ������� �ʰ� ������ �����Ѵ�.
            getOrder�� �÷��̾� �迭�� ī�� ��ȣ�� ������ ī�� ���� ������ �°� "���ĵ� �÷��̾� ����"�� ��ȯ�Ѵ�.
            */
            if (mode) {
                if (pOP[0] == null) {
                    if (pOP[2] == null) order = getOrder(new int[][]{new int[]{1, 3}, new int[]{pOP[1][i], pOP[3][i]}});
                    else if (pOP[3] == null)
                        order = getOrder(new int[][]{new int[]{1, 2}, new int[]{pOP[1][i], pOP[2][i]}});
                    else order = getOrder(new int[][]{new int[]{1, 2, 3}, new int[]{pOP[1][i], pOP[2][i], pOP[3][i]}});
                } else if (pOP[1] == null) {
                    if (pOP[2] == null) order = getOrder(new int[][]{new int[]{0, 3}, new int[]{pOP[0][i], pOP[3][i]}});
                    else if (pOP[3] == null)
                        order = getOrder(new int[][]{new int[]{0, 2}, new int[]{pOP[0][i], pOP[2][i]}});
                    else order = getOrder(new int[][]{new int[]{0, 2, 3}, new int[]{pOP[0][i], pOP[2][i], pOP[3][i]}});
                } else {
                    if (pOP[2] == null)
                        order = getOrder(new int[][]{new int[]{0, 1, 3}, new int[]{pOP[0][i], pOP[1][i], pOP[3][i]}});
                    else if (pOP[3] == null)
                        order = getOrder(new int[][]{new int[]{0, 1, 2}, new int[]{pOP[0][i], pOP[1][i], pOP[2][i]}});
                    else
                        order = getOrder(new int[][]{new int[]{0, 1, 2, 3}, new int[]{pOP[0][i], pOP[1][i], pOP[2][i], pOP[3][i]}});
                }
            } else order = getOrder(new int[][]{new int[]{0, 1}, new int[]{pOP[0][i], pOP[1][i]}});

            //���������� ī�带 �����ϴ� for���̴�.
            for (int k = 0; k < order.length; ++k) {
                /*
                ���ĵ� �÷��̾� ���� order[k]�� �°� Card list���� ī�� ��ȣ�� act()�� �����Ѵ�.
                star =23ī���� ��� �������� ��ǥ�� �ʿ��ϹǷ� StarPoint�� �Ἥ ��ǥ�� ���� �޴´�.
                */
                if (pOP[order[k]][i] == 23)
                    Card.cards.get(pOP[order[k]][i]).act(mode ? order[k] : 2 * order[k], StarPoint[order[k]][0], StarPoint[order[k]][1]);
                else
                    Card.cards.get(pOP[order[k]][i]).act(mode ? order[k] : 2 * order[k]);
            }
            //�÷��̾�� ai�� shield�� ������� ����ǹǷ� ���� �ٽ� 0���� �ʱ�ȭ ���ش�.
            player1.shield = ai1.shield = 0;
            if (mode) player2.shield = ai2.shield = 0;
            //���Ͽ��� �� ����� ������ ���� ����� ��Ȳ�� ����Ѵ�. ��ü �ϰ��� �ٸ��Ƿ� +900 ���ؼ� �������ش�(������)
            showStatus(i + 900);
        }
        //�� ���� ������ mp�� �⺻������ 15ȸ���Ѵ�.
        player1.mp = (player1.mp > 85 ? 100 : player1.mp + 15);
        ai1.mp = (ai1.mp > 85 ? 100 : ai1.mp + 15);
        if (mode) {
            player2.mp = (player2.mp > 85 ? 100 : player2.mp + 15);
            ai2.mp = (ai2.mp > 85 ? 100 : ai2.mp + 15);
        }
    }

    public int[] getOrder(int[][] op) {
        //op[0]���� ������ �÷��̾� �迭�� ������ op[1]�� �� �÷��̾���� ����ϴ� ī�尡 �迭�� ���´�.
        //�� �÷��̾ ����ϴ� ī���� priority�� �޾Ƽ� �� ���� �°� op[0]�� �����ϰ� ��ȯ�Ѵ�.
        for (int i = 0; i < op[0].length - 1; ++i) {
            for (int j = i + 1; j < op[0].length; ++j) {
                if (Card.cards.get(op[1][i]).getPriority() > Card.cards.get(op[1][j]).getPriority()) {
                    int t = op[0][i];
                    op[0][i] = op[0][j];
                    op[0][j] = t;
                    t = op[1][i];
                    op[1][i] = op[1][j];
                    op[1][j] = t;
                }
            }
        }
        return op[0];
    }

    public void showStatus(int t) {
        //field�� �׷��ش�. ���� ��ġ�� ���� �� �ɸ��ۿ� �Ⱥ��̹Ƿ� ��ǥ������ �����ϸ� �ȴ�. ������
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
                for (int j = 0; j < 4; ++j) {
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
        //�÷��̾�, ai�� hp���� Ȯ���ؼ� ������ ���������� ��ȯ���ش�.
        if (mode) {
            return (player1.hp <= 0 && player2.hp <= 0) || (ai1.hp <= 0 && ai2.hp <= 0);
        }
        return player1.hp <= 0 || ai1.hp <= 0;
    }

    public Player getPlayer(int va) {
        //Gamesystem���� �÷��̾ ��ȯ���ִ� �Լ�
        switch (va) {
            case 0:
                return GameSystem.gs.player1;
            case 1:
                return GameSystem.gs.player2;
        }
        return null;
    }

    public AI getAI(int va) {
        //ai�� ��ȯ���ִ� �Լ�.
        switch (va) {
            case 2:
                return GameSystem.gs.ai1;
            case 3:
                return GameSystem.gs.ai2;
        }
        return null;
    }
}
