package use_case.signup;

import entity.UserFactory;
import frameworks_and_drivers.DataAccess.LoginSignupTestDataAccessObject;
import frameworks_and_drivers.DataAccess.SignupUserDataAccessObject;
import use_case.DataAccessException;
import use_case.registration.login.*;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.registration.signup.*;

import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorTest {

    @Test
    void successTest() throws DataAccessException {
        // Create a new user
        User testUser = new User("login_test_user", "123");
        // Repeated password matches
        final String repeatedPassword = testUser.getPassword();

        // This creates a successPresenter that tests whether the test case is as we expect.
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals(testUser.getUserName(), outputData.getUsername());
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
        SignupInputData inputData = new SignupInputData(testUser.getUserName(), testUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO, successPresenter, new UserFactory());
        interactor.execute(inputData);

        // Remove this test user
        LoginSignupTestDataAccessObject deleteDAO = new LoginSignupTestDataAccessObject();
        deleteDAO.delete(testUser);
    }


    @Test
    void failurePasswordMismatchTest() throws DataAccessException {
        // Create a new user
        User testUser = new User("login_test_user", "123");
        // Repeated password does not match
        final String repeatedPassword = testUser.getPassword() + "haha";

        // This creates a successPresenter that tests whether the test case is as we expect.
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Passwords don't match.", error);
            }

            @Override
            public void switchToLoginView() {
                fail("Should never call switchToLoginView when repeated password doesn't match.");
            }
        };
        SignupInputData inputData = new SignupInputData(testUser.getUserName(), testUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO, successPresenter, new UserFactory());
        interactor.execute(inputData);
    }
    @Test
    void failureEmptyPasswordTest() throws DataAccessException {
        // Create a new user with empty password
        User testUser = new User("empty_pass_user", "");
        final String repeatedPassword = "";

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("New password cannot be empty", error);
            }

            @Override
            public void switchToLoginView() {
                fail("Should never switch to login view when password is empty.");
            }
        };

        SignupInputData inputData = new SignupInputData(testUser.getUserName(), testUser.getPassword(), repeatedPassword);

        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO, presenter, new UserFactory());

        interactor.execute(inputData);
    }
    @Test
    void failureEmptyUsernameTest() throws DataAccessException {
        // Create user with empty username
        User testUser = new User("", "123");
        final String repeatedPassword = "123";

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Username cannot be empty", error);
            }

            @Override
            public void switchToLoginView() {
                fail("Should never switch to login view when username is empty.");
            }
        };

        SignupInputData inputData =
                new SignupInputData(testUser.getUserName(), testUser.getPassword(), repeatedPassword);

        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor =
                new SignupInteractor(signupDAO, presenter, new UserFactory());

        interactor.execute(inputData);
    }
    @Test
    void failureUserAlreadyExistsTest() throws DataAccessException {
        // Existing username
        User existingUser = new User("signup_test_user", "123");
        final String repeatedPassword = "123";

        // First create the user manually
        SignupUserDataAccessObject signupDAOSetup = new SignupUserDataAccessObject();
        signupDAOSetup.create(existingUser);

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected when username is already taken.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("User already exists.", error);
            }

            @Override
            public void switchToLoginView() {
                fail("Should never switch to login view when user already exists.");
            }
        };

        // Attempt to create another user with the same username
        SignupInputData inputData = new SignupInputData(existingUser.getUserName(), existingUser.getPassword(), repeatedPassword);
        SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        SignupInputBoundary interactor = new SignupInteractor(signupDAO, presenter, new UserFactory());
        interactor.execute(inputData);

        // Remove the test user
        LoginSignupTestDataAccessObject deleteDAO = new LoginSignupTestDataAccessObject();
        deleteDAO.delete(existingUser);
    }


}