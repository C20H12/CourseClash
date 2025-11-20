package view;

import interface_adapter.MultiPlayer.MultiPlayerState;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.MultiPlayer.submit_answer.SubmitAnswerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MultiPlayerView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "multiplayer game";

    private final MultiPlayerViewModel viewModel;
    private final SubmitAnswerController submitAnswerController;

    // UI Components
    private final JLabel player1ScoreLabel = new JLabel("Player 1: 0");
    private final JLabel player2ScoreLabel = new JLabel("Player 2: 0");
    private final JLabel turnLabel = new JLabel("Current Turn: -");
    private final JLabel cardLabel = new JLabel("Waiting for game...");
    private final JLabel messageLabel = new JLabel(" ");

    private final JTextField answerInputField = new JTextField(15);
    private final JButton submitButton = new JButton(MultiPlayerViewModel.SUBMIT_BUTTON_LABEL);

    public MultiPlayerView(MultiPlayerViewModel viewModel, SubmitAnswerController controller) {
        this.viewModel = viewModel;
        this.submitAnswerController = controller;
        this.viewModel.addPropertyChangeListener(this);

        // 1. Main Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // 2. Score Panel (Top)
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        player1ScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        player2ScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);
        this.add(scorePanel);

        this.add(Box.createVerticalStrut(20)); // Spacer

        // 3. Turn Indicator
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        turnLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        this.add(turnLabel);

        this.add(Box.createVerticalStrut(30)); // Spacer

        // 4. Question/Card Area
        cardLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(cardLabel);

        this.add(Box.createVerticalStrut(30)); // Spacer

        // 5. Input Area
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Your Answer: "));
        inputPanel.add(answerInputField);
        this.add(inputPanel);

        // 6. Submit Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        this.add(buttonPanel);

        // 7. Feedback Message
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setForeground(Color.BLUE);
        this.add(messageLabel);

        // --- ACTION LISTENERS ---

        // Submit Logic
        submitButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(submitButton)) {
                            MultiPlayerState currentState = viewModel.getState();
                            String answer = answerInputField.getText();
                            String user = currentState.getCurrentTurnUser();

                            if (answer.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Please enter an answer.");
                                return;
                            }

                            // Execute Controller
                            submitAnswerController.execute(answer, user);

                            // Clear input
                            answerInputField.setText("");
                        }
                    }
                }
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MultiPlayerState state = (MultiPlayerState) evt.getNewValue();

        // Update Scores
        player1ScoreLabel.setText("Player 1 (" + state.getPlayerA() + "): " + state.getScoreA());
        player2ScoreLabel.setText("Player 2 (" + state.getPlayerB() + "): " + state.getScoreB());

        // Update Turn
        turnLabel.setText("Current Turn: " + state.getCurrentTurnUser());

        // Update Card/Question
        if (state.getCurrentCard() != null) {
            // Assuming StudyCard has getQuestion(). If it's getTerm(), change this line!
            cardLabel.setText(state.getCurrentCard().getQuestion());
        } else {
            cardLabel.setText("Game Over!"); // Or empty if waiting
        }

        // Update Message
        messageLabel.setText(state.getMessage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Required by interface, but we use anonymous inner class above
    }
}
