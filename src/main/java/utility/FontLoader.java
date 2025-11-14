package utility;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
    public static void registerFonts() {
        try {
            InputStream is = FontLoader.class.getResourceAsStream("/font/Helvetica.ttf");
            Font Helvetica = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Helvetica);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // The following try-catch block are required for style to load on macos system properly.
        try {
            // Use a cross-platform Look and Feel (respects colors on all OS)
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            // Global UI overrides for buttons
            UIManager.put("Button.opaque", true);
            UIManager.put("Button.focusPainted", false);
            UIManager.put("Button.borderPainted", false);
            UIManager.put("Button.contentAreaFilled", true);
            UIManager.put("Button.background", Color.GRAY);
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
