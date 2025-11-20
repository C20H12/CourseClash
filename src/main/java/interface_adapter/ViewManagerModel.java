package interface_adapter;

import entity.User;

public class ViewManagerModel extends ViewModel<String> {

    private User currentUser;

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

}