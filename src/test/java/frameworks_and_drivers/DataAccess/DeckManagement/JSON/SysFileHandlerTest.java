//// Test suite for SysFileHandler class..
//// Uses SysFileHandler (obviously lol)
//// Archie
//package frameworks_and_drivers.DataAccess.DeckManagement.JSON;
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
//import static org.junit.jupiter.api.Assertions.*;
//
//class SysFileHandlerTest {
//
//    private SysFileHandler handler;
//    private String testStorageDir = System.getProperty("user.home") + "/.CourseClash/local_storage";
//    private String testFileName = testStorageDir + "/Test_Deck.json";
//
//    @BeforeEach
//    void setUp() {
//        handler = new SysFileHandler();
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
//        // Clean up after tests
//        try {
//            Files.deleteIfExists(Paths.get(testFileName));
//        } catch (IOException e) {
//            // Ignore cleanup errors
//        }
//    }
//
//    @Test
//    @DisplayName("Test SysFileHandler constructor executes without error")
//    void testConstructor() {
//        // Verify the constructor runs without throwing exceptions
//        assertDoesNotThrow(() -> new SysFileHandler());
//    }
//
//    @Test
//    @DisplayName("Test saveDeck method executes without error for valid deck")
//    void testSaveDeck() {
//        // Create a test deck using constructor
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("London");
//        answers.add("Berlin");
//        answers.add("Paris");
//        answers.add("Madrid");
//        StudyCard testCard = new StudyCard("What is the capital of France?", answers, 2); // Paris is index 2
//        cards.add(testCard);
//
//        StudyDeck testDeck = new StudyDeck("Test Deck", "A test deck for verification", cards, 123);
//
//        // Verify the method executes without throwing exceptions
//        assertDoesNotThrow(() -> {
//            handler.saveDeck(testDeck);
//        });
//
//        // Verify file was created
//        assertTrue(Files.exists(Paths.get(testFileName)));
//    }
//
//    @Test
//    @DisplayName("Test loadAllDecks returns empty list when no decks exist")
//    void testLoadAllDecksEmpty() {
//        // Clean storage directory first
//        File storageDir = new File(testStorageDir);
//        File[] files = storageDir.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.getName().endsWith(".json")) {
//                    file.delete();
//                }
//            }
//        }
//
//        var result = handler.loadAllDecks();
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Test loadAllDecks loads existing decks")
//    void testLoadAllDecksWithDecks() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("A1");
//        answers.add("A2");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Test Deck", "Test Description", cards, 456);
//        handler.saveDeck(testDeck);
//
//        // Load all decks
//        var result = handler.loadAllDecks();
//        assertEquals(1, result.size());
//        assertEquals("Test Deck", result.get(0).getTitle());
//        assertEquals(456, result.get(0).getId());
//    }
//
//    @Test
//    @DisplayName("Test loadDeck loads specific deck by name")
//    void testLoadDeckByName() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("A1");
//        answers.add("A2");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Test Deck", "Test Description", cards, 789);
//        handler.saveDeck(testDeck);
//
//        // Load specific deck
//        StudyDeck result = handler.loadDeck("Test Deck");
//        assertNotNull(result);
//        assertEquals("Test Deck", result.getTitle());
//        assertEquals(789, result.getId());
//        assertEquals("Test Description", result.getDescription());
//        assertEquals(1, result.getDeck().size());
//        assertEquals("Test Question", result.getDeck().get(0).getQuestion());
//    }
//
//    @Test
//    @DisplayName("Test loadDeck returns null when deck doesn't exist")
//    void testLoadDeckNotFound() {
//        StudyDeck result = handler.loadDeck("NonExistentDeck");
//        assertNull(result);
//    }
//
//    @Test
//    @DisplayName("Test deleteDeck removes deck file")
//    void testDeleteDeck() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("A1");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Delete Test", "To be deleted", cards, 999);
//        handler.saveDeck(testDeck);
//
//        // Verify file exists
//        assertTrue(Files.exists(Paths.get(testStorageDir + "/Delete_Test.json")));
//
//        // Delete the deck
//        handler.deleteDeck("Delete Test");
//
//        // Verify file is gone
//        assertFalse(Files.exists(Paths.get(testStorageDir + "/Delete_Test.json")));
//    }
//
//    @Test
//    @DisplayName("Test deckExists returns true when deck exists")
//    void testDeckExistsTrue() {
//        // Create and save a test deck
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("A1");
//        StudyCard testCard = new StudyCard("Test Question", answers, 0);
//        cards.add(testCard);
//        StudyDeck testDeck = new StudyDeck("Exists Test", "Check if exists", cards, 111);
//        handler.saveDeck(testDeck);
//
//        assertTrue(handler.deckExists("Exists Test"));
//    }
//
//    @Test
//    @DisplayName("Test deckExists returns false when deck doesn't exist")
//    void testDeckExistsFalse() {
//        assertFalse(handler.deckExists("NonExistentDeck"));
//    }
//
//    @Test
//    @DisplayName("Test deckToJson converts StudyDeck to JSON string correctly")
//    void testDeckToJson() throws Exception {
//        // Create a test deck with known values using constructor
//        ArrayList<StudyCard> cards = new ArrayList<>();
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("A1");
//        answers.add("A2");
//        answers.add("A3");
//        answers.add("A4");
//        StudyCard testCard = new StudyCard("Q1", answers, 2); // solutionId = 2
//        cards.add(testCard);
//
//        StudyDeck testDeck = new StudyDeck("Sample Deck", "Test Description", cards, 456);
//
//        // Use reflection to access the private deckToJson method
//        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("deckToJson", StudyDeck.class);
//        method.setAccessible(true);
//        String result = (String) method.invoke(new SysFileHandler(), testDeck);
//
//        // Verify the JSON contains expected elements
//        assertNotNull(result);
//        assertTrue(result.contains("\"title\": \"Sample Deck\""));
//        assertTrue(result.contains("\"description\": \"Test Description\""));
//        assertTrue(result.contains("\"id\": 456"));
//        assertTrue(result.contains("\"question\": \"Q1\""));
//        assertTrue(result.contains("\"answers\": ["));
//        assertTrue(result.contains("\"A1\""));
//        assertTrue(result.contains("\"solutionId\": 2"));
//    }
//
//    @Test
//    @DisplayName("Test jsonToDeck converts JSON string to StudyDeck correctly")
//    void testJsonToDeck() throws Exception {
//        String json = "{\n" +
//                "  \"title\": \"Sample Deck\",\n" +
//                "  \"description\": \"Test Description\",\n" +
//                "  \"id\": 456,\n" +
//                "  \"deck\": [\n" +
//                "    {\n" +
//                "      \"question\": \"Q1\",\n" +
//                "      \"answers\": [\n" +
//                "        \"A1\",\n" +
//                "        \"A2\",\n" +
//                "        \"A3\",\n" +
//                "        \"A4\"\n" +
//                "      ],\n" +
//                "      \"solutionId\": 2\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";
//
//        // Use reflection to access the private jsonToDeck method
//        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("jsonToDeck", String.class);
//        method.setAccessible(true);
//        StudyDeck result = (StudyDeck) method.invoke(new SysFileHandler(), json);
//
//        // Verify the deck was reconstructed correctly
//        assertNotNull(result);
//        assertEquals("Sample Deck", result.getTitle());
//        assertEquals("Test Description", result.getDescription());
//        assertEquals(456, result.getId());
//        assertEquals(1, result.getDeck().size());
//
//        var card = result.getDeck().get(0);
//        assertEquals("Q1", card.getQuestion());
//        assertEquals(4, card.getAnswers().size());
//        assertEquals("A1", card.getAnswers().get(0));
//        assertEquals("A2", card.getAnswers().get(1));
//        assertEquals("A3", card.getAnswers().get(2));
//        assertEquals("A4", card.getAnswers().get(3));
//        assertEquals(2, card.getSolutionId());
//    }
//
//    @Test
//    @DisplayName("Test jsonToCard converts JSON string to StudyCard correctly")
//    void testJsonToCard() throws Exception {
//        String json = "{\n" +
//                "  \"question\": \"Test Question\",\n" +
//                "  \"answers\": [\n" +
//                "    \"Answer 1\",\n" +
//                "    \"Answer 2\",\n" +
//                "    \"Answer 3\"\n" +
//                "  ],\n" +
//                "  \"solutionId\": 1\n" +
//                "}";
//
//        // Use reflection to access the private jsonToCard method
//        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("jsonToCard", String.class);
//        method.setAccessible(true);
//        StudyCard result = (StudyCard) method.invoke(new SysFileHandler(), json);
//
//        // Verify the card was reconstructed correctly
//        assertNotNull(result);
//        assertEquals("Test Question", result.getQuestion());
//        assertEquals(3, result.getAnswers().size());
//        assertEquals("Answer 1", result.getAnswers().get(0));
//        assertEquals("Answer 2", result.getAnswers().get(1));
//        assertEquals("Answer 3", result.getAnswers().get(2));
//        assertEquals(1, result.getSolutionId());
//    }
//
//    @Test
//    @DisplayName("Test findMatchingBracket finds matching bracket correctly")
//    void testFindMatchingBracket() throws Exception {
//        // Use reflection to access the private findMatchingBracket method
//        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("findMatchingBracket", String.class, int.class);
//        method.setAccessible(true);
//
//        // Test simple nested brackets
//        String input1 = "{ { } }";
//        int result1 = (int) method.invoke(new SysFileHandler(), input1, 0);
//        assertEquals(input1.length() - 1, result1);
//
//        // Test more complex nested brackets
//        String input2 = "{ { { } } }";
//        int result2 = (int) method.invoke(new SysFileHandler(), input2, 0);
//        assertEquals(input2.length() - 1, result2);
//
//        // Test unmatched brackets
//        String input3 = "{ { }";
//        int result3 = (int) method.invoke(new SysFileHandler(), input3, 0);
//        assertEquals(-1, result3);
//    }
//
//    @Test
//    @DisplayName("Test escapeJsonString handles special characters correctly")
//    void testEscapeJsonString() throws Exception {
//        // Use reflection to access the private escapeJsonString method
//        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("escapeJsonString", String.class);
//        method.setAccessible(true);
//
//        // Test various special characters
//        String input1 = "Hello \"World\"";
//        String expected1 = "Hello \\\"World\\\"";
//        String result1 = (String) method.invoke(new SysFileHandler(), input1);
//        assertEquals(expected1, result1);
//
//        String input2 = "Line 1\nLine 2";
//        String expected2 = "Line 1\\nLine 2";
//        String result2 = (String) method.invoke(new SysFileHandler(), input2);
//        assertEquals(expected2, result2);
//
//        String input3 = "Tab\tSeparated";
//        String expected3 = "Tab\\tSeparated";
//        String result3 = (String) method.invoke(new SysFileHandler(), input3);
//        assertEquals(expected3, result3);
//
//        String input4 = "Back\\Slash";
//        String expected4 = "Back\\\\Slash";
//        String result4 = (String) method.invoke(new SysFileHandler(), input4);
//        assertEquals(expected4, result4);
//
//        String input5 = null;
//        String expected5 = "null";
//        String result5 = (String) method.invoke(new SysFileHandler(), input5);
//        assertEquals(expected5, result5);
//    }
//
//    @Test
//    @DisplayName("Test unescapeJsonString handles escaped characters correctly")
//    void testUnescapeJsonString() throws Exception {
//        // Use reflection to access the private unescapeJsonString method
//        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("unescapeJsonString", String.class);
//        method.setAccessible(true);
//
//        // Test various escaped characters
//        String input1 = "Hello \\\"World\\\"";
//        String expected1 = "Hello \"World\"";
//        String result1 = (String) method.invoke(new SysFileHandler(), input1);
//        assertEquals(expected1, result1);
//
//        String input2 = "Line 1\\nLine 2";
//        String expected2 = "Line 1\nLine 2";
//        String result2 = (String) method.invoke(new SysFileHandler(), input2);
//        assertEquals(expected2, result2);
//
//        String input3 = "Tab\\tSeparated";
//        String expected3 = "Tab\tSeparated";
//        String result3 = (String) method.invoke(new SysFileHandler(), input3);
//        assertEquals(expected3, result3);
//
//        String input4 = "Back\\\\Slash";
//        String expected4 = "Back\\Slash";
//        String result4 = (String) method.invoke(new SysFileHandler(), input4);
//        assertEquals(expected4, result4);
//
//        String input5 = null;
//        String expected5 = null;
//        String result5 = (String) method.invoke(new SysFileHandler(), input5);
//        assertEquals(expected5, result5);
//    }
//}