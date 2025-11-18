package view.leaderboard;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class LeaderboardView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "leaderboard";
    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private LeaderboardController leaderboardController = null;

    // Panels for leaderboards
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JPanel levelLeaderboardPanel = new JPanel();
    private final JPanel experiencePointsLeaderboardPanel = new JPanel();
    private final JPanel questionsAnsweredLeaderboardPanel = new JPanel();
    private final JPanel questionsCorrectLeaderboardPanel = new JPanel();

    public LeaderboardView(LeaderboardViewModel leaderboardViewModel, ViewManagerModel viewManagerModel) {
        // Initialize leaderboard view components here
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());

        // adding tabs for different leaderboards
        tabbedPane.addTab("levelLeaderboard", levelLeaderboardPanel);
        tabbedPane.addTab("experiencePointsLeaderboard", experiencePointsLeaderboardPanel);
        tabbedPane.addTab("questionsAnsweredLeaderboard", questionsAnsweredLeaderboardPanel);
        tabbedPane.addTab("questionsCorrectLeaderboard", questionsCorrectLeaderboardPanel);
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Handle action events here
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        // Handle property change events here
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        // Set the controller for the leaderboard view
        this.leaderboardController = leaderboardController;
    }
}
