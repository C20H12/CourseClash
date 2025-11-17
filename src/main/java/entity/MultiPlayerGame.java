//Entity for multiplayer gameplay
//Mahir
package entity;

import entity.DeckManagement.StudyDeck;

public class MultiPlayerGame {
    private final User playerA;
    private final User playerB;
    private final StudyDeck deck;

    private int currentQuestionIndex;
    private User currentTurn;
    private boolean isFinished;

    private int scoreA;
    private int scoreB;

    public MultiPlayerGame(User playerA, User playerB, StudyDeck deck) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.deck = deck;
        this.currentQuestionIndex = 0;
        this.currentTurn = playerA;
        this.isFinished = false;
        this.scoreA = 0;
        this.scoreB = 0;
    }


    public User getPlayerA() {
        return playerA;
    }

    public User getPlayerB() {
        return playerB;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int index) {
        this.currentQuestionIndex = index;
    }

    public StudyDeck getDeck() {return deck;}

    public User getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(User currentTurn) {
        this.currentTurn = currentTurn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }
}
