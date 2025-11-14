package data_access;

import entity.User;
import entity.UserFactory;

import use_case.registration.login.LoginUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * DAO for user data implemented using an in-memory Map (dummy mode for testing).
 */
public class FileUserDataAccessObject implements LoginUserDataAccessInterface {

    private final Map<String, User> accounts = new HashMap<>();
    private String currentUsername;

    /**
     * Dummy version constructor: ignores csvPath and loads hardcoded sample users.
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        // Dummy test users
        User u1 = userFactory.create("alice", "1234");
        User u2 = userFactory.create("bob", "abcd");
        User u3 = userFactory.create("test", "test");

        accounts.put(u1.getUserName(), u1);
        accounts.put(u2.getUserName(), u2);
        accounts.put(u3.getUserName(), u3);
    }

    @Override
    public void save(User user) {
        // Simply replace or add in the dummy map
        accounts.put(user.getUserName(), user);
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }
}
