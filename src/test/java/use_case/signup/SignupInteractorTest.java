package use_case.signup;

import entity.UserFactory;
import frameworks_and_drivers.DataAccess.LoginSignupTestDataAccessObject;
import frameworks_and_drivers.DataAccess.SignupUserDataAccessObject;
import org.junit.jupiter.api.AfterEach;
import use_case.DataAccessException;
import use_case.registration.login.*;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.registration.signup.*;

import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorTest {

    private User createdUser = null;
    final private UserFactory userFactory = new UserFactory();

    @AfterEach
    void cleanup() throws DataAccessException {
        if (createdUser != null) {
            LoginSignupTestDataAccessObject deleteDAO = new LoginSignupTestDataAccessObject();
            deleteDAO.delete(createdUser);
            createdUser = null;
        }
    }

    // Helper method for success presenter
    private SignupOutputBoundary successPresenter(User expectedUser) {
        return new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals(expectedUser.getUserName(), outputData.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToLoginView() {
                fail("Switch to login view is unexpected.");
            }
        };
    }

    // Helper method for failure presenter
    private SignupOutputBoundary failurePresenter(String expectedError) {
        return new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(expectedError, error);
            }

            @Override
            public void switchToLoginView() {
                fail("Switch to login view is unexpected.");
            }
        };
    }

    @Test
    void successTest() throws DataAccessException {
        // Create a new user
        User testUser = userFactory.create(System.currentTimeMillis() + "login_test_user", "123");
        createdUser = testUser;
        // Repeated password matches
        final String repeatedPassword = testUser.getPassword();

        SignupInputData inputData = new SignupInputData(
                testUser.getUserName(), testUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO, successPresenter(testUser), userFactory);
        interactor.execute(inputData);
    }

    @Test
    void failurePasswordMismatchTest() throws DataAccessException {
        // Create a new user
        User testUser = userFactory.create(System.currentTimeMillis() + "_login_test_user", "123");
        // Repeated password does not match
        final String repeatedPassword = testUser.getPassword() + "haha";

        SignupInputData inputData = new SignupInputData(
                testUser.getUserName(), testUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO,
                failurePresenter("Passwords don't match."), userFactory);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyPasswordTest() throws DataAccessException {
        // Create a new user with empty password
        User testUser = userFactory.create("empty_pass_user", "");
        final String repeatedPassword = "";

        SignupInputData inputData = new SignupInputData(
                testUser.getUserName(), testUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO,
                failurePresenter("New password cannot be empty"), userFactory);
        interactor.execute(inputData);
    }

    @Test
    void failureEmptyUsernameTest() throws DataAccessException {
        // Create user with empty username
        User testUser = userFactory.create("", "123");
        final String repeatedPassword = "123";

        SignupInputData inputData = new SignupInputData(
                testUser.getUserName(), testUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO,
                failurePresenter("Username cannot be empty"), userFactory);
        interactor.execute(inputData);
    }

    @Test
    void failureUserAlreadyExistsTest() throws DataAccessException {
        // Existing username
        User existingUser = userFactory.create(System.currentTimeMillis() + "_login_test_user", "123");
        createdUser = existingUser;
        final String repeatedPassword = "123";

        // First create the user manually
        SignupUserDataAccessObject signupDAOSetup = new SignupUserDataAccessObject();
        signupDAOSetup.create(existingUser);

        SignupInputData inputData = new SignupInputData(
                existingUser.getUserName(), existingUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO,
                failurePresenter("User already exists."), userFactory);
        interactor.execute(inputData);
    }


}