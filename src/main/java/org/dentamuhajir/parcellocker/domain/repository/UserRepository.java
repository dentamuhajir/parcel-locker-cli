package org.dentamuhajir.parcellocker.domain.repository;

import org.dentamuhajir.parcellocker.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    void save(User user);
}