package project.scene;

import java.util.ArrayList;
import java.util.List;

import project.system.GameSystem;
import project.util.StringUtils;

public abstract class Scene {

	protected List<Component> components = new ArrayList<>();
	protected char[][] toRender;
	protected SceneManager sm;
	protected GameSystem gs;

	protected Scene() {
		sm = SceneManager.getInstance();
		gs = GameSystem.getInstance();
		toRender = new char[50][140];
		addComponent(new BorderComponent(0, 0, 140, 50));
	}

	public void render() {
		sm.clearScreen();
		for (int i = 0; i < toRender.length; i++) {
			System.out.println(StringUtils.alignedToString(toRender[i]));
		}
	}

	public void addComponent(Component c) {
		components.add(c);
	}

	public void deleteComponent(Component c) {
		components.remove(c);
	}

	public void resize(int x, int y) {
		toRender = new char[y][x];
		deleteComponent(components.get(0));
		components.add(0, new BorderComponent(0, 0, x, y));
	}

	public void repaint() {
		for (int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			for (int y = c.loc.getY(); y < c.loc.getY() + c.size.getY(); y++) {
				for (int x = c.loc.getX(); x < c.loc.getX() + c.size.getX(); x++) {
					toRender[y][x] = c.charAt(x, y);
				}
			}
		}
		render();
	}

	protected void onLoad() {

	}

	public String getInput(String s) {
		return gs.getInput(s);
	}

}
