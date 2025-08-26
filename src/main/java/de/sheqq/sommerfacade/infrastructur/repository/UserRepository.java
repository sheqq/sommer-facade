package de.sheqq.sommerfacade.infrastructur.repository;

import de.sheqq.sommerfacade.infrastructur.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {
    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    void deleteById(String id);
    boolean existsById(String id);
}
