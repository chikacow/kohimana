package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.service.HealthMonitoringService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthMonitoring {
    @Builder
    @Getter
    @Setter
    public static class HealthAspect {
        private String javaVersion;
        private String checkMinioConnection;
        private String checkDatabaseConnection;
        private String checkRedisConnection;
        private String checkRateLimiting;

    }

    private final HealthMonitoringService healthMonitoringService;

    @GetMapping
    public ResponseData<?> health() {
        HealthAspect healthAspect = HealthAspect.builder()
                .javaVersion(System.getProperty("java.version"))
                .checkDatabaseConnection(healthMonitoringService.isMySqlDatabaseUp() ? "Database connected" : "Database not connected")
                .checkRedisConnection(healthMonitoringService.isRedisUp() ? "Redis connected" : "Redis not connected")
                .checkMinioConnection(healthMonitoringService.isMinioUp() ? "Minio connected" : "Minio not connected")
                .checkRateLimiting(healthMonitoringService.checkRateLimitStatus())
                .build();

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Check health completed")
                .data(healthAspect)
                .build();



    }
}
