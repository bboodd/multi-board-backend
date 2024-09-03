package com.hh.multiboarduserbackend.config;

import com.hh.multiboarduserbackend.interceptor.AuthInterceptor;
import com.hh.multiboarduserbackend.interceptor.LoggerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggerInterceptor loggerInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/**");
        registry.addInterceptor(loggerInterceptor)
                .order(2)
                .excludePathPatterns("/css/**", "/images/**", "/js/**");
    }
}
