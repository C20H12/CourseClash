package use_case.registration.signup;

import entity.User;
import entity.UserFactory;
import use_case.DataAccessException;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface signupDAO;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.signupDAO = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) throws DataAccessException {
        final String username = signupInputData.getUsername();
        final String password = signupInputData.getPassword();
        final String repeatPassword = signupInputData.getRepeatPassword();


        if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
            return;
        }
        else if ("".equals(signupInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
            return;
        }
        else if ("".equals(signupInputData.getUsername())) {
            userPresenter.prepareFailView("Username cannot be empty");
            return;
        }

        // Case: Make signup request
        final User user = userFactory.create(signupInputData.getUsername(), signupInputData.getPassword());
        String status_message = signupDAO.create(user);
        if (status_message.equals("username taken")) {
            userPresenter.prepareFailView("User already exists.");
        }

        // Case: Successfully create
        else if (status_message.equals("success")){
            final SignupOutputData signupOutputData = new SignupOutputData(user.getUserName());
            userPresenter.prepareSuccessView(signupOutputData);
        }

        // Case: Unknown error
        else {
            userPresenter.prepareFailView("Unknown error when creating new user.");
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
