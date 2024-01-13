package com.waterbucket.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterbucket.chatroom.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
class ChatRoomApplicationTests {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() throws Exception {
        this.mockMvc.perform(get("/api/users")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testRegisterUsers() throws Exception {
        List<UserDTO> userDTOList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i <= 5; i++) {
            userDTOList.add(new UserDTO(UUID.randomUUID(), "test" + i, "pass" + i, new ArrayList<>()));
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDTOList.get(i)))).andExpect(status().isOk());
        }
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTOList.get(3)))).andExpect(status().isBadRequest());
    }

    @Test
    void testGetUsers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        //noinspection unchecked
        ArrayList<LinkedHashMap<String, String>> userDTOList = restTemplate.getForObject("http://localhost:" + port + "/api/users", ArrayList.class);
        assertThat(userDTOList).isNotNull();
        mockMvc.perform(get("/api/users/" + userDTOList.get(3).get("id")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDTOList.get(3))));
        mockMvc.perform(get("/api/users?name=" + userDTOList.get(3).get("username")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDTOList.get(3))));
        mockMvc.perform(get("/api/users/" + UUID.randomUUID())).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/users?name=" + RandomString.make())).andExpect(status().isNotFound());
    }

    @Test
    void deleteAllUsers() throws Exception {
        //noinspection unchecked
        ArrayList<LinkedHashMap<String, String>> userDTOList = restTemplate.getForObject("http://localhost:" + port + "/api/users", ArrayList.class);
        assertThat(userDTOList).isNotNull();
        for (LinkedHashMap<String, String> stringStringLinkedHashMap : userDTOList) {
            mockMvc.perform(delete("/api/users/" + stringStringLinkedHashMap.get("id"))).andExpect(status().isNoContent());
        }

    }

}

