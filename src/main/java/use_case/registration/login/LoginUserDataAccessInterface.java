package use_case.registration.login;

import java.util.Map;

import use_case.DataAccessException;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Returns login status and user information.
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return map of status and user info
     *         Example:
     *         login successful: {"status": true, "status_message": "", "user": User, "apiKey": "my_key"}
     *         Username not found: {"status": false, "status_message": "username dne"}
     *         Invalid Credential: {"status": false, "status_message": "invalid cred"}
     *         Unknown error: {"status": false, "status_message": "other status message"}
     * @throws DataAccessException if there is an error accessing login data
     */
    Map<String, Object> login(String username, String password) throws DataAccessException;
}
