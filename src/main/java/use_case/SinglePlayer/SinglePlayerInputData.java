//huzaifa - player, deck, timer, shuffle, numQuestions
package use_case.SinglePlayer;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.User;

public class SinglePlayerInputData {
    private final User player;
    private final String deckTitle;
    private final int timerPerQuestion;
    private final boolean shuffle;
    private final int numQuestions;

    public SinglePlayerInputData(User player, String deckTitle,
                                 int timerPerQuestion, boolean shuffle, int numQuestions) {
        this.player = player;
        this.deckTitle = deckTitle;
        this.timerPerQuestion = timerPerQuestion;
        this.shuffle = shuffle;
        this.numQuestions = numQuestions;
    }

    public User getPlayer() { return player; }
    public String getDeckTitle() { return deckTitle; }
    public int getTimerPerQuestion() { return timerPerQuestion; }
    public boolean isShuffle() { return shuffle; }
    public int getNumQuestions() { return numQuestions; }
}

