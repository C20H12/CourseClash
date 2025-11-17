/// File: DBStudySetDataAccessObject.java
/// Description: Implement Study Set Data Access Interface
/// Wrote by Daniel

package data_access;
import java.util.HashMap;

import entity.StudyDeck;
import org.json.JSONArray;
import org.json.JSONObject;

import use_case.DataAccessException;
import use_case.StudySet.StudySetDataAccessInterface;

import static data_access.StaticMethods.makeApiRequest;


// Example: https://github.com/CSC207-2025F-UofT/NoteApplication/blob/main/src/main/java/data_access/DBNoteDataAccessObject.java#L15

public class DBStudySetDataAccessObject implements StudySetDataAccessInterface {

    private final String apiKey;

    public DBStudySetDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String testAPIConnection() throws DataAccessException {
        String test_api_key = "abc123";
        JSONObject responseJSON = makeApiRequest("get", "/test-api",null, test_api_key);
        return "Successfully connected to API Server! Message: " + responseJSON.getString("message");
    }

    @Override
    public HashMap<String, Integer> getAllSetNameAndID() throws DataAccessException {
        final String method = "/api/get-all-study-set-name-and-id";
        JSONObject responseJSON = makeApiRequest("get", method, null, apiKey);
        HashMap<String, Integer> deck = new HashMap<>();
        JSONArray titles = responseJSON.getJSONArray("titles");
        for (int i = 0; i < titles.length(); i++) {
            JSONObject pair = titles.getJSONObject(i);
            String title = pair.keys().next();
            int id = pair.getInt(title);
            deck.put(title, id);
        }
        return deck;
    }

    @Override
    public StudyDeck getSetByName(String setName) throws DataAccessException {
        return null;
    }

}

