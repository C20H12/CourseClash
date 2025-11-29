package view.StudyDeck;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import interface_adapter.studyDeck.StudyDeckController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Popup window that provides a GUI for editing a {@link StudyDeck}.
 * It mirrors the wireframe provided by design and keeps all state in a map so it can be
 * reapplied to the {@link StudyDeck} when requested.
 */
public class StudyDeckEditPopup extends JDialog {

	private static final Color SELECTED_CARD_COLOR = new Color(220, 235, 250);

	private final StudyDeck originalDeck;

	private JPanel cardListPanel;
	private JTextArea questionArea;
	private final List<JTextField> answerFields = new ArrayList<>();
	private final List<JRadioButton> answerChoiceButtons = new ArrayList<>();
	private final ButtonGroup answerButtonGroup = new ButtonGroup();

	private int selectedCardId = -1;

	private StudyDeckController controller;

	public StudyDeckEditPopup(StudyDeck deck, StudyDeckController controller) {
		this.originalDeck = deck;
		this.controller = controller;

		setTitle("Study Set Editor - " + deck.getTitle());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(1100, 650);
		setLocationRelativeTo(null);

		controller.initEdit(deck);

    initUi();
		StudyCard firstCard = controller.getFirstCardInCurrentEditedDeck();
		if (null != firstCard) {
			selectedCardId = 0;
			loadCardIntoEditor(firstCard);
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
			selectedCardId = controller.addNewCardToEdit();
			refreshCardListUi();
			loadCardIntoEditor(controller.getCardByIndex(selectedCardId));
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

		JPanel choicesPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
		answerFields.clear();
		answerChoiceButtons.clear();
		answerButtonGroup.clearSelection();
		for (int i = 0; i < 4; i++) {
            // setting the margin between each answer field
            gbc.gridy = i;
            gbc.weighty = 1.0;
            gbc.insets = new Insets(5, 5, 5, 5);

            JRadioButton radioButton = new JRadioButton("");
            radioButton.setFont(new Font("Helvetica", Font.PLAIN, 20));
            answerChoiceButtons.add(radioButton);
            answerButtonGroup.add(radioButton);

			JTextField answerField = new JTextField();
			answerField.setFont(new Font("Helvetica", Font.PLAIN, 16));
			answerFields.add(answerField);

            gbc.gridx = 0;
            gbc.weightx = 0.1;
            choicesPanel.add(radioButton,gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
			choicesPanel.add(answerField,gbc);
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

	private void refreshCardListUi() {
		if (cardListPanel == null) {
			return;
		}
		cardListPanel.removeAll();
		List<StudyCard> allCards = controller.getAllCards();
		for (int i = 0; i < allCards.size(); i++) {
			cardListPanel.add(createCardRow(i, allCards.get(i)));
		}

		Component[] rows = cardListPanel.getComponents();
		for (int i = 0; i < rows.length; i++) {
			if (rows[i] instanceof JPanel) {
				JPanel panel = (JPanel) rows[i];
				panel.setBackground(i == selectedCardId ? SELECTED_CARD_COLOR : Color.WHITE);
				panel.repaint();
			}
		}
		cardListPanel.revalidate();
		cardListPanel.repaint();
	}

	private JPanel createCardRow(int id, StudyCard state) {
		JPanel row = new JPanel(new BorderLayout());
		row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		row.setBackground(id == selectedCardId ? SELECTED_CARD_COLOR : Color.WHITE);

		JLabel questionLabel = new JLabel(state.getQuestion()); 
		questionLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
		questionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		questionLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (selectedCardId == id) {
					return;
				}
				persistCurrentCard();
				selectedCardId = id;
				refreshCardListUi();
				loadCardIntoEditor(controller.getCardByIndex(id));
			}
		});
		row.add(questionLabel, BorderLayout.CENTER);

		JButton deleteButton = new JButton();
        // Set trash can icon for delete
        ImageIcon rawIcon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/icon/trash_can_black.png"))
        );
        // scale to 24×24 (pick size you want)
        Image scaled = rawIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        ImageIcon trashCanIcon = new ImageIcon(scaled);
        deleteButton.setIcon(trashCanIcon);
        deleteButton.setBackground(Color.WHITE);
		deleteButton.setPreferredSize(new Dimension(50, 40));
		deleteButton.addActionListener(e -> deleteCard(id));
		row.add(deleteButton, BorderLayout.EAST);

		return row;
	}

	private void loadCardIntoEditor(StudyCard card) {
		if (card == null) {
			return;
		}
		toggleCardFieldsEnable(true);
		questionArea.setText(card.getQuestion());
		for (int i = 0; i < card.getAnswers().size(); i++) {
			answerFields.get(i).setText(card.getAnswers().get(i));
		}
		if (card.getSolutionId() >= 0 && card.getSolutionId() < answerChoiceButtons.size()) {
			answerChoiceButtons.get(card.getSolutionId()).setSelected(true);
		} else {
			answerButtonGroup.clearSelection();
		}
	}

	private void persistCurrentCard() {
		if (selectedCardId == -1 || controller.getAllCards().size() < selectedCardId) {
			return;
		}
		String question = questionArea.getText().trim();
    ArrayList<String> answers = new ArrayList<>();
		for (int i = 0; i < answerFields.size(); i++) {
			String ansText = answerFields.get(i).getText().trim();
			if (ansText.isEmpty()) continue;
			answers.add(ansText);
		}
    int solutionIndex = 0;
		for (int i = 0; i < answerChoiceButtons.size(); i++) {
			if (answerChoiceButtons.get(i).isSelected()) {
				solutionIndex = i;
				break;
			}
		}
		if (question.isEmpty() && answers.size() == 0) return;
		controller.updateCardInCurrentDeck(selectedCardId, question, answers, solutionIndex);
	}

	private void deleteCard(int id) {
		if (selectedCardId == -1 || controller.getAllCards().size() < selectedCardId) {
			return;
		}
		controller.removeCardFromCurrentDeck(id);
		StudyCard firstCard = controller.getFirstCardInCurrentEditedDeck();
		if (null != firstCard) {
			selectedCardId = 0;
			loadCardIntoEditor(firstCard);
		} else {
			toggleCardFieldsEnable(false);
		}
		refreshCardListUi();
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
		return controller.getCurrentDeck();
	}
}
