package org.dentamuhajir.parcellocker.session;

import org.dentamuhajir.parcellocker.domain.model.User;

public class UserSession {

    private User currentUser;

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }
}