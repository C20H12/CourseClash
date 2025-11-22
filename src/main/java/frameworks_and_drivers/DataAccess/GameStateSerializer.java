package frameworks_and_drivers.DataAccess;


import entity.MultiPlayerGame;
import entity.User;
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;
import entity.UserFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameStateSerializer {

    public static String serialize(MultiPlayerGame game) {
        JSONObject json = new JSONObject();

        // Users
        json.put("playerA", game.getPlayerA().getUserName());
        json.put("playerB", game.getPlayerB().getUserName());

        // Game State
        json.put("scoreA", game.getScoreA());
        json.put("scoreB", game.getScoreB());
        json.put("currentTurn", game.getCurrentTurn().getUserName());
        json.put("cardIndex", getCurrentCardIndex(game)); // Helper needed in Game entity or access field

        // Deck Info (We just send the ID/Name, assuming both players have the deck loaded)
        json.put("deckName", game.getDeck().getTitle()); // Or ID

        return json.toString();
    }

    public static MultiPlayerGame deserialize(String jsonString, UserFactory userFactory, StudyDeck deck) {
        JSONObject json = new JSONObject(jsonString);

        String nameA = json.getString("playerA");
        String nameB = json.getString("playerB");
        User playerA = userFactory.create(nameA, "placeholder");
        User playerB = userFactory.create(nameB, "placeholder");

        MultiPlayerGame game = new MultiPlayerGame(playerA, playerB, deck);

        game.setScoreA(json.getInt("scoreA"));
        game.setScoreB(json.getInt("scoreB"));

        // Restore Turn
        String turnName = json.getString("currentTurn");
        if (turnName.equals(nameB)) {
            game.setCurrentTurn(playerB);
        } else {
            game.setCurrentTurn(playerA);
        }

        // Restore Index (You need to add setCurrentCardIndex to MultiPlayerGame entity)
        // game.setCurrentCardIndex(json.getInt("cardIndex"));

        return game;
    }

    // Helper to access private index field via getter if available,
    // otherwise you might need to add a getter to MultiPlayerGame
    private static int getCurrentCardIndex(MultiPlayerGame game) {
        // Assuming you have a getter. If not, you must add: public int getCardIndex() { return currentCardIndex; }
        // For now returning 0 to prevent compile error until you update Entity.
        return 0;
    }
}
