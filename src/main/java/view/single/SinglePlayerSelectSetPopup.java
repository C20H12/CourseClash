package view.single;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import entity.DeckManagement.StudyDeck;

public class SinglePlayerSelectSetPopup extends JDialog {
  private  JList<String> deckList;
    private StudyDeck selectedDeck = null;

  public SinglePlayerSelectSetPopup(List<StudyDeck> decks) {
    setTitle("Select Deck");
    setModal(true);
    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    setLayout(new BorderLayout());
    List<StudyDeck> validDecks = new ArrayList<>(decks);
    validDecks.removeIf(deck -> deck.getTitle().contains("Error"));
    // Create the list
    List<String> rowTextList = new ArrayList<>();
    for (StudyDeck deck : validDecks) {
      rowTextList.add("[ " + deck.getTitle() + "] " + deck.getDescription() + " (" + deck.getCardCount() + " cards)");
    }
    deckList = new JList<>(rowTextList.toArray(new String[0]));
    deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(deckList);
    add(scrollPane, BorderLayout.CENTER);

    // Confirm button
      JButton confirmButton = new JButton("Confirm");
    confirmButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = deckList.getSelectedIndex();
        if (selectedIndex != -1) {
          selectedDeck = validDecks.get(selectedIndex);
          dispose();
        } else {
          JOptionPane.showMessageDialog(SinglePlayerSelectSetPopup.this,
                  "Please select a deck.",
                  "No Selection",
                  JOptionPane.WARNING_MESSAGE);
        }
      }
    });
    add(confirmButton, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(null);
  }

  public StudyDeck getSelectedDeck() {
    return selectedDeck;
  }
}
