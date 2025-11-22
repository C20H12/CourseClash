package app;

import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.studyset.studyset_browse.BrowseStudySetViewModel;
import interface_adapter.registration.login.*;
import interface_adapter.registration.signup.SignupController;
import interface_adapter.registration.signup.SignupPresenter;
import interface_adapter.registration.signup.SignupViewModel;
import interface_adapter.user_session.UserSession;
import interface_adapter.SinglePlayer.SinglePlayerController;
import interface_adapter.SinglePlayer.SinglePlayerPresenter;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import use_case.SinglePlayer.SinglePlayerInputBoundary;
import use_case.SinglePlayer.SinglePlayerInteractor;
import use_case.SinglePlayer.SinglePlayerOutputBoundary;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardOutputBoundary;
import use_case.registration.login.*;
import use_case.registration.signup.SignupInputBoundary;
import use_case.registration.signup.SignupInteractor;
import use_case.registration.signup.SignupOutputBoundary;
import view.SinglePlayerView;
import view.leaderboard.LeaderboardView;
import view.main_screen.MainScreenView;
import view.registration.*;
import view.study_set.BrowseStudySetView;
import utility.FontLoader;
import view.ViewManager;

// --- DATA ACCESS IMPORTS ---
import frameworks_and_drivers.DataAccess.InMemoryUserDataAccessObject;
import frameworks_and_drivers.DataAccess.InMemoryStudySetDataAccessObject;
import frameworks_and_drivers.DataAccess.NetworkGameDataAccessObject;
import frameworks_and_drivers.DataAccess.SinglePlayerDataAccessObject;
import frameworks_and_drivers.DataAccess.LeaderboardUserDataAccessObject;
import frameworks_and_drivers.DataAccess.LoginUserDataAccessObject;
import frameworks_and_drivers.DataAccess.SignupUserDataAccessObject;
// ---------------------------

// --- MULTIPLAYER IMPORTS ---
import entity.peer.PeerConnection;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import view.MultiPlayerView;
import interface_adapter.MultiPlayer.start_match.MPStartController;
import interface_adapter.MultiPlayer.start_match.MPStartPresenter;
import use_case.MultiPlayer.start_match.MPStartInteractor;
import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerController;
import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerPresenter;
import use_case.MultiPlayer.submit_answer.SubmitAnswerInteractor;
import interface_adapter.MultiPlayer.receive_message.ReceiveMessageController;
import use_case.MultiPlayer.receive_message.ReceiveMessageInteractor;
import use_case.registration.login.LoginUserDataAccessInterface;
import use_case.StudySet.StudySetDataAccessInterface;
// ---------------------------

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final UserSession session = new UserSession();
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager;

    // --- DATA ACCESS OBJECTS ---
    // Using In-Memory Mocks for stable testing of Multiplayer logic
    final LoginUserDataAccessInterface userDataAccessObject = new InMemoryUserDataAccessObject(userFactory);
    final StudySetDataAccessInterface studySetDataAccessObject = new InMemoryStudySetDataAccessObject();
    final SinglePlayerDataAccessObject spDAO = new SinglePlayerDataAccessObject();

    // PeerConnection defined as a class field so listeners can access it
    private PeerConnection peerConnection;

    // ViewModels
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private MainScreenViewModel mainScreenViewModel;
    private BrowseStudySetViewModel browseStudySetViewModel;
    private LeaderboardViewModel leaderboardViewModel;
    private SinglePlayerViewModel singlePlayerViewModel;
    private final MultiPlayerViewModel multiPlayerViewModel = new MultiPlayerViewModel();

    // Views
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

        // --- LISTENER: Sync Login State with Network & View ---
        mainScreenViewModel.addPropertyChangeListener(evt -> {
            if (peerConnection != null) {
                String newUsername = mainScreenViewModel.getState().getUsername();

                if (newUsername != null && !newUsername.isEmpty()) {
                    // 1. Update Network Identity
                    peerConnection.switchIdentity(newUsername);

                    // 2. Update View Identity (So it knows whose turn it is)
                    if (multiPlayerView != null) {
                        multiPlayerView.setLocalPlayerName(newUsername);
                    }
                }
            }
        });

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
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel, mainScreenViewModel, loginViewModel, session);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, // Uses InMemory Mock for stability
                loginOutputBoundary
        );
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addMultiPlayerUseCase() {
        // 1. Initialize PeerConnection
        String currentUser = userDataAccessObject.getCurrentUsername();
        if (currentUser == null || currentUser.isEmpty()) {
            currentUser = "TestUser"; // Default for safety
        }
        this.peerConnection = new PeerConnection(currentUser);

        // 2. Initialize Network DAO
        NetworkGameDataAccessObject networkDAO = new NetworkGameDataAccessObject(peerConnection);

        // 3. Setup Receive Message Listener
        ReceiveMessageInteractor receiveInteractor = new ReceiveMessageInteractor(
                networkDAO,
                new SubmitAnswerPresenter(multiPlayerViewModel, viewManagerModel),
                userFactory,
                studySetDataAccessObject
        );
        ReceiveMessageController receiveController = new ReceiveMessageController(receiveInteractor);

        // 4. Attach Network Callback (Thread Safe)
        peerConnection.onDataRecieved(data -> {
            System.out.println(" [DEBUG] NETWORK: Raw data received!");
            SwingUtilities.invokeLater(() -> {
                try {
                    receiveController.execute(data);
                } catch (Exception e) {
                    System.out.println(" [DEBUG] ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        });

        // 5. Setup Start Match Use Case
        MPStartPresenter mpStartPresenter = new MPStartPresenter(multiPlayerViewModel, viewManagerModel);
        MPStartInteractor mpStartInteractor = new MPStartInteractor(
                mpStartPresenter,
                networkDAO,
                userDataAccessObject,
                studySetDataAccessObject
        );
        MPStartController mpStartController = new MPStartController(mpStartInteractor);

        // 6. Setup Submit Answer Use Case
        SubmitAnswerPresenter submitAnswerPresenter = new SubmitAnswerPresenter(multiPlayerViewModel, viewManagerModel);
        SubmitAnswerInteractor submitAnswerInteractor = new SubmitAnswerInteractor(
                networkDAO,
                submitAnswerPresenter
        );
        SubmitAnswerController submitAnswerController = new SubmitAnswerController(submitAnswerInteractor);

        // 7. Create and Add View
        multiPlayerView = new MultiPlayerView(multiPlayerViewModel, submitAnswerController);
        cardPanel.add(multiPlayerView, multiPlayerView.viewName);

        // 8. Inject Controller into Main Screen
        if (mainScreenView != null) {
            mainScreenView.setMPStartController(mpStartController);
        }

        // 9. Open Connection
        peerConnection.onConnection(() -> System.out.println("NETWORK: Peer Connected!"));

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
        final JFrame application = new JFrame("CourseClash [P2P ENABLED]");
        application.setSize(1200, 800);
        application.setResizable(true);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FontLoader.registerFonts();

        application.add(cardPanel);
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel, application);

        // --- INITIAL VIEW ---
        // Default to Login screen
        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange("view");

        return application;
    }
}