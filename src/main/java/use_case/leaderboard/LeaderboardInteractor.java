package use_case.leaderboard;

import entity.User;
import use_case.registration.login.LoginOutputBoundary;
import use_case.registration.login.LoginUserDataAccessInterface;

import java.util.ArrayList;

public class LeaderboardInteractor implements LeaderboardInputBoundary {
    private final LeaderboardUserDataAccessInterface userDataAccessObject;
    private final LeaderboardOutputBoundary leaderboardPresenter;

    public LeaderboardInteractor(LeaderboardUserDataAccessInterface userDataAccessInterface, LeaderboardOutputBoundary leaderboardOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.leaderboardPresenter = leaderboardOutputBoundary;
    }

    @Override
    public void execute(LeaderboardInputData inputData) {
        User currentUser = inputData.getUser();
        ArrayList<User> topUsers = userDataAccessObject.getTopUsers(5);
        int rank = userDataAccessObject.getUserRank(currentUser);
        LeaderboardOutputData outputData = new LeaderboardOutputData(topUsers, currentUser, rank);
        leaderboardPresenter.presentLeaderboard(outputData);
    }
}
