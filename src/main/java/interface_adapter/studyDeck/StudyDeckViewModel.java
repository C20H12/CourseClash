package interface_adapter.studyDeck;

import java.util.List;

import entity.DeckManagement.StudyDeck;
import interface_adapter.ViewModel;

public class StudyDeckViewModel extends ViewModel<List<StudyDeck>> {

    public StudyDeckViewModel() {
        super("browse study set");
    }

}
