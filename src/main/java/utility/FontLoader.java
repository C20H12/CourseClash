package utility;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class FontLoader {

    /**
     * Registers custom fonts and sets UI look and feel.
     * This method loads the Helvetica font from the resources and registers it
     * with the local graphics environment. It also sets a cross-platform look and feel
     * for consistent UI appearance across different operating systems.
     * @throws IOException if the font file cannot be read
     * @throws FontFormatException if the font file is not in the correct format
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
