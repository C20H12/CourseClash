package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import entity.DeckManagement.StudyDeck;

public class MultiPlayerView extends JPanel {

  private DefaultTableModel studySetModel;
  private JTextArea createStatusArea;
  private JTextArea joinStatusArea;
  private final List<StudyDeck> studySets = new ArrayList<>();
  private final Map<String, Object> quickSettings = new HashMap<>();
  private StudyDeck selectedStudySet;

  private JTextField idDisp;
  private JTextField roomIdField;

  public MultiPlayerView() {
    setLayout(new BorderLayout());
    studySetModel = new DefaultTableModel(new Object[] { "", "Select Study" }, 0) {
      @Override
      public Class<?> getColumnClass(int columnIndex) {
        // boolean class defaults to checkboxes
        return columnIndex == 0 ? Boolean.class : String.class;
      }

      @Override
      public boolean isCellEditable(int row, int column) {
        // Only allow toggling checkboxes for real study sets, not the trailing note.
        return column == 0 && row < studySets.size();
      }

      @Override
      public void setValueAt(Object value, int row, int column) {
        if (column == 0) {
          for (int i = 0; i < studySets.size(); i++) {
            super.setValueAt(false, i, column);
          }
          selectedStudySet = studySets.get(row);
          super.setValueAt(value, row, column);
        }
      }
    };

    createStatusArea = buildStatusArea();
    joinStatusArea = buildStatusArea();

    loadStudySets();

    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Create Room", buildCreateRoomPanel());
    tabs.addTab("Join", buildJoinPanel());

    add(tabs, BorderLayout.CENTER);
  }

  private JPanel buildCreateRoomPanel() {
    final int padding = 15;
    JPanel panel = new JPanel(new BorderLayout(padding, padding));
    panel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

    JPanel selectionAndSettings = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 0, 0, 15);
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 0.6;
    gbc.weighty = 1;

    JTable table = new JTable(studySetModel);
    table.setRowHeight(28);
    table.getColumnModel().getColumn(0).setMaxWidth(40);
    JScrollPane tableScroll = new JScrollPane(table);
    JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
    tablePanel.add(new JLabel("Select Study Set"), BorderLayout.NORTH);
    tablePanel.add(tableScroll, BorderLayout.CENTER);
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    selectionAndSettings.add(tablePanel, gbc);
    
    JPanel quickSettings = buildQuickSettingsPanel();
    gbc.gridx = 2;
    gbc.gridwidth = 1;
    gbc.weightx = 0;
    selectionAndSettings.add(quickSettings, gbc);

    panel.add(selectionAndSettings, BorderLayout.CENTER);

    JPanel statusContainer = new JPanel(new BorderLayout(10, 10));
    statusContainer.add(new JLabel("Status"), BorderLayout.NORTH);
    statusContainer.add(new JScrollPane(createStatusArea), BorderLayout.CENTER);

    JPanel idRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
    JLabel idLabel = new JLabel("Your room ID is:");
    idDisp = new JTextField("", 20);
    idDisp.setEditable(false);
    idDisp.setHorizontalAlignment(JTextField.CENTER);
    idRow.add(idLabel);
    idRow.add(idDisp);
    statusContainer.add(idRow, BorderLayout.SOUTH);

    JPanel southContainer = new JPanel(new BorderLayout());
    southContainer.add(statusContainer, BorderLayout.CENTER);
    southContainer.add(buildNavigationButtons(), BorderLayout.SOUTH);
    panel.add(southContainer, BorderLayout.SOUTH);

    showRoomId("abcd-1234-efgh-2345");
    updateStatus(createStatusArea, "Initializing...");
    updateStatus(createStatusArea, "Waiting...");
    updateStatus(createStatusArea, "Player (id: otherid) joined.");
    updateStatus(createStatusArea, "Ready.");

