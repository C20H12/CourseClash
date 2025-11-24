package use_case.studyDeck;

import entity.DeckManagement.StudyDeck;


public class StudyDeckInputData {
    private StudyDeck studyDeck;

    public StudyDeckInputData(StudyDeck studyDeck) {
        this.studyDeck = studyDeck;
    }

    public StudyDeck getStudyDeck() {
        return studyDeck;
    }
}
