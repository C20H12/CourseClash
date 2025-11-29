// Handles all raw file system interactions for StudyDeck persistence.
// Archie

package frameworks_and_drivers.DataAccess.DeckManagement;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

public class StudyDeckJSONFileHandler {
    private final String STORAGE_DIRECTORY = "src/main/local_storage";
    private String activeStorageDirectory = STORAGE_DIRECTORY;

    // Init directories via constructor when none present
    public StudyDeckJSONFileHandler() {
        new File(activeStorageDirectory).mkdirs();
    }

    // Overloading constructor for storage directory pass-in
    public StudyDeckJSONFileHandler(String storageDirectory) {
        this.activeStorageDirectory = storageDirectory;
        new File(activeStorageDirectory).mkdirs();
    }

    // Save StudyDeck object as a JSON file in local storage dir
    // @param deck The StudyDeck object to save.
    // @return None
    public void saveDeck(StudyDeck deck) {
        final String fileName = activeStorageDirectory + "/" + deck.getTitle().replace(" ", "_") + ".json";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(deckToJson(deck));
        }
        catch (IOException e) {
            System.err.println("Failed to save deck: " + e.getMessage());
        }
    }

    // Load all decks
    // @param None
    // @return A List of StudyDeck objects loaded from the directory.
    public List<StudyDeck> loadAllDecks() {
        final List<StudyDeck> decks = new ArrayList<>();
        final File folder = new File(activeStorageDirectory);

        // Return empty list if directory doesn't exist
        if (!folder.exists()) {
            return decks;
        }

        // Otherwise collect them all
        final File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    try {
                        final String content = new String(Files.readAllBytes(file.toPath()));
                        final StudyDeck deck = jsonToDeck(content);
                        if (deck != null) {
                            decks.add(deck);
                        }
                    }
                    catch (IOException e) {
                        System.err.println("Error loading deck " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
        }
        return decks;
    }

    // Load specified StudyDeck object from JSON file by name
    // @param deckName The name of the deck file (without extension).
    // @return The loaded StudyDeck object, or null if not found.
    public StudyDeck loadDeck(String deckName) {
        final String fileName = activeStorageDirectory + "/" + deckName.replace(" ", "_") + ".json";
        final File file = new File(fileName);

        if (!file.exists()) {
            return null;
        }

        try {
            final String content = new String(Files.readAllBytes(file.toPath()));
            return jsonToDeck(content);
        }
        catch (IOException e) {
            System.err.println("Error loading deck " + deckName + ": " + e.getMessage());
            return null;
        }
    }

    // Delete StudyDeck file from local storage
    // @param deckName The name of the deck file (without extension) to delete.
    public void deleteDeck(String deckName) {
        final String fileName = activeStorageDirectory + "/" + deckName.replace(" ", "_") + ".json";
        final File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
    }

    // Check if deck file with given name already exists
    // @param deckName The name of the deck to check.
    // @return True if the file exists, false otherwise.
    public boolean deckExists(String deckName) {
        final String fileName = activeStorageDirectory + "/" + deckName.replace(" ", "_") + ".json";
        final File file = new File(fileName);
        return file.exists();
    }

    // Convert a StudyDeck object to a JSON string representation.
    // @param The StudyDeck object to convert.
    // @return The JSON string representation.
    private String deckToJson(StudyDeck deck) {
        final StringBuilder json = new StringBuilder();

        // Construct boilerplate
        json.append("{\n");
        json.append("  \"title\": \"").append(escapeJsonString(deck.getTitle())).append("\",\n");
        json.append("  \"description\": \"").append(escapeJsonString(deck.getDescription())).append("\",\n");
        json.append("  \"id\": ").append(deck.getId()).append(",\n");
        json.append("  \"deck\": [\n");

        // Serialize each card
        final List<StudyCard> cards = deck.getDeck();
        for (int i = 0; i < cards.size(); i++) {
            final StudyCard card = cards.get(i);
            json.append("    {\n");
            json.append("      \"question\": \"").append(escapeJsonString(card.getQuestionTitle())).append("\",\n");
            json.append("      \"answers\": [\n");

            // Serialize each answer
            final ArrayList<String> answers = card.getOptions();
            for (int j = 0; j < answers.size(); j++) {
                json.append("        \"").append(escapeJsonString(answers.get(j))).append("\"");

                // Append commas to non final answer entries
                if (j < answers.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("      ],\n");
            json.append("      \"solutionId\": ").append(card.getAnswerId()).append("\n");
            json.append("    }");

            // Append commas to non-final card entries
            if (i < cards.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}");
        return json.toString();
    }


    // Convert JSON string back to StudyDeck object
    // @param json The JSON string to convert.
    // @return The reconstructed StudyDeck object.
    private StudyDeck jsonToDeck(String json) {

        // JSON constants
        final int TITLE_PREFIX_LEN = "\"title\": \"".length();
        final int DESC_PREFIX_LEN = "\"description\": \"".length();
        final int ID_PREFIX_LEN = "\"id\": ".length();
        final int DECK_PREFIX_LEN = "\"deck\": [".length();

        // Extract title
        final int titleStart = json.indexOf("\"title\": \"") + TITLE_PREFIX_LEN;
        final int titleEnd = json.indexOf("\"", titleStart);
        String title = "Error: Title not found";
        if (titleStart > 9 && titleEnd > titleStart) {
            title = unescapeJsonString(json.substring(titleStart, titleEnd));
        }

        // Extract description
        final int descStart = json.indexOf("\"description\": \"") + DESC_PREFIX_LEN;
        final int descEnd = json.indexOf("\"", descStart);
        String description = "";
        if (descStart > 15 && descEnd > descStart) {
            description = unescapeJsonString(json.substring(descStart, descEnd));
        }

        // Extract id
        final int idStart = json.indexOf("\"id\": ") + ID_PREFIX_LEN;
        final int idEnd = json.indexOf(",", idStart);
        int id = 0;
        if (idStart > 5 && idEnd > idStart) {
            try {
                id = Integer.parseInt(json.substring(idStart, idEnd).trim());
            }
            catch (NumberFormatException e) {
                id = 0;
            }
        }

        // Extract deck array (array of StudyCards)
        final ArrayList<StudyCard> deckList = new ArrayList<>();
        int deckStart = json.indexOf("\"deck\": [");
        if (deckStart != -1) {
            deckStart += DECK_PREFIX_LEN;
            final int deckEnd = json.lastIndexOf("]");
            if (deckEnd > deckStart) {
                final String cardsJson = json.substring(deckStart, deckEnd);

                // Parse each card in the deck array
                int cardStart = cardsJson.indexOf("{");
                while (cardStart != -1) {
                    final int cardEnd = findMatchingBracket(cardsJson, cardStart);
                    if (cardEnd != -1) {
                        final String cardJson = cardsJson.substring(cardStart, cardEnd + 1);
                        final StudyCard card = jsonToCard(cardJson);
                        if (card != null) {
                            deckList.add(card);
                        }

                        // Look for next card
                        cardStart = cardsJson.indexOf("{", cardEnd + 1);
                    }
                    else {
                        break;
                    }
                }
            }
        }

        // Create new StudyDeck with constructor
        return new StudyDeck(title, description, deckList, id);
    }

    // Convert JSON string to StudyCard object
    // @param cardJson The JSON string for the card.
    // @return The reconstructed StudyCard object.
    private StudyCard jsonToCard(String cardJson) {
        // Extract question
        final int qStart = cardJson.indexOf("\"question\": \"") + 13;
        final int qEnd = cardJson.indexOf("\"", qStart);
        String question = "Error: Question not found";
        if (qStart > 12 && qEnd > qStart) {
            question = unescapeJsonString(cardJson.substring(qStart, qEnd));
        }

        // Extract answers array
        final ArrayList<String> answers = new ArrayList<>();
        final int answersStart = cardJson.indexOf("\"answers\": [") + 11;
        if (answersStart > 10) {
            final int answersEnd = cardJson.indexOf("]", answersStart);
            if (answersEnd > answersStart) {
                final String answersJson = cardJson.substring(answersStart, answersEnd);

                // Parse each answer option
                int ansStart = answersJson.indexOf("\"");
                while (ansStart != -1) {
                    final int ansEnd = answersJson.indexOf("\"", ansStart + 1);
                    if (ansEnd != -1) {
                        final String answer = unescapeJsonString(answersJson.substring(ansStart + 1, ansEnd));
                        answers.add(answer);

                        // Look for next answer
                        ansStart = answersJson.indexOf("\"", ansEnd + 1);
                    }
                    else {
                        break;
                    }
                }
            }
        }

        // Extract solutionId
        final int solutionIdStart = cardJson.indexOf("\"solutionId\": ") + 14;
        int solutionIdEnd = cardJson.indexOf(",", solutionIdStart);

        if (solutionIdEnd == -1) {
            solutionIdEnd = cardJson.indexOf("}", solutionIdStart);
        }

        if (solutionIdEnd == -1) {
            solutionIdEnd = cardJson.length();
        }

        int solutionId = 0;
        if (solutionIdStart > 13 && solutionIdEnd > solutionIdStart) {
            try {
                solutionId = Integer.parseInt(cardJson.substring(solutionIdStart, solutionIdEnd).trim());
            }
            catch (NumberFormatException e) {
                solutionId = 0;
            }
        }

        // Create new StudyCard with constructor
        return new StudyCard(question, answers, solutionId);
    }

    // Find matching closing bracket for JSON parsing
    // @param json The JSON string to search in.
    // @param start The starting index for the search.
    // @return The index of the matching closing bracket, or -1 if not found.
    private int findMatchingBracket(String json, int start) {
        int bracketCount = 0;
        for (int i = start; i < json.length(); i++) {
            final char c = json.charAt(i);
            if (c == '{') {
                bracketCount++;
            }
            else if (c == '}') {
                bracketCount--;
                if (bracketCount == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Escape special characters in JSON strings
    // @param input The input string to escape.
    // @return The escaped string suitable for JSON.
    private String escapeJsonString(String input) {
        if (input == null) {
            return "null";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Unescape special characters from JSON strings
    // @param input The JSON string to unescape.
    // @return The original unescaped string.
    private String unescapeJsonString(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}
