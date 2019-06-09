package project.scene.battle;

import java.util.List;

import project.cards.Card;
import project.scene.Scene;
import project.scene.transition.WinScene;
import project.system.AI;
import project.system.AIProcess;
import project.system.GameSystem;
import project.system.Player;
import project.system.PlayerProcess;
import project.system.ProcessLock;
import project.util.StringUtils;

public class SelectScene extends Scene {

	private static SelectScene ss;

	private FieldScene fs;

	private CardComponent[][] cards;

	private Player player1, player2;
	private AI ai1, ai2;

	private int usedMP = 0;
	private final double constant = Math.pow(115, 0.8307);

	public static SelectScene getInstance() {
		if (ss == null)
			ss = new SelectScene();
		return ss;
	}

	private SelectScene() {
		fs = FieldScene.getInstance();

		addComponent(fs.vs);

		cards = new CardComponent[3][6];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				cards[i][j] = new CardComponent(17 + 18 * j, 8 + 10 * i, null);
			}
		}

		addComponent(fs.p1);
		addComponent(fs.p1Health);
		addComponent(fs.p1Mana);

		addComponent(fs.e1);
		addComponent(fs.e1Health);
		addComponent(fs.e1Mana);

		for (int i = 0; i < 3; i++) {
			addComponent(fs.p1Selected[i]);

			for (int j = 0; j < 6; j++) {
				addComponent(cards[i][j]);
			}
		}

		addComponent(fs.separator);
	}

	public void changeMode() {
		if (GameSystem.mode) {
			resize(140, 66);

			addComponent(fs.p2);
			addComponent(fs.p2Health);
			addComponent(fs.p2Mana);

			addComponent(fs.e2);
			addComponent(fs.e2Health);
			addComponent(fs.e2Mana);

			for (int i = 0; i < 3; i++) {
				addComponent(fs.p2Selected[i]);

				for (int j = 0; j < 6; j++) {
					cards[i][j].changeLocation(17 + 18 * j, 14 + 10 * i);
				}
			}
		} else {
			resize(140, 50);

			deleteComponent(fs.p2);
			deleteComponent(fs.p2Health);
			deleteComponent(fs.p2Mana);

			deleteComponent(fs.e2);
			deleteComponent(fs.e2Health);
			deleteComponent(fs.e2Mana);

			for (int i = 0; i < 3; i++) {
				deleteComponent(fs.p2Selected[i]);

				for (int j = 0; j < 6; j++) {
					cards[i][j].changeLocation(17 + 18 * j, 8 + 10 * i);
				}
			}
		}
	}

	public void resetCard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				cards[i][j].setCard(null);
			}
		}
	}

	public void resetSelect(CardComponent[] c) {
		for (int i = 0; i < 3; i++) {
			c[i].setCard(null);
		}
	}

	public void init(int stage) {
		usedMP = 0;

		fs.p1Health.setAmount(100);
		fs.p1Mana.setAmount(100);
		fs.p2Health.setAmount(100);
		fs.p2Mana.setAmount(100);
		fs.e1Health.setAmount(100);
		fs.e1Mana.setAmount(100);
		fs.e2Health.setAmount(100);
		fs.e2Mana.setAmount(100);
		resetSelect(fs.p1Selected);
		resetSelect(fs.p2Selected);
		repaint();

		// ai와 player 객체 생성 기본 설정 3스테이지
		if (stage == 1)
			player1 = new Player(1, 0);
		else
			player1.nextStage(1, 0);
		ai1 = new AI(1, 3, stage);
		fs.field[player1.y][player1.x].toggleValue(1);
		fs.field[ai1.y][ai1.x].toggleValue(2);
		// 2대2 모드면 객체 2개 더 생성
		if (GameSystem.mode) {
			if (stage == 1)
				player2 = new Player(3, 0);
			else
				player2.nextStage(3, 0);
			ai2 = new AI(3, 3, stage);
			fs.field[player2.y][player2.x].toggleValue(3);
			fs.field[ai2.y][ai2.x].toggleValue(4);
		}
		ProcessLock sharedLock = new ProcessLock();
		// 플레이어와 ai결정이 같이 끝나도록 lock 걸어주는 객체
		// 플레이어와 ai의 카드 결정을 저장하는 int 배열 size는 3
		int[] player1OP = null, ai1OP = null, player2OP = null, ai2OP = null;
		// 현재 턴
		// int turn = 0;

		// 플레이어의 패를 보여주고 멀리건을 실행
		showCards(player1.getCards());
		repaint();
		if (stage == 1) {
			player1.mulligan();

			showCards(player1.getCards());
			repaint();
		}
		if (GameSystem.mode) {
			getInput("Press Enter to see Player 2's cards");
			// 2대2 모드면 플레이어2도 현재 패를 보여주고 멀리건을 실행
			showCards(player2.getCards());
			repaint();
			if (stage == 1) {
				player2.mulligan();

				showCards(player2.getCards());
				repaint();
			}
		}
		// 현재 게임 상태를 그려주는 함수 (UI 추가되면 없어도됨, ai 디버깅용
		// gs.showStatus(turn++);

		getInput("Press Enter to select cards");

		// 게임이 끝날때 까지 while을 돌린다.
		while (!checkGameEnded()) {
			// 플레이어와 ai의 카드 선택을 결정하는 thread 생성.
			PlayerProcess PlayerDecision = null;
			AIProcess AIDecision = null;
			// 플레이어 1의 체력이 0보다 크면 선택을 결정한다.
			showCards(player1.getCards());
			repaint();
			if (player1.hp > 0) {
				PlayerDecision = new PlayerProcess(0, sharedLock);
				player1OP = PlayerDecision.call();
				setCards(fs.p1Selected, player1OP);
				repaint();
			}
			// ai1의 체력이 0보다 크면 선택을 결정한다.
			if (ai1.hp > 0) {
				AIDecision = new AIProcess(2, sharedLock);
				ai1OP = AIDecision.call();
			}
			// 2대2 모드고 플레이어 2의 체력이 0보다 크면 결정.
			if (GameSystem.mode && player2.hp > 0) {
				showCards(player2.getCards());
				repaint();
				PlayerDecision = new PlayerProcess(1, sharedLock);
				player2OP = PlayerDecision.call();
				setCards(fs.p2Selected, player2OP);
				repaint();
			}
			if (GameSystem.mode && ai2.hp > 0) {
				AIDecision = new AIProcess(3, sharedLock);
				ai2OP = AIDecision.call();
			}
			// Player's card operation
			// System.out.printf("Player 1's OP: %d %d %d\n", player1OP[0], player1OP[1],
			// player1OP[2]);
			// if (GameSystem.mode)
			// System.out.printf("Player 2's OP: %d %d %d\n", player2OP[0], player2OP[1],
			// player2OP[2]);

			// AI's card operation
			// System.out.printf("AI 1: %d %d %d\n", ai1OP[0], ai1OP[1], ai1OP[2]);
			// setCards(fs.e1Selected, ai1OP);
			// if (GameSystem.mode)
			// setCards(fs.e2Selected, ai2OP);
			// System.out.printf("AI 2: %d %d %d\n", ai2OP[0], ai2OP[1], ai2OP[2]);

			sm.loadScene("field");

			resetSelect(fs.e1Selected);
			resetSelect(fs.e2Selected);
			fs.repaint();
			if (GameSystem.mode)
				play_turn(new int[][] { player1OP, player2OP, ai1OP, ai2OP });
			else
				play_turn(new int[][] { player1OP, ai1OP });

			// showStatus(turn++);
			// System.out.println();
		}
		if (GameSystem.mode) {
			if (player1.hp <= 0 && player2.hp <= 0) {
				sm.loadScene("gameover");
			} else if (ai1.hp <= 0 && ai2.hp <= 0) {
				if (stage == 3)
					sm.loadScene("ending");
				else {
					gs.addScore((player1.hp + player2.hp) / Math.pow(usedMP, 0.8307) * constant);
					WinScene.getInstance().setNext(stage + 1);
					sm.loadScene("win");
				}
			}
		} else {
			if (player1.hp <= 0) {
				sm.loadScene("gameover");
			} else if (ai1.hp <= 0)
				if (stage == 3)
					sm.loadScene("ending");
				else {
					gs.addScore(
							player1.hp / Math.pow(usedMP, 0.8307) * (1.5 - player1.cnt_def / ai1.cnt_atk) * constant);
					WinScene.getInstance().setNext(stage + 1);
					sm.loadScene("win");
				}
		}
	}

	public void showCards(List<Card> c) {
		resetCard();
		for (int i = 0; i < c.size(); i++) {
			cards[i / 6][i % 6].setCard(c.get(i));
		}
	}

	public void setCards(CardComponent[] cards, int[] c) {
		for (int i = 0; i < 3; i++) {
			cards[i].setCard(Card.cards.get(c[i]));
		}
	}

	public void updateField() {
		updateLocation();
		updateAmount();
		fs.repaint();
	}

	public void updateLocation() {
		fs.resetField();
		fs.field[player1.y][player1.x].toggleValue(1);
		fs.field[ai1.y][ai1.x].toggleValue(2);
		if (GameSystem.mode) {
			fs.field[player2.y][player2.x].toggleValue(3);
			fs.field[ai2.y][ai2.x].toggleValue(4);
		}
	}

	public void updateAmount() {
		fs.p1Health.setAmount(player1.hp);
		fs.p1Mana.setAmount(player1.mp);
		fs.e1Health.setAmount(ai1.hp);
		fs.e1Mana.setAmount(ai1.mp);
		if (GameSystem.mode) {
			fs.p2Health.setAmount(player2.hp);
			fs.p2Mana.setAmount(player2.mp);
			fs.e2Health.setAmount(ai2.hp);
			fs.e2Mana.setAmount(ai2.mp);
		}
	}

	public void play_turn(int[][] pOP) {
		int[] order;
		String str;
		int[][] StarPoint = new int[4][];
		for (int i = 0; i < 3; ++i) {
			for (int k = 0; k < pOP.length; ++k) {
				if (pOP[k] == null)
					continue; // null 명령어면 플레이어가 죽었다는 뜻이므로 넘어간다.
				if (pOP[k][i] == 23) {
					if (GameSystem.mode) { // 2:2 이면 k = 0,1이 사용자 2,3이 ai이다.
						if (k < 2) {
							// 사용자가 쓴 star의 좌표를 입력받는다.
							str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
							StarPoint[k] = StringUtils.Split2Int(str, ",");
							// 좌표입력 예외처리, 다시 입력받는다.
							if (StarPoint[k] == null) {
								--k;
								continue;
							}
						} else {
							double ch = Math.random();
							if (ch < 0.2)
								StarPoint[k] = new int[] { player1.y, player1.x + 1 };
							else if (ch < 0.4)
								StarPoint[k] = new int[] { player1.y + 1, player1.x };
							else if (ch < 0.6)
								StarPoint[k] = new int[] { player1.y, player1.x - 1 };
							else if (ch < 0.8)
								StarPoint[k] = new int[] { player1.y - 1, player1.x };
							else
								StarPoint[k] = new int[] { player1.y, player1.x };
						}
					} else {
						// 1:1 모드면 0이 사용자 1이 ai이다.
						if (k == 0) {
							// 사용자 star 좌표입력
							str = gs.getInput("Submit (y,x) to use XVII: The Star\n");
							StarPoint[k] = StringUtils.Split2Int(str, ",");
							// 예외처리
							if (StarPoint[k] == null) {
								--k;
								continue;
							}
						} else {
							// ai star좌표입력
							double ch = Math.random();
							if (ch < 0.2)
								StarPoint[k] = new int[] { player1.y, player1.x + 1 };
							else if (ch < 0.4)
								StarPoint[k] = new int[] { player1.y + 1, player1.x };
							else if (ch < 0.6)
								StarPoint[k] = new int[] { player1.y, player1.x - 1 };
							else if (ch < 0.8)
								StarPoint[k] = new int[] { player1.y - 1, player1.x };
							else
								StarPoint[k] = new int[] { player1.y, player1.x };
						}
					}
				}
			}
		}

		for (int i = 0; i < 3; ++i) {
			for (int k = 0; k < pOP.length; ++k) {
				if (pOP[k] == null)
					continue; // pOP가 null이면 죽은 플레이어니까 스킵
				// 2:2일때 fool card 발동여부를 결정한다.
				if (GameSystem.mode) {
					if (k == 0 && player1.getHand()[0] && Math.random() < 0.15) {
						pOP[k][i] = 6;
						System.out.printf("Player 1's %dth card was changed by FOOL CARD\n", i + 1);
					} else if (k == 1 && player2.getHand()[0] && Math.random() < 0.15) {
						pOP[k][i] = 6;
						System.out.printf("Player 2's %dth card was changed by FOOL CARD\n", i + 1);
					} else if (k == 2 && ai1.getHand()[0] && Math.random() < 0.15) {
						pOP[k][i] = 6;
						System.out.printf("AI 1's %dth card was changed by FOOL CARD\n", i + 1);
					} else if (k == 3 && ai2.getHand()[0] && Math.random() < 0.15) {
						pOP[k][i] = 6;
						System.out.printf("AI 2's %dth card was changed by FOOL CARD\n", i + 1);
					}
				}
				// 1:1 일때도 똑같이 fool card 발동여부를 결정한다.
				else {
					if (k == 0 && player1.getHand()[0] && Math.random() < 0.15) {
						pOP[k][i] = 6;
						System.out.printf("Player %dth card was changed by FOOL CARD\n", i + 1);
					} else if (k == 1 && ai1.getHand()[0] && Math.random() < 0.15) {
						pOP[k][i] = 6;
						System.out.printf("AI %dth card was changed by FOOL CARD\n", i + 1);
					}
				}
			}

			if (6 <= pOP[0][i]) {
				if (!ai1.getPredictedHand1()[pOP[0][i] - 6])
					ai1.getPredictedHand1()[pOP[0][i] - 6] = true;
				if (GameSystem.mode && !ai2.getPredictedHand1()[pOP[0][i] - 6])
					ai2.getPredictedHand1()[pOP[0][i] - 6] = true;
			}

			if (GameSystem.mode && 6 <= pOP[1][i]) {
				if (!ai1.getPredictedHand2()[pOP[1][i] - 6])
					ai1.getPredictedHand2()[pOP[1][i] - 6] = true;
				if (!ai2.getPredictedHand2()[pOP[1][i] - 6])
					ai2.getPredictedHand2()[pOP[1][i] - 6] = true;
			}

			if (GameSystem.mode) {
				if (pOP[0] == null) {
					if (pOP[2] == null)
						order = getOrder(new int[][] { new int[] { 1, 3 }, new int[] { pOP[1][i], pOP[3][i] } });
					else if (pOP[3] == null)
						order = getOrder(new int[][] { new int[] { 1, 2 }, new int[] { pOP[1][i], pOP[2][i] } });
					else
						order = getOrder(
								new int[][] { new int[] { 1, 2, 3 }, new int[] { pOP[1][i], pOP[2][i], pOP[3][i] } });
				} else if (pOP[1] == null) {
					if (pOP[2] == null)
						order = getOrder(new int[][] { new int[] { 0, 3 }, new int[] { pOP[0][i], pOP[3][i] } });
					else if (pOP[3] == null)
						order = getOrder(new int[][] { new int[] { 0, 2 }, new int[] { pOP[0][i], pOP[2][i] } });
					else
						order = getOrder(
								new int[][] { new int[] { 0, 2, 3 }, new int[] { pOP[0][i], pOP[2][i], pOP[3][i] } });
				} else {
					if (pOP[2] == null)
						order = getOrder(
								new int[][] { new int[] { 0, 1, 3 }, new int[] { pOP[0][i], pOP[1][i], pOP[3][i] } });
					else if (pOP[3] == null)
						order = getOrder(
								new int[][] { new int[] { 0, 1, 2 }, new int[] { pOP[0][i], pOP[1][i], pOP[2][i] } });
					else
						order = getOrder(new int[][] { new int[] { 0, 1, 2, 3 },
								new int[] { pOP[0][i], pOP[1][i], pOP[2][i], pOP[3][i] } });
				}
			} else
				order = getOrder(new int[][] { new int[] { 0, 1 }, new int[] { pOP[0][i], pOP[1][i] } });

			for (int k = 0; k < order.length; ++k) {
				getInput("Press Enter to see next action");
				int caster;
				if (order.length == 4)
					caster = order[k];
				else
					caster = order[k] * 2;
				if (caster < 2)
					if (Card.cards.get(pOP[caster][i]).getCost() > 0)
						usedMP += Card.cards.get(pOP[caster][i]).getCost();
				if (caster == 2)
					fs.e1Selected[i].setCard(Card.cards.get(pOP[GameSystem.mode ? 2 : 1][i]));
				if (caster == 3)
					fs.e2Selected[i].setCard(Card.cards.get(pOP[3][i]));
				if (pOP[order[k]][i] == 23)
					Card.cards.get(pOP[order[k]][i]).act(GameSystem.mode ? order[k] : 2 * order[k],
							StarPoint[order[k]][0], StarPoint[order[k]][1]);
				else
					Card.cards.get(pOP[order[k]][i]).act(GameSystem.mode ? order[k] : 2 * order[k]);
				updateField();
			}
			player1.shield = ai1.shield = 0;
			if (GameSystem.mode)
				player2.shield = ai2.shield = 0;

			if (checkGameEnded())
				return;
			// showStatus(i + 900);
		}
		player1.mp = (player1.mp > 85 ? 100 : player1.mp + 15);
		ai1.mp = (ai1.mp > 85 ? 100 : ai1.mp + 15);
		if (GameSystem.mode) {
			player2.mp = (player2.mp > 85 ? 100 : player2.mp + 15);
			ai2.mp = (ai2.mp > 85 ? 100 : ai2.mp + 15);
		}
		updateField();
		getInput("Press Enter to go to select scene");
		resetSelect(fs.p1Selected);
		if (GameSystem.mode)
			resetSelect(fs.p2Selected);
		sm.loadScene("select");
	}

	boolean checkGameEnded() {
		if (GameSystem.mode) {
			return (player1.hp <= 0 && player2.hp <= 0) || (ai1.hp <= 0 && ai2.hp <= 0);
		}
		return player1.hp <= 0 || ai1.hp <= 0;
	}

	public int[] getOrder(int[][] op) {
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

//	public void showStatus(int t) {
//		System.out.printf("Turn : %d\n", t);
//		if (!GameSystem.mode) {
//			System.out.printf("Player = HP: %d, MP: %d ------ AI = HP: %d, MP: %d\n", player1.hp, player1.mp, ai1.hp,
//					ai1.mp);
//			System.out.printf("Player : y %d, x %d ------ AI : y %d, x %d\n", player1.y, player1.x, ai1.y, ai1.x);
//			for (int i = 0; i < 3; i++) {
//				for (int j = 0; j < 4; ++j) {
//					if (i == player1.y && j == player1.x)
//						System.out.print("P ");
//					else if (i == ai1.y && j == ai1.x)
//						System.out.print("A ");
//					else
//						System.out.print("O ");
//				}
//				System.out.println();
//			}
//			System.out.println();
//		} else {
//			System.out.printf("Player 1 = HP: %d, MP: %d ------ AI 1 = HP: %d, MP: %d\n", player1.hp, player1.mp,
//					ai1.hp, ai1.mp);
//			System.out.printf("Player 1 : y %d, x %d ------ AI 1 : y %d, x %d\n", player1.y, player1.x, ai1.y, ai1.x);
//			System.out.printf("Player 2 = HP: %d, MP: %d ------ AI 2 = HP: %d, MP: %d\n\n", player2.hp, player2.mp,
//					ai2.hp, ai2.mp);
//			System.out.printf("Player 2 : y %d, x %d ------ AI 2 : y %d, x %d\n", player2.y, player2.x, ai2.y, ai2.x);
//			for (int i = 0; i < 5; ++i) {
//				for (int j = 0; j < 5; ++j) {
//					if (i == player1.y && j == player1.x && player1.hp > 0)
//						System.out.print("P ");
//					else if (i == player2.y && j == player2.x && player2.hp > 0)
//						System.out.print("W ");
//					else if (i == ai1.y && j == ai1.x && ai1.hp > 0)
//						System.out.print("A ");
//					else if (i == ai2.y && j == ai2.x && ai2.hp > 0)
//						System.out.print("B ");
//					else
//						System.out.print("O ");
//				}
//				System.out.println();
//			}
//		}
//	}

	public Player getPlayer(int va) {
		switch (va) {
		case 0:
			return player1;
		case 1:
			return player2;
		}
		return null;
	}

	public AI getAI(int va) {
		switch (va) {
		case 2:
			return ai1;
		case 3:
			return ai2;
		}
		return null;
	}

}
