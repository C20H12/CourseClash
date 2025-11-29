//Mahir

package interface_adapter.MultiPlayer;

import entity.DeckManagement.StudyCard;

/**
 * Represents the internal state of a multiplayer game session.
 * Stores information about players, scores, turns, and the current card in play.
 */
public class MultiPlayerState {
    private String playerA = "";
    private String playerB = "";
    private int scoreA;
    private int scoreB;
    private String currentTurnUser = "";
    private StudyCard currentCard;
    private String message = "";
    private boolean isGameOver;

    /**
     * Copy constructor to create a new state instance from an existing one.
     * @param copy The MultiPlayerState object to copy data from.
     */
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

    /**
     * Default constructor initializing an empty state.
     */
    public MultiPlayerState() {

    }

    /**
     * Retrieves the username or identifier of Player A.
     * @return The string representing Player A.
     */
    public String getPlayerA() {
        return playerA;
    }

    /**
     * Retrieves the username or identifier of Player B.
     * @return The string representing Player B.
     */
    public String getPlayerB() {
        return playerB;
    }

    /**
     * Retrieves the current score of Player A.
     * @return The integer score.
     */
    public int getScoreA() {
        return scoreA;
    }

    /**
     * Retrieves the current score of Player B.
     * @return The integer score.
     */
    public int getScoreB() {
        return scoreB;
    }

    /**
     * Retrieves the identifier of the user whose turn it currently is.
     * @return The username of the current active player.
     */
    public String getCurrentTurnUser() {
        return currentTurnUser;
    }

    /**
     * Retrieves the study card currently being played.
     * @return The StudyCard object.
     */
    public StudyCard getCurrentCard() {
        return currentCard;
    }

    /**
     * Retrieves the current system or game status message.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks if the game session has concluded.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Sets the username or identifier for Player A.
     * @param playerA The string to set as Player A.
     */
    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    /**
     * Sets the username or identifier for Player B.
     * @param playerB The string to set as Player B.
     */
    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    /**
     * Updates the score for Player A.
     * @param scoreA The new score to set.
     */
    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    /**
     * Updates the score for Player B.
     * @param scoreB The new score to set.
     */
    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    /**
     * Sets the user whose turn it is currently.
     * @param currentTurnUser The username of the active player.
     */
    public void setCurrentTurnUser(String currentTurnUser) {
        this.currentTurnUser = currentTurnUser;
    }

    /**
     * Sets the study card for the current round.
     * @param currentCard The StudyCard object to be used.
     */
    public void setCurrentCard(StudyCard currentCard) {
        this.currentCard = currentCard;
    }

    /**
     * Sets a status message for the game state (e.g., error or info).
     * @param message The message string to store.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Updates the game completion status.
     * @param isGameOver True to mark the game as ended, false otherwise.
     */
    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }
}