package de.sheqq.sommerfacade.domain;

import de.sheqq.sommerfacade.infrastructur.model.UserDO;
import de.sheqq.sommerfacade.infrastructur.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class Db implements UserRepository {

    private final Map<String, UserDO> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public UserDO save(UserDO userDO) {
        if (userDO.getId() == null || userDO.getId().isBlank()) {
            throw new IllegalArgumentException("id darf nicht leer sein");
        }
        store.put(userDO.getId(), userDO);
        return userDO;
    }

    @Override
    public Optional<UserDO> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<UserDO> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<UserDO> findByUsername(String username) {
        return store.values().stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst();
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return store.containsKey(id);
    }

}
