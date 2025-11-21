//Mahir
package use_case.MultiPlayer.submit_answer;

public class SubmitAnswerInputData {
    private final String answer;
    private final String username;

    public SubmitAnswerInputData(String answer, String username) {
        this.answer = answer;
        this.username = username;
    }

    public String getAnswer() {
        return answer;
    }

    public String getUsername() {
        return username;
    }
}