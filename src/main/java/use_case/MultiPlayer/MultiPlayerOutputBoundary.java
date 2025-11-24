package use_case.MultiPlayer;

import java.util.List;

import entity.DeckManagement.StudyDeck;

public interface MultiPlayerOutputBoundary {
  void presentQuestion(MultiPlayerOutputData data);
  void presentEndGame(MultiPlayerOutputData data);
  void presentAllDecks(List<StudyDeck> decks);
  void presentUpdateScore(MultiPlayerOutputData data);
}
