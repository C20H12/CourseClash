package use_case.MultiPlayer.start_match;

public class MPStartInputData {
    private final String playerAName;
    private final String playerBName;
    private final String deckId;

    public MPStartInputData(String playerAName, String playerBName, String deckId) {
        this.playerAName = playerAName;
        this.playerBName = playerBName;
        this.deckId = deckId;
    }

    public String getPlayerAName() { return playerAName; }
    public String getPlayerBName() { return playerBName; }
    public String getDeckId() { return deckId; }
}
