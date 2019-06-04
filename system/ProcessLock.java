package project.system;

public class ProcessLock {
    private volatile int PlayerTurnCount, AITurnCount;

    public ProcessLock() {
        PlayerTurnCount = AITurnCount = 0;
    }

    public synchronized void EndPlayerTurn() {
        ++PlayerTurnCount;
    }

    public synchronized void EndAITurn() {
        ++AITurnCount;
    }

    public boolean checkPlayerLock() {
        return PlayerTurnCount > AITurnCount;
    }

    public boolean checkAILock() {
        return PlayerTurnCount < AITurnCount;
    }
}
