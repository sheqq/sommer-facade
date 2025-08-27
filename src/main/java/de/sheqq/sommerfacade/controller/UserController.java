package de.sheqq.sommerfacade.controller;


import de.sheqq.sommerfacade.api.UsersApi;
import de.sheqq.sommerfacade.dto.CreateUserRequest;
import de.sheqq.sommerfacade.dto.UpdateUserRequest;
import de.sheqq.sommerfacade.infrastructur.model.User;
import de.sheqq.sommerfacade.infrastructur.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UsersApi {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

//    @GetMapping
//    public ResponseEntity<List<User>> getAll() {
//        return ResponseEntity.ok(service.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getOne(@PathVariable String id) {
//        return service.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<User> create(@Valid @RequestBody User user) {
//        User saved = service.create(user);
//        return ResponseEntity
//                .created(URI.create("/api/v1/users/" + saved.getId()))
//                .body(saved);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> update(@PathVariable String id,
//                                       @Valid @RequestBody User user) {
//        return service.update(id, user)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable String id) {
//        boolean deleted = service.delete(id);
//        return deleted ? ResponseEntity.noContent().build()
//                : ResponseEntity.notFound().build();
//    }

    @Override
    public ResponseEntity<de.sheqq.sommerfacade.dto.User> createUser(CreateUserRequest user) {
        User saved = service.create(user);
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + saved.getId()))
                .body(saved);
    }

    @Override
    public ResponseEntity<Void> deleteUser(String userId) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<de.sheqq.sommerfacade.dto.User>> getAllUsers() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<de.sheqq.sommerfacade.dto.User> getUserById(String userId) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<de.sheqq.sommerfacade.dto.User> updateUser(String userId, UpdateUserRequest user) {
        return service.update(user.getId(), user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
