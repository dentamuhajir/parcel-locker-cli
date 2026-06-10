package org.dentamuhajir.parcellocker.application;

import org.dentamuhajir.parcellocker.domain.model.Locker;
import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryLockerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LockerServiceTest {

    private LockerService lockerService;
    private InMemoryLockerRepository lockerRepository;

    @BeforeEach
    void setUp() {

        lockerRepository =
                new InMemoryLockerRepository();

        lockerService =
                new LockerService(
                        lockerRepository
                );
    }

    @Test
    void addLocker_shouldCreateLocker() {

        lockerService.addLocker("A101");

        assertTrue(
                lockerRepository
                        .findById("A101")
                        .isPresent()
        );
    }

    @Test
    void addLocker_shouldRejectDuplicateLocker() {

        lockerService.addLocker("A101");

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> lockerService.addLocker("A101")
                );

        assertEquals(
                "Locker already exists.",
                exception.getMessage()
        );
    }

    @Test
    void reserveLocker_shouldAssignLocker() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        Locker locker =
                lockerRepository
                        .findById("A101")
                        .orElseThrow();

        assertEquals(
                alice,
                locker.getAssignedUser().orElseThrow()
        );
    }

    @Test
    void reserveLocker_shouldRejectReservedLocker() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        User bob =
                new User("Bob");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> lockerService.reserveLocker(
                                "A101",
                                bob
                        )
                );

        assertEquals(
                "Locker is already reserved.",
                exception.getMessage()
        );
    }

    @Test
    void joinQueue_shouldAddUser() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        User bob =
                new User("Bob");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        lockerService.joinQueue(
                "A101",
                bob
        );

        Locker locker =
                lockerRepository
                        .findById("A101")
                        .orElseThrow();

        assertTrue(
                locker.isUserQueued(bob)
        );
    }

    @Test
    void joinQueue_shouldRejectDuplicateUser() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        User bob =
                new User("Bob");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        lockerService.joinQueue(
                "A101",
                bob
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> lockerService.joinQueue(
                                "A101",
                                bob
                        )
                );

        assertEquals(
                "User is already in queue.",
                exception.getMessage()
        );
    }

    @Test
    void releaseLocker_shouldReleaseLocker() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        lockerService.releaseLocker(
                "A101",
                alice
        );

        Locker locker =
                lockerRepository
                        .findById("A101")
                        .orElseThrow();

        assertTrue(
                locker.isAvailable()
        );
    }

    @Test
    void releaseLocker_shouldRejectNonOwner() {

        lockerService.addLocker("A101");

        User alice =
                new User("Alice");

        User bob =
                new User("Bob");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> lockerService.releaseLocker(
                                "A101",
                                bob
                        )
                );

        assertEquals(
                "You do not own this locker.",
                exception.getMessage()
        );
    }
}