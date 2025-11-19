// Temporary interactive deck creator for testing
// Archie

package frameworks_and_drivers.DataAccess.DeckManagement;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import java.util.ArrayList;
import java.util.Scanner;

public class DeckCreatorTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Interactive Deck Creator ===");

        // Get deck specifications
        System.out.print("Enter deck title: ");
        String title = scanner.nextLine();

        System.out.print("Enter deck description: ");
        String description = scanner.nextLine();

        System.out.print("Enter deck ID (number): ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("How many cards do you want to add? ");
        int numCards = Integer.parseInt(scanner.nextLine());

        ArrayList<StudyCard> cards = new ArrayList<>();

        for (int i = 0; i < numCards; i++) {
            System.out.println("\n--- Card " + (i + 1) + " ---");
            System.out.print("Enter question: ");
            String question = scanner.nextLine();

            System.out.print("How many answer options? ");
            int numAnswers = Integer.parseInt(scanner.nextLine());

            ArrayList<String> answers = new ArrayList<>();
            for (int j = 0; j < numAnswers; j++) {
                System.out.print("Enter answer option " + (j + 1) + ": ");
                answers.add(scanner.nextLine());
            }

            System.out.print("Enter solution ID (index of correct answer, starting from 0): ");
            int solutionId = Integer.parseInt(scanner.nextLine());

            StudyCard card = new StudyCard(question, answers, solutionId);
            cards.add(card);

            System.out.println("Added card: " + question);
        }

        // Create the deck
        StudyDeck deck = new StudyDeck(title, description, cards, id);

        // Save to local storage
        LocalDeckManager manager = new LocalDeckManager();
        manager.saveDeck(deck);

        System.out.println("\n=== Deck Created Successfully! ===");
        System.out.println("Title: " + deck.getTitle());
        System.out.println("Description: " + deck.getDescription());
        System.out.println("ID: " + deck.getId());
        System.out.println("Number of cards: " + deck.getDeck().size());
        System.out.println("Deck saved to local storage!");

        scanner.close();
    }
}