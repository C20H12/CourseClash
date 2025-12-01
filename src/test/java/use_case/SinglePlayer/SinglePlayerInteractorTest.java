package use_case.SinglePlayer;

import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckLocalDataAccessObject;
import frameworks_and_drivers.DataAccess.SinglePlayerDataAccessObject;
import interface_adapter.SinglePlayer.SinglePlayerPresenter;
import interface_adapter.SinglePlayer.SinglePlayerState;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import interface_adapter.user_session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerInteractorTest {

    private SinglePlayerDataAccessObject dao;
    private SinglePlayerViewModel viewModel;
    private SinglePlayerPresenter presenter;
    private SinglePlayerInteractor interactor;
    private List<String> firedEvents;

    @BeforeEach
    void setUp() {
        UserSession session = new UserSession();
        session.setUser(new User("tester", "pw"));

        dao = new SinglePlayerDataAccessObject(session);
        viewModel = new SinglePlayerViewModel();
        presenter = new SinglePlayerPresenter(viewModel);
        interactor = new SinglePlayerInteractor(presenter, dao);

        firedEvents = new ArrayList<>();
        viewModel.addPropertyChangeListener(evt -> firedEvents.add(evt.getPropertyName()));
    }

    // ---------- Interactor tests ----------

    @Test
    void startGamePublishesFirstQuestion() throws Exception {
        StudyDeck deck = firstDeck();
        dao.setTestDeck(deck);  // ensure DAO returns correct deck

        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                deck.getTitle(),
                10,
                false,
                3
        );

        interactor.startGame(input);

        assertEquals(List.of("question"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertEquals("What is the capital of France?", state.getQuestionText());
        assertEquals(3, state.getTotal());
        assertEquals(1, state.getCurrentIndex());
        assertFalse(state.isGameOver());
    }

    @Test
    void submittingCorrectAnswerMovesToNextQuestion() throws Exception {
        dao.setTestDeck(firstDeck());
        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                firstDeck().getTitle(),
                10,
                false,
                3
        );

        interactor.startGame(input);
        firedEvents.clear(); // reset events after first question

        interactor.submitAnswer("Paris");

        assertEquals(List.of("question"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertEquals("Which city is the capital of Canada?", state.getQuestionText());
        assertEquals(2, state.getCurrentIndex());
        assertFalse(state.isGameOver());
        assertEquals(5, state.getScore());
    }

    @Test
    void submitIncorrectAnswersUpdatesScoreCorrectly() throws Exception {
        dao.setTestDeck(firstDeck());
        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                "World Capitals",
                10,
                false,
                3
        );

        interactor.startGame(input);
        firedEvents.clear(); // reset after first question

        interactor.submitAnswer("Wrong Answer");

        assertEquals(List.of("question"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertEquals("Which city is the capital of Canada?", state.getQuestionText());
        assertEquals(2, state.getCurrentIndex());
        // wrong answer → score stays 0
        assertEquals(0, state.getScore());
        assertFalse(state.isGameOver());
    }

    @Test
    void submitAnswersMovesToNextQuestion() throws Exception {
        dao.setTestDeck(firstDeck());
        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                "World Capitals",
                10,
                false,
                3
        );

        interactor.startGame(input);
        firedEvents.clear();

        interactor.submitAnswer("Paris");

        assertEquals(List.of("question"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertEquals("Which city is the capital of Canada?", state.getQuestionText());
        assertEquals(2, state.getCurrentIndex());
    }

    @Test
    void endingGamePublishesEndEvent() throws Exception {
        dao.setTestDeck(firstDeck());
        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                "World Capitals",
                10,
                false,
                3
        );

        interactor.startGame(input);
        firedEvents.clear();

        interactor.endGame();

        assertEquals(List.of("end"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertTrue(state.isGameOver());
        assertEquals("Ended by user", state.getMessage());
    }

    @Test
    void errorWhenDeckMissing() throws Exception {
        dao.setTestDeck(null); // simulate missing deck

        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                "MissingDeck",
                10,
                false,
                3
        );

        interactor.startGame(input);

        assertEquals(List.of("error"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertEquals("Could not load deck: MissingDeck", state.getError());
    }

    @Test
    void finishingLastQuestionPublishesEndEvent() throws Exception {
        dao.setTestDeck(firstDeck());
        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                "World Capitals",
                10,
                false,
                3
        );

        interactor.startGame(input);
        firedEvents.clear();

        interactor.submitAnswer("Paris");   // Q1
        interactor.submitAnswer("Ottawa");  // Q2
        firedEvents.clear();
        interactor.submitAnswer("Tokyo");   // Q3 → auto end

        assertEquals(List.of("end"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertTrue(state.isGameOver());
        assertEquals("Finished", state.getMessage());
    }

    @Test
    void accuracyIsCalculatedCorrectly() throws Exception {
        dao.setTestDeck(firstDeck());

        SinglePlayerInputData input = new SinglePlayerInputData(
                dao.getSession().getUser(),
                firstDeck().getTitle(),
                10,
                false,
                3
        );

        interactor.startGame(input);
        interactor.submitAnswer("Paris");   // correct
        interactor.submitAnswer("WRONG");   // wrong
        interactor.submitAnswer("Tokyo");   // correct

        SinglePlayerState state = viewModel.getState();
        assertEquals(10, state.getScore());
        assertEquals((2 * 100.0) / 3, state.getAccuracy(), 0.001);
    }

    @Test
    void submitAnswerWhenGameNotStartedShowsError() throws Exception {
        interactor.submitAnswer("Anything");

        SinglePlayerState state = viewModel.getState();
        assertEquals("Game not started.", state.getError());
    }

    @Test
    void endGameWhenNotStartedShowsError() throws Exception {
        interactor.endGame();

        SinglePlayerState state = viewModel.getState();
        assertEquals("Nothing to end.", state.getError());
    }

    @Test
    void startGameFailsOnEmptyDeck() throws Exception {
        StudyDeck emptyDeck = new StudyDeck("Empty", "desc", new ArrayList<>(), 1);
        dao.setTestDeck(emptyDeck);

        interactor.startGame(new SinglePlayerInputData(
                dao.getSession().getUser(), "Empty", 10, false, 5
        ));

        SinglePlayerState state = viewModel.getState();
        assertEquals("Could not load deck: Empty", state.getError());
    }

    @Test
    void finishingGameWithWrongAnswerUpdatesAccuracy() throws Exception {
        dao.setTestDeck(firstDeck());

        User user = dao.getSession().getUser();
        SinglePlayerInputData input = new SinglePlayerInputData(
                user, firstDeck().getTitle(), 10, false, 3
        );

        interactor.startGame(input);

        interactor.submitAnswer("WRONG");
        interactor.submitAnswer("WRONG");
        interactor.submitAnswer("WRONG");

        SinglePlayerState state = viewModel.getState();
        assertEquals(0, state.getScore());
        assertEquals(0.0, state.getAccuracy(), 0.001);
    }

    @Test
    void showAllDeckNamesPublishesDeckList() throws Exception {
        dao.setTestDeck(firstDeck());
        firedEvents.clear();

        interactor.showAllDeckNames();

        assertEquals(List.of("initShowAllDeckNames"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertEquals(3, state.getDecksList().size());
        assertEquals("test", state.getDecksList().get(0).getTitle());
    }

    // ---------- InputData tests ----------

    @Test
    void testInputDataGetters() {
        User user = new User("test", "pass");
        SinglePlayerInputData data = new SinglePlayerInputData(
                user,
                "Deck1",
                15,
                true,
                10
        );

        assertEquals(user, data.getPlayer());
        assertEquals("Deck1", data.getDeckTitle());
        assertEquals(15, data.getTimerPerQuestion());
        assertTrue(data.isShuffle());
        assertEquals(10, data.getNumQuestions());
    }

    // ---------- Presenter tests (for full coverage) ----------

    @Test
    void presentErrorSetsErrorMessage() {
        presenter.presentError("Deck missing");
        SinglePlayerState state = viewModel.getState();
        assertEquals("Deck missing", state.getError());
        assertEquals("Deck missing", state.getMessage());
    }

    @Test
    void presentAllDecksUpdatesState() {
        StudyDeck d = new StudyDeck("Test", "Desc", new ArrayList<>(), 0);
        presenter.presentAllDecks(List.of(d));

        SinglePlayerState state = viewModel.getState();
        assertEquals(1, state.getDecksList().size());
        assertEquals("Test", state.getDecksList().get(0).getTitle());
    }
    @Test
    void showAllDeckNamesHandlesEmptyList() throws Exception {
        dao.setTestDeckList(List.of());  // no decks

        interactor.showAllDeckNames();

        SinglePlayerState state = viewModel.getState();
        assertTrue(state.getDecksList().isEmpty());
    }

    @Test
    void presentResultsPopulatesAllFields() {
        SinglePlayerOutputData out = new SinglePlayerOutputData(
                null,
                null,
                3,
                3,
                15,
                66.7,
                2.5,
                2,
                true,
                "Finished"
        );

        presenter.presentResults(out);

        SinglePlayerState state = viewModel.getState();
        assertEquals(15, state.getScore());
        assertEquals(66.7, state.getAccuracy(), 0.001);
        assertEquals(2.5, state.getAvgResponseTime(), 0.001);
        assertEquals("Finished", state.getMessage());
        assertTrue(state.isGameOver());
    }



    // ---------- Helper ----------

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
