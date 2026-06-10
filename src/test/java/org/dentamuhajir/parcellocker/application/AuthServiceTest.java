package org.dentamuhajir.parcellocker.application;

import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryUserRepository;
import org.dentamuhajir.parcellocker.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;
    private InMemoryUserRepository userRepository;
    private UserSession userSession;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        userSession = new UserSession();

        authService = new AuthService(
                userRepository,
                userSession
        );
    }

    @Test
    void login_shouldCreateUserIfNotExists() {

        User user = authService.login("Alice");

        assertEquals("Alice", user.getUsername());
        assertTrue(authService.isLoggedIn());
        assertEquals(user, authService.getCurrentUser());
    }

    @Test
    void login_shouldReuseExistingUser() {

        User firstLogin = authService.login("Alice");

        authService.logout();

        User secondLogin = authService.login("Alice");

        assertSame(firstLogin, secondLogin);
    }

    @Test
    void logout_shouldClearSession() {

        authService.login("Alice");

        authService.logout();

        assertFalse(authService.isLoggedIn());
        assertNull(authService.getCurrentUser());
    }

    @Test
    void consumeNotifications_shouldReturnAndClearNotifications() {

        User user = authService.login("Alice");

        user.addNotification("Locker A101 assigned");
        user.addNotification("Locker A102 assigned");

        List<String> notifications =
                authService.consumeNotifications();

        assertEquals(2, notifications.size());

        assertTrue(
                notifications.contains(
                        "Locker A101 assigned"
                )
        );

        assertTrue(
                notifications.contains(
                        "Locker A102 assigned"
                )
        );

        assertTrue(
                user.getNotifications().isEmpty()
        );
    }

    @Test
    void consumeNotifications_shouldReturnEmptyListWhenNotLoggedIn() {

        List<String> notifications =
                authService.consumeNotifications();

        assertTrue(notifications.isEmpty());
    }
}