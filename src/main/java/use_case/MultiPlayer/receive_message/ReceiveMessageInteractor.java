package use_case.MultiPlayer.receive_message;

import frameworks_and_drivers.DataAccess.GameStateSerializer;
import frameworks_and_drivers.DataAccess.NetworkGameDataAccessObject;
import entity.MultiPlayerGame;
import entity.UserFactory;
import entity.DeckManagement.StudyDeck;
import entity.DeckManagement.StudyCard;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputBoundary;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputData;
import use_case.StudySet.StudySetDataAccessInterface;

public class ReceiveMessageInteractor implements ReceiveMessageInputBoundary {
    private final NetworkGameDataAccessObject gameDAO;
    private final SubmitAnswerOutputBoundary presenter;
    private final UserFactory userFactory;
    private final StudySetDataAccessInterface deckDAO;

    public ReceiveMessageInteractor(NetworkGameDataAccessObject gameDAO,
                                    SubmitAnswerOutputBoundary presenter,
                                    UserFactory userFactory,
                                    StudySetDataAccessInterface deckDAO) {
        this.gameDAO = gameDAO;
        this.presenter = presenter;
        this.userFactory = userFactory;
        this.deckDAO = deckDAO;
    }

    @Override
    public void execute(String jsonState) {
        try {
            System.out.println("NETWORK: Processing incoming game state...");

            // 1. Retrieve the Deck
            // (Hardcoded "Biology" for this test phase, ideally passed in JSON)
            StudyDeck deck = deckDAO.getSetByName("Biology");

            // 2. Deserialize JSON -> MultiPlayerGame Object
            MultiPlayerGame game = GameStateSerializer.deserialize(jsonState, userFactory, deck);

            // 3. Update Local Cache so we are in sync
            gameDAO.updateLocalGame(game);

            // 4. Prepare Output for View
            boolean isGameOver = game.isGameOver();
            StudyCard nextCard = isGameOver ? null : game.getCurrentCard();

            // We assume the incoming move was valid/correct for UI update purposes
            SubmitAnswerOutputData output = new SubmitAnswerOutputData(
                    game.getScoreA(),
                    game.getScoreB(),
                    game.getCurrentTurn().getUserName(),
                    nextCard,
                    true,
                    "",
                    isGameOver,
                    game.getPlayerA().getUserName(), // Sync names to Guest View
                    game.getPlayerB().getUserName()
            );

            presenter.prepareSuccessView(output);

        } catch (Exception e) {
            System.out.println("Error processing incoming move: " + e.getMessage());
            e.printStackTrace();
        }
    }
}