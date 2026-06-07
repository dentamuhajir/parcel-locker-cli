package org.dentamuhajir.parcellocker.application;


import org.dentamuhajir.parcellocker.domain.model.Locker;
import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.domain.repository.LockerRepository;

import java.util.List;

public class LockerService {

    private final LockerRepository lockerRepository;

    public LockerService(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    public void addLocker(String lockerId) {

        if (lockerRepository.findById(lockerId).isPresent()) {
            throw new IllegalArgumentException(
                    "Locker already exists."
            );
        }

        lockerRepository.save(
                new Locker(lockerId)
        );
    }

    public List<Locker> getAllLockers() {
        return lockerRepository.findAll();
    }

    public void reserveLocker(String lockerId, User user) {

        Locker locker = lockerRepository
                .findById(lockerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Locker not found."
                        )
                );

        if (!locker.isAvailable()) {
            throw new IllegalArgumentException(
                    "Locker is already reserved."
            );
        }

        locker.assign(user);
    }

    public void joinQueue(String lockerId, User user) {

        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Locker not found."
                        )
                );

        if (locker.isAvailable()) {
            throw new IllegalArgumentException(
                    "Locker is available. Reserve it directly."
            );
        }

        locker.addToQueue(user);
    }

    public String releaseLocker(String lockerId, User currentUser) {

        Locker locker = lockerRepository
                .findById(lockerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Locker not found."
                        )
                );

        if (locker.isAvailable()) {
            throw new IllegalArgumentException(
                    "Locker is already available."
            );
        }

        User assignedUser = locker
                .getAssignedUser()
                .orElseThrow();

        if (!assignedUser.equals(currentUser)) {
            throw new IllegalArgumentException(
                    "You do not own this locker."
            );
        }

        locker.release();

        User nextUser =
                locker.pollNextQueuedUser();

        if (nextUser != null) {

            locker.assign(nextUser);

            return nextUser.getUsername();
        }

        return null;
    }
}