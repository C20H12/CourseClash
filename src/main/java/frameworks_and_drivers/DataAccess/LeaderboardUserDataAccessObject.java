package frameworks_and_drivers.DataAccess;

import entity.User;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardType;
import use_case.leaderboard.LeaderboardUserDataAccessInterface;

import java.util.*;

import static frameworks_and_drivers.DataAccess.StaticMethods.makeApiRequest;

public class LeaderboardUserDataAccessObject implements LeaderboardUserDataAccessInterface {

    final private String apiKey;
    public LeaderboardUserDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN) throws DataAccessException {
        final String method = "/api/get-top-user";
        HashMap<String, String> params = new HashMap<>();
        params.put("top-n", String.valueOf(topN));
        Map<LeaderboardType, ArrayList<User>> result = new HashMap<>();

        for (LeaderboardType x : LeaderboardType.values()) {
            String type = x.name();
            result.put(x, new ArrayList<>());
            params.put("type", type);
            JSONObject response = makeApiRequest("GET", method, params, apiKey);
            boolean ascOrder = response.getBoolean("asc-order");
            JSONArray rank = response.getJSONArray("rank");
            for (int i = 0; i < rank.length(); i++) {
                JSONObject cu = rank.getJSONObject(i);
                User t = new User(cu.getString("username"), null);
                t.setLevel(cu.optInt("level", 0));
                t.setExperiencePoints(cu.optInt("points", 0));
                t.setQuestionsCorrect(cu.optInt("correct", 0));
                t.setQuestionsAnswered(cu.optInt("answered", 0));
                result.get(x).add(t);
            }
            if (ascOrder) {
                Collections.reverse(result.get(x));
            }
        }
        return result;
    }


//    @Override
//    public Map<LeaderboardType, Integer> getUserRank(User user) throws DataAccessException {
//        final String method = "/api/get-user-rank";
//        HashMap<String, String> params = new HashMap<>();
//        params.put("username", user.getUserName());
//        JSONObject response = makeApiRequest("GET", method, params, apiKey);
//        Map<LeaderboardType, Integer> result = new HashMap<>();
//        result.put(LeaderboardType.LEVEL, response.getInt("level-rank"));
//        result.put(LeaderboardType.EXPERIENCE_POINTS, response.getInt("points-rank"));
//        result.put(LeaderboardType.QUESTIONS_ANSWERED, response.getInt("answered-rank"));
//        result.put(LeaderboardType.QUESTIONS_CORRECT, response.getInt("correct-rank"));
//        return result;
//    }
}
