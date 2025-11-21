//Mahir
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
import java.util.ArrayList;

public class MultiPlayerView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "multiplayer game";

    private final MultiPlayerViewModel viewModel;
    private final SubmitAnswerController submitAnswerController;

    private final JLabel player1ScoreLabel = new JLabel("Player 1: 0");
    private final JLabel player2ScoreLabel = new JLabel("Player 2: 0");
    private final JLabel turnLabel = new JLabel("Current Turn: -");
    private final JLabel cardLabel = new JLabel("Waiting for game...");
    private final JLabel messageLabel = new JLabel(" ");

    private final JPanel optionButtonPanel = new JPanel();

    public MultiPlayerView(MultiPlayerViewModel viewModel, SubmitAnswerController controller) {
        this.viewModel = viewModel;
        this.submitAnswerController = controller;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new GridBagLayout());
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
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(scorePanel, gbc);

        turnLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.weighty = 0;
        this.add(turnLabel, gbc);

        cardLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 2;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(cardLabel, gbc);

        optionButtonPanel.setLayout(new GridLayout(0, 2, 20, 20));

        gbc.gridy = 3;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(optionButtonPanel, gbc);

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLUE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        this.add(messageLabel, gbc);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MultiPlayerState state = (MultiPlayerState) evt.getNewValue();

        player1ScoreLabel.setText("Player 1 (" + state.getPlayerA() + "): " + state.getScoreA());
        player2ScoreLabel.setText("Player 2 (" + state.getPlayerB() + "): " + state.getScoreB());
        turnLabel.setText("Current Turn: " + state.getCurrentTurnUser());
        messageLabel.setText(state.getMessage());

        optionButtonPanel.removeAll();

        if (state.isGameOver()) {
            cardLabel.setText("--- MATCH COMPLETE ---");
        }
        else if (state.getCurrentCard() != null) {
            cardLabel.setText("<html><div style='text-align: center;'>" + state.getCurrentCard().getQuestion() + "</div></html>");

            ArrayList<String> options = state.getCurrentCard().getAnswers();
            for (String optionText : options) {
                JButton optionButton = new JButton(optionText);
                optionButton.setFont(new Font("Arial", Font.PLAIN, 18));
                optionButton.setFocusPainted(false);

                optionButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (Component c : optionButtonPanel.getComponents()) {
                            c.setEnabled(false);
                        }
                        String currentUser = state.getCurrentTurnUser();
                        submitAnswerController.execute(optionText, currentUser);
                    }
                });

                optionButtonPanel.add(optionButton);
            }
        } else {
            cardLabel.setText("Waiting for next card...");
        }

        optionButtonPanel.revalidate();
        optionButtonPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}