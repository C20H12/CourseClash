// Comprehensive test suite for StudyDeckInteractor. Tests all public methods and edge cases.
// Archie

package frameworks_and_drivers.DataAccess.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.studyDeck.StudyDeckAction;
import use_case.studyDeck.StudyDeckDataAccessInterface;
import use_case.studyDeck.StudyDeckInputData;
import use_case.studyDeck.StudyDeckInteractor;
import use_case.studyDeck.StudyDeckOutputBoundary;
import use_case.studyDeck.StudyDeckOutputData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudyDeckInteractorTest {

    private InMemoryStudyDeckGateway gateway;
    private CapturingPresenter presenter;
    private StudyDeckInteractor interactor;

    @BeforeEach
    void setUp() {
        this.gateway = new InMemoryStudyDeckGateway();
        this.presenter = new CapturingPresenter();
        this.interactor = new StudyDeckInteractor(gateway, presenter);
    }

    @Test
    @DisplayName("Constructor initializes dependencies correctly")
    void testConstructor() {
        assertNotNull(gateway);
        assertNotNull(presenter);
        assertNotNull(interactor);
    }

    @Test
    @DisplayName("execute ADD_DECK saves deck and notifies presenter")
    void executeAddDeck_savesDeckAndNotifiesPresenter() {
        StudyDeck deck = createSampleDeck("Biology");
        interactor.execute(new StudyDeckInputData(deck), StudyDeckAction.ADD_DECK);

        StudyDeck stored = gateway.getDeck("Biology");
        assertNotNull(stored);
        assertEquals(deck.getTitle(), stored.getTitle());
        assertEquals(deck.getDescription(), stored.getDescription());
        assertEquals(deck.getDeck(), stored.getDeck());

        assertNotNull(presenter.lastOutput);
        assertEquals(1, presenter.lastOutput.getDecks().size());
        assertEquals("Biology", presenter.lastOutput.getDecks().get(0).getTitle());
    }

    @Test
    @DisplayName("execute REMOVE_DECK deletes deck and updates presenter")
    void executeRemoveDeck_deletesDeckAndUpdatesPresenter() {
        StudyDeck deck = createSampleDeck("Physics");
        gateway.saveDeck(deck);

        interactor.execute(new StudyDeckInputData(deck), StudyDeckAction.REMOVE_DECK);

        assertNull(gateway.getDeck("Physics"));
        assertNotNull(presenter.lastOutput);
        assertTrue(presenter.lastOutput.getDecks().isEmpty());
    }

    @Test
    @DisplayName("execute EDIT_DECK updates deck and notifies presenter")
    void executeEditDeck_updatesDeckAndNotifiesPresenter() {
        StudyDeck originalDeck = createSampleDeck("Chemistry");
        gateway.saveDeck(originalDeck);

        // Modify the deck
        ArrayList<String> newAnswers = new ArrayList<>(Arrays.asList("New A", "New B"));
        ArrayList<StudyCard> modifiedCards = new ArrayList<>();
        modifiedCards.add(new StudyCard("New Question", newAnswers, 0));
        StudyDeck modifiedDeck = new StudyDeck("Chemistry", "Updated Description", modifiedCards, originalDeck.getId());

        interactor.execute(new StudyDeckInputData(modifiedDeck), StudyDeckAction.EDIT_DECK);

        StudyDeck updated = gateway.getDeck("Chemistry");
        assertNotNull(updated);
        assertEquals("Updated Description", updated.getDescription());
        assertEquals("New Question", updated.getDeck().get(0).getQuestionTitle());

        assertNotNull(presenter.lastOutput);
        assertEquals(1, presenter.lastOutput.getDecks().size());
    }

    @Test
    @DisplayName("execute LOAD_ALL retrieves all decks and notifies presenter")
    void executeLoadAll_retrievesAllDecksAndNotifiesPresenter() {
        StudyDeck deck1 = createSampleDeck("Deck 1");
        StudyDeck deck2 = createSampleDeck("Deck 2");
        gateway.saveDeck(deck1);
        gateway.saveDeck(deck2);

        interactor.execute(new StudyDeckInputData(null), StudyDeckAction.LOAD_ALL);

        assertNotNull(presenter.lastOutput);
        assertEquals(2, presenter.lastOutput.getDecks().size());
        assertTrue(presenter.lastOutput.getDecks().stream()
                .anyMatch(deck -> deck.getTitle().equals("Deck 1")));
        assertTrue(presenter.lastOutput.getDecks().stream()
                .anyMatch(deck -> deck.getTitle().equals("Deck 2")));
    }

    @Test
    @DisplayName("createNewDeck creates empty deck with specified title and description")
    void createNewDeck_createsEmptyDeckWithTitleAndDescription() {
        interactor.createNewDeck("Math", "Calculus review");

        StudyDeck currentDeck = interactor.getCurrentDeck();
        assertNotNull(currentDeck);
        assertEquals("Math", currentDeck.getTitle());
        assertEquals("Calculus review", currentDeck.getDescription());
        assertTrue(currentDeck.getDeck().isEmpty());
        assertNotNull(currentDeck.getId());
    }

    @Test
    @DisplayName("addCardToCurrentDeck adds card when current deck exists")
    void addCardToCurrentDeck_addsCardWhenCurrentDeckExists() {
        interactor.createNewDeck("History", "WWII");

        ArrayList<String> answers = createSampleAnswers("Answer");
        interactor.addCardToCurrentDeck("When did WWII start?", answers, 1);

        StudyDeck currentDeck = interactor.getCurrentDeck();
        assertNotNull(currentDeck);
        assertEquals(1, currentDeck.getDeck().size());
        assertEquals("When did WWII start?", currentDeck.getDeck().get(0).getQuestionTitle());
        assertEquals(answers, currentDeck.getDeck().get(0).getOptions());
        assertEquals(1, currentDeck.getDeck().get(0).getAnswerId());
    }

    @Test
    @DisplayName("addCardToCurrentDeck does nothing when current deck is null")
    void addCardToCurrentDeck_doesNothingWhenCurrentDeckIsNull() {
        ArrayList<String> answers = createSampleAnswers("Answer");
        interactor.addCardToCurrentDeck("Question", answers, 0);

        assertNull(interactor.getCurrentDeck());
    }

    @Test
    @DisplayName("removeCardFromCurrentDeck removes card at valid index")
    void removeCardFromCurrentDeck_removesCardAtValidIndex() {
        interactor.createNewDeck("Biology", "Cells");

        ArrayList<String> answers = createSampleAnswers("Cell");
        interactor.addCardToCurrentDeck("What is a cell?", answers, 0);
        interactor.addCardToCurrentDeck("What is a nucleus?", answers, 0);

        interactor.removeCardFromCurrentDeck(0);

        StudyDeck currentDeck = interactor.getCurrentDeck();
        assertEquals(1, currentDeck.getDeck().size());
        assertEquals("What is a nucleus?", currentDeck.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("removeCardFromCurrentDeck does nothing with invalid index")
    void removeCardFromCurrentDeck_doesNothingWithInvalidIndex() {
        interactor.createNewDeck("Biology", "Cells");

        ArrayList<String> answers = createSampleAnswers("Cell");
        interactor.addCardToCurrentDeck("What is a cell?", answers, 0);

        interactor.removeCardFromCurrentDeck(-1);
        StudyDeck currentDeckAfterNegative = interactor.getCurrentDeck();
        assertEquals(1, currentDeckAfterNegative.getDeck().size());

        interactor.removeCardFromCurrentDeck(5);
        StudyDeck currentDeckAfterOutOfBounds = interactor.getCurrentDeck();
        assertEquals(1, currentDeckAfterOutOfBounds.getDeck().size());
    }

    @Test
    @DisplayName("removeCardFromCurrentDeck does nothing when current deck is null")
    void removeCardFromCurrentDeck_doesNothingWhenCurrentDeckIsNull() {
        interactor.removeCardFromCurrentDeck(0);
        assertNull(interactor.getCurrentDeck());
    }

    @Test
    @DisplayName("updateCardInCurrentDeck updates card at valid index")
    void updateCardInCurrentDeck_updatesCardAtValidIndex() {
        interactor.createNewDeck("Physics", "Forces");

        ArrayList<String> originalAnswers = createSampleAnswers("Original");
        interactor.addCardToCurrentDeck("What is force?", originalAnswers, 0);

        ArrayList<String> newAnswers = createSampleAnswers("Updated");
        interactor.updateCardInCurrentDeck(0, "What is gravity?", newAnswers, 1);

        StudyDeck currentDeck = interactor.getCurrentDeck();
        assertEquals(1, currentDeck.getDeck().size());
        assertEquals("What is gravity?", currentDeck.getDeck().get(0).getQuestionTitle());
        assertEquals(newAnswers, currentDeck.getDeck().get(0).getOptions());
        assertEquals(1, currentDeck.getDeck().get(0).getAnswerId());
    }

    @Test
    @DisplayName("updateCardInCurrentDeck does nothing with invalid index")
    void updateCardInCurrentDeck_doesNothingWithInvalidIndex() {
        interactor.createNewDeck("Physics", "Forces");

        ArrayList<String> originalAnswers = createSampleAnswers("Original");
        interactor.addCardToCurrentDeck("What is force?", originalAnswers, 0);

        ArrayList<String> newAnswers = createSampleAnswers("Updated");
        interactor.updateCardInCurrentDeck(-1, "Invalid", newAnswers, 0);
        StudyDeck currentDeckAfterNegative = interactor.getCurrentDeck();
        assertEquals("What is force?", currentDeckAfterNegative.getDeck().get(0).getQuestionTitle());

        interactor.updateCardInCurrentDeck(5, "Invalid", newAnswers, 0);
        StudyDeck currentDeckAfterOutOfBounds = interactor.getCurrentDeck();
        assertEquals("What is force?", currentDeckAfterOutOfBounds.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("updateCardInCurrentDeck does nothing when current deck is null")
    void updateCardInCurrentDeck_doesNothingWhenCurrentDeckIsNull() {
        ArrayList<String> newAnswers = createSampleAnswers("Updated");
        interactor.updateCardInCurrentDeck(0, "Question", newAnswers, 0);
        assertNull(interactor.getCurrentDeck());
    }

    @Test
    @DisplayName("saveCurrentDeck saves current deck to gateway")
    void saveCurrentDeck_savesCurrentDeckToGateway() {
        interactor.createNewDeck("Geography", "Capitals");

        ArrayList<String> answers = createSampleAnswers("Capital");
        interactor.addCardToCurrentDeck("Capital of France?", answers, 0);

        interactor.saveCurrentDeck();

        StudyDeck savedDeck = gateway.getDeck("Geography");
        assertNotNull(savedDeck);
        assertEquals("Geography", savedDeck.getTitle());
        assertEquals("Capitals", savedDeck.getDescription());
        assertEquals(1, savedDeck.getDeck().size());
    }

    @Test
    @DisplayName("saveCurrentDeck does nothing when current deck is null")
    void saveCurrentDeck_doesNothingWhenCurrentDeckIsNull() {
        interactor.saveCurrentDeck();
        assertTrue(gateway.getAllDecks().isEmpty());
    }

    @Test
    @DisplayName("loadDeck loads deck from gateway and filters error cards")
    void loadDeck_loadsDeckFromGatewayAndFiltersErrorCards() {
        ArrayList<StudyCard> cards = new ArrayList<>();
        cards.add(new StudyCard("Error: Question not found - placeholder", createSampleAnswers("Bad"), 0));
        cards.add(new StudyCard("Legit question", createSampleAnswers("Good"), 1));
        StudyDeck storedDeck = new StudyDeck("History", "Desc", cards, 77);
        gateway.saveDeck(storedDeck);

        interactor.loadDeck("History");

        StudyDeck loaded = interactor.getCurrentDeck();
        assertNotNull(loaded);
        assertEquals("History", loaded.getTitle());
        assertEquals(1, loaded.getDeck().size());
        assertEquals("Legit question", loaded.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("loadDeck does nothing when deck doesn't exist")
    void loadDeck_doesNothingWhenDeckDoesntExist() {
        interactor.loadDeck("NonExistentDeck");
        assertNull(interactor.getCurrentDeck());
    }

    @Test
    @DisplayName("unloadDeck clears current deck")
    void unloadDeck_clearsCurrentDeck() {
        interactor.createNewDeck("Temp", "Temporary");
        assertNotNull(interactor.getCurrentDeck());

        interactor.unloadDeck();
        assertNull(interactor.getCurrentDeck());
    }

    @Test
    @DisplayName("deleteDeck removes deck from gateway and clears current deck if it matches")
    void deleteDeck_removesDeckFromGatewayAndClearsCurrentDeck() {
        interactor.createNewDeck("ToDelete", "Will be deleted");
        gateway.saveDeck(interactor.getCurrentDeck());
        assertNotNull(interactor.getCurrentDeck());

        interactor.deleteDeck("ToDelete");

        assertNull(gateway.getDeck("ToDelete"));
        assertNull(interactor.getCurrentDeck());
    }

    @Test
    @DisplayName("deleteDeck removes deck from gateway but doesn't clear current deck if different")
    void deleteDeck_removesDeckFromGatewayButDoesntClearDifferentCurrentDeck() {
        interactor.createNewDeck("Current", "Current deck");
        StudyDeck toDelete = createSampleDeck("ToDelete");
        gateway.saveDeck(toDelete);
        gateway.saveDeck(interactor.getCurrentDeck());

        interactor.deleteDeck("ToDelete");

        assertNull(gateway.getDeck("ToDelete"));
        assertNotNull(interactor.getCurrentDeck());
        assertEquals("Current", interactor.getCurrentDeck().getTitle());
    }

    @Test
    @DisplayName("renameCurrentDeck updates deck in gateway and current deck reference")
    void renameCurrentDeck_updatesDeckInGatewayAndCurrentDeckReference() {
        interactor.createNewDeck("Old Name", "Description");
        interactor.saveCurrentDeck();

        interactor.renameCurrentDeck("New Name");

        assertNull(gateway.getDeck("Old Name"));
        StudyDeck renamed = gateway.getDeck("New Name");
        assertNotNull(renamed);
        assertEquals("New Name", renamed.getTitle());
        assertEquals("New Name", interactor.getCurrentDeck().getTitle());
    }

    @Test
    @DisplayName("renameCurrentDeck does nothing when current deck is null")
    void renameCurrentDeck_doesNothingWhenCurrentDeckIsNull() {
        interactor.renameCurrentDeck("New Name");
        assertNull(interactor.getCurrentDeck());
        assertTrue(gateway.getAllDecks().isEmpty());
    }

    @Test
    @DisplayName("switchToDeck loads existing deck as current deck")
    void switchToDeck_loadsExistingDeckAsCurrentDeck() {
        StudyDeck deck = createSampleDeck("SwitchTo");
        gateway.saveDeck(deck);

        interactor.switchToDeck("SwitchTo");

        StudyDeck current = interactor.getCurrentDeck();
        assertNotNull(current);
        assertEquals("SwitchTo", current.getTitle());
        assertEquals(deck.getDescription(), current.getDescription());
        assertEquals(deck.getDeck(), current.getDeck());
    }

    @Test
    @DisplayName("switchToDeck does nothing when deck doesn't exist")
    void switchToDeck_doesNothingWhenDeckDoesntExist() {
        interactor.createNewDeck("Existing", "Original");
        StudyDeck original = interactor.getCurrentDeck();

        interactor.switchToDeck("NonExistent");

        StudyDeck current = interactor.getCurrentDeck();
        assertEquals(original.getTitle(), current.getTitle());
    }

    @Test
    @DisplayName("getCurrentDeck returns current deck")
    void getCurrentDeck_returnsCurrentDeck() {
        interactor.createNewDeck("Test", "Description");
        StudyDeck current = interactor.getCurrentDeck();

        assertNotNull(current);
        assertEquals("Test", current.getTitle());
        assertEquals("Description", current.getDescription());
    }

    @Test
    @DisplayName("getAllDeckNames returns list of all deck names")
    void getAllDeckNames_returnsListOfAllDeckNames() {
        StudyDeck deck1 = createSampleDeck("Deck 1");
        StudyDeck deck2 = createSampleDeck("Deck 2");
        gateway.saveDeck(deck1);
        gateway.saveDeck(deck2);

        ArrayList<String> deckNames = interactor.getAllDeckNames();

        assertEquals(2, deckNames.size());
        assertTrue(deckNames.contains("Deck 1"));
        assertTrue(deckNames.contains("Deck 2"));
    }

    @Test
    @DisplayName("getAllDeckNames returns empty list when no decks exist")
    void getAllDeckNames_returnsEmptyListWhenNoDecksExist() {
        ArrayList<String> deckNames = interactor.getAllDeckNames();
        assertTrue(deckNames.isEmpty());
    }

    private StudyDeck createSampleDeck(String title) {
        ArrayList<StudyCard> cards = new ArrayList<>();
        ArrayList<String> answers = createSampleAnswers(title + "-Answer");
        StudyCard card = new StudyCard(title + " Question", answers, 0);
        cards.add(card);
        return new StudyDeck(title, "Description of " + title, cards, title.hashCode());
    }

    private ArrayList<String> createSampleAnswers(String prefix) {
        return new ArrayList<>(Arrays.asList(prefix + " A", prefix + " B", prefix + " C", prefix + " D"));
    }

    private static class CapturingPresenter implements StudyDeckOutputBoundary {
        private StudyDeckOutputData lastOutput;

        @Override
        public void prepareView(StudyDeckOutputData studyDeckOutputData) {
            this.lastOutput = studyDeckOutputData;
        }
    }

    private static class InMemoryStudyDeckGateway implements StudyDeckDataAccessInterface {
        private final Map<String, StudyDeck> decks = new LinkedHashMap<>();

        @Override
        public void saveDeck(StudyDeck deck) {
            decks.put(deck.getTitle(), deck);
        }

        @Override
        public List<StudyDeck> getAllDecks() {
            return new ArrayList<>(decks.values());
        }

        @Override
        public StudyDeck getDeck(String deckName) {
            return decks.get(deckName);
        }

        @Override
        public void deleteDeck(String deckName) {
            decks.remove(deckName);
        }

        @Override
        public void updateDeck(StudyDeck deck) {
            String keyToRemove = null;
            for (Map.Entry<String, StudyDeck> entry : decks.entrySet()) {
                if (entry.getValue().getId() == deck.getId()) {
                    keyToRemove = entry.getKey();
                    break;
                }
            }
            if (keyToRemove != null) {
                decks.remove(keyToRemove);
            }
            decks.put(deck.getTitle(), deck);
        }

        @Override
        public void addCardToDeck(String deckName, StudyCard card) {
            StudyDeck existing = decks.get(deckName);
            if (existing == null) {
                return;
            }
            ArrayList<StudyCard> updatedCards = new ArrayList<>(existing.getDeck());
            updatedCards.add(card);
            decks.put(deckName, new StudyDeck(deckName, existing.getDescription(), updatedCards, existing.getId()));
        }

        @Override
        public void removeCardFromDeck(String deckName, int cardIndex) {
            StudyDeck existing = decks.get(deckName);
            if (existing == null) {
                return;
            }
            ArrayList<StudyCard> updatedCards = new ArrayList<>(existing.getDeck());
            if (cardIndex >= 0 && cardIndex < updatedCards.size()) {
                updatedCards.remove(cardIndex);
                decks.put(deckName, new StudyDeck(deckName, existing.getDescription(), updatedCards, existing.getId()));
            }
        }

        @Override
        public void updateCardInDeck(String deckName, int cardIndex, StudyCard newCard) {
            StudyDeck existing = decks.get(deckName);
            if (existing == null) {
                return;
            }
            ArrayList<StudyCard> updatedCards = new ArrayList<>(existing.getDeck());
            if (cardIndex >= 0 && cardIndex < updatedCards.size()) {
                updatedCards.set(cardIndex, newCard);
                decks.put(deckName, new StudyDeck(deckName, existing.getDescription(), updatedCards, existing.getId()));
            }
        }

        @Override
        public void renameDeck(String oldDeckName, String newDeckName) {
            StudyDeck deck = decks.remove(oldDeckName);
            if (deck != null) {
                decks.put(newDeckName, new StudyDeck(newDeckName, deck.getDescription(), new ArrayList<>(deck.getDeck()), deck.getId()));
            }
        }
    }
}