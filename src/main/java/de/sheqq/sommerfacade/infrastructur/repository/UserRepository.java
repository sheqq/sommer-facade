package de.sheqq.sommerfacade.infrastructur.repository;

import de.sheqq.sommerfacade.infrastructur.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}
