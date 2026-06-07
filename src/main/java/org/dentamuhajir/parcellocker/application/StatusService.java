package org.dentamuhajir.parcellocker.application;


import org.dentamuhajir.parcellocker.domain.model.Locker;
import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.domain.repository.LockerRepository;

import java.util.ArrayList;
import java.util.List;

public class StatusService {

    private final LockerRepository lockerRepository;

    public StatusService(
            LockerRepository lockerRepository
    ) {
        this.lockerRepository = lockerRepository;
    }

    public List<String> getAssignedLockers(User user) {

        List<String> assignedLockers =
                new ArrayList<>();

        for (Locker locker :
                lockerRepository.findAll()) {

            locker.getAssignedUser()
                    .ifPresent(assignedUser -> {

                        if (assignedUser.equals(user)) {

                            assignedLockers.add(
                                    locker.getLockerId()
                            );
                        }
                    });
        }

        return assignedLockers;
    }

    public List<String> getQueuePositions(
            User user
    ) {

        List<String> queuePositions =
                new ArrayList<>();

        for (Locker locker :
                lockerRepository.findAll()) {

            int position =
                    locker.getQueuePosition(user);

            if (position > 0) {

                queuePositions.add(
                        locker.getLockerId()
                                + " -> position "
                                + position
                );
            }
        }

        return queuePositions;
    }
}