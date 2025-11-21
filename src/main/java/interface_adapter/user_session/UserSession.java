package interface_adapter.user_session;

import entity.User;

public class UserSession {
    private User currentSessionUser;
    private String apiKey;

    public void setUser(User user) { currentSessionUser = user; }
    public User getUser() { return currentSessionUser; }

    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiKey() { return apiKey; }
}
