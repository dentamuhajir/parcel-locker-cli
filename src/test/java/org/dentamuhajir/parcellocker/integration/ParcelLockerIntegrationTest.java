package org.dentamuhajir.parcellocker.integration;

import org.dentamuhajir.parcellocker.application.AuthService;
import org.dentamuhajir.parcellocker.application.LockerService;
import org.dentamuhajir.parcellocker.domain.model.Locker;
import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryLockerRepository;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryUserRepository;
import org.dentamuhajir.parcellocker.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParcelLockerIntegrationTest {

    private AuthService authService;
    private LockerService lockerService;
    private InMemoryLockerRepository lockerRepository;

    @BeforeEach
    void setUp() {

        lockerRepository =
                new InMemoryLockerRepository();

        authService =
                new AuthService(
                        new InMemoryUserRepository(),
                        new UserSession()
                );

        lockerService =
                new LockerService(
                        lockerRepository
                );
    }

    @Test
    void parcelFlow_shouldAutoAssignLockerToNextQueuedUser() {

        lockerService.addLocker("A101");

        User alice =
                authService.login("Alice");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        authService.logout();

        User bob =
                authService.login("Bob");

        lockerService.joinQueue(
                "A101",
                bob
        );

        authService.logout();

        authService.login("Alice");

        String assignedUser =
                lockerService.releaseLocker(
                        "A101",
                        alice
                );

        assertEquals(
                "Bob",
                assignedUser
        );

        Locker locker =
                lockerRepository
                        .findById("A101")
                        .orElseThrow();

        assertEquals(
                "Bob",
                locker.getAssignedUser()
                        .orElseThrow()
                        .getUsername()
        );

        assertEquals(
                1,
                bob.getNotifications().size()
        );

        assertTrue(
                bob.getNotifications()
                        .get(0)
                        .contains(
                                "automatically assigned"
                        )
        );
    }

    @Test
    void parcelFlow_shouldAssignUsersInQueueOrder() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        User bob =
                new User("Bob");

        User charlie =
                new User("Charlie");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        lockerService.joinQueue(
                "A101",
                bob
        );

        lockerService.joinQueue(
                "A101",
                charlie
        );

        lockerService.releaseLocker(
                "A101",
                alice
        );

        Locker locker =
                lockerRepository
                        .findById("A101")
                        .orElseThrow();

        assertEquals(
                "Bob",
                locker.getAssignedUser()
                        .orElseThrow()
                        .getUsername()
        );

        assertEquals(
                1,
                locker.getQueuePosition(charlie)
        );
    }

    @Test
    void notification_shouldBeConsumedOnNextLogin() {

        User bob =
                authService.login("Bob");

        bob.addNotification(
                "Locker A101 has been automatically assigned to you."
        );

        List<String> notifications =
                authService.consumeNotifications();

        assertEquals(
                1,
                notifications.size()
        );

        assertTrue(
                notifications.get(0)
                        .contains(
                                "automatically assigned"
                        )
        );

        assertTrue(
                bob.getNotifications().isEmpty()
        );
    }
}