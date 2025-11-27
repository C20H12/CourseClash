package use_case.SinglePlayer;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import interface_adapter.MultiPlayer.MultiPlayerGameState;
import org.junit.jupiter.api.BeforeEach;
import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.SinglePlayerDataAccessObject;
import interface_adapter.SinglePlayer.SinglePlayerPresenter;
import interface_adapter.SinglePlayer.SinglePlayerState;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import use_case.DataAccessException;
import use_case.SinglePlayer.SinglePlayerInputData;
import interface_adapter.user_session.UserSession;
import org.junit.jupiter.api.Test;

public class SinglePlayerInteractorTest {
    private SinglePlayerDataAccessObject dao;
    private SinglePlayerViewModel viewModel;
    private SinglePlayerInteractor interactor;
    private List<String> firedEvents;
    private SinglePlayerInputData input;

    @BeforeEach
    void setUp(){
        UserSession session = new UserSession();
        session.setUser(new User("tester", "pw"));
        dao = new SinglePlayerDataAccessObject(session);
        viewModel = new SinglePlayerViewModel();
        SinglePlayerPresenter presenter = new SinglePlayerPresenter(viewModel);
        interactor = new SinglePlayerInteractor(presenter, dao);
        firedEvents = new ArrayList<>();
        viewModel.addPropertyChangeListener(evt -> firedEvents.add(evt.getPropertyName()));
    }

    @Test
    void startGamePublishesFirstQuestion() throws Exception {
        StudyDeck deck = firstDeck();
        dao.setTestDeck(deck);  // <-- ensure DAO returns correct deck

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
        //question 2 :
        assertEquals("Which city is the capital of Canada?", state.getQuestionText());
        assertEquals(2, state.getCurrentIndex());
        // INCORRECT ANSWER => decrementScore() → score remains 0
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
        // Submit correct → moves to question 2
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
    void errorwhenDeckMissing() throws Exception{
        dao.setTestDeck(null); // Simulating no deck available/missing

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
        interactor.submitAnswer("Paris");     // Q1
        interactor.submitAnswer("Ottawa");    // Q2
        firedEvents.clear();
        interactor.submitAnswer("Tokyo");     // Q3 → auto end
        assertEquals(List.of("end"), firedEvents);
        SinglePlayerState state = viewModel.getState();
        assertTrue(state.isGameOver());
        assertEquals("Finished", state.getMessage());
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
