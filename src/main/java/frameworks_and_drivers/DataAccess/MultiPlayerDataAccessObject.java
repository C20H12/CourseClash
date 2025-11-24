// Multi dao, overhalled
// authored by luhan
package frameworks_and_drivers.DataAccess;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckLocalDataAccessObject;
import interface_adapter.user_session.UserSession;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.MultiPlayer.MultiPlayerAccessInterface;

import java.util.*;

/**
 * Data access object for Multi Player mode.
 * Loads decks from backend and saves results to leaderboard.
 */
import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;

public class MultiPlayerDataAccessObject implements MultiPlayerAccessInterface {


    private UserSession session;   // set when user logs in

    public MultiPlayerDataAccessObject(UserSession ses) {
        this.session = ses;
    }


    @Override
    public List<StudyDeck> getAllDecks() {
        return new StudyDeckLocalDataAccessObject().getAllDecks();
    }

    public UserSession getSession() {
      return session;
    }
}
