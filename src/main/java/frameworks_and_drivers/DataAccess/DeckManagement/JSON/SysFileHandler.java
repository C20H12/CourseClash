// Manages file I/O operations in user's directory
// Uses JSONToDeckWorker, DeckToJSONWorker.
// Archie
package frameworks_and_drivers.DataAccess.DeckManagement.JSON;

// entities
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

// vanilla java libs
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SysFileHandler {

    // Declarations
    private final String STORAGE_DIRECTORY = System.getProperty("user.home") + "/.CourseClash/local_storage";

    // Make directories if there's none existin
    public SysFileHandler() {
        new File(STORAGE_DIRECTORY).mkdirs();
    }

    // Deck saving functionality
    public void saveDeck(StudyDeck deck) {
        String fileName = STORAGE_DIRECTORY + deck.title.replace(" ", "_") + ".json";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(deckToJson(deck));
        } catch (IOException e) {
            System.err.println("Failed to save deck: " + e.getMessage());
        }
    }

    // Helper method <- gonna transfer to DeckToJsonWorker...
    private String decktoJson(StudyDeck deck) {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"title\": \"").append(escapeJsonString(deck.title)).append("\",\n");
        json.append("  \"description\": \"").append(escapeJsonString(deck.description)).append("\",\n");
        json.append("  \"id\": ").append(deck.id).append(",\n");
        json.append("  \"deck\": [\n");

        // serializing each card in deck loop
        for (int i = 0; i < deck.deck.size(); i++) {
            StudyCard card = deck.deck.get(i);
            json.append("    {\n");
            json.append("      \"question\": \"").append(escapeJsonString(card.getQuestion())).append("\",\n");
            json.append("      \"answer\": [\n");

            // serializing every single answer
            for (int j = 0; j < card.answer.size(); j++) {
                System.out.println("TODO"); // TODO
            }
            }
    }

    public List<StudyDeck> loadAllDecks(){
        // Empty list to collect decks
        List<StudyDeck> decks = new ArrayList<>();

        //directory
        File folder = new File(STORAGE_DIRECTORY);

        // for loop throygh all files in directory
    }

    //escape special characters
    private String escapeJsonString(String input) {
        if (input == null) return "null";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

}
