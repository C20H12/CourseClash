// Entity for Study Cards
// Archie

package entity.DeckManagement;

import java.util.ArrayList;

public class StudyCard {
    private final String question;
    private final ArrayList<String> options;
    private final int answerId;

    // Defensive copying pattern from Clean Architecture
    public StudyCard(String question, ArrayList<String> options, int answerId) {
        this.question = question;
        this.options = new ArrayList<>(options);
        this.answerId = answerId;
    }

    // Helper method to get question title.
    // @param None
    // @return String
    public String getQuestionTitle() {
        return question;
    }

    // Helper method to get card options.
    // @param None
    // @return ArrayList<String>
    public ArrayList<String> getOptions() {
        return new ArrayList<>(options);
    }

    // Helper method to get answer ID.
    // @param None
    // @return int
    public int getAnswerId() {
        return answerId;
    }
}
