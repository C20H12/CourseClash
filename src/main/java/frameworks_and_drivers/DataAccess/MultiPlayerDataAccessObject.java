// Multi dao, overhalled
// authored by luhan

package frameworks_and_drivers.DataAccess;

import java.util.List;

import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckLocalDataAccessObject;
import interface_adapter.user_session.UserSession;
import use_case.MultiPlayer.MultiPlayerAccessInterface;

public class MultiPlayerDataAccessObject implements MultiPlayerAccessInterface {

    // set when user logs in
    private UserSession session;

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
