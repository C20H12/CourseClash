package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addMainScreenView()
                .addLeaderboardView()
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
