package com.waterbucket.chatroom;

import com.waterbucket.chatroom.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ChatRoomApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Test
    void testRegisterAndDeleteUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            userList.add(new User(UUID.randomUUID(), "test" + i, "pass" + i, new ArrayList<>()));
            this.mockMvc.perform(post("http://localhost:" + port + "/users/register", userList.get(i))).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("username").value("test" + 1));
        }
        for (int i = 0; i <= 5; i++) {
            this.mockMvc.perform(delete("users/" + userList.get(i).getId())).andExpect(status().isOk());
        }
    }
}
