package app;
import entity.UserFactory;
import interface_adapter.*;
import interface_adapter.MultiPlayer.MultiPlayerController;
import interface_adapter.MultiPlayer.MultiPlayerPresenter;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.SinglePlayer.SinglePlayerController;
import interface_adapter.SinglePlayer.SinglePlayerPresenter;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.registration.login.*;
import interface_adapter.registration.signup.SignupController;
import interface_adapter.registration.signup.SignupPresenter;
import interface_adapter.registration.signup.SignupViewModel;
import interface_adapter.studyDeck.StudyDeckController;
import interface_adapter.studyDeck.StudyDeckPresenter;
import interface_adapter.studyDeck.StudyDeckViewModel;
import interface_adapter.user_session.UserSession;
import use_case.DataAccessException;
import use_case.SinglePlayer.SinglePlayerInteractor;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.registration.login.*;
import use_case.registration.signup.SignupInputBoundary;
import use_case.registration.signup.SignupInteractor;
import use_case.registration.signup.SignupOutputBoundary;
import use_case.studyDeck.StudyDeckInputBoundary;
import use_case.studyDeck.StudyDeckInteractor;
import use_case.studyDeck.StudyDeckOutputBoundary;
import view.single.SinglePlayerView;
import frameworks_and_drivers.DataAccess.SinglePlayerDataAccessObject;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckLocalDataAccessObject;
import view.leaderboard.LeaderboardView;
import view.main_screen.MainScreenView;
import view.multi.MultiPlayerView;
import view.registration.*;
import frameworks_and_drivers.DataAccess.*;
import utility.FontLoader;
import use_case.MultiPlayer.MultiPlayerInputBoundary;
import use_case.MultiPlayer.MultiPlayerInteractor;
import use_case.MultiPlayer.MultiPlayerOutputBoundary;
import use_case.SinglePlayer.*;
import view.ViewManager;
import view.StudyDeck.StudyDeckView;

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

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private MainScreenViewModel mainScreenViewModel;
    private StudyDeckViewModel studyDeckViewModel;
    private LeaderboardViewModel leaderboardViewModel;
    private LoginView loginView;
    private MainScreenView mainScreenView;
    private StudyDeckView browseStudySetView;
    private LeaderboardView leaderboardView;
    private SinglePlayerViewModel singlePlayerViewModel;
    private SinglePlayerView singlePlayerView;
    private MultiPlayerViewModel multiPlayerViewModel;
    private MultiPlayerView multiPlayerView;


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

    public AppBuilder addMainScreenView() throws DataAccessException {
        mainScreenViewModel = new MainScreenViewModel();
        studyDeckViewModel = new StudyDeckViewModel();
        leaderboardViewModel = new LeaderboardViewModel();
        singlePlayerViewModel = new SinglePlayerViewModel();
        multiPlayerViewModel = new MultiPlayerViewModel();
        leaderboardViewModel = new LeaderboardViewModel();

        mainScreenView = new MainScreenView(mainScreenViewModel,
                viewManagerModel, studyDeckViewModel, leaderboardViewModel,  singlePlayerViewModel);
        cardPanel.add(mainScreenView, mainScreenView.getViewName());

        browseStudySetView = new StudyDeckView(studyDeckViewModel, mainScreenViewModel, viewManagerModel);
        cardPanel.add(browseStudySetView, browseStudySetView.getViewName());

        leaderboardView = new LeaderboardView(leaderboardViewModel, viewManagerModel, mainScreenViewModel);
        cardPanel.add(leaderboardView, leaderboardView.getViewName());

        singlePlayerView = new SinglePlayerView(singlePlayerViewModel, viewManagerModel, session);
        cardPanel.add(singlePlayerView, singlePlayerView.getViewName());

        multiPlayerView = new MultiPlayerView(multiPlayerViewModel, viewManagerModel, session);
        cardPanel.add(multiPlayerView, multiPlayerView.getViewName());

        leaderboardView = new LeaderboardView(leaderboardViewModel, viewManagerModel, mainScreenViewModel);
        cardPanel.add(leaderboardView, leaderboardView.getViewName());
        return this;

    }

    public AppBuilder addSignupUseCase() {
        final frameworks_and_drivers.DataAccess.SignupUserDataAccessObject signupDAO = new frameworks_and_drivers.DataAccess.SignupUserDataAccessObject();
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

    public AppBuilder addStudyDeckUseCase() {
        final StudyDeckLocalDataAccessObject studyDeckDAO = new StudyDeckLocalDataAccessObject();
        final StudyDeckOutputBoundary studyDeckOutputBoundary = new StudyDeckPresenter(studyDeckViewModel);
        final StudyDeckInputBoundary studyDeckInteractor = new StudyDeckInteractor(studyDeckDAO, studyDeckOutputBoundary);

        StudyDeckController studyDeckController = new StudyDeckController(studyDeckInteractor);
        browseStudySetView.setStudySetViewController(studyDeckController);
        return this;
    }

    public AppBuilder addLeaderboardUseCase() throws DataAccessException {
        final LeaderboardUserDataAccessObject userDataAccessObject = new LeaderboardUserDataAccessObject(session);
        final LeaderboardOutputBoundary leaderboardOutputBoundary = new LeaderboardPresenter(leaderboardViewModel,
                viewManagerModel);
        final LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(
                userDataAccessObject, leaderboardOutputBoundary);

        LeaderboardController leaderboardController = new LeaderboardController(leaderboardInteractor);
        leaderboardView.setLeaderboardController(leaderboardController);
        return this;
    }

    public AppBuilder addSinglePlayerUseCase() {
        // 1) DAO (gateway)
        SinglePlayerDataAccessObject spDAO = new SinglePlayerDataAccessObject(session);
        // 2) Presenter
        SinglePlayerOutputBoundary spPresenter = new SinglePlayerPresenter(singlePlayerViewModel);
        // 3) Interactor
        SinglePlayerInputBoundary spInteractor = new SinglePlayerInteractor(spPresenter, spDAO);
        // 4) Controller
        SinglePlayerController spController = new SinglePlayerController(spInteractor);
        singlePlayerView.setController(spController);
        return this;
    }

    public AppBuilder addMultiPlayerUseCase() {
        MultiPlayerDataAccessObject mpDAO = new MultiPlayerDataAccessObject(session);
        MultiPlayerOutputBoundary mpPresenter = new MultiPlayerPresenter(multiPlayerViewModel);
        MultiPlayerInputBoundary mpInteractor = new MultiPlayerInteractor(mpPresenter, mpDAO);
        MultiPlayerController mpController = new MultiPlayerController(mpInteractor);
        multiPlayerView.setMultiPlayerController(mpController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("CourseClash");
        application.setSize(1200, 800);
        application.setResizable(true);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FontLoader.registerFonts();

        application.add(cardPanel);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel, application);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

}