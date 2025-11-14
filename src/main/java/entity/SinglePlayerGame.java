// HUZAIFA - Entity for Single Player
package entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class SinglePlayerGame {
    private final User player;
    private final StudyDeck studySet;
    private final List<StudyCard> questions;
    private int score;
    private int correctAnswers;
    private int totalQuestions;
    private double averageResponseTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final int timerPerQuestion; // seconds
    private final boolean shuffleEnabled;
    private final int fixedTimePerQuestion;

    // Constructor
    public SinglePlayerGame(User player, StudyDeck studySet, int timerPerQuestion, boolean shuffleEnabled) {
        this.player = player;
        this.studySet = studySet;
        this.questions = studySet.getCards();
        // this will come from StudySet file
        this.timerPerQuestion = timerPerQuestion;
        this.shuffleEnabled = shuffleEnabled;
        this.totalQuestions = questions.size();
        this.score = 0;
        this.correctAnswers = 0;
        this.fixedTimePerQuestion = 10; //10 seconds per question? not decided
        if (shuffleEnabled) {
               Collections.shuffle(this.questions);
        }
    }

    // Getters
    public int getScore() { return score; }
    public int getCorrectAnswers() { return correctAnswers; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public StudyDeck getStudySet() { return studySet; }
    public User getPlayer() { return player; }

    // Optional  setters
    public void setScore(int score) {
        if (score >= 0) this.score = score;}
    public void setCorrectAnswers(int correctAnswers) {
        if (correctAnswers >= 0 && correctAnswers <= totalQuestions)
            this.correctAnswers = correctAnswers;}
    public void setAverageResponseTime(double averageResponseTime) {
        if (averageResponseTime >= 0) this.averageResponseTime = averageResponseTime;}
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
