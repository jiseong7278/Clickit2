package com.project.clickit.integration;

import com.project.clickit.dto.FacilityDTO;
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

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacilityTest {
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
        FacilityDTO facilityDTO = FacilityDTO.builder()
                .id("dor_1_test11")
                .name("생성 테스트")
                .info("테스트 생성 시설 정보")
                .open(6)
                .close(22)
                .capacity(100)
                .terms("테스트 생성 시설 약관")
                .dormitoryId("dor_1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FacilityDTO> entity = new HttpEntity<>(facilityDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/facility/create", entity, String.class);

        log.info("create response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getAll")
    public void getAll(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/getAll?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("getAll response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findById")
    public void findById(){
        String id = "dor_1_test";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FacilityDTO> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/findById?id=" + id, HttpMethod.GET, entity, String.class);

        log.info("findById response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByName")
    public void findByName(){
        String name = "테스트";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/findByName?name=" + name + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByName response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByDormitoryId")
    public void findByDormitoryId(){
        String dormitoryId = "dor_1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/findByDormitoryId?dormitoryId=" + dormitoryId + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByDormitoryId response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("update")
    public void update(){
        FacilityDTO facilityDTO = FacilityDTO.builder()
                .id("dor_1_test")
                .name("업데이트 테스트")
                .info("테스트 업데이트 시설 정보")
                .open(7)
                .close(23)
                .capacity(200)
                .terms("테스트 업데이트 시설 약관")
                .dormitoryId("dor_1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FacilityDTO> entity = new HttpEntity<>(facilityDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/update", HttpMethod.PUT, entity, String.class);

        log.info("update response: {}", response.getBody());
    }

    @Test
    @DisplayName("updateFacilityId")
    public void updateFacilityId(){
        String id = "dor_1_test";
        String newId = "dor_1_test11";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/updateFacilityId?id=" + id + "&newId=" + newId, HttpMethod.PUT, entity, String.class);

        log.info("updateFacilityId response: {}", response.getBody());
    }

    @Test
    @DisplayName("delete")
    public void delete(){
        String id = "dor_1_test11";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/facility/delete?id=" + id, HttpMethod.DELETE, entity, String.class);

        log.info("delete response: {}", response.getBody());
    }
}
