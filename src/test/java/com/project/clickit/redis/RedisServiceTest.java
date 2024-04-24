package com.project.clickit.redis;

import com.project.clickit.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@DisplayName("RedisService Test")
@SpringBootTest
public class RedisServiceTest {
    final String KEY = "key";
    final String VALUE = "value";
    final Duration DURATION = Duration.ofMillis(5000);

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        redisService.setData(KEY, VALUE, DURATION);
    }

    @AfterEach
    void tearDown() {
        redisService.deleteData(KEY);
    }

    @Test
    @DisplayName("save and read test")
    void saveAndReadTest() {
        // when
        String result = redisService.getData(KEY);

        // then
        log.info("result: {}", result);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(VALUE);
    }
}
