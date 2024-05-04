package com.project.clickit.integration;

import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.SeatDTO;
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

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeatTest {
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
    @DisplayName("duplicateCheck")
    public void duplicateCheck(){
        String id = "bad1_1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/duplicateCheck?id=" + id, HttpMethod.GET, entity, String.class);

        log.info("response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("create")
    public void create(){
        SeatDTO seatDTO = SeatDTO.builder()
                .id("bad1_2")
                .name("테스트 좌석")
                .time(11)
                .isEmpty(true)
                .facilityId("dor_1_badminton")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SeatDTO> entity = new HttpEntity<>(seatDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/seat/create", entity, String.class);

        log.info("create response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("createList")
    public void createList(){
        SeatDTO seatDTO1 = SeatDTO.builder()
                .id("bad1_3")
                .name("테스트 좌석1")
                .time(11)
                .isEmpty(true)
                .facilityId("dor_1_badminton")
                .build();

        SeatDTO seatDTO2 = SeatDTO.builder()
                .id("bad1_4")
                .name("테스트 좌석2")
                .time(11)
                .isEmpty(true)
                .facilityId("dor_1_badminton")
                .build();

        List<SeatDTO> seatDTOList = List.of(seatDTO1, seatDTO2);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<SeatDTO>> entity = new HttpEntity<>(seatDTOList, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/seat/createList", entity, String.class);

        log.info("createList response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getAll")
    public void getAll() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/getAll?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("getAll response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findById")
    public void findById(){
        String id = "bad1_2";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SeatDTO> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/findById?id=" + id, HttpMethod.GET, entity, String.class);

        log.info("findById response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByFacilityId")
    public void findByFacilityId(){
        String facilityId = "dor_1_badminton";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SeatDTO> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/findByFacilityId?facilityId=" + facilityId + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByFacilityId response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("update")
    public void update(){
        SeatDTO seatDTO = SeatDTO.builder()
                .id("bad1_2")
                .name("업데이트 좌석")
                .time(11)
                .isEmpty(true)
                .facilityId("dor_1_badminton")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SeatDTO> entity = new HttpEntity<>(seatDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/update", HttpMethod.PUT, entity, String.class);

        log.info("update response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateSeatFacility")
    public void updateSeatFacility() {
        String id = "bad1_2";
        String facilityId = "dor_2_badminton";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/updateSeatFacility?id=" + id + "&facilityId=" + facilityId, HttpMethod.PUT, entity, String.class);

        log.info("updateSeatFacility response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateIsEmpty")
    public void updateIsEmpty() {
        String id = "bad1_2";
        boolean isEmpty = false;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/updateIsEmpty?id=" + id + "&isEmpty=" + isEmpty, HttpMethod.PUT, entity, String.class);

        log.info("updateIsEmpty response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete")
    public void delete(){
        String id = "bad1_3";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/seat/delete?id=" + id, HttpMethod.DELETE, entity, String.class);

        log.info("delete response: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
