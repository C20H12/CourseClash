package interface_adapter.studyDeck;

import interface_adapter.ViewModel;

public class StudyDeckViewModel extends ViewModel<StudyDeckViewModel> {

    public StudyDeckViewModel() {
        super("browse study set");
        setState(new StudyDeckViewModel());
    }

}
