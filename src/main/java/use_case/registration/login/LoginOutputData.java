package use_case.registration.login;

import entity.User;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final String apiKey;
    private final User loggedinUser;

    public LoginOutputData(String username, String apiKey, User loggedinUser) {
        this.username = username;
        this.apiKey = apiKey;
        this.loggedinUser = loggedinUser;
    }

    public String getUsername() {
        return username;
    }

    public String getApiKey() {return apiKey;}

    public User getLoggedinUser() {return loggedinUser;}
}
