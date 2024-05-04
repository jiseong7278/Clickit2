package com.project.clickit.integration;

import com.project.clickit.dto.LoginDTO;
import com.project.clickit.dto.MemberDTO;
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
public class MemberTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;
    private final int page = 0;
    private final int size = 10;
    private TokenDTO tokenDTO;

    @BeforeEach
    public void setUp() {
        LoginDTO loginDTO = new LoginDTO("clickit_dev", "dev1234");

        ResponseEntity<TokenDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/login/signIn", loginDTO, TokenDTO.class);

        tokenDTO = response.getBody();
        token = Objects.requireNonNull(tokenDTO).getAccessToken();
        log.info("token: {}", token);
    }

    @Test
    @DisplayName("Create")
    public void create(){
        MemberDTO memberDTO = MemberDTO.builder()
                .id("create_test3")
                .password("test1234")
                .name("테스트")
                .phone("010-1234-5678")
                .email("test@test.com")
                .studentNum("20210000")
                .type("CLICKIT_STUDENT")
                .dormitoryId("dor_1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberDTO> entity = new HttpEntity<>(memberDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/member/create", entity, String.class);

        log.info("create response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Create List")
    public void createList(){
        MemberDTO memberDTO1 = MemberDTO.builder()
                .id("create_test4")
                .password("test1234")
                .name("테스트")
                .phone("010-1234-5678")
                .email("test4@test.com")
                .studentNum("20240001")
                .type("CLICKIT_STUDENT")
                .dormitoryId("dor_1")
                .build();

        MemberDTO memberDTO2 = MemberDTO.builder()
                .id("create_test5")
                .password("test1234")
                .name("테스트")
                .phone("010-1234-5678")
                .email("test5@test.com")
                .studentNum("20240002")
                .type("CLICKIT_STUDENT")
                .dormitoryId("dor_1")
                .build();

        List<MemberDTO> memberDTOList = List.of(memberDTO1, memberDTO2);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<MemberDTO>> entity = new HttpEntity<>(memberDTOList, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/member/createList", entity, String.class);

        log.info("createList response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getAll")
    public void getAll(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/getAll?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("getAll response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("getAllStudent")
    public void getAllStudent(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/getAllStudent?page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("getAllStudent response: {}", response);
        log.info("getAllStudent response body: {}", response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByMemberId")
    public void findByMemberId(){
        String id = "create_test3";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/findByMemberId?id=" + id, HttpMethod.GET, entity, String.class);

        log.info("findByMemberId response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByMemberName")
    public void findByMemberName(){
        String name = "테스트";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/findByMemberName?name=" + name + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByMemberName response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("findByDormitoryId")
    public void findByDormitoryId(){
        String dormitoryId = "dor_1";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/findByDormitoryId?dormitoryId=" + dormitoryId + "&page=" + page + "&size=" + size, HttpMethod.GET, entity, String.class);

        log.info("findByDormitoryId response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updatePhone")
    public void updatePhone(){
        String phone = "010-9999-9999";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/updatePhone?phone=" + phone, HttpMethod.PUT, entity, String.class);

        log.info("updatePhone response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateEmail")
    public void updateEmail() {
        String email = "jisung3344@gmail.com";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/updateEmail?email=" + email, HttpMethod.PUT, entity, String.class);

        log.info("updateEmail response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updatePassword")
    public void updatePassword() {
        String password = "dev1234";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/updatePassword?password=" + password, HttpMethod.PUT, entity, String.class);

        log.info("updatePassword response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateMemberForStaff")
    public void updateMemberForStaff() {
        MemberDTO memberDTO = MemberDTO.builder()
                .id("create_test3")
                .password("test1234")
                .name("테스트")
                .phone("010-8888-8888")
                .email("updateForStaff")
                .studentNum("99999999")
                .type("CLICKIT_STUDENT")
                .dormitoryId("dor_1")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MemberDTO> entity = new HttpEntity<>(memberDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/updateMemberForStaff", HttpMethod.PUT, entity, String.class);

        log.info("updateMemberForStaff response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("updateRefreshToken")
    public void updateRefreshToken() {
        String id = "create_test3";
        String refreshToken = tokenDTO.getRefreshToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/updateRefreshToken?id=" + id + "&refreshToken=" + refreshToken, HttpMethod.PUT, entity, String.class);

        log.info("updateRefreshToken response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete")
    public void delete(){
        String id = "create_test3";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/delete?id=" + id, HttpMethod.DELETE, entity, String.class);

        log.info("delete response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("deleteAllStudent")
    public void deleteAllStudent(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/member/deleteAllStudent", HttpMethod.DELETE, entity, String.class);

        log.info("deleteAllStudent response: {}", response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
