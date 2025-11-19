// Entity for Study Decks
// Archie
package entity;

import java.util.ArrayList;
import java.util.List;

public class StudyDeck {
    public String title;
    public String description;
    public ArrayList<StudyCard> deck;
    public int id;

    public List<StudyCard> getCards() {
        return deck;
    }
    public String getTitle() {
        return this.title;
    }
}

// todos:
// validate ai generated / manually made decks
// max 25 cards per deck