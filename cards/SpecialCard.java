package project.cards;

public class SpecialCard extends Card {
    public SpecialCard(String name, int num, String[] ascii, String description, int deal, int cost){
        super(CardType.SPECIAL, name, num, ascii, description, deal, cost);
    }
    public boolean inBound(int dy, int dx){
        return false;
    }
    public void act(){}
}
