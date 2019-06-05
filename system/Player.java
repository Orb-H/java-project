package project.system;

import project.util.StringUtils;
import project.cards.Card;

import java.util.Random;
import java.util.List;

public class Player {
    int x, y, hp, mp;
    boolean is2v2;
    boolean[] hand = new boolean[24];

    Player(int sy, int sx) {
        for (int i = 0; i < 24; ++i) hand[i] = false;
        is2v2 = false;
        x = sx;
        y = sy;
        hp = mp = 100;
        getCardFromDeck(4);
    }

    Player(int sy, int sx, boolean mod){
        is2v2=mod;
        y=sy;
        x=sx;
        hp=mp=100;
        getCardFromDeck(4);
    }

    public void getCardFromDeck(int cnt) {
        Random gen = new Random();
        int drawCard;
        for (int i = 0; i < cnt; ++i) {
            do {
                drawCard = gen.nextInt(24);
            } while (hand[drawCard]);
            if (drawCard == 4 || drawCard == 6 || drawCard == 8 || drawCard == 16 || drawCard == 18) {
                if (is2v2)
                    hand[drawCard] = true;
                else
                    --i;
            } else
                hand[drawCard] = true;
        }
    }

    public int mulligan() {
        String inputStr = GameSystem.gs.getInput("Submit cards' number to change with ascending order : ");
        int[] removedCard = StringUtils.Split2Int(inputStr, " ");
        //Verifying removed card list
        for (int i = 0; i < removedCard.length - 1; ++i)
            for (int j = i + 1; j < removedCard.length; ++j)
                if (removedCard[i] == removedCard[j]) {
                    System.out.println("You cannot remove the same card more than once");
                    return 4;
                }
        for (int i = 0; i < removedCard.length; ++i) {
            if (removedCard[i] == 0) {
                System.out.println("You cannot remove \"0: The Fool card\" : Player Mulligan Error");
                return 2;
            }
            if (removedCard[i] > 24 || removedCard[i] < 0) {
                System.out.println("Card number should be in bound of 0-23 : Player Mulligan Error");
                return 3;
            }
            if (!hand[removedCard[i]]) {
                System.out.printf("You don't have %d card in your hand : Player Mulligan Error\n", removedCard[i]);
                return 1;
            }
        }

        getCardFromDeck(removedCard.length);
        for (int i = 0; i < removedCard.length; ++i)
            hand[removedCard[i]] = false;
        return 0;
    }

    public void show(List<Card> cards) {
        System.out.println("Your Hand is");
        for (int i = 0; i < 24; ++i) {
            if (hand[i]) {
                System.out.println(cards.get(i + 6).getName());
            }
        }
    }
}