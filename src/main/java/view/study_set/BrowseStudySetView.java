// Initial UI for browsing study set - HUZAIFA
package view.study_set;

import interface_adapter.ViewManagerModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.studyset.studyset_browse.BrowseStudySetViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BrowseStudySetView extends JPanel implements PropertyChangeListener {
    private final String viewName = "browse study set";
    private final BrowseStudySetViewModel browseStudySetViewModel;
    private final MainScreenViewModel mainScreenViewModel;
    private final ViewManagerModel viewManagerModel;

    public BrowseStudySetView(BrowseStudySetViewModel browseStudySetViewModel,
                              MainScreenViewModel mainScreenViewModel,
                              ViewManagerModel viewManagerModel) {
        this.browseStudySetViewModel = browseStudySetViewModel;
        this.mainScreenViewModel = mainScreenViewModel;
        this.viewManagerModel = viewManagerModel;
        this.browseStudySetViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ---------- Root Panel ----------
        JPanel rootPanel = new JPanel(new BorderLayout(10, 10));
        rootPanel.setBackground(Color.WHITE);
        rootPanel.setBorder(new EmptyBorder(20, 60, 20, 60));
        add(rootPanel, BorderLayout.CENTER);

        // ---------- Title ----------
        JLabel title = new JLabel("Study Sets Overview", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        rootPanel.add(title, BorderLayout.NORTH);

        // ---------- Center Panel (Search Bar + Scrollable List) ----------
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);

        // ---------- Search Bar ----------
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JTextField searchField = new JTextField("Search Field...");
        searchField.setFont(new Font("Helvetica", Font.PLAIN, 18));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JButton menuButton = new JButton("☰");
        menuButton.setFont(new Font("Helvetica", Font.PLAIN, 18));
        menuButton.setFocusPainted(false);
        menuButton.setBackground(Color.WHITE);
        menuButton.setBorder(new LineBorder(Color.GRAY, 1));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(menuButton, BorderLayout.EAST);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // ---------- Study Sets (Scrollable List) ----------
        String[][] sets = {
                {"MAT137", "All About Integrals"},
                {"BIO694", "Cardiovascular Rupture Set"},
                {"STA237", "Standard Deviation Practice"},
                {"CSC207", "Java OOP Review"},
                {"ECO200", "Microeconomics Problem Sets"},
                {"CSC236", "Structural Induction Notes"},
                {"MAT235", "Calculus Notes"},
                {"APS1001", "Project Management Case Study"},
                {"CSC110", "Introduction to Python"},
                {"MAT223", "All about Linear Algebra"}
        };

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        for (String[] set : sets) {
            listPanel.add(createStudySetCard(set[0], set[1]));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        // ---------- Bottom Buttons ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton backButton = createStyledButton("Back");
        JButton uploadButton = createStyledButton("Upload to Cloud");
        JButton createButton = createStyledButton("Create");

        buttonPanel.add(backButton);
        buttonPanel.add(uploadButton);
        buttonPanel.add(createButton);

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ---------- Button Logic ----------
        backButton.addActionListener(e -> switchToMainScreen());

        uploadButton.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Upload feature coming soon!")
        );

        createButton.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Create new study set...")
        );
    }

    // ---------- Create a Study Set Card ----------
    private JPanel createStudySetCard(String code, String name) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel label = new JLabel("[" + code + "] " + name);
        label.setFont(new Font("Helvetica", Font.PLAIN, 18));

        // ---------- Right Icons ----------
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton bookmarkButton = new JButton("bookmark");
        JButton editButton = new JButton("edit");
        styleIconButton(bookmarkButton);
        styleIconButton(editButton);

        actionPanel.add(bookmarkButton);
        actionPanel.add(editButton);

        card.add(label, BorderLayout.CENTER);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    // ---------- Styled Main Buttons ----------
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Helvetica", Font.BOLD, 18));
        button.setBackground(new Color(70, 130, 180)); // blue accent
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return button;
    }

    // ---------- Styled Small Icon Buttons ----------
    private void styleIconButton(JButton button) {
        button.setFont(new Font("Helvetica", Font.PLAIN, 18));
        button.setBackground(new Color(70, 130, 180)); // blue accent
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void switchToMainScreen() {
        viewManagerModel.setState(mainScreenViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // No dynamic fields to update yet.
    }
}
