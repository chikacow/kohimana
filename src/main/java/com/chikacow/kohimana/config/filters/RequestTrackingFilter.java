package com.chikacow.kohimana.config.filters;


import com.chikacow.kohimana.a_rate_limiting.RateLimitingService;
import com.chikacow.kohimana.common.AppConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "REQUEST-TRACKING-FILTER")
public class RequestTrackingFilter extends OncePerRequestFilter {
    private  final RateLimitingService rateLimitingService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("start requestTrackingFilter----------------");

        boolean isAllowed = rateLimitingService.isAllowed();

        if (isAllowed) {
            filterChain.doFilter(request, response);
        } else {
            log.info("denied");
        }

        log.info("finish requestTrackingFilter----------------");


    }
}
