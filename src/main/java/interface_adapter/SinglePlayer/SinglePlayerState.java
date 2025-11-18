package interface_adapter.SinglePlayer;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayerState {

    private String questionText = "";
    private List<String> options = new ArrayList<>();
    private int currentIndex = 0;
    private int total = 0;
    private int score = 0;
    private double accuracy = 0.0;
    private double avgResponseTime = 0.0;
    private boolean gameOver = false;
    private String message = "";
    private String error = "";

    public SinglePlayerState() {}

    // --- GETTERS ---
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }
    public int getCurrentIndex() { return currentIndex; }
    public int getTotal() { return total; }
    public int getScore() { return score; }
    public double getAccuracy() { return accuracy; }
    public double getAvgResponseTime() { return avgResponseTime; }
    public boolean isGameOver() { return gameOver; }
    public String getMessage() { return message; }
    public String getError() { return error; }

    // --- SETTERS ---
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setCurrentIndex(int index) { this.currentIndex = index; }
    public void setTotal(int total) { this.total = total; }
    public void setScore(int score) { this.score = score; }
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
    public void setAvgResponseTime(double time) { this.avgResponseTime = time; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public void setMessage(String message) { this.message = message; }
    public void setError(String error) { this.error = error; }
    // For final results
    public void setFinalScore(int score) { this.score = score; }
    public void setFinalAccuracy(double accuracy) { this.accuracy = accuracy; }
    public void setFinalAvgTime(double time) { this.avgResponseTime = time; }
}
