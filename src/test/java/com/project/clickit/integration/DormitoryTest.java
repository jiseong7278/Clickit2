package com.project.clickit.integration;

import com.project.clickit.dto.DormitoryDTO;
import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DormitoryTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;
    private final int page = 0;
    private final int size = 10;

    @BeforeEach
    public void setUp() {
        LoginDTO loginDTO = new LoginDTO("clickit_dev", "dev");

        ResponseEntity<TokenDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/login/signIn", loginDTO, TokenDTO.class);

        TokenDTO tokenDTO = response.getBody();
        token = Objects.requireNonNull(tokenDTO).getAccessToken();
        log.info("token: {}", token);
    }

    @Test
    @DisplayName("create Dormitory")
    public void create() {
        DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                .id("test2")
                .name("테스트 기숙사")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DormitoryDTO> entity = new HttpEntity<>(dormitoryDTO, headers);


        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dormitory/create", HttpMethod.POST, entity, String.class);

        log.info("create response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getAll Dormitory")
    public void getAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DormitoryDTO> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dormitory/getAll?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("getAll response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findById")
    public void findById(){
        String id = "test1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<DormitoryDTO> response = restTemplate.exchange("http://localhost:" + port + "/dormitory/findById?id=" + id, HttpMethod.GET, entity, DormitoryDTO.class);

        log.info("findById response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).getId(), id);
    }

    @Test
    @DisplayName("findByName")
    public void findByName(){
        String name = "테스트";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dormitory/findByName?name=" + name + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByName response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateDormitory")
    public void updateDormitory(){
        DormitoryDTO dormitoryDTO = DormitoryDTO.builder()
                .id("test1")
                .name("업데이트 테스트")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DormitoryDTO> entity = new HttpEntity<>(dormitoryDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dormitory/update", HttpMethod.PUT, entity, String.class);

        log.info("updateDormitory response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete")
    public void delete(){
        String id = "test2";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/dormitory/delete?id=" + id, HttpMethod.DELETE, entity, String.class);

        log.info("delete response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
