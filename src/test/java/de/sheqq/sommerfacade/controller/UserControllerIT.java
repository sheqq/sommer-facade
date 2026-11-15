package de.sheqq.sommerfacade.controller;

import de.sheqq.sommerfacade.infrastructur.model.UserEntity;
import de.sheqq.sommerfacade.infrastructur.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIT {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserRepository repository;

    private final Map<String, UserEntity> users = new HashMap<>();
    private final String base = "/api/v1/users";

    @BeforeEach
    void setUpRepository() {
        users.clear();
        reset(repository);
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            users.put(user.getId(), user);
            return user;
        });
        when(repository.findById(anyString()))
                .thenAnswer(invocation -> Optional.ofNullable(users.get(invocation.getArgument(0))));
        when(repository.findAll()).thenAnswer(invocation -> new ArrayList<>(users.values()));
        when(repository.existsById(anyString()))
                .thenAnswer(invocation -> users.containsKey(invocation.getArgument(0)));
        when(repository.findByUsername(anyString())).thenAnswer(invocation -> users.values().stream()
                .filter(user -> user.getUsername().equals(invocation.getArgument(0)))
                .findFirst());
        doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            users.remove(id);
            return null;
        }).when(repository).deleteById(anyString());
    }

    @Test
    @DisplayName("Create + Get Roundtrip")
    void createAndGet() throws Exception {
        String body = """
          {
            "id":"u1",
            "username":"Alice"
          }
          """;

        mvc.perform(post(base)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", base + "/u1"));

        mvc.perform(get(base + "/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("u1"))
                .andExpect(jsonPath("$.username").value("Alice"));
    }

    @Test
    @DisplayName("Update überschreibt Werte")
    void updateUser() throws Exception {
        mvc.perform(post(base)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"u2","username":"Bob"}
                  """))
                .andExpect(status().isCreated());

        mvc.perform(put(base + "/u2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"u2","username":"Bobby"}
                  """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Bobby"));
    }

    @Test
    @DisplayName("Liste enthält angelegte Users")
    void listUsers() throws Exception {
        mvc.perform(post(base)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"a","username":"A"}
                  """))
                .andExpect(status().isCreated());
        mvc.perform(post(base)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"b","username":"B"}
                  """))
                .andExpect(status().isCreated());

        mvc.perform(get(base))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Delete entfernt User")
    void deleteUser() throws Exception {
        mvc.perform(post(base)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"del","username":"X"}
                  """))
                .andExpect(status().isCreated());

        mvc.perform(delete(base + "/del"))
                .andExpect(status().isNoContent());

        mvc.perform(get(base + "/del"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get unbekannt -> 404")
    void getNotFound() throws Exception {
        mvc.perform(get(base + "/nope"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update unbekannt -> 404")
    void updateNotFound() throws Exception {
        mvc.perform(put(base + "/ghost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"ghost","username":"Nobody"}
                  """))
                .andExpect(status().isNotFound());
    }
}