package com.example.demo.integration;

import com.example.demo.DemoApplication;
import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc
@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_get_all_users() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(2)));
    }

    @Test
    public void should_get_a_user_by_id() throws Exception {
        mockMvc.perform(get("/users/user/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(100)))
                .andExpect(jsonPath("$.firstName", equalTo("waleed")))
                .andExpect(jsonPath("$.lastName", equalTo("ehsan")))
                .andExpect(jsonPath("$.email", equalTo("waleed.ehsan@gmail.com")));
    }

    @Test
    public void should_throw_exception_when_getting_a_user_that_does_not_exist() throws Exception {
        String id = "9999999999999";
        mockMvc.perform(get("/users/user/".concat(id)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),"No such value exists with id: ".concat(id)));
    }

    @Test
    public void should_create_a_user() throws Exception{
        mockMvc.perform(post("/users/user")
                .contentType("application/json")
                .content(asJsonString(new User(1L, "john","ehsan","john.ehsan@gmail.com"))))
                .andExpect(status().isCreated());
    }

    @Test
    @Disabled
    public void should_throw_exception_when_creating_a_user_with_email_that_already_exists() throws Exception{
//        User user = new User(1L, "waleed","ehsan","waleed.ehsan@gmail.com");
        mockMvc.perform(post("/users/user")
                        .contentType("application/json")
                        .content(asJsonString(new User(1L, "waleed","ehsan","waleed.ehsan@gmail.com"))))
                .andExpect(status().isConflict())
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),"A user with email: waleed.ehsan@gmail.com already exists"));
    }

    @Test
    public void should_delete_a_user_by_id() throws Exception {
        mockMvc.perform(delete("/users/user/100"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_exception_when_deleting_a_user_that_does_not_exist() throws Exception {
        String id = "9999999999999";
        mockMvc.perform(delete("/users/user/".concat(id)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(),"No such value exists with id: ".concat(id)));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
