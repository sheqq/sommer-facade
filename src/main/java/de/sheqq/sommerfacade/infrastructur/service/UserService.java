package de.sheqq.sommerfacade.infrastructur.service;

import de.sheqq.sommerfacade.infrastructur.model.UserDO;
import de.sheqq.sommerfacade.infrastructur.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<UserDO> findAll() {
        return repo.findAll();
    }

    public Optional<UserDO> findById(String id) {
        return repo.findById(id);
    }

    public UserDO create(UserDO u) {
        return repo.save(u);
    }

    public Optional<UserDO> update(String id, UserDO incoming) {
        return repo.findById(id).map(existing -> {
            existing.setUsername(incoming.getUsername());
            existing.setMember(incoming.getMember());
            existing.setLevel(incoming.getLevel());
            existing.setColor(incoming.getColor());
            return repo.save(existing);
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
        return repo.findByUsername(username);
    }
}
