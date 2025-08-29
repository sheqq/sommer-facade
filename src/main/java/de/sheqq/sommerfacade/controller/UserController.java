package de.sheqq.sommerfacade.controller;

import de.sheqq.sommerfacade.api.UsersApi;
import de.sheqq.sommerfacade.dto.User;
import de.sheqq.sommerfacade.infrastructur.model.UserDO;
import de.sheqq.sommerfacade.infrastructur.service.UserService;
import de.sheqq.sommerfacade.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController implements UsersApi {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        // Erwartet: Client liefert id (Spec)
        if (user.getId() == null || user.getId().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        UserDO saved = service.create(mapper.toEntity(user));
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + saved.getId()))
                .body(mapper.toDto(saved));
    }

    @Override
    public ResponseEntity<Void> deleteUser(String userId) {
        return service.delete(userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = service.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<User> getUserById(String userId) {
        return service.findById(userId)
                .map(u -> ResponseEntity.ok(mapper.toDto(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<User> updateUser(String userId, User user) {
        // Optional: ID-Konsistenz prüfen
        if (user.getId() == null || user.getId().isBlank()) {
            // Body ohne id -> aus Pfad setzen
            user.setId(userId);
        } else if (!userId.equals(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        UserDO incoming = mapper.toEntity(user);
        return service.update(userId, incoming)
                .map(updated -> ResponseEntity.ok(mapper.toDto(updated)))
                .orElse(ResponseEntity.notFound().build());
    }
}