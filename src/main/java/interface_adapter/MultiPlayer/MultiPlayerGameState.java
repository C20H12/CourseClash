package interface_adapter.MultiPlayer;

import java.util.List;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

/**
 * Represents the current state of the multiplayer game view.
 * Stores data required to render the UI, including scores, players, and current card.
 */
public class MultiPlayerGameState {

    private String playerA = "Host";
    private String playerB = "Guest";
    private int scoreA;
    private int scoreB;
    private String message = "Waiting to start";
    private StudyCard currentCard;
    private boolean gameOver;
    private int countdownSeconds;
    private String roundResult = "";

    private List<StudyDeck> availableDecks;

    /**
     * Retrieves the list of study decks available for selection.
     * @return A list of StudyDeck objects.
     */
    public List<StudyDeck> getAvailableDecks() {
        return availableDecks;
    }

    /**
     * Sets the list of study decks available for selection.
     * @param availableDecks The list of StudyDeck objects to store in the state.
     */
    public void setAvailableDecks(List<StudyDeck> availableDecks) {
        this.availableDecks = availableDecks;
    }

    /**
     * Retrieves the name or identifier of Player A (Host).
     * @return The string representing Player A.
     */
    public String getPlayerA() {
        return playerA;
    }

    /**
     * Sets the name or identifier of Player A (Host).
     * @param playerA The string to set as Player A.
     */
    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    /**
     * Retrieves the name or identifier of Player B (Guest).
     * @return The string representing Player B.
     */
    public String getPlayerB() {
        return playerB;
    }

    /**
     * Sets the name or identifier of Player B (Guest).
     * @param playerB The string to set as Player B.
     */
    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    /**
     * Retrieves the current score of Player A.
     * @return The integer score.
     */
    public int getScoreA() {
        return scoreA;
    }

    /**
     * Updates the score of Player A.
     * @param scoreA The new score to set.
     */
    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    /**
     * Retrieves the current score of Player B.
     * @return The integer score.
     */
    public int getScoreB() {
        return scoreB;
    }

    /**
     * Updates the score of Player B.
     * @param scoreB The new score to set.
     */
    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    /**
     * Retrieves the current status or game message.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the current status or game message to be displayed.
     * @param message The message string to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the current study card being played.
     * @return The StudyCard object.
     */
    public StudyCard getCurrentCard() {
        return currentCard;
    }

    /**
     * Sets the current study card for the round.
     * @param currentCard The StudyCard object to set.
     */
    public void setCurrentCard(StudyCard currentCard) {
        this.currentCard = currentCard;
    }

    /**
     * Checks if the game has ended.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game over status.
     * @param gameOver True to mark the game as ended, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Retrieves the remaining seconds for the current turn or state.
     * @return The integer count of seconds.
     */
    public int getCountdownSeconds() {
        return countdownSeconds;
    }

    /**
     * Sets the remaining seconds for the countdown, ensuring it is not negative.
     * @param countdownSeconds The number of seconds to set.
     */
    public void setCountdownSeconds(int countdownSeconds) {
        this.countdownSeconds = Math.max(0, countdownSeconds);
    }

    /**
     * Retrieves the result message of the last round (e.g., Correct/Incorrect).
     * @return The round result string.
     */
    public String getRoundResult() {
        return roundResult;
    }

    /**
     * Sets the result message for the round, defaulting to an empty string if null is provided.
     * @param roundResult The result string to set.
     */
    public void setRoundResult(String roundResult) {
        this.roundResult = roundResult == null ? "" : roundResult;
    }

    /**
     * Returns a string representation of the game state for debugging purposes.
     * @return A string containing all state field values.
     */
    @Override
    public String toString() {
        return "MultiPlayerGameState{" +
                "playerA='" + playerA + '\n' +
                ", playerB='" + playerB + '\n' +
                ", scoreA=" + scoreA +
                ", scoreB=" + scoreB +
                ", message='" + message + '\n' +
                ", currentCard=" + currentCard +
                ", gameOver=" + gameOver +
                ", countdownSeconds=" + countdownSeconds +
                ", roundResult='" + roundResult + '\n' +
                ", availableDecks=" + availableDecks +
                '}';
    }
}