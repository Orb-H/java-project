package project.system;

public class ProcessLock {
    private volatile int PlayerTurnCount, AITurnCount;
    private volatile boolean WriteLock;

    public ProcessLock() {
        PlayerTurnCount = AITurnCount = 0;
        WriteLock=false;
    }

    public synchronized void EndPlayerTurn() {
        ++PlayerTurnCount;
    }

    public synchronized void EndAITurn() {
        ++AITurnCount;
    }

    public synchronized void DoWrite(){
        WriteLock=true;
    }

    public synchronized  void EndWrite(){
        WriteLock=false;
    }

    public boolean checkPlayerLock() {
        return PlayerTurnCount > AITurnCount;
    }

    public boolean checkAILock() {
        return PlayerTurnCount < AITurnCount;
    }

    public boolean checkWrite(){
        return WriteLock;
    }
}
