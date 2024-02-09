package com.waterbucket.chatroom;

import com.waterbucket.chatroom.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
class ChatRoomApplicationTests {
    private final List<UserDTO> userDTOList = new ArrayList<>();
    private final List<UserDTO> getUserDTOS = new ArrayList<>();
    @LocalServerPort
    private int port;

    @Test
    void getAllUsers() {
        String uri = "http://127.0.0.1:" + port;
        Flux<UserDTO> userDTOFlux = WebClient.create().get().uri(uri + "/api/users").retrieve().bodyToFlux(UserDTO.class);
        userDTOFlux.subscribe(userDTO -> {
            log.info("Got user: {}", userDTO.getId());
            getUserDTOS.add(userDTO);
        });
    }

    @Test
    void testRegisterUsers() throws Exception {
        String uri = "http://127.0.0.1:" + port;
        for (int i = 0; i <= 5; i++) {
            userDTOList.add(new UserDTO(UUID.randomUUID(), "test" + i, "pass" + i, new ArrayList<>()));
            var userMono = WebClient.create().post().uri(uri + "/api/users/register")
                    .body(Mono.just(userDTOList.get(i)), UserDTO.class).exchangeToMono(response -> {
                        //noinspection ResultOfMethodCallIgnored
                        assertThat(response.statusCode().equals(HttpStatus.OK));
                        return response.bodyToMono(UserDTO.class);
                    });
            int finalI = i;
            assertThat(userMono.mapNotNull(userDTO -> userDTO.equals(userDTOList.get(finalI))).block()).isTrue();
        }
    }
}

