/**
 * File: LoginUserDataAccessObject.java
 * Description: The implemented DAO for login actions.
 * Author: Daniel
 */
package frameworks_and_drivers.DataAccess;

import entity.User;
import use_case.DataAccessException;
import use_case.registration.login.LoginUserDataAccessInterface;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;


public class LoginUserDataAccessObject implements LoginUserDataAccessInterface {

    private String currentUsername; // REQUIRED for getCurrentUsername()

    @Override
    public HashMap<String, Object> login(String username, String password) throws DataAccessException {
        final String method = "/api/login";
        final String loginApiKey = "6C1BLoves207"; // Your team's API key
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject response = makeApiRequest("GET", method, params, loginApiKey);

        boolean isLoggedIn   = response.optBoolean("isloggedIn", false);
        // ... (other parsing logic) ...
        User loggedInUser = new User(username, password);

        // ... (set user details) ...

        if (isLoggedIn) {
            this.currentUsername = username; // Set session after successful login
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("status", isLoggedIn);
        // ... (put other results) ...
        result.put("user", loggedInUser);

        return result;
    }

    // --- METHODS ADDED TO SATISFY INTERFACE CONTRACT ---

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public User get(String username) {
        // CRUCIAL FOR MPSTARTINTERACTOR: Since this is an API DAO,
        // we assume the user must be fetched via another API call or is not locally cached.
        // Returning null for simplicity, expecting Interactor to handle it.
        return null;
    }

    @Override
    public boolean existsByName(String identifier) {
        // Placeholder check for compilation
        return true;
    }

    // Note: If your interface requires save(User user), you must add it here as well.
}