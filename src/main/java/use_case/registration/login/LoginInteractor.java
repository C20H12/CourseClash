package use_case.registration.login;

import entity.User;
import use_case.DataAccessException;

import java.util.HashMap;
import java.util.Objects;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface loginDAO;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.loginDAO = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) throws DataAccessException {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        HashMap<String, Object> loginResponse = loginDAO.login(username, password);
        // Return a <key, val> with <"status", bool> and <"status_message", String> in key
        boolean isLoggedIn = (boolean) loginResponse.get("status");
        String message = (String) loginResponse.get("status_message");

        // Case: user DNE
        if (!isLoggedIn && message.equals("username dne")) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
            return;

        }

        // Case: unmatched username and password
        else if (!isLoggedIn && message.equals("invalid cred")) {
            loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            return;
        }

        // Case: Unknown error
        else if (!isLoggedIn) {
            loginPresenter.prepareFailView("Unknown login error!");
            return;
        }

        // Case: Login successful
        else {
            User user = (User) loginResponse.get("user");
            String apiKey = (String) loginResponse.get("apiKey");
            final LoginOutputData loginOutputData = new LoginOutputData(user.getUserName(), apiKey, user);
            loginPresenter.prepareSuccessView(loginOutputData);
            return;
        }

        /// Restructured below s.t. DAO does not require api key.
//        if (!userDataAccessObject.existsByName(username)) {
//            loginPresenter.prepareFailView(username + ": Account does not exist.");
//        }
//        else {
//            final String pwd = userDataAccessObject.get(username).getPassword();
//            if (!password.equals(pwd)) {
//                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
//            }
//            else {
//
//                final User user = userDataAccessObject.get(loginInputData.getUsername());
//
//                userDataAccessObject.setCurrentUsername(username);
//
//                final LoginOutputData loginOutputData = new LoginOutputData(user.getUserName());
//                loginPresenter.prepareSuccessView(loginOutputData);
//            }
//        }

    }
}
