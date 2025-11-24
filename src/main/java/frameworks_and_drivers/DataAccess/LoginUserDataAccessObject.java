/**
 * File: LoginUserDataAccessObject.java
 * Description: The implemented DAO for login actions.
 * Author: Daniel
 */
package frameworks_and_drivers.DataAccess;

import entity.User;

import org.json.JSONObject;
import use_case.DataAccessException;

import use_case.registration.login.LoginUserDataAccessInterface;
import java.util.HashMap;
import java.util.Map;

import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;


public class LoginUserDataAccessObject implements LoginUserDataAccessInterface {

    @Override
    public HashMap<String, Object> login(String username, String password) throws DataAccessException {
        final String method = "/api/login";
        final String loginApiKey = "6C1BLoves207";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject response = makeApiRequest("GET", method, params, loginApiKey);
        // sample response: {"isloggedIn" "status_message" "level" "points" "answered" "correct"}

        boolean isLoggedIn   = response.optBoolean("isloggedIn", false);
        String statusMessage = response.optString("status_message", "");
        String apiKey        = response.optString("apiKey", "");
        int level            = response.optInt("level", 0);
        int points           = response.optInt("points", 0);
        int answered         = response.optInt("answered", 0);
        int correct          = response.optInt("correct", 0);
        User loggedInUser = new User(username, password);
        loggedInUser.setLevel(level);
        loggedInUser.setExperiencePoints(points);
        loggedInUser.setQuestionsAnswered(answered);
        loggedInUser.setQuestionsCorrect(correct);

        HashMap<String, Object> result = new HashMap<>();
        result.put("status", isLoggedIn);
        result.put("status_message", statusMessage);
        result.put("apiKey", apiKey);
        result.put("user", loggedInUser);

        return result;
    }
}