package com.chikacow.kohimana.config.filters;


import com.chikacow.kohimana.a_rate_limiting.RateLimitingService;
import com.chikacow.kohimana.common.AppConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j(topic = "REQUEST-TRACKING-FILTER")
public class RequestTrackingFilter extends OncePerRequestFilter {
    private final RateLimitingService rateLimitingService;

    public RequestTrackingFilter(@Qualifier("token-bucket") RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("start requestTrackingFilter----------------");

        boolean isAllowed = rateLimitingService.isAllowed();

        if (isAllowed) {
            log.info("request tracking is allowed");
            filterChain.doFilter(request, response);
        } else {
            log.info("denied");

            getTooManyRequestResponse(request, response);
        }

        log.info("finish requestTrackingFilter----------------");
    }


    public void getTooManyRequestResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Too many requests. Please try again later\", \"status\": 429}");
    }
}
