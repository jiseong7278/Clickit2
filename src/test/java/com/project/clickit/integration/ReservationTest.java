//package com.project.clickit.integration;
//
//import com.project.clickit.dto.LoginDTO;
//import com.project.clickit.dto.ReservationDTO;
//import com.project.clickit.dto.TokenDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Objects;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Slf4j
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ReservationTest {
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private String token;
//    private final int page = 0;
//    private final int size = 10;
//
//    @BeforeEach
//    public void setUp() {
//        LoginDTO loginDTO = new LoginDTO("clickit_dev", "dev1234");
//
//        ResponseEntity<TokenDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/login/signIn", loginDTO, TokenDTO.class);
//
//        TokenDTO tokenDTO = response.getBody();
//        token = Objects.requireNonNull(tokenDTO).getAccessToken();
//        log.info("token: {}", token);
//    }
//
//    @Test
//    @DisplayName("create")
//    public void create(){
//        ReservationDTO reservationDTO = ReservationDTO.builder()
//                .seatId("bad1_2")
//                .memberId("clickit_dev")
//                .timestamp(LocalDateTime.now())
//                .status("예약")
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<ReservationDTO> entity = new HttpEntity<>(reservationDTO, headers);
//
//        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/reservation/create", entity, Object.class);
//
//        log.info("create response: {}", response);
//
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("getAll")
//    public void getAll() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/getAll?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);
//
//        log.info("getAll response: {}", response);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("findByMemberId")
//    public void findByMemberId() {
//        String memberId = "clickit_dev";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/findByMemberId?memberId=" + memberId + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);
//
//        log.info("\n\nfindByMemberId response.getBody(): {}\n\n", response.getBody());
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("findMyReservation")
//    public void findMyReservation() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/findMyReservation?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);
//
//        log.info("\n\nfindMyReservation response.getBody(): {}\n\n", response.getBody());
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("findBySeatIdAndToday")
//    public void findBySeatIdAndToday() {
//        String seatId = "bad1_1";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/findBySeatIdAndToday?seatId=" + seatId + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);
//
//        log.info("\n\nfindBySeatIdAndToday response.getBody(): {}\n\n", response.getBody());
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("findByMemberIdAndToday")
//    public void findByMemberIdAndToday() {
//        String memberId = "clickit_dev";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/findByMemberIdAndToday?memberId=" + memberId + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);
//
//        log.info("\n\nfindByMemberIdAndToday response.getBody(): {}\n\n", response.getBody());
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("findMyReservationToday")
//    public void findMyReservationToday() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/findMyReservationToday?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);
//
//        log.info("\n\nfindMyReservationToday response.getBody(): {}\n\n", response.getBody());
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("update")
//    public void update(){
//        ReservationDTO reservationDTO = ReservationDTO.builder()
//                .num(6)
//                .seatId("bad1_4")
//                .memberId("clickit_dev")
//                .timestamp(LocalDateTime.now())
//                .status("예약")
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<ReservationDTO> entity = new HttpEntity<>(reservationDTO, headers);
//
//        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:" + port + "/reservation/update", HttpMethod.PUT, entity, Object.class);
//
//        log.info("update response: {}", response);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("updateStatus")
//    public void updateStatus(){
//        ReservationDTO reservationDTO = ReservationDTO.builder()
//                .num(5)
//                .status("취소")
//                .build();
//
//        List<ReservationDTO> reservationDTOList = List.of(reservationDTO);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<List<ReservationDTO>> entity = new HttpEntity<>(reservationDTOList, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/updateStatus", HttpMethod.PUT, entity, String.class);
//
//        log.info("updateStatus response: {}", response);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//
//    @Test
//    @DisplayName("delete")
//    public void delete() {
//        int num = 6;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/reservation/delete?num=" + num, HttpMethod.DELETE, entity, String.class);
//
//        log.info("delete response: {}", response);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//    }
//}
