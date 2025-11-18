package use_case.registration.login;

import entity.User;
import use_case.DataAccessException;

import java.util.HashMap;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     * @throws DataAccessException if there is an error accessing data
     */
    boolean existsByName(String username) throws DataAccessException;

    /**
     * Saves the user.
     * @param user the user to save
     * @throws DataAccessException if there is an error accessing data
     */
    void save(User user) throws DataAccessException;

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     * @throws DataAccessException if there is an error accessing data
     */
    User get(String username) throws DataAccessException;

    /**
     * Sets the currently logged-in username.
     */
    void setCurrentUsername(String name);

    /**
     * Returns the currently logged-in username.
     */
    String getCurrentUsername();

    /**
     * Return login status and user information.
     * @param username the username of the account
     * @param password the password of the account
     * @return map of status and user info
     * Example:
     * login successful: {"status": true, "status_message": "", "user": User, "apiKey": "my_key"}
     * Username not found: {"status": false, "status_message": "username dne"}
     * Invalid Credential: {"status": false, "status_message": "invalid cred"}
     * Unknown error: {"status": false, "status_message": "other status message"}
     */
    HashMap<String, Object> login(String username, String password) throws DataAccessException;
}
