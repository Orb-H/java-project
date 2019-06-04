package project.system;

import java.util.Random;

import java.util.concurrent.Callable;

public class AIProcess implements  Callable<int[]>{
    ProcessLock Lock;
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
        //busy waiting
        while(Lock.checkAILock());
        Random gen=new Random();
        int a=gen.nextInt(24), b=gen.nextInt(24), c=gen.nextInt(24);
        Lock.EndAITurn();
        return new int[] {a, b, c};
    }
}
