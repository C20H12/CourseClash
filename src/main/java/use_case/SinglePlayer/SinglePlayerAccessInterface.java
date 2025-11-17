// Huzaifa - interface
package use_case.SinglePlayer;

import entity.StudyDeck;

public interface SinglePlayerAccessInterface {
    StudyDeck loadDeck(String deckName);
    void saveSinglePlayerResult(String username, String deckName, int score, double accuracy, double avgTime);
}