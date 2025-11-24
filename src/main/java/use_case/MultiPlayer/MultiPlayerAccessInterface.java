//Mahir
package use_case.MultiPlayer;

import java.util.List;

import entity.DeckManagement.StudyDeck;
import interface_adapter.user_session.UserSession;

public interface MultiPlayerAccessInterface {
  List<StudyDeck> getAllDecks();
  UserSession getSession();
}