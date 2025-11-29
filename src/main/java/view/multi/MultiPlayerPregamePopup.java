package view.multi;

import java.awt.BorderLayout;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.peer.PeerConnection;


public class MultiPlayerPregamePopup extends JDialog {

  private final DefaultTableModel studySetModel;
  private final JTextArea createStatusArea;
  private final JTextArea joinStatusArea;
  private final List<StudyDeck> studyDecks = new ArrayList<>();
  private final Map<String, Object> quickSettings = new HashMap<>();
  private StudyDeck selectedStudyDeck;

  private JTextField idDisp;
  private JTextField roomIdField;
  private JSpinner questionSpinner;
  private JButton hostNextButton;

  private PeerConnection pc;
  private String mode;

  private User user;
  private User otherUser;

  public MultiPlayerPregamePopup(List<StudyDeck> allDecks, User user) {
    this.user = user;
    if (allDecks.size() == 0) {
      throw new IllegalArgumentException("At least one study deck must be provided");
    }
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
        return column == 0 && row < studyDecks.size();
      }

      @Override
      public void setValueAt(Object value, int row, int column) {
        if (column == 0) {
          for (int i = 0; i < studyDecks.size(); i++) {
            super.setValueAt(false, i, column);
          }
          selectedStudyDeck = studyDecks.get(row);
          questionSpinner.setModel(
              new SpinnerNumberModel(selectedStudyDeck.getCardCount(), 0, selectedStudyDeck.getCardCount(), 1));
          super.setValueAt(value, row, column);
        }
      }
    };

    createStatusArea = buildStatusArea();
    joinStatusArea = buildStatusArea();

    studySetModel.setRowCount(0);
    studyDecks.clear();
    studyDecks.addAll(allDecks);
    studyDecks.removeIf(deck -> deck.getCardCount() == 0);
    for (StudyDeck set : studyDecks) {
      studySetModel.addRow(new Object[] { false, String.format("[%s] %s", set.getTitle(), set.getDescription()) });
    }

    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Create Room", buildCreateRoomPanel());
    tabs.addTab("Join", buildJoinPanel());

    tabs.addChangeListener(e -> {

      updateStatus(createStatusArea, "Tab changed. Resetting peer connection...");
      updateStatus(joinStatusArea, "Tab changed. Resetting peer connection...");
      resetPeerConnection();

      if (tabs.getSelectedIndex() == 0) {
        showRoomId(pc.getUid());
        // create room tab
        registerHostCallbacks();
      }
    });

    add(tabs, BorderLayout.CENTER);

    resetPeerConnection();
    // default to hosting mode
    SwingUtilities.invokeLater(() -> tabs.setSelectedIndex(0));
    registerHostCallbacks();
    showRoomId(pc.getUid());
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
    idDisp = new JTextField("", 30);
    idDisp.setEditable(false);
    idDisp.setHorizontalAlignment(JTextField.CENTER);
    idRow.add(idLabel);
    idRow.add(idDisp);
    statusContainer.add(idRow, BorderLayout.SOUTH);

    JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    hostNextButton = new JButton("Next...");
    hostNextButton.setEnabled(false);
    hostNextButton.addActionListener(e -> {
      mode = "host";
      if (selectedStudyDeck == null) {
        JOptionPane.showMessageDialog(this, "Please select a study deck before proceeding.", "No Deck Selected",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      pc.sendData("SECONDS:" + this.quickSettings.get("secondsPerQuestion").toString());
      pc.sendData(serializeDeck(selectedStudyDeck));
      this.dispose();
    });
    buttonRow.add(hostNextButton);

    JPanel southContainer = new JPanel(new BorderLayout());
    southContainer.add(statusContainer, BorderLayout.CENTER);
    southContainer.add(buttonRow, BorderLayout.SOUTH);
    panel.add(southContainer, BorderLayout.SOUTH);

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

    JSpinner secondsSpinner = new JSpinner(new SpinnerNumberModel(5, 5, 30, 5));
    gbc.gridx = 1;
    quickSettingsPanel.add(secondsSpinner, gbc);
    quickSettings.put("secondsPerQuestion", secondsSpinner.getValue());
    secondsSpinner.addChangeListener(e -> quickSettings.put("secondsPerQuestion", secondsSpinner.getValue()));

    JLabel questionCountLabel = new JLabel("# of questions");
    gbc.gridx = 0;
    gbc.gridy = 1;
    quickSettingsPanel.add(questionCountLabel, gbc);

    questionSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
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
    gbc.gridy = 1;
    gbc.gridx = 1;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    formPanel.add(joinArrow, gbc);

    joinArrow.addActionListener(e -> {
      String roomId = roomIdField.getText().trim();
      if (!roomId.isEmpty()) {
        updateStatus(joinStatusArea, "Attempting to join room " + roomId + "...");
        pc.connectToPeer(roomId, () -> {
          updateStatus(joinStatusArea, "Successfully joined room " + roomId + ".");
          pc.sendData(user.getUserName());

          // recieved deck or user data
          pc.onDataRecieved(data -> {
            if (data.startsWith("USER:")) {
              String username = data.substring(5);
              otherUser = new User(username, "");
              updateStatus(joinStatusArea, "Other player joined: " + otherUser.getUserName());
              return;
            } else if (data.startsWith("SECONDS:")) {
              this.quickSettings.put("secondsPerQuestion", Integer.parseInt(data.substring(8)));
            } else {
              StudyDeck deck = deserializeDeck(data);
              selectedStudyDeck = deck;
              SwingUtilities.invokeLater(() -> {
                mode = "guest";
                this.dispose();
              });
            }
          });
        });
      } else {
        updateStatus(joinStatusArea, "Please enter a valid Room ID.");
      }
    });

    panel.add(formPanel, BorderLayout.CENTER);

    JPanel statusPanel = new JPanel(new BorderLayout(5, 5));
    statusPanel.add(new JLabel("Status"), BorderLayout.NORTH);
    statusPanel.add(new JScrollPane(joinStatusArea), BorderLayout.CENTER);

    JPanel southContainer = new JPanel(new BorderLayout());
    southContainer.add(statusPanel, BorderLayout.CENTER);
    panel.add(southContainer, BorderLayout.SOUTH);

    return panel;
  }

  private JTextArea buildStatusArea() {
    JTextArea area = new JTextArea(4, 30);
    area.setEditable(false);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    return area;
  }

  private void registerHostCallbacks() {
    pc.onConnection(() -> {
      updateStatus(createStatusArea, "Peer connected.");
      pc.sendData("USER:" + user.getUserName());
    });
    pc.onDataRecieved(data -> {
      otherUser = new User(data, "");
      updateStatus(createStatusArea, "Other player joined: " + otherUser.getUserName());
      SwingUtilities.invokeLater(() -> hostNextButton.setEnabled(true));
    });
  }

  private void updateStatus(JTextArea target, String message) {
    target.append(message + System.lineSeparator());
    target.setCaretPosition(target.getDocument().getLength());
  }

  private void resetPeerConnection() {
    if (pc != null) {
      pc.dispose();
    }
    updateStatus(createStatusArea, "Initializing...");
    updateStatus(joinStatusArea, "Initializing...");

    pc = new PeerConnection();

    updateStatus(createStatusArea, "Initialization complete.");
    updateStatus(joinStatusArea, "Initializing complete.");
  }

  private void showRoomId(String id) {
    idDisp.setText(id);
  }

  private String serializeDeck(StudyDeck deck) {
    JSONArray cardArray = new JSONArray();
    for (StudyCard card : deck.getDeck()) {
      JSONArray answerArray = new JSONArray();
      for (String answer : card.getOptions()) {
        answerArray.put(answer);
      }
      JSONObject cardEntry = new JSONObject();
      cardEntry.put("question", card.getQuestionTitle());
      cardEntry.put("answers", answerArray);
      cardEntry.put("solutionId", card.getAnswerId());
      cardArray.put(cardEntry);
    }
    return cardArray.toString();
  }

  private StudyDeck deserializeDeck(String deckData) {
    JSONArray cardArray = new JSONArray(deckData);
    ArrayList<StudyCard> cards = new ArrayList<>();
    for (int i = 0; i < cardArray.length(); i++) {
      JSONObject cardEntry = cardArray.getJSONObject(i);
      String question = cardEntry.getString("question");
      JSONArray answerArray = cardEntry.getJSONArray("answers");
      ArrayList<String> answers = new ArrayList<>();
      for (int j = 0; j < answerArray.length(); j++) {
        answers.add(answerArray.getString(j));
      }
      int solutionId = cardEntry.getInt("solutionId");
      StudyCard card = new StudyCard(question, answers, solutionId);
      cards.add(card);
    }
    return new StudyDeck("Imported Deck", "Deck imported from peer", cards, (int) Math.floor(Math.random() * 9999));
  }

  public StudyDeck getSelectedDeck() {
    return selectedStudyDeck;
  }

  public PeerConnection getPeerConnection() {
    return pc;
  }

  public Map<String, Object> getSettings() {
    return quickSettings;
  }

  public String getMode() {
    return mode;
  }

  public User getOtherUser() {
    return otherUser;
  }

  // public static void main(String[] args) {
  //   List<StudyDeck> demoDecks = new ArrayList<>();
  //   demoDecks.add(new StudyDeck("Biology 101", "Basic biology concepts", new ArrayList<>(), 1));
  //   demoDecks.add(new StudyDeck("History of Art", "Famous art movements", new ArrayList<>(
  //       List.of(new StudyCard("Who painted the Mona Lisa?",
  //           new ArrayList<>(List.of("Vincent van Gogh", "Pablo Picasso", "Leonardo da Vinci", "Claude Monet")), 2),
  //           new StudyCard("What art movement is Salvador Dalí associated with?",
  //               new ArrayList<>(List.of("Cubism", "Surrealism", "Impressionism", "Baroque")), 1))),
  //       2));
  //   MultiPlayerPregamePopup t = new MultiPlayerPregamePopup(demoDecks);
  //   t.pack();
  //   t.setModalityType(DEFAULT_MODALITY_TYPE);
  //   t.setVisible(true);

  //   System.out.println(t.getSelectedDeck());
  // }
}
