package use_case.Player;

import entity.User;

public interface PlayerDataAccessInterface {

    boolean existsByName(String username);

    User getUser(String username);

    void saveUser(User user);
}

