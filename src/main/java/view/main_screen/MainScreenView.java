package view.main_screen;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.MultiPlayer.start_match.MPStartController;
import interface_adapter.studyset.studyset_browse.BrowseStudySetViewModel;
import use_case.DataAccessException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "main screen";
    private final MainScreenViewModel mainScreenViewModel;
    private final ViewManagerModel viewManagerModel;

    private final BrowseStudySetViewModel browseStudySetViewModel;
    private final LeaderboardViewModel leaderboardViewModel;
    // private final SinglePlayerViewModel singlePlayerViewModel;

    private MPStartController mpStartController;

    public MainScreenView(MainScreenViewModel mainScreenViewModel,
                          ViewManagerModel viewManagerModel,
                          BrowseStudySetViewModel browseStudySetViewModel,
                          LeaderboardViewModel leaderboardViewModel
            /* Add SinglePlayerViewModel here if needed */) {

        this.mainScreenViewModel = mainScreenViewModel;
        this.viewManagerModel = viewManagerModel;
        this.browseStudySetViewModel = browseStudySetViewModel;
        this.leaderboardViewModel = leaderboardViewModel;
        // this.singlePlayerViewModel = singlePlayerViewModel;

        this.mainScreenViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout(10, 10));

        try {
            ImageIcon originalTitleImage = new ImageIcon("images/TitleImage.png");
            if (originalTitleImage.getIconWidth() > 0) {
                int w = originalTitleImage.getIconWidth();
                int h = originalTitleImage.getIconHeight();
                Image titleImage = originalTitleImage.getImage()
                        .getScaledInstance((int)(w*0.9), (int)(h*0.9), Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(titleImage));
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                JPanel imagePanel = new JPanel(new GridLayout());
                imagePanel.add(imageLabel);
                this.add(imagePanel, BorderLayout.NORTH);
            }
        } catch (Exception e) {
            System.out.println("Image not found, skipping.");
        }

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(30, 30, 30, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;

        JButton singlePlayerButton = new JButton("Single Player");
        JButton multiplayerButton = new JButton("Multiplayer");
        JButton manageSetButton = new JButton("Manage Study Set");
        JButton leaderboardButton = new JButton("Leaderboard");

        manageSetButton.addActionListener(e -> switchToBrowseStudySet());
        leaderboardButton.addActionListener(e -> switchToLeaderboard());
        singlePlayerButton.addActionListener(e -> System.out.println("Single Player clicked (WIP)"));

        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String opponent = JOptionPane.showInputDialog("Enter Opponent Username:");
                if (opponent == null || opponent.isEmpty()) return;

                String deckName = JOptionPane.showInputDialog("Enter Deck Name:");
                if (deckName == null || deckName.isEmpty()) return;

                String currentUser = JOptionPane.showInputDialog("Confirm your username (for testing):");

                if (mpStartController != null) {
                    // Assuming execute() does NOT throw checked exceptions
                    try {
                        mpStartController.execute(currentUser, opponent, deckName);
                    } catch (DataAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Multiplayer Controller not connected.");
                }
            }
        });

        JButton[][] buttons = {
                {singlePlayerButton, multiplayerButton},
                {manageSetButton, leaderboardButton}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = buttons[row][col];
                button.setFont(new Font("Helvetica", Font.BOLD, 32));
                button.setBackground(Color.GRAY);
                button.setForeground(Color.WHITE);
                c.gridx = col;
                c.gridy = row;
                button.setPreferredSize(new Dimension(400, 150));
                buttonPanel.add(button, c);
            }
        }
        this.add(buttonPanel, BorderLayout.CENTER);
    }

    public void setMPStartController(MPStartController controller) {
        this.mpStartController = controller;
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    private void switchToBrowseStudySet() {
        if (browseStudySetViewModel != null) {
            viewManagerModel.setState(browseStudySetViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        }
    }

    private void switchToLeaderboard() {
        if (leaderboardViewModel != null) {
            viewManagerModel.setState(leaderboardViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        }
    }
}