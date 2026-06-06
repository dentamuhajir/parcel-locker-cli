package org.dentamuhajir.parcellocker.domain.repository;

import org.dentamuhajir.parcellocker.domain.model.Locker;

import java.util.List;
import java.util.Optional;

public interface LockerRepository {

    void save(Locker locker);

    Optional<Locker> findById(String lockerId);

    List<Locker> findAll();
}