package view.singleplayer;


import interface_adapter.SinglePlayer.SinglePlayerController;
import interface_adapter.SinglePlayer.SinglePlayerState;
import interface_adapter.SinglePlayer.SinglePlayerViewModel;
import interface_adapter.ViewManagerModel;
import use_case.DataAccessException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class SinglePlayerView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "single player";

    private final SinglePlayerViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private SinglePlayerController controller;

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

    public SinglePlayerView(SinglePlayerViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        buildTopBar();
        buildQuestionArea();
        buildBottomBar();
    }

    private void buildTopBar() {
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 3));

        questionCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        top.add(wrapWithTitle("Questions Left", questionCounterLabel));
        top.add(wrapWithTitle("Total Score", scoreLabel));
        top.add(wrapWithTitle("Question Timer", timerLabel));

        add(top, BorderLayout.NORTH);
    }

    private JPanel wrapWithTitle(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void buildQuestionArea() {
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());

        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setFont(questionTextArea.getFont().deriveFont(16f));

        JScrollPane questionScroll = new JScrollPane(questionTextArea);
        center.add(questionScroll, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 2, 2));

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        center.add(optionsPanel, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void buildBottomBar() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(submitButton);
        buttonsPanel.add(endGameButton);

        submitButton.addActionListener(this);
        endGameButton.addActionListener(this);

        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
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
                          entity.User user,
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
                controller.endGame();
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
        SinglePlayerState state = viewModel.getState();

        questionCounterLabel.setText("Q " + state.getCurrentIndex() + "/" + state.getTotal());
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
