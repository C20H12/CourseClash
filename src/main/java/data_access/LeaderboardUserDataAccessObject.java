package data_access;

import entity.User;
import use_case.leaderboard.LeaderboardType;
import use_case.leaderboard.LeaderboardUserDataAccessInterface;

import java.util.ArrayList;
import java.util.Map;

public class LeaderboardUserDataAccessObject implements LeaderboardUserDataAccessInterface {

    @Override
    public Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN) {
        return null;
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
