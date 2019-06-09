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
import project.scene.transition.EndingScene;
import project.scene.transition.GameOverScene;
import project.scene.transition.WinScene;
import project.util.StringUtils;

public class GameSystem {

	private static GameSystem gs;
	private SceneManager sceneManager;
	private Ranking[] rankings;

	private Scanner in;

	private double score;

	public static boolean mode = false;// false for 1v1, true for 2v2. not for ranking

	public static GameSystem getInstance() {
		if (gs == null)
			gs = new GameSystem();
		return gs;
	}

	private GameSystem() {
		in = new Scanner(System.in);
		rankings = new Ranking[] { new Ranking<Double>("1v1.rank", "1v1", Score1v1.class),
				new Ranking<Double>("2v2.rank", "2v2", Score2v2.class) };
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
		sceneManager.addScene("field", FieldScene.getInstance());
		sceneManager.addScene("select", SelectScene.getInstance());

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

	public void resetScore() {
		score = 0;
	}

	public void addScore(double d) {
		score += d;
	}

	public double getScore() {
		return score;
	}

	public static void main(String[] args) {
		GameSystem gs = GameSystem.getInstance();
		gs.onEnable();
	}

}
