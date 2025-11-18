package use_case.registration.login;

import entity.User;
import use_case.DataAccessException;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username) throws DataAccessException;

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user) throws DataAccessException;

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    User get(String username) throws DataAccessException;

    void setCurrentUsername(String name);

    String getCurrentUsername();
}
