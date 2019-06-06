package project.system;

import java.util.Scanner;

import project.cards.Card;
import project.cards.CardType;
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

public class GameSystem {

    public static Player player;
    public static AI ai;

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
        // gs.onEnable();

        //Example of 1vs1 mod with stage 1
        player = new Player(1, 0);
        ai = new AI(1, 1, 2);

        ProcessLock sharedLock = new ProcessLock();
        int[] playerOP, aiOP;

        //test for getCardFromDeck and Mulligan, show player's hand methods
        player.show();
        String b = GameSystem.gs.getInput("mulligan true/false : ");
        if (b.equals("true") || b.equals("True") || b.equals("TRUE") || b.equals("t") || b.equals("T")) {
            while (player.mulligan() != 0) ;
            System.out.println("Result of Mulligan");
            player.show();
            System.out.println();
        }
        int turn = 0;
        gs.showStatus(turn++);
        //Test for turn playing
        while (player.hp > 0 && ai.hp > 0) {
            PlayerProcess PlayerDecision = new PlayerProcess();
            PlayerDecision.Lock = sharedLock;
            AIProcess AIDecision = new AIProcess();
            AIDecision.Lock = sharedLock;
            playerOP = PlayerDecision.call();
            aiOP = AIDecision.call();
            if (aiOP == null) return;

            //Player's card operation
            System.out.printf("Player : %d %d %d\n", playerOP[0], playerOP[1], playerOP[2]);
            //AI's card operation
            System.out.printf("AI : %d %d %d\n", aiOP[0], aiOP[1], aiOP[2]);
            gs.play_turn(playerOP, aiOP);
            gs.showStatus(turn++);
            System.out.println();
        }

        if (player.hp <= 0) System.out.println("AI WIN");
        else System.out.println("Player WIN");
    }

    public void play_turn(int[] pOP, int[] aOP) {
        for (int i = 0; i < 3; ++i) {
            if (ai.hand[0] && Math.random() < 0.15) {
                aOP[i] = 0;
                System.out.printf("AI FOOL ACTIVATE %dth card\n", i);
            }
            if (player.hand[0] && Math.random() < 0.15) {
                pOP[i] = 0;
                System.out.printf("PLAYER FOOL ACTIVATE %dth card\n", i);
            }

            if (6 <= pOP[i] && !ai.hand[pOP[i] - 6])
                ai.hand[pOP[i] - 6] = true;
            if (isAIFirst(pOP[i], aOP[i])) {
                Card.cards.get(aOP[i]).act(1);
                Card.cards.get(pOP[i]).act(0);
            } else {
                Card.cards.get(pOP[i]).act(0);
                Card.cards.get(aOP[i]).act(1);
            }
            player.shield = ai.shield = 0;
        }
        player.mp += 15;
        if (player.mp > 100) player.mp = 100;
        ai.mp += 15;
        if (ai.mp > 100) ai.mp = 100;

    }

    public boolean isAIFirst(int a, int b) {
        return Card.cards.get(b).type.ordinal() < Card.cards.get(a).type.ordinal();
    }

    public void showStatus(int t) {
        System.out.printf("Turn : %d\n", t);
        System.out.printf("Player = HP: %d, MP: %d -------------------- AI = HP: %d, MP: %d\n", player.hp, player.mp, ai.hp, ai.mp);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; ++j) {
                if (i == player.y && j == player.x) System.out.print("P ");
                else if (i == ai.y && j == ai.x) System.out.print("A ");
                else System.out.print("O ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
