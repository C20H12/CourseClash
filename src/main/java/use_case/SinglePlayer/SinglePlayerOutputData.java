//huzaifa - (question text, options, index, totals, score, accuracy, avgTime, isGameOver, message)

package use_case.SinglePlayer;

import java.util.List;

public class SinglePlayerOutputData {
    private final String questionText;
    private final List<String> options;
    private final int currentIndex;
    private final int total;
    private final int score;
    private final double accuracy;
    private final double avgResponseTime;
    private final boolean gameOver;
    private final String message;

    public SinglePlayerOutputData(String questionText, List<String> options, int currentIndex, int total,
                                  int score, double accuracy, double avgResponseTime, int correctAnswers, boolean gameOver, String message) {
        this.questionText = questionText;
        this.options = options;
        this.currentIndex = currentIndex;
        this.total = total;
        this.score = score;
        this.accuracy = accuracy;
        this.avgResponseTime = avgResponseTime;
        this.gameOver = gameOver;
        this.message = message;
    }

    /**
     * Returns the text of the current question.
     *
     * @return the current question text
     */
    public String getQuestionText() { return questionText;
    }

    /**
     * Returns the available answer options for the current question.
     *
     * @return a list of answer choices
     */
    public List<String> getOptions() { return options;}

    /**
     * Returns the index of the current question within the deck.
     *
     * @return the zero-based question index
     */
    public int getCurrentIndex() { return currentIndex; }

    /**
     * Returns the total number of questions in the game session.
     *
     * @return the total question count
     */
    public int getTotal() { return total; }

    /**
     * Returns the player's current score.
     *
     * @return the accumulated score
     */
    public int getScore() { return score; }

    /**
     * Returns the player's answer accuracy as a percentage value.
     *
     * @return the accuracy score
     */
    public double getAccuracy() { return accuracy; }

    /**
     * Returns the player's answer accuracy as a percentage value.
     *
     * @return the accuracy score
     */
    public double getAvgResponseTime() { return avgResponseTime; }

    /**
     * Indicates whether the game session has ended.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() { return gameOver; }

    /**
     * Returns the status or feedback message associated with the game state.
     *
     * @return a descriptive status message
     */
    public String getMessage() { return message; }
}