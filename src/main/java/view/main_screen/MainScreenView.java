package view.main_screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.studyDeck.StudyDeckViewModel;

public class MainScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "main screen";
    private final MainScreenViewModel mainScreenViewModel;
    private final ViewManagerModel viewManagerModel;
    private final StudyDeckViewModel browseStudySetViewModel;
    private final LeaderboardViewModel leaderboardViewModel;
    private final interface_adapter.SinglePlayer.SinglePlayerViewModel singlePlayerViewModel;

    public MainScreenView(MainScreenViewModel mainScreenViewModel,
                          ViewManagerModel viewManagerModel,
                          StudyDeckViewModel browseStudySetViewModel, LeaderboardViewModel leaderboardViewModel,
                          interface_adapter.SinglePlayer.SinglePlayerViewModel singlePlayerViewModel) {
        this.mainScreenViewModel = mainScreenViewModel;
        this.viewManagerModel = viewManagerModel;
        this.browseStudySetViewModel = browseStudySetViewModel;
        this.leaderboardViewModel = leaderboardViewModel;
        this.singlePlayerViewModel = singlePlayerViewModel;
        this.mainScreenViewModel.addPropertyChangeListener(this);

        // Set panel layout (this is now the root of this panel)
        this.setLayout(new BorderLayout(10, 10));

        // ---------- Title Image ----------
        ImageIcon originalTitleImage = new ImageIcon(getClass().getResource("/images/TitleImage.png"));
        int titleImageWidth = originalTitleImage.getIconWidth();
        int titleImageHeight = originalTitleImage.getIconHeight();
        Image titleImage = originalTitleImage.getImage()
                .getScaledInstance((int) (titleImageWidth * 0.9), (int) (titleImageHeight * 0.9), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(titleImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel imagePanel = new JPanel(new GridLayout());
        imagePanel.add(imageLabel);

        this.add(imagePanel, BorderLayout.NORTH);

        // ---------- 2x2 Buttons ----------
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(null);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(30, 30, 30, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;

        JButton[][] buttons = {
                {new JButton("Single Player"), new JButton("Multiplayer")},
                {new JButton("Manage Study Set"), new JButton("Leaderboard")},
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = buttons[row][col];
                button.setFont(new Font("Helvetica", Font.BOLD, 48));
                button.setBackground(Color.GRAY);
                button.setForeground(Color.WHITE);
                if ("Manage Study Set".equals(button.getText())) {
                    button.addActionListener(e -> switchToBrowseStudySet());
                }
                if ("Leaderboard".equals(button.getText())) {
                    button.addActionListener(e -> switchToLeaderboard());
                }
                if ("Single Player".equals(button.getText())) {
                    button.addActionListener(e -> switchToSinglePlayer());
                }
                if ("Multiplayer".equals(button.getText())) {
                    button.addActionListener(e -> switchToMultiPlayer());
                }

                c.gridx = col;
                c.gridy = row;
                button.setPreferredSize(new Dimension(400, 150));
                buttonPanel.add(button, c);
            }
        }

        this.add(buttonPanel, BorderLayout.CENTER);
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
        viewManagerModel.setState(browseStudySetViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    private void switchToLeaderboard() {
        viewManagerModel.setState(leaderboardViewModel.getViewName());
        leaderboardViewModel.firePropertyChange("leaderboard");
        viewManagerModel.firePropertyChange();
    }

    private void switchToSinglePlayer() {
        viewManagerModel.setState(singlePlayerViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    private void switchToMultiPlayer() {
        viewManagerModel.setState("multi player");
        viewManagerModel.firePropertyChange();
    }
}