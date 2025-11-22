// Mahir
package use_case.MultiPlayer.start_match;

import entity.MultiPlayerGame;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.User;
import use_case.DataAccessException;
import use_case.registration.login.LoginUserDataAccessInterface;
import use_case.StudySet.StudySetDataAccessInterface;
import use_case.MultiPlayer.MultiPlayerAccessInterface;

public class MPStartInteractor implements MPStartInputBoundary {
    private final MPStartOutputBoundary presenter;
    private final MultiPlayerAccessInterface gameDataAccess;
    private final LoginUserDataAccessInterface userDataAccess;
    private final StudySetDataAccessInterface deckDataAccess;

    public MPStartInteractor(MPStartOutputBoundary presenter,
                             MultiPlayerAccessInterface gameDataAccess,
                             LoginUserDataAccessInterface userDataAccess,
                             StudySetDataAccessInterface deckDataAccess) {
        this.presenter = presenter;
        this.gameDataAccess = gameDataAccess;
        this.userDataAccess = userDataAccess;
        this.deckDataAccess = deckDataAccess;
    }

    @Override
    public void execute(MPStartInputData inputData) throws DataAccessException {

        StudyDeck deck = deckDataAccess.getSetByName(inputData.getDeckName());


        User player1 = userDataAccess.get(inputData.getPlayerAName());
        User player2 = userDataAccess.get(inputData.getPlayerBName());


        if (player1 == null || player2 == null) {
            presenter.prepareFailView("One of the players does not exist.");
            return;
        }
        if (deck == null) {
            presenter.prepareFailView("Study Deck not found.");
            return;
        }
        if (deck.getCardCount() == 0) {
            presenter.prepareFailView("The selected deck is empty.");
            return;
        }

        MultiPlayerGame game = new MultiPlayerGame(player1, player2, deck);

        gameDataAccess.save(game);

        StudyCard firstCard = deck.getDeck().get(0);

        MPStartOutputData outputData = new MPStartOutputData(
                player1.getUserName(),
                player2.getUserName(),
                firstCard
        );

        presenter.prepareSuccessView(outputData);
    }
}