package use_case.leaderboard;

import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.user_session.UserSession;
import frameworks_and_drivers.DataAccess.LeaderboardUserDataAccessObject;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.leaderboard.LeaderboardViewModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.DataAccessException;

import java.util.ArrayList;

class LeaderboardInteractorTest {
    public UserSession session;
    // Let the user session begin for API key
    @BeforeEach
    void setUp() {
        session = new UserSession();
        session.setUser(new User("TestUser", "password"));
        session.setApiKey("6b714a7a52c3c318");
    }
    // Test if leaderboard can be correctly retrieved from the data access object based on the user count we want.
    @Test
    void testGetLeaderboard() throws DataAccessException {
        LeaderboardInputData leaderboardInputData = new LeaderboardInputData(10);
        LeaderboardUserDataAccessInterface leaderboardDAO = new LeaderboardUserDataAccessObject(session);
        LeaderboardOutputBoundary leaderboardPresenter = new LeaderboardPresenter(new LeaderboardViewModel(),
                new ViewManagerModel()) {
            @Override
            public void presentLeaderboard(LeaderboardOutputData outputData) {
                // Check if the output data contains the expected number of users
                int expectedUserCount = 10; // Assuming we requested top 10 users
                int actualUserCount = outputData.getLeaderboard().get(LeaderboardType.LEVEL).size();
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
        LeaderboardUserDataAccessInterface leaderboardDAO = new LeaderboardUserDataAccessObject(session);
        LeaderboardOutputBoundary leaderboardPresenter = new LeaderboardPresenter(new LeaderboardViewModel(),
                new ViewManagerModel()) {
            @Override
            public void presentLeaderboard(LeaderboardOutputData outputData) throws DataAccessException {
                // Check if the output data contains the expected number of users
                int expectedUserCount = leaderboardDAO.getTotalUserCount(); // Assuming we requested top 10 users
                int actualUserCount = outputData.getLeaderboard().get(LeaderboardType.LEVEL).size();
                Assertions.assertEquals(expectedUserCount, actualUserCount);
            }
        };
        LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(leaderboardDAO, leaderboardPresenter);
        leaderboardInteractor.execute(leaderboardInputData);
    }

    // Test if the leaderboardViewModel will correctly fetch the leaderboard data (by type) and show it in the view.
    @Test
    void testLeaderboardCorrectTypeFetch() throws DataAccessException {
        LeaderboardInputData leaderboardInputData = new LeaderboardInputData(Integer.MAX_VALUE);
        // get all the users from the database
        LeaderboardUserDataAccessInterface leaderboardDAO = new LeaderboardUserDataAccessObject(session);
        LeaderboardViewModel leaderboardViewModel = new LeaderboardViewModel();
        LeaderboardOutputBoundary leaderboardPresenter = new LeaderboardPresenter(leaderboardViewModel,
                new ViewManagerModel());
        LeaderboardInputBoundary leaderboardInteractor = new LeaderboardInteractor(leaderboardDAO, leaderboardPresenter);
        leaderboardInteractor.execute(leaderboardInputData);

        for (LeaderboardType leaderboardType : LeaderboardType.values()) {
            ArrayList<ArrayList<Object>> leaderboardArray = leaderboardViewModel.getLeaderboardByType(leaderboardType);
            // Check if it is correctly ordered by each type
            for (int i = 1; i < leaderboardArray.size(); i++) {
                switch (leaderboardType) {
                    case LEVEL:
                        Assertions.assertTrue((Integer) leaderboardArray.get(i - 1).get(2) >=
                                ((Integer) leaderboardArray.get(i).get(2)));
                        break;
                    case EXPERIENCE_POINTS:
                        Assertions.assertTrue((Integer) leaderboardArray.get(i - 1).get(3) >=
                                ((Integer) leaderboardArray.get(i).get(3)));
                        break;
                    case QUESTIONS_ANSWERED:
                        Assertions.assertTrue((Integer) leaderboardArray.get(i - 1).get(4) >=
                                ((Integer) leaderboardArray.get(i).get(4)));
                        break;
                    case QUESTIONS_CORRECT:
                        Assertions.assertTrue((Integer) leaderboardArray.get(i - 1).get(5) >=
                                ((Integer) leaderboardArray.get(i).get(5)));
                        break;
                }
            }
        }
    }
}
