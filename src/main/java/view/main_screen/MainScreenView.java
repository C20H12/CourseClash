package view.main_screen;

import interface_adapter.ViewManagerModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.studyset.studyset_browse.BrowseStudySetViewModel;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainScreenView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "main screen";
    private final MainScreenViewModel mainScreenViewModel;
    private final ViewManagerModel viewManagerModel;
    private final BrowseStudySetViewModel browseStudySetViewModel;

    public MainScreenView(MainScreenViewModel mainScreenViewModel,
                          ViewManagerModel viewManagerModel,
                          BrowseStudySetViewModel browseStudySetViewModel) {
        this.mainScreenViewModel = mainScreenViewModel;
        this.viewManagerModel = viewManagerModel;
        this.browseStudySetViewModel = browseStudySetViewModel;
        this.mainScreenViewModel.addPropertyChangeListener(this);

        // Set panel layout (this is now the root of this panel)
        this.setLayout(new BorderLayout(10, 10));

        // ---------- Title Image ----------
        ImageIcon originalTitleImage = new ImageIcon("images/TitleImage.png");
        int titleImageWidth = originalTitleImage.getIconWidth();
        int titleImageHeight = originalTitleImage.getIconHeight();
        Image titleImage = originalTitleImage.getImage()
                .getScaledInstance((int)(titleImageWidth*0.9), (int)(titleImageHeight*0.9), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(titleImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel imagePanel = new JPanel(new GridLayout());
        imagePanel.add(imageLabel);

        this.add(imagePanel, BorderLayout.NORTH);

        // ---------- 2x2 Buttons ----------
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(null);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(30, 30, 30, 30);
        c.weightx = 1.0;
        c.weighty = 1.0;

        JButton[][] buttons = {
                {new JButton("Single Player"), new JButton("Multiplayer")},
                {new JButton("Manage Study Set"), new JButton("Leaderboard")}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                JButton button = buttons[row][col];
                button.setFont(new Font("Helvetica", Font.BOLD, 48));
                button.setBackground(Color.GRAY);
                button.setForeground(Color.WHITE);
                if ("Manage Study Set".equals(button.getText())) {
                    button.addActionListener(e -> switchToBrowseStudySet());
                }

                c.gridx = col;
                c.gridy = row;
                button.setPreferredSize(new Dimension(400, 150));
                buttonPanel.add(button, c);
            }
        }

        this.add(buttonPanel, BorderLayout.CENTER);
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

    }

    private void switchToBrowseStudySet() {
        viewManagerModel.setState(browseStudySetViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
