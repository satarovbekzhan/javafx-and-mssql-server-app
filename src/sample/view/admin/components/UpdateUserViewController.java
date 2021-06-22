package sample.view.admin.components;

import sample.model.User;

public class UpdateUserViewController extends AdminComponentController {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }
}
