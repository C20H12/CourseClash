package view.StudyDeck;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Popup window that provides a GUI for editing a {@link StudyDeck}.
 * It mirrors the wireframe provided by design and keeps all state in a map so it can be
 * reapplied to the {@link StudyDeck} when requested.
 */
public class StudyDeckEditPopup extends JDialog {

	private final StudyDeck originalDeck;
	private final Map<Integer, StudyCard> cardStateMap = new LinkedHashMap<>();

	private JPanel cardListPanel;
	private JTextArea questionArea;
	private final List<JTextField> answerFields = new ArrayList<>();
	private final List<JRadioButton> answerChoiceButtons = new ArrayList<>();
	private final ButtonGroup answerButtonGroup = new ButtonGroup();

	private int nextCardId = 1;
	private int selectedCardId = -1;

	public StudyDeckEditPopup(StudyDeck deck) {
		this.originalDeck = deck;

		setTitle("Study Set Editor - " + deck.getTitle());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(1100, 650);
		setLocationRelativeTo(null);

		List<StudyCard> deckCards = deck.getDeck();
		for (StudyCard card : deckCards) {
			addNewStudyCard(card.getQuestion(), card.getAnswers(), card.getSolutionId());
		}
    
    initUi();
		if (!cardStateMap.isEmpty()) {
			selectedCardId = cardStateMap.keySet().iterator().next();
			loadCardIntoEditor(selectedCardId);
		} else {
			toggleCardFieldsEnable(false);
		}
	}

	private void initUi() {
		setLayout(new BorderLayout(10, 10));
		JLabel titleLabel = new JLabel("Study Set Editor", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 26));
		titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(titleLabel, BorderLayout.NORTH);

		JPanel bodyPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		bodyPanel.add(buildLeftPanel());
		bodyPanel.add(buildRightPanel());
		add(bodyPanel, BorderLayout.CENTER);

