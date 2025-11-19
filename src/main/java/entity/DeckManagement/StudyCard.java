// Entity for Study Cards
// Archie
package entity.DeckManagement;

import java.util.ArrayList;

public class StudyCard {
    private final String question;
    private final ArrayList<String> answers;

    // Defensive copying from Clean Architecture
    public StudyCard(String question, ArrayList<String> answers) {
        this.question = question;
        this.answers = new ArrayList<>(answers);
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return new ArrayList<>(answers);
    }
}

// TODO raise issue: deploy quick patches to fix naming discrepancies w/ methods.