//Mahir
package frameworks_and_drivers.DataAccess;

import entity.User;
import entity.UserFactory;
import use_case.DataAccessException;
import use_case.registration.login.LoginUserDataAccessInterface;
import use_case.registration.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDataAccessObject implements LoginUserDataAccessInterface, SignupUserDataAccessInterface {
    private final Map<String, User> accounts = new HashMap<>();
    private String currentUsername;

    public InMemoryUserDataAccessObject(UserFactory userFactory) {
        User u1 = userFactory.create("TestUser", "password123");
        User u2 = userFactory.create("Opponent", "password123");

        initializeUser(u1);
        initializeUser(u2);

        accounts.put(u1.getUserName(), u1);
        accounts.put(u2.getUserName(), u2);
    }

    private void initializeUser(User u) {
        u.setLevel(1);
        u.setExperiencePoints(0);
        u.setQuestionsAnswered(0);
        u.setQuestionsCorrect(0);
    }


    @Override
    public String create(User user) throws DataAccessException {
        if (accounts.containsKey(user.getUserName())) {
            return "username taken";
        }
        accounts.put(user.getUserName(), user);
        return "success";
    }


    @Override
    public HashMap<String, Object> login(String username, String password) throws DataAccessException {
        HashMap<String, Object> result = new HashMap<>();

        if (accounts.containsKey(username)) {
            User user = accounts.get(username);
            if (user.getPassword().equals(password)) {
                result.put("status", true);
                result.put("user", user);
                result.put("status_message", "Login successful (InMemory)");
                result.put("apiKey", "test-key");
                return result;
            }
        }

        result.put("status", false);
        result.put("status_message", "Invalid credentials");
        return result;
    }

    public void save(User user) {
        accounts.put(user.getUserName(), user);
    }

    public User get(String username) {
        return accounts.get(username);
    }

    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }
}