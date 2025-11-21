package view.leaderboard_as_chart;

import interface_adapter.ViewManagerModel;
import interface_adapter.leaderboard_as_chart.LeaderboardAsChartController;
import interface_adapter.leaderboard_as_chart.LeaderboardAsChartViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LeaderboardChartView extends JPanel implements PropertyChangeListener {

    private final String viewName = "LeaderboardChartView";
    private final LeaderboardAsChartController controller;
    private final LeaderboardAsChartViewModel viewModel;
    private ViewManagerModel viewManagerModel; // to switch views
    private final LeaderboardChartPanel chartPanel = new LeaderboardChartPanel();

    public LeaderboardChartView(LeaderboardAsChartController controller,
                                LeaderboardAsChartViewModel viewModel,
                                ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ---------- Top Panel for Buttons ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Back to Table button
        JButton backButton = createStyledButton("Back to Table");
        backButton.addActionListener(e -> switchToLeaderboardTable());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.NORTH);

        // ---------- Chart Panel ----------
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        add(chartContainer, BorderLayout.CENTER);
    }

    // ---------- Styled Button Method ----------
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

    // ---------- Switch Back to Table ----------
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
        var state = viewModel.getState();
        chartPanel.setScores(state.getScores()); // update chart when ViewModel changes
        chartPanel.repaint();
    }

    public String getViewName() {
        return viewName;
    }
}