		JPanel footer = new JPanel(new BorderLayout());
		JButton backButton = new JButton("Back");
		backButton.setPreferredSize(new Dimension(140, 45));
		backButton.addActionListener(e -> dispose());
		footer.add(backButton, BorderLayout.WEST);
	}

	private JComponent buildLeftPanel() {
		JPanel left = new JPanel(new BorderLayout(5, 10));
		left.setBorder(new EmptyBorder(10, 20, 10, 10));

		JLabel header = new JLabel("Questions");
		header.setFont(new Font("Helvetica", Font.PLAIN, 20));
		header.setHorizontalAlignment(SwingConstants.CENTER);
		left.add(header, BorderLayout.NORTH);

		cardListPanel = new JPanel();
		cardListPanel.setLayout(new BoxLayout(cardListPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(cardListPanel);
		scrollPane.setPreferredSize(new Dimension(400, 450));
		refreshCardListUi();
		left.add(scrollPane, BorderLayout.CENTER);

		JButton addButton = new JButton("Add card");
		addButton.setFont(new Font("Helvetica", Font.BOLD, 16));
		addButton.setPreferredSize(new Dimension(130, 50));
		addButton.addActionListener(e -> {
			persistCurrentCard();
			int newId = addNewStudyCard("", Arrays.asList("", "", "", ""), 0);
			selectedCardId = newId;
			refreshCardListUi();
			loadCardIntoEditor(newId);
		});
		JPanel addButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		addButtonWrapper.add(addButton);
		left.add(addButtonWrapper, BorderLayout.SOUTH);
		return left;
	}

	private JComponent buildRightPanel() {
		JPanel right = new JPanel(new BorderLayout(10, 10));
		right.setBorder(new EmptyBorder(10, 10, 10, 20));

		JPanel questionPanel = new JPanel(new BorderLayout(5, 5));
		JLabel questionLabel = new JLabel("Question:");
		questionLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
		questionPanel.add(questionLabel, BorderLayout.NORTH);
		questionArea = new JTextArea(3, 30);
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		questionArea.setFont(new Font("Helvetica", Font.PLAIN, 18));
		questionPanel.add(new JScrollPane(questionArea), BorderLayout.CENTER);
		right.add(questionPanel, BorderLayout.NORTH);

		JPanel answersPanel = new JPanel(new BorderLayout(5, 5));
		JLabel answersLabel = new JLabel("Answers:");
		answersLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
		answersPanel.add(answersLabel, BorderLayout.NORTH);

		JPanel choicesPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		answerFields.clear();
		answerChoiceButtons.clear();
		answerButtonGroup.clearSelection();
		for (int i = 0; i < 4; i++) {
			JTextField answerField = new JTextField();
			answerField.setFont(new Font("Helvetica", Font.PLAIN, 16));
			answerFields.add(answerField);

			JRadioButton radioButton = new JRadioButton("Choice " + (i + 1));
			radioButton.setFont(new Font("Helvetica", Font.PLAIN, 15));
			answerChoiceButtons.add(radioButton);
			answerButtonGroup.add(radioButton);

			choicesPanel.add(answerField);
			choicesPanel.add(radioButton);
		}
		answersPanel.add(choicesPanel, BorderLayout.CENTER);
		right.add(answersPanel, BorderLayout.CENTER);

		JButton saveButton = new JButton("Save");
		saveButton.setFont(new Font("Helvetica", Font.BOLD, 18));
		saveButton.addActionListener(e -> {
			persistCurrentCard();
			JOptionPane.showMessageDialog(this, "Card saved.");
			refreshCardListUi();
		});
		JPanel saveButtonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		saveButtonWrapper.add(saveButton);
		right.add(saveButtonWrapper, BorderLayout.SOUTH);
		return right;
	}

  // ============ crud ==============

	private int addNewStudyCard(String question, List<String> answers, int solutionIndex) {
		ArrayList<String> normalizedAnswers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			if (i < answers.size()) {
				normalizedAnswers.add(answers.get(i));
			} else {
				normalizedAnswers.add("");
			}
		}
		int id = nextCardId++;
		cardStateMap.put(id, new StudyCard(question, normalizedAnswers, clampSolutionIndex(solutionIndex)));
		return id;
	}

	private void refreshCardListUi() {
		if (cardListPanel == null) {
			return;
		}
		cardListPanel.removeAll();
		for (Map.Entry<Integer, StudyCard> entry : cardStateMap.entrySet()) {
			cardListPanel.add(createCardRow(entry.getKey(), entry.getValue()));
		}
		cardListPanel.revalidate();
		cardListPanel.repaint();
	}

	private JPanel createCardRow(int id, StudyCard state) {
		JPanel row = new JPanel(new BorderLayout());
		row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		row.setBackground(id == selectedCardId ? new Color(220, 235, 250) : Color.WHITE);

		JLabel questionLabel = new JLabel(state.getQuestion()); 
		questionLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
		questionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		questionLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectCard(id);
			}
		});
		row.add(questionLabel, BorderLayout.CENTER);

		JButton deleteButton = new JButton("X");
		deleteButton.setPreferredSize(new Dimension(50, 40));
		deleteButton.addActionListener(e -> deleteCard(id));
		row.add(deleteButton, BorderLayout.EAST);

		return row;
	}

	private void selectCard(int id) {
		if (selectedCardId == id) {
			return;
		}
		selectedCardId = id;
		loadCardIntoEditor(id);
	}

	private void loadCardIntoEditor(int id) {
		StudyCard state = cardStateMap.get(id);
		if (state == null) {
			return;
		}
		toggleCardFieldsEnable(true);
		questionArea.setText(state.getQuestion());
		for (int i = 0; i < answerFields.size(); i++) {
			answerFields.get(i).setText(state.getAnswers().get(i));
		}
		if (state.getSolutionId() >= 0 && state.getSolutionId() < answerChoiceButtons.size()) {
			answerChoiceButtons.get(state.getSolutionId()).setSelected(true);
		} else {
			answerButtonGroup.clearSelection();
		}
	}

	private void persistCurrentCard() {
		if (selectedCardId == -1 || !cardStateMap.containsKey(selectedCardId)) {
			return;
		}
		String question = questionArea.getText().trim();
    ArrayList<String> answers = new ArrayList<>();
		for (int i = 0; i < answerFields.size(); i++) {
			answers.add(answerFields.get(i).getText().trim());
		}
    int solutionIndex = 0;
		for (int i = 0; i < answerChoiceButtons.size(); i++) {
			if (answerChoiceButtons.get(i).isSelected()) {
				solutionIndex = i;
				break;
			}
		}
    StudyCard card = new StudyCard(question, answers, solutionIndex);
    cardStateMap.put(selectedCardId, card);
	}

	private void deleteCard(int id) {
		if (!cardStateMap.containsKey(id)) {
			return;
		}
		cardStateMap.remove(id);
		if (cardStateMap.isEmpty()) {
			selectedCardId = -1;
			toggleCardFieldsEnable(false);
		} else if (selectedCardId == id) {
			selectedCardId = cardStateMap.keySet().iterator().next();
			loadCardIntoEditor(selectedCardId);
		}
		refreshCardListUi();
	}

	private int clampSolutionIndex(int idx) {
		if (idx < 0) {
			return 0;
		}
		if (idx > 3) {
			return 3;
		}
		return idx;
	}

	private void toggleCardFieldsEnable(boolean enab) {
		questionArea.setEnabled(enab);
		for (JTextField field : answerFields) {
			field.setEnabled(enab);
		}
		for (JRadioButton btn : answerChoiceButtons) {
			btn.setEnabled(enab);
		}
	}

	/**
	 * Returns a new {@link StudyDeck} that reflects all the edits performed in this popup.
	 * This method persists the currently edited card before building the deck.
	 *
	 * @return Updated StudyDeck instance.
	 */
	public StudyDeck getUpdatedStudyDeck() {
		persistCurrentCard();
		ArrayList<StudyCard> newCards = new ArrayList<>();
		for (StudyCard state : cardStateMap.values()) {
			ArrayList<String> answersCopy = new ArrayList<>();
      // ensure only non blank answers are added
      for (String ans : state.getAnswers()) {
        if (ans.isBlank()) {
          continue;
        }
        answersCopy.add(ans);
      }
			// Ensure there is always a selection
			int solutionIndex = clampSolutionIndex(state.getSolutionId());

			newCards.add(new StudyCard(state.getQuestion(), answersCopy, solutionIndex));
		}
		return new StudyDeck(originalDeck.getTitle(), originalDeck.getDescription(), newCards, originalDeck.getId());
	}

  public static void main(String[] args) {
    new StudyDeckEditPopup(
        new StudyDeck(
            "Sample Deck",
            "This is a sample study deck.",
            new ArrayList<>(),
            1
        )
    ).setVisible(true);
  }
}
