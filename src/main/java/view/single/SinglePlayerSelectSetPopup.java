package view.single;

import javax.swing.*;

import entity.DeckManagement.StudyDeck;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SinglePlayerSelectSetPopup extends JDialog {
  private JList<String> deckList;
  private JButton confirmButton;
  private StudyDeck selectedDeck = null;

  public SinglePlayerSelectSetPopup(List<StudyDeck> decks) {
    setTitle("Select Deck");
    setModal(true);
    setLayout(new BorderLayout());

    // Create the list
    List<String> rowTextList = new ArrayList<>();
    for (StudyDeck deck : decks) {
      if (deck.getTitle().contains("Error")) continue;

      rowTextList.add("[ " + deck.getTitle() + "] " + deck.getDescription() + " (" + deck.getCardCount() + " cards)");
    }
    deckList = new JList<>(rowTextList.toArray(new String[0]));
    deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(deckList);
    add(scrollPane, BorderLayout.CENTER);

    // Confirm button
    confirmButton = new JButton("Confirm");
    confirmButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = deckList.getSelectedIndex();
        if (selectedIndex != -1) {
          selectedDeck = decks.get(selectedIndex);
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
