package entity;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import java.util.List;

public class MultiPlayerGame {
    private final User playerA;
    private final User playerB;
    private final StudyDeck deck;

    private int currentCardIndex;
    private User currentTurn;
    private int currentCardAnswersSubmitted;
    private boolean isFinished;
    private int scoreA;
    private int scoreB;

    public MultiPlayerGame(User playerA, User playerB, StudyDeck deck) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.deck = deck;
        this.currentCardIndex = 0;
        this.currentTurn = playerA;
        this.isFinished = false;
        this.scoreA = 0;
        this.scoreB = 0;
        this.currentCardAnswersSubmitted = 0;
    }


    public boolean recordAnswerAndIsReadyToAdvance() {
        this.currentCardAnswersSubmitted++;
        return this.currentCardAnswersSubmitted >= 2;
    }

    public void advanceCardAndResetCounter() {
        this.currentCardIndex++;
        this.currentCardAnswersSubmitted = 0;
    }


    public void switchTurn() {
        if (currentTurn.getUserName().equals(playerA.getUserName())) {
            currentTurn = playerB;
        } else {
            currentTurn = playerA;
        }
    }

    public void incrementScoreFor(String username) {
        if (username.equals(playerA.getUserName())) {
            scoreA++;
        } else if (username.equals(playerB.getUserName())) {
            scoreB++;
        }
    }

    public StudyCard getCurrentCard() {
        List<StudyCard> cards = deck.getDeck();
        if (currentCardIndex < cards.size()) {
            return cards.get(currentCardIndex);
        }
        return null;
    }

    public boolean isGameOver() {
        return currentCardIndex >= deck.getDeck().size();
    }


    public User getPlayerA() { return playerA; }
    public User getPlayerB() { return playerB; }
    public StudyDeck getDeck() { return deck; }

    public User getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(User currentTurn) { this.currentTurn = currentTurn; }

    public int getScoreA() { return scoreA; }
    public void setScoreA(int scoreA) { this.scoreA = scoreA; }

    public int getScoreB() { return scoreB; }
    public void setScoreB(int scoreB) { this.scoreB = scoreB; }

    public int getCardIndex() { return currentCardIndex; }
    public void setCardIndex(int index) { this.currentCardIndex = index; }

    public int getCurrentCardAnswersSubmitted() { return currentCardAnswersSubmitted; }
    public void setCurrentCardAnswersSubmitted(int count) { this.currentCardAnswersSubmitted = count; }
}