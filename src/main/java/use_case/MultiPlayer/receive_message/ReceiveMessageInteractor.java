package use_case.MultiPlayer.receive_message;

import frameworks_and_drivers.DataAccess.GameStateSerializer;
import frameworks_and_drivers.DataAccess.NetworkGameDataAccessObject;
import entity.MultiPlayerGame;
import entity.UserFactory;
import entity.DeckManagement.StudyDeck;
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputBoundary; // Reuse the presenter!
import use_case.MultiPlayer.submit_answer.SubmitAnswerOutputData;
import use_case.StudySet.StudySetDataAccessInterface;

public class ReceiveMessageInteractor implements ReceiveMessageInputBoundary {
    private final NetworkGameDataAccessObject gameDAO;
    private final SubmitAnswerOutputBoundary presenter;
    private final UserFactory userFactory;
    private final StudySetDataAccessInterface deckDAO; // Needed to reconstruct game

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
            // 1. We need the Deck to reconstruct the game.
            // Simplify: Assume we are using the "Biology" deck for this test.
            StudyDeck deck = deckDAO.getSetByName("Biology");

            // 2. Convert JSON -> Game Object
            MultiPlayerGame game = GameStateSerializer.deserialize(jsonState, userFactory, deck);

            // 3. Update the DAO so our local state matches opponent
            gameDAO.updateLocalGame(game);

            // 4. Update the View (Reuse existing Output Data logic)
            boolean isGameOver = game.isGameOver();
            entity.DeckManagement.StudyCard nextCard = isGameOver ? null : game.getCurrentCard();

            SubmitAnswerOutputData output = new SubmitAnswerOutputData(
                    game.getScoreA(),
                    game.getScoreB(),
                    game.getCurrentTurn().getUserName(),
                    nextCard,
                    true, // Assume correct for UI update purposes
                    "",
                    isGameOver
            );

            presenter.prepareSuccessView(output);

        } catch (Exception e) {
            System.out.println("Error processing incoming move: " + e.getMessage());
        }
    }
}
