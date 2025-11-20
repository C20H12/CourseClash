package interface_adapter.leaderboard;

import entity.User;
import interface_adapter.ViewModel;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaderboardViewModel extends ViewModel<LeaderboardState> {

    private Map<LeaderboardType, ArrayList<User>> leaderboard = new HashMap<>();
    private Map<LeaderboardType, Integer> myRank = new HashMap<>();
    private LeaderboardType currentType;
    private User currentUser;

    public LeaderboardViewModel() {
        super("leaderboard");
    }

    public void setState(LeaderboardState state) {
        if (state == null) return;

        this.leaderboard = state.getLeaderboard();
        this.myRank = state.getMyRank();
    }

    public ArrayList<Object> getLeaderboardByType(LeaderboardType leaderboardType) {
//        Map<LeaderboardType, ArrayList<Object>> converted = new HashMap<>();
//        for (LeaderboardType t : LeaderboardType.values()) {
//            ArrayList<Object> rows = new ArrayList<>();
//            ArrayList<User> users = leaderboard.get(t);
//            if (users != null) {
//                for (int i = 0; i < users.size(); i++) {
//                    User u = users.get(i);
//                    ArrayList<Object> row = new ArrayList<>();
//                    row.add(i + 1); // rank (1-based)
//                    row.add(u.getUserName());
//                    row.add(u.getLevel());
//                    row.add(u.getExperiencePoints());
//                    row.add(u.getQuestionsAnswered());
//                    row.add(u.getQuestionsCorrect());
//                    rows.add(row);
//                }
//            }
//            converted.put(t, rows);
//        }
//        return converted.get(leaderboardType);
        // Dummy data for testing: 5 example users
        ArrayList<Object> rows = new ArrayList<>();

        Object[] r1 = {
                1,
                "Alice",
                10,
                1500,
                200,
                180
        };

        Object[] r2 = {
                2,
                "Bob",
                9,
                1320,
                180,
                160
        };

        Object[] r3 = {
                3,
                "Carol",
                8,
                1100,
                150,
                130
        };

        Object[] r4 = {
                4,
                "Dave",
                7,
                900,
                120,
                100
        };

        Object[] r5 = {
                5,
                "Eve",
                6,
                700,
                100,
                80
        };

        rows.add(r1);
        rows.add(r2);
        rows.add(r3);
        rows.add(r4);
        rows.add(r5);

        return rows;
    }

    public ArrayList<Object> getMyRankInfo(LeaderboardType leaderboardType) {
        ArrayList<Object> myRankInfo = new ArrayList<>();
        Integer myRanking = this.myRank.get(leaderboardType);
        if (myRanking == null) {
            myRanking = 10; // Not ranked
        }
        myRankInfo.add(myRanking);
        myRankInfo.add(this.currentUser.getUserName());
        myRankInfo.add(this.currentUser.getLevel());
        myRankInfo.add(this.currentUser.getExperiencePoints());
        myRankInfo.add(this.currentUser.getQuestionsAnswered());
        myRankInfo.add(this.currentUser.getQuestionsCorrect());

        return myRankInfo;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
