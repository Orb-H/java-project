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
        //모드 입력
        String temp = gs.getInput("is 2v2 mode ? : ");
        //모드 설정
        if (temp.equals("true") || temp.equals("t")) mode = true;
        //ai와 player 객체 생성 기본 설정 3스테이지
        gs.player1 = new Player(1, 0);
        gs.ai1 = new AI(1, 3, 3);
        //2대2 모드면 객체 2개 더 생성
        if (mode) {
            gs.player2 = new Player(3, 0);
            gs.ai2 = new AI(3, 3, 3);
        }
        //플레이어와 ai결정이 같이 끝나도록 lock 걸어주는 객체
        ProcessLock sharedLock = new ProcessLock();
        //플레이어와 ai의 카드 결정을 저장하는 int 배열 size는 3
        int[] player1OP = null, ai1OP = null, player2OP = null, ai2OP = null;
        //현재 턴
        int turn = 0;

        //플레이어의 패를 보여주고 멀리건을 실행
        System.out.println("---Player 1's hand---");
        gs.player1.show();
        gs.player1.mulligan();
        if (mode) {
            //2대2 모드면 플레이어2도 현재 패를 보여주고 멀리건을 실행
            System.out.println("---Player 2's hand----");
            gs.player2.show();
            gs.player2.mulligan();
        }
        //현재 게임 상태를 그려주는 함수 (UI 추가되면 없어도됨, ai 디버깅용
        gs.showStatus(turn++);

        //게임이 끝날때 까지 while을 돌린다.
        while (!gs.checkGameEnded()) {
            //플레이어와 ai의 카드 선택을 결정하는 thread 생성.
            PlayerProcess PlayerDecision = null;
            AIProcess AIDecision = null;
            //플레이어 1의 체력이 0보다 크면 선택을 결정한다.
            if (gs.player1.hp > 0) {
                PlayerDecision = new PlayerProcess(0, sharedLock);
                player1OP = PlayerDecision.call();
            }
            //ai1의 체력이 0보다 크면 선택을 결정한다.
            if (gs.ai1.hp > 0) {
                AIDecision = new AIProcess(2, sharedLock);
                ai1OP = AIDecision.call();
            }
            //2대2 모드고 플레이어 2의 체력이 0보다 크면 결정.
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

            //결정된 명령 배열로 턴을 실행한다.
            if (mode) gs.play_turn(new int[][]{player1OP, player2OP, ai1OP, ai2OP});
            else gs.play_turn(new int[][]{player1OP, ai1OP});

            //턴이 끝난후 결과 field를 한번 보여준다.
            gs.showStatus(turn++);
            System.out.println();
        }
        //게임이 끝난후 승,패, 무승부를 결정하는 부분이다.
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
        //각 명령어를 이차원 배열로 받는다. pOP의 크기는 [2][3] 이거나 2:2 모드면 [4][3]이 된다.
        int[] order; //한 페이즈의 명령어 실행 순서를 저장하는 배열
        String str;
        int[][] StarPoint = new int[4][]; //만약 star 카드를 사용했다면 star가 떨어지는 좌표를 저장하는 배열
        //for문으로 star카드가 사용됬는지를 검사하고 사용됬으면 star의 좌표를 입력받는다.
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < pOP.length; ++k) {
                if (pOP[k] == null) continue; //null 명령어면 플레이어가 죽었다는 뜻이므로 넘어간다.
                if (pOP[k][i] == 23) { //star가 사용됬으면
                    if (mode) { //2:2 이면 k = 0,1이 사용자 2,3이 ai이다.
                        if (k < 2) {
                            //사용자가 쓴 star의 좌표를 입력받는다.
                            str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
                            StarPoint[k] = StringUtils.Split2Int(str, ",");
                            //좌표입력 예외처리, 다시 입력받는다.
                            if (StarPoint[k] == null) {
                                --k;
                                continue;
                            }
                        } else {
                            //ai가 star를 썻으면 상하좌우, 현재위치를 1/5 확률로 랜덤하게 준다.
                            double ch = Math.random();
                            if (ch < 0.2) StarPoint[k] = new int[]{player1.y, player1.x + 1};
                            else if (ch < 0.4) StarPoint[k] = new int[]{player1.y + 1, player1.x};
                            else if (ch < 0.6) StarPoint[k] = new int[]{player1.y, player1.x - 1};
                            else if (ch < 0.8) StarPoint[k] = new int[]{player1.y - 1, player1.x};
                            else StarPoint[k] = new int[]{player1.y, player1.x};
                        }
                    } else {
                        //1:1 모드면 0이 사용자 1이 ai이다.
                        if (k == 0) {
                            //사용자 star 좌표입력
                            str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
                            StarPoint[k] = StringUtils.Split2Int(str, ",");
                            //예외처리
                            if (StarPoint[k] == null) {
                                --k;
                                continue;
                            }
                        } else {
                            //ai star좌표입력
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
        //페이즈를 실행하는 for문이다. 총 3개의 페이즈가 있으며 페이즈 실행 전에 fool card가 발동될지를 결정한다.
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < pOP.length; ++k) {
                if (pOP[k] == null) continue; //pOP가 null이면 죽은 플레이어니까 스킵
                //2:2일때 fool card 발동여부를 결정한다.
                if (mode) {
                    /*
                    각 플레이어 마다 foolcard가 있는지 hand[0], 그리고 발동되는지 math.random()<0.15를 결정하고 발동됬으면
                    pOP[k][i]를 foolcard = 6 으로 바꿔주고 메세지를 출력한다.
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
                //1:1 일때도 똑같이 fool card 발동여부를 결정한다.
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
            fool card 발동이 결정됬으면 ai의 predictedhand( 플레이어의 패를 예상하는 부분) 을 업데이트 해준다.
            6미만의 카드들은 기본카드이므로 6이상인 경우에만 정보를 업데이트해준다.
            */
            if (6 <= pOP[0][i]) {
                //만약 predictedHand[pOP[0][i]-6] 이 false면 기존까지 없다고 생각한 카드가 새로 나왔으므로 true로 업데이트
                if (!ai1.predictedHand1[pOP[0][i] - 6]) ai1.predictedHand1[pOP[0][i] - 6] = true;
                //2:2 모드면 ai2의 predictedHand도 없데이트 해준다.
                if (mode && !ai2.predictedHand1[pOP[0][i] - 6]) ai2.predictedHand1[pOP[0][i] - 6] = true;
            }
            //위의 업데이트 과정을 player2 의 경우에도 반복해서 실행한다.
            if (mode && 6 <= pOP[1][i]) {
                if (!ai1.predictedHand2[pOP[1][i] - 6]) ai1.predictedHand2[pOP[1][i] - 6] = true;
                if (!ai2.predictedHand2[pOP[1][i] - 6]) ai2.predictedHand2[pOP[1][i] - 6] = true;
            }
            /*
            한 페이즈에 카드 발동 순서를 결정해주는 조건문이다.
            pOP[i]==null이면 해당 플레이어는 죽었으므로 그 플레이어의 명령은 고려하지 않고 순서를 결정한다.
            getOrder는 플레이어 배열과 카드 번호를 넣으면 카드 실행 순서에 맞게 "정렬된 플레이어 순서"를 반환한다.
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

            //본격적으로 카드를 실행하는 for문이다.
            for (int k = 0; k < order.length; ++k) {
                /*
                정렬된 플레이어 순서 order[k]에 맞게 Card list에서 카드 번호의 act()를 실행한다.
                star =23카드의 경우 떨어지는 좌표가 필요하므로 StarPoint를 써서 좌표도 같이 받는다.
                */
                if (pOP[order[k]][i] == 23)
                    Card.cards.get(pOP[order[k]][i]).act(mode ? order[k] : 2 * order[k], StarPoint[order[k]][0], StarPoint[order[k]][1]);
                else
                    Card.cards.get(pOP[order[k]][i]).act(mode ? order[k] : 2 * order[k]);
            }
            //플레이어와 ai의 shield는 한페이즈만 적용되므로 값을 다시 0으로 초기화 해준다.
            player1.shield = ai1.shield = 0;
            if (mode) player2.shield = ai2.shield = 0;
            //한턴에서 한 페이즈가 끝날때 마다 진행된 상황을 출력한다. 전체 턴과는 다르므로 +900 을해서 구분해준다(디버깅용)
            showStatus(i + 900);
        }
        //한 턴이 끝나면 mp를 기본적으로 15회복한다.
        player1.mp = (player1.mp > 85 ? 100 : player1.mp + 15);
        ai1.mp = (ai1.mp > 85 ? 100 : ai1.mp + 15);
        if (mode) {
            player2.mp = (player2.mp > 85 ? 100 : player2.mp + 15);
            ai2.mp = (ai2.mp > 85 ? 100 : ai2.mp + 15);
        }
    }

    public int[] getOrder(int[][] op) {
        //op[0]에는 정렬할 플레이어 배열이 들어오고 op[1]은 각 플레이어들이 사용하는 카드가 배열로 들어온다.
        //각 플레이어가 사용하는 카드의 priority를 받아서 그 값에 맞게 op[0]를 정렬하고 반환한다.
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
        //field를 그려준다. 서로 겹치는 경우는 한 케릭밖에 안보이므로 좌표값으로 구분하면 된다. 디버깅용
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
        //플레이어, ai의 hp값을 확인해서 게임이 끝났는지를 반환해준다.
        if (mode) {
            return (player1.hp <= 0 && player2.hp <= 0) || (ai1.hp <= 0 && ai2.hp <= 0);
        }
        return player1.hp <= 0 || ai1.hp <= 0;
    }

    public Player getPlayer(int va) {
        //Gamesystem에서 플레이어를 반환해주는 함수
        switch (va) {
            case 0:
                return GameSystem.gs.player1;
            case 1:
                return GameSystem.gs.player2;
        }
        return null;
    }

    public AI getAI(int va) {
        //ai를 반환해주는 함수.
        switch (va) {
            case 2:
                return GameSystem.gs.ai1;
            case 3:
                return GameSystem.gs.ai2;
        }
        return null;
    }
}
