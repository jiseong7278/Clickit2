package com.project.clickit.integration;

import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.NoticeDTO;
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

import java.time.LocalDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoticeTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;
    private final int page = 0;
    private final int size = 10;

    @BeforeEach
    public void setUp() {
        LoginDTO loginDTO = new LoginDTO("clickit_dev", "dev1234");

        ResponseEntity<TokenDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/login/signIn", loginDTO, TokenDTO.class);

        TokenDTO tokenDTO = response.getBody();
        token = Objects.requireNonNull(tokenDTO).getAccessToken();
        log.info("token: {}", token);
    }

    @Test
    @DisplayName("Create")
    public void create(){
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .title("통합 테스트 생성3")
                .content("생성 기능 동작 테스트3")
                .date(LocalDateTime.now())
                .memberId("clickit_dev")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NoticeDTO> entity = new HttpEntity<>(noticeDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/notice/create", entity, String.class);

        log.info("create response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getAll Test")
    public void getAll(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/notice/getAll?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("getAll response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByNoticeNum Test")
    public void findByNoticeNum(){
        int num = 11;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/notice/findByNoticeNum?num=" + num, HttpMethod.GET, entity, String.class);

        log.info("findByNoticeNum response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByWriterId Test")
    public void findByWriterId() {
        String id = "clickit_dev";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/notice/findByWriterId?writerId=" + id + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByWriterId response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("update Test")
    public void update(){
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .num(11)
                .title("통합 테스트 수정")
                .content("수정 기능 동작 테스트")
                .date(LocalDateTime.now())
                .memberId("clickit_dev")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NoticeDTO> entity = new HttpEntity<>(noticeDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/notice/update", HttpMethod.PUT, entity, String.class);

        log.info("update response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete Test")
    public void delete() {
        int num = 12;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/notice/delete?num=" + num, HttpMethod.DELETE, entity, String.class);

        log.info("delete response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
