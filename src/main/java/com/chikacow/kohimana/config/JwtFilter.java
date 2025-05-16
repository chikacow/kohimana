package com.chikacow.kohimana.config;

import com.chikacow.kohimana.service.JwtService;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.TokenType;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("start prefilter----------------");
        final String authorizationContent = request.getHeader("Authorization");
        System.out.println(request.getRequestURL().toString());
        if (StringUtils.isBlank(authorizationContent) || !authorizationContent.startsWith("Bearer ")) {
            System.out.println("jump here");

//            if (request.getRequestURL().toString().contains("/api/v1/auth/access-token")) {
//                SecurityContextHolder.getContext().setAuthentication();
//                filterChain.doFilter(request, response);
//            } else {
//                filterChain.doFilter(request, response);
//            }

            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("continue-------------------");
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

        System.out.println("finish prefilter----------------");
        filterChain.doFilter(request, response);
    }

}
