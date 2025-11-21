package app;

import entity.UserFactory;
import frameworks_and_drivers.DataAccess.*;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.MultiPlayer.start_match.MPStartController;
import interface_adapter.MultiPlayer.start_match.MPStartPresenter;
import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerController;
import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerPresenter;
import interface_adapter.SinglePlayer.SinglePlayerController;
import interface_adapter.SinglePlayer.SinglePlayerPresenter;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.registration.login.LoginController;
import interface_adapter.registration.login.LoginPresenter;
import interface_adapter.registration.login.LoginViewModel;
import interface_adapter.registration.signup.SignupController;
import interface_adapter.registration.signup.SignupPresenter;
import interface_adapter.registration.signup.SignupViewModel;
import interface_adapter.studyset.studyset_browse.BrowseStudySetViewModel;
import interface_adapter.user_session.UserSession;
import use_case.MultiPlayer.start_match.MPStartInteractor;
import use_case.MultiPlayer.submit_answer.SubmitAnswerInteractor;
import use_case.SinglePlayer.SinglePlayerInputBoundary;
import use_case.SinglePlayer.SinglePlayerInteractor;
import use_case.SinglePlayer.SinglePlayerOutputBoundary;
import use_case.StudySet.StudySetDataAccessInterface;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.registration.login.LoginInputBoundary;
import use_case.registration.login.LoginInteractor;
import use_case.registration.login.LoginOutputBoundary;
import use_case.registration.login.LoginUserDataAccessInterface;
import use_case.registration.signup.SignupInputBoundary;
import use_case.registration.signup.SignupInteractor;
import use_case.registration.signup.SignupOutputBoundary;
import utility.FontLoader;
import view.MultiPlayerView;
import view.SinglePlayerView;
import view.ViewManager;
import view.leaderboard.LeaderboardView;
import view.main_screen.MainScreenView;
import view.registration.LoginView;
import view.registration.SignupView;
import view.study_set.BrowseStudySetView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final UserSession session = new UserSession();
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager;

    final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(userFactory);
    final StudySetDataAccessInterface studySetDataAccessObject = new InMemoryStudySetDataAccessObject();

    final InMemoryGameDataAccessObject gameDataAccessObject = new InMemoryGameDataAccessObject();
    final SinglePlayerDataAccessObject spDAO = new SinglePlayerDataAccessObject();

    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private MainScreenViewModel mainScreenViewModel;
    private BrowseStudySetViewModel browseStudySetViewModel;
    private LeaderboardViewModel leaderboardViewModel;
    private SinglePlayerViewModel singlePlayerViewModel;
    private final MultiPlayerViewModel multiPlayerViewModel = new MultiPlayerViewModel();

    private SignupView signupView;
    private LoginView loginView;
    private MainScreenView mainScreenView;
    private BrowseStudySetView browseStudySetView;
    private LeaderboardView leaderboardView;
    private SinglePlayerView singlePlayerView;
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

    public AppBuilder addMainScreenView() {
        mainScreenViewModel = new MainScreenViewModel();
        browseStudySetViewModel = new BrowseStudySetViewModel();
        leaderboardViewModel = new LeaderboardViewModel();
        singlePlayerViewModel = new SinglePlayerViewModel();

        mainScreenView = new MainScreenView(mainScreenViewModel, viewManagerModel, browseStudySetViewModel, leaderboardViewModel);
        cardPanel.add(mainScreenView, mainScreenView.getViewName());

        browseStudySetView = new BrowseStudySetView(browseStudySetViewModel, mainScreenViewModel, viewManagerModel);
        cardPanel.add(browseStudySetView, browseStudySetView.getViewName());

        leaderboardView = new LeaderboardView(leaderboardViewModel, viewManagerModel, mainScreenViewModel);
        cardPanel.add(leaderboardView, leaderboardView.getViewName());

        singlePlayerView = new SinglePlayerView(singlePlayerViewModel, viewManagerModel);
        cardPanel.add(singlePlayerView, singlePlayerView.getViewName());

        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupUserDataAccessObject signupDAO = new SignupUserDataAccessObject();
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(signupDAO, signupOutputBoundary, userFactory);
        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                mainScreenViewModel, loginViewModel, session);

        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, // Replaces 'loginDAO'
                loginOutputBoundary
        );

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addMultiPlayerUseCase() {
        MPStartPresenter mpStartPresenter = new MPStartPresenter(multiPlayerViewModel, viewManagerModel);
        MPStartInteractor mpStartInteractor = new MPStartInteractor(
                mpStartPresenter,
                gameDataAccessObject,
                userDataAccessObject,
                studySetDataAccessObject
        );
        MPStartController mpStartController = new MPStartController(mpStartInteractor);

        SubmitAnswerPresenter submitAnswerPresenter = new SubmitAnswerPresenter(multiPlayerViewModel);
        SubmitAnswerInteractor submitAnswerInteractor = new SubmitAnswerInteractor(
                gameDataAccessObject,
                submitAnswerPresenter
        );
        SubmitAnswerController submitAnswerController = new SubmitAnswerController(submitAnswerInteractor);

        multiPlayerView = new MultiPlayerView(multiPlayerViewModel, submitAnswerController);
        cardPanel.add(multiPlayerView, multiPlayerView.viewName);

        if (mainScreenView != null) {
            mainScreenView.setMPStartController(mpStartController);
        }

        return this;
    }

    public AppBuilder addLeaderboardUseCase() {
        final LeaderboardUserDataAccessObject leaderboardDAO = new LeaderboardUserDataAccessObject(session.getApiKey());
        final LeaderboardOutputBoundary leaderboardOutputBoundary = new LeaderboardPresenter(leaderboardViewModel, viewManagerModel);
        final LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(leaderboardDAO, leaderboardOutputBoundary);
        LeaderboardController leaderboardController = new LeaderboardController(leaderboardInteractor);
        leaderboardView.setLeaderboardController(leaderboardController);
        return this;
    }

    public AppBuilder addSinglePlayerUseCase() {
        SinglePlayerOutputBoundary spPresenter = new SinglePlayerPresenter(singlePlayerViewModel);
        SinglePlayerInputBoundary spInteractor = new SinglePlayerInteractor(spPresenter, spDAO);
        SinglePlayerController spController = new SinglePlayerController(spInteractor);
        singlePlayerView.setController(spController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("CourseClash [TEST MODE]");
        application.setSize(1200, 800);
        application.setResizable(false);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FontLoader.registerFonts();

        application.add(cardPanel);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel, application);

        return application;
    }
}