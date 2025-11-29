package interface_adapter.leaderboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.User;
import interface_adapter.ViewModel;
import use_case.leaderboard.LeaderboardType;

public class LeaderboardViewModel extends ViewModel<LeaderboardState> {

    private static final int LEADERBOARD_ENTRY_COUNT = 50;

    private Map<LeaderboardType, ArrayList<User>> leaderboard = new HashMap<>();

        public LeaderboardViewModel() {
        super("leaderboard");
    }

    public void setLeaderboard(Map<LeaderboardType, ArrayList<User>> leaderboard) {
        this.leaderboard = leaderboard;
    }

    /**
     * Converts the leaderboard data into a format suitable for display in the UI
     * @param leaderboardType The type of leaderboard to retrieve
     * @return A list of rows, where each row is a list of objects representing
     */
    public ArrayList<ArrayList<Object>> getLeaderboardByType(LeaderboardType leaderboardType) {
        Map<LeaderboardType, ArrayList<ArrayList<Object>>> converted = new HashMap<>();
        for (LeaderboardType t : LeaderboardType.values()) {
            ArrayList<ArrayList<Object>> rows = new ArrayList<>();
            ArrayList<User> users = leaderboard.get(t);
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);
                    ArrayList<Object> row = new ArrayList<>();
                    row.add(i + 1);
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

    public int getLeaderboardEntryCount() {
        return LEADERBOARD_ENTRY_COUNT;
    }
}
