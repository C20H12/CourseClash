package app;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addMainScreenView()
                .addLoginUseCase()
                .addSignupUseCase()
                .build();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

