package use_case.leaderboard;

import java.util.ArrayList;
import java.util.Map;

import entity.User;
import use_case.DataAccessException;

public class LeaderboardInteractor implements LeaderboardInputBoundary {
    private final LeaderboardUserDataAccessInterface userDataAccessObject;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(LeaderboardUserDataAccessInterface userDataAccessInterface,
                                 LeaderboardOutputBoundary leaderboardOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.leaderboardPresenter = leaderboardOutputBoundary;
    }

    @Override
    public void execute(LeaderboardInputData inputData) throws DataAccessException {
        Map<LeaderboardType, ArrayList<User>> leaderboard =
                userDataAccessObject.getTopUsers(inputData.getLeaderboardEntryCount());
        LeaderboardOutputData outputData = new LeaderboardOutputData(leaderboard);
        leaderboardPresenter.presentLeaderboard(outputData);
    }
}
