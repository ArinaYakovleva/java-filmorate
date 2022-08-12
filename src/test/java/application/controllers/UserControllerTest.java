package application.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    final String formatString = "{\"id\":%d,\"email\":\"%s\",\"login\":\"%s\",\"name\":\"%s\"," +
            "\"birthday\":\"%s\"}";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEmail() throws Exception {
        String jsonStr = String.format(formatString, 1, "", "login", "name", "2022-05-05");
        performBadRequest(jsonStr);
    }

    @Test
    public void loginTest() throws Exception {
        String jsonStr = String.format(formatString, 1, "mail@mail.ru", "", "name", "2022-05-05");
        performBadRequest(jsonStr);
    }

    @Test
    public void nameTest() throws Exception {
        String jsonStr = "{\"id\":1,\"email\":\"mail@mail.ru\",\"login\":\"login\",\"name\":null," +
                "\"birthday\":\"2000-05-03\"}";
        performOKRequest(jsonStr);
    }

    @Test
    public void birthdayTest() throws Exception {
        String jsonStr = String.format(formatString, 1, "a@mail.ru", "login", "name", "2022-10-30");
        performBadRequest(jsonStr);
    }

    private void performBadRequest(String jsonStr) throws Exception {
        mockMvc.perform(post("/users")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private void performOKRequest(String jsonStr) throws Exception {
        mockMvc.perform(post("/users")
                .content(jsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
