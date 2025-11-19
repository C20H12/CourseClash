package data_access;

import entity.User;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardType;
import use_case.leaderboard.LeaderboardUserDataAccessInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static data_access.StaticMethods.makeApiRequest;

public class LeaderboardUserDataAccessObject implements LeaderboardUserDataAccessInterface {

    final private String apiKey;
    public LeaderboardUserDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN) throws DataAccessException {
        final String method = "/get-top-user";
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
            // TODO: asce or desc order?
        }


        // TODO: WHAT SHOULD THE MAP LOOK LIKE
        // Everything has to be sorted, i made it to be an arraylist so it has a clear order.
        // Its a Map, key should be enums in
        // LEVEL,
        // EXPERIENCE_POINTS,
        // QUESTIONS_ANSWERED,
        // QUESTIONS_CORRECT
        // and value should be an arraylist of users sorted by that criteria.
        // length should be equal to topN
    }



    @Override
    public Map<LeaderboardType, Integer> getUserRank(User user) {
        return null;
        // TODO: WHAT SHOULD THE MAP LOOK LIKE
        // key should be enums in
        // LEVEL,
        // EXPERIENCE_POINTS,
        // QUESTIONS_ANSWERED,
        // QUESTIONS_CORRECT
        // value should be the rank of the user in that criteria.
    }
}
