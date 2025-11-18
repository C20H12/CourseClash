package data_access;

import entity.User;

import org.json.JSONObject;

import use_case.DataAccessException;
import use_case.registration.login.LoginUserDataAccessInterface;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

import static data_access.StaticMethods.makeApiRequest;


public class LoginUserDataAccessObject implements LoginUserDataAccessInterface {

    private String apiKey;
    private String currentUser;
    public LoginUserDataAccessObject() {
        // No need to pass in api_key as we will always stay here
    }

    @Override
    public boolean existsByName(String username) throws DataAccessException {
        final String method = "/api/exists-user";
        Map<String, String> params = Map.of("username", username);
        JSONObject response = makeApiRequest("GET", method, params, apiKey);
        return response.getBoolean("exists");
    }

    @Override
    public void save(User user) throws DataAccessException {
        final String method = "/api/update-user-data";
        Map<String, String> params = Map.of(
                "username", user.getUserName(),
                "level", String.valueOf(user.getLevel()),
                "points", String.valueOf(user.getExperiencePoints()),
                "answered", String.valueOf(user.getQuestionsAnswered()),
                "correct", String.valueOf(user.getQuestionsCorrect())
        );
        makeApiRequest("POST", method, params, apiKey);
    }


    @Override
    public User get(String username) throws DataAccessException {
        final String method = "/api/get-user";
        Map<String, String> params = Map.of("username", username);
        JSONObject response = makeApiRequest("GET", method, params, apiKey);

        User result = new User(username, response.getString("password"));
        result.setLevel(response.getInt("points"));
        result.setExperiencePoints(response.getInt("points"));
        result.setQuestionsAnswered(response.getInt("answered"));
        result.setQuestionsCorrect(response.getInt("correct"));
        return result;
    }


    @Override
    public void setCurrentUsername(String name) {
        this.currentUser = name;

    }

    @Override
    public String getCurrentUsername() {
        return this.currentUser;
    }

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
