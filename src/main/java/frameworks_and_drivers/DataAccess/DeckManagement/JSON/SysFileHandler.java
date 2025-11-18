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
    private final String STORAGE_DIRECTORY = System.getProperty("user.home") + "/.CourseClash/local_storage";

    // Init directories if none present
    public SysFileHandler() {
        new File(STORAGE_DIRECTORY).mkdirs();
    }

    // Method to save deck
    public void saveDeck(StudyDeck deck) {
        String fileName = STORAGE_DIRECTORY + deck.title.replace(" ", "_") + ".json";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(deckToJson(deck));
        } catch (IOException e) {
            System.err.println("Failed to save deck: " + e.getMessage());
        }
    }

    // TODO transfer to DeckToJsonWorker when testing's done
    // Method to convert deck object to json file
    private String deckToJson(StudyDeck deck) {
        StringBuilder json = new StringBuilder();

        // Construct boilerplate
        json.append("{\n");
        json.append("  \"title\": \"").append(escapeJsonString(deck.title)).append("\",\n");
        json.append("  \"description\": \"").append(escapeJsonString(deck.description)).append("\",\n");
        json.append("  \"id\": ").append(deck.id).append(",\n");
        json.append("  \"deck\": [\n");

        // Serialize each card
        for (int i = 0; i < deck.deck.size(); i++) {
            StudyCard card = deck.deck.get(i);
            json.append("    {\n");
            json.append("      \"question\": \"").append(escapeJsonString(card.getQuestion())).append("\",\n");
            json.append("      \"answer\": [\n");

            // Serialize each answer
            for (int j = 0; j < card.answer.size(); j++) {
                json.append("        \"").append(escapeJsonString(card.answer.get(j))).append("\"");

                // Append commas to non final answer entries
                if (j < card.answer.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("      ]\n");
            json.append("    }");

            // Append commas to non final card entries
            if (i < deck.deck.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}");
        return json.toString();
    }

    public List<StudyDeck> loadAllDecks(){
        // Empty list to collect decks
        List<StudyDeck> decks = new ArrayList<>();

        //directory
        File folder = new File(STORAGE_DIRECTORY);

        // for loop throygh all files in directory
        throw new UnsupportedOperationException("Method not yet implemented");
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
