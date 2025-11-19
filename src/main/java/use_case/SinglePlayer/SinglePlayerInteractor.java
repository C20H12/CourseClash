// Huzaifa - hosting the session
package use_case.SinglePlayer;

import entity.User;
import entity.SinglePlayerGame;
import entity.StudyCard;
import entity.StudyDeck;
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
    public void startGame(SinglePlayerInputData in) {
        final User player = in.getPlayer();
        final StudyDeck deck = in.getDeck();
        if (deck == null || deck.getCards() == null || deck.getCards().isEmpty()) {
            presenter.presentError("Selected deck is empty.");
            return;
        }

        this.game = new SinglePlayerGame(player, deck, in.getTimerPerQuestion(), in.isShuffle());
        this.cards = deck.getCards();

        final int limit = Math.min(in.getNumQuestions(), cards.size());
        if (limit < cards.size()) {
            this.cards = this.cards.subList(0, limit);
        }

        // First question
        final StudyCard first = cards.get(0);
        presenter.presentQuestion(new SinglePlayerOutputData(
                first.getQuestion(), first.getOptions(),
                1, cards.size(),
                0, 0.0, 0.0, false,
                "Game started"
        ));
    }

    @Override
    public void submitAnswer(String answer) {
        if (game == null) {
            presenter.presentError("Game not started.");
            return;
        }
        final StudyCard current = cards.get(idx);
        final boolean correct = current.getAnswer().equalsIgnoreCase(answer);

        if (correct) {
            game.setScore(game.getScore() + 10);
            game.setCorrectAnswers(game.getCorrectAnswers() + 1);
        } else {
            game.setScore(Math.max(0, game.getScore() - 5));
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
                    true,
                    "Finished"
            ));
            gateway.saveSinglePlayerResult(
                    game.getPlayer().getUserName(),
                    game.getStudyDeck().title,
                    game.getScore(),
                    game.getCorrectAnswers() * 100.0 / cards.size(),
                    game.getAverageResponseTime()
            );
        } else {
            final StudyCard next = cards.get(idx);
            presenter.presentQuestion(new SinglePlayerOutputData(
                    next.getQuestion(), next.getOptions(),
                    idx + 1, cards.size(),
                    game.getScore(),
                    game.getCorrectAnswers() * 100.0 / cards.size(),
                    game.getAverageResponseTime(),
                    false,
                    "Next"
            ));
        }
    }
    @Override
    public void endGame() {
        if (game == null) {
            presenter.presentError("Nothing to end.");
            return;
        }
        game.endGame();
        presenter.presentResults(new SinglePlayerOutputData(
                null, null, idx, cards.size(),
                game.getScore(),
                game.getCorrectAnswers() * 100.0 / cards.size(),
                game.getAverageResponseTime(),
                true,
                "Ended by user"
        ));
    }
}
