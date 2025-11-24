package app;

import use_case.DataAccessException;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addMainScreenView()
                .addLoginUseCase()
                .addSignupUseCase()
                .addLeaderboardUseCase()
                .addSinglePlayerUseCase()
                .addMultiPlayerUseCase()
                .addStudyDeckUseCase()
                .build();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
