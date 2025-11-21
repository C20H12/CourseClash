package use_case.registration.signup;

import entity.User;
import use_case.DataAccessException;

/**
 * DAO interface for the Signup Use Case.
 */
public interface SignupUserDataAccessInterface {

//     //API call will handle if user already exists
//    /**
//     * Checks if the given username exists.
//     * @param username the username to look for
//     * @return true if a user with the given username exists; false otherwise
//     * @throws DataAccessException if there is an error accessing data
//     */
//    boolean existsByName(String username) throws DataAccessException;

    /**
     * Create the user.
     * @param user the user to create
     * @return creation status: "success" if successful, "username taken" otherwise
     * @throws DataAccessException if there is an error accessing data
     */
    String create(User user) throws DataAccessException;
}
