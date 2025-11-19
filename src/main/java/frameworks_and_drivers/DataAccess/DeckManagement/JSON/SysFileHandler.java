// Handles all raw file system interactions for StudyDeck persistence.
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
        String fileName = STORAGE_DIRECTORY + "/" + deck.getTitle().replace(" ", "_") + ".json";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(deckToJson(deck));
        } catch (IOException e) {
            System.err.println("Failed to save deck: " + e.getMessage());
        }
    }

    // Load all decks
    // @return A List of StudyDeck objects loaded from the directory.
    public List<StudyDeck> loadAllDecks() {
        List<StudyDeck> decks = new ArrayList<>();
        File folder = new File(STORAGE_DIRECTORY);

        // Return empty list if directory doesn't exist
        if (!folder.exists()) {
            return decks;
        }

        // Otherwise collect them all
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    try {
                        String content = new String(Files.readAllBytes(file.toPath()));
                        StudyDeck deck = jsonToDeck(content);
                        if (deck != null) {
                            decks.add(deck);
                        }
                    } catch (IOException e) {
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
        String fileName = STORAGE_DIRECTORY + "/" + deckName.replace(" ", "_") + ".json";
        File file = new File(fileName);

        if (!file.exists()) {
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            return jsonToDeck(content);
        } catch (IOException e) {
            System.err.println("Error loading deck " + deckName + ": " + e.getMessage());
            return null;
        }
    }

    // Delete StudyDeck file from local storage
    // @param deckName The name of the deck file (without extension) to delete.
    public void deleteDeck(String deckName) {
        String fileName = STORAGE_DIRECTORY + "/" + deckName.replace(" ", "_") + ".json";
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
    }

    // Check if deck file with given name already exists
    // @param deckName The name of the deck to check.
    // @return True if the file exists, false otherwise.
    public boolean deckExists(String deckName) {
        String fileName = STORAGE_DIRECTORY + "/" + deckName.replace(" ", "_") + ".json";
        File file = new File(fileName);
        return file.exists();
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
        List<StudyCard> cards = deck.getDeck();
        for (int i = 0; i < cards.size(); i++) {
            StudyCard card = cards.get(i);
            json.append("    {\n");
            json.append("      \"question\": \"").append(escapeJsonString(card.getQuestion())).append("\",\n");
            json.append("      \"answers\": [\n"); // Changed from "answer" to "answers"

            // Serialize each answer
            ArrayList<String> answers = card.getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                json.append("        \"").append(escapeJsonString(answers.get(j))).append("\"");

                // Append commas to non final answer entries
                if (j < answers.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("      ],\n"); // Changed to comma for solutionId
            json.append("      \"solutionId\": ").append(card.getSolutionId()).append("\n"); // Added solutionId
            json.append("    }");

            // Append commas to non final card entries
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
        // Extract title
        int titleStart = json.indexOf("\"title\": \"") + 10;
        int titleEnd = json.indexOf("\"", titleStart);
        String title = "Error: Title not found";
        if (titleStart > 9 && titleEnd > titleStart) {
            title = unescapeJsonString(json.substring(titleStart, titleEnd));
        }

        // Extract description
        int descStart = json.indexOf("\"description\": \"") + 16;
        int descEnd = json.indexOf("\"", descStart);
        String description = "";
        if (descStart > 15 && descEnd > descStart) {
            description = unescapeJsonString(json.substring(descStart, descEnd));
        }

        // Extract id
        int idStart = json.indexOf("\"id\": ") + 6;
        int idEnd = json.indexOf(",", idStart);
        int id = 0;
        if (idStart > 5 && idEnd > idStart) {
            try {
                id = Integer.parseInt(json.substring(idStart, idEnd).trim());
            } catch (NumberFormatException e) {
                id = 0; // Default value if parsing fails
            }
        }

        // Extract deck array (array of StudyCards)
        ArrayList<StudyCard> deckList = new ArrayList<>();
        int deckStart = json.indexOf("\"deck\": [");
        if (deckStart != -1) {
            deckStart += 9; // Move past "\"deck\": ["
            int deckEnd = json.lastIndexOf("]");
            if (deckEnd > deckStart) {
                String cardsJson = json.substring(deckStart, deckEnd);

                // Parse each card in the deck array
                int cardStart = cardsJson.indexOf("{");
                while (cardStart != -1) {
                    int cardEnd = findMatchingBracket(cardsJson, cardStart);
                    if (cardEnd != -1) {
                        String cardJson = cardsJson.substring(cardStart, cardEnd + 1);
                        StudyCard card = jsonToCard(cardJson);
                        if (card != null) {
                            deckList.add(card);
                        }

                        // Look for next card
                        cardStart = cardsJson.indexOf("{", cardEnd + 1);
                    } else {
                        break; // No more cards found
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
        int qStart = cardJson.indexOf("\"question\": \"") + 13;
        int qEnd = cardJson.indexOf("\"", qStart);
        String question = "Error: Question not found";
        if (qStart > 12 && qEnd > qStart) {
            question = unescapeJsonString(cardJson.substring(qStart, qEnd));
        }

        // Extract answers array
        ArrayList<String> answers = new ArrayList<>();
        int answersStart = cardJson.indexOf("\"answers\": [") + 11; // Changed from "answer" to "answers"
        if (answersStart > 10) {
            int answersEnd = cardJson.indexOf("]", answersStart);
            if (answersEnd > answersStart) {
                String answersJson = cardJson.substring(answersStart, answersEnd);

                // Parse each answer option
                int ansStart = answersJson.indexOf("\"");
                while (ansStart != -1) {
                    int ansEnd = answersJson.indexOf("\"", ansStart + 1);
                    if (ansEnd != -1) {
                        String answer = unescapeJsonString(answersJson.substring(ansStart + 1, ansEnd));
                        answers.add(answer);

                        // Look for next answer
                        ansStart = answersJson.indexOf("\"", ansEnd + 1);
                    } else {
                        break; // No more answers found
                    }
                }
            }
        }

        // Extract solutionId
        int solutionIdStart = cardJson.indexOf("\"solutionId\": ") + 14;
        int solutionIdEnd = cardJson.indexOf("\n", solutionIdStart);
        if (solutionIdEnd == -1) solutionIdEnd = cardJson.length();
        int solutionId = 0;
        if (solutionIdStart > 13 && solutionIdEnd > solutionIdStart) {
            try {
                solutionId = Integer.parseInt(cardJson.substring(solutionIdStart, solutionIdEnd).trim());
            } catch (NumberFormatException e) {
                solutionId = 0; // Default value if parsing fails
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
            char c = json.charAt(i);
            if (c == '{') {
                bracketCount++;
            } else if (c == '}') {
                bracketCount--;
                if (bracketCount == 0) {
                    return i;
                }
            }
        }
        return -1; // Not found
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

    // Unescape special characters from JSON strings
    // @param input The JSON string to unescape.
    // @return The original unescaped string.
    private String unescapeJsonString(String input) {
        if (input == null) return null;
        return input.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}
