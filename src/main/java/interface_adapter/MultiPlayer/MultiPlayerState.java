//Mahir
package interface_adapter.MultiPlayer;

import entity.DeckManagement.StudyCard;

public class MultiPlayerState {
    private String playerA = "";
    private String playerB = "";
    private int scoreA = 0;
    private int scoreB = 0;
    private String currentTurnUser = "";
    private StudyCard currentCard = null;
    private String message = "";
    private boolean isGameOver = false;

    // Copy Constructor
    public MultiPlayerState(MultiPlayerState copy) {
        playerA = copy.playerA;
        playerB = copy.playerB;
        scoreA = copy.scoreA;
        scoreB = copy.scoreB;
        currentTurnUser = copy.currentTurnUser;
        currentCard = copy.currentCard;
        message = copy.message;
        isGameOver = copy.isGameOver;
    }

    public MultiPlayerState() {}

    public String getPlayerA() { return playerA; }
    public String getPlayerB() { return playerB; }
    public int getScoreA() { return scoreA; }
    public int getScoreB() { return scoreB; }
    public String getCurrentTurnUser() { return currentTurnUser; }
    public StudyCard getCurrentCard() { return currentCard; }
    public String getMessage() { return message; }
    public boolean isGameOver() { return isGameOver; }

    public void setPlayerA(String playerA) { this.playerA = playerA; }
    public void setPlayerB(String playerB) { this.playerB = playerB; }
    public void setScoreA(int scoreA) { this.scoreA = scoreA; }
    public void setScoreB(int scoreB) { this.scoreB = scoreB; }
    public void setCurrentTurnUser(String currentTurnUser) { this.currentTurnUser = currentTurnUser; }
    public void setCurrentCard(StudyCard currentCard) { this.currentCard = currentCard; }
    public void setMessage(String message) { this.message = message; }
    public void setIsGameOver(boolean isGameOver) { this.isGameOver = isGameOver; }
}