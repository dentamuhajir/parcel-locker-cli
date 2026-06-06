package org.dentamuhajir.parcellocker.infrastructure.memory;

import org.dentamuhajir.parcellocker.domain.model.User;
import org.dentamuhajir.parcellocker.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}