package view.leaderboard;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardViewModel;
import interface_adapter.main_screen.MainScreenViewModel;
import use_case.leaderboard.LeaderboardType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class LeaderboardView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "leaderboard";
    private final LeaderboardViewModel leaderboardViewModel;
    private final ViewManagerModel viewManagerModel;
    private final MainScreenViewModel mainScreenViewModel;
    private LeaderboardController leaderboardController = null;

    // Panels for leaderboards
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel levelLeaderboardPanel = new JPanel();
    private JPanel experiencePointsLeaderboardPanel = new JPanel();
    private JPanel questionsAnsweredLeaderboardPanel = new JPanel();
    private JPanel questionsCorrectLeaderboardPanel = new JPanel();

    public LeaderboardView(LeaderboardViewModel leaderboardViewModel, ViewManagerModel viewManagerModel,
                           MainScreenViewModel mainScreenViewModel) {
        // Initialize leaderboard view components here
        this.leaderboardViewModel = leaderboardViewModel;
        this.viewManagerModel = viewManagerModel;
        this.mainScreenViewModel = mainScreenViewModel; // because back button goes to main screen

        setLayout(new BorderLayout());
        levelLeaderboardPanel = createContainerPanel(LeaderboardType.LEVEL);
        experiencePointsLeaderboardPanel = createContainerPanel(LeaderboardType.EXPERIENCE_POINTS);
        questionsAnsweredLeaderboardPanel = createContainerPanel(LeaderboardType.QUESTIONS_ANSWERED);
        questionsCorrectLeaderboardPanel = createContainerPanel(LeaderboardType.QUESTIONS_CORRECT);

        // adding tabs for different leaderboards
        tabbedPane.addTab("levelLeaderboard", levelLeaderboardPanel);
        tabbedPane.addTab("experiencePointsLeaderboard", experiencePointsLeaderboardPanel);
        tabbedPane.addTab("questionsAnsweredLeaderboard", questionsAnsweredLeaderboardPanel);
        tabbedPane.addTab("questionsCorrectLeaderboard", questionsCorrectLeaderboardPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // ---------- Bottom Buttons ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton backButton = createStyledButton("Back");
        buttonPanel.add(backButton);
        backButton.addActionListener(e -> {
            switchToMainScreen();
        });
        add(buttonPanel, BorderLayout.SOUTH);

        leaderboardViewModel.addPropertyChangeListener(this);
        setupTabListener();
    }

    // ---------- Leaderboard Panels ----------
    private JPanel createContainerPanel(LeaderboardType leaderboardType) {

        /*
         * containerPanel holds 2 panels: leaderboardPanel and myRankPanel
         * leaderboardPanel holds the leaderboard table
         * myRankPanel holds the user's personal raking under the leaderboard
         */

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        // initialize both panels
        JPanel leaderboardPanel = new JPanel();
        JPanel myRankPanel = new JPanel();

        // set margin around leaderboardPanel
        leaderboardPanel.setLayout(new BorderLayout());
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        String[] header = { "Rank", "Username", "Level",
                "Experience Points", "Questions Answered", "Questions Correct" };
        DefaultTableModel leaderboardTableModel = new DefaultTableModel(header, 0); // 0 = start with no data
        // get the leaderboard
        ArrayList<Object> leaderboardAsArray = leaderboardViewModel.getLeaderboardByType(leaderboardType);
            for (Object rowObj : leaderboardAsArray) {
                Object[] row = (Object[]) rowObj;
                leaderboardTableModel.addRow(row);
            }
        JTable leaderboardTable = new JTable(leaderboardTableModel);
        JScrollPane leaderboardTableScrollPane = new JScrollPane(leaderboardTable);
        leaderboardTable.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 30));
        leaderboardTable.setFont(new Font("Helvetica", Font.PLAIN, 30));
        leaderboardTable.setRowHeight(50);
        leaderboardPanel.add(leaderboardTableScrollPane, BorderLayout.CENTER);

        // populate myRankPanel
        myRankPanel.setLayout(new BorderLayout());
        myRankPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        DefaultTableModel myRankTableModel = new DefaultTableModel();
        ArrayList<Object> myRankAsArray = leaderboardViewModel.getMyRankInfo(leaderboardType);
        myRankTableModel.addRow(myRankAsArray.toArray());
        JTable myRankTable = new JTable(myRankTableModel);
        JScrollPane myRankTableScrollPane = new JScrollPane(myRankTable);
        myRankTable.setFont(new Font("Helvetica", Font.PLAIN, 30));
        myRankTable.setRowHeight(50);
        myRankPanel.add(myRankTableScrollPane, BorderLayout.CENTER);

        // populate containerPanel
        containerPanel.add(leaderboardPanel);
        containerPanel.add(myRankPanel);
        return containerPanel;
    }

    // ---------- Styled Buttons ----------
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Helvetica", Font.BOLD, 18));
        button.setBackground(new Color(70, 130, 180)); // blue accent
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return button;
    }

    private void switchToMainScreen() {
        viewManagerModel.setState(mainScreenViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    private void setupTabListener() {
        tabbedPane.addChangeListener(e -> {
            LeaderboardType type;

            int index = tabbedPane.getSelectedIndex();
            switch (index) {
                case 1 -> type = LeaderboardType.EXPERIENCE_POINTS;
                case 2 -> type = LeaderboardType.QUESTIONS_ANSWERED;
                case 3 -> type = LeaderboardType.QUESTIONS_CORRECT;
                default -> type = LeaderboardType.LEVEL;
            }

            createContainerPanel(type);
        });
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
