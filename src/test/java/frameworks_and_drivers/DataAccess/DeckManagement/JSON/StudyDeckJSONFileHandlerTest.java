// Test suite for StudyDeckJSONFileHandler class. Verifies JSON serialization/deserialization and file operations.
// Archie

package frameworks_and_drivers.DataAccess.DeckManagement.JSON;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class StudyDeckJSONFileHandlerTest {

    private frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler handler;
    private String testStorageDir = Path.of("").toAbsolutePath().toString() + "/src/test/java/frameworks_and_drivers/DataAccess/DeckManagement/JSON/testing_storage";
    private String testFileName = testStorageDir + "/Test_Deck.json";

    @BeforeEach
    void setUp() {
        handler = new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(testStorageDir);
        // Clean up all JSON files before each test
        cleanupTestFiles();
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
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
    @DisplayName("Constructor with default directory creates handler without error")
    void testConstructorWithDefaultDirectory() {
        // Verify the constructor runs without throwing exceptions
        assertDoesNotThrow(() -> new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler());
    }

    @Test
    @DisplayName("Constructor with custom directory creates handler without error")
    void testConstructorWithCustomDirectory() {
        // Verify the constructor with custom directory runs without throwing exceptions
        assertDoesNotThrow(() -> new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(testStorageDir));
    }

    @Test
    @DisplayName("saveDeck saves valid deck to file system")
    void testSaveDeck() {
        StudyDeck testDeck = createSampleDeck("Test Deck", "A test deck for verification", 123);

        assertDoesNotThrow(() -> handler.saveDeck(testDeck));

        assertTrue(Files.exists(Paths.get(testFileName)));
    }

    @Test
    @DisplayName("saveDeck handles deck with special characters in title")
    void testSaveDeckWithSpecialCharacters() {
        StudyDeck testDeck = createSampleDeck("Test Deck with Spaces & Symbols!", "Description", 456);

        assertDoesNotThrow(() -> handler.saveDeck(testDeck));

        String specialFileName = testStorageDir + "/Test_Deck_with_Spaces_&_Symbols!.json";
        assertTrue(Files.exists(Paths.get(specialFileName)));
    }

    @Test
    @DisplayName("loadAllDecks returns empty list when no decks exist")
    void testLoadAllDecksEmpty() {
        cleanupTestFiles(); // Ensure directory is clean
        var result = handler.loadAllDecks();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("loadAllDecks loads existing decks")
    void testLoadAllDecksWithDecks() {
        StudyDeck testDeck1 = createSampleDeck("Test Deck 1", "Test Description 1", 456);
        StudyDeck testDeck2 = createSampleDeck("Test Deck 2", "Test Description 2", 789);
        handler.saveDeck(testDeck1);
        handler.saveDeck(testDeck2);

        var result = handler.loadAllDecks();
        assertEquals(2, result.size());

        // Verify both decks are loaded
        assertTrue(result.stream().anyMatch(deck -> deck.getTitle().equals("Test Deck 1")));
        assertTrue(result.stream().anyMatch(deck -> deck.getTitle().equals("Test Deck 2")));
    }

    @Test
    @DisplayName("loadDeck loads specific deck by name")
    void testLoadDeckByName() {
        StudyDeck testDeck = createSampleDeck("Test Deck", "Test Description", 789);
        handler.saveDeck(testDeck);

        StudyDeck result = handler.loadDeck("Test Deck");
        assertNotNull(result);
        assertEquals("Test Deck", result.getTitle());
        assertEquals(789, result.getId());
        assertEquals("Test Description", result.getDescription());
        assertEquals(1, result.getDeck().size());
        assertEquals("Test Question", result.getDeck().get(0).getQuestionTitle());
    }

    @Test
    @DisplayName("loadDeck returns null when deck doesn't exist")
    void testLoadDeckNotFound() {
        StudyDeck result = handler.loadDeck("NonExistentDeck");
        assertNull(result);
    }

    @Test
    @DisplayName("deleteDeck removes deck file")
    void testDeleteDeck() {
        StudyDeck testDeck = createSampleDeck("Delete Test", "To be deleted", 999);
        handler.saveDeck(testDeck);

        String deleteFileName = testStorageDir + "/Delete_Test.json";
        assertTrue(Files.exists(Paths.get(deleteFileName)));

        handler.deleteDeck("Delete Test");

        assertFalse(Files.exists(Paths.get(deleteFileName)));
    }

    @Test
    @DisplayName("deleteDeck does nothing when deck doesn't exist")
    void testDeleteDeckWhenNotExists() {
        // Verify no exception is thrown when trying to delete non-existent file
        assertDoesNotThrow(() -> handler.deleteDeck("NonExistentDeck"));
    }

    @Test
    @DisplayName("deckExists returns true when deck exists")
    void testDeckExistsTrue() {
        StudyDeck testDeck = createSampleDeck("Exists Test", "Check if exists", 111);
        handler.saveDeck(testDeck);

        assertTrue(handler.deckExists("Exists Test"));
    }

    @Test
    @DisplayName("deckExists returns false when deck doesn't exist")
    void testDeckExistsFalse() {
        assertFalse(handler.deckExists("NonExistentDeck"));
    }

    @Test
    @DisplayName("deckToJson converts StudyDeck to JSON string correctly")
    void testDeckToJson() throws Exception {
        StudyDeck testDeck = createSampleDeck("Sample Deck", "Test Description", 456);

        // Use reflection to access the private deckToJson method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("deckToJson", StudyDeck.class);
        method.setAccessible(true);
        String result = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), testDeck);

        assertNotNull(result);
        assertTrue(result.contains("\"title\": \"Sample Deck\""));
        assertTrue(result.contains("\"description\": \"Test Description\""));
        assertTrue(result.contains("\"id\": 456"));
        assertTrue(result.contains("\"question\": \"Test Question\""));
        assertTrue(result.contains("\"answers\": ["));
        assertTrue(result.contains("\"solutionId\": 0"));
    }

    @Test
    @DisplayName("jsonToDeck converts JSON string to StudyDeck correctly")
    void testJsonToDeck() throws Exception {
        String json = "{\n" +
                "  \"title\": \"Sample Deck\",\n" +
                "  \"description\": \"Test Description\",\n" +
                "  \"id\": 456,\n" +
                "  \"deck\": [\n" +
                "    {\n" +
                "      \"question\": \"Q1\",\n" +
                "      \"answers\": [\n" +
                "        \"A1\",\n" +
                "        \"A2\",\n" +
                "        \"A3\",\n" +
                "        \"A4\"\n" +
                "      ],\n" +
                "      \"solutionId\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Use reflection to access the private jsonToDeck method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("jsonToDeck", String.class);
        method.setAccessible(true);
        StudyDeck result = (StudyDeck) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), json);

        assertNotNull(result);
        assertEquals("Sample Deck", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(456, result.getId());
        assertEquals(1, result.getDeck().size());

        var card = result.getDeck().get(0);
        assertEquals("Q1", card.getQuestionTitle());
        assertEquals(4, card.getOptions().size());
        assertEquals("A1", card.getOptions().get(0));
        assertEquals("A2", card.getOptions().get(1));
        assertEquals("A3", card.getOptions().get(2));
        assertEquals("A4", card.getOptions().get(3));
        assertEquals(2, card.getAnswerId());
    }

    @Test
    @DisplayName("jsonToCard converts JSON string to StudyCard correctly")
    void testJsonToCard() throws Exception {
        String json = "{\n" +
                "  \"question\": \"Test Question\",\n" +
                "  \"answers\": [\n" +
                "    \"Answer 1\",\n" +
                "    \"Answer 2\",\n" +
                "    \"Answer 3\"\n" +
                "  ],\n" +
                "  \"solutionId\": 1\n" +
                "}";

        // Use reflection to access the private jsonToCard method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("jsonToCard", String.class);
        method.setAccessible(true);
        StudyCard result = (StudyCard) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), json);

        assertNotNull(result);
        assertEquals("Test Question", result.getQuestionTitle());
        assertEquals(3, result.getOptions().size());
        assertEquals("Answer 1", result.getOptions().get(0));
        assertEquals("Answer 2", result.getOptions().get(1));
        assertEquals("Answer 3", result.getOptions().get(2));
        assertEquals(1, result.getAnswerId());
    }

    @Test
    @DisplayName("jsonToCard handles missing question field gracefully")
    void testJsonToCardHandlesMissingQuestion() throws Exception {
        String json = "{\n" +
                "  \"answers\": [\n" +
                "    \"Answer 1\",\n" +
                "    \"Answer 2\"\n" +
                "  ],\n" +
                "  \"solutionId\": 0\n" +
                "}";

        // Use reflection to access the private jsonToCard method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("jsonToCard", String.class);
        method.setAccessible(true);
        StudyCard result = (StudyCard) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), json);

        assertNotNull(result);
        assertEquals("Error: Question not found", result.getQuestionTitle());
    }

    @Test
    @DisplayName("jsonToCard handles missing answers field gracefully")
    void testJsonToCardHandlesMissingAnswers() throws Exception {
        String json = "{\n" +
                "  \"question\": \"Test Question\",\n" +
                "  \"solutionId\": 0\n" +
                "}";

        // Use reflection to access the private jsonToCard method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("jsonToCard", String.class);
        method.setAccessible(true);
        StudyCard result = (StudyCard) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), json);

        assertNotNull(result);
        assertEquals("Test Question", result.getQuestionTitle());
        assertTrue(result.getOptions().isEmpty());
    }

    @Test
    @DisplayName("jsonToCard handles missing solutionId field gracefully")
    void testJsonToCardHandlesMissingSolutionId() throws Exception {
        String json = "{\n" +
                "  \"question\": \"Test Question\",\n" +
                "  \"answers\": [\n" +
                "    \"Answer 1\",\n" +
                "    \"Answer 2\"\n" +
                "  ]\n" +
                "}";

        // Use reflection to access the private jsonToCard method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("jsonToCard", String.class);
        method.setAccessible(true);
        StudyCard result = (StudyCard) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), json);

        assertNotNull(result);
        assertEquals("Test Question", result.getQuestionTitle());
        assertEquals(0, result.getAnswerId());
    }

    @Test
    @DisplayName("findMatchingBracket finds matching bracket correctly")
    void testFindMatchingBracket() throws Exception {
        // Use reflection to access the private findMatchingBracket method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("findMatchingBracket", String.class, int.class);
        method.setAccessible(true);

        // Test simple nested brackets
        String input1 = "{ { } }";
        int result1 = (int) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input1, 0);
        assertEquals(input1.length() - 1, result1);

        // Test more complex nested brackets
        String input2 = "{ { { } } }";
        int result2 = (int) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input2, 0);
        assertEquals(input2.length() - 1, result2);

        // Test unmatched brackets
        String input3 = "{ { }";
        int result3 = (int) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input3, 0);
        assertEquals(-1, result3);
    }

    @Test
    @DisplayName("escapeJsonString handles special characters correctly")
    void testEscapeJsonString() throws Exception {
        // Use reflection to access the private escapeJsonString method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("escapeJsonString", String.class);
        method.setAccessible(true);

        // Test various special characters
        String input1 = "Hello \"World\"";
        String expected1 = "Hello \\\"World\\\"";
        String result1 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input1);
        assertEquals(expected1, result1);

        String input2 = "Line 1\nLine 2";
        String expected2 = "Line 1\\nLine 2";
        String result2 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input2);
        assertEquals(expected2, result2);

        String input3 = "Tab\tSeparated";
        String expected3 = "Tab\\tSeparated";
        String result3 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input3);
        assertEquals(expected3, result3);

        String input4 = "Back\\Slash";
        String expected4 = "Back\\\\Slash";
        String result4 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input4);
        assertEquals(expected4, result4);

        String input5 = null;
        String expected5 = "null";
        String result5 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input5);
        assertEquals(expected5, result5);
    }

    @Test
    @DisplayName("unescapeJsonString handles escaped characters correctly")
    void testUnescapeJsonString() throws Exception {
        // Use reflection to access the private unescapeJsonString method
        java.lang.reflect.Method method = frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler.class.getDeclaredMethod("unescapeJsonString", String.class);
        method.setAccessible(true);

        // Test various escaped characters
        String input1 = "Hello \\\"World\\\"";
        String expected1 = "Hello \"World\"";
        String result1 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input1);
        assertEquals(expected1, result1);

        String input2 = "Line 1\\nLine 2";
        String expected2 = "Line 1\nLine 2";
        String result2 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input2);
        assertEquals(expected2, result2);

        String input3 = "Tab\\tSeparated";
        String expected3 = "Tab\tSeparated";
        String result3 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input3);
        assertEquals(expected3, result3);

        String input4 = "Back\\\\Slash";
        String expected4 = "Back\\Slash";
        String result4 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input4);
        assertEquals(expected4, result4);

        String input5 = null;
        String expected5 = null;
        String result5 = (String) method.invoke(new frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler(), input5);
        assertEquals(expected5, result5);
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
        ArrayList<String> answers = new ArrayList<>();
        answers.add("A1");
        answers.add("A2");
        answers.add("A3");
        answers.add("A4");
        StudyCard testCard = new StudyCard("Test Question", answers, 0);
        cards.add(testCard);
        return new StudyDeck(title, description, cards, id);
    }
}