package project.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.system.AI;
import project.system.GameSystem;
import project.system.Player;
import project.util.Point;

public class AttackCard extends Card {

	List<Point> range = new ArrayList<>();

	public AttackCard(String name, int number, String[] ascii, String description, int deal, int cost, Point[] range) {
		super(CardType.ATTACK, name, number, ascii, description, deal, cost);
		this.range = Arrays.asList(range);
	}

	public boolean inBound(int dy, int dx) {
		return range.contains(new Point(dx, dy));
	}

	public void act(int caster) {
		int dmg;
		Player[] ply;
		AI[] ai;
		if (GameSystem.mode) {
			ply = new Player[] { ss.getPlayer(0), ss.getPlayer(1) };
			ai = new AI[] { ss.getAI(2), ss.getAI(3) };
		} else {
			ply = new Player[] { ss.getPlayer(0) };
			ai = new AI[] { ss.getAI(2) };
		}
		if (caster < 2) {
			for (int i = 0; i < ai.length; ++i) {
				if (inBound(ai[i].y - ply[caster].y, ai[i].x - ply[caster].x)) {
					dmg = deal + ai[i].shield;
					ai[i].hp -= dmg > 0 ? dmg : 0;
					ai[i].cnt_def++;
				}
			}
			ply[caster].mp -= cost;
			ply[caster].cnt_atk++;
		} else {
			for (int i = 0; i < ply.length; ++i) {
				if (inBound(ply[i].y - ai[caster - 2].y, ply[i].x - ai[caster - 2].x)) {
					dmg = deal + ply[i].shield;
					ply[i].hp -= dmg > 0 ? dmg : 0;
					ply[i].cnt_def++;
				}
			}
			ai[caster - 2].mp -= cost;
			ai[caster - 2].cnt_atk++;
		}
	}

	public void act(int caster, int sy, int sx) {
	}

}
