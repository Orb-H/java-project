package project.system;

public class AI extends Player {
    int y_player1, x_player1;
    int y_player2, x_player2;
    int stage;
    boolean[] predictedHand= new boolean[24];

    public AI(int sy, int sx){
        super(sy, sx);
        y_player1=1;
        x_player1=0;
        stage=1;
    }

    public AI(int sy, int sx, int st){
        super(sy, sx);
        y_player1=1;
        x_player1=0;
        stage=st;
    }

    public AI(int sy, int sx, int st, boolean[] preHand){
        super(sy, sx);
        y_player1=1;
        x_player1=3;
        stage=st;
        predictedHand=preHand;
    }

    public AI(int sy, int sx, boolean mod){
        super(sy, sx, mod);
        y_player1=1;
        x_player1=0;
        y_player2=3;
        x_player2=0;
    }
    public AI(int sy, int sx, boolean mod, boolean[] preHand){
        super(sy, sx, mod);
        y_player1=1;
        x_player1=0;
        y_player2=3;
        x_player2=0;
        predictedHand=preHand;
    }

    public void mulligan(int[] removedCard) {
        int t = 0;
        for (int i = 0; i < removedCard.length; ++i)
            if (hand[removedCard[i]]) ++t;
        getCardFromDeck(t);
        for (int i = 0; i < removedCard.length; ++i)
            hand[removedCard[i]] = false;
    }

}
