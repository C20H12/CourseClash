package app;

import entity.UserFactory;
import interface_adapter.*;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.studyset.studyset_browse.BrowseStudySetViewModel;
import interface_adapter.registration.login.*;
import interface_adapter.registration.signup.SignupController;
import interface_adapter.registration.signup.SignupPresenter;
import interface_adapter.registration.signup.SignupViewModel;
import interface_adapter.user_session.UserSession;
import use_case.registration.login.*;
import use_case.registration.signup.SignupInputBoundary;
import use_case.registration.signup.SignupInteractor;
import use_case.registration.signup.SignupOutputBoundary;
import view.main_screen.MainScreenView;
import view.registration.*;
import view.study_set.BrowseStudySetView;
import data_access.*;
import utility.FontLoader;

import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final UserSession session = new UserSession();

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager;

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
//    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private MainScreenViewModel mainScreenViewModel;
    private LoginView loginView;
    private MainScreenView mainScreenView;
    private BrowseStudySetViewModel browseStudySetViewModel;
    private BrowseStudySetView browseStudySetView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addMainScreenView() {
        mainScreenViewModel = new MainScreenViewModel();
        browseStudySetViewModel = new BrowseStudySetViewModel();

        mainScreenView = new MainScreenView(mainScreenViewModel, viewManagerModel, browseStudySetViewModel);
        cardPanel.add(mainScreenView, mainScreenView.getViewName());

        browseStudySetView = new BrowseStudySetView(browseStudySetViewModel, mainScreenViewModel, viewManagerModel);
        cardPanel.add(browseStudySetView, browseStudySetView.getViewName());
        return this;
    }

    public AppBuilder addBrowseStudySetView() {

        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                signupDAO, signupOutputBoundary, userFactory);
        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginUserDataAccessObject loginDAO = new LoginUserDataAccessObject();
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                mainScreenViewModel, loginViewModel, session);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                loginDAO, loginOutputBoundary);
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("CourseClash");
        application.setSize(1200, 800);
        application.setResizable(false); // Fixed size window
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FontLoader.registerFonts();

        application.add(cardPanel);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel, application);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
