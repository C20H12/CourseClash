//Mahir
package use_case.MultiPlayer.start_match;

import entity.DeckManagement.StudyCard;

public class MPStartOutputData {
    private final String player1;
    private final String player2;
    private final StudyCard firstCard;

    public MPStartOutputData(String player1, String player2, StudyCard firstCard) {
        this.player1 = player1;
        this.player2 = player2;
        this.firstCard = firstCard;
    }

    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public StudyCard getFirstCard() { return firstCard; }
}