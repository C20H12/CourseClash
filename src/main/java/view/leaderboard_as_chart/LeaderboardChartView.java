package view.leaderboard_as_chart;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard_as_chart.LeaderboardAsChartController;
import interface_adapter.leaderboard_as_chart.LeaderboardAsChartViewModel;
import use_case.leaderboard.LeaderboardType;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LeaderboardChartView extends JPanel implements PropertyChangeListener {

    private final String viewName = "LeaderboardChartView";
    private final LeaderboardAsChartController controller;
    private final LeaderboardAsChartViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final LeaderboardChartPanel chartPanel = new LeaderboardChartPanel();

    public LeaderboardChartView(LeaderboardAsChartController controller,
                                LeaderboardAsChartViewModel viewModel,
                                ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ---------- Top Panel for Buttons and Selectors ----------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topPanel.setBackground(new Color(245, 245, 245));

        // Back button
        JButton backButton = createStyledButton("Back to Table");
        backButton.addActionListener(e -> switchToLeaderboardTable());
        topPanel.add(backButton);

        // Combo box for leaderboard type
        String[] leaderboardTypes = {"Level", "Experience Points", "Questions Answered", "Questions Correct"};
        JComboBox<String> typeSelector = new JComboBox<>(leaderboardTypes);
        typeSelector.setFont(new Font("Helvetica", Font.BOLD, 16));
        typeSelector.addActionListener(e -> {
            int selectedIndex = typeSelector.getSelectedIndex();
            LeaderboardType type = switch (selectedIndex) {
                case 1 -> LeaderboardType.EXPERIENCE_POINTS;
                case 2 -> LeaderboardType.QUESTIONS_ANSWERED;
                case 3 -> LeaderboardType.QUESTIONS_CORRECT;
                default -> LeaderboardType.LEVEL;
            };
            chartPanel.setCurrentType(type);
        });
        topPanel.add(new JLabel("Metric:"));
        topPanel.add(typeSelector);

        // Combo box for Top N users
        Integer[] topOptions = {5, 10, 20, 50};
        JComboBox<Integer> topSelector = new JComboBox<>(topOptions);
        topSelector.setFont(new Font("Helvetica", Font.BOLD, 16));
        topSelector.addActionListener(e -> {
            int topN = (Integer) topSelector.getSelectedItem();
            chartPanel.setTopN(topN);
        });
        topPanel.add(new JLabel("Top Users:"));
        topPanel.add(topSelector);

        add(topPanel, BorderLayout.NORTH);

        // ---------- Chart Panel ----------
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        add(chartContainer, BorderLayout.CENTER);
    }

    // ---------- Styled Button ----------
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

    // ---------- Switch back to table view ----------
    private void switchToLeaderboardTable() {
        if (viewManagerModel != null) {
            viewManagerModel.setState("leaderboard"); // must match LeaderboardView.getViewName()
            viewManagerModel.firePropertyChange();
        } else {
            System.err.println("ViewManagerModel not set in LeaderboardChartView!");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Update chart when ViewModel changes
        var state = viewModel.getState();
        chartPanel.setScores(state.getScores());
        chartPanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}
