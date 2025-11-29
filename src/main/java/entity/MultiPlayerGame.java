//Entity for multiplayer gameplay
//Mahir + luhan updates

package entity;

import java.util.List;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

/**
 * Represents a multiplayer game session between a host and a guest.
 * Manages the state of the game including players, scores, and deck progress.
 */
public class MultiPlayerGame {
    private final User host;
    private final User guest;

    private final StudyDeck deck;
    private int currentCardIndex;
    private int hostScore;
    private int guestScore;

    /**
     * Constructs a new MultiPlayerGame session.
     * @param playerA The user acting as the host.
     * @param playerB The user acting as the guest.
     * @param deck The StudyDeck to be used for the game.
     */
    public MultiPlayerGame(User playerA, User playerB, StudyDeck deck) {
        this.host = playerA;
        this.guest = playerB;
        this.deck = deck;
        this.currentCardIndex = 0;
        this.hostScore = 0;
        this.guestScore = 0;
    }

    /**
     * Increments the score for the specified user based on their username.
     * @param username The username of the player who answered correctly.
     */
    public void incrementScoreFor(String username) {
        if (username.equals(host.getUserName())) {
            hostScore++;
        } else if (username.equals(guest.getUserName())) {
            guestScore++;
        }
    }

    /**
     * Retrieves the card currently being played based on the index.
     * @return The current StudyCard, or null if the index is out of bounds.
     */
    public StudyCard getCurrentCard() {
        List<StudyCard> cards = deck.getDeck();
        if (currentCardIndex < cards.size()) {
            return cards.get(currentCardIndex);
        }
        return null;
    }

    /**
     * Advances the game to the next card index.
     */
    public void advanceCard() {
        currentCardIndex++;
    }

    /**
     * Checks if the game has finished by comparing the index to the deck size.
     * @return True if all cards have been played, false otherwise.
     */
    public boolean isGameOver() {
        return currentCardIndex >= deck.getDeck().size();
    }

    /**
     * Retrieves the host user.
     * @return The User object representing the host.
     */
    public User getHost() {
        return host;
    }

    /**
     * Retrieves the guest user.
     * @return The User object representing the guest.
     */
    public User getGuest() {
        return guest;
    }

    /**
     * Retrieves the deck being used in this game.
     * @return The StudyDeck object.
     */
    public StudyDeck getDeck() {
        return deck;
    }

    /**
     * Retrieves the current score of the host.
     * @return The integer score of the host.
     */
    public int getHostScore() {
        return hostScore;
    }

    /**
     * Manually sets the host's score (e.g., for synchronization).
     * @param scoreA The new score for the host.
     */
    public void setHostScore(int scoreA) {
        this.hostScore = scoreA;
    }

    /**
     * Retrieves the current score of the guest.
     * @return The integer score of the guest.
     */
    public int getGuestScore() {
        return guestScore;
    }

    /**
     * Manually sets the guest's score (e.g., for synchronization).
     * @param scoreB The new score for the guest.
     */
    public void setGuestScore(int scoreB) {
        this.guestScore = scoreB;
    }

    /**
     * Retrieves the current index of the card being played.
     * @return The integer index of the current card.
     */
    public int getCardIndex() {
        return currentCardIndex;
    }

    /**
     * Manually sets the card index (e.g., for synchronization or restoration).
     * @param index The new card index to set.
     */
    public void setCardIndex(int index) {
        this.currentCardIndex = index;
    }

}