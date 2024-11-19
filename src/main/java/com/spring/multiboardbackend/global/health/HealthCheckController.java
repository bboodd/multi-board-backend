package com.spring.multiboardbackend.global.health;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.activation.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    private final HikariDataSource dataSource;

    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthcheck() {
        boolean isDatabaseUp = checkDatabase().equals("UP");

        if (isDatabaseUp) {
            return ResponseEntity.ok(Map.of("status", "ok"));
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("status", "error"));
    }

    private String checkDatabase() {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT 1")) {
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? "UP" : "DOWN";
                }
            }
        } catch (Exception e) {
            log.info("Database health check failed", e);
            return "DOWN";
        }

    }
}
