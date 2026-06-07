package org.dentamuhajir.parcellocker.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private final String username;

    private final List<String> notifications = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void clearNotifications() {
        notifications.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}