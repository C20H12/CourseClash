package app;

import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.registration.login.*;
import use_case.registration.login.*;
import view.main_screen.MainScreenView;
import view.registration.*;
import data_access.*;
import utility.FontLoader;
import view.ViewManager;

// --- NEW IMPORTS FOR MULTIPLAYER ---
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import view.MultiPlayerView;
import data_access.InMemoryGameDataAccessObject;
import data_access.DBStudySetDataAccessObject;

import interface_adapter.MultiPlayer.start_match.MPStartController;
import interface_adapter.MultiPlayer.start_match.MPStartPresenter;
import use_case.MultiPlayer.start_match.MPStartInteractor;

import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerController;
import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerPresenter;
import use_case.MultiPlayer.submit_answer.SubmitAnswerInteractor;
// ------------------------------------

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager;

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // --- NEW DATA ACCESS FOR MULTIPLAYER ---
    // We use the API_KEY from your Constants file
    final DBStudySetDataAccessObject studySetDataAccessObject = new DBStudySetDataAccessObject("abc123");
    final InMemoryGameDataAccessObject gameDataAccessObject = new InMemoryGameDataAccessObject();
    // ---------------------------------------

    // ViewModels
    private LoginViewModel loginViewModel;
    private MainScreenViewModel mainScreenViewModel;
    // --- NEW VIEWMODEL ---
    private final MultiPlayerViewModel multiPlayerViewModel = new MultiPlayerViewModel();
    // ---------------------

    // Views
    private LoginView loginView;
    private MainScreenView mainScreenView;
    private MultiPlayerView multiPlayerView; // New View

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
        // Note: If you updated MainScreenView to require LogoutController, update this line.
        // For now, we keep it matching your uploaded file.
        mainScreenView = new MainScreenView(mainScreenViewModel, null);
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

    // --- NEW METHOD: ADD MULTIPLAYER LOGIC ---
    public AppBuilder addMultiPlayerUseCase() {
        // 1. Start Match Stack
        MPStartPresenter mpStartPresenter = new MPStartPresenter(multiPlayerViewModel, viewManagerModel);

        MPStartInteractor mpStartInteractor = new MPStartInteractor(
                mpStartPresenter,
                gameDataAccessObject,       // MultiPlayerAccessInterface
                userDataAccessObject,       // LoginUserDataAccessInterface (to find User)
                studySetDataAccessObject    // StudySetDataAccessInterface (to find Deck)
        );

        MPStartController mpStartController = new MPStartController(mpStartInteractor);

        // 2. Submit Answer Stack
        SubmitAnswerPresenter submitAnswerPresenter = new SubmitAnswerPresenter(multiPlayerViewModel);

        SubmitAnswerInteractor submitAnswerInteractor = new SubmitAnswerInteractor(
                gameDataAccessObject,       // SubmitAnswerDataAccessInterface
                submitAnswerPresenter
        );

        SubmitAnswerController submitAnswerController = new SubmitAnswerController(submitAnswerInteractor);

        // 3. Create the Multiplayer View
        multiPlayerView = new MultiPlayerView(multiPlayerViewModel, submitAnswerController);
        cardPanel.add(multiPlayerView, multiPlayerView.viewName);

        // 4. Inject the Start Controller into the Main Screen
        // IMPORTANT: Ensure MainScreenView.java has the method:
        // public void setMPStartController(MPStartController controller)
        if (mainScreenView != null) {
            mainScreenView.setMPStartController(mpStartController);
        }

        return this;
    }
    // -----------------------------------------

    public JFrame build() {
        final JFrame application = new JFrame("CourseClash"); // Updated Title
        application.setSize(1200, 800);
        application.setResizable(false);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FontLoader.registerFonts();

        application.add(cardPanel);
        // Adjusted to match your constructor: ViewManager(JPanel, CardLayout, ViewManagerModel, JFrame)
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel, application);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}