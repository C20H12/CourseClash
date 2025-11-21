package frameworks_and_drivers.DataAccess;

import entity.MultiPlayerGame;
import use_case.MultiPlayer.MultiPlayerAccessInterface;
import use_case.MultiPlayer.submit_answer.SubmitAnswerDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryGameDataAccessObject implements MultiPlayerAccessInterface, SubmitAnswerDataAccessInterface {
    private final Map<String, MultiPlayerGame> games = new HashMap<>();

    @Override
    public void save(MultiPlayerGame game) {
        games.put(game.getPlayerA().getUserName(), game);
        games.put(game.getPlayerB().getUserName(), game);
    }

    @Override
    public MultiPlayerGame getMultiPlayerGame(String username) {
        return games.get(username);
    }
}
