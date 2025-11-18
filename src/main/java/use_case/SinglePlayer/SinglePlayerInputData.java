//huzaifa - player, deck, timer, shuffle, numQuestions
package use_case.SinglePlayer;

import entity.User;
import entity.DeckManagement.StudyDeck;

public class SinglePlayerInputData {
    private final User player;
    private final StudyDeck deck;
    private final int timerPerQuestion;
    private final boolean shuffle;
    private final int numQuestions;

    public SinglePlayerInputData(User player, StudyDeck deck, int timerPerQuestion, boolean shuffle, int numQuestions) {
        this.player = player;
        this.deck = deck;
        this.timerPerQuestion = timerPerQuestion;
        this.shuffle = shuffle;
        this.numQuestions = numQuestions;
    }

    public User getPlayer() { return player; }
    public StudyDeck getDeck() { return deck; }
    public int getTimerPerQuestion() { return timerPerQuestion; }
    public boolean isShuffle() { return shuffle; }
    public int getNumQuestions() { return numQuestions; }
}

