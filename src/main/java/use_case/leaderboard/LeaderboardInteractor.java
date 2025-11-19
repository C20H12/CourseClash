package use_case.leaderboard;

import entity.User;
import use_case.registration.login.LoginOutputBoundary;
import use_case.registration.login.LoginUserDataAccessInterface;

import java.util.ArrayList;

public class LeaderboardInteractor implements LeaderboardInputBoundary {
    private final LeaderboardUserDataAccessInterface userDataAccessObject;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(LeaderboardUserDataAccessInterface userDataAccessObject, LeaderboardOutputBoundary leaderboardPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.leaderboardPresenter = leaderboardPresenter;
    }

    @Override
    public void execute(LeaderboardInputData inputData) {
        User currentUser = inputData.getUser();
        LeaderboardType leaderboardType = inputData.getLeaderboardType();
        ArrayList<User> topUsers = userDataAccessObject.getTopUsers(5, leaderboardType);
        int rank = userDataAccessObject.getUserRank(currentUser);
        LeaderboardOutputData outputData = new LeaderboardOutputData(topUsers, currentUser, rank, leaderboardType);
        leaderboardPresenter.presentLeaderboard(outputData);
    }
}
