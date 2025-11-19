package interface_adapter.registration.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.main_screen.LoggedInState;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.user_session.UserSession;
import use_case.registration.login.LoginOutputBoundary;
import use_case.registration.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final MainScreenViewModel mainScreenViewModel;
    private final ViewManagerModel viewManagerModel;
    private final UserSession session;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          MainScreenViewModel mainScreenViewModel,
                          LoginViewModel loginViewModel,
                          UserSession session) {
        this.viewManagerModel = viewManagerModel;
        this.mainScreenViewModel = mainScreenViewModel;
        this.loginViewModel = loginViewModel;
        this.session = session;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // First, update session with the logged-in user and api_key
        session.setApiKey(response.getApiKey());
        session.setUser(response.getLoggedinUser());

        // On success, update the loggedInViewModel's state
        final LoggedInState loggedInState = mainScreenViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.mainScreenViewModel.firePropertyChange();

        // and clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());

        // switch to the logged in view
        this.viewManagerModel.setState(mainScreenViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }
}
