//Mahir
package use_case.MultiPlayer.start_match;

public class MPStartInputData {
    private final String playerAName;
    private final String playerBName;
    private final String deckName;

    public MPStartInputData(String playerAName, String playerBName, String deckName) {
        this.playerAName = playerAName;
        this.playerBName = playerBName;
        this.deckName = deckName;
    }

    public String getPlayerAName() { return playerAName; }
    public String getPlayerBName() { return playerBName; }
    public String getDeckName() { return deckName; }
}
