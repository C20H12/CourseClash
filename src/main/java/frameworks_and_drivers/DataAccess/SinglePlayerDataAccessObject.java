package frameworks_and_drivers.DataAccess;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
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

    private String apiKey = "";   // set when user logs in

    public SinglePlayerDataAccessObject() {
        // empty constructor; apiKey set later
    }

    public void setApiKey(String key) {
        this.apiKey = key;
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
        JSONObject response = makeApiRequest("GET", method, params, apiKey);
        return response.optBoolean("exists", false);
    }

    // -----------------------------------------------------------
    // 2. Load full StudyDeck from backend
    // -----------------------------------------------------------

    @Override
    public StudyDeck loadDeck(String deckTitle) throws DataAccessException {
        final String method = "/api/get-study-set";
        Map<String, String> params = Map.of(
                "title", deckTitle
        );

        JSONObject response = makeApiRequest("GET", method, params, apiKey);

        // Expected response format:
        // {
        //   "title": "...",
        //   "description": "...",
        //   "id": 12,
        //   "cards": [
        //       {
        //          "question": "...",
        //          "answers": ["A", "B", "C", "D"],
        //          "solutionId": 2
        //       }, ...
        //   ]
        // }

        String title = response.getString("title");
        String description = response.optString("description", "");
        int id = response.optInt("id", 0);

        JSONArray cardArray = response.getJSONArray("cards");
        ArrayList<StudyCard> cards = new ArrayList<>();

        for (int i = 0; i < cardArray.length(); i++) {
            JSONObject c = cardArray.getJSONObject(i);

            String question = c.getString("question");

            JSONArray answersJSON = c.getJSONArray("answers");
            ArrayList<String> answers = new ArrayList<>();
            for (int j = 0; j < answersJSON.length(); j++) {
                answers.add(answersJSON.getString(j));
            }

            int solutionId = c.getInt("solutionId");

            cards.add(new StudyCard(question, answers, solutionId));
        }

        return new StudyDeck(title, description, cards, id);
    }

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

        makeApiRequest("POST", method, params, apiKey);
    }
}
