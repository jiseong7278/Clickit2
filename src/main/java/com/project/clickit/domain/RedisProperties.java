package com.project.clickit.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
@Component
public class RedisProperties {
    private String host;
    private int port;
}