    return panel;
  }

  private JPanel buildQuickSettingsPanel() {
    JPanel quickSettingsPanel = new JPanel(new GridBagLayout());
    quickSettingsPanel.setBorder(BorderFactory.createTitledBorder("Quick Settings"));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel secondsLabel = new JLabel("# sec time per question");
    gbc.gridx = 0;
    gbc.gridy = 0;
    quickSettingsPanel.add(secondsLabel, gbc);

    JSpinner secondsSpinner = new JSpinner(new SpinnerNumberModel(30, 5, 300, 5));
    gbc.gridx = 1;
    quickSettingsPanel.add(secondsSpinner, gbc);
    quickSettings.put("secondsPerQuestion", secondsSpinner.getValue());
    secondsSpinner.addChangeListener(e -> quickSettings.put("secondsPerQuestion", secondsSpinner.getValue()));

    JLabel questionCountLabel = new JLabel("# of questions");
    gbc.gridx = 0;
    gbc.gridy = 1;
    quickSettingsPanel.add(questionCountLabel, gbc);

    JSpinner questionSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 200, 1));
    gbc.gridx = 1;
    quickSettingsPanel.add(questionSpinner, gbc);
    quickSettings.put("questionCount", questionSpinner.getValue());
    questionSpinner.addChangeListener(e -> quickSettings.put("questionCount", questionSpinner.getValue()));

    JLabel keepScoreLabel = new JLabel("on/off keep score");
    gbc.gridx = 0;
    gbc.gridy = 2;
    quickSettingsPanel.add(keepScoreLabel, gbc);

    JCheckBox keepScoreCheck = new JCheckBox("Keep score", true);
    gbc.gridx = 1;
    quickSettingsPanel.add(keepScoreCheck, gbc);
    quickSettings.put("keepScore", keepScoreCheck.isSelected());
    keepScoreCheck.addActionListener(e -> quickSettings.put("keepScore", keepScoreCheck.isSelected()));

    return quickSettingsPanel;
  }

  private JPanel buildJoinPanel() {
    JPanel panel = new JPanel(new BorderLayout(15, 15));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JPanel formPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;

    JLabel roomIdLabel = new JLabel("Room ID");
    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(roomIdLabel, gbc);

    roomIdField = new JTextField();
    gbc.gridy = 1;
    formPanel.add(roomIdField, gbc);

    JButton joinArrow = new JButton("Go");
    joinArrow.addActionListener(e -> updateStatus(joinStatusArea, "Attempting to join room " + roomIdField.getText()));
    gbc.gridy = 1;
    gbc.gridx = 1;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(joinArrow, gbc);

    panel.add(formPanel, BorderLayout.CENTER);

    JPanel statusPanel = new JPanel(new BorderLayout(5, 5));
    statusPanel.add(new JLabel("Status"), BorderLayout.NORTH);
    statusPanel.add(new JScrollPane(joinStatusArea), BorderLayout.CENTER);

    JPanel southContainer = new JPanel(new BorderLayout());
    southContainer.add(statusPanel, BorderLayout.CENTER);
    southContainer.add(buildNavigationButtons(), BorderLayout.SOUTH);
    panel.add(southContainer, BorderLayout.SOUTH);

    updateStatus(joinStatusArea, "Initializing...");
    updateStatus(joinStatusArea, "Joined...");
    updateStatus(joinStatusArea, "Ready.");

    return panel;
  }

  private JPanel buildNavigationButtons() {
    JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    JButton backButton = new JButton("Back");
    JButton nextButton = new JButton("Next...");
    buttonRow.add(backButton);
    buttonRow.add(nextButton);
    return buttonRow;
  }

  private JTextArea buildStatusArea() {
    JTextArea area = new JTextArea(4, 30);
    area.setEditable(false);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    return area;
  }

  private void loadStudySets() {
    studySetModel.setRowCount(0);
    studySets.clear();
    studySets.addAll(fetchStudySets());
    for (StudyDeck set : studySets) {
      studySetModel.addRow(new Object[] { false, String.format("[%s] %s", set.getTitle(), set.getDescription()) });
    }
    studySetModel.addRow(new Object[] { false, "... Other saved study sets." });
  }

  private List<StudyDeck> fetchStudySets() {
    List<StudyDeck> sets = new ArrayList<>();
    sets.add(new StudyDeck("MAT137", "All About Integrals", new ArrayList<>(), 0));
    sets.add(new StudyDeck("BIO694", "Cardiovascular Rupture Set", new ArrayList<>(), 0));
    sets.add(new StudyDeck("STA237", "Standard Deviation Practice", new ArrayList<>(), 0));
    return sets;
  }

  private void updateStatus(JTextArea target, String message) {
    target.append(message + System.lineSeparator());
    target.setCaretPosition(target.getDocument().getLength());
  }

  private void showRoomId(String id) {
    idDisp.setText(id);
  }



  public static void main(String[] args) {
    JPanel t = new MultiPlayerView();
    JFrame f = new JFrame("Study Session Lobby");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(new Dimension(950, 520));
    f.setLayout(new BorderLayout());
    f.add(t, BorderLayout.CENTER);
    f.pack();
    f.setLocationRelativeTo(null);
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException
          | IllegalAccessException e) {
        e.printStackTrace();
      }

      f.setVisible(true);
    });
  }
}
