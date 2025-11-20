package entity; // Or entity.DeckManagement if that's your package

import java.util.ArrayList;

public class StudyCard {
    private final String question;
    private final ArrayList<String> answers;
    private final int solutionId;

    public StudyCard(String question, ArrayList<String> answers, int solutionId) {
        this.question = question;
        this.answers = new ArrayList<>(answers);
        this.solutionId = solutionId;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return new ArrayList<>(answers);
    }

    public int getSolutionId() {
        return solutionId;
    }

    // --- NEW HELPER METHOD ---
    // This is what SubmitAnswerInteractor needs!
    public String getCorrectAnswer() {
        if (answers != null && solutionId >= 0 && solutionId < answers.size()) {
            return answers.get(solutionId);
        }
        return ""; // Fallback if data is invalid
    }
}

