package frameworks_and_drivers.DataAccess.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
	void executeAddDeck_savesDeckAndNotifiesPresenter() {
		StudyDeck deck = sampleDeck("Biology");
		interactor.execute(new StudyDeckInputData(deck), StudyDeckAction.ADD_DECK);

		StudyDeck stored = gateway.getDeck("Biology");
		assertNotNull(stored);
		assertEquals(deck.getTitle(), stored.getTitle());
		assertNotNull(presenter.lastOutput);
		assertEquals(1, presenter.lastOutput.getDecks().size());
		assertEquals("Biology", presenter.lastOutput.getDecks().get(0).getTitle());
	}

	@Test
	void executeRemoveDeck_deletesDeckAndUpdatesPresenter() {
		StudyDeck deck = sampleDeck("Physics");
		gateway.saveDeck(deck);

		interactor.execute(new StudyDeckInputData(deck), StudyDeckAction.REMOVE_DECK);

		assertNull(gateway.getDeck("Physics"));
		assertNotNull(presenter.lastOutput);
		assertTrue(presenter.lastOutput.getDecks().isEmpty());
	}

	@Test
	void cardEditingOperations_mutateCurrentDeck() {
		interactor.createNewDeck("Chemistry", "Intro");

		ArrayList<String> answers = sampleAnswers("Chem1");
		interactor.addCardToCurrentDeck("What is Chem1?", answers, 2);
		assertEquals(1, interactor.getCurrentDeck().getCardCount());

		ArrayList<String> updatedAnswers = sampleAnswers("Chem2");
		interactor.updateCardInCurrentDeck(0, "Updated question", updatedAnswers, 1);
		StudyCard updatedCard = interactor.getCurrentDeck().getDeck().get(0);
		assertEquals("Updated question", updatedCard.getQuestion());
		assertEquals(updatedAnswers, updatedCard.getAnswers());

		interactor.removeCardFromCurrentDeck(0);
		assertEquals(0, interactor.getCurrentDeck().getCardCount());
	}

	@Test
	void loadDeck_filtersErrorCards() {
		ArrayList<StudyCard> cards = new ArrayList<>();
		cards.add(new StudyCard("Error: Question not found - placeholder", sampleAnswers("Bad"), 0));
		cards.add(new StudyCard("Legit question", sampleAnswers("Good"), 1));
		StudyDeck storedDeck = new StudyDeck("History", "Desc", cards, 77);
		gateway.saveDeck(storedDeck);

		interactor.loadDeck("History");

		StudyDeck loaded = interactor.getCurrentDeck();
		assertNotNull(loaded);
		assertEquals(1, loaded.getDeck().size());
		assertEquals("Legit question", loaded.getDeck().get(0).getQuestion());
	}

	@Test
	void renameCurrentDeck_updatesGatewayEntry() {
		interactor.createNewDeck("Temp", "Description");
		interactor.saveCurrentDeck();

		interactor.renameCurrentDeck("Final Name");

		assertNull(gateway.getDeck("Temp"));
		StudyDeck renamed = gateway.getDeck("Final Name");
		assertNotNull(renamed);
		assertEquals("Final Name", renamed.getTitle());
		assertEquals("Final Name", interactor.getCurrentDeck().getTitle());
	}

	private StudyDeck sampleDeck(String title) {
		ArrayList<StudyCard> cards = new ArrayList<>();
		cards.add(new StudyCard(title + " Q1", sampleAnswers(title + "-1"), 0));
		return new StudyDeck(title, "Description of " + title, cards, title.hashCode());
	}

	private ArrayList<String> sampleAnswers(String prefix) {
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
