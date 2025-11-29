package frameworks_and_drivers.DataAccess;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import entity.User;
import use_case.DataAccessException;

import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;

public class LoginSignupTestDataAccessObject {
    /**
     * Deletes a test user from the system.
     *
     * @param user the user to delete
     * @throws DataAccessException if there is an error accessing the data
     */
    public void delete(User user) throws DataAccessException {
        final String method = "/api/delete-test-user-only";
        final String deleteApiKey = "delete-test-user-only";
        Map<String, String> params = new HashMap<>();
        params.put("username", String.valueOf(user.getUserName()));
        JSONObject response = makeApiRequest("GET", method, params, deleteApiKey);
        response.getString("status_message");
    }

    /**
     * Creates a new test user in the system.
     *
     * @param user the user to create
     * @return the status message from the API
     * @throws DataAccessException if there is an error accessing the data
     */
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
