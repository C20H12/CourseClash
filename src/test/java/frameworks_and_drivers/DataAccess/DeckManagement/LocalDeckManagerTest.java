//// Test suite for LocalDeckManager
//// Archie
//
//package frameworks_and_drivers.DataAccess.DeckManagement;
//
//import entity.DeckManagement.StudyCard;
//import entity.DeckManagement.StudyDeck;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import java.util.ArrayList;
//import static org.junit.jupiter.api.Assertions.*;
//
//class LocalDeckManagerTest {
//
//    @Test
//    @DisplayName("Test LocalDeckManager constructor executes without error")
//    void testConstructor() {
//        // Verify the constructor runs without throwing exceptions
//        assertDoesNotThrow(() -> new LocalDeckManager());
//    }
//
//    @Test
//    @DisplayName("Test saveDeck throws UnsupportedOperationException")
//    void testSaveDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        StudyDeck dummyDeck = new StudyDeck();
//        assertThrows(UnsupportedOperationException.class, () -> manager.saveDeck(dummyDeck));
//    }
//
//    @Test
//    @DisplayName("Test getAllDecks throws UnsupportedOperationException")
//    void testGetAllDecks() {
//        LocalDeckManager manager = new LocalDeckManager();
//        assertThrows(UnsupportedOperationException.class, () -> manager.getAllDecks());
//    }
//
//    @Test
//    @DisplayName("Test getDeck throws UnsupportedOperationException")
//    void testGetDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        assertThrows(UnsupportedOperationException.class, () -> manager.getDeck("test_deck"));
//    }
//
//    @Test
//    @DisplayName("Test deleteDeck throws UnsupportedOperationException")
//    void testDeleteDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        assertThrows(UnsupportedOperationException.class, () -> manager.deleteDeck("test_deck"));
//    }
//
//    @Test
//    @DisplayName("Test updateDeck throws UnsupportedOperationException")
//    void testUpdateDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        StudyDeck dummyDeck = new StudyDeck();
//        assertThrows(UnsupportedOperationException.class, () -> manager.updateDeck(dummyDeck));
//    }
//
//    @Test
//    @DisplayName("Test addCardToDeck throws UnsupportedOperationException")
//    void testAddCardToDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        StudyCard dummyCard = new StudyCard();
//        assertThrows(UnsupportedOperationException.class, () -> manager.addCardToDeck("test_deck", dummyCard));
//    }
//
//    @Test
//    @DisplayName("Test removeCardFromDeck throws UnsupportedOperationException")
//    void testRemoveCardFromDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        assertThrows(UnsupportedOperationException.class, () -> manager.removeCardFromDeck("test_deck", 0));
//    }
//
//    @Test
//    @DisplayName("Test updateCardInDeck throws UnsupportedOperationException")
//    void testUpdateCardInDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        StudyCard dummyCard = new StudyCard();
//        assertThrows(UnsupportedOperationException.class, () -> manager.updateCardInDeck("test_deck", 0, dummyCard));
//    }
//
//    @Test
//    @DisplayName("Test renameDeck throws UnsupportedOperationException")
//    void testRenameDeck() {
//        LocalDeckManager manager = new LocalDeckManager();
//        assertThrows(UnsupportedOperationException.class, () -> manager.renameDeck("old_name", "new_name"));
//    }
//}