package interface_adapter.SinglePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.DeckManagement.StudyDeck;

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

    private List<StudyDeck> decksList;

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
    public List<StudyDeck> getDecksList() { return decksList; }

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
    public void setDecksList(List<StudyDeck> decksList) { this.decksList = decksList; }
    public boolean isError() {
        return error != null && !error.isEmpty();
    }
}
