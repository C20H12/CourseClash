package app;

import javax.swing.*;
import java.awt.*;

import use_case.DataAccessException;



public class Main {
    /**
     * The entry point of the application. Initializes the AppBuilder, constructs
     * all required views and use cases, and launches the main JFrame for the UI.
     *
     * @param args command-line arguments (unused)
     * @throws DataAccessException if application initialization fails due to data access issues
     */
    public static void main(String[] args) throws DataAccessException {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addMainScreenView()
                .addLeaderboardChartView()
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

