package com.chikacow.kohimana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

//@Configuration
//@EnableJpaAuditing
public class AuditingConfigurer {

    //@Bean
//    public AuditorAware<String> auditorProvider() {
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .filter(auth -> auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken))
//                .map(Principal::getName);
//    }
}

