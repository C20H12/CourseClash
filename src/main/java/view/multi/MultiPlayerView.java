package view.multi;


import javax.swing.*;

import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.peer.PeerConnection;
import interface_adapter.ViewManagerModel;
import interface_adapter.MultiPlayer.MultiPlayerController;
import interface_adapter.MultiPlayer.MultiPlayerGameState;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.user_session.UserSession;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MultiPlayerView extends JPanel implements PropertyChangeListener {

    private static final String EVENT_ADVANCE = "ADVANCE";
    private static final String EVENT_END = "END";
    private static final String EVENT_SCORE = "SCORE";

    public final String viewName = "multi player";
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
                    pregameFlowStarted = false; // allow pre-game dialog to show again next init
                    // show a dialog with all the decks for selection
                    if (multiPlayerController != null) {
                        multiPlayerController.showAllDecks();
                    }
                }
            }
        });
    }

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

    @Override
    public void removeNotify() {
        stopCountdownThread();
        super.removeNotify();
    }

    public void setMultiPlayerController(MultiPlayerController controller) {
        this.multiPlayerController = controller;
    }

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
            startCountdownThread(secondsPerQuestion);;
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
        popup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        popup.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (popup.getPeerConnection() == null || popup.getMode() == null) {
                    viewManagerModel.setState("main screen");
                    viewManagerModel.firePropertyChange();
                }
            }
        });
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

    private void handleAdvanceButton() {
        if (!hostMode) {
            return;
        }
        messageLabel.setText("Advance sent to guest.");
        stopCountdownThread();
        multiPlayerController.advance();
        sendPeerEvent(EVENT_ADVANCE);
    }

    private void handleLocalEndGame() {
        if (!hostMode) {
            return;
        }
        stopCountdownThread();
        multiPlayerController.endGame();
        sendPeerEvent(EVENT_END);
    }

    private void handleRemoteAdvance() {
        stopCountdownThread();
        multiPlayerController.advance();
    }

    private void handleRemoteEndGame() {
        stopCountdownThread();
        multiPlayerController.endGame();
    }

    private void sendScoreUpdate(int score) {
        String payload = EVENT_SCORE + ":" + score;
        peerConnection.sendData(payload);
    }

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

    private void stopCountdownThread() {
        countdownActive = false;
        if (countdownThread != null) {
            countdownThread.interrupt();
            countdownThread = null;
        }
    }

    // peer

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

        if (hostMode) return;
        // only guest needs these events
        switch (upper) {
            case EVENT_ADVANCE -> SwingUtilities.invokeLater(this::handleRemoteAdvance);
            case EVENT_END -> SwingUtilities.invokeLater(this::handleRemoteEndGame);
            default -> {
                // ignore unrecognized control packets
            }
        }
    }

    private void sendPeerEvent(String event) {
        if (peerConnection != null) {
            peerConnection.sendData(event);
        }
    }

    // utils

    private void disableOptionButtons() {
        for (Component c : optionButtonPanel.getComponents()) {
            c.setEnabled(false);
        }
    }

    private String formatCountdown(int value) {
        return String.format("Time Remaining: %02ds", Math.max(0, value));
    }

    private int extractSecondsPerQuestion(Map<String, Object> settings) {
        if (settings == null || settings.isEmpty()) {
            return 10;
        }
        Object value = settings.get("secondsPerQuestion");
        if (value instanceof Number) {
            return Math.max(1, ((Number)value).intValue());
        }
        if (value instanceof String) {
            try {
                return Math.max(1, Integer.parseInt((String)value));
            } catch (NumberFormatException ignored) {
            }
        }
        return 10;
    }

    public String getViewName() {
        return viewName;
    }
}