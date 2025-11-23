package app;

import use_case.DataAccessException;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addMainScreenView()
                .addSinglePlayerView()
                .addLeaderboardView()
                .addLoginUseCase()
                .addSignupUseCase()
                .addLeaderboardUseCase()
                .addSinglePlayerUseCase()
                .addStudyDeckUseCase()
                .build();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

