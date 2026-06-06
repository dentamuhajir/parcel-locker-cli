package org.dentamuhajir.parcellocker.infrastructure.memory;

import org.dentamuhajir.parcellocker.domain.model.Locker;
import org.dentamuhajir.parcellocker.domain.repository.LockerRepository;

import java.util.*;

public class InMemoryLockerRepository
        implements LockerRepository {

    private final Map<String, Locker> lockers =
            new HashMap<>();

    @Override
    public void save(Locker locker) {
        lockers.put(locker.getLockerId(), locker);
    }

    @Override
    public Optional<Locker> findById(String lockerId) {
        return Optional.ofNullable(lockers.get(lockerId));
    }

    @Override
    public List<Locker> findAll() {
        return new ArrayList<>(lockers.values());
    }
}