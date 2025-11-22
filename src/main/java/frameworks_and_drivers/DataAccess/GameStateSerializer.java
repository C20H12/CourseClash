package frameworks_and_drivers.DataAccess;

import entity.MultiPlayerGame;
import entity.User;
import entity.DeckManagement.StudyDeck;
import entity.UserFactory;
import org.json.JSONObject;

public class GameStateSerializer {

    public static String serialize(MultiPlayerGame game) {
        JSONObject json = new JSONObject();

        // Players
        json.put("playerA", game.getPlayerA().getUserName());
        json.put("playerB", game.getPlayerB().getUserName());

        // Score & Turn
        json.put("scoreA", game.getScoreA());
        json.put("scoreB", game.getScoreB());
        json.put("currentTurn", game.getCurrentTurn().getUserName());

        // Card Progress & Sync Data
        json.put("cardIndex", game.getCardIndex());
        json.put("answersSubmitted", game.getCurrentCardAnswersSubmitted()); // CRITICAL FIX

        // Deck Info
        json.put("deckName", game.getDeck().getTitle());

        return json.toString();
    }

    public static MultiPlayerGame deserialize(String jsonString, UserFactory userFactory, StudyDeck deck) {
        JSONObject json = new JSONObject(jsonString);

        // Reconstruct Users (Placeholders)
        String nameA = json.getString("playerA");
        String nameB = json.getString("playerB");
        User playerA = userFactory.create(nameA, "remote_user");
        User playerB = userFactory.create(nameB, "remote_user");

        MultiPlayerGame game = new MultiPlayerGame(playerA, playerB, deck);

        // Restore State
        game.setScoreA(json.getInt("scoreA"));
        game.setScoreB(json.getInt("scoreB"));
        game.setCardIndex(json.getInt("cardIndex"));

        // CRITICAL FIX: Restore the counter so game doesn't reset logic
        game.setCurrentCardAnswersSubmitted(json.optInt("answersSubmitted", 0));

        String turnName = json.getString("currentTurn");
        if (turnName.equals(nameB)) {
            game.setCurrentTurn(playerB);
        } else {
            game.setCurrentTurn(playerA);
        }

        return game;
    }
}