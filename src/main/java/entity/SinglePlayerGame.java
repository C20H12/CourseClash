// HUZAIFA - Entity for Single Player

package entity;

// Standard library imports
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

// Project imports
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;






public class SinglePlayerGame {
    private final User player;
    private final StudyDeck deck;
    private int score;
    private int correctAnswers;
    private final int totalQuestions;
    private double averageResponseTime;
    private LocalDateTime startTime;
    // private final int fixedTimePerQuestion;

    // Constructor
    public SinglePlayerGame(User player, StudyDeck studyDeck, boolean shuffleEnabled) {
        this.player = player;
        this.deck = studyDeck;
        List<StudyCard> questions = studyDeck.getDeck();
        this.totalQuestions = questions.size();
        this.score = 0;
        this.correctAnswers = 0;
        if (shuffleEnabled) {
            Collections.shuffle(questions);
        }
    }

    // Getters
    public int getScore() { return score;
    }

    public int getCorrectAnswers() { return correctAnswers;
    }

    public double getAverageResponseTime() { return averageResponseTime;
    }

    public StudyDeck getDeck() { return deck;
    }

    public User getPlayer() { return player;
    }


    // Optional  setters
    public void startGame() {
        this.startTime = LocalDateTime.now();
    }

    public void setScore(int score) {
        if (score >= 0) this.score = score;
    }

    public void endGame() {
        LocalDateTime endTime = LocalDateTime.now();

        if (startTime != null && totalQuestions > 0) {
            long durationSeconds =
                    java.time.Duration.between(startTime, endTime).toSeconds();
            this.averageResponseTime = (double) durationSeconds / totalQuestions;
        } else {
            this.averageResponseTime = 0.0;
        }
    }

    public StudyDeck getStudyDeck() {
        return deck;
    }

    public void incrementScoreCorrect() {
        this.score += 5;
        this.correctAnswers += 1;
    }

    }

