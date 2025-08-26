package de.sheqq.sommerfacade.infrastructur.service;

import de.sheqq.sommerfacade.infrastructur.model.User;
import de.sheqq.sommerfacade.infrastructur.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public Optional<User> findById(String id) {
        return repo.findById(id);
    }

    public User create(User u) {
        u.setId(null);
        return repo.save(u);
    }

    public Optional<User> update(String id, User incoming) {
        return repo.findById(id).map(existing -> {
            existing.setUsername(incoming.getUsername());
            existing.setEmail(incoming.getEmail());
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

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}
