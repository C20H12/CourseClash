//Mahir
package frameworks_and_drivers.DataAccess;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import use_case.DataAccessException;
import use_case.StudySet.StudySetDataAccessInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InMemoryStudySetDataAccessObject implements StudySetDataAccessInterface {

    @Override
    public String testAPIConnection() throws DataAccessException { return "Mock Connection Success"; }

    @Override
    public HashMap<String, Integer> getAllSetNameAndID() throws DataAccessException {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Biology", 1);
        return map;
    }

    @Override
    public StudyDeck getSetByName(String setName) throws DataAccessException {
        // Card 1
        ArrayList<String> ans1 = new ArrayList<>(Arrays.asList("Mitochondria", "Nucleus", "67", "Engine"));
        StudyCard c1 = new StudyCard("Powerhouse of cell?", ans1, 0);

        // Card 2
        ArrayList<String> ans2 = new ArrayList<>(Arrays.asList("H2O", "O2", "NaCl", "NaOH"));
        StudyCard c2 = new StudyCard("Chemical formula for water?", ans2, 0);

        ArrayList<StudyCard> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);

        return new StudyDeck("Biology", "Biology", cards, 1);
    }

    public StudyDeck getStudySet(String name) throws DataAccessException { return getSetByName(name); }
}