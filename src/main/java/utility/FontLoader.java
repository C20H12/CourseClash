package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.UIManager;

/**
 * Utility class for loading and registering custom fonts,
 * and configuring global UI settings for consistent styling.
 */
public class FontLoader {

    /**
     * Registers custom fonts and configures global UI styles.
     * Loads the Helvetica font from resources and registers it with the system.
     * Also sets cross-platform Look and Feel and button styling to ensure
     * consistent appearance across operating systems.
     */
    public static void registerFonts() {
        try {
            InputStream is = FontLoader.class.getResourceAsStream("/font/Helvetica.ttf");
            Font helvetica = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(helvetica);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // The following block ensures proper style loading on macOS systems
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
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}
