package application.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
class FilmControllerTest {
    final String formatString = "{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"duration\":%d," +
            "\"releaseDate\":\"%s\"}";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testName() throws Exception {
        String jsonStr = String.format(formatString, 1, "", "description", 120, "2022-05-05");
        mockMvc.perform(post("/films")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDescription() throws Exception {
        String description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. " +
                "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quaa";
        String jsonStr = String.format(formatString, 1, "name", description, 120, "2022-05-05");

        mockMvc.perform(post("/films")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testReleaseDate() throws Exception {
        String jsonStr = String.format(formatString, 1, "name", "description", 120, "1895-12-27");

        mockMvc.perform(post("/films")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDuration() throws Exception {
        String jsonStr = String.format(formatString, 1, "name", "description", 0, "2000-12-27");

        mockMvc.perform(post("/films")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}