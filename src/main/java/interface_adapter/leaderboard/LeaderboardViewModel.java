package interface_adapter.leaderboard;

import entity.User;
import interface_adapter.ViewModel;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaderboardViewModel extends ViewModel<LeaderboardState> {

    private Map<LeaderboardType, ArrayList<User>> leaderboard = new HashMap<>();

    public LeaderboardViewModel() {
        super("leaderboard");
    }

    public void setLeaderboard(Map<LeaderboardType, ArrayList<User>> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public static final int LEADERBOARD_ENTRY_COUNT = 5;

    public ArrayList<Object> getLeaderboardByType(LeaderboardType leaderboardType) {
        Map<LeaderboardType, ArrayList<Object>> converted = new HashMap<>();
        for (LeaderboardType t : LeaderboardType.values()) {
            ArrayList<Object> rows = new ArrayList<>();
            ArrayList<User> users = leaderboard.get(t);
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);
                    ArrayList<Object> row = new ArrayList<>();
                    row.add(i + 1); // rank (1-based)
                    row.add(u.getUserName());
                    row.add(u.getLevel());
                    row.add(u.getExperiencePoints());
                    row.add(u.getQuestionsAnswered());
                    row.add(u.getQuestionsCorrect());
                    rows.add(row);
                }
            }
            converted.put(t, rows);
        }
        return converted.get(leaderboardType);
    }
}
