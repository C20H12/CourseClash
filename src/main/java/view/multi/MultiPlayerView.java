package view.multi;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.peer.PeerConnection;
import interface_adapter.MultiPlayer.MultiPlayerController;
import interface_adapter.MultiPlayer.MultiPlayerGameState;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.user_session.UserSession;

/**
 * The main Swing panel for the multiplayer game mode.
 * Handles UI rendering, user input, and synchronizes state via the ViewModel and PeerConnection.
 * Implements PropertyChangeListener to react to ViewModel updates.
 */
public class MultiPlayerView extends JPanel implements PropertyChangeListener {

    private static final String EVENT_ADVANCE = "ADVANCE";
    private static final String EVENT_END = "END";
    private static final String EVENT_SCORE = "SCORE";

    private String viewName = "multi player";
    private ViewManagerModel viewManagerModel;
    private final MultiPlayerViewModel viewModel;
    private MultiPlayerController multiPlayerController;
    private UserSession userSession;

    private PeerConnection peerConnection;
    private boolean hostMode;
    private boolean pregameFlowStarted;

    // Components
    private final JLabel player1ScoreLabel = new JLabel(": 0");
    private final JLabel player2ScoreLabel = new JLabel(": 0");
    private final JLabel countdownLabel = new JLabel("Countdown: --");
    private final JProgressBar countdownProgress = new JProgressBar();
    private final JLabel cardLabel = new JLabel("Waiting...");
    private final JLabel resultLabel = new JLabel(" ");
    private final JLabel messageLabel = new JLabel(" ");
    private final JPanel optionButtonPanel = new JPanel();
    private final JButton advanceButton = new JButton("Advance");
    private final JButton endGameButton = new JButton("End Game");

    private Thread countdownThread;
    private volatile boolean countdownActive;
    private int secondsPerQuestion = -1;

    /**
     * Constructs the MultiPlayerView and sets up initial listeners.
     * @param viewModel The view model containing the multiplayer game state.
     * @param viewManagerModel The manager for switching between different views.
     * @param userSession The current user's session data.
     */
    public MultiPlayerView(MultiPlayerViewModel viewModel, ViewManagerModel viewManagerModel, UserSession userSession) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.userSession = userSession;
        this.viewModel.addPropertyChangeListener(this);
        initUi();

