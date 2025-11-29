package use_case.login;

import frameworks_and_drivers.DataAccess.LoginSignupTestDataAccessObject;
import frameworks_and_drivers.DataAccess.LoginUserDataAccessObject;
import frameworks_and_drivers.DataAccess.SignupUserDataAccessObject;
import use_case.DataAccessException;
import use_case.registration.login.*;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import use_case.registration.signup.SignupUserDataAccessInterface;

import static org.junit.jupiter.api.Assertions.*;

class LoginInteractorTest {

    private User createdUser = null;  // Register user for cleanup

    @AfterEach
    void cleanup() throws DataAccessException {
        if (createdUser != null) {
            LoginSignupTestDataAccessObject deleteDAO = new LoginSignupTestDataAccessObject();
            deleteDAO.delete(createdUser);
            createdUser = null;
        }
    }

    @Test
    void successTest() throws DataAccessException {
        // Create a new user
        User testUser = new User(System.currentTimeMillis() + "_login_test_user", "123");
        // Add this user to DB
        SignupUserDataAccessInterface signupDAO = new SignupUserDataAccessObject();
        signupDAO.create(testUser);
        createdUser = testUser;


        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals(testUser.getUserName(), outputData.getUsername());
                assertNotNull(outputData.getApiKey());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToSignupView() {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'switchToSignupView'");
            }
        };
        LoginInputData inputData = new LoginInputData(testUser.getUserName(), testUser.getPassword());
        LoginUserDataAccessInterface loginDAO = new LoginUserDataAccessObject();
        LoginInputBoundary interactor = new LoginInteractor(loginDAO, successPresenter);
        interactor.execute(inputData);
    }


    @Test
    void failurePasswordMismatchTest() throws DataAccessException {
        // Create a new user
        User testUser = new User(System.currentTimeMillis() + "_login_test_user", "123");
        // Add this user to DB
        SignupUserDataAccessInterface signupDAO = new SignupUserDataAccessObject();
        signupDAO.create(testUser);
        createdUser = testUser;

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for \"" + testUser.getUserName() + "\".", error);
            }

            @Override
            public void switchToSignupView() {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'switchToSignupView'");
            }
        };

        // Attempt login using the wrong password
        final String wrongTestPassword = testUser.getPassword() + "wrong";
        LoginInputData inputData = new LoginInputData(testUser.getUserName(), wrongTestPassword);
        LoginUserDataAccessInterface loginDAO = new LoginUserDataAccessObject();
        LoginInputBoundary interactor = new LoginInteractor(loginDAO, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() throws DataAccessException {
        // Create a new user, but we don't add to DB
        User testUser = new User(System.currentTimeMillis() + "_login_nonexistent_user", "123");

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(testUser.getUserName() + ": Account does not exist.", error);
            }

            @Override
            public void switchToSignupView() {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'switchToSignupView'");
            }
        };

        // Attempt login with this non-existence user
        LoginInputData inputData = new LoginInputData(testUser.getUserName(), testUser.getPassword());
        LoginUserDataAccessInterface loginDAO = new LoginUserDataAccessObject();
        LoginInputBoundary interactor = new LoginInteractor(loginDAO, failurePresenter);
        interactor.execute(inputData);
    }
}