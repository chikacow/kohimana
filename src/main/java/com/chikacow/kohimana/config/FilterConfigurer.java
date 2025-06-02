package com.chikacow.kohimana.config;

import com.chikacow.kohimana.config.filters.RequestTrackingFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfigurer {
    private final RequestTrackingFilter requestTrackingFilter;

//    @Bean
//    public RequestTrackingFilter requestTrackingFilter() {
//        return new RequestTrackingFilter();
//    }

    @Bean
    public FilterRegistrationBean<Filter> customPreSecurityFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestTrackingFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(-101);
        return registrationBean;
    }
}