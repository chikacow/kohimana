package com.chikacow.kohimana.config;

import com.chikacow.kohimana.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppScheduler {
    private final FileService fileService;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void cleanFileDatabase() {
        log.info("Cleaning file database... found:" + Long.toString(fileService.cleanAllFiles()));
    }

//    @Scheduled(fixedRate = 5000, )
//    public void runEveryFiveSeconds() {
//        System.out.println("Running task at " + LocalDateTime.now());
//    }
//
//    @Scheduled(cron = "0 0 8 * * ?") // chạy lúc 8:00 sáng mỗi ngày
//    public void sendDailyEmail() {
//        System.out.println("Sending daily email...");
//    }
}
