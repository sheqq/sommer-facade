package de.sheqq.sommerfacade.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIT {

    @Autowired
    MockMvc mvc;

    private final String base = "/api/v1/users";

    @Test
    @DisplayName("Create + Get Roundtrip")
    void createAndGet() throws Exception {
        String body = """
          {
            "id":"u1",
            "username":"Alice",
            "member":["2025-08-27"],
            "level":3,
            "color":"#112233"
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
                .andExpect(jsonPath("$.username").value("Alice"))
                .andExpect(jsonPath("$.member[0]").value("2025-08-27"))
                .andExpect(jsonPath("$.level").value(3))
                .andExpect(jsonPath("$.color").value("#112233"));
    }

    @Test
    @DisplayName("Update überschreibt Werte")
    void updateUser() throws Exception {
        mvc.perform(post(base)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"u2","username":"Bob","member":["2025-08-27"]}
                  """))
                .andExpect(status().isCreated());

        mvc.perform(put(base + "/u2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {"id":"u2","username":"Bobby","member":["2025-08-27"],"level":9,"color":"#ABCDEF"}
                  """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Bobby"))
                .andExpect(jsonPath("$.member[0]").value("2025-08-27"))
                .andExpect(jsonPath("$.level").value(9))
                .andExpect(jsonPath("$.color").value("#ABCDEF"));
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