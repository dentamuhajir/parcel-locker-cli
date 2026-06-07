package org.dentamuhajir.parcellocker.application;

import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.domain.repository.UserRepository;
import org.dentamuhajir.parcellocker.session.UserSession;

import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private final UserRepository userRepository;
    private final UserSession userSession;

    public AuthService(
            UserRepository userRepository,
            UserSession userSession
    ) {
        this.userRepository = userRepository;
        this.userSession = userSession;
    }

    public User login(String username) {

        User user = userRepository
                .findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User(username);
                    userRepository.save(newUser);
                    return newUser;
                });

        userSession.login(user);

        return user;
    }

    public void logout() {
        userSession.logout();
    }

    public User getCurrentUser() {
        return userSession.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return userSession.isLoggedIn();
    }

    public List<String> consumeNotifications() {

        if (!userSession.isLoggedIn()) {
            return List.of();
        }

        User currentUser =
                userSession.getCurrentUser();

        List<String> notifications =
                new ArrayList<>(
                        currentUser.getNotifications()
                );

        currentUser.clearNotifications();

        return notifications;
    }
}