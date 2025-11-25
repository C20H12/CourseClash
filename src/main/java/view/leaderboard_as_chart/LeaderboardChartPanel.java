package view.leaderboard_as_chart;

import entity.User;
import use_case.leaderboard.LeaderboardType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class LeaderboardChartPanel extends JPanel {

    private Map<LeaderboardType, List<User>> scores;
    private LeaderboardType currentType = LeaderboardType.LEVEL;
    private int topN = 10; // default top N

    // ---------- Set leaderboard scores ----------
    public void setScores(Map<LeaderboardType, List<User>> scores) {
        this.scores = scores;
        repaint();
    }

    // ---------- Set current metric type ----------
    public void setCurrentType(LeaderboardType type) {
        this.currentType = type;
        repaint();
    }

    // ---------- Set Top N users ----------
    public void setTopN(int topN) {
        this.topN = topN;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (scores == null || !scores.containsKey(currentType)) return;

        List<User> users = scores.get(currentType);
        if (users.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();

        // Determine how many users to display
        int displayCount = Math.min(topN, users.size());
        int barWidth = width / (displayCount * 2);

        // Determine max value to scale bars
        int maxValue = users.stream()
                .mapToInt(u -> switch (currentType) {
                    case LEVEL -> u.getLevel();
                    case EXPERIENCE_POINTS -> u.getExperiencePoints();
                    case QUESTIONS_ANSWERED -> u.getQuestionsAnswered();
                    case QUESTIONS_CORRECT -> u.getQuestionsCorrect();
                })
                .max().orElse(1);

        // Draw bars and labels
        for (int i = 0; i < displayCount; i++) {
            User u = users.get(i);

            int value = switch (currentType) {
                case LEVEL -> u.getLevel();
                case EXPERIENCE_POINTS -> u.getExperiencePoints();
                case QUESTIONS_ANSWERED -> u.getQuestionsAnswered();
                case QUESTIONS_CORRECT -> u.getQuestionsCorrect();
            };

            int barHeight = (int) (((double) value / maxValue) * (height - 60));
            int x = i * 2 * barWidth + barWidth / 2;
            int y = height - barHeight - 30;

            // Draw bar
            g.setColor(getBarColor(currentType));
            g.fillRect(x, y, barWidth, barHeight);

            // Draw value above bar
            g.setColor(Color.BLACK);
            String valueStr = String.valueOf(value);
            int strWidth = g.getFontMetrics().stringWidth(valueStr);
            g.drawString(valueStr, x + (barWidth - strWidth) / 2, y - 5);

            // Draw username below bar
            String username = u.getUserName();
            int nameWidth = g.getFontMetrics().stringWidth(username);
            g.drawString(username, x + (barWidth - nameWidth) / 2, height - 10);
        }

        // Optional: show message if fewer users than top N
        if (displayCount < topN) {
            g.setColor(Color.RED.darker());
            g.setFont(new Font("Helvetica", Font.BOLD, 16));
            String msg = "Only " + displayCount + " users available";
            int msgWidth = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (width - msgWidth) / 2, 20);
        }
    }

    // ---------- Get color based on leaderboard type ----------
    private Color getBarColor(LeaderboardType type) {
        return switch (type) {
            case LEVEL -> new Color(70, 130, 180); // blue
            case EXPERIENCE_POINTS -> new Color(34, 139, 34); // green
            case QUESTIONS_ANSWERED -> new Color(255, 165, 0); // orange
            case QUESTIONS_CORRECT -> new Color(220, 20, 60); // crimson
        };
    }
}
