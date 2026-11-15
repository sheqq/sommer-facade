package de.sheqq.sommerfacade.infrastructur.service;

import de.sheqq.sommerfacade.infrastructur.model.UserDO;
import de.sheqq.sommerfacade.infrastructur.model.UserEntity;
import de.sheqq.sommerfacade.infrastructur.repository.UserRepository;
import de.sheqq.sommerfacade.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository repo;
    private final UserMapper mapper;

    public UserService(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<UserDO> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    public Optional<UserDO> findById(String id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    public UserDO create(UserDO u) {
        return mapper.toDomain(repo.save(mapper.toPersistence(u)));
    }

    public Optional<UserDO> update(String id, UserDO incoming) {
        return repo.findById(id).map(existing -> {
            UserEntity updated = new UserEntity(existing.getId(), incoming.getUsername());
            return mapper.toDomain(repo.save(updated));
        });
    }

    public boolean delete(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<UserDO> findByUsername(String username) {
        return repo.findByUsername(username).map(mapper::toDomain);
    }
}
