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
    private String localPlayerName = "";

    // Components
    private final JLabel player1ScoreLabel = new JLabel("Player 1: 0");
    private final JLabel player2ScoreLabel = new JLabel("Player 2: 0");
    private final JLabel turnLabel = new JLabel("Current Turn: -");
    private final JLabel cardLabel = new JLabel("Waiting...");
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
        gbc.gridx = 0; gbc.gridy = 0;
        this.add(scorePanel, gbc);

        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        this.add(turnLabel, gbc);

        cardLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2; gbc.weighty = 0.2;
        this.add(cardLabel, gbc);

        optionButtonPanel.setLayout(new GridLayout(0, 2, 20, 20));
        gbc.gridy = 3; gbc.weighty = 0.5; gbc.fill = GridBagConstraints.BOTH;
        this.add(optionButtonPanel, gbc);

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 4; gbc.weighty = 0.1;
        this.add(messageLabel, gbc);
    }

    public void setLocalPlayerName(String name) {
        this.localPlayerName = name;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MultiPlayerState state = (MultiPlayerState) evt.getNewValue();

        System.out.println("VIEW UPDATE: Turn=" + state.getCurrentTurnUser() + ", Me=" + localPlayerName);

        player1ScoreLabel.setText(state.getPlayerA() + ": " + state.getScoreA());
        player2ScoreLabel.setText(state.getPlayerB() + ": " + state.getScoreB());
        turnLabel.setText("Current Turn: " + state.getCurrentTurnUser());
        messageLabel.setText(state.getMessage());

        optionButtonPanel.removeAll();

        boolean isMyTurn = localPlayerName != null && localPlayerName.equals(state.getCurrentTurnUser());
        if (isMyTurn) System.out.println("VIEW: It is my turn! Buttons enabled.");
        else System.out.println("VIEW: Not my turn. Buttons disabled.");

        if (state.isGameOver()) {
            cardLabel.setText("GAME OVER");
        } else if (state.getCurrentCard() != null) {
            cardLabel.setText("<html><center>" + state.getCurrentCard().getQuestion() + "</center></html>");

            for (String option : state.getCurrentCard().getAnswers()) {
                JButton btn = new JButton(option);
                btn.setFont(new Font("Arial", Font.PLAIN, 16));
                btn.setEnabled(isMyTurn);

                btn.addActionListener(e -> {
                    for(Component c : optionButtonPanel.getComponents()) c.setEnabled(false);
                    submitAnswerController.execute(option, localPlayerName);
                });
                optionButtonPanel.add(btn);
            }
        }

        optionButtonPanel.revalidate();
        optionButtonPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}