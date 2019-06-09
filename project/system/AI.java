package project.system;

public class AI extends Player {

	int stage;
	boolean[] predictedHand1 = new boolean[24];
	boolean[] predictedHand2 = new boolean[24];

	public AI(int sy, int sx) {
		super(sy, sx);
		stage = 1;
	}

	public AI(int sy, int sx, int st) {
		super(sy, sx);
		for (int i = 0; i < 24; ++i)
			getPredictedHand2()[i] = getPredictedHand1()[i] = false;
		stage = st;
	}

	public void mulligan(int[] removedCard) {
		int t = 0;
		for (int i = 0; i < removedCard.length; ++i) {
			if (getHand()[removedCard[i]])
				++t;
			else
				getHand()[removedCard[i]] = true;
		}
		getCardFromDeck(t);
		for (int i = 0; i < removedCard.length; ++i)
			getHand()[removedCard[i]] = false;
	}

	public boolean[] getPredictedHand1() {
		return predictedHand1;
	}

	public boolean[] getPredictedHand2() {
		return predictedHand2;
	}

}
