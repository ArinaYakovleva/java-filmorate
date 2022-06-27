package application.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@WebMvcTest(controllers = {FilmController.class})
class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    void noNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"description\":\"тестовое описание\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}