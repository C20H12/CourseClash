package use_case.multiplayer;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import interface_adapter.MultiPlayer.MultiPlayerGameState;
import interface_adapter.MultiPlayer.MultiPlayerPresenter;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.user_session.UserSession;
import use_case.MultiPlayer.MultiPlayerAccessInterface;
import use_case.MultiPlayer.MultiPlayerInteractor;


public class MultiPlayerInteractorTest {

  private InMemoryMultiPlayerGateway gateway;
  private MultiPlayerViewModel viewModel;
  private MultiPlayerInteractor interactor;
  private List<String> firedEvents;

  @BeforeEach
  void setUp() {
    UserSession ses = new UserSession();
    ses.setUser(new User("e", "e"));
    gateway = new InMemoryMultiPlayerGateway(ses);
    viewModel = new MultiPlayerViewModel();
    MultiPlayerPresenter presenter = new MultiPlayerPresenter(viewModel);
    interactor = new MultiPlayerInteractor(presenter, gateway);
    firedEvents = new ArrayList<>();
    viewModel.addPropertyChangeListener(evt -> firedEvents.add(evt.getPropertyName()));
  }

  @Test
  void startGamePublishesFirstQuestion() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("f", "f"));

    assertEquals(List.of("question"), firedEvents, "Start should emit a question event");
    MultiPlayerGameState state = viewModel.getState();
    assertNotNull(state.getCurrentCard(), "First card should be available");
    assertEquals("What is the capital of France?", state.getCurrentCard().getQuestionTitle());
    assertEquals("Card 1 of 3", state.getMessage());
    assertFalse(state.isGameOver());
  }

  @Test
  void choosingCorrectAnswerUpdatesHostScore() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("f", "f"));
    firedEvents.clear();

    interactor.chooseAnswer("Paris", true);

    assertEquals(List.of("submitAnswer"), firedEvents, "Choosing an answer should emit update" );
    MultiPlayerGameState state = viewModel.getState();
    assertEquals(1, state.getScoreA(), "Host score increments on correct answer");
    assertEquals(0, state.getScoreB());
    assertEquals("Correct!", state.getRoundResult());
  }

  @Test
  void choosingCorrectAnswerAsGuestUpdatesGuestScore() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("g", "g"));
    firedEvents.clear();

    interactor.chooseAnswer("Paris", false);

    assertEquals(List.of("submitAnswer"), firedEvents);
    MultiPlayerGameState state = viewModel.getState();
    assertEquals(0, state.getScoreA());
    assertEquals(1, state.getScoreB());
    assertEquals("Correct!", state.getRoundResult());
  }

  @Test
  void choosingAnswerBeforeGameStartsDoesNothing() {
    interactor.chooseAnswer("Paris", true);

    assertTrue(firedEvents.isEmpty());
    assertNull(viewModel.getState().getCurrentCard());
    assertEquals(0, viewModel.getState().getScoreA());
    assertEquals(0, viewModel.getState().getScoreB());
  }

  @Test
  void advancingPastDeckEndsGame() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("f", "f"));
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

  @Test
  void chooseAnswerIgnoredWhenGameIsOver() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("guest", "guest"));
    // triggers end
    for(int i = 0; i < firstDeck().getCardCount(); i++) {
      interactor.advance();
    }
    assertTrue(viewModel.getState().isGameOver(), "Game should be over before retrying answer");
    firedEvents.clear();

    interactor.chooseAnswer("Paris", true);

    assertTrue(firedEvents.isEmpty(), "No events should fire once the game is over");
    assertEquals(0, viewModel.getState().getScoreA());
    assertEquals(0, viewModel.getState().getScoreB());
  }

  @Test
  void choosingAnswerDisplaysIncorrectWhenSolutionIndexOutOfBounds() {
    interactor.startGame(invalidSolutionDeck(), gateway.getSession().getUser(), new User("guest", "guest"));
    firedEvents.clear();

    interactor.chooseAnswer("Any", true);

    assertEquals(List.of("submitAnswer"), firedEvents);
    MultiPlayerGameState state = viewModel.getState();
    assertEquals("Incorrect! The correct answer was: ", state.getRoundResult());
    assertEquals(0, state.getScoreA());
    assertEquals(0, state.getScoreB());

    interactor.advance();
    firedEvents.clear();
    interactor.chooseAnswer("Any", true);
    assertEquals(List.of("submitAnswer"), firedEvents);
    state = viewModel.getState();
    assertEquals("Incorrect! The correct answer was: ", state.getRoundResult());
    assertEquals(0, state.getScoreA());
    assertEquals(0, state.getScoreB());
  }

  @Test
  void updateOtherPlayerScoreWithGuestFlagSetsHostScore() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("guest", "guest"));

    interactor.updateScore(5, 0);
    interactor.endGame();

    assertEquals(5, viewModel.getState().getScoreA());
    assertEquals(0, viewModel.getState().getScoreB());
  }

  @Test
  void updateOtherPlayerScoreIgnoredWhenGameIsOver() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("guest", "guest"));
    interactor.advance();
    interactor.advance();
    interactor.advance(); // end game
    assertTrue(viewModel.getState().isGameOver());

    interactor.updateScore(7, 0);
    interactor.endGame();

    assertEquals(0, viewModel.getState().getScoreA());
    assertEquals(0, viewModel.getState().getScoreB());
  }

  @Test
  void updateOtherPlayerScoreBeforeGameStartsDoesNothing() {
    interactor.updateScore(4, 9);

    assertEquals(0, viewModel.getState().getScoreA());
    assertEquals(0, viewModel.getState().getScoreB());
  }

  @Test
  void endGameSignalsPresenterWithFinalScores() {
    interactor.startGame(firstDeck(), gateway.getSession().getUser(), new User("guest", "guest"));
    firedEvents.clear();

    interactor.endGame();

    assertEquals(List.of("end"), firedEvents);
    MultiPlayerGameState state = viewModel.getState();
    assertTrue(state.isGameOver(), "game ended");
    assertEquals("Host ended the match.", state.getMessage());
    assertEquals("Host ended the match.", state.getRoundResult());
  }

  @Test
  void showAllDecksPublishesAvailableDecks() {
    List<StudyDeck> decks = List.of(firstDeck(), singleCardDeck("Biology"));
    gateway.setDecks(decks);
    firedEvents.clear();

    interactor.showAllDecks();

    assertEquals(List.of("init"), firedEvents);
    assertEquals(decks, viewModel.getState().getAvailableDecks());
  }

  @Test
  void updateOtherPlayerScoreSynchronizesRemoteTotals() {
    User host = gateway.getSession().getUser();
    User guest = new User("guest", "guest");
    interactor.startGame(firstDeck(), host, guest);

    interactor.updateScore(3, 0);  // host tells guest score
    interactor.updateScore(3, 2); // guest tells host score
    firedEvents.clear();

    interactor.endGame();

    MultiPlayerGameState state = viewModel.getState();
    assertEquals(List.of("end"), firedEvents);
    assertEquals(3, state.getScoreA());
    assertEquals(2, state.getScoreB());
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

  private StudyDeck singleCardDeck(String title) {
    ArrayList<StudyCard> cards = new ArrayList<>();
    cards.add(new StudyCard(title + " question",
        new ArrayList<>(List.of("A", "B", "C", "D")), 1));
    return new StudyDeck(title, "desc", cards, title.hashCode());
  }

  private StudyDeck invalidSolutionDeck() {
    ArrayList<StudyCard> cards = new ArrayList<>();
    cards.add(new StudyCard("Impossible question",
        new ArrayList<>(List.of("Option")), 5));
    cards.add(new StudyCard("Impossible question",
        new ArrayList<>(List.of("Option")), -1));
    return new StudyDeck("Invalid", "bad data", cards, 111);
  }

  private static class InMemoryMultiPlayerGateway implements MultiPlayerAccessInterface {
    private final UserSession session;
    private List<StudyDeck> decks = new ArrayList<>();

    private InMemoryMultiPlayerGateway(UserSession session) {
      this.session = session;
    }

    void setDecks(List<StudyDeck> decks) {
      this.decks = decks;
    }

    @Override
    public List<StudyDeck> getAllDecks() {
      return decks;
    }

    @Override
    public UserSession getSession() {
      return session;
    }
  }
}