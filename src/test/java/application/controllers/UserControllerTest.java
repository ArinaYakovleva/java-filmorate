package application.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    void noNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"email\":\"user@server.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}