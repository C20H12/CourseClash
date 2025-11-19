package data_access;

import entity.User;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.registration.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

import static data_access.StaticMethods.makeApiRequest;

public class SignupUserDataAccessObject implements SignupUserDataAccessInterface {

//    @Override
//    public boolean existsByName(String username) throws DataAccessException {
//        final String method = "/api/exists-user";
//        Map<String, String> params = Map.of("username", username);
//        JSONObject response = makeApiRequest("GET", method, params, apiKey);
//        return response.getBoolean("exists");
//    }

//    @Override
//    public void save(User user) throws DataAccessException {
//        final String method = "/api/update-user-data";
//        Map<String, String> params = Map.of(
//                "username", user.getUserName(),
//                "level", String.valueOf(user.getLevel()),
//                "points", String.valueOf(user.getExperiencePoints()),
//                "answered", String.valueOf(user.getQuestionsAnswered()),
//                "correct", String.valueOf(user.getQuestionsCorrect())
//        );
//        makeApiRequest("POST", method, params, apiKey);
//    }


    @Override
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
