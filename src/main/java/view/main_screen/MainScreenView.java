package view.main_screen;

import interface_adapter.main_screen.MainScreenViewModel;
// Import the Controller
import interface_adapter.MultiPlayer.start_match.MPStartController;
import interface_adapter.registration.logout.LogoutController; // Assuming you will add logout logic later
import use_case.DataAccessException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "main screen";
    private final MainScreenViewModel mainScreenViewModel;

    // 1. Add the Controller Field
    private MPStartController mpStartController;
    private LogoutController logoutController; // Optional if you want to wire the logout button later

    // Constructor needs to match what AppBuilder passes
    // (If AppBuilder currently passes LogoutController, keep it. If not, just ViewModel is fine for now).
    public MainScreenView(MainScreenViewModel mainScreenViewModel, LogoutController logoutController) {
        this.mainScreenViewModel = mainScreenViewModel;
        this.logoutController = logoutController;

        // Set panel layout
        this.setLayout(new BorderLayout(10, 10));

        // ---------- Title Image ----------
        // (Keep your existing image loading code)
        try {
            ImageIcon originalTitleImage = new ImageIcon("images/TitleImage.png");
            if (originalTitleImage.getIconWidth() > 0) {
                int titleImageWidth = originalTitleImage.getIconWidth();
                int titleImageHeight = originalTitleImage.getIconHeight();
                Image titleImage = originalTitleImage.getImage()
                        .getScaledInstance((int)(titleImageWidth*0.9), (int)(titleImageHeight*0.9), Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(titleImage));
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                JPanel imagePanel = new JPanel(new GridLayout());
                imagePanel.add(imageLabel);
                this.add(imagePanel, BorderLayout.NORTH);
            }
        } catch (Exception e) {
            System.out.println("Image not found, skipping.");
        }

        // ---------- 2x2 Buttons ----------
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(null);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(30, 30, 30, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;

        // Define buttons explicitly so we can assign logic to them
        JButton singlePlayerButton = new JButton("Single Player");
        JButton multiplayerButton = new JButton("Multiplayer");
        JButton manageSetButton = new JButton("Manage Study Set");
        JButton leaderboardButton = new JButton("Leaderboard");
        // Optional: Add a logout button somewhere if you want logic for it
        // JButton logoutButton = new JButton("Log Out");

        JButton[][] buttons = {
                {singlePlayerButton, multiplayerButton},
                {manageSetButton, leaderboardButton}
        };

        // Style Loop
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = buttons[row][col];
                button.setFont(new Font("Helvetica", Font.BOLD, 32)); // Adjusted size slightly
                button.setBackground(Color.GRAY);
                button.setForeground(Color.WHITE);

                c.gridx = col;
                c.gridy = row;
                button.setPreferredSize(new Dimension(400, 150));
                buttonPanel.add(button, c);
            }
        }

        this.add(buttonPanel, BorderLayout.CENTER);

        // 2. Add Logic to "Multiplayer" Button
        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ask for Opponent and Deck
                String opponent = JOptionPane.showInputDialog("Enter Opponent Username:");
                if (opponent == null || opponent.isEmpty()) return;

                String deckName = JOptionPane.showInputDialog("Enter Deck Name:");
                if (deckName == null || deckName.isEmpty()) return;

                // Get current user (Placeholder logic if ViewModel doesn't have it yet)
                // String currentUser = mainScreenViewModel.getState().getUsername();
                String currentUser = JOptionPane.showInputDialog("Confirm your username (for testing):");

                if (mpStartController != null) {
                    try {
                        mpStartController.execute(currentUser, opponent, deckName);
                    } catch (DataAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Multiplayer Controller is not connected.");
                }
            }
        });

        // Logic for other buttons...
        singlePlayerButton.addActionListener(e -> System.out.println("Single Player clicked"));
    }

    // 3. Add the Setter (Crucial for AppBuilder)
    public void setMPStartController(MPStartController controller) {
        this.mpStartController = controller;
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Update UI if needed
    }
}