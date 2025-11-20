/**
 * File: LoginUserDataAccessInterface.java
 * Description: Defines the data access interface for login actions.
 * Author: Albert and Daniel
 */
package use_case.registration.login;
import use_case.DataAccessException;
import java.util.HashMap;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {
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
