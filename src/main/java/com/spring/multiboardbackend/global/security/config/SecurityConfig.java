package com.spring.multiboardbackend.global.security.config;

import com.spring.multiboardbackend.global.security.jwt.JwtProperties;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    public MacAlgorithm macAlgorithm() {
        return SIG.HS256;
    }
}
