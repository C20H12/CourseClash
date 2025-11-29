package use_case.MultiPlayer;

import entity.DeckManagement.StudyCard;

/**
 * Data passed from the interactor to the presenter so the view model can be updated.
 */
public class MultiPlayerOutputData {

    private String playerA;
    private String playerB;
    private int scoreA;
    private int scoreB;
    private String message;
    private StudyCard currentCard;
    private String roundResult;
    private boolean gameOver;

    /**
     * Retrieves the username or identifier for Player A.
     * @return The string representing Player A.
     */
    public String getPlayerA() {
        return playerA;
    }

    /**
     * Sets the username or identifier for Player A.
     * @param playerA The string to set as Player A.
     */
    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    /**
     * Retrieves the username or identifier for Player B.
     * @return The string representing Player B.
     */
    public String getPlayerB() {
        return playerB;
    }

    /**
     * Sets the username or identifier for Player B.
     * @param playerB The string to set as Player B.
     */
    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    /**
     * Retrieves the current score for Player A.
     * @return The integer score of Player A.
     */
    public int getScoreA() {
        return scoreA;
    }

    /**
     * Updates the current score for Player A.
     * @param scoreA The new score to set.
     */
    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    /**
     * Retrieves the current score for Player B.
     * @return The integer score of Player B.
     */
    public int getScoreB() {
        return scoreB;
    }

    /**
     * Updates the current score for Player B.
     * @param scoreB The new score to set.
     */
    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    /**
     * Retrieves the system or game status message.
     * @return A string containing the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets a message regarding the game state or errors.
     * @param message The message string to display or store.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the StudyCard currently being played in this round.
     * @return The current StudyCard object.
     */
    public StudyCard getCurrentCard() {
        return currentCard;
    }

    /**
     * Sets the StudyCard for the current round.
     * @param currentCard The StudyCard object to be used.
     */
    public void setCurrentCard(StudyCard currentCard) {
        this.currentCard = currentCard;
    }

    /**
     * Retrieves the result of the specific round (e.g., who won or if the answer was correct).
     * @return A string describing the round outcome.
     */
    public String getRoundResult() {
        return roundResult;
    }

    /**
     * Sets the result description for the specific round.
     * @param roundResult The string describing the round outcome.
     */
    public void setRoundResult(String roundResult) {
        this.roundResult = roundResult;
    }

    /**
     * Checks if the game session has concluded.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Updates the game completion status.
     * @param gameOver True to mark the game as ended, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
