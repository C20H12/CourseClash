package use_case.multiplayer;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.MultiPlayerDataAccessObject;
import interface_adapter.MultiPlayer.MultiPlayerGameState;
import interface_adapter.MultiPlayer.MultiPlayerPresenter;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.user_session.UserSession;
import use_case.MultiPlayer.MultiPlayerInteractor;


public class MultiPlayerInteractorTest {

  private MultiPlayerDataAccessObject dao;
  private MultiPlayerViewModel viewModel;
  private MultiPlayerInteractor interactor;
  private List<String> firedEvents;

  @BeforeEach
  void setUp() {
    UserSession ses = new UserSession();
    ses.setUser(new User("e", "e"));
    dao = new MultiPlayerDataAccessObject(ses);
    viewModel = new MultiPlayerViewModel();
    MultiPlayerPresenter presenter = new MultiPlayerPresenter(viewModel);
    interactor = new MultiPlayerInteractor(presenter, dao);
    firedEvents = new ArrayList<>();
    viewModel.addPropertyChangeListener(evt -> firedEvents.add(evt.getPropertyName()));
  }

  @Test
  void startGamePublishesFirstQuestion() {
    interactor.startGame(firstDeck(), dao.getSession().getUser(), new User("f", "f"));

    assertEquals(List.of("question"), firedEvents, "Start should emit a question event");
    MultiPlayerGameState state = viewModel.getState();
    assertNotNull(state.getCurrentCard(), "First card should be available");
    assertEquals("What is the capital of France?", state.getCurrentCard().getQuestion());
    assertEquals("Card 1 of 3", state.getMessage());
    assertFalse(state.isGameOver());
  }

  @Test
  void choosingCorrectAnswerUpdatesHostScore() {
    interactor.startGame(firstDeck(), dao.getSession().getUser(), new User("f", "f"));
    firedEvents.clear();

    interactor.chooseAnswer("Paris", true);

    assertEquals(List.of("update"), firedEvents, "Choosing an answer should emit update" );
    MultiPlayerGameState state = viewModel.getState();
    assertEquals(1, state.getScoreA(), "Host score increments on correct answer");
    assertEquals(0, state.getScoreB());
    assertEquals("Correct!", state.getRoundResult());
  }

  @Test
  void advancingPastDeckEndsGame() {
    interactor.startGame(firstDeck(), dao.getSession().getUser(), new User("f", "f"));
    firedEvents.clear();

    interactor.advance();
    assertEquals(List.of("question"), firedEvents, "Advance should show next card");
    assertEquals("Card 2 of 3", viewModel.getState().getMessage());

    firedEvents.clear();
    interactor.advance();
    assertEquals(List.of("question"), firedEvents, "Advance to third card");
    assertEquals("Card 3 of 3", viewModel.getState().getMessage());

    firedEvents.clear();
    interactor.advance();
    assertEquals(List.of("end"), firedEvents, "Deck completion should signal end");
    MultiPlayerGameState state = viewModel.getState();
    assertTrue(state.isGameOver());
    assertEquals("Deck complete", state.getMessage());
  }

  private StudyDeck firstDeck() {
    ArrayList<StudyCard> cards = new ArrayList<>();
    cards.add(new StudyCard("What is the capital of France?",
        new ArrayList<>(List.of("Paris", "Berlin", "Rome", "Madrid")), 0));
    cards.add(new StudyCard("Which city is the capital of Canada?",
        new ArrayList<>(List.of("Toronto", "Vancouver", "Ottawa", "Montreal")), 2));
    cards.add(new StudyCard("What is the capital of Japan?",
        new ArrayList<>(List.of("Osaka", "Seoul", "Kyoto", "Tokyo")), 3));
    return new StudyDeck("World Capitals", "Test your geography recall", cards, 999);
  }
}
