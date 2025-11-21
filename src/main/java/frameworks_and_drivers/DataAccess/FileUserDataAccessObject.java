package frameworks_and_drivers.DataAccess;

import entity.User;
import entity.UserFactory;

import use_case.DataAccessException; // Added this import
import use_case.registration.login.LoginUserDataAccessInterface;
import use_case.registration.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * DAO for user data implemented using an in-memory Map (dummy mode for testing).
 */
//public class FileUserDataAccessObject implements LoginUserDataAccessInterface, SignupUserDataAccessInterface {
//
//    private final Map<String, User> accounts = new HashMap<>();
//    private String currentUsername;
//
//    /**
//     * Dummy version constructor: ignores csvPath and loads hardcoded sample users.
//     */
//    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {
//        // Dummy test users
//        User u1 = userFactory.create("alice", "1234");
//        User u2 = userFactory.create("bob", "abcd");
//        User u3 = userFactory.create("test", "test");
//
//        // Add the TestUser you need for Multiplayer
//        User u4 = userFactory.create("TestUser", "password123");
//        User u5 = userFactory.create("Opponent", "password123");
//
//        accounts.put(u1.getUserName(), u1);
//        accounts.put(u2.getUserName(), u2);
//        accounts.put(u3.getUserName(), u3);
//        accounts.put(u4.getUserName(), u4);
//        accounts.put(u5.getUserName(), u5);
//    }
//
//    public void save(User user) {
//        accounts.put(user.getUserName(), user);
//    }
//
//    @Override
//    public User get(String username) {
//        return accounts.get(username);
//    }
//
//    // --- NEW MISSING METHOD ---
//    @Override
//    public HashMap<String, Object> login(String username, String password) throws DataAccessException {
//        HashMap<String, Object> result = new HashMap<>();
//        if (accounts.containsKey(username)) {
//            User user = accounts.get(username);
//            if (user.getPassword().equals(password)) {
//                result.put("status", true);
//                result.put("user", user);
//                result.put("status_message", "Login successful (Local)");
//                result.put("apiKey", "dummy-local-key");
//                return result;
//            }
//        }
//        result.put("status", false);
//        result.put("status_message", "Invalid credentials");
//        return result;
//    }
//    // --------------------------
//
//    @Override
//    public void setCurrentUsername(String name) {
//        currentUsername = name;
//    }
//
//    @Override
//    public String getCurrentUsername() {
//        return currentUsername;
//    }
//
//    @Override
//    public boolean existsByName(String identifier) {
//        return accounts.containsKey(identifier);
//    }
//}