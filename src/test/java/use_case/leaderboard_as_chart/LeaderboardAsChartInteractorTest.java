package use_case.leaderboard_as_chart;

import entity.User;
import frameworks_and_drivers.DataAccess.LeaderboardAsChartTestDataAccessObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;
import use_case.leaderboard.LeaderboardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LeaderboardAsChartInteractorTest {

    private Map<LeaderboardType, ArrayList<User>> fakeData;

    @BeforeEach
    void setUp() {
        fakeData = new HashMap<>();

        // Create fake user data for all leaderboard types
        for (LeaderboardType type : LeaderboardType.values()) {
            ArrayList<User> users = new ArrayList<>();

            // Add predictable descending users User3, User2, User1
            for (int i = 3; i >= 1; i--) {
                User u = new User("User" + i, null);
                u.setLevel(i);
                u.setExperiencePoints(i * 100);
                u.setQuestionsAnswered(i * 10);
                u.setQuestionsCorrect(i * 5);
                users.add(u);
            }
            fakeData.put(type, users);
        }
    }

    /**
     * Tests that the interactor:
     * - calls DAO.getTopUsers()
     * - copies the lists correctly into chartData
     * - passes correct data to the presenter
     */
    @Test
    void testInteractorCopiesDataCorrectly() throws DataAccessException {

        LeaderboardAsChartTestDataAccessObject mockDAO =
                new LeaderboardAsChartTestDataAccessObject(fakeData);

        LeaderboardAsChartOutputBoundary presenter = new LeaderboardAsChartOutputBoundary() {
            @Override
            public void present(LeaderboardAsChartOutputData outputData) {
                Map<LeaderboardType, List<User>> result = outputData.getTopUsers();

                // Validate all leaderboard types exist
                Assertions.assertEquals(LeaderboardType.values().length, result.size());

                // Validate each type has exactly 3 users in descending order
                for (LeaderboardType type : LeaderboardType.values()) {
                    List<User> users = result.get(type);

                    Assertions.assertNotNull(users);
                    Assertions.assertEquals(3, users.size());

                    Assertions.assertEquals("User3", users.get(0).getUserName());
                    Assertions.assertEquals("User2", users.get(1).getUserName());
                    Assertions.assertEquals("User1", users.get(2).getUserName());
                }
            }
        };

        LeaderboardAsChartInputData inputData = new LeaderboardAsChartInputData(50);

        LeaderboardAsChartInteractor interactor = new LeaderboardAsChartInteractor(mockDAO, presenter);

        interactor.execute(inputData);
    }
}
