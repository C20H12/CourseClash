// Initial UI for browsing study set - HUZAIFA
package view.StudyDeck;

import interface_adapter.ViewManagerModel;
import interface_adapter.main_screen.MainScreenViewModel;
import interface_adapter.studyDeck.StudyDeckController;
import interface_adapter.studyDeck.StudyDeckViewModel;
import use_case.studyDeck.StudyDeckAction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;

import entity.DeckManagement.StudyDeck;
import frameworks_and_drivers.DataAccess.DeckManagement.StudyDeckJSONFileHandler;

public class StudyDeckView extends JPanel implements PropertyChangeListener {
  private final String viewName = "browse study set";
  private final StudyDeckViewModel studyDeckViewModel;
  private final MainScreenViewModel mainScreenViewModel;
  private final ViewManagerModel viewManagerModel;

  // UI components that need to be refreshed
  private JPanel listPanel;
  private JScrollPane scrollPane;
  private JTextField searchField;

  private StudyDeckController studyDeckController;
  private List<StudyDeck> currentDecks;

  public StudyDeckView(StudyDeckViewModel browseStudySetViewModel,
      MainScreenViewModel mainScreenViewModel,
      ViewManagerModel viewManagerModel) {
    this.studyDeckViewModel = browseStudySetViewModel;
    this.mainScreenViewModel = mainScreenViewModel;
    this.viewManagerModel = viewManagerModel;
    this.studyDeckViewModel.addPropertyChangeListener(this);

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

    JLabel searchTexJLabel = new JLabel("Search: ");
    searchField = new JTextField();
    searchField.setFont(new Font("Helvetica", Font.PLAIN, 18));
    searchField.setBorder(BorderFactory.createCompoundBorder(
        new LineBorder(Color.GRAY, 1),
        new EmptyBorder(8, 10, 8, 10)));

    searchPanel.add(searchTexJLabel, BorderLayout.WEST);
    searchPanel.add(searchField, BorderLayout.CENTER);
    centerPanel.add(searchPanel, BorderLayout.NORTH);

    // ---------- Study Sets (Scrollable List) ----------
    // create the list panel and populate from storage
    listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    listPanel.setBackground(Color.WHITE);

    scrollPane = new JScrollPane(listPanel);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    centerPanel.add(scrollPane, BorderLayout.CENTER);

    // listen to search input for live filtering
    searchField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        updateStudySetList(searchField.getText(), currentDecks);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateStudySetList(searchField.getText(), currentDecks);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateStudySetList(searchField.getText(), currentDecks);
      }
    });

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

    uploadButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Upload feature coming soon!"));

    createButton.addActionListener(e -> createNewDeck());
  }

  /**
   * creating a entry in the study set list
   * 
   * @param deck the deck in this row
   * @return a panel
   */
  private JPanel createStudySetCard(StudyDeck deck) {
    // ---------- Create a Study Set Card ----------
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        new MatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
        new EmptyBorder(15, 20, 15, 20)));
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

    JLabel label = new JLabel("[" + deck.getTitle() + "] " + deck.getDescription());
    label.setFont(new Font("Helvetica", Font.PLAIN, 18));

    // error decks do not have buttons
    if (deck.getTitle().contains("Error")) {
      label.setForeground(Color.RED);
      card.add(label, BorderLayout.CENTER);
      return card;
    }

    // ---------- Right Icons ----------
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    actionPanel.setBackground(Color.WHITE);

    JButton editButton = new JButton("edit");
    JButton deleButton = new JButton("delete");
    styleIconButton(editButton);
    styleIconButton(deleButton);

    actionPanel.add(editButton);
    actionPanel.add(deleButton);

    card.add(label, BorderLayout.CENTER);
    card.add(actionPanel, BorderLayout.EAST);

    deleButton.addActionListener(e -> {
      studyDeckController.execute(deck, StudyDeckAction.REMOVE_DECK);
    });

    editButton.addActionListener(e -> {
      StudyDeckEditPopup editPopup = new StudyDeckEditPopup(deck);
      editPopup.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
      editPopup.setVisible(true);
      System.out.println(1);

      StudyDeck updatedDeck = editPopup.getUpdatedStudyDeck();
      studyDeckController.execute(updatedDeck, StudyDeckAction.EDIT_DECK);
    });

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

  /**
   * shows a dialog prompt for the name and description and creates a new deck
   */
  private void createNewDeck() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Name:"));
    JTextField nameField = new JTextField();
    panel.add(nameField);
    panel.add(new JLabel("Description:"));
    JTextArea descField = new JTextArea();
    JScrollPane descJScrollPane = new JScrollPane(descField);
    descJScrollPane.setPreferredSize(new Dimension(200, 100));
    panel.add(descJScrollPane);

    int result = JOptionPane.showConfirmDialog(this, panel, "Create New Study Set", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
      String deckTitle = nameField.getText().trim();
      String description = descField.getText().trim();
      if (!deckTitle.isEmpty()) {
        try {
          int newId = (int) Math.floor(Math.random() * 99999);
          StudyDeck newDeck = new StudyDeck(deckTitle, description, new ArrayList<>(), newId);
          studyDeckController.execute(newDeck, StudyDeckAction.ADD_DECK);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "Failed to create study set: " + ex.getMessage());
        }
      } else {
        JOptionPane.showMessageDialog(this, "Name cannot be empty.");
      }
    }
  }

  // ============== change updating ==================
  private void switchToMainScreen() {
    viewManagerModel.setState(mainScreenViewModel.getViewName());
    viewManagerModel.firePropertyChange();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // System.out.println(evt.getNewValue().toString());
    currentDecks = (List<StudyDeck>) evt.getNewValue();
    // Refresh the list when the view model fires updates
    SwingUtilities.invokeLater(
      () -> updateStudySetList(searchField != null ? searchField.getText() : "", currentDecks));
    
  }

  /**
   * Load all decks from storage, apply the optional filter, and rebuild the list
   * UI.
   * Filter matches against title and description (case-insensitive, substring).
   * 
   * @param filter the search text to filter by; empty string -> show all
   */
  private void updateStudySetList(String filter, List<StudyDeck> decks) {
    if (listPanel == null)
      return;

    String normalized = filter == null ? "" : filter.trim().toLowerCase();

    // Clear previous contents
    listPanel.removeAll();

    for (StudyDeck deck : decks) {
      String title = deck.getTitle();
      String desc = deck.getDescription();

      boolean matches = normalized.isEmpty()
          || (title != null && title.toLowerCase().contains(normalized))
          || (desc != null && desc.toLowerCase().contains(normalized));

      if (matches) {
        listPanel.add(createStudySetCard(deck));
      }
    }

    // If no items matched, show a friendly message
    if (listPanel.getComponentCount() == 0) {
      JLabel empty = new JLabel("No study sets match your search.");
      empty.setFont(new Font("Helvetica", Font.ITALIC, 16));
      empty.setBorder(new EmptyBorder(10, 10, 10, 10));
      listPanel.add(empty);
    }

    listPanel.revalidate();
    listPanel.repaint();
  }

  public String getViewName() {
    return viewName;
  }

  public void setStudySetViewController(StudyDeckController studyDeckController) {
    this.studyDeckController = studyDeckController;
    // initially populate with all decks
    studyDeckController.execute(null, StudyDeckAction.LOAD_ALL);
  }
}
