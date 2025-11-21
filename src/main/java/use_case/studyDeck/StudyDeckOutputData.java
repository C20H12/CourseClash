package use_case.studyDeck;

import java.util.List;

import entity.DeckManagement.StudyDeck;

public class StudyDeckOutputData {
    private List<StudyDeck> decks;

    public StudyDeckOutputData(List<StudyDeck> deck) {
        this.decks = deck;
    }

    public List<StudyDeck> getDecks() {
        return this.decks;
    }
}
