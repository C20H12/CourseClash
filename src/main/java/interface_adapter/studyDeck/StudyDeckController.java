package interface_adapter.studyDeck;

import entity.DeckManagement.StudyDeck;
import use_case.registration.signup.SignupInputData;
import use_case.studyDeck.StudyDeckAction;
import use_case.studyDeck.StudyDeckInputBoundary;
import use_case.studyDeck.StudyDeckInputData;

public class StudyDeckController {

    private final StudyDeckInputBoundary studyDeckInteractor;

    public StudyDeckController(StudyDeckInputBoundary studyDeckInteractor) {
        this.studyDeckInteractor = studyDeckInteractor;
    }

    public void execute(StudyDeck studyDeck, StudyDeckAction action) {
        StudyDeckInputData studyDeckInputData = new StudyDeckInputData(studyDeck);
        studyDeckInteractor.execute(studyDeckInputData, action);
    }

}
