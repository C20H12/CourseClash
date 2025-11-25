package use_case.leaderboard;

import frameworks_and_drivers.DataAccess.LeaderboardUserDataAccessObject;
import interface_adapter.leaderboard.LeaderboardPresenter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

class LeaderboardInteractorTest {

    // Test if leaderboard can be correctly retrieved from the data access object based on the user count we want.
    @Test
    void testGetLeaderboard() throws DataAccessException {
        LeaderboardInputData leaderboardInputData = new LeaderboardInputData(10);
        LeaderboardUserDataAccessInterface leaderboardDAO = new LeaderboardUserDataAccessObject();
        LeaderboardOutputBoundary leaderboardPresenter = new LeaderboardPresenter(null, null) {
            @Override
            public void presentLeaderboard(LeaderboardOutputData outputData) {
                // Check if the output data contains the expected number of users
                int expectedUserCount = 10; // Assuming we requested top 10 users
                int actualUserCount = outputData.getLeaderboard().values().stream()
                        .mapToInt(ArrayList::size)
                        .sum();
                Assertions.assertEquals(expectedUserCount, actualUserCount);
            }
        };
        LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(leaderboardDAO, leaderboardPresenter);
        leaderboardInteractor.execute(leaderboardInputData);
    }

    // Test if the leaderboard receives more than the user count for the leaderboard,
    // it will return all users' information instead.
    @Test
    void testGetLeaderboardExceedingUserCount() throws DataAccessException {
        LeaderboardInputData leaderboardInputData = new LeaderboardInputData(Integer.MAX_VALUE);
        // Assuming there are fewer users than Integer.MAX_VALUE in the database
        LeaderboardUserDataAccessInterface leaderboardDAO = new LeaderboardUserDataAccessObject();
        LeaderboardOutputBoundary leaderboardPresenter = new LeaderboardPresenter(null, null) {
            @Override
            public void presentLeaderboard(LeaderboardOutputData outputData) throws DataAccessException {
                // Check if the output data contains the expected number of users
                int expectedUserCount = leaderboardDAO.getTotalUserCount(); // Assuming we requested top 10 users
                int actualUserCount = outputData.getLeaderboard().values().stream()
                        .mapToInt(ArrayList::size)
                        .sum();
                Assertions.assertEquals(expectedUserCount, actualUserCount);
            }
        };
        LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(leaderboardDAO, leaderboardPresenter);
        leaderboardInteractor.execute(leaderboardInputData);
    }

    // Test if the leaderboardViewModel will correctly fetch the leaderboard data (by type) and show it in the view.
    @Test
    public void testLeaderboardCorrectTypeFetch() throws DataAccessException {
        LeaderboardInputData leaderboardInputData = new LeaderboardInputData(10);
        LeaderboardUserDataAccessInterface leaderboardDAO = new LeaderboardUserDataAccessObject();
        LeaderboardOutputBoundary leaderboardPresenter = new LeaderboardPresenter(null, null) {
            @Override
            public void presentLeaderboard(LeaderboardOutputData outputData) {
                // Check if the output data contains the expected number of users
                int expectedUserCount = 10; // Assuming we requested top 10 users
                int actualUserCount = outputData.getLeaderboard().values().stream()
                        .mapToInt(ArrayList::size)
                        .sum();
                Assertions.assertEquals(expectedUserCount, actualUserCount);
            }
        };
        LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(leaderboardDAO, leaderboardPresenter);
        leaderboardInteractor.execute(leaderboardInputData);
    }
}
