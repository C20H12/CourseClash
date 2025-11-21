// Handles all raw file system interactions for StudyDeck persistence.
// TODO Uses JSONToDeckWorker, DeckToJSONWorker. (Split)
// Archie
package frameworks_and_drivers.DataAccess.DeckManagement.JSON;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SysFileHandler {
    private final String STORAGE_DIRECTORY = System.getProperty("user.home") + "/.CourseClash/local_storage";

    // Init directories via constructor when none present
    public SysFileHandler() {
        new File(STORAGE_DIRECTORY).mkdirs();
    }

    // Save StudyDeck object as a JSON file in local storage dir
    // @param deck The StudyDeck object to save.
    public void saveDeck(StudyDeck deck) {
        String fileName = STORAGE_DIRECTORY + deck.getTitle().replace(" ", "_") + ".json";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(deckToJson(deck));
        } catch (IOException e) {
            System.err.println("Failed to save deck: " + e.getMessage());
        }
    }

    // TODO Load all decks
    // @return A List of StudyDeck objects loaded from the directory.
    public List<StudyDeck> loadAllDecks() {
        List<StudyDeck> decks = new ArrayList<>();
        File folder = new File(STORAGE_DIRECTORY);
        // IPR
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Load specified StudyDeck object from JSON file by name
    // @param deckName The name of the deck file (without extension).
    // @return The loaded StudyDeck object, or null if not found.
    public StudyDeck loadDeck(StudyDeck deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Delete StudyDeck file from local storage
    // @param deckName The name of the deck file (without extension) to delete.
    public void deleteDeck(StudyDeck deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Check if deck file with given name already exists
    // @param deckName The name of the deck to check.
    // @return True if the file exists, false otherwise.
    public boolean deckExists(StudyDeck deckName) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // Convert a StudyDeck object to a JSON string representation.
    // @param The StudyDeck object to convert.
    // @return The JSON string representation.
    private String deckToJson(StudyDeck deck) {
        StringBuilder json = new StringBuilder();

        // Construct boilerplate
        json.append("{\n");
        json.append("  \"title\": \"").append(escapeJsonString(deck.getTitle())).append("\",\n");
        json.append("  \"description\": \"").append(escapeJsonString(deck.getDescription())).append("\",\n");
        json.append("  \"id\": ").append(deck.getId()).append(",\n");
        json.append("  \"deck\": [\n");

        // Serialize each card
        for (int i = 0; i < deck.getDeck().size(); i++) {
            StudyCard card = deck.getDeck().get(i);
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

    // TODO Convert JSON string back to StudyDeck object
    // @param json The JSON string to convert.
    // @return The reconstructed StudyDeck object.
    private StudyDeck jsonToDeck(String json) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Convert JSON string to StudyCard object
    // @param cardJson The JSON string for the card.
    // @return The reconstructed StudyCard object.
    private StudyCard jsonToCard(String json) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // TODO Find matching closing bracket for JSON parsing
    // @param json The JSON string to search in.
    // @param start The starting index for the search.
    // @return The index of the matching closing bracket, or -1 if not found.
    private int findMatchingBracket(String json, int start) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    // Escape special characters in JSON strings
    // @param input The input string to escape.
    // @return The escaped string suitable for JSON.
    private String escapeJsonString(String input) {
        if (input == null) return "null";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // TODO Unescape special characters from JSON strings
    // @param input The JSON string to unescape.
    // @return The original unescaped string.
    private String unescapeJsonString(String input) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}
