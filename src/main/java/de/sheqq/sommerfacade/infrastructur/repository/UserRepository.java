package de.sheqq.sommerfacade.infrastructur.repository;

import de.sheqq.sommerfacade.infrastructur.model.UserDO;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {
    UserDO save(UserDO userDO);
    Optional<UserDO> findById(String id);
    List<UserDO> findAll();
    Optional<UserDO> findByUsername(String username);
    void deleteById(String id);
    boolean existsById(String id);
}
