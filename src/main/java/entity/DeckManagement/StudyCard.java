package entity.DeckManagement;

import java.util.ArrayList;

public class StudyCard {
    private final String question;
    private final ArrayList<String> answers;
    private final int solutionId;

    public StudyCard(String question, ArrayList<String> answers,  int solutionId) {
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

    /**
     * FIX: Returns the correct answer by indexing into the answers list.
     */
    public String getCorrectAnswer() {
        if (answers != null && solutionId >= 0 && solutionId < answers.size()) {
            return answers.get(solutionId);
        }
        return "ERROR: NO ANSWER DEFINED";
    }
}