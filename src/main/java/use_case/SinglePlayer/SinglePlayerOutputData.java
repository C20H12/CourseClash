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
                                  int score, double accuracy, double avgResponseTime, boolean gameOver, String message) {
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
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }
    public int getCurrentIndex() { return currentIndex; }
    public int getTotal() { return total; }
    public int getScore() { return score; }
    public double getAccuracy() { return accuracy; }
    public double getAvgResponseTime() { return avgResponseTime; }
    public boolean isGameOver() { return gameOver; }
    public String getMessage() { return message; }
}