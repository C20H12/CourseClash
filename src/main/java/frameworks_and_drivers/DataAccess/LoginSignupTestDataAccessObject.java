package frameworks_and_drivers.DataAccess;

import entity.User;
import org.json.JSONObject;
import use_case.DataAccessException;

import java.util.HashMap;
import java.util.Map;

import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;

public class LoginSignupTestDataAccessObject {

    public void delete(User user) throws DataAccessException {
        final String method = "/api/delete-test-user-only";
        final String deleteApiKey = "delete-test-user-only";
        Map<String, String> params = new HashMap<>();
        params.put("username", String.valueOf(user.getUserName()));
        JSONObject response = makeApiRequest("GET", method, params, deleteApiKey);
        response.getString("status_message");
    }

    public String create(User user) throws DataAccessException {
        final String method = "/api/signup";
        final String signupApiKey = "6C1BLovesCS";
        Map<String, String> params = new HashMap<>();
        params.put("username", String.valueOf(user.getUserName()));
        params.put("password", String.valueOf(user.getPassword()));
        params.put("level", String.valueOf(user.getLevel()));
        params.put("points", String.valueOf(user.getExperiencePoints()));
        params.put("answered", String.valueOf(user.getQuestionsAnswered()));
        params.put("correct", String.valueOf(user.getQuestionsCorrect()));
        JSONObject response = makeApiRequest("GET", method, params, signupApiKey);
        return response.getString("status_message");
    }
}
