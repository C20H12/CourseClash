package interface_adapter.studyDeck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import use_case.studyDeck.StudyDeckAction;
import use_case.studyDeck.StudyDeckInputBoundary;
import use_case.studyDeck.StudyDeckInputData;

public class StudyDeckController {

    private final StudyDeckInputBoundary studyDeckInteractor;

    public StudyDeckController(StudyDeckInputBoundary studyDeckInteractor) {
        this.studyDeckInteractor = studyDeckInteractor;
    }

    public void execute(StudyDeck studyDeck, StudyDeckAction action) {
        studyDeckInteractor.unloadDeck();
        StudyDeckInputData studyDeckInputData = new StudyDeckInputData(studyDeck);
        studyDeckInteractor.execute(studyDeckInputData, action);
    }

    public void initEdit(StudyDeck deck) {
        studyDeckInteractor.loadDeck(deck.getTitle());
    }

    public StudyCard getFirstCardInCurrentEditedDeck() {
        StudyDeck d = studyDeckInteractor.getCurrentDeck();
        if (d.isEmpty()) {
            return null;
        }
        return d.getDeck().get(0);
    }

    /**
     * returns the index of the added card
     */
    public int addNewCardToEdit() {
        studyDeckInteractor.addCardToCurrentDeck(
            "", 
            new ArrayList<>(Arrays.asList("", "", "", "")), 
            0);
        return studyDeckInteractor.getCurrentDeck().getCardCount() - 1;        
    }

    public StudyCard getCardByIndex(int index) {
        return studyDeckInteractor.getCurrentDeck().getDeck().get(index);
    }

    public List<StudyCard> getAllCards() {
        return studyDeckInteractor.getCurrentDeck().getDeck();
    }

    public StudyDeck getCurrentDeck() {
        return studyDeckInteractor.getCurrentDeck();
    }

    public void updateCardInCurrentDeck(int cardIndex, String question, ArrayList<String> answers, int correctAnswerIndex) {
        ArrayList<String> filtered = answers.stream()
            .filter(ans -> ans != null && !ans.isBlank())
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        studyDeckInteractor.updateCardInCurrentDeck(cardIndex, question, filtered, correctAnswerIndex);
    }

    public void removeCardFromCurrentDeck(int cardIndex) {
        studyDeckInteractor.removeCardFromCurrentDeck(cardIndex);
    }
}
