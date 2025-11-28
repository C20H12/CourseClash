package view.single;

// UI for single player
import interface_adapter.SinglePlayer.SinglePlayerController;
import interface_adapter.SinglePlayer.SinglePlayerState;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import interface_adapter.user_session.UserSession;
import interface_adapter.ViewManagerModel;
import use_case.DataAccessException;
import javax.swing.*;

import entity.User;
import entity.DeckManagement.StudyDeck;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

public class SinglePlayerView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "single player";

    private final SinglePlayerViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private SinglePlayerController controller;

    private UserSession session;

    // --- Top bar ---
    private final JLabel questionCounterLabel = new JLabel("Q 0/0");
    private final JLabel scoreLabel = new JLabel("0");
    private final JLabel timerLabel = new JLabel("0");  // placeholder until real timer

    // --- Center question/answers ---
    private final JTextArea questionTextArea = new JTextArea();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionsGroup = new ButtonGroup();

    // --- Bottom controls / feedback ---
    private final JButton submitButton = new JButton("Submit");
    private final JButton endGameButton = new JButton("End Game");
    private final JLabel messageLabel = new JLabel(" ");
    private final JLabel accuracyLabel = new JLabel(" ");
    private final JLabel avgTimeLabel = new JLabel(" ");
    private static final int BASE_FONT_SIZE = 18;
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, BASE_FONT_SIZE);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 16);

    public SinglePlayerView(SinglePlayerViewModel viewModel, ViewManagerModel viewManagerModel, UserSession session) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.session = session;

        this.viewModel.addPropertyChangeListener(this);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // when this first loads
        this.viewManagerModel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue().equals(viewName)) {
                    // show a dialog with all the decks for selection
                    controller.showAllDecks();
                }
            }
        });

        setLayout(new BorderLayout());
        buildTopBar();
        buildQuestionArea();
        buildBottomBar();

    }

    private void buildTopBar() {
        JPanel top = new JPanel(new GridLayout(1, 3, 20, 0));

        questionCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        questionCounterLabel.setFont(TITLE_FONT);
        scoreLabel.setFont(TITLE_FONT);
        timerLabel.setFont(TITLE_FONT);

        top.add(wrapWithTitle("Question", questionCounterLabel));
        top.add(wrapWithTitle("Total Score", scoreLabel));
        top.add(wrapWithTitle("Question Timer", timerLabel));

        add(top, BorderLayout.NORTH);
    }

    private JPanel wrapWithTitle(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(label, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void buildQuestionArea() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFont(new Font("SansSerif", Font.PLAIN, 20));
        questionTextArea.setOpaque(false);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JScrollPane questionScroll = new JScrollPane(questionTextArea);
        questionScroll.setBorder(null);
        questionScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        center.add(questionScroll, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setFont(LABEL_FONT);
            optionButtons[i].setFocusPainted(false);
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
    }
        center.add(optionsPanel, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }
    private void buildBottomBar() {
            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JPanel buttonsPanel = new JPanel();
            submitButton.setFont(BUTTON_FONT);
            endGameButton.setFont(BUTTON_FONT);

            submitButton.setPreferredSize(new Dimension(120, 35));
            endGameButton.setPreferredSize(new Dimension(120, 35));

            buttonsPanel.add(submitButton);
            buttonsPanel.add(endGameButton);

            submitButton.addActionListener(this);
            endGameButton.addActionListener(this);

            JPanel statsPanel = new JPanel(new GridLayout(3, 1));
            statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            messageLabel.setFont(LABEL_FONT);
            accuracyLabel.setFont(LABEL_FONT);
            avgTimeLabel.setFont(LABEL_FONT);

            statsPanel.add(messageLabel);
            statsPanel.add(accuracyLabel);
            statsPanel.add(avgTimeLabel);

            bottom.add(buttonsPanel, BorderLayout.NORTH);
            bottom.add(statsPanel, BorderLayout.CENTER);

            add(bottom, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(SinglePlayerController controller) {
        this.controller = controller;
    }

    /**
     * Called by MainScreen or BrowseStudySetView to start the game.
     */
    public void startGame(String deckTitle,
                          User user,
                          int timerPerQuestion,
                          boolean shuffle,
                          int numQuestions) throws DataAccessException {

        if (controller != null) {
            controller.startGame(user, deckTitle, timerPerQuestion, shuffle, numQuestions);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (controller == null) {
            return;
        }

        if (e.getSource() == submitButton) {
            String selected = getSelectedOptionText();
            if (selected != null) {
                try {
                    controller.submitAnswer(selected);
                } catch (DataAccessException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                messageLabel.setText("Please select an answer.");
            }

        } else if (e.getSource() == endGameButton) {
            try {
                if (!viewModel.getState().isGameOver()){
                    controller.endGame();
                }
                viewManagerModel.setState("main screen");
                viewManagerModel.firePropertyChange();
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String getSelectedOptionText() {
        for (JRadioButton button : optionButtons) {
            if (button.isVisible() && button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("initShowAllDeckNames")) {
            List<StudyDeck> allDecks = viewModel.getState().getDecksList();
            SinglePlayerSelectSetPopup popup = new SinglePlayerSelectSetPopup(allDecks);
            popup.setVisible(true);
            popup.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
            StudyDeck selectedDeck = popup.getSelectedDeck();
            if (selectedDeck != null) {
                try {
                    // start game with selected deck
                    startGame(selectedDeck.getTitle(), session.getUser(), 10, false, selectedDeck.getCardCount());
                }
                catch (DataAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                viewManagerModel.setState("main screen");
                viewManagerModel.firePropertyChange();
            }
            return;
        }
        SinglePlayerState state = viewModel.getState();

        int qLeft = state.getTotal() - state.getCurrentIndex() + 1;
        questionCounterLabel.setText("Q " + qLeft + "/" + state.getTotal());
        scoreLabel.setText(String.valueOf(state.getScore()));

        questionTextArea.setText(state.getQuestionText());

        List<String> options = state.getOptions();
        optionsGroup.clearSelection();

        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.size()) {
                optionButtons[i].setText(options.get(i));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        messageLabel.setText(state.getMessage() != null ? state.getMessage() : " ");

        if (state.isGameOver()) {
            accuracyLabel.setText(String.format("Accuracy: %.1f%%", state.getAccuracy()));
            avgTimeLabel.setText(String.format("Avg. response time: %.2f s", state.getAvgResponseTime()));
            submitButton.setEnabled(false);
        } else {
            accuracyLabel.setText(" ");
            avgTimeLabel.setText(" ");
            submitButton.setEnabled(true);
        }

        if (state.getError() != null && !state.getError().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
