package com.chikacow.kohimana.config;

import com.chikacow.kohimana.service.JwtService;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//hung cac request vao ung dung, xu ly r ms chuyen sang cac api
@Component
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationContent = request.getHeader("Authorization");

        if (StringUtils.isBlank(authorizationContent) || !authorizationContent.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            return;
        }

        final String token = authorizationContent.substring("Bearer ".length());
        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetaill = userService.getUserDetailsService().loadUserByUsername(username);

            if (jwtService.isValid(token, TokenType.ACCESS_TOKEN, userDetaill)) {
                SecurityContext newContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetaill, null, userDetaill.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                newContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(newContext);

            } else {
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

}
