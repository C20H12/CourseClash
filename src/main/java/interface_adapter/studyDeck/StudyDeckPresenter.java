package interface_adapter.studyDeck;

import use_case.studyDeck.StudyDeckOutputBoundary;
import use_case.studyDeck.StudyDeckOutputData;

public class StudyDeckPresenter implements StudyDeckOutputBoundary {
    private final StudyDeckViewModel studyDeckViewModel;

    public StudyDeckPresenter(StudyDeckViewModel studyDeckViewModel) {
        this.studyDeckViewModel = studyDeckViewModel;
    }

    @Override
    public void prepareView(StudyDeckOutputData studyDeckOutputData) {
        studyDeckViewModel.setState(studyDeckOutputData.getDecks());
        studyDeckViewModel.firePropertyChange();
    }
}
