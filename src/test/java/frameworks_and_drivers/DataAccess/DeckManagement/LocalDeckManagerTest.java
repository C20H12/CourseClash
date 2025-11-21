//// Test suite for LocalDeckManager.
//// Archie
//
//package frameworks_and_drivers.DataAccess.DeckManagement;
//
//import entity.DeckManagement.StudyCard;
//import entity.DeckManagement.StudyDeck;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.AfterEach;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//
//class LocalDeckManagerTest {
//
//    private LocalDeckManager manager;
//    private String testStorageDir = System.getProperty("user.home") + "/.CourseClash/local_storage";
//
//    @BeforeEach
//    void setUp() {
//        manager = new LocalDeckManager();
//        // Clean up all JSON files before each test
//        File storageDir = new File(testStorageDir);
//        File[] files = storageDir.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.getName().endsWith(".json")) {
//                    file.delete();
//                }
//            }
//        }
//    }
//
//    @AfterEach
//    void tearDown() {
//        // Clean up all JSON files after each test
//        File storageDir = new File(testStorageDir);
//        File[] files = storageDir.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.getName().endsWith(".json")) {
//                    file.delete();
//                }
//            }
//        }
//    }
//
//    @Test
//    @DisplayName("Test LocalDeckManager constructor executes without error")
//    void testConstructor() {
//        // Verify the constructor runs without throwing exceptions
//        assertDoesNotThrow(() -> new LocalDeckManager());
//    }
//
//    @Test
//    @DisplayName("Test saveDeck saves deck to file system")
//    void testSaveDeck() {
//        // Create a test deck using constructor
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("Answer 1");
//        answers.add("Answer 2");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Test Deck", "Test Description", cards, 123);
//
//        // Save the deck
//        manager.saveDeck(testDeck);
//
//        // Verify the deck exists
//        assertTrue(manager.getDeck("Test Deck") != null);
//    }
//
//    @Test
//    @DisplayName("Test getAllDecks returns empty list when no decks exist")
//    void testGetAllDecksEmpty() {
//        List<StudyDeck> result = manager.getAllDecks();
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Test getAllDecks returns all saved decks")
//    void testGetAllDecksWithDecks() {
//        // Create and save multiple decks
//        ArrayList<StudyCard> cards1 = new ArrayList<>();
//        ArrayList<String> answers1 = new ArrayList<>();
//        answers1.add("A1");
//        StudyCard card1 = new StudyCard("Q1", answers1, 0);
//        cards1.add(card1);
//        StudyDeck deck1 = new StudyDeck("Deck 1", "Description 1", cards1, 1);
//        manager.saveDeck(deck1);
//
//        ArrayList<StudyCard> cards2 = new ArrayList<>();
//        ArrayList<String> answers2 = new ArrayList<>();
//        answers2.add("A2");
//        StudyCard card2 = new StudyCard("Q2", answers2, 0);
//        cards2.add(card2);
//        StudyDeck deck2 = new StudyDeck("Deck 2", "Description 2", cards2, 2);
//        manager.saveDeck(deck2);
//
//        List<StudyDeck> result = manager.getAllDecks();
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    @DisplayName("Test getDeck returns specific deck by name")
//    void testGetDeck() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("Answer 1");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Test Deck", "Test Description", cards, 456);
//        manager.saveDeck(testDeck);
//
//        // Get the deck by name
//        StudyDeck result = manager.getDeck("Test Deck");
//        assertNotNull(result);
//        assertEquals("Test Deck", result.getTitle());
//        assertEquals(456, result.getId());
//        assertEquals("Test Description", result.getDescription());
//        assertEquals(1, result.getDeck().size());
//    }
//
//    @Test
//    @DisplayName("Test getDeck returns null when deck doesn't exist")
//    void testGetDeckNotFound() {
//        StudyDeck result = manager.getDeck("NonExistentDeck");
//        assertNull(result);
//    }
//
//    @Test
//    @DisplayName("Test deleteDeck removes deck from storage")
//    void testDeleteDeck() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("Answer 1");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Delete Test", "To be deleted", cards, 789);
//        manager.saveDeck(testDeck);
//
//        // Verify deck exists
//        assertNotNull(manager.getDeck("Delete Test"));
//
//        // Delete the deck
//        manager.deleteDeck("Delete Test");
//
//        // Verify deck is gone
//        assertNull(manager.getDeck("Delete Test"));
//    }
//
//    @Test
//    @DisplayName("Test updateDeck updates existing deck")
//    void testUpdateDeck() {
//        // Create and save initial deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("Answer 1");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck initialDeck = new StudyDeck("Update Test", "Initial Description", cards, 999);
//        manager.saveDeck(initialDeck);
//
//        // Create updated deck with same title
//        ArrayList<StudyCard> updatedCards = new ArrayList<>();
//        ArrayList<String> updatedAnswers = new ArrayList<>();
//        updatedAnswers.add("Updated Answer");
//        StudyCard updatedCard = new StudyCard("Updated Question", updatedAnswers, 0);
//        updatedCards.add(updatedCard);
//        StudyDeck updatedDeck = new StudyDeck("Update Test", "Updated Description", updatedCards, 999);
//
//        // Update the deck
//        manager.updateDeck(updatedDeck);
//
//        // Verify the deck was updated
//        StudyDeck result = manager.getDeck("Update Test");
//        assertNotNull(result);
//        assertEquals("Updated Description", result.getDescription());
//        assertEquals("Updated Question", result.getDeck().get(0).getQuestion());
//    }
//
//    @Test
//    @DisplayName("Test addCardToDeck adds card to existing deck")
//    void testAddCardToDeck() {
//        // Create and save initial deck with one card
//        ArrayList<StudyCard> initialCards = new ArrayList<>();
//        ArrayList<String> initialAnswers = new ArrayList<>();
//        initialAnswers.add("Initial Answer");
//        StudyCard initialCard = new StudyCard("Initial Question", initialAnswers, 0);
//        initialCards.add(initialCard);
//        StudyDeck initialDeck = new StudyDeck("Add Card Test", "Test Description", initialCards, 111);
//        manager.saveDeck(initialDeck);
//
//        // Create new card to add
//        ArrayList<String> newAnswers = new ArrayList<>();
//        newAnswers.add("New Answer");
//        StudyCard newCard = new StudyCard("New Question", newAnswers, 0);
//
//        // Add card to deck
//        manager.addCardToDeck("Add Card Test", newCard);
//
//        // Verify the card was added
//        StudyDeck result = manager.getDeck("Add Card Test");
//        assertNotNull(result);
//        assertEquals(2, result.getDeck().size());
//        assertEquals("New Question", result.getDeck().get(1).getQuestion());
//    }
//
//    @Test
//    @DisplayName("Test removeCardFromDeck removes card from deck")
//    void testRemoveCardFromDeck() {
//        // Create and save deck with multiple cards
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers1 = new ArrayList<>();
//        answers1.add("Answer 1");
//        StudyCard card1 = new StudyCard("Question 1", answers1, 0);
//        ArrayList<String> answers2 = new ArrayList<>();
//        answers2.add("Answer 2");
//        StudyCard card2 = new StudyCard("Question 2", answers2, 0);
//        ArrayList<String> answers3 = new ArrayList<>();
//        answers3.add("Answer 3");
//        StudyCard card3 = new StudyCard("Question 3", answers3, 0);
//        cards.add(card1);
//        cards.add(card2);
//        cards.add(card3);
//        StudyDeck deck = new StudyDeck("Remove Card Test", "Test Description", cards, 222);
//        manager.saveDeck(deck);
//
//        // Remove middle card (index 1)
//        manager.removeCardFromDeck("Remove Card Test", 1);
//
//        // Verify card was removed
//        StudyDeck result = manager.getDeck("Remove Card Test");
//        assertNotNull(result);
//        assertEquals(2, result.getDeck().size());
//        assertEquals("Question 3", result.getDeck().get(1).getQuestion()); // Third card should now be at index 1
//    }
//
//    @Test
//    @DisplayName("Test updateCardInDeck replaces card in deck")
//    void testUpdateCardInDeck() {
//        // Create and save deck with multiple cards
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers1 = new ArrayList<>();
//        answers1.add("Answer 1");
//        StudyCard card1 = new StudyCard("Question 1", answers1, 0);
//        ArrayList<String> answers2 = new ArrayList<>();
//        answers2.add("Answer 2");
//        StudyCard card2 = new StudyCard("Question 2", answers2, 0);
//        cards.add(card1);
//        cards.add(card2);
//        StudyDeck deck = new StudyDeck("Update Card Test", "Test Description", cards, 333);
//        manager.saveDeck(deck);
//
//        // Create new card to replace with
//        ArrayList<String> newAnswers = new ArrayList<>();
//        newAnswers.add("Updated Answer");
//        StudyCard newCard = new StudyCard("Updated Question", newAnswers, 0);
//
//        // Replace card at index 1
//        manager.updateCardInDeck("Update Card Test", 1, newCard);
//
//        // Verify card was replaced
//        StudyDeck result = manager.getDeck("Update Card Test");
//        assertNotNull(result);
//        assertEquals(2, result.getDeck().size());
//        assertEquals("Updated Question", result.getDeck().get(1).getQuestion());
//        assertEquals("Updated Answer", result.getDeck().get(1).getAnswers().get(0));
//    }
//
//    @Test
//    @DisplayName("Test renameDeck renames existing deck")
//    void testRenameDeck() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("Answer 1");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Old Name", "Test Description", cards, 444);
//        manager.saveDeck(testDeck);
//
//        // Verify deck exists with old name
//        assertNotNull(manager.getDeck("Old Name"));
//        assertNull(manager.getDeck("New Name"));
//
//        // Rename the deck
//        manager.renameDeck("Old Name", "New Name");
//
//        // Verify deck exists with new name and not old name
//        assertNull(manager.getDeck("Old Name"));
//        assertNotNull(manager.getDeck("New Name"));
//        assertEquals("New Name", manager.getDeck("New Name").getTitle());
//    }
//}