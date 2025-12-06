/**
 * File: LoginUserDataAccessObject.java
 * Description: The implemented DAO for login actions.
 * Author: Daniel
 */

package frameworks_and_drivers.DataAccess;

import java.util.Map;

import org.json.JSONObject;

import entity.User;
import use_case.DataAccessException;
import use_case.registration.login.LoginUserDataAccessInterface;

public class LoginUserDataAccessObject implements LoginUserDataAccessInterface {

    @Override
    public Map<String, Object> login(String username, String password) throws DataAccessException {
        final String method = "/api/login";
        final String loginApiKey = "6C1BLoves207";

        Map<String, String> params = new java.util.LinkedHashMap<>();
        params.put("username", username);
        params.put("password", password);

        JSONObject response = StaticMethods.makeApiRequest("GET", method, params, loginApiKey);
        // sample response: {"isloggedIn", "status_message", "level", "points", "answered", "correct"}

        boolean isLoggedIn = response.optBoolean("isloggedIn", false);
        String statusMessage = response.optString("status_message", "");
        String apiKey = response.optString("apiKey", "");
        int level = response.optInt("level", 0);
        int points = response.optInt("points", 0);
        int answered = response.optInt("answered", 0);
        int correct = response.optInt("correct", 0);

        User loggedInUser = new User(username, password);
        loggedInUser.setLevel(level);
        loggedInUser.setExperiencePoints(points);
        loggedInUser.setQuestionsAnswered(answered);
        loggedInUser.setQuestionsCorrect(correct);

        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("status", isLoggedIn);
        result.put("status_message", statusMessage);
        result.put("apiKey", apiKey);
        result.put("user", loggedInUser);

        return result;
    }

}