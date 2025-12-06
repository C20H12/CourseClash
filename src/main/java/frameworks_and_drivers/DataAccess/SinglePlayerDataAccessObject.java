/**
 * Data access object for Single Player mode.
 * Loads decks from backend and saves results to leaderboard.
 */

package frameworks_and_drivers.DataAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckLocalDataAccessObject;
import interface_adapter.user_session.UserSession;
import use_case.DataAccessException;
import use_case.SinglePlayer.SinglePlayerAccessInterface;

import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;


public class SinglePlayerDataAccessObject implements SinglePlayerAccessInterface {


    private UserSession session;
    private StudyDeck testDeck;

    public SinglePlayerDataAccessObject(UserSession ses) {
        this.session = ses;
    }

    public boolean existsDeck(String deckTitle) throws DataAccessException {
        final String method = "/api/exists-deck";
        Map<String, String> params = Map.of(
                "deckTitle", deckTitle
        );
        JSONObject response = makeApiRequest("GET", method, params, session.getApiKey());
        return response.optBoolean("exists", false);
    }
    private List<StudyDeck> testDeckList;

    public void setTestDeckList(List<StudyDeck> decks) {
        this.testDeckList = decks;
    }

    @Override
    public List<StudyDeck> getAllDecks() {
        if (testDeckList != null) {
            return testDeckList;
        }
        return new StudyDeckLocalDataAccessObject().getAllDecks();
    }

    @Override
    public StudyDeck loadDeck(String deckTitle) throws DataAccessException {
        if (testDeck != null && testDeck.getTitle().equals(deckTitle)) {
            return testDeck;
        }
        return new StudyDeckLocalDataAccessObject().getDeck(deckTitle);
    }

    public UserSession getSession() {
        return session;
    }

    @Override
    public void saveSinglePlayerResult(String username,
                                       String deckTitle,
                                       int score,
                                       double accuracy,
                                       double avgResponseTime) throws DataAccessException {
        if (testDeck != null) {
            return;
        }

        // final String method = "/api/save-singleplayer-result";

        // Map<String, String> params = new HashMap<>();
        // params.put("username", username);
        // params.put("deckTitle", deckTitle);
        // params.put("score", String.valueOf(score));
        // params.put("accuracy", String.valueOf(accuracy));
        // params.put("avgTime", String.valueOf(avgResponseTime));

        // makeApiRequest("POST", method, params, session.getApiKey());
    }

    public void setTestDeck(StudyDeck deck) {
        this.testDeck = deck;
    }

}
