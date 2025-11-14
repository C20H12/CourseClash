package app;

import entity.UserFactory;
import interface_adapter.*;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.registration.login.*;
import use_case.registration.login.*;
import view.main_screen.MainScreenView;
import view.registration.*;
import data_access.*;
import utility.FontLoader;

import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager;

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    //private SignupView signupView;
    //private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private MainScreenViewModel mainScreenViewModel;
    private LoginView loginView;
    private MainScreenView mainScreenView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addMainScreenView() {
        mainScreenViewModel = new MainScreenViewModel();
        mainScreenView = new MainScreenView(mainScreenViewModel);
        cardPanel.add(mainScreenView, mainScreenView.getViewName());
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                mainScreenViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setSize(1200, 800);
        application.setResizable(false); // Fixed size window
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FontLoader.registerFonts();

        application.add(cardPanel);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel, application);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
