package frameworks_and_drivers.DataAccess;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import entity.User;
import use_case.DataAccessException;
import use_case.registration.signup.SignupUserDataAccessInterface;

public class SignupUserDataAccessObject implements SignupUserDataAccessInterface {

    @Override
    public String create(User user) throws DataAccessException {
        // final String method = "/api/signup";
        // final String signupApiKey = "6C1BLovesCS";
        // Map<String, String> params = new HashMap<>();
        // params.put("username", String.valueOf(user.getUserName()));
        // params.put("password", String.valueOf(user.getPassword()));
        // params.put("level", String.valueOf(user.getLevel()));
        // params.put("points", String.valueOf(user.getExperiencePoints()));
        // params.put("answered", String.valueOf(user.getQuestionsAnswered()));
        // params.put("correct", String.valueOf(user.getQuestionsCorrect()));
        // JSONObject response = StaticMethods.makeApiRequest("GET", method, params, signupApiKey);
        // return response.getString("status_message");
        return "success";
    }
}
