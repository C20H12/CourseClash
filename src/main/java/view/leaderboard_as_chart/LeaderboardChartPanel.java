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

    public void setScores(Map<LeaderboardType, List<User>> scores) {
        this.scores = scores;
        repaint();
    }

    public void setCurrentType(LeaderboardType type) {
        this.currentType = type;
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
        int barWidth = width / (users.size() * 2);
        int maxValue = users.stream()
                .mapToInt(u -> switch (currentType) {
                    case LEVEL -> u.getLevel();
                    case EXPERIENCE_POINTS -> u.getExperiencePoints();
                    case QUESTIONS_ANSWERED -> u.getQuestionsAnswered();
                    case QUESTIONS_CORRECT -> u.getQuestionsCorrect();
                })
                .max().orElse(1);

        g.setColor(Color.BLUE);
        g.setFont(new Font("Helvetica", Font.PLAIN, 14));

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            int value = switch (currentType) {
                case LEVEL -> u.getLevel();
                case EXPERIENCE_POINTS -> u.getExperiencePoints();
                case QUESTIONS_ANSWERED -> u.getQuestionsAnswered();
                case QUESTIONS_CORRECT -> u.getQuestionsCorrect();
            };
            int barHeight = (int) (((double) value / maxValue) * (height - 50));
            int x = i * 2 * barWidth + barWidth / 2;
            int y = height - barHeight - 30;

            // Draw bar
            g.setColor(Color.BLUE);
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
    }
}
