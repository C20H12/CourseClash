package interface_adapter.registration.login;

import use_case.DataAccessException;
import use_case.registration.login.LoginInputBoundary;
import use_case.registration.login.LoginInputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {

    private final LoginInputBoundary loginUseCaseInteractor;

    public LoginController(LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     *
     * @param username the username of the user logging in
     * @param password the password of the user logging in
     * @throws DataAccessException if there is an error accessing the data
     */
    public void execute(String username, String password) throws DataAccessException {
        final LoginInputData loginInputData = new LoginInputData(username, password);

        loginUseCaseInteractor.execute(loginInputData);
    }

    /**
     * Executes the "switch to SignupView" Use Case.
     */
    public void switchToSignupView() {
        loginUseCaseInteractor.switchToSignupView();
    }
}
