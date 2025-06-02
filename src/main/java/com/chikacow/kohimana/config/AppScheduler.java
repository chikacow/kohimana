package com.chikacow.kohimana.config;

import com.chikacow.kohimana.common.AppConstant;
import com.chikacow.kohimana.service.impl.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
@RequiredArgsConstructor
public class AppScheduler {
    private final FileService fileService;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void cleanFileDatabase() {
        log.info("Cleaning file database... found:" + Long.toString(fileService.cleanAllFiles()));
    }

    @Scheduled(fixedRate = 600)
    public void resetRateLimiting() {
        AppConstant.reset();
    }

    /**
     * clean order that is pending for more than 30 min from the created_at
     */
    //@Scheduled(fixedRate = 1000 * 60 * 60)
    public void cleanOrderDatabase() {
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
