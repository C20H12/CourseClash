package use_case.studyDeck;

import entity.DeckManagement.StudyDeck;

public class StudyDeckOutputData {
    private StudyDeck deck;

    public StudyDeckOutputData(StudyDeck deck) {
        this.deck = deck;
    }

    public StudyDeck getDeck() {
        return this.deck;
    }
}
