// Test suite for SysFileHandler class.
// Uses SysFileHandler (obviously lol)
// Archie
package frameworks_and_drivers.DataAccess.DeckManagement.JSON;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class SysFileHandlerTest {

    @Test
    @DisplayName("Test SysFileHandler constructor executes without error")
    void testConstructor() {
        // Verify the constructor runs without throwing exceptions
        assertDoesNotThrow(() -> new SysFileHandler());
    }

    @Test
    @DisplayName("Test saveDeck method executes without error for valid deck")
    void testSaveDeck() {
        // Create a test deck
        StudyDeck testDeck = new StudyDeck();
        testDeck.title = "Test Deck";
        testDeck.description = "A test deck for verification";
        testDeck.id = 123;
        testDeck.deck = new ArrayList<>();

        StudyCard testCard = new StudyCard();
        testCard.question = "What is the capital of France?";
        testCard.answer = new ArrayList<>();
        testCard.answer.add("London");
        testCard.answer.add("Berlin");
        testCard.answer.add("Paris");
        testCard.answer.add("Madrid");
        testDeck.deck.add(testCard);

        // Verify the method executes without throwing exceptions
        assertDoesNotThrow(() -> {
            SysFileHandler handler = new SysFileHandler();
            handler.saveDeck(testDeck);
        });
    }

    @Test
    @DisplayName("Test loadAllDecks throws UnsupportedOperationException")
    void testLoadAllDecks() {
        SysFileHandler handler = new SysFileHandler();
        assertThrows(UnsupportedOperationException.class, () -> handler.loadAllDecks());
    }

    @Test
    @DisplayName("Test loadDeck throws UnsupportedOperationException")
    void testLoadDeck() {
        SysFileHandler handler = new SysFileHandler();
        StudyDeck dummyDeck = new StudyDeck();
        assertThrows(UnsupportedOperationException.class, () -> handler.loadDeck(dummyDeck));
    }

    @Test
    @DisplayName("Test deleteDeck throws UnsupportedOperationException")
    void testDeleteDeck() {
        SysFileHandler handler = new SysFileHandler();
        StudyDeck dummyDeck = new StudyDeck();
        assertThrows(UnsupportedOperationException.class, () -> handler.deleteDeck(dummyDeck));
    }

    @Test
    @DisplayName("Test deckExists throws UnsupportedOperationException")
    void testDeckExists() {
        SysFileHandler handler = new SysFileHandler();
        StudyDeck dummyDeck = new StudyDeck();
        assertThrows(UnsupportedOperationException.class, () -> handler.deckExists(dummyDeck));
    }

    @Test
    @DisplayName("Test deckToJson converts StudyDeck to JSON string correctly")
    void testDeckToJson() throws Exception {
        // Create a test deck with known values
        StudyDeck testDeck = new StudyDeck();
        testDeck.title = "Sample Deck";
        testDeck.description = "Test Description";
        testDeck.id = 456;
        testDeck.deck = new ArrayList<>();

        StudyCard testCard = new StudyCard();
        testCard.question = "Q1";
        testCard.answer = new ArrayList<>();
        testCard.answer.add("A1");
        testCard.answer.add("A2");
        testCard.answer.add("A3");
        testCard.answer.add("A4");
        testDeck.deck.add(testCard);

        // Use reflection to access the private deckToJson method
        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("deckToJson", StudyDeck.class);
        method.setAccessible(true);
        String result = (String) method.invoke(new SysFileHandler(), testDeck);

        // Verify the JSON contains expected elements
        assertNotNull(result);
        assertTrue(result.contains("\"title\": \"Sample Deck\""));
        assertTrue(result.contains("\"description\": \"Test Description\""));
        assertTrue(result.contains("\"id\": 456"));
        assertTrue(result.contains("\"question\": \"Q1\""));
        assertTrue(result.contains("\"A1\""));
    }

    @Test
    @DisplayName("Test jsonToDeck throws UnsupportedOperationException")
    void testJsonToDeck() throws Exception {
        String dummyJson = "{}";

        // Use reflection to access the private jsonToDeck method
        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("jsonToDeck", String.class);
        method.setAccessible(true);

        assertThrows(UnsupportedOperationException.class, () -> {
            method.invoke(new SysFileHandler(), dummyJson);
        });
    }

    @Test
    @DisplayName("Test jsonToCard throws UnsupportedOperationException")
    void testJsonToCard() throws Exception {
        String dummyJson = "{}";

        // Use reflection to access the private jsonToCard method
        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("jsonToCard", String.class);
        method.setAccessible(true);

        assertThrows(UnsupportedOperationException.class, () -> {
            method.invoke(new SysFileHandler(), dummyJson);
        });
    }

    @Test
    @DisplayName("Test findMatchingBracket throws UnsupportedOperationException")
    void testFindMatchingBracket() throws Exception {
        String dummyJson = "{}";
        int dummyStart = 0;

        // Use reflection to access the private findMatchingBracket method
        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("findMatchingBracket", String.class, int.class);
        method.setAccessible(true);

        assertThrows(UnsupportedOperationException.class, () -> {
            method.invoke(new SysFileHandler(), dummyJson, dummyStart);
        });
    }

    @Test
    @DisplayName("Test escapeJsonString handles special characters correctly")
    void testEscapeJsonString() throws Exception {
        // Use reflection to access the private escapeJsonString method
        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("escapeJsonString", String.class);
        method.setAccessible(true);

        // Test various special characters
        String input1 = "Hello \"World\"";
        String expected1 = "Hello \\\"World\\\"";
        String result1 = (String) method.invoke(new SysFileHandler(), input1);
        assertEquals(expected1, result1);

        String input2 = "Line 1\nLine 2";
        String expected2 = "Line 1\\nLine 2";
        String result2 = (String) method.invoke(new SysFileHandler(), input2);
        assertEquals(expected2, result2);

        String input3 = "Tab\tSeparated";
        String expected3 = "Tab\\tSeparated";
        String result3 = (String) method.invoke(new SysFileHandler(), input3);
        assertEquals(expected3, result3);

        String input4 = "Back\\Slash";
        String expected4 = "Back\\\\Slash";
        String result4 = (String) method.invoke(new SysFileHandler(), input4);
        assertEquals(expected4, result4);

        String input5 = null;
        String expected5 = "null";
        String result5 = (String) method.invoke(new SysFileHandler(), input5);
        assertEquals(expected5, result5);
    }

    @Test
    @DisplayName("Test unescapeJsonString throws UnsupportedOperationException")
    void testUnescapeJsonString() throws Exception {
        String dummyInput = "test";

        // Use reflection to access the private unescapeJsonString method
        java.lang.reflect.Method method = SysFileHandler.class.getDeclaredMethod("unescapeJsonString", String.class);
        method.setAccessible(true);

        assertThrows(UnsupportedOperationException.class, () -> {
            method.invoke(new SysFileHandler(), dummyInput);
        });
    }
}