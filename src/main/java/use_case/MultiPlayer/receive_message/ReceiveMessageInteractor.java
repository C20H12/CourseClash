//Mahir
package use_case.MultiPlayer.receive_message;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.MultiPlayerGame;
import entity.UserFactory;
import frameworks_and_drivers.DataAccess.GameStateSerializer;
import frameworks_and_drivers.DataAccess.NetworkGameDataAccessObject;
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

            StudyDeck deck = deckDAO.getSetByName("Biology");

            MultiPlayerGame game = GameStateSerializer.deserialize(jsonState, userFactory, deck);

            gameDAO.updateLocalGame(game);

            boolean isGameOver = game.isGameOver();
            StudyCard nextCard = isGameOver ? null : game.getCurrentCard();

            SubmitAnswerOutputData output = new SubmitAnswerOutputData(
                    game.getScoreA(),
                    game.getScoreB(),
                    game.getCurrentTurn().getUserName(),
                    nextCard,
                    true,
                    "",
                    isGameOver,
                    game.getPlayerA().getUserName(),
                    game.getPlayerB().getUserName()
            );

            presenter.prepareSuccessView(output);

        } catch (Exception e) {
            System.out.println("Error processing incoming move: " + e.getMessage());
            e.printStackTrace();
        }
    }
}