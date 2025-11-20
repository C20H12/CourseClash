package use_case.MultiPlayer.start_match;

import entity.MultiPlayerGame;
import entity.StudyCard;
import entity.StudyDeck;
import entity.User;
import use_case.DataAccessException;
import use_case.registration.login.LoginUserDataAccessInterface;
import use_case.StudySet.StudySetDataAccessInterface;
import use_case.MultiPlayer.MultiPlayerAccessInterface; // Ensure this import is correct!

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
        // --- CHECK 1: The User DAO ---
        // If .get() turns red, check LoginUserDataAccessInterface.
        // Is the method named .get(), .getUser(), or .getByName()?
        // Change .get(...) below to match your interface.
        User player1 = userDataAccess.get(inputData.getPlayerAName());
        User player2 = userDataAccess.get(inputData.getPlayerBName());

        // --- CHECK 2: The Deck DAO ---
        // If .getStudySet() turns red, check StudySetDataAccessInterface.
        // Is it named .getDeck(), .getSet(), or something else?
        // Change .getStudySet(...) below to match.
        StudyDeck deck = deckDataAccess.getSetByName(inputData.getDeckId());

        if (player1 == null || player2 == null) {
            presenter.prepareFailView("One of the players does not exist.");
            return;
        }
        if (deck == null) {
            presenter.prepareFailView("Study Deck not found.");
            return;
        }
        if (deck.getCards().isEmpty()) {
            presenter.prepareFailView("The selected deck is empty.");
            return;
        }

        // --- CHECK 3: The Game Constructor ---
        // If new MultiPlayerGame(...) is red, check your Entity file.
        // Does the constructor expect (User, User, StudyDeck)?
        MultiPlayerGame game = new MultiPlayerGame(player1, player2, deck);

        gameDataAccess.save(game);

        StudyCard firstCard = deck.getCards().get(0);

        MPStartOutputData outputData = new MPStartOutputData(
                player1.getUserName(),
                player2.getUserName(),
                firstCard
        );

        presenter.prepareSuccessView(outputData);
    }
}