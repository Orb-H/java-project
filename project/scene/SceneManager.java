package project.scene;

import java.util.HashMap;

public class SceneManager {

	private static SceneManager sm;
	private Scene current = null;

	private HashMap<String, Scene> scenes = new HashMap<>();

	public static SceneManager getInstance() {
		if (sm == null)
			sm = new SceneManager();
		return sm;
	}

	private SceneManager() {
	}

	public void addScene(String name, Scene s) {
		scenes.put(name, s);
	}

	public void loadScene(String name) {
		current = scenes.get(name);
		current.repaint();
		current.onLoad();
	}

	public void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void repaint() {
		clearScreen();
		current.render();
	}

}