        // when this first loads
        this.viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (viewName.equals(evt.getNewValue())) {
                    // allow pre-game dialog to show again next init
                    pregameFlowStarted = false;
                    // show a dialog with all the decks for selection
                    if (multiPlayerController != null) {
                        multiPlayerController.showAllDecks();
                    }
                }
            }
        });
    }

    /**
     * Initializes the UI components and layout constraints.
     */
    private void initUi() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(scorePanel, gbc);

        JPanel countdownGroup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countdownGroup.add(countdownLabel, gbc);
        countdownGroup.add(countdownProgress, gbc);
        gbc.gridy = 1;
        add(countdownGroup, gbc);

        cardLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        add(cardLabel, gbc);

        optionButtonPanel.setLayout(new GridLayout(0, 2, 20, 20));
        gbc.gridy = 3;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        add(optionButtonPanel, gbc);

        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(resultLabel, gbc);

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 5;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(messageLabel, gbc);

        advanceButton.setEnabled(false);
        advanceButton.addActionListener(e -> handleAdvanceButton());
        endGameButton.setEnabled(false);
        endGameButton.addActionListener(e -> handleLocalEndGame());
        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlRow.add(advanceButton);
        controlRow.add(endGameButton);
        gbc.gridy = 6;
        gbc.weighty = 0;
        add(controlRow, gbc);
    }

    /**
     * Cleans up resources (e.g., stopping threads) when the component is removed.
     */
    @Override
    public void removeNotify() {
        stopCountdownThread();
        super.removeNotify();
    }

    /**
     * Sets the controller for handling user actions.
     * @param controller The MultiPlayerController instance.
     */
    public void setMultiPlayerController(MultiPlayerController controller) {
        this.multiPlayerController = controller;
    }

    /**
     * Responds to property changes from the ViewModel to update the UI.
     * @param evt The property change event containing the new state.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("init".equals(evt.getPropertyName()) && !pregameFlowStarted) {
            pregameFlowStarted = true;
            List<StudyDeck> decks = viewModel.getState().getAvailableDecks();
            showPregameDialog(decks);
            return;
        }

        MultiPlayerGameState state = (MultiPlayerGameState) evt.getNewValue();

        if ("question".equals(evt.getPropertyName())) {
            StudyCard currentCard = state.getCurrentCard();
            if (currentCard != null) {
                cardLabel.setText("<html><center>" + currentCard.getQuestion() + "</center></html>");
                optionButtonPanel.removeAll();
                for (String option : currentCard.getAnswers()) {
                    JButton btn = new JButton(option);
                    btn.setFont(new Font("Arial", Font.PLAIN, 16));
                    btn.setEnabled(true);
                    btn.addActionListener(e -> {
                        disableOptionButtons();
                        multiPlayerController.chooseAnswer(option, hostMode);
                    });
                    optionButtonPanel.add(btn);
                }
                optionButtonPanel.revalidate();
                optionButtonPanel.repaint();
            } else {
                cardLabel.setText("Waiting for host...");
            }
            player1ScoreLabel.setText(state.getPlayerA() + ": " + state.getScoreA());
            player2ScoreLabel.setText(state.getPlayerB() + ": " + state.getScoreB());
            messageLabel.setText(state.getMessage());
            resultLabel.setText("");
            startCountdownThread(secondsPerQuestion);
        }

        if ("end".equals(evt.getPropertyName())) {
            stopCountdownThread();
            countdownLabel.setText("Countdown complete");
            countdownProgress.setValue(0);
            resultLabel.setText(state.getRoundResult());
            cardLabel.setText("GAME OVER");

            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Game Over!\n" + state.getRoundResult(),
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE
            );
            viewManagerModel.setState("main screen");
            viewManagerModel.firePropertyChange();
        }

        if ("update".equals(evt.getPropertyName())) {
            stopCountdownThread();
            player1ScoreLabel.setText(state.getPlayerA() + ": " + state.getScoreA());
            player2ScoreLabel.setText(state.getPlayerB() + ": " + state.getScoreB());
            messageLabel.setText(state.getMessage());
            resultLabel.setText(state.getRoundResult());

            if (hostMode) {
                sendScoreUpdate(state.getScoreA());
            } else {
                sendScoreUpdate(state.getScoreB());
            }
        }

    }

    // pregame

    /**
     * Displays the pre-game setup dialog to select decks and establish peer connection.
     * @param decks The list of available study decks.
     */
    private void showPregameDialog(List<StudyDeck> decks) {
        if (decks.isEmpty()) {
            messageLabel.setText("No study decks available.");
            return;
        }

        MultiPlayerPregamePopup popup = new MultiPlayerPregamePopup(decks, userSession.getUser());
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            popup.setLocationRelativeTo(window);
        }
        popup.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        popup.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        popup.pack();
        popup.setVisible(true);

        if (popup.getPeerConnection() == null || popup.getMode() == null) {
            return;
        }

        peerConnection = popup.getPeerConnection();
        StudyDeck selectedDeck = popup.getSelectedDeck();
        Map<String, Object> popupSettings = popup.getSettings();
        secondsPerQuestion = extractSecondsPerQuestion(popupSettings);

        hostMode = "host".equalsIgnoreCase(popup.getMode());
        advanceButton.setEnabled(hostMode);
        endGameButton.setEnabled(hostMode);

        if (hostMode) {
            multiPlayerController.startGame(selectedDeck, userSession.getUser(), popup.getOtherUser());
        } else {
            multiPlayerController.startGame(selectedDeck, popup.getOtherUser(), userSession.getUser());
        }

        peerConnection.onDataRecieved(this::handlePeerMessage);
    }

    // actions

    /**
     * Handles the 'Advance' button click (Host only).
     * Sends the advance signal to the guest and updates the local controller.
     */
    private void handleAdvanceButton() {
        if (!hostMode) {
            return;
        }
        messageLabel.setText("Advance sent to guest.");
        stopCountdownThread();
        multiPlayerController.advance();
        sendPeerEvent(EVENT_ADVANCE);
    }

    /**
     * Handles the 'End Game' button click (Host only).
     * Sends the end signal to the guest and terminates the local game.
     */
    private void handleLocalEndGame() {
        if (!hostMode) {
            return;
        }
        stopCountdownThread();
        multiPlayerController.endGame();
        sendPeerEvent(EVENT_END);
    }

    /**
     * Handles the 'Advance' signal received from the host.
     */
    private void handleRemoteAdvance() {
        stopCountdownThread();
        multiPlayerController.advance();
    }

    /**
     * Handles the 'End Game' signal received from the host.
     */
    private void handleRemoteEndGame() {
        stopCountdownThread();
        multiPlayerController.endGame();
    }

    /**
     * Sends the local player's score to the connected peer.
     * @param score The current score to transmit.
     */
    private void sendScoreUpdate(int score) {
        String payload = EVENT_SCORE + ":" + score;
        peerConnection.sendData(payload);
    }

    /**
     * Updates the UI with the score received from the peer.
     * @param scoreString The raw score string received (e.g., "SCORE:5").
     */
    private void updatePeerScore(String scoreString) {
        String[] parts = scoreString.split(":", 2);
        if (hostMode) {
            String currText = player2ScoreLabel.getText();
            player2ScoreLabel.setText(currText.split(":")[0] + ": " + parts[1]);
        } else {
            String currText = player1ScoreLabel.getText();
            player1ScoreLabel.setText(currText.split(":")[0] + ": " + parts[1]);
        }
        int score;
        try {
            score = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return;
        }
        multiPlayerController.updateOtherPlayerScore(score, hostMode);
    }

    // countdown

    /**
     * Starts the countdown timer for the current question.
     * @param seconds The number of seconds for the countdown.
     */
    private void startCountdownThread(int seconds) {
        countdownActive = true;
        countdownLabel.setText(formatCountdown(seconds));
        countdownProgress.setMaximum(seconds);
        resultLabel.setText(" ");
        countdownThread = new Thread(() -> {
            int remaining = seconds;
            try {
                while (countdownActive && remaining > 0) {
                    Thread.sleep(1000);
                    remaining--;
                    final int value = remaining;
                    SwingUtilities.invokeLater(() -> {
                        countdownLabel.setText(formatCountdown(value));
                        countdownProgress.setValue(seconds - value);
                    });
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                return;
            }

            if (countdownActive) {
                SwingUtilities.invokeLater(() -> {
                    countdownLabel.setText("Countdown complete");
                    countdownProgress.setValue(seconds);
                    disableOptionButtons();
                    multiPlayerController.chooseAnswer("", hostMode);
                });
            }
        }, "MP-Countdown");
        countdownThread.setDaemon(true);
        countdownThread.start();
    }

    /**
     * Stops the currently running countdown thread.
     */
    private void stopCountdownThread() {
        countdownActive = false;
        if (countdownThread != null) {
            countdownThread.interrupt();
            countdownThread = null;
        }
    }

    // peer

    /**
     * Processes incoming data messages from the peer connection.
     * @param rawMessage The raw string message received.
     */
    private void handlePeerMessage(String rawMessage) {
        if (rawMessage == null) {
            return;
        }
        String trimmed = rawMessage.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        String upper = trimmed.toUpperCase(Locale.ROOT);
        if (upper.startsWith(EVENT_SCORE + ":")) {
            SwingUtilities.invokeLater(() -> updatePeerScore(trimmed));
            return;
        }

        if (hostMode) {
            return;
        }
        // only guest needs these events
        switch (upper) {
            case EVENT_ADVANCE -> SwingUtilities.invokeLater(this::handleRemoteAdvance);
            case EVENT_END -> SwingUtilities.invokeLater(this::handleRemoteEndGame);
            default -> {
                // ignore unrecognized control packets
            }
        }
    }

    /**
     * Sends a control event string to the connected peer.
     * @param event The event string (e.g., ADVANCE, END).
     */
    private void sendPeerEvent(String event) {
        if (peerConnection != null) {
            peerConnection.sendData(event);
        }
    }

    // utils

    /**
     * Disables all answer option buttons in the UI.
     */
    private void disableOptionButtons() {
        for (Component c : optionButtonPanel.getComponents()) {
            c.setEnabled(false);
        }
    }

    /**
     * Formats the countdown seconds into a displayable string.
     * @param value The seconds remaining.
     * @return The formatted string (e.g., "Time Remaining: 05s").
     */
    private String formatCountdown(int value) {
        return String.format("Time Remaining: %02ds", Math.max(0, value));
    }

    /**
     * Parses the 'secondsPerQuestion' setting from the map, dealing with type safety.
     * @param settings The map of settings.
     * @return The integer value for seconds per question, defaulting to 10.
     */
    private int extractSecondsPerQuestion(Map<String, Object> settings) {
        if (settings == null || settings.isEmpty()) {
            return 10;
        }
        Object value = settings.get("secondsPerQuestion");
        if (value instanceof Number) {
            return Math.max(1, ((Number) value).intValue());
        }
        if (value instanceof String) {
            try {
                return Math.max(1, Integer.parseInt((String) value));
            } catch (NumberFormatException ignored) {
                ignored.printStackTrace();
            }
        }
        return 10;
    }

    /**
     * Retrieves the name associated with this view.
     * @return The view name string.
     */
    public String getViewName() {
        return viewName;
    }
}