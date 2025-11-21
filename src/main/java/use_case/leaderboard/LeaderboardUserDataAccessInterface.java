package use_case.leaderboard;

import entity.User;
import use_case.DataAccessException;

import java.util.ArrayList;
import java.util.Map;

public interface LeaderboardUserDataAccessInterface {

    /**
     * Get the top ranking user information. Note: user password will be null.
     * @param topN specify the nth top user
     * @return a map with key as type of ranking and an arraylist of user in descending order (highest ranked is first)
     * If user number < topn, all user will be returned in order and the arraylist has length < topn
     * @throws DataAccessException Exception when data access fails
     */
    Map<LeaderboardType, ArrayList<User>> getTopUsers(int topN) throws DataAccessException;

    /**
     * Get the rank of specified user.
     * @param user specify which user
     * @return a map with key as type of ranking and the rank this user is at
     * @throws DataAccessException Exception when data access fails
     */
    Map<LeaderboardType, Integer> getUserRank(User user) throws DataAccessException;
}
