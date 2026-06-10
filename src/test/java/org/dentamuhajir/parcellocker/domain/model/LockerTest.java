package org.dentamuhajir.parcellocker.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LockerTest {

    @Test
    void addToQueue_shouldRejectDuplicateUser() {

        Locker locker =
                new Locker("A101");

        User bob =
                new User("Bob");

        locker.addToQueue(bob);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> locker.addToQueue(bob)
                );

        assertEquals(
                "User is already in queue.",
                exception.getMessage()
        );
    }

    @Test
    void pollNextQueuedUser_shouldReturnFirstQueuedUser() {

        Locker locker =
                new Locker("A101");

        User bob =
                new User("Bob");

        User charlie =
                new User("Charlie");

        locker.addToQueue(bob);
        locker.addToQueue(charlie);

        User nextUser =
                locker.pollNextQueuedUser();

        assertEquals(
                bob,
                nextUser
        );
    }

    @Test
    void getQueuePosition_shouldReturnCorrectPosition() {

        Locker locker =
                new Locker("A101");

        User bob =
                new User("Bob");

        User charlie =
                new User("Charlie");

        locker.addToQueue(bob);
        locker.addToQueue(charlie);

        assertEquals(
                1,
                locker.getQueuePosition(bob)
        );

        assertEquals(
                2,
                locker.getQueuePosition(charlie)
        );
    }

    @Test
    void assignAndRelease_shouldUpdateAvailability() {

        Locker locker =
                new Locker("A101");

        User alice =
                new User("Alice");

        assertTrue(
                locker.isAvailable()
        );

        locker.assign(alice);

        assertFalse(
                locker.isAvailable()
        );

        locker.release();

        assertTrue(
                locker.isAvailable()
        );
    }
}