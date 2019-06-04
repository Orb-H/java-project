package project.AI;

import project.system.Player;

import java.util.concurrent.Callable;

public class AIProcess implements  Callable<int[]>{
    Player ai;
    boolean[] predictedDeck=new boolean[24];
    public AIProcess(Player p){
        ai=p;
        for(int i=0;i<24;++i) predictedDeck[i]=false;
    }
    public AIProcess(Player p, boolean[] arr_b){
        ai=p;
        predictedDeck=arr_b;
    }
    public int[] call(){
        return new int[] {1, 2, 3};
    }
}
