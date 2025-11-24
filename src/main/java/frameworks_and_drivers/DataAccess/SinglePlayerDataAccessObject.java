package frameworks_and_drivers.DataAccess;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckLocalDataAccessObject;
import interface_adapter.user_session.UserSession;

import org.json.JSONArray;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.SinglePlayer.SinglePlayerAccessInterface;

import java.util.*;

/**
 * Data access object for Single Player mode.
 * Loads decks from backend and saves results to leaderboard.
 */
import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;

public class SinglePlayerDataAccessObject implements SinglePlayerAccessInterface {


    private UserSession session;   // set when user logs in

    public SinglePlayerDataAccessObject(UserSession ses) {
        this.session = ses;
    }

    // -----------------------------------------------------------
    // 1. Check whether deck exists
    // -----------------------------------------------------------
    @Override
    public boolean existsDeck(String deckTitle) throws DataAccessException {
        final String method = "/api/exists-deck";
        Map<String, String> params = Map.of(
                "deckTitle", deckTitle
        );
        JSONObject response = makeApiRequest("GET", method, params, session.getApiKey());
        return response.optBoolean("exists", false);
    }

    @Override
    public List<StudyDeck> getAllDecks() {
        return new StudyDeckLocalDataAccessObject().getAllDecks();
    }

    // @Override
    // public List<String> getAllDeckTitlesRemote() {
    //     //  {
    //     //   "decks": [
    //     //     { "id": id, "title" : "name", "description":"..."},
    //     //     { "id": id, "title": "name2", "description": "..."}
    //     //   ]
    //     // }
    //     final String method = "/api/get-all-decks";
    //     JSONObject response = makeApiRequest("GET", method, new HashMap<>(), session.getApiKey());
    //     List<String> titles = new ArrayList<>();
    //     JSONArray decks = response.getJSONArray("decks");
    //     for (int i = 0; i < decks.length(); i++) {
    //         JSONObject deck = decks.getJSONObject(i);
    //         titles.add(deck.getString("title"));
    //     }
    //     return titles;
    // }

    // -----------------------------------------------------------
    // 2. Load full StudyDeck from backend
    // -----------------------------------------------------------
    @Override
    public StudyDeck loadDeck(String deckTitle) throws DataAccessException {
        return new StudyDeckLocalDataAccessObject().getDeck(deckTitle);
    }
    // @Override
    // public StudyDeck loadDeckRemote(String deckTitle) throws DataAccessException {
    //     final String method = "/api/get-study-set";
    //     Map<String, String> params = Map.of(
    //             "title", deckTitle
    //     );

    //     JSONObject response = makeApiRequest("GET", method, params, session.getApiKey());

    //     // Expected response format:
    //     // {
    //     //   "title": "...",
    //     //   "description": "...",
    //     //   "id": 12,
    //     //   "cards": [
    //     //       {
    //     //          "question": "...",
    //     //          "answers": ["A", "B", "C", "D"],
    //     //          "solutionId": 2
    //     //       }, ...
    //     //   ]
    //     // }

    //     String title = response.getString("title");
    //     String description = response.optString("description", "");
    //     int id = response.optInt("id", 0);

    //     JSONArray cardArray = response.getJSONArray("cards");
    //     ArrayList<StudyCard> cards = new ArrayList<>();

    //     for (int i = 0; i < cardArray.length(); i++) {
    //         JSONObject c = cardArray.getJSONObject(i);

    //         String question = c.getString("question");

    //         JSONArray answersJSON = c.getJSONArray("answers");
    //         ArrayList<String> answers = new ArrayList<>();
    //         for (int j = 0; j < answersJSON.length(); j++) {
    //             answers.add(answersJSON.getString(j));
    //         }

    //         int solutionId = c.getInt("solutionId");

    //         cards.add(new StudyCard(question, answers, solutionId));
    //     }

    //     return new StudyDeck(title, description, cards, id);
    // }

    // -----------------------------------------------------------
    // 3. Save single-player results to backend
    // -----------------------------------------------------------

    @Override
    public void saveSinglePlayerResult(String username,
                                       String deckTitle,
                                       int score,
                                       double accuracy,
                                       double avgResponseTime) throws DataAccessException {

        final String method = "/api/save-singleplayer-result";

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("deckTitle", deckTitle);
        params.put("score", String.valueOf(score));
        params.put("accuracy", String.valueOf(accuracy));
        params.put("avgTime", String.valueOf(avgResponseTime));

        makeApiRequest("POST", method, params, session.getApiKey());
    }
}
