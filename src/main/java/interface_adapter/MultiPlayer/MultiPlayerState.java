package interface_adapter.MultiPlayer;

import entity.StudyCard;

public class MultiPlayerState {
    private String playerA = "";
    private String playerB = "";
    private String currentTurnUser = "";
    private StudyCard currentCard = null; // Missing field
    private String message = "";
    private int scoreA = 0;
    private int scoreB = 0;

    public MultiPlayerState(MultiPlayerState copy) {
        playerA = copy.playerA;
        playerB = copy.playerB;
        currentTurnUser = copy.currentTurnUser;
        currentCard = copy.currentCard;
        message = copy.message;
        scoreA = copy.scoreA;
        scoreB = copy.scoreB;
    }

    public MultiPlayerState() {}

    // --- The Missing Setters ---
    public String getPlayerA() { return playerA; }
    public void setPlayerA(String playerA) { this.playerA = playerA; }

    public String getPlayerB() { return playerB; }
    public void setPlayerB(String playerB) { this.playerB = playerB; }

    public String getCurrentTurnUser() { return currentTurnUser; }
    public void setCurrentTurnUser(String currentTurnUser) { this.currentTurnUser = currentTurnUser; }

    public StudyCard getCurrentCard() { return currentCard; }
    public void setCurrentCard(StudyCard currentCard) { this.currentCard = currentCard; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getScoreA() { return scoreA; }
    public void setScoreA(int scoreA) { this.scoreA = scoreA; }

    public int getScoreB() { return scoreB; }
    public void setScoreB(int scoreB) { this.scoreB = scoreB; }
}