package frameworks_and_drivers.DataAccess.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for StudyDeckLocalDataAccessObject.
 * Verifies file-based storage operations for StudyDeck objects.
 */
class StudyDeckLocalDataAccessObjectTest {

    private StudyDeckLocalDataAccessObject manager;
    private String testStorageDir = Path.of("").toAbsolutePath().toString() + "/src/test/java/frameworks_and_drivers/DataAccess/DeckManagement/JSON/testing_storage";

    @BeforeEach
    void setUp() {
        manager = new StudyDeckLocalDataAccessObject(testStorageDir);
        // Clean up all JSON files before each test
        cleanupTestFiles();
    }

    @AfterEach
    void tearDown() {
        // Clean up all JSON files after each test
        cleanupTestFiles();
    }

    /**
     * Helper method to clean up test JSON files.
     */
    private void cleanupTestFiles() {
        File storageDir = new File(testStorageDir);
        File[] files = storageDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    assertTrue(file.delete(), "Failed to delete test file: " + file.getName());
                }
            }
        }
    }

    @Test
    @DisplayName("Constructor with directory parameter creates object without error")
    void testConstructorWithDirectory() {
        // Verify the constructor runs without throwing exceptions
        assertDoesNotThrow(() -> new StudyDeckLocalDataAccessObject(testStorageDir));
    }

    @Test
    @DisplayName("Constructor without parameters creates object without error")
    void testConstructorWithoutParameters() {
        // Verify the default constructor runs without throwing exceptions
        assertDoesNotThrow(() -> new StudyDeckLocalDataAccessObject());
    }

    @Test
    @DisplayName("saveDeck saves deck to file system and can be retrieved")
    void testSaveDeck() {
        StudyDeck testDeck = createSampleDeck("Test Deck", "Test Description", 123);

        manager.saveDeck(testDeck);

        StudyDeck retrievedDeck = manager.getDeck("Test Deck");
        assertNotNull(retrievedDeck);
        assertEquals("Test Deck", retrievedDeck.getTitle());
        assertEquals("Test Description", retrievedDeck.getDescription());
        assertEquals(123, retrievedDeck.getId());
        assertEquals(1, retrievedDeck.getDeck().size());
        assertEquals("Test Question", retrievedDeck.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("getAllDecks returns empty list when no decks exist")
    void testGetAllDecksEmpty() {
        List<StudyDeck> result = manager.getAllDecks();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getAllDecks returns all saved decks")
    void testGetAllDecksWithDecks() {
        StudyDeck deck1 = createSampleDeck("Deck 1", "Description 1", 1);
        StudyDeck deck2 = createSampleDeck("Deck 2", "Description 2", 2);
        manager.saveDeck(deck1);
        manager.saveDeck(deck2);

        List<StudyDeck> result = manager.getAllDecks();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(deck -> deck.getTitle().equals("Deck 1")));
        assertTrue(result.stream().anyMatch(deck -> deck.getTitle().equals("Deck 2")));
    }

    @Test
    @DisplayName("getDeck returns specific deck by name")
    void testGetDeck() {
        StudyDeck testDeck = createSampleDeck("Test Deck", "Test Description", 456);
        manager.saveDeck(testDeck);

        StudyDeck result = manager.getDeck("Test Deck");
        assertNotNull(result);
        assertEquals("Test Deck", result.getTitle());
        assertEquals(456, result.getId());
        assertEquals("Test Description", result.getDescription());
        assertEquals(1, result.getDeck().size());
        assertEquals("Test Question", result.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("getDeck returns null when deck doesn't exist")
    void testGetDeckNotFound() {
        StudyDeck result = manager.getDeck("NonExistentDeck");
        assertNull(result);
    }

    @Test
    @DisplayName("deleteDeck removes deck from storage")
    void testDeleteDeck() {
        StudyDeck testDeck = createSampleDeck("Delete Test", "To be deleted", 789);
        manager.saveDeck(testDeck);

        assertNotNull(manager.getDeck("Delete Test"));

        manager.deleteDeck("Delete Test");

        assertNull(manager.getDeck("Delete Test"));
        assertEquals(0, manager.getAllDecks().size());
    }

    @Test
    @DisplayName("updateDeck updates existing deck with same name")
    void testUpdateDeck() {
        StudyDeck initialDeck = createSampleDeck("Update Test", "Initial Description", 999);
        manager.saveDeck(initialDeck);

        ArrayList<StudyCard> updatedCards = new ArrayList<>();
        ArrayList<String> updatedAnswers = new ArrayList<>();
        updatedAnswers.add("Updated Answer");
        StudyCard updatedCard = new StudyCard("Updated Question", updatedAnswers, 0);
        updatedCards.add(updatedCard);
        StudyDeck updatedDeck = new StudyDeck("Update Test", "Updated Description", updatedCards, 999);

        manager.updateDeck(updatedDeck);

        StudyDeck result = manager.getDeck("Update Test");
        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Question", result.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("updateDeck handles replacement of deck with same name")
    void testUpdateDeckReplacesExisting() {
        // Create and save first deck
        StudyDeck firstDeck = createSampleDeck("Same Name", "First Description", 1000);
        manager.saveDeck(firstDeck);

        // Create and save second deck with same name
        StudyDeck secondDeck = createSampleDeck("Same Name", "Second Description", 1001);

        manager.updateDeck(secondDeck);

        StudyDeck result = manager.getDeck("Same Name");
        assertNotNull(result);
        assertEquals("Second Description", result.getDescription());
    }

    @Test
    @DisplayName("addCardToDeck adds card to existing deck")
    void testAddCardToDeck() {
        StudyDeck initialDeck = createSampleDeck("Add Card Test", "Test Description", 111);
        manager.saveDeck(initialDeck);

        ArrayList<String> newAnswers = new ArrayList<>();
        newAnswers.add("New Answer");
        StudyCard newCard = new StudyCard("New Question", newAnswers, 0);

        manager.addCardToDeck("Add Card Test", newCard);

        StudyDeck result = manager.getDeck("Add Card Test");
        assertNotNull(result);
        assertEquals(2, result.getDeck().size());
        assertEquals("New Question", result.getDeck().get(1).getQuestionTitle());
        assertEquals("Test Question", result.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("addCardToDeck does nothing when deck doesn't exist")
    void testAddCardToDeckWhenDeckDoesNotExist() {
        ArrayList<String> newAnswers = new ArrayList<>();
        newAnswers.add("New Answer");
        StudyCard newCard = new StudyCard("New Question", newAnswers, 0);

        manager.addCardToDeck("NonExistentDeck", newCard);

        assertEquals(0, manager.getAllDecks().size());
    }

    @Test
    @DisplayName("removeCardFromDeck removes card from deck")
    void testRemoveCardFromDeck() {
        ArrayList<StudyCard> cards = new ArrayList<>();
        cards.add(createSampleCard("Question 1", 0));
        cards.add(createSampleCard("Question 2", 1));
        cards.add(createSampleCard("Question 3", 2));
        StudyDeck deck = new StudyDeck("Remove Card Test", "Test Description", cards, 222);
        manager.saveDeck(deck);

        manager.removeCardFromDeck("Remove Card Test", 1);

        StudyDeck result = manager.getDeck("Remove Card Test");
        assertNotNull(result);
        assertEquals(2, result.getDeck().size());
        assertEquals("Question 3", result.getDeck().get(1).getQuestionTitle());
    }

    @Test
    @DisplayName("removeCardFromDeck does nothing with invalid index")
    void testRemoveCardFromDeckWithInvalidIndex() {
        StudyDeck deck = createSampleDeck("Remove Test", "Test Description", 333);
        manager.saveDeck(deck);

        manager.removeCardFromDeck("Remove Test", -1);
        StudyDeck resultAfterNegative = manager.getDeck("Remove Test");
        assertEquals(1, resultAfterNegative.getDeck().size());

        manager.removeCardFromDeck("Remove Test", 5);
        StudyDeck resultAfterOutOfBounds = manager.getDeck("Remove Test");
        assertEquals(1, resultAfterOutOfBounds.getDeck().size());
    }

    @Test
    @DisplayName("updateCardInDeck replaces card in deck")
    void testUpdateCardInDeck() {
        ArrayList<StudyCard> cards = new ArrayList<>();
        cards.add(createSampleCard("Question 1", 0));
        cards.add(createSampleCard("Question 2", 1));
        StudyDeck deck = new StudyDeck("Update Card Test", "Test Description", cards, 444);
        manager.saveDeck(deck);

        ArrayList<String> newAnswers = new ArrayList<>();
        newAnswers.add("Updated Answer");
        StudyCard newCard = new StudyCard("Updated Question", newAnswers, 0);

        manager.updateCardInDeck("Update Card Test", 1, newCard);

        StudyDeck result = manager.getDeck("Update Card Test");
        assertNotNull(result);
        assertEquals(2, result.getDeck().size());
        assertEquals("Updated Question", result.getDeck().get(1).getQuestionTitle());
        assertEquals("Question 1", result.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("updateCardInDeck does nothing with invalid index")
    void testUpdateCardInDeckWithInvalidIndex() {
        StudyDeck deck = createSampleDeck("Update Test", "Test Description", 555);
        manager.saveDeck(deck);

        ArrayList<String> newAnswers = new ArrayList<>();
        newAnswers.add("Updated Answer");
        StudyCard newCard = new StudyCard("Updated Question", newAnswers, 0);

        manager.updateCardInDeck("Update Test", -1, newCard);
        StudyDeck resultAfterNegative = manager.getDeck("Update Test");
        assertEquals("Test Question", resultAfterNegative.getDeck().get(0).getQuestionTitle());

        manager.updateCardInDeck("Update Test", 5, newCard);
        StudyDeck resultAfterOutOfBounds = manager.getDeck("Update Test");
        assertEquals("Test Question", resultAfterOutOfBounds.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("renameDeck renames existing deck")
    void testRenameDeck() {
        StudyDeck testDeck = createSampleDeck("Old Name", "Test Description", 666);
        manager.saveDeck(testDeck);

        assertNotNull(manager.getDeck("Old Name"));
        assertNull(manager.getDeck("New Name"));

        manager.renameDeck("Old Name", "New Name");

        assertNull(manager.getDeck("Old Name"));
        StudyDeck renamedDeck = manager.getDeck("New Name");
        assertNotNull(renamedDeck);
        assertEquals("New Name", renamedDeck.getTitle());
        assertEquals("Test Description", renamedDeck.getDescription());
        assertEquals(666, renamedDeck.getId());
    }

    @Test
    @DisplayName("renameDeck does nothing when deck doesn't exist")
    void testRenameDeckWhenDeckDoesNotExist() {
        manager.renameDeck("NonExistent", "New Name");
        assertEquals(0, manager.getAllDecks().size());
    }

    /**
     * Helper method to create a sample deck.
     * @param title The title of the deck
     * @param description The description of the deck
     * @param id The ID of the deck
     * @return A new StudyDeck object
     */
    private StudyDeck createSampleDeck(String title, String description, int id) {
        ArrayList<StudyCard> cards = new ArrayList<>();
        cards.add(createSampleCard("Test Question", 0));
        return new StudyDeck(title, description, cards, id);
    }

    /**
     * Helper method to create a sample card.
     * @param question The question for the card
     * @param correctIndex The correct answer index
     * @return A new StudyCard object
     */
    private StudyCard createSampleCard(String question, int correctIndex) {
        ArrayList<String> answers = new ArrayList<>();
        answers.add("Answer 1");
        answers.add("Answer 2");
        return new StudyCard(question, answers, correctIndex);
    }
}