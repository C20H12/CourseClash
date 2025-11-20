package use_case.MultiPlayer.submit_answer;

import entity.StudyCard;

public class SubmitAnswerOutputData {
    private final int player1Score;
    private final int player2Score;
    private final String currentTurnUser;
    private final StudyCard nextCard;
    private final boolean isCorrect;
    private final String correctAnswer;
    private final boolean isGameOver; // <--- NEW FIELD

    public SubmitAnswerOutputData(int player1Score, int player2Score, String currentTurnUser,
                                  StudyCard nextCard, boolean isCorrect, String correctAnswer,
                                  boolean isGameOver) { // <--- Update Constructor
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.currentTurnUser = currentTurnUser;
        this.nextCard = nextCard;
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
        this.isGameOver = isGameOver;
    }

    // ... existing getters ...
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }
    public String getCurrentTurnUser() { return currentTurnUser; }
    public StudyCard getNextCard() { return nextCard; }
    public boolean isCorrect() { return isCorrect; }
    public String getCorrectAnswer() { return correctAnswer; }

    public boolean isGameOver() { return isGameOver; } // <--- New Getter
}