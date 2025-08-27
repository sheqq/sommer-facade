package de.sheqq.sommerfacade.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.sheqq.sommerfacade.infrastructur.model.User;
import de.sheqq.sommerfacade.infrastructur.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("GET /api/v1/users liefert Liste")
    void getAll() throws Exception {
        User u1 = mk("1","alice");
        User u2 = mk("2","bob");
        given(userService.findAll()).willReturn(List.of(u1, u2));

        mvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("alice"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} vorhanden")
    void getOneFound() throws Exception {
        User u = mk("10","neo");
        given(userService.findById("10")).willReturn(Optional.of(u));

        mvc.perform(get("/api/v1/users/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10"))
                .andExpect(jsonPath("$.username").value("neo"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} nicht gefunden")
    void getOneNotFound() throws Exception {
        given(userService.findById("99")).willReturn(Optional.empty());

        mvc.perform(get("/api/v1/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/users erzeugt 201 und Location")
    void create() throws Exception {
        User incoming = new User();
        incoming.setUsername("newbie");
        incoming.setEmail("n@example.com");

        User saved = mk("42","newbie");
        saved.setEmail("n@example.com");

        given(userService.create(ArgumentMatchers.any(User.class))).willReturn(saved);

        mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/users/42"))
                .andExpect(jsonPath("$.id").value("42"))
                .andExpect(jsonPath("$.username").value("newbie"));
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} aktualisiert vorhanden")
    void updateFound() throws Exception {
        User update = new User();
        update.setUsername("changed");
        update.setEmail("c@example.com");

        User after = mk("5","changed");
        after.setEmail("c@example.com");

        given(userService.update(eq("5"), any(User.class))).willReturn(Optional.of(after));

        mvc.perform(put("/api/v1/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("changed"));
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} 404 wenn nicht vorhanden")
    void updateNotFound() throws Exception {
        User update = new User();
        update.setUsername("x");

        given(userService.update(eq("77"), any(User.class))).willReturn(Optional.empty());

        mvc.perform(put("/api/v1/users/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} liefert 204 bei Erfolg")
    void deleteOk() throws Exception {
        given(userService.delete("3")).willReturn(true);

        mvc.perform(delete("/api/v1/users/3"))
                .andExpect(status().isNoContent());

        verify(userService).delete("3");
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} liefert 404 wenn nicht vorhanden")
    void deleteNotFound() throws Exception {
        given(userService.delete("88")).willReturn(false);

        mvc.perform(delete("/api/v1/users/88"))
                .andExpect(status().isNotFound());
    }

    private User mk(String id, String username) {
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        u.setEmail(username + "@example.com");
        u.setMember(new LocalDateTime[]{ LocalDateTime.now() });
        return u;
    }
}