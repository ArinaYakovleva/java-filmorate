package application.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    final String formatString = "{\"id\":%d,\"email\":\"%s\",\"login\":\"%s\",\"name\":\"%s\"," +
            "\"birthday\":\"%s\"}";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEmail() throws Exception {
        String jsonStr = String.format(formatString, 1, "", "login", "name", "2022-05-05");
        mockMvc.perform(post("/users")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTest() throws Exception {
        String jsonStr = String.format(formatString, 1, "mail@mail.ru", "", "name", "2022-05-05");
        mockMvc.perform(post("/users")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void nameTest() throws Exception {
        mockMvc.perform(post("/users")
                .content("{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"login\",\"name\":null," +
                        "\"birthday\":\"2000-05-03\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void birthdayTest() throws Exception {
        String jsonStr = String.format(formatString, 1, "a@mail.ru", "login", "name", "2022-07-30");
        mockMvc.perform(post("/users")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}