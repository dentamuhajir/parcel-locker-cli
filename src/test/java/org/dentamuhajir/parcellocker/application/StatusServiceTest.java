package org.dentamuhajir.parcellocker.application;

import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.infrastructure.memory.InMemoryLockerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusServiceTest {

    private LockerService lockerService;
    private StatusService statusService;

    @BeforeEach
    void setUp() {

        InMemoryLockerRepository lockerRepository =
                new InMemoryLockerRepository();

        lockerService =
                new LockerService(lockerRepository);

        statusService =
                new StatusService(lockerRepository);
    }

    @Test
    void getAssignedLockers_shouldReturnAssignedLockers() {

        User alice =
                new User("Alice");

        lockerService.addLocker("A101");
        lockerService.addLocker("A102");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        List<String> assignedLockers =
                statusService.getAssignedLockers(
                        alice
                );

        assertEquals(
                1,
                assignedLockers.size()
        );

        assertTrue(
                assignedLockers.contains("A101")
        );
    }

    @Test
    void getQueuePositions_shouldReturnQueuePositions() {

        User alice =
                new User("Alice");

        User bob =
                new User("Bob");

        lockerService.addLocker("A101");

        lockerService.reserveLocker(
                "A101",
                alice
        );

        lockerService.joinQueue(
                "A101",
                bob
        );

        List<String> positions =
                statusService.getQueuePositions(
                        bob
                );

        assertEquals(
                1,
                positions.size()
        );

        assertEquals(
                "A101 -> position 1",
                positions.get(0)
        );
    }
}