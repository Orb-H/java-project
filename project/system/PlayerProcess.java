package project.system;

import java.util.concurrent.Callable;

import project.cards.Card;
import project.scene.battle.SelectScene;

public class PlayerProcess implements Callable<int[]> {

	ProcessLock Lock;
	int plyInfo;

	private GameSystem gs;
	private SelectScene ss;

	public PlayerProcess(int plynum, ProcessLock b) {
		plyInfo = plynum;
		Lock = b;

		gs = GameSystem.getInstance();
		ss = SelectScene.getInstance();
	}

	public int[] call() {
		while (Lock.checkPlayerLock() && Lock.checkWrite())
			;
		Lock.DoWrite();
		boolean done = false;
		int[] op = new int[3];
		int estimate_cost;
		// String input;
		String[] op_string;
		Player ply = ss.getPlayer(plyInfo);
		// Show current hand
		// System.out.printf("Player %d's hand\n", plyInfo + 1);
		// ply.show();
		// System.out.println("Left -> L, Right -> R, Up -> U, Down -> D, Guard -> G,
		// Charge -> C");
		do {
			estimate_cost = 0;
			op_string = gs.getInput("Choose order of Cards : ").split(" ");
			if (op_string.length != 3) {
				System.out.println("Wrong Length of Cards\n");
				continue;
			}
			if (op_string[0].equals(op_string[1]) || op_string[0].equals(op_string[2])
					|| op_string[1].equals(op_string[2])) {
				System.out.println("Cards can use only one time per turn");
				continue;
			}
			for (int i = 0; i < 3; ++i)
				if (op_string[i].equals("0")) {
					System.out.println("Cannot choose FOOL CARD");
					continue;
				}
			for (int i = 0; i < 3; ++i) {
				if (op_string[i].equals("L") || op_string[i].equals("l"))
					op[i] = 0;
				else if (op_string[i].equals("R") || op_string[i].equals("r"))
					op[i] = 1;
				else if (op_string[i].equals("U") || op_string[i].equals("u"))
					op[i] = 2;
				else if (op_string[i].equals("D") || op_string[i].equals("d"))
					op[i] = 3;
				else if (op_string[i].equals("G") || op_string[i].equals("g"))
					op[i] = 4;
				else if (op_string[i].equals("C") || op_string[i].equals("c")) {
					op[i] = 5;
					estimate_cost -= 15;
				} else {
					try {
						op[i] = Integer.parseInt(op_string[i]);
					} catch (NumberFormatException ex) {
						System.out.println("Cannot convert to integer error");
						break;
					}
					try {
						if (ply.getHand()[op[i]]) {
							op[i] += 6;
							estimate_cost += Card.cards.get(op[i]).getCost();
						} else {
							System.out.println("Not in your hand error");
							break;
						}
					} catch (ArrayIndexOutOfBoundsException ex) {
						System.out.println("Array out of bounds error");
						break;
					}
				}
				if (estimate_cost > ply.mp) {
					System.out.println("Not enough mp");
					break;
				}
				if (i == 2)
					done = true;
			}
		} while (!done);
		Lock.EndPlayerTurn();
		Lock.EndWrite();
		return op;
	}

}
