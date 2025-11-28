// Huzaifa - hosting the session
package use_case.SinglePlayer;

import entity.User;
import entity.SinglePlayerGame;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import use_case.DataAccessException;

import java.util.List;

public class SinglePlayerInteractor implements SinglePlayerInputBoundary {
    private final SinglePlayerOutputBoundary presenter;
    private final SinglePlayerAccessInterface gateway;
    private SinglePlayerGame game;
    private List<StudyCard> cards;
    private int idx = 0;

    public SinglePlayerInteractor(SinglePlayerOutputBoundary presenter,
                                  SinglePlayerAccessInterface gateway) {
        this.presenter = presenter;
        this.gateway = gateway;
    }
    @Override
    public void startGame(SinglePlayerInputData in) throws DataAccessException {
        final User player = in.getPlayer();
        String deckTitle = in.getDeckTitle();
        StudyDeck deck = gateway.loadDeck(deckTitle);
        if (deck == null || deck.isEmpty()) {
            presenter.presentError("Could not load deck: " + deckTitle);
            return;
        }
        this.game = new SinglePlayerGame(player, deck, in.getTimerPerQuestion(), in.isShuffle());
        this.game.startGame();
        this.idx = 0;
        this.cards = deck.getDeck();
        final int limit = Math.min(in.getNumQuestions(), cards.size());
        if (limit < cards.size()) {
            this.cards = this.cards.subList(0, limit);
        }
        // First question
        final StudyCard first = cards.get(0);
        presenter.presentQuestion(new SinglePlayerOutputData(
                first.getQuestion(), first.getAnswers(),
                1, cards.size(),
                game.getScore(),
                (game.getCorrectAnswers() * 100.0) / (idx + 1),
                game.getAverageResponseTime(), game.getCorrectAnswers(), false,
                "Game started"
        ));
    }
    @Override
    public void submitAnswer(String answer) throws DataAccessException {
        if (game == null) {
            presenter.presentError("Game not started.");
            return;
        }
        final StudyCard current = cards.get(idx);
        boolean correct = current.getAnswers().get(current.getSolutionId()).equalsIgnoreCase(answer);
        if (correct) {
            game.incrementScoreCorrect();
        }
        idx++;
        final boolean finished = idx >= cards.size();
        if (finished) {
            game.endGame();
            presenter.presentResults(new SinglePlayerOutputData(
                    null, null, idx, cards.size(),
                    game.getScore(),
                    game.getCorrectAnswers() * 100.0 / cards.size(),
                    game.getAverageResponseTime(),
                    game.getCorrectAnswers(),
                    true,
                    "Finished"
            ));
            gateway.saveSinglePlayerResult(
                    game.getPlayer().getUserName(),
                    game.getStudyDeck().getTitle(),
                    game.getScore(),
                    game.getCorrectAnswers() * 100.0 / cards.size(),
                    game.getAverageResponseTime()
            );
        } else {
            double accuracy = (game.getCorrectAnswers() * 100.0) / (idx);
            final StudyCard next = cards.get(idx);
            presenter.presentQuestion(new SinglePlayerOutputData(
                    next.getQuestion(), next.getAnswers(),
                    idx + 1, cards.size(),
                    game.getScore(),
                    accuracy,
                    game.getAverageResponseTime(),
                    game.getCorrectAnswers(), false,
                    "Next"
            ));
        }
    }
    @Override
    public void endGame() throws DataAccessException {
        if (game == null) {
            presenter.presentError("Nothing to end.");
            return;
        }
        game.endGame();
        double accuracy = game.getCorrectAnswers() * 100.0 / cards.size();
        gateway.saveSinglePlayerResult(
                game.getPlayer().getUserName(),
                game.getStudyDeck().getTitle(),
                game.getScore(),
                accuracy,
                game.getAverageResponseTime()
        );
        presenter.presentResults(new SinglePlayerOutputData(
                null, null, idx, cards.size(),
                game.getScore(),
                game.getCorrectAnswers() * 100.0 / cards.size(),
                game.getAverageResponseTime(),
                game.getCorrectAnswers(), true,
                "Ended by user"
        ));
    }

    @Override
    public void showAllDeckNames() {
        List<StudyDeck> decks = gateway.getAllDecks();
        presenter.presentAllDecks(decks);
    }
}
