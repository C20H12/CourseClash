//Entity for multiplayer gameplay
//Mahir + luhan updates

package entity;

import java.util.List;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

public class MultiPlayerGame {
    private final User host;
    private final User guest;

    private final StudyDeck deck;
    private int currentCardIndex;
    private int hostScore;
    private int guestScore;

    public MultiPlayerGame(User playerA, User playerB, StudyDeck deck) {
        this.host = playerA;
        this.guest = playerB;
        this.deck = deck;
        this.currentCardIndex = 0;
        this.hostScore = 0;
        this.guestScore = 0;
    }

    public void incrementScoreFor(String username) {
        if (username.equals(host.getUserName())) {
            hostScore++;
        } else if (username.equals(guest.getUserName())) {
            guestScore++;
        }
    }

    public StudyCard getCurrentCard() {
        List<StudyCard> cards = deck.getDeck();
        if (currentCardIndex < cards.size()) {
            return cards.get(currentCardIndex);
        }
        return null;
    }

    public void advanceCard() {
        currentCardIndex++;
    }

    public boolean isGameOver() {
        return currentCardIndex >= deck.getDeck().size();
    }

    public User getHost() {
        return host;
    }

    public User getGuest() {
        return guest;
    }

    public StudyDeck getDeck() {
        return deck;
    }

    public int getHostScore() {
        return hostScore;
    }

    public void setHostScore(int scoreA) {
        this.hostScore = scoreA;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(int scoreB) {
        this.guestScore = scoreB;
    }

    public int getCardIndex() {
        return currentCardIndex;
    }

    public void setCardIndex(int index) {
        this.currentCardIndex = index;
    }

}